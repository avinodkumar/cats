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
package com.comcast.cats.messaging;

import java.util.Date;

import com.comcast.cats.domain.User;

/**
 * 
 * @author SSugun00c
 * 
 */
public class UserMessage extends BaseMessage< User >
{
    private static final long serialVersionUID = 1L;

    public UserMessage()
    {
        super();
    }

    public UserMessage( User user, MessageType messageType )
    {
        super( user, messageType );
    }

    public UserMessage( String id, String name, MessageType messageType, String userId, Date startDate, Date endDate )
    {
        super( id, name, messageType, userId, startDate, endDate );
    }
    
    public User getUser()
    {
        return domainObject;
    }

    public void setUser( User user )
    {
        this.domainObject = user;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " []";
    }
}
