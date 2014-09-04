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
package com.comcast.cats.messaging;

import java.util.Date;

import com.comcast.cats.domain.Reservation;

/**
 * 
 * @author minu
 * 
 */
public class ReservationMessage extends BaseMessage< Reservation >
{
    private static final long serialVersionUID = 1L;

    public ReservationMessage()
    {
        super();
    }

    public ReservationMessage( Reservation reservation, MessageType messageType )
    {
        super( reservation, messageType );
    }

    public ReservationMessage( String id, String name, MessageType messageType, Date startDate, Date endDate )
    {
        super( id, name, messageType, null, startDate, endDate );
    }

    public Reservation getReservation()
    {
        return domainObject;
    }

    public void setReservation( Reservation reservation )
    {
        this.domainObject = reservation;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " []";
    }
}
