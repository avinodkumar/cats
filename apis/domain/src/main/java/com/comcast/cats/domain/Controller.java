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
 * The controller associated with the RFPlant.
 * Used to consider if the settop is connected to a controller.
 * 
 * @author minu
 * 
 */
@XmlRootElement
public class Controller extends Domain
{
    private static final long serialVersionUID = 1550490711944446818L;

    private String            type;
    private String            ipAddress;
    private String            version;

    public Controller()
    {
        // TODO Auto-generated constructor stub
    }

    public Controller( String id )
    {
        super( id );
    }

    @XmlAttribute
    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    @XmlAttribute
    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress( String ipAddress )
    {
        this.ipAddress = ipAddress;
    }

    @XmlAttribute
    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [controllerType=" + getType() + ", ipAddress= "
                + getIpAddress() + ", version=" + getVersion() + "]";
    }
}
