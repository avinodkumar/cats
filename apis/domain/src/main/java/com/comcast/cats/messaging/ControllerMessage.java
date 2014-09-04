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

import com.comcast.cats.domain.Controller;

/**
 * 
 * @author minu
 * 
 */
public class ControllerMessage extends BaseMessage< Controller >
{

    private static final long serialVersionUID = 1L;

    public ControllerMessage()
    {
        super();
    }

    public ControllerMessage( Controller controller, MessageType messageType )
    {
        super( controller, messageType );
    }

    public ControllerMessage( String id, String name, MessageType messageType )
    {
        super( id, name, messageType );
    }

    public Controller getController()
    {
        return domainObject;
    }

    public void setController( Controller controller )
    {
        this.domainObject = controller;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " []";
    }
}
