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
package com.comcast.cats.info;

import java.util.Date;

import com.comcast.cats.domain.SettopDesc;

/**
 * A Helper class that holds the properties of the settop and also allocation
 * and reservation informations. This class is made a subclass of SettopDesc as
 * it needs most of the attributes of SettopDesc.
 * 
 * This is deprecated for cats 2.4. Please use {@link SettopDesc} instead.
 * 
 * @author ajith
 * 
 */
@Deprecated
public class SettopAllocationDesc extends SettopDesc
{
    /**
     * Generated Serial Version ID.
     */
    private static final long serialVersionUID     = 8191903534193367603L;
    /**
     * The Start date of the allocation.
     */
    private Date              allocationStartDate  = null;
    /**
     * The End date of the allocation
     */
    private Date              allocationEndDate    = null;
    /**
     * The start date of the reservation.
     */
    private Date              reservationStartDate = null;
    /**
     * End date of reservation.
     */
    private Date              reservationEndDate   = null;

    /**
     * The name of the Settop.
     */
    private String            stbName              = null;

    /**
     * The reserved environment to which the component belongs.
     */
    private String            reservedEnvironment  = null;
    /**
     * The reservation name.
     */
    private String            reservationName      = null;

    /**
     * Getter for reservationName
     * 
     * @return ReservationName
     */
    public String getReservationName()
    {
        return reservationName;
    }

    /**
     * Setter for reservationName
     * 
     * @param reservationName
     *            reservation name
     */
    public void setReservationName( String reservationName )
    {
        this.reservationName = reservationName;
    }

    /**
     * Getter for allocationStartDate
     * 
     * @return allocationStartDate
     */
    public Date getAllocationStartDate()
    {
        return allocationStartDate;
    }

    /**
     * Setter for allocationStartDate
     * 
     * @param allocationStartDate
     */
    public void setAllocationStartDate( final Date allocationStartDate )
    {
        this.allocationStartDate = allocationStartDate;
    }

    /**
     * Getter for allocation end date
     * 
     * @return allocationEndDate
     */
    public Date getAllocationEndDate()
    {
        return allocationEndDate;
    }

    /**
     * setter for allocation end date
     * 
     * @param allocationEndDate
     */
    public void setAllocationEndDate( Date allocationEndDate )
    {
        this.allocationEndDate = allocationEndDate;
    }

    /**
     * Getter for reservationStartDate
     * 
     * @return reservationStartDate
     */
    public Date getReservationStartDate()
    {
        return reservationStartDate;
    }

    /**
     * Setter for reservationStartDate
     * 
     * @param reservationStartDate
     */
    public void setReservationStartDate( Date reservationStartDate )
    {
        this.reservationStartDate = reservationStartDate;
    }

    /**
     * Getter for reservationEndDate
     * 
     * @return reservationEndDate
     */
    public Date getReservationEndDate()
    {
        return reservationEndDate;
    }

    /**
     * Setter for reservationEndDate
     * 
     * @param reservationEndDate
     */
    public void setReservationEndDate( Date reservationEndDate )
    {
        this.reservationEndDate = reservationEndDate;
    }

    /**
     * Getter for STB Name.
     * 
     * @return stbName
     */
    public String getStbName()
    {
        return stbName;
    }

    /**
     * Setter for STB Name.
     * 
     * @param stbName
     */
    public void setStbName( final String stbName )
    {
        this.stbName = stbName;
    }

    /**
     * Getter for reservedEnvironment
     * 
     * @return reservedEnvironment
     */
    public String getReservedEnvironment()
    {
        return reservedEnvironment;
    }

    /**
     * Setter for reservedEnvironment
     * 
     * @param reservedEnvironment
     */
    public void setReservedEnvironment( String reservedEnvironment )
    {
        this.reservedEnvironment = reservedEnvironment;
    }

    /**
     * For custom hash code generation
     */
    @Override
    public int hashCode()
    {
        int hashCode = super.hashCode();
        String hostMACAddress = getHostMacAddress();
        String ipAddress = this.getHostIp4Address();
        if ( hostMACAddress != null )
        {
            hashCode = hostMACAddress.hashCode();
        }
        if ( reservationEndDate != null )
        {
            hashCode += reservationEndDate.hashCode();
        }
        if ( reservationStartDate != null )
        {
            hashCode += reservationStartDate.hashCode();
        }
        if ( allocationStartDate != null )
        {
            hashCode += allocationStartDate.hashCode();
        }
        if ( allocationEndDate != null )
        {
            hashCode += allocationEndDate.hashCode();
        }
        if ( ipAddress != null )
        {
            hashCode += ipAddress.hashCode();
        }
        if ( reservationName != null )
        {
            hashCode += reservationName.hashCode();
        }
        return hashCode;
    }

    /**
     * For custom equal criteria.
     */
    @Override
    public boolean equals( Object obj )
    {
        boolean isEqual = super.equals( obj );
        if ( !isEqual && obj instanceof SettopAllocationDesc )
        {
            SettopAllocationDesc settopAllocationDesc = ( SettopAllocationDesc ) obj;
            if ( settopAllocationDesc.hashCode() == this.hashCode() )
            {
                isEqual = true;
            }
        }
        return isEqual;
    }

    /**
     * toString() is used in search functionality of CATS Vision. Please check
     * before modification
     */
    @Override
    public String toString()
    {
        return super.toString() + "SettopName = " + stbName + "Reservation Name=" + reservationName
                + "Reservation Start Date=" + reservationStartDate + "Reservation End Date=" + reservationEndDate
                + " Allocation Start Date=" + allocationStartDate + " Allocation End Date=" + allocationEndDate;
    }

}
