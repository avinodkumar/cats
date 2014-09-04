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
package com.comcast.cats.local.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rack implements Serializable, Comparable< Rack >
{

    private static final long serialVersionUID = -3026366618632553794L;
    String                    name;
    int                       noOfSlots;
    List< Slot >              slots            = new ArrayList< Slot >();
    private String            id;

    public Rack()
    {
        super();
        id = UUID.randomUUID().toString();
    }

    public Rack( String name, int noOfSlots )
    {
        this();
        this.name = name;
        this.noOfSlots = noOfSlots;
    }

    public Rack( String name, List< Slot > slots, int noOfSlots )
    {
        this();
        this.name = name;
        this.slots = slots;
        this.noOfSlots = noOfSlots;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getNoOfSlots()
    {
        return noOfSlots;
    }

    public void setNoOfSlots( int noOfSlots )
    {
        this.noOfSlots = noOfSlots;
    }

    public List< Slot > getSlots()
    {
        ArrayList< Slot > rackSlots = new ArrayList< Slot >( slots );
        return rackSlots;
    }

    public void setSlots( List< Slot > slots )
    {
        this.slots = slots;
    }

    @Override
    public boolean equals( Object object )
    {
        boolean retVal = false;

        if ( object instanceof Rack )
        {
            Rack rack = ( Rack ) object;
            if ( rack.getName().equals( this.name ) )
            {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        if ( this.getName() != null )
        {
            char[] charArray = this.getName().toCharArray();
            for ( char c : charArray )
            {
                hashCode += ( 'A' - c );
            }
        }
        return hashCode;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "Rack [name=" + name + "]";
    }

    @Override
    public int compareTo( Rack rhs  )
    {
        int compareResult = 0;
        if(getName()!=null && rhs != null){
            compareResult = getName().compareTo( rhs.getName() );
        }
        return compareResult;
    }
}