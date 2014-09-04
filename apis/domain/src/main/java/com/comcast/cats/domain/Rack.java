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
 * A logical representation of a rack.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Rack extends Domain
{
    private static final long serialVersionUID = 1732314252797032636L;

    private List< Slot >      slots;

    public Rack()
    {
        // TODO Auto-generated constructor stub
    }

    public Rack( String id )
    {
        super( id );
    }

    @XmlElementWrapper( name = "slots" )
    @XmlElement( name = "slot" )
    public List< Slot > getSlots()
    {
        return slots;
    }

    public void setSlots( List< Slot > slots )
    {
        this.slots = slots;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [slots=" + getSlots() + "]";
    }
}
