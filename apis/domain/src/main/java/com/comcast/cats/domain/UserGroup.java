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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A group of users, who are allowed to allocate {@link SettopDesc} for a given
 * {@link Reservation}.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class UserGroup extends Domain
{
    private static final long serialVersionUID = 3186116698500310770L;

    private List< User >      users;

    public UserGroup()
    {

    }

    public UserGroup( String id )
    {
        super( id );
    }

    public UserGroup( String id, String name )
    {
        super( id, name );
    }

    @XmlElementWrapper( name = "users" )
    @XmlElement( name = "user" )
    public List< User > getUsers()
    {
        return users;
    }

    public void setUsers( List< User > users )
    {
        this.users = users;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [users=" + getUsers() + "]";
    }
}
