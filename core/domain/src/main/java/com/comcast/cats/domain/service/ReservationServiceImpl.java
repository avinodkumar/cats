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
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.MultipleActiveReservationFoundException;
import com.comcast.cats.domain.exception.ReservationInstantiationException;
import com.comcast.cats.domain.exception.ReservationNotFoundException;

/**
 * 
 * @author SSugun00c
 * 
 */
@Named
public class ReservationServiceImpl extends DomainServiceImpl< Reservation > implements ReservationService
{

    @Override
    public Reservation createBySettopGroupId( String settopGroupId, Date startDate, Date endDate )
            throws ReservationInstantiationException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Reservation createByRackId( String rackId, Date startDate, Date endDate )
            throws ReservationInstantiationException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Reservation findBySettopGroupId( String settopGroupId ) throws ReservationNotFoundException,
            MultipleActiveReservationFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Reservation findByRackId( String rackId ) throws ReservationNotFoundException,
            MultipleActiveReservationFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Reservation findByAllocationId( String allocationId ) throws ReservationNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Reservation > findAllBySettopGroupId( String settopGroupId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Reservation > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Reservation > findAllBySettopRackId( String rackId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Reservation > findAllBySettopRackId( String rackId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Reservation > findAllByComponentId( String componentId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Reservation > findAllByComponentId( String componentId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Reservation update( String reservationId, Date endDate ) throws ReservationNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Integer countActive()
    {
        String requestUrl = getBaseUrl(  MDS + BACK_SLASH + RESERVATIONS ) + ACTIVE + BACK_SLASH + COUNT;

        int count = 0;

        try
        {
            count = getResponseAsNumber( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve active reservation count. " + e.getMessage() );
        }

        return count;
    }

    @Override
    public List< Reservation > findActive( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + RESERVATIONS ) + ACTIVE + BACK_SLASH + offset + BACK_SLASH
                + count;
        
        List< Reservation > activeReservations = new ArrayList< Reservation >();
        try
        {
            activeReservations = getResponseAsDomainList( requestUrl, Reservation.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve active reservations  list. " + e.getMessage() );
        }

        return activeReservations;
    }
}
