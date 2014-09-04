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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A simple wrapper class for {@link List} interface. List is not directly
 * supported in JAX-WS or JAXB. A work around is to use a serialized wrapper of
 * the list instead.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement( name = "list" )
public class SimpleListWrapper< T > implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 845626611933942469L;

    protected List< T >       list;

    @XmlElement( name = "item" )
    public List< T > getList()
    {
        return list;
    }

    public void setList( List< T > list )
    {
        this.list = list;
    }

    public SimpleListWrapper()
    {
    }

    public SimpleListWrapper( List< T > list )
    {
        this.list = list;
    }
}
