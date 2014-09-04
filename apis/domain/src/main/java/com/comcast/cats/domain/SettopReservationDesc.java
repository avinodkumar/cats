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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A special {@link SettopDesc} class which holds additional information about
 * {@link Reservation} and {@link Rack} of a Settop.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class SettopReservationDesc extends SettopDesc
{
    private static final long   serialVersionUID = -5761677798437361314L;

    private Rack                rack;
    /**
     * TODO: There is no need of a list here. Settops will have only one active
     * reservation at a time.
     */
    private List< Reservation > activeReservationList;

    public SettopReservationDesc()
    {

    }

    public SettopReservationDesc( SettopReservationDesc settopReservationDesc )
    {
        super( settopReservationDesc );
        this.rack = settopReservationDesc.rack;
        this.activeReservationList = settopReservationDesc.activeReservationList;
    }
    
    public SettopReservationDesc( String id )
    {
        super( id );
    }

    @XmlElement( )
    public Rack getRack()
    {
        return rack;
    }

    public void setRack( Rack rack )
    {
        this.rack = rack;
    }

    @XmlElementWrapper( name = "reservations" )
    @XmlElement( name = "reservation" )
    public List< Reservation > getActiveReservationList()
    {
        return activeReservationList;
    }

    public void setActiveReservationList( List< Reservation > activeReservationList )
    {
        this.activeReservationList = activeReservationList;
    }

    /**
     * toString() is used in search functionality of CATS Vision. Please check
     * before modification
     */
    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [Rack=" + rack + "ActiveReservationList = "
                + activeReservationList + "]";
    }
}
