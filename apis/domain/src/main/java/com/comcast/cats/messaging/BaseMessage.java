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

import java.io.Serializable;
import java.util.Date;

import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.User;

/**
 * 
 * @author SSugun00c
 * 
 */
public class BaseMessage< Type > implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * UUID of {@link Domain} object
     */
    String                    id;

    /**
     * Name of {@link Domain} object
     */
    String                    name;

    /**
     * Type of message
     */
    MessageType               messageType;

    /**
     * UUID of {@link User}
     */
    String                    userId;

    /**
     * Start Date
     */
    Date                      startDate;

    /**
     * End Date
     */
    Date                      endDate;

    /**
     * Last modified date in source system
     */
    Date                      lastModifiedDate;

    /**
     * Revision in source system
     */
    Long                      revision;

    /**
     * Additional informatory messages.
     */
    String                    textMessage;

    /**
     * The domain object
     */
    Type                      domainObject;

    public BaseMessage()
    {
        super();
    }

    public BaseMessage( Type domain, MessageType messageType )
    {
        super();
        this.domainObject = domain;
        this.messageType = messageType;
    }

    public BaseMessage( String id, String name, MessageType messageType )
    {
        super();
        this.id = id;
        this.name = name;
        this.messageType = messageType;
    }

    public BaseMessage( String id, String name, MessageType messageType, String userId, Date startDate, Date endDate )
    {
        super();
        this.id = id;
        this.name = name;
        this.messageType = messageType;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BaseMessage( String id, String name, MessageType messageType, String userId, Date startDate, Date endDate,
            Date lastModifiedDate, Long revision )
    {
        super();
        this.id = id;
        this.name = name;
        this.messageType = messageType;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastModifiedDate = lastModifiedDate;
        this.revision = revision;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public MessageType getMessageType()
    {
        return messageType;
    }

    public void setMessageType( MessageType messageType )
    {
        this.messageType = messageType;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    public Date getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate( Date lastModifiedDate )
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getRevision()
    {
        return revision;
    }

    public void setRevision( Long revision )
    {
        this.revision = revision;
    }

    public String getTextMessage()
    {
        return textMessage;
    }

    public void setTextMessage( String textMessage )
    {
        this.textMessage = textMessage;
    }

    public Type getDomainObject()
    {
        return domainObject;
    }

    public void setDomainObject( Type domain )
    {
        this.domainObject = domain;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [id=" + getId() + ", name=" + getName() + ", messageType="
                + getMessageType() + ", userId=" + getUserId() + ", startDate=" + getStartDate() + ", endDate="
                + getEndDate() + ", lastModifiedDate=" + getLastModifiedDate() + ", revision=" + getRevision()
                + ", textMessage=" + getTextMessage() + "]";
    }
}
