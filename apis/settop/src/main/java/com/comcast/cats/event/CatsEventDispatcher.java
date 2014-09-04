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

import java.util.List;

public interface CatsEventDispatcher
{
    /**
     * Listen to all events of CatsEventType for all sources.
     * 
     * @param handler
     *            {@link CatsEventHandler}
     * @param type
     *            {@link CatsEventType}
     */
    public void addListener( CatsEventHandler handler, CatsEventType type );

    /**
     * Only listen to events from unique source specified by Object source.
     * 
     * @param handler
     *            {@link CatsEventHandler}
     * @param type
     *            {@link CatsEventType}
     * @param source
     */
    public void addListener( CatsEventHandler handler, CatsEventType type, Object source );

    /**
     * To add listener.
     * 
     * @param handler
     *            {@link CatsEventHandler}
     * @param types
     *            - List {@link CatsEventType}
     */
    public void addListener( CatsEventHandler handler, List< CatsEventType > types );

    /**
     * To send a catsEvent.
     * 
     * @param evt
     *            {@link CatsEvent}
     */
    public void sendCatsEvent( CatsEvent evt ) throws IllegalArgumentException;

    /**
     * To remove the listener.
     * 
     * @param handler
     *            {@link CatsEventHandler}
     */
    public void removeListener( CatsEventHandler handler );
}
