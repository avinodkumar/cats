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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A definitive location for a piece of hardware. Based upon a lab, zone, row,
 * rack, and usespace.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Location extends Domain
{
    private static final long serialVersionUID = 4728565824109361354L;

    private String            lab;
    private String            zone;
    private String            row;
    private String            rack;
    private String            usespace;

    public Location()
    {
        // TODO Auto-generated constructor stub
    }

    public Location( String id )
    {
        super( id );
    }
    
    @XmlAttribute
    public String getLab()
    {
        return lab;
    }

    public void setLab( String lab )
    {
        this.lab = lab;
    }

    @XmlAttribute
    public String getZone()
    {
        return zone;
    }

    public void setZone( String zone )
    {
        this.zone = zone;
    }

    @XmlAttribute
    public String getRow()
    {
        return row;
    }

    public void setRow( String row )
    {
        this.row = row;
    }

    @XmlAttribute
    public String getRack()
    {
        return rack;
    }

    public void setRack( String rack )
    {
        this.rack = rack;
    }

    @XmlAttribute
    public String getUsespace()
    {
        return usespace;
    }

    public void setUsespace( String usespace )
    {
        this.usespace = usespace;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [lab=" + getLab() + ", zone=" + getZone() + ", row="
                + getRow() + ", rack=" + getRack() + ", usespace=" + getUsespace() + "]";
    }
}
