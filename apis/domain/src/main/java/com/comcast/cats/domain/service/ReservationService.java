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

import java.util.Date;
import java.util.List;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.MultipleActiveReservationFoundException;
import com.comcast.cats.domain.exception.ReservationInstantiationException;
import com.comcast.cats.domain.exception.ReservationNotFoundException;

/**
 * Service interface for {@linkplain Reservation}
 * 
 * @author subinsugunan
 * 
 */
public interface ReservationService extends DomainService< Reservation >
{

    /**
     * Creates a new reservation for a particular {@link SettopGroup}. Up on
     * creation, the reservation status will be 'draft'.
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param startDate
     *            - Start date of {@linkplain Reservation}
     * @param endDate
     *            - End date of {@linkplain Reservation}
     * @return The {@linkplain Reservation}
     * @throws ReservationInstantiationException
     */
    Reservation createBySettopGroupId( String settopGroupId, Date startDate, Date endDate )
            throws ReservationInstantiationException;

    /**
     * Creates a new reservation for a particular {@link Rack}. Up on creation,
     * the reservation status will be 'draft'.
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param startDate
     *            - Start date of {@linkplain Reservation}
     * @param endDate
     *            - End date of {@linkplain Reservation}
     * @return The {@linkplain Reservation}
     * @throws ReservationInstantiationException
     */
    Reservation createByRackId( String rackId, Date startDate, Date endDate ) throws ReservationInstantiationException;

    /**
     * Finds an active {@link Reservation} for a given {@link SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return The {@linkplain Reservation}
     * @throws ReservationNotFoundException
     * @throws MultipleActiveReservationFoundException
     */
    Reservation findBySettopGroupId( String settopGroupId ) throws ReservationNotFoundException,
            MultipleActiveReservationFoundException;

    /**
     * Finds an active {@link Reservation} for a given {@link Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return The {@linkplain Reservation}
     * @throws ReservationNotFoundException
     * @throws MultipleActiveReservationFoundException
     */
    Reservation findByRackId( String rackId ) throws ReservationNotFoundException,
            MultipleActiveReservationFoundException;

    /**
     * Finds the {@link Reservation} of a given {@link Allocation}
     * 
     * @param allocationId
     *            - Id of the {@linkplain Allocation}
     * @return The {@linkplain Reservation}
     * @throws ReservationNotFoundException
     */
    Reservation findByAllocationId( String allocationId ) throws ReservationNotFoundException;

    /**
     * Finds all {@link Reservation} against a given {@link SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @return A {@linkplain List} of {@linkplain Reservation}
     */
    List< Reservation > findAllBySettopGroupId( String settopGroupId );

    /**
     * Finds all {@link Reservation} against a given {@link SettopGroup}
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Reservation}
     */
    List< Reservation > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count );

    /**
     * Finds all {@link Reservation} against a given {@link Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @return A {@linkplain List} of {@linkplain Reservation}
     */
    List< Reservation > findAllBySettopRackId( String rackId );

    /**
     * Finds all {@link Reservation} against a given {@link Rack}
     * 
     * @param rackId
     *            - Id of the {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Reservation}
     */
    List< Reservation > findAllBySettopRackId( String rackId, Integer offset, Integer count );

    /**
     * Finds all {@link Reservation} against a given {@link SettopDesc}
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @return A {@linkplain List} of {@linkplain Reservation}
     */
    List< Reservation > findAllByComponentId( String componentId );

    /**
     * Finds all {@link Reservation} against a given {@link SettopDesc}
     * 
     * @param componentId
     *            - Id of the {@linkplain SettopDesc}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Reservation}
     */
    List< Reservation > findAllByComponentId( String componentId, Integer offset, Integer count );

    /**
     * Update the end date of a {@link Reservation}.
     * 
     * @param reservationId
     *            - Id of the {@linkplain Reservation}
     * @param endDate
     *            - New end date of {@linkplain Reservation}
     * @return The updated {@linkplain Reservation}
     * @throws ReservationNotFoundException
     */
    Reservation update( String reservationId, Date endDate ) throws ReservationNotFoundException;

}
