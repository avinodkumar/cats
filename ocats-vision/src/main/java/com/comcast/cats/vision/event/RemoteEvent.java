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
package com.comcast.cats.vision.event;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventType;

public class RemoteEvent extends CatsEvent
{
    private static final long serialVersionUID = 4010835911311383551L;
    private ActionType        actionType;
    private RemoteCommand     remoteCommand;
    private static int        eventCount       = 0;
    private int               count;
    private String 			  channelNumber=null;
    public RemoteEvent(RemoteEvent rhs) {
    	super(rhs);
    	this.actionType = rhs.actionType;
    	this.remoteCommand = rhs.remoteCommand;
    	this.eventCount = rhs.eventCount;
    	this.count = rhs.count;
    	this.channelNumber=rhs.channelNumber;
    }
    
    public RemoteEvent( ActionType actionType, RemoteCommand remoteCommand, String sourceId, Object source )
    {
        super( ++eventCount, sourceId, source,CatsEventType.REMOTE );
        this.actionType = actionType;
        this.remoteCommand = remoteCommand;
    }

    public RemoteEvent( ActionType actionType, RemoteCommand remoteCommand, String sourceId, Object source, int count )
    {
        super( ++eventCount, sourceId, source,CatsEventType.REMOTE );
        this.actionType = actionType;
        this.remoteCommand = remoteCommand;
        this.count = count;
    }
    public RemoteEvent( ActionType actionType,String channelNumber, String sourceId, Object source )
    {
        super( ++eventCount, sourceId, source,CatsEventType.REMOTE );
        this.actionType = actionType;
        this.remoteCommand = remoteCommand;
        this.channelNumber=channelNumber;
        this.count = count;
    }

    public ActionType getActionType()
    {
        return actionType;
    }

    public void setActionType( ActionType actionType )
    {
        this.actionType = actionType;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }

    public RemoteCommand getRemoteCommand()
    {
        return remoteCommand;
    }

    public void setRemoteCommand( RemoteCommand remoteCommand )
    {
        this.remoteCommand = remoteCommand;
    }
    public String getChannelNumber()
    {
    	return channelNumber;
    }
}
