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
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.comcast.cats.domain.util.PropertiesAdapter;

/**
 * Interface to communicate with a Hardware device in a {@link Slot}.
 * 
 * @author SSugun00c
 * 
 */
@XmlRootElement
public class HardwareInterface extends Domain
{
    private static final long     serialVersionUID = 8570615356455334528L;

    private HardwarePurpose       hardwarePurpose;

    /**
     * The component type.
     */
    private String                componentType;
    private String                deviceHost;
    private Integer               devicePort;
    private Integer               connectionPort;
    private URI                   interfacePath;

    private Map< String, String > extraProperties  = new HashMap< String, String >();

    public HardwareInterface()
    {
        super();
    }

    public HardwareInterface( String id )
    {
        super( id );
    }

    public HardwareInterface( String id, String name )
    {
        super( id, name );
    }

    public HardwareInterface( String id, String name, HardwarePurpose hardwarePurpose, String componentType,
            String deviceHost, Integer devicePort, Integer connectionPort )
    {
        super( id, name );
        this.hardwarePurpose = hardwarePurpose;
        this.componentType = componentType;
        this.deviceHost = deviceHost;
        this.devicePort = devicePort;
        this.connectionPort = connectionPort;
    }

    @XmlElement
    public HardwarePurpose getHardwarePurpose()
    {
        return hardwarePurpose;
    }

    public void setHardwarePurpose( HardwarePurpose hardwarePurpose )
    {
        this.hardwarePurpose = hardwarePurpose;
    }

    @XmlElement
    public String getComponentType()
    {
        return componentType;
    }

    public void setComponentType( String componentType )
    {
        this.componentType = componentType;
    }

    @XmlElement
    public String getDeviceHost()
    {
        return deviceHost;
    }

    public void setDeviceHost( String deviceHost )
    {
        this.deviceHost = deviceHost;
    }

    @XmlElement
    public Integer getDevicePort()
    {
        return devicePort;
    }

    public void setDevicePort( Integer devicePort )
    {
        this.devicePort = devicePort;
    }

    @XmlElement
    public Integer getConnectionPort()
    {
        return connectionPort;
    }

    public void setConnectionPort( Integer connectionPort )
    {
        this.connectionPort = connectionPort;
    }

    @XmlElement
    @XmlJavaTypeAdapter( PropertiesAdapter.class )
    public Map< String, String > getExtraProperties()
    {
        return extraProperties;
    }

    public void setExtraProperties( Map< String, String > extraProperties )
    {
        this.extraProperties = extraProperties;
    }

    @XmlElement( name = "path" )
    public URI getInterfacePath()
    {
        if ( null == interfacePath || null == interfacePath.getHost() )
        {
            try
            {
                interfacePath = new URI( getUriString() );
            }
            catch ( URISyntaxException e )
            {
                e.printStackTrace();
            }
        }

        return interfacePath;
    }

    public void setInterfacePath( URI interfacePath )
    {
        this.interfacePath = interfacePath;
    }

    private String getUriString()
    {
        String uriString = null;

        if ( null != componentType )
        {
            uriString = getValidURIScheme( componentType.toLowerCase() ) + "://" + deviceHost;

            if ( devicePort > 0 )
            {
                uriString += ":" + devicePort;
            }

            switch ( hardwarePurpose )
            {
            case VIDEOSERVER:
                uriString += "/?camera=" + connectionPort;
                break;

            default:
                uriString += "/?connectionPort=" + connectionPort;
                break;
            }

            uriString = encodeURI( uriString ).toLowerCase().trim();
        }

        return uriString;
    }

    private String getValidURIScheme( String uriScheme )
    {
        uriScheme = uriScheme.replaceAll( "[^a-zA-Z0-9+-.]", "" );
        return uriScheme;
    }

    private String encodeURI( String uriString )
    {
        uriString = uriString.replaceAll( " ", "%20" );
        return uriString;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [type=" + getComponentType() + ", deviceHost="
                + getDeviceHost() + ", devicePort=" + getDevicePort() + ", connectionPort=" + getConnectionPort()
                + ", extraProperties=" + getExtraProperties() + "]";
    }
}
