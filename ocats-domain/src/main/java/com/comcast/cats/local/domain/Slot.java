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

import static com.comcast.cats.domain.HardwarePurpose.IR;
import static com.comcast.cats.domain.HardwarePurpose.POWER;
import static com.comcast.cats.domain.HardwarePurpose.TRACE;
import static com.comcast.cats.domain.HardwarePurpose.VIDEOSERVER;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;

public class Slot implements Serializable, Comparable< Slot >
{

    private static final long                         serialVersionUID = -2752813407824601103L;
    protected Integer                                 number;
    protected Map< HardwarePurpose, HardwareInterface > connections      = new HashMap< HardwarePurpose, HardwareInterface >();
    protected Rack                                    rack;
    private String                                    id;

    public Slot()
    {
        super(); // Do nothing.
        id = UUID.randomUUID().toString();
    }

    public Slot( Integer number )
    {
        this();
        this.number = number;
    }

    public Slot( Integer number, Map< HardwarePurpose, HardwareInterface > connections )
    {
        this( number );
        this.connections = connections;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber( Integer number )
    {
        this.number = number;
    }

    public Map< HardwarePurpose, HardwareInterface > getConnections()
    {
        return connections;
    }

    public void setConnections( Map< HardwarePurpose, HardwareInterface > connections )
    {
        this.connections = connections;
    }

    public int compareTo( Slot rhs )
    {
        
        int compareResult = 0;
        if(rack != null && rhs != null  && rhs.getRack() != null){
            compareResult = rack.compareTo( rhs.getRack() );
            
            if (compareResult == 0 && number != null ) // if racks are same, sort by slot number.
            {
                compareResult = number.compareTo(  rhs.getNumber() );
            }
        }
        return compareResult;
    }

    protected HardwareInterface getHardwareConnection( HardwarePurpose type )
    {
        if ( this.connections.containsKey( type ) )
        {
            return this.connections.get( type );
        }
        return null;
    }

    protected Integer getPortHelper( HardwarePurpose type )
    {
        HardwareInterface conn = getHardwareConnection( type );
        if ( conn != null )
        {
            return conn.getConnectionPort();
        }
        return -1;
    }

    protected String getHostHelper( HardwarePurpose type )
    {
        HardwareInterface conn = getHardwareConnection( type );
        if ( conn != null )
        {
            return conn.getDeviceHost();
        }
        return "--";
    }

    public HardwareInterface getIrConnection()
    {
        return getHardwareConnection( IR );
    }

    public HardwareInterface getPowerConnection()
    {
        return getHardwareConnection( POWER );
    }

    public HardwareInterface getVideoConnection()
    {
        return getHardwareConnection( VIDEOSERVER );
    }

    public HardwareInterface getTraceConnection()
    {
        return getHardwareConnection( TRACE );
    }

    public String getIrHost()
    {
        return getHostHelper( IR );
    }

    public Integer getIrPort()
    {
        return getPortHelper( IR );
    }

    public String getPowerHost()
    {
        return getHostHelper( POWER );
    }

    public Integer getPowerPort()
    {
        return getPortHelper( POWER );
    }

    public String getVideoHost()
    {
        return getHostHelper( VIDEOSERVER );
    }

    public Integer getVideoPort()
    {
        return getPortHelper( VIDEOSERVER );
    }

    public String getTraceHost()
    {
        return getHostHelper( TRACE );
    }

    public Integer getTracePort()
    {
        return getPortHelper( TRACE );
    }

    public Rack getRack()
    {
        return rack;
    }

    public void setRack( Rack rack )
    {
        this.rack = rack;
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
        if ( rack != null )
        {
            return "Slot [num=" + number + " Rack " + rack.name + "]";// +
                                                                      // ", connections="
                                                                      // +
                                                                      // connections
                                                                      // +
                                                                      // "]\n";
        }
        else
        {
            return "Slot [num=" + number + "]";
        }
    }

    @Override
    public boolean equals( Object object )
    {
        boolean retVal = false;
        if ( object instanceof Slot )
        {
            Slot slot = ( Slot ) object;
            if ( slot.getNumber() != null && slot.getRack() != null )
            {
                if ( slot.getNumber().equals( this.getNumber() ) )
                {
                    if ( slot.getRack().equals( this.getRack() ) )
                    {
                        retVal = true;
                    }
                }
            }
        }
        return retVal;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        if ( this.rack != null && rack.getName() != null )
        {
            char[] charArray = this.rack.getName().toCharArray();
            for ( char c : charArray )
            {
                hashCode += ( 'A' - c );
            }
        }
        return hashCode;
    }
}
