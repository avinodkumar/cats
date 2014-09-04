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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A user in the hardware reservation system.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement
public class User extends Domain
{
    private static final long serialVersionUID = 461102899507903809L;

    private String            username;
    private String            emailAddress;
    private String            firstName;
    private String            middleName;
    private String            lastName;
    private Boolean           isActive;

    private List< UserGroup > userGroups;

    public User()
    {
    }

    public User( String id )
    {
        super( id );
    }

    @XmlAttribute
    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    @XmlAttribute
    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress( String emailAddress )
    {
        this.emailAddress = emailAddress;
    }

    @XmlAttribute
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    @XmlAttribute
    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName( String middleName )
    {
        this.middleName = middleName;
    }

    @XmlAttribute
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    @XmlAttribute
    public Boolean isActive()
    {
        return isActive;
    }

    public void setActive( Boolean isActive )
    {
        this.isActive = isActive;
    }

    @XmlElementWrapper( name = "userGroups" )
    @XmlElement( name = "userGroup" )
    public List< UserGroup > getUserGroups()
    {
        return userGroups;
    }

    public void setUserGroups( List< UserGroup > userGroups )
    {
        this.userGroups = userGroups;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [username=" + getUsername() + ",  emailAddress="
                + getEmailAddress() + ",  firstName=" + getFirstName() + ",  middleName=" + getMiddleName()
                + ",  lastName=" + getLastName() + ",  isActive=" + isActive() + "]";
    }
}
