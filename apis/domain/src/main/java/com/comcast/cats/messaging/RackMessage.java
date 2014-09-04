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

import com.comcast.cats.domain.Rack;

/**
 * 
 * @author SSugun00c
 * 
 */
public class RackMessage extends BaseMessage< Rack >
{
    private static final long serialVersionUID = 1L;

    public RackMessage()
    {
        super();
    }

    public RackMessage( Rack rack, MessageType messageType )
    {
        super( rack, messageType );
    }

    public RackMessage( String id, String name, MessageType messageType, String userId, Date startDate, Date endDate )
    {
        super( id, name, messageType, userId, startDate, endDate );
    }

    public Rack getRack()
    {
        return domainObject;
    }

    public void setRack( Rack rack )
    {
        this.domainObject = rack;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " []";
    }

}
