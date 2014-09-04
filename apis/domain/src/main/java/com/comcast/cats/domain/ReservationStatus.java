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
package com.comcast.cats.domain;

/**
 * Status of {@link Reservation}
 * 
 * @author minu
 * 
 */
public enum ReservationStatus
{
    DRAFT( "Draft" ), RESERVED( "Reserved" ), COMPLETE( "Complete" );

    private final String status;

    ReservationStatus( String status )
    {
        this.status = status;
    }

    public String getStatusString()
    {
        return status;
    }

    public static ReservationStatus parse( final String value )
    {
        for ( ReservationStatus reservationStatus : values() )
        {
            if ( value != null
                    && ( value.equals( reservationStatus.name() ) || value.equals( reservationStatus.status ) ) )
            {
                return reservationStatus;
            }
        }
        throw new IllegalArgumentException( value + " is not a kind of ReservationStatus" );
    }
}
