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

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.AllocationCategory;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;

/**
 * Service interface for {@linkplain Allocation}
 * 
 * @author subinsugunan
 * 
 */
public interface AllocationService extends DomainService< Allocation >
{
    /**
     * This duration value will be used in reacquire calls.
     */
    public int REACQUIRE_DURATION = -1;

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
     * @throws {@linkplain AllocationInstantiationException}
     */
    Allocation createByComponentId( String componentId ) throws AllocationInstantiationException;

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
     * @throws {@linkplain AllocationInstantiationException}
     */
    Allocation createByComponentId( String componentId, Integer duration ) throws AllocationInstantiationException;

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
    Allocation createByComponentId( String componentId, Integer duration, String authToken )
            throws AllocationInstantiationException;

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
    Allocation createByComponentId( String componentId, Integer duration, String authToken,
            AllocationCategory allocationCategory ) throws AllocationInstantiationException;

    /**
     * Try to reacquire a device for use with a specified componentId If the
     * device is currently not allocated by the same user,this call will fail.
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    Allocation reacquireByComponentId( String componentId ) throws AllocationInstantiationException;

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
    Allocation reacquireByComponentId( String componentId, String authToken, AllocationCategory allocationCategory )
            throws AllocationInstantiationException;

    /**
     * Finds an allocation based on the macId specified.
     * 
     * @param componentId
     *            - The id of the {@linkplain SettopDesc}
     * @return The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    Allocation findByComponentId( String componentId ) throws AllocationNotFoundException;

    /**
     * Check to see if the {@linkplain Allocation} is valid.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @return True if the {@linkplain Allocation} is valid, False otherwise
     */
    Boolean verify( String allocationId );

    /**
     * Check to see if the {@linkplain Allocation} is valid.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param authToken
     * @return True if the {@linkplain Allocation} is valid, False otherwise
     */
    Boolean verify( String allocationId, String authToken );

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
    Allocation update( String allocationId ) throws AllocationNotFoundException;

    /**
     * Update the specified allocation by extending the end time from now by the
     * specified number of minutes.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param minutes
     *            - This will be added to the current system time to to set the
     *            new end date for the {@linkplain Allocation}
     * @return The updated {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    Allocation update( String allocationId, Integer minutes ) throws AllocationNotFoundException;

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
    Allocation update( String allocationId, Integer minutes, String authToken ) throws AllocationNotFoundException;

    /**
     * Release the allocation based on the allocationId specified.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    void release( String allocationId ) throws AllocationNotFoundException;

    /**
     * Release the allocation based on the allocationId specified and authToken.
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @param authToken
     * @throws {@linkplain AllocationInstantiationException}
     */
    void release( String allocationId, String authToken ) throws AllocationNotFoundException;

    /**
     * Release the allocation.
     * 
     * @param allocation
     *            - The {@linkplain Allocation}
     * @throws {@linkplain AllocationInstantiationException}
     */
    void release( Allocation allocation ) throws AllocationNotFoundException;

    /**
     * Release this allocation based on the macId specified.
     * 
     * @param componentId
     *            - The id of the {@linkplain SettopDesc}
     * @throws {@linkplain AllocationInstantiationException}
     */
    void releaseByComponentId( String componentId ) throws AllocationNotFoundException;

}
