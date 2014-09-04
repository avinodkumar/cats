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
package com.comcast.cats.domain.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.domain.Service;

/**
 * This class defines the xml data returned from the listing REST service.
 * 
 * @author bemman01c
 * 
 * @param <T>
 */

@XmlRootElement( name = "collection" )
public class ServiceListWrapper< T > implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 845626611933942469L;

    protected List< Service > list             = new ArrayList< Service >();

    @XmlElement( name = "service" )
    public List< Service > getServices()
    {
        return list;
    }

    public void setServices( Service service )
    {
        this.list.add( service );
    }

    public ServiceListWrapper()
    {
    }
}