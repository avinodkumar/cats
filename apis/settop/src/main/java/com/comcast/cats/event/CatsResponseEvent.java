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
 * Common event class for all CATS response events.
 * 
 * @author aswathyann
 * 
 */
public abstract class CatsResponseEvent extends CatsEvent
{
    /**
     * Serial version id
     */
    private static final long serialVersionUID = -8845953027718637938L;
    private static String     EMPTY_STRING     = "";
    private String            message          = EMPTY_STRING;

    /**
     * Constructor
     */
    public CatsResponseEvent()
    {

    }

    /**
     * Constructor
     * 
     * @param rhs
     *            -{@link CatsEvent}
     */
    public CatsResponseEvent( CatsEvent rhs )
    {
        this( rhs.sequence, rhs.sourceId, rhs.source, rhs.type );
    }

    /**
     * Constructor
     * 
     * @param sequence
     *            - Integer
     * @param sourceId
     *            - String
     * @param source
     *            - Object
     */
    public CatsResponseEvent( Integer sequence, String sourceId, Object source )
    {
        this( sequence, sourceId, source, CatsEventType.UNKOWN );
    }

    /**
     * Constructor
     * 
     * @param sequence
     *            - Integer
     * @param sourceId
     *            - String
     * @param source
     *            - Object
     * @param type
     *            - {@link CatsEventType}
     */
    public CatsResponseEvent( Integer sequence, String sourceId, Object source, CatsEventType type )
    {
        this.sequence = sequence;
        this.sourceId = sourceId;
        this.source = source;
        this.type = type;
    }

    /**
     * Constructor
     * 
     * @param sequence
     *            - Integer
     * @param sourceId
     *            - String
     * @param source
     *            - Object
     * @param type
     *            - {@link CatsEventType}
     * @param message
     *            - String
     */
    public CatsResponseEvent( Integer sequence, String sourceId, Object source, CatsEventType type, String message )
    {
        this( sequence, sourceId, source, type );
        this.message = message;
    }

    /**
     * To get the message
     * 
     * @return String message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * To set the message
     * 
     * @param message
     *            - String
     */
    public void setMessage( String message )
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "CatsResponseEvent [sequence=" + sequence + ", sourceId=" + sourceId + ", source=" + source + ", type="
                + type + "message = " + message + "]";
    }
}
