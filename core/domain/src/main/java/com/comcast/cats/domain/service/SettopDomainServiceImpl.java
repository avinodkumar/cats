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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.http.HttpMethod;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Implementation of {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class SettopDomainServiceImpl extends DomainServiceImpl< SettopDesc > implements SettopDomainService
{
    private static final String  MAC_ID        = "mac";

    private static final Boolean GET_DEEP_COPY = false;

    /**
     * Return {@link SettopDesc} for a given macId.
     * 
     * @param macId
     *            - The MAC address of the settop box
     * @return The {@link SettopDesc}
     * @throws SettopNotFoundException
     */
    @Override
    public SettopDesc findByMacId( String macId ) throws SettopNotFoundException
    {
        return findByMacId( macId, GET_DEEP_COPY );
    }

    /**
     * Return {@link SettopDesc} for a given macId.
     * 
     * @param macId
     *            - The MAC address of the settop box
     * @return The {@link SettopDesc}
     * @throws SettopNotFoundException
     */
    @Override
    public SettopDesc findByMacId( String macId, Boolean isShallow ) throws SettopNotFoundException
    {
        AssertUtil.isValidMacId( macId, "Invalid macId" );
        Map< String, String > params = new HashMap< String, String >();
        params.put( MAC_ID, macId );
        params.put( SHALLOW, Boolean.toString( isShallow ) );
        String requestUrl = properties.getConfigServerUrl() + SETTOP + BACK_SLASH + SHOW
                + CommonUtil.getNameValuePair( params );

        SettopDesc settopDesc = null;
        try
        {
            settopDesc = getResponseAsDomain( requestUrl, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            throw new SettopNotFoundException( e.getMessage() );
        }
        return settopDesc;
    }

    @Override
    public List< SettopDesc > findByMacIdList( List< String > macIds )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Return all allocated {@link SettopReservationDesc} for a given user.
     * 
     * @return List of {@linkplain SettopReservationDesc}
     */
    @Override
    public List< SettopReservationDesc > findAllAllocated()
    {

        return findAllAllocated( GET_DEEP_COPY );
    }

    /**
     * Return all allocated {@link SettopReservationDesc} for a given user.
     * 
     * @return List of {@linkplain SettopReservationDesc}
     */
    @Override
    public List< SettopReservationDesc > findAllAllocated( Boolean isShallow )
    {
        Map< String, String > params = getParamMapByToken();
        params.put( SHALLOW, Boolean.toString( isShallow ) );
        String requestUrl = getBaseUrl( SETTOP ) + ALLOCATED + CommonUtil.getNameValuePair( params );

        List< SettopReservationDesc > settops = new ArrayList< SettopReservationDesc >();

        try
        {
            settops = getResponseAsSettopLockDescList( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve List< SettopLockDesc >. " + e.getMessage() );
        }
        return settops;
    }

    /**
     * Return all available {@link SettopReservationDesc} for a given user. This
     * method will return a deep copied version of SettopReservationDesc
     * 
     * @return List of {@linkplain SettopReservationDesc}
     */
    @Override
    public List< SettopReservationDesc > findAllAvailable()
    {
        return findAllAvailable( GET_DEEP_COPY );
    }

    /**
     * Return all available {@link SettopReservationDesc} for a given user.
     * 
     * @param isShallow
     *            -true:returns a shallow representation of
     *            SettopReservationDesc false:returns the full represetnation.
     * @return List of {@linkplain SettopReservationDesc}
     */
    @Override
    public List< SettopReservationDesc > findAllAvailable( Boolean isShallow )
    {
        Map< String, String > params = getParamMapByToken();
        params.put( SHALLOW, Boolean.toString( isShallow ) );
        String requestUrl = getBaseUrl( SETTOP ) + AVAILABLE + CommonUtil.getNameValuePair( params );

        List< SettopReservationDesc > settops = new ArrayList< SettopReservationDesc >();

        try
        {
            settops = getResponseAsSettopLockDescList( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve List< SettopLockDesc >. " + e.getMessage() );
        }
        return settops;
    }

    /**
     * Return all {@link SettopDesc}, which matches the criteria.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria )
    {
        return findAllByCriteria( criteria, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByCriteria(Map, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Boolean isShallow )
    {
        return findAllByCriteria( criteria, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc}, which matches the criteria, 'up to' the
     * count specified, as per the availability.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @param offset
     *            - The starting point
     * @param count
     *            - The max limit
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count )
    {
        return findAllByCriteria( criteria, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByCriteria(Map, Integer, Integer,
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow )
    {

        AssertUtil.isNullOrEmptyMap( criteria, "Invalid or Empty criteria" );
        Map< String, String > params = getParamMapByCriteria( criteria, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + CRITERIA + BACK_SLASH + LIST + CommonUtil.getNameValuePair( params );

        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc}, which matches the specified
     * property-values combination.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @return A {@linkplain List} deep copy of {@link SettopDesc} which matches
     *         the specified property-values combination.
     */
    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values )
    {
        return findAllByProperty( property, values, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByProperty(String, String[],Boolean)
     */
    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Boolean isShallow )
    {
        return findAllByProperty( property, values, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc}, which matches the specified
     * property-values combination,'up to' the count specified, as per the
     * availability.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @param offset
     *            - The starting point
     * @param count
     *            - The max limit
     * @return A {@linkplain List} of deep copied {@link SettopDesc} which
     *         matches the specified property-values combination.
     */
    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count )
    {
        return findAllByProperty( property, values, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByProperty(String, String[], Integer,
     *      Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( property, "Invalid or Empty property" );
        AssertUtil.isNullOrEmpty( values, "Invalid or Empty values" );
        Map< String, String > params = getParamMapByPropertyValues( property, values, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + PROPERTY + BACK_SLASH + LIST + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId )
    {
        return findAllByReservationId( reservationId, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByReservationId(String, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Boolean isShallow )
    {
        return findAllByReservationId( reservationId, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count )
    {
        return findAllByReservationId( reservationId, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByReservationId(String, Integer, Integer,
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( reservationId, "Invalid or Empty reservationId" );
        Map< String, String > params = getParamMapById( reservationId, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RESERVATION + BACK_SLASH + LIST
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAllByRackId( String rackId )
    {
        return findAllByRackId( rackId, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByRackId(String, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByRackId( String rackId, Boolean isShallow )
    {
        return findAllByRackId( rackId, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count )
    {
        return findAllByRackId( rackId, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByRackId(String, Integer, Integer,
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count, Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( rackId, "Invalid or Empty rackId" );
        Map< String, String > params = getParamMapById( rackId, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RACK + BACK_SLASH + LIST + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId )
    {
        return findAllBySettopGroupId( settopGroupId, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Boolean isShallow )
    {
        return findAllBySettopGroupId( settopGroupId, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup} The
     * SettopDesc object returned will be a deep copied version.
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count )
    {
        return findAllBySettopGroupId( settopGroupId, offset, count, GET_DEEP_COPY );
    }

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            - true:shallow copy of {@linkplain SettopDesc} object
     *            false:deep copy of {@linkplain SettopDesc} object
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( settopGroupId, "Invalid or Empty settopGroupId" );
        Map< String, String > params = getParamMapById( settopGroupId, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + SETTOP_GROUP + BACK_SLASH + LIST
                + CommonUtil.getNameValuePair( params );

        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation )
    {
        return findAllByReservation( reservation, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByReservation(Reservation, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Boolean isShallow )
    {
        return findAllByReservation( reservation, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count )
    {
        return findAllByReservation( reservation, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByReservation(Reservation, Integer,
     *      Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNull( reservation, "Invalid or reservation" );
        String reservationName = reservation.getName();
        AssertUtil.isNullOrEmpty( reservationName, "Invalid or reservation name" );
        Map< String, String > params = getParamMapByName( reservationName, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RESERVATION + BACK_SLASH + LIST
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAllByRack( Rack rack )
    {
        return findAllByRack( rack, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByRack(Rack, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByRack( Rack rack, Boolean isShallow )
    {
        return findAllByRack( rack, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count )
    {
        return findAllByRack( rack, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllByRack(Rack, Integer, Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count, Boolean isShallow )
    {
        AssertUtil.isNull( rack, "Invalid or rack" );
        String rackName = rack.getName();
        AssertUtil.isNullOrEmpty( rackName, "Invalid or rack name" );
        Map< String, String > params = getParamMapByName( rackName, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RACK + BACK_SLASH + LIST + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all {@link SettopDesc} for a given {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     * 
     * @throws SettopNotFoundException
     */
    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup ) throws SettopNotFoundException
    {
        return findAllBySettopGroup( settopGroup, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllBySettopGroup(SettopGroup, Boolean)
     */
    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
            throws SettopNotFoundException
    {
        return findAllBySettopGroup( settopGroup, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc} for a given {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     * @throws SettopNotFoundException
     */
    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count )
            throws SettopNotFoundException
    {
        return findAllBySettopGroup( settopGroup, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAllBySettopGroup(SettopGroup, Integer,
     *      Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow ) throws SettopNotFoundException
    {
        AssertUtil.isNull( settopGroup, "Invalid or settopGroup" );
        String settopGroupName = settopGroup.getName();
        AssertUtil.isNullOrEmpty( settopGroupName, "Invalid or settopGroup name" );
        Map< String, String > params = getParamMapByName( settopGroupName, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + SETTOP_GROUP + BACK_SLASH + LIST
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc}, which matches the criteria.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria )
    {
        return findAvailableByCriteria( criteria, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByCriteria(Map, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Boolean isShallow )
    {
        return findAvailableByCriteria( criteria, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc}, which matches the criteria, 'up
     * to' the count specified, as per the availability.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @param offset
     *            - The starting point
     * @param count
     *            - The max limit
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count )
    {
        return findAvailableByCriteria( criteria, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByCriteria(Map, Integer, Integer,
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmptyMap( criteria, "Invalid or Empty criteria" );
        Map< String, String > params = getParamMapByCriteriaAndToken( criteria, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + CRITERIA + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available{@link SettopDesc}, which matches the specified
     * property-values combination.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values )
    {
        return findAvailableByProperty( property, values, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByProperty(String, String[],
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Boolean isShallow )
    {
        return findAvailableByProperty( property, values, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all {@link SettopDesc}, which matches the specified
     * property-values combination,'up to' the count specified, as per the
     * availability.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @param offset
     *            - The starting point
     * @param count
     *            - The max limit
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count )
    {
        return findAvailableByProperty( property, values, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByProperty(String, String[],
     *      Integer, Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( property, "Invalid or Empty property" );
        AssertUtil.isNullOrEmpty( values, "Invalid or Empty values" );
        Map< String, String > params = getParamMapByPropertyValuesAndToken( property, values, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + PROPERTY + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId )
    {
        return findAvailableByReservationId( reservationId, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByReservationId(String, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Boolean isShallow )
    {
        return findAvailableByReservationId( reservationId, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count )
    {
        return findAvailableByReservationId( reservationId, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByReservationId(String, Integer,
     *      Integer, Boolean)
     */

    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( reservationId, "Invalid or Empty reservationId" );

        Map< String, String > params = getParamMapByIdAndToken( reservationId, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RESERVATION + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );

        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId )
    {
        return findAvailableByRackId( rackId, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByRackId(String, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Boolean isShallow )
    {
        return findAvailableByRackId( rackId, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count )
    {
        return findAvailableByRackId( rackId, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByRackId(String, Integer, Integer,
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count, Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( rackId, "Invalid or Empty rackId" );
        Map< String, String > params = getParamMapByIdAndToken( rackId, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RACK + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId )
    {
        return findAvailableBySettopGroupId( settopGroupId, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableBySettopGroupId(String, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Boolean isShallow )
    {
        return findAvailableBySettopGroupId( settopGroupId, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count )
    {
        return findAvailableBySettopGroupId( settopGroupId, offset, count, GET_DEEP_COPY );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNullOrEmpty( settopGroupId, "Invalid or Empty settopGroupId" );
        Map< String, String > params = getParamMapByIdAndToken( settopGroupId, offset, count, isShallow );

        String requestUrl = getBaseUrl( SETTOP ) + SETTOP_GROUP + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation )
    {
        return findAvailableByReservation( reservation, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByReservation(Reservation, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Boolean isShallow )
    {
        return findAvailableByReservation( reservation, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count )
    {
        return findAvailableByReservation( reservation, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByReservation(Reservation, Integer,
     *      Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNull( reservation, "Invalid or reservation" );
        String reservationName = reservation.getName();
        AssertUtil.isNullOrEmpty( reservationName, "Invalid or reservation name" );
        Map< String, String > params = getParamMapByNameAndToken( reservationName, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RESERVATION + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack )
    {
        return findAvailableByRack( rack, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByRack(Rack, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Boolean isShallow )
    {
        return findAvailableByRack( rack, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count )
    {
        return findAvailableByRack( rack, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableByRack(Rack, Integer, Integer,
     *      Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count, Boolean isShallow )
    {
        AssertUtil.isNull( rack, "Invalid or rack" );
        String rackName = rack.getName();
        AssertUtil.isNullOrEmpty( rackName, "Invalid or rack name" );
        Map< String, String > params = getParamMapByNameAndToken( rackName, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + RACK + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@link SettopDesc}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup )
    {
        return findAvailableBySettopGroup( settopGroup, DEFAULT_OFFSET, DEFAULT_COUNT, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableBySettopGroup(SettopGroup, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
    {
        return findAvailableBySettopGroup( settopGroup, DEFAULT_OFFSET, DEFAULT_COUNT, isShallow );
    }

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@link SettopDesc}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count )
    {
        return findAvailableBySettopGroup( settopGroup, offset, count, GET_DEEP_COPY );
    }

    /**
     * @see SettopDomainService#findAvailableBySettopGroup(SettopGroup, Integer,
     *      Integer, Boolean)
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow )
    {
        AssertUtil.isNull( settopGroup, "Invalid or settopGroup" );
        String settopGroupName = settopGroup.getName();
        AssertUtil.isNullOrEmpty( settopGroupName, "Invalid or settopGroup name" );
        Map< String, String > params = getParamMapByNameAndToken( settopGroupName, offset, count, isShallow );
        String requestUrl = getBaseUrl( SETTOP ) + SETTOP_GROUP + BACK_SLASH + LIST_AVAILABLE
                + CommonUtil.getNameValuePair( params );
        return getResponseAsDomainList( requestUrl );
    }

    /**
     * Count all {@link SettopDesc} of a {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return The count
     */
    @Override
    public Integer countAllByReservationId( String reservationId )
    {
        AssertUtil.isNullOrEmpty( reservationId, "Invalid or Empty reservationId" );

        String requestUrl = getBaseUrl( SETTOP ) + RESERVATION + BACK_SLASH + COUNT
                + CommonUtil.getNameValuePair( getParamMapById( reservationId, DEFAULT_OFFSET, DEFAULT_COUNT ) );

        return getResponseAsNumber( requestUrl );
    }

    /**
     * Count all {@link SettopDesc} of a {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return The count
     */
    @Override
    public Integer countAllBySettopGroupId( String settopGroupId )
    {
        AssertUtil.isNullOrEmpty( settopGroupId, "Invalid or Empty settopGroupId" );

        String requestUrl = getBaseUrl( SETTOP ) + SETTOP_GROUP + BACK_SLASH + COUNT
                + CommonUtil.getNameValuePair( getParamMapById( settopGroupId, DEFAULT_OFFSET, DEFAULT_COUNT ) );

        return getResponseAsNumber( requestUrl );
    }

    /**
     * Count all {@link SettopDesc} of a {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return The count
     */
    @Override
    public Integer countAllByRackId( String rackId )
    {
        AssertUtil.isNullOrEmpty( rackId, "Invalid or Empty rackId" );

        String requestUrl = getBaseUrl( SETTOP ) + RACK + BACK_SLASH + COUNT
                + CommonUtil.getNameValuePair( getParamMapById( rackId, DEFAULT_OFFSET, DEFAULT_COUNT ) );

        return getResponseAsNumber( requestUrl );
    }

    /**
     * Count all available {@link SettopDesc} of a {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return The count
     */
    @Override
    public Integer countAvailableByReservationId( String reservationId )
    {
        AssertUtil.isNullOrEmpty( reservationId, "Invalid or Empty reservationId" );

        String requestUrl = getBaseUrl( SETTOP ) + RESERVATION + BACK_SLASH + COUNT_AVAILABLE
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( reservationId, DEFAULT_OFFSET, DEFAULT_COUNT ) );

        return getResponseAsNumber( requestUrl );
    }

    /**
     * Count all available {@link SettopDesc} of a {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return The count
     */
    @Override
    public Integer countAvailableBySettopGroupId( String settopGroupId )
    {
        AssertUtil.isNullOrEmpty( settopGroupId, "Invalid or Empty settopGroupId" );

        String requestUrl = getBaseUrl( SETTOP ) + SETTOP_GROUP + BACK_SLASH + COUNT_AVAILABLE
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( settopGroupId, DEFAULT_OFFSET, DEFAULT_COUNT ) );

        return getResponseAsNumber( requestUrl );
    }

    /**
     * Count all available {@link SettopDesc} of a {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return The count
     */
    @Override
    public Integer countAvailableByRackId( String rackId )
    {
        AssertUtil.isNullOrEmpty( rackId, "Invalid or Empty rackId" );

        String requestUrl = getBaseUrl( SETTOP ) + RACK + BACK_SLASH + COUNT_AVAILABLE
                + CommonUtil.getNameValuePair( getParamMapByIdAndToken( rackId, DEFAULT_OFFSET, DEFAULT_COUNT ) );

        return getResponseAsNumber( requestUrl );
    }

    /**
     * To get response as number
     * 
     * @param requestUrl
     * @return Integer
     */
    @Override
    protected Integer getResponseAsNumber( String requestUrl )
    {
        Integer count = 0;
        try
        {
            count = super.getResponseAsNumber( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve count. " + e.getMessage() );
        }

        return count;
    }

    private List< SettopDesc > getResponseAsDomainList( String requestUrl )
    {
        List< SettopDesc > settops = new ArrayList< SettopDesc >();

        try
        {
            settops = getResponseAsDomainList( requestUrl, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve List< SettopDesc >. " + e.getMessage() );
        }
        return settops;
    }

    /**
     * To get response as Settop Lock Description List
     * 
     * @param requestUrl
     * @return List of {@link SettopReservationDesc}
     */
    @SuppressWarnings( "unchecked" )
    protected List< SettopReservationDesc > getResponseAsSettopLockDescList( String requestUrl )
            throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.GET, requestUrl );
        List< SettopReservationDesc > responselist = Collections.emptyList();

        try
        {
            responselist = ( List< SettopReservationDesc > ) restTemplateProducer.getRestTemplate().getForObject(
                    requestUrl, SettopReservationDesc.class );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.GET, requestUrl );
        }
        return responselist;
    }

    @Override
    public Integer count()
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + SETTOPS ) + COUNT;

        int count = 0;

        try
        {
            count = getResponseAsNumber( requestUrl );
        }
        catch ( Exception e )
        {
            logger.error( "Failed to retrieve settop count. " + e.getMessage() );
        }

        return count;
    }

    @Override
    public List< SettopDesc > find( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + SETTOPS ) + offset + BACK_SLASH + count;

        List< SettopDesc > settops = new ArrayList< SettopDesc >();
        try
        {
            settops = getResponseAsDomainList( requestUrl, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve settop  list. " + e.getMessage() );
        }

        return settops;
    }
}
