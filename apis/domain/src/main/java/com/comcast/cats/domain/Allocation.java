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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents one allocation with in a {@link Reservation}.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Allocation extends Domain
{
    private static final long  serialVersionUID = -1787561307597288983L;

    private AllocationCategory category;
    private Date               startDate;
    private Date               endDate;
    private String             status;
    private Component          component;
    private User               user;
    private Reservation        reservation;

    public Allocation()
    {
      
    }

    public Allocation( String id )
    {
        super( id );
    }

    /**
     * Get the category the allocation belongs to.
     * Ex: Stability, Manual, Automaton etc etc.
     *
     * @return {@link AllocationCategory}
     */
    @XmlAttribute
    public AllocationCategory getCategory()
    {
        return category;
    }

    /**
     * Set the category for this allocation
     * @param category {@link AllocationCategory} 
     */
    public void setCategory( AllocationCategory category )
    {
        this.category = category;
    }

    /**
     * Get the time the Allocation was started
     * @return Date
     */
    @XmlAttribute
    public Date getStartDate()
    {
        return startDate;
    }

    /**
     * Set the time the Allocation was started
     * @return
     */
    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    /**
     * Get the time the Allocation ended.
     * @return
     */
    @XmlAttribute
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     * Set the end date of the allocation.
     * @param endDate
     */
    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    /**
     * Return the status of the {@link Allocation}
     * @return
     */
    @XmlAttribute
    public String getStatus()
    {
        return status;
    }

    /**
     * Set the status of the allocation.
     * 
     * @param status
     */
    public void setStatus( String status )
    {
        this.status = status;
    }

    /**
     * The component which was allocated. usually the settop object.
     * @return
     */
    @XmlElement( )
    public Component getComponent()
    {
        return component;
    }

    /**
     * Set the component this allocation belongs to.
     * @see getComponent();
     * @param component
     */
    public void setComponent( Component component )
    {
        this.component = component;
    }

    /**
     * The user the allocation belongs to.
     *
     * @return
     */
    @XmlElement( )
    public User getUser()
    {
        return user;
    }

    /**
     * The user the allocation belongs to.
     * @param user
     */
    public void setUser( User user )
    {
        this.user = user;
    }

    /**
     * The reservation the allocation belongs to. 
     * Settops are usually placed in a reservation before they are allocated.
     *
     * @return
     */
    @XmlElement( )
    public Reservation getReservation()
    {
        return reservation;
    }

    /**
     * Set the reservation the allocation belongs to.
     * @see getReservation()
     * @param reservation
     */
    public void setReservation( Reservation reservation )
    {
        this.reservation = reservation;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [startDate=" + getStartDate() + ", endDate=" + getEndDate()
                + ", status=" + getStatus() + ", component=" + getComponent() + ", user=" + getUser()
                + ", reservation=" + getReservation() + "]";
    }
}
