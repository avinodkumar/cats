/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.domain.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.springframework.http.HttpStatus;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.AllocationCategory;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.SameUserAllocationException;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Implementation of {@link AllocationService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class AllocationServiceImpl extends DomainServiceImpl< Allocation > implements AllocationService
{
    /**
     * Try to allocate a device for use with a specified componentId The
     * duration value,authtoken,allocation category is taken from the cats
     * properties. The value can be set by setting the value cats.lock.duration
     * in cats.props If the device is currently allocated the allocation should
     * fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @return The {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation createByComponentId( String componentId ) throws AllocationInstantiationException
    {

        return createByComponentId( componentId, properties.getAllocationDuration(), properties.getAuthToken(),
                properties.getAllocationCategory() );

    }

    /**
     * Try to allocate a device for use with a specified componentId and for a
     * given duration. If the device is currently allocated the allocation
     * should fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @param duration
     *            - {@linkplain Allocation} duration
     * @return The {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation createByComponentId( String componentId, Integer duration )
            throws AllocationInstantiationException
    {
        return createByComponentId( componentId, duration, properties.getAuthToken(),
                properties.getAllocationCategory() );
    }

    /**
     * Try to allocate a device for use with a specified componentId and for a
     * given duration with auth token. If the device is currently allocated the
     * allocation should fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @param duration
     *            - {@linkplain Allocation} duration
     * @param authToken
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation createByComponentId( String componentId, Integer duration, String authToken )
            throws AllocationInstantiationException
    {
        return createByComponentId( componentId, duration, authToken, properties.getAllocationCategory() );
    }

    /**
     * Try to allocate a device for use with a specified componentId and for a
     * given duration with auth token and allocationCategory. If the device is
     * currently allocated the allocation should fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @param duration
     *            - {@linkplain Allocation} duration
     * @param authToken
     * @param allocationCategory
     *            {@linkplain AllocationCategory}
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation createByComponentId( String componentId, Integer duration, String authToken,
            AllocationCategory allocationCategory ) throws AllocationInstantiationException
    {

        return createByComponentId( componentId, duration, authToken, allocationCategory, false );

    }

    private Allocation createByComponentId( String componentId, Integer duration, String authToken,
            AllocationCategory allocationCategory, boolean reacquireFlag ) throws AllocationInstantiationException
    {
        AssertUtil.isNullOrEmpty( componentId, "Cannot create Allocation. componentId cannot be null or empty" );
        String requestUrl = getBaseUrl( ALLOCATION )
                + COMPONENT
                + BACK_SLASH
                + ALLOCATE
                + CommonUtil.getNameValuePair( getParamMapByIdDurationAndToken( componentId, duration, authToken,
                        allocationCategory, reacquireFlag ) );
        Allocation allocation = null;
        try
        {
            allocation = postForDomainObject( requestUrl, Allocation.class );
        }
        catch ( DomainServiceException e )
        {
            Integer errorCode = e.getErrorCode();
            HttpStatus statusCode = HttpStatus.valueOf( errorCode );
            // NOT acceptable is thrown in the case where the same user has
            // acquired the lock
            if ( statusCode == HttpStatus.NOT_ACCEPTABLE )
            {
                throw new SameUserAllocationException( e.getMessage() );
            }
            else
            {
                throw new AllocationInstantiationException( e.getMessage() );
            }

        }

        return allocation;
    }

    /**
     * Try to reacquire a device for use with a specified componentId If the
     * device is currently not allocated by the same user,this call will fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation reacquireByComponentId( String componentId ) throws AllocationInstantiationException
    {
        return createByComponentId( componentId, REACQUIRE_DURATION, properties.getAuthToken(),
                properties.getAllocationCategory(), true );
    }

    /**
     * Try to reacquire a device for use with a specified componentId If the
     * device is currently not allocated by the same user,this call will fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @param authToken
     * @param allocationCategory
     *            {@linkplain AllocationCategory}
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation reacquireByComponentId( String componentId, String authToken,
            AllocationCategory allocationCategory ) throws AllocationInstantiationException
    {
        return createByComponentId( componentId, REACQUIRE_DURATION, authToken, allocationCategory, true );
    }

    /**
     * Finds all active allocations (Not expired and status is 'Active')
     * 
     * @return A {@linkplain List} of {@linkplain Allocation}
     */
    @Override
    public List< Allocation > findActive()
    {
        return findActive( 0, 0 );
    }

    /**
     * Finds all active allocations (Not expired and status is 'Active')
     * 
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Allocation}
     */
    @Override
    public List< Allocation > findActive( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + ALLOCATIONS ) + ACTIVE + BACK_SLASH + offset + BACK_SLASH
                + count;

        List< Allocation > activeAllocations = new ArrayList< Allocation >();
        try
        {
            activeAllocations = getResponseAsDomainList( requestUrl, Allocation.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve active allocations list. " + e.getMessage() );
        }

        return activeAllocations;
    }

    /**
     * Finds an allocation based on the macId specified.
     * 
     * @param componentId
     *            - The id of the {@linkplain SettopDesc}
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation findByComponentId( String componentId ) throws AllocationNotFoundException
    {
        AssertUtil.isNullOrEmpty( componentId, "Cannot search Allocation. componentId cannot be null" );
        String requestUrl = getBaseUrl( ALLOCATION ) + COMPONENT + BACK_SLASH + SHOW
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( componentId ) );

        Allocation allocation = null;
        try
        {
            allocation = getResponseAsDomain( requestUrl, Allocation.class );
        }
        catch ( DomainServiceException e )
        {
            throw new AllocationNotFoundException( e.getMessage() );
        }

        return allocation;
    }

    /**
     * Check to see if the {@linkplain Allocation} is valid.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @return True if the {@linkplain Allocation} is valid, False otherwise
     */
    @Override
    public Boolean verify( String allocationId )
    {
        return verify( allocationId, properties.getAuthToken() );
    }

    /**
     * Check to see if the {@linkplain Allocation} is valid.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param authToken
     * @return True if the {@linkplain Allocation} is valid, False otherwise
     */
    @Override
    public Boolean verify( String allocationId, String authToken )
    {
        AssertUtil.isNullOrEmpty( allocationId, "Cannot verify Allocation. allocationId cannot be null" );
        String requestUrl = getBaseUrl( ALLOCATION ) + VERIFY
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( allocationId, authToken ) );

        Boolean status = false;
        try
        {
            status = getResponseAsBoolean( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.warn( "Verify allocation thrown exception: " + e.getMessage() );
        }
        return status;
    }

    /**
     * Update the specified allocation by extending the end time from now by the
     * specified number of minutes. The extension duration is taken from
     * cats.props.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @return The updated {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation update( String allocationId ) throws AllocationNotFoundException
    {
        return update( allocationId, properties.getAllocationDuration(), properties.getAuthToken() );
    }

    /**
     * Update the specified allocation by extending the end time from now by the
     * specified number of minutes.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param duration
     *            - This will be added to the current system time to to set the
     *            new end date for the {@linkplain Allocation}
     * @return The updated {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation update( String allocationId, Integer minutes ) throws AllocationNotFoundException
    {
        return update( allocationId, minutes, properties.getAuthToken() );
    }

    /**
     * Update the specified allocation by extending the end time from now by the
     * specified number of minutes and authToken.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param minutes
     *            - This will be added to the current system time to to set the
     *            new end date for the {@linkplain Allocation}
     * @param authToken
     * @return The updated {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public Allocation update( String allocationId, Integer minutes, String authToken )
            throws AllocationNotFoundException
    {
        AssertUtil.isNullOrEmpty( allocationId, "Cannot update Allocation. allocationId cannot be null" );
        String requestUrl = getBaseUrl( ALLOCATION ) + UPDATE
                + CommonUtil.getNameValuePair( getParamMapByIdDurationAndToken( allocationId, minutes, authToken ) );

        Allocation allocation = null;
        try
        {
            allocation = postForDomainObject( requestUrl, Allocation.class );
        }
        catch ( DomainServiceException e )
        {
            throw new AllocationNotFoundException( e.getMessage() );
        }

        return allocation;
    }

    /**
     * Release the allocation.
     * 
     * @param allocation
     *            - The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public void release( Allocation allocation ) throws AllocationNotFoundException
    {
        AssertUtil.isNull( allocation, "Cannot release Allocation. allocation cannot be null" );
        AssertUtil.isNullOrEmpty( allocation.getId(), "Cannot release Allocation. allocationId cannot be null" );
        release( allocation.getId() );
    }

    /**
     * Release the allocation based on the allocationId specified.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public void release( String allocationId ) throws AllocationNotFoundException
    {
        release( allocationId, properties.getAuthToken() );
    }

    /**
     * Release the allocation based on the allocationId specified and authToken.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param authToken
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public void release( String allocationId, String authToken ) throws AllocationNotFoundException
    {
        AssertUtil.isNullOrEmpty( allocationId, "Cannot release Allocation. allocationId cannot be null" );
        String requestUrl = getBaseUrl( ALLOCATION ) + RELEASE
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( allocationId, authToken ) );

        releaseAllocation( requestUrl );
    }

    /**
     * Release this allocation based on the macId specified.
     * 
     * @param componentId
     *            - The id of the {@linkplain SettopDesc}
     * @throws {@linkplain AllocationInstantiationException}
     */
    @Override
    public void releaseByComponentId( String componentId ) throws AllocationNotFoundException
    {
        AssertUtil.isNullOrEmpty( componentId, "Cannot release Allocation. componentId cannot be null" );
        String requestUrl = getBaseUrl( ALLOCATION ) + COMPONENT + BACK_SLASH + RELEASE
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( componentId ) );

        releaseAllocation( requestUrl );
    }

    private void releaseAllocation( String requestUrl ) throws AllocationNotFoundException
    {
        try
        {
            put( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            throw new AllocationNotFoundException( e.getMessage() );
        }
    }

    /**
     * Finds the count of total active allocations.
     * 
     * @return The count of active {@linkplain Allocation}
     */
    @Override
    public Integer countActive()
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + ALLOCATIONS ) + ACTIVE + BACK_SLASH + COUNT;

        Integer count = 0;

        try
        {
            count = getResponseAsNumber( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve active allocations count. " + e.getMessage() );
        }

        return count;
    }

}
