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

import java.util.List;
import java.util.Map;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * Service interface for {@link SettopDesc}
 * 
 * @author subinsugunan
 * 
 */
public interface SettopDomainService extends DomainService< SettopDesc >
{

    /**
     * Return {@link SettopDesc} for a given macId.
     * 
     * @param macId
     *            - The MAC address of the settop box
     * @return The deep copy of {@link SettopDesc}
     * 
     * @throws SettopNotFoundException
     */
    SettopDesc findByMacId( String macId ) throws SettopNotFoundException;

    /**
     * Return {@link SettopDesc} for a given macId. The returned SettopDesc will
     * not have information regarding the hardware connectors if the isShallow
     * is set to true
     * 
     * @param macId
     *            - The MAC address of the settop box
     * @param isShallow
     *            - true: shallow representation of {@link SettopDesc} false:
     *            deep copy of SettopDesc object
     * @return The {@link SettopDesc}
     * @throws SettopNotFoundException
     */
    SettopDesc findByMacId( String macId, Boolean isShallow ) throws SettopNotFoundException;

    List< SettopDesc > findByMacIdList( List< String > macIds );

    /**
     * Return all allocated {@link SettopDesc} for a given user. Calling this
     * API will return a deep copy version of SettopReservationDesc.
     * 
     * @return List of deep copied {@linkplain SettopReservationDesc}
     * 
     */
    List< SettopReservationDesc > findAllAllocated();

    /**
     * Return all allocated {@link SettopReservationDesc} for a given user.
     * 
     * @param isShallow
     *            - true: shallow representation of
     *            {@link SettopReservationDesc} false: deep copy of
     *            SettopReservationDesc object
     * @return List of {@linkplain SettopReservationDesc}
     */
    List< SettopReservationDesc > findAllAllocated( Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given user. Calling this
     * API will return a deep copy of SettopDesc.
     * 
     * @return List of deep copied {@linkplain SettopReservationDesc}
     */
    List< SettopReservationDesc > findAllAvailable();

    /**
     * Return all available {@link SettopReservationDesc} for a given user.
     * 
     * @param isShallow
     *            - true: shallow representation of
     *            {@link SettopReservationDesc} false: complete SettopDesc
     *            object
     * @return List of {@linkplain SettopReservationDesc}
     */
    List< SettopReservationDesc > findAllAvailable( Boolean isShallow );

    /**
     * Return all {@link SettopDesc}, which matches the criteria. Calling this
     * api will return a deep copied version of SettopDesc
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @return A {@linkplain List} of deep copied {@link SettopDesc} which
     *         matches the criteria
     */
    List< SettopDesc > findAllByCriteria( Map< String, String > criteria );

    /**
     * Return all {@link SettopDesc}, which matches the criteria.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Boolean isShallow );

    /**
     * Return all {@link SettopDesc}, which matches the criteria, 'up to' the
     * count specified, as per the availability.Calling this API will return a
     * deep copy version of SettopDesc
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
     * @return A {@linkplain List} of deep copied {@link SettopDesc} which
     *         matches the criteria
     */
    List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count );

    /**
     * Return all {@link SettopDesc}, which matches the criteria.
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
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all {@link SettopDesc}, which matches the specified
     * property-values combination. Calling this api will return a deep copy of
     * SettopDesc
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    List< SettopDesc > findAllByProperty( String property, String[] values );

    /**
     * Return all {@link SettopDesc}, which matches the specified
     * property-values combination.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    List< SettopDesc > findAllByProperty( String property, String[] values, Boolean isShallow );

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
    List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count );

    /**
     * Return all {@link SettopDesc}, which matches the specified
     * property-values combination.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @param offset
     *            - The starting point
     * @param count
     *            - The max limit
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return A {@linkplain List} of deep copied {@link SettopDesc} of a
     *         particular {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservationId( String reservationId );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservationId( String reservationId, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Rack}
     */
    List< SettopDesc > findAllByRackId( String rackId );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAllByRackId( String rackId, Boolean isShallow );

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
    List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            true:return a shallow copied version of {@link SettopDesc}
     *            false:return a deep copied version of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return A {@linkplain List} of deep copied {@link SettopDesc} of a
     *         particular {@linkplain SettopGroup}
     */
    List< SettopDesc > findAllBySettopGroupId( String settopGroupId );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param isShallow
     *            - true: to get the shallow copy of {@linkplain SettopDesc}
     *            false: to get the deep copy of {@linkplain SettopDesc}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} from a given {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of deep copied {@link SettopDesc} of a
     *         particular {@linkplain SettopGroup}
     */
    List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count );

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
     *            - true: to get the shallow copy of {@linkplain SettopDesc}
     *            false: to get the deep copy of {@linkplain SettopDesc}
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservation( Reservation reservation );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservation( Reservation reservation, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Rack}
     */
    List< SettopDesc > findAllByRack( Rack rack );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAllByRack( Rack rack, Boolean isShallow );

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
    List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count, Boolean isShallow );

    /**
     * Return all {@link SettopDesc} for a given {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain SettopGroup}
     * @throws SettopNotFoundException
     */
    List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup ) throws SettopNotFoundException;

    /**
     * Return all {@link SettopDesc} for a given {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     * @throws SettopNotFoundException
     */
    List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
            throws SettopNotFoundException;

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
    List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count )
            throws SettopNotFoundException;

    /**
     * Return all {@link SettopDesc} for a given {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     * @throws SettopNotFoundException
     */
    List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count, Boolean isShallow )
            throws SettopNotFoundException;

    /**
     * Return all available {@link SettopDesc}, which matches the criteria.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @return A {@linkplain List} of deep copied {@link SettopDesc} which
     *         matches the criteria
     */
    List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria );

    /**
     * Return all available {@link SettopDesc}, which matches the criteria.
     * 
     * @param criteria
     *            - Criteria in the form of {@linkplain Map} (key-value pair).
     *            Key and value of the map will be compared to property and
     *            value correspondingly in the implementing system. Each entry
     *            in the map will be logically ANDed with other entries to
     *            create the final criteria
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Boolean isShallow );

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
    List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count );

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
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         criteria
     */
    List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all available{@link SettopDesc}, which matches the specified
     * property-values combination.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @return A {@linkplain List} of deep copied {@link SettopDesc} which
     *         matches the specified property-values combination.
     */
    List< SettopDesc > findAvailableByProperty( String property, String[] values );

    /**
     * Return all available{@link SettopDesc}, which matches the specified
     * property-values combination.
     * 
     * @param property
     *            - The property of the settop box
     * @param values
     *            - A String array of possible values
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    List< SettopDesc > findAvailableByProperty( String property, String[] values, Boolean isShallow );

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
     * @return A {@linkplain List} of deep copied{@link SettopDesc} which
     *         matches the specified property-values combination.
     */
    List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count );

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
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} which matches the
     *         specified property-values combination.
     */
    List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservationId( String reservationId );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservationId( String reservationId, Boolean isShallow );

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
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count );

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
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Rack}
     */
    List< SettopDesc > findAvailableByRackId( String rackId );

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAvailableByRackId( String rackId, Boolean isShallow );

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
    List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count );

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count, Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * 
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Boolean isShallow );

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
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count );

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
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservation( Reservation reservation );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain Reservation}
     * 
     * @param reservation
     *            - The {@linkplain Reservation}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservation( Reservation reservation, Boolean isShallow );

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
     * @return A {@linkplain List} deep copy of {@link SettopDesc} of a
     *         particular {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count );

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
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Reservation}
     */
    List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @return A {@linkplain List} of deep copied{@link SettopDesc} of a
     *         particular {@linkplain Rack}
     */
    List< SettopDesc > findAvailableByRack( Rack rack );

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAvailableByRack( Rack rack, Boolean isShallow );

    /**
     * Return all available {@link SettopDesc} for a given {@linkplain Rack}
     * 
     * @param rack
     *            - The {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain Rack}
     */
    List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count, Boolean isShallow );

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
    List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@link SettopDesc}
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup );

    /**
     * Return all available {@link SettopDesc} for a given
     * {@linkplain SettopGroup}
     * 
     * @param settopGroup
     *            - The {@link SettopDesc}
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Boolean isShallow );

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
    List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count );

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
     * @param isShallow
     *            - true: to get the shallow copy of {@link SettopDesc} false:
     *            to get the deep copy of SettopDesc
     * 
     * @return A {@linkplain List} of {@link SettopDesc} of a particular
     *         {@linkplain SettopGroup}
     */
    List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow );

    /**
     * Count all {@link SettopDesc} of a {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return The count
     */
    Integer countAllByReservationId( String reservationId );

    /**
     * Count all {@link SettopDesc} of a {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return The count
     */
    Integer countAllBySettopGroupId( String settopGroupId );

    /**
     * Count all {@link SettopDesc} of a {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return The count
     */
    Integer countAllByRackId( String rackId );

    /**
     * Count all available {@link SettopDesc} of a {@linkplain Reservation}
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @return The count
     */
    Integer countAvailableByReservationId( String reservationId );

    /**
     * Count all available {@link SettopDesc} of a {@linkplain SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return The count
     */
    Integer countAvailableBySettopGroupId( String settopGroupId );

    /**
     * Count all available {@link SettopDesc} of a {@linkplain Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return The count
     */
    Integer countAvailableByRackId( String rackId );

}
