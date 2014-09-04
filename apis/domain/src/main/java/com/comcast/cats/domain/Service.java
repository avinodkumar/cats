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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a service deployed in CATS server.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class Service extends Domain
{
    private static final long serialVersionUID = -4146598441262673467L;

    private URL               path;
    private ServiceType       serviceType;
    private String            version;

    public Service()
    {
        super();
    }

    public Service( String id )
    {
        super( id );
    }

    public Service( ServiceType serviceType )
    {
        this.serviceType = serviceType;
    }

    @XmlAttribute
    public URL getPath()
    {
        return path;
    }

    public void setPath( URL path )
    {
        this.path = path;
    }

    @XmlAttribute
    public ServiceType getServiceType()
    {
        return serviceType;
    }

    public void setServiceType( ServiceType serviceType )
    {
        this.serviceType = serviceType;
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
        return super.toString() + getClass().getName() + " [path=" + getPath() + ", serviceType=" + getServiceType()
                + ", version=" + getVersion() + "]";
    }
}
