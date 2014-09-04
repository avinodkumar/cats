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

import java.net.URL;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a CATS instance.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Server extends Component
{
    private static final long serialVersionUID = -8588726005295403755L;

    private String            protocol;
    private String            host;
    private Integer               port;
    private URL               baseUrl;
    private List< Service >   services;

    public Server()
    {
        // TODO Auto-generated constructor stub
    }

    public Server( String id )
    {
        super( id );
    }

    public Server( String id, String name )
    {
        super( id, name );
    }

    @XmlAttribute
    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol( String protocol )
    {
        this.protocol = protocol;
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
    public URL getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl( URL baseUrl )
    {
        this.baseUrl = baseUrl;
    }

    @XmlElementWrapper( name = "services" )
    @XmlElement( name = "service" )
    public List< Service > getServices()
    {
        return services;
    }

    public void setServices( List< Service > services )
    {
        this.services = services;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [protocol=" + getProtocol() + ", host=" + getHost()
                + ", port=" + getPort() + ", baseUrl=" + getBaseUrl() + "]";
    }
}
