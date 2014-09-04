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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Logical representation of a hardware.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Component extends Domain
{
    private static final long serialVersionUID = -342013801896544899L;

    private String            componentType;
    private Location          location;

    public Component()
    {
    }

    public Component( String id )
    {
        super( id );
    }

    public Component( String id, String name )
    {
        super( id, name );
    }

    public Component( Component component )
    {
        super( component );
        this.componentType = component.componentType;
        this.location = component.location;
    }

    @XmlElement( )
    public Location getLocation()
    {
        return location;
    }

    public void setLocation( Location location )
    {
        this.location = location;
    }

    @XmlAttribute
    public String getComponentType()
    {
        return componentType;
    }

    public void setComponentType( String componentType )
    {
        this.componentType = componentType;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [componentType=" + getComponentType() + ", location="
                + getLocation() + "]";
    }
}
