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
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a slot in a {@link Rack}.
 * 
 * @author SSugun00c
 * @see Rack
 */
@XmlRootElement
public class Slot extends Domain
{
    private static final long         serialVersionUID = 1L;

    private Integer                   number;

    private String                    settopId;

    private List< HardwareInterface > hardwareInterfaces = new ArrayList< HardwareInterface >();

    public Slot()
    {
        super();
    }

    public Slot( String id )
    {
        super( id );
    }

    public Slot( List< HardwareInterface > hardwareInterfaces )
    {
        super();
        this.hardwareInterfaces = hardwareInterfaces;
    }

    @XmlElement
    public String getSettopId()
    {
        return settopId;
    }

    public void setSettopId( String settopId )
    {
        this.settopId = settopId;
    }

    @XmlElementWrapper( name = "hardwareInterfaces" )
    @XmlElement( name = "hardwareInterface" )
    public List< HardwareInterface > getHardwareInterfaces()
    {
        return hardwareInterfaces;
    }

    public void setHardwareInterfaces( List< HardwareInterface > hardwareInterfaces )
    {
        this.hardwareInterfaces = hardwareInterfaces;
    }

    public void addToHardwareInterface( HardwarePurpose type, HardwareInterface hardwareInterface )
    {
        this.hardwareInterfaces.add( hardwareInterface );
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber( Integer number )
    {
        this.number = number;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [hardwareInterfaces=" + getHardwareInterfaces()
                + ", number=" + getNumber() + "]";
    }
}
