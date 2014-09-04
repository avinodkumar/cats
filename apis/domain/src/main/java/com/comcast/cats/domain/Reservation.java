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

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A reservation is the exclusive usage of a group of components for a given
 * period of time.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Reservation extends Domain
{
    private static final long serialVersionUID = -9069192451305790814L;

    private Date              startDate;
    private Date              endDate;
    private String            status;
    private Environment       environment;
    private User              reservationOwner;
    private List< UserGroup > userGroups;

    public Reservation()
    {
        // TODO Auto-generated constructor stub
    }

    public Reservation( String id )
    {
        super( id );
    }

    public Reservation( String id, String name )
    {
        super( id, name );
    }

    @XmlAttribute
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @XmlAttribute
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    @XmlAttribute
    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    @XmlElement( )
    public Environment getEnvironment()
    {
        return environment;
    }

    public void setEnvironment( Environment environment )
    {
        this.environment = environment;
    }

    @XmlElement( )
    public User getReservationOwner()
    {
        return reservationOwner;
    }

    public void setReservationOwner( User reservationOwner )
    {
        this.reservationOwner = reservationOwner;
    }

    @XmlElementWrapper( name = "userGroups" )
    @XmlElement( name = "userGroup" )
    public List< UserGroup > getUserGroups()
    {
        return userGroups;
    }

    public void setUserGroups( List< UserGroup > userGroups )
    {
        this.userGroups = userGroups;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [startDate=" + getStartDate() + ", endDate=" + getEndDate()
                + ", status=" + getStatus() + ", environment=" + getEnvironment() + ", reservationOwner="
                + getReservationOwner() + "]";
    }

}
