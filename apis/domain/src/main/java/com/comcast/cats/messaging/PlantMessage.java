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

import com.comcast.cats.domain.RFPlant;

/**
 * 
 * @author minu
 * 
 */
public class PlantMessage extends BaseMessage< RFPlant >
{

    private static final long serialVersionUID = 1L;

    public PlantMessage()
    {
        super();
    }

    public PlantMessage( RFPlant plant, MessageType messageType )
    {
        super( plant, messageType );
    }

    public PlantMessage( String id, String name, MessageType messageType )
    {
        super( id, name, messageType );
    }

    public RFPlant getPlant()
    {
        return domainObject;
    }

    public void setPlant( RFPlant plant )
    {
        this.domainObject = plant;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " []";
    }
}
