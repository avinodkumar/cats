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
package com.comcast.cats.event;

/**
 * Class for the TraceEvent
 * 
 */
public class TraceEvent extends CatsEvent
{
    /**
     * Serial version id
     */
    private static final long serialVersionUID = -1902866908981219457L;

    String                    payload;

    /**
     * Constructor
     */
    public TraceEvent()
    {
        super( -1, "noSource", null, CatsEventType.TRACE );
    }

    /**
     * Constructor
     * 
     * @param sequence
     * @param sourceId
     * @param source
     */
    public TraceEvent( Integer sequence, String sourceId, Object source )
    {
        super( sequence, sourceId, source, CatsEventType.TRACE );
    }

    /**
     * Constructor
     * 
     * @param sequence
     * @param sourceId
     * @param source
     * @param payload
     */
    public TraceEvent( Integer sequence, String sourceId, Object source, String payload )
    {
        super( sequence, sourceId, source, CatsEventType.TRACE );
        this.payload = payload;
    }

    /**
     * To get the payload
     * 
     * @return payload as String
     */
    public String getPayload()
    {
        return payload;
    }

    /**
     * To set the payload
     * 
     * @param payload
     *            as String
     */
    public void setPayload( String payload )
    {
        this.payload = payload;
    }

    @Override
    public String toString()
    {
        return "TraceEvent [payload=" + payload + super.toString() + "]";
    }
}
