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
import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventType;

public class RemoteResponseEvent extends RemoteEvent
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    protected boolean         response         = false;
    protected Settop          settop;

    public RemoteResponseEvent( RemoteEvent remoteEvent )
    {
        super( remoteEvent );
        this.setType( CatsEventType.REMOTE_RESPONSE );
    }

    public RemoteResponseEvent( RemoteEvent remoteEvent, boolean response )
    {
        super( remoteEvent );
        this.response = response;
        this.setType( CatsEventType.REMOTE_RESPONSE );
    }

    public RemoteResponseEvent( ActionType actionType, RemoteCommand remoteCommand, String sourceId, Object source )
    {
        super( actionType, remoteCommand, sourceId, source );
        this.setType( CatsEventType.REMOTE_RESPONSE );
    }

    public RemoteResponseEvent( ActionType actionType, RemoteCommand remoteCommand, String sourceId, Object source,
            int count )
    {
        super( actionType, remoteCommand, sourceId, source, count );
        this.setType( CatsEventType.REMOTE_RESPONSE );
    }

    public boolean isResponse()
    {
        return response;
    }

    public void setResponse( boolean response )
    {
        this.response = response;
    }

    public Settop getSettop()
    {
        return settop;
    }

    public void setSettop( Settop settop )
    {
        this.settop = settop;
    }
}
