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

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A hardware connection between two components..
 * 
 * @deprecated This class is deprecated. Use {@link HardwareInterface} instead.
 * 
 * @author subinsugunan
 * 
 */
@Deprecated
@XmlRootElement
public class HardwareConnection extends Domain
{
    private static final long serialVersionUID = -1352962005537222038L;

    private HardwareDevice    hardwareDevice;
    private Integer           port;

    public HardwareConnection()
    {
        // TODO Auto-generated constructor stub
    }

    public HardwareConnection( String id )
    {
        super( id );
    }

    @XmlElement( )
    public HardwareDevice getHardwareDevice()
    {
        return hardwareDevice;
    }

    public void setHardwareDevice( HardwareDevice hardwareDevice )
    {
        this.hardwareDevice = hardwareDevice;
    }

    @XmlAttribute
    public Integer getPort()
    {
        return port;
    }

    public void setPort( Integer port )
    {
        this.port = port;
    }

    public URI getConnectionPath()
    {
        String uriString = getUriString();

        URI uri = null;

        try
        {
            uri = new URI( uriString );
        }
        catch ( URISyntaxException e )
        {
            e.printStackTrace();
        }

        return uri;
    }

    private String getUriString()
    {
        int devicePort = 0;
        String deviceIp = null;
        String uriString = null;

        if ( null != hardwareDevice )
        {
            devicePort = hardwareDevice.getPort();
            deviceIp = ( null != hardwareDevice.getHost() ) ? hardwareDevice.getHost() : null;

            uriString = hardwareDevice.getComponentType() + "://" + deviceIp;

            if ( devicePort > 0 )
            {
                uriString += ":" + devicePort;
            }

            uriString += "/?connectionPort=" + port;

            uriString = encodeURI( uriString ).toLowerCase().trim();
        }
        return uriString;
    }

    private String encodeURI( String uriString )
    {
        uriString = uriString.replaceAll( " ", "%20" );
        return uriString;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [id=" + getId() + ", name=" + getName()
                + ", hardwareDevice=" + getHardwareDevice() + ", port" + getPort() + "]";
    }
}
