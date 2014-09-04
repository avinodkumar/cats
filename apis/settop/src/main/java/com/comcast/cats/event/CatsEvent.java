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

import java.io.Serializable;

/**
 * Common event class for all CATS events.
 * 
 * @author cfrede001
 * 
 */
public abstract class CatsEvent implements Serializable, Cloneable
{
    /**
     * serial Version ID
     */
    private static final long serialVersionUID = -8845953027718637938L;
    /**
     * Added for completeness, but can be used to show order of events.
     */
    Integer                   sequence;
    /**
     * The source Id
     */
    String                    sourceId;

    /**
     * Source object for emanating event. This will typically be an object like
     * a Settop.
     */
    Object                    source;

    CatsEventType             type;

    /**
     * Constructor
     */
    public CatsEvent()
    {

    }

    /**
     * Constructor
     * 
     * @param rhs
     *            {@link CatsEvent}
     */
    public CatsEvent( CatsEvent rhs )
    {
        super();
        this.sequence = rhs.sequence;
        this.source = rhs.source;
        this.sourceId = rhs.sourceId;
        this.type = rhs.type;
    }

    /**
     * Constructor
     * 
     * @param sequence
     * @param sourceId
     * @param source
     */
    public CatsEvent( Integer sequence, String sourceId, Object source )
    {
        super();
        this.sequence = sequence;
        this.sourceId = sourceId;
        this.source = source;
        this.type = CatsEventType.UNKOWN;
    }

    /**
     * Constructor
     * 
     * @param sequence
     * @param sourceId
     * @param source
     * @param type
     *            {@link CatsEventType}
     */
    public CatsEvent( Integer sequence, String sourceId, Object source, CatsEventType type )
    {
        super();
        this.sequence = sequence;
        this.sourceId = sourceId;
        this.source = source;
        this.type = type;
    }

    /**
     * To get the sequence
     * 
     * @return Integer
     */
    public Integer getSequence()
    {
        return sequence;
    }

    /**
     * To set the sequence
     * 
     * @param sequence
     */
    public void setSequence( Integer sequence )
    {
        this.sequence = sequence;
    }

    /**
     * To get the sourceId
     * 
     * @return String
     */
    public String getSourceId()
    {
        return sourceId;
    }

    /**
     * To set the sourceId
     * 
     * @param sourceId
     */
    public void setSourceId( String sourceId )
    {
        this.sourceId = sourceId;
    }

    /**
     * To get the source
     * 
     * @return object
     */
    public Object getSource()
    {
        return source;
    }

    /**
     * To set the source
     * 
     * @param source
     */
    public void setSource( Object source )
    {
        this.source = source;
    }

    /**
     * To get the type
     * 
     * @return {@link CatsEvent}
     */
    public CatsEventType getType()
    {
        return type;
    }

    /**
     * To set the type
     * 
     * @param type
     *            {@link CatsEventType}
     */
    public void setType( CatsEventType type )
    {
        this.type = type;
    }

    /**
     * To get the details
     * 
     * @return String
     */
    @Override
    public String toString()
    {
        return "CatsEvent [sequence=" + sequence + ", sourceId=" + sourceId + ", source=" + source + ", type=" + type
                + "]";
    }
}
