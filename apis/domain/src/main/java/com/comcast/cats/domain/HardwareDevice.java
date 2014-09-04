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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.comcast.cats.domain.util.PropertiesAdapter;

/**
 * Logical representation of a hardware device.
 * 
 * @deprecated This class is deprecated. Use {@link HardwareInterface} instead.
 * 
 * @author subinsugunan
 * 
 */
@Deprecated
@XmlRootElement
public class HardwareDevice extends Component
{
    private static final long     serialVersionUID = 4879822883171368683L;

    private HardwareType          hardwareType;
    private String                host;
    private Integer               port;
    private Integer               maxPort;

    private Map< String, String > extraProperties  = new HashMap< String, String >();

    public HardwareDevice()
    {
        super();
    }

    public HardwareDevice( String id )
    {
        super( id );
    }

    public HardwareDevice( HardwareType hardwareType )
    {
        this.hardwareType = hardwareType;
    }

    @XmlElement( )
    public HardwareType getHardwareType()
    {
        return hardwareType;
    }

    public void setHardwareType( HardwareType hardwareType )
    {
        this.hardwareType = hardwareType;
    }

    @XmlAttribute
    public String getHost()
    {
        return host;
    }

    public void setHost( String host )
    {
        this.host = host;
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

    @XmlAttribute
    public Integer getMaxPort()
    {
        return maxPort;
    }

    public void setMaxPort( Integer maxPort )
    {
        this.maxPort = maxPort;
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

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [hardwareType=" + getHardwareType() + ", host=" + getHost()
                + ", port=" + getPort() + ", maxPort=" + getMaxPort() + "]";
    }
}
