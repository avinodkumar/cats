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

import com.comcast.cats.domain.Slot;

/**
 * 
 * @author SSugun00c
 * 
 */
public class SlotMessage extends BaseMessage< Slot >
{
    private static final long serialVersionUID = 1L;

    private String            rackId;

    public SlotMessage()
    {
        super();
    }

    public SlotMessage( Slot slot, MessageType messageType, String rackId )
    {
        super( slot, messageType );
        this.rackId = rackId;
    }

    public Slot getSlot()
    {
        return domainObject;
    }

    public void setSlot( Slot slot )
    {
        this.domainObject = slot;
    }

    public String getRackId()
    {
        return rackId;
    }

    public void setRackId( String rackId )
    {
        this.rackId = rackId;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " []";
    }
}
