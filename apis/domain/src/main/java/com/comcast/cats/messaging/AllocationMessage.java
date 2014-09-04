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

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.AllocationCategory;

/**
 * 
 * @author SSugun00c
 * 
 */
public class AllocationMessage extends BaseMessage< Allocation >
{
    private static final long  serialVersionUID = 1L;

    private String             settopId;
    private String             reservationId;
    private String             reservationName;
    private String             allocationStatus;
    private AllocationCategory allocationCategory;

    public AllocationMessage()
    {
        super();
    }

    public AllocationMessage( String settopId, String userId, MessageType messageType )
    {
        this.settopId = settopId;
        this.userId = userId;
        this.messageType = messageType;
    }

    public AllocationMessage( Allocation alllocation, MessageType messageType )
    {
       super( alllocation, messageType );
    }

    public AllocationMessage( Allocation alllocation, String userId, MessageType messageType )
    {
        super( alllocation, messageType );
        this.userId = userId;
    }

    public AllocationMessage( String id, String allocationStatus, MessageType messageType, String userId,
            Date startDate, Date endDate )
    {
        super( id, null, messageType, userId, startDate, endDate );
        this.allocationStatus = allocationStatus;
    }

    public AllocationMessage( String id, MessageType messageType, String userId, Date startDate, Date endDate,
            String settopId, String reservationId, String allocationStatus )
    {
        super( id, null, messageType, userId, startDate, endDate );
        this.settopId = settopId;
        this.reservationId = reservationId;
        this.allocationStatus = allocationStatus;
    }

    public String getSettopId()
    {
        return settopId;
    }

    public void setSettopId( String settopId )
    {
        this.settopId = settopId;
    }

    public String getReservationId()
    {
        return reservationId;
    }

    public void setReservationId( String reservationId )
    {
        this.reservationId = reservationId;
    }

    public String getReservationName()
    {
        return reservationName;
    }

    public void setReservationName( String reservationName )
    {
        this.reservationName = reservationName;
    }

    public String getAllocationStatus()
    {
        return allocationStatus;
    }

    public void setAllocationStatus( String allocationStatus )
    {
        this.allocationStatus = allocationStatus;
    }

    public AllocationCategory getAllocationCategory()
    {
        return allocationCategory;
    }

    public void setAllocationCategory( AllocationCategory allocationCategory )
    {
        this.allocationCategory = allocationCategory;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [settopId=" + getSettopId() + ", reservationName="
                + getReservationName() + ", allocationStatus=" + getAllocationStatus() + ", allocationCategory="
                + getAllocationCategory() + "]";
    }

}
