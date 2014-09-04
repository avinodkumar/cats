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

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsResponseEvent;

public class PressKeyResponseEvent extends CatsResponseEvent
{
    private static final long serialVersionUID = -3043480532460641318L;
    private Settop            settop;
    private CatsEvent         event;
    private String            message;

    public PressKeyResponseEvent( Settop settop, CatsEvent event )
    {
        super();
        this.settop = settop;
        this.event = event;
    }

    public PressKeyResponseEvent( Settop settop, CatsEvent event, String message )
    {
        super();
        this.settop = settop;
        this.event = event;
        this.message = message;
    }
}
