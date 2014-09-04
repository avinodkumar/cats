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
package com.comcast.cats.event.impl;

import com.comcast.cats.event.CatsResponseEvent;

/**
 * Abstract class for task to be created for ManagedWorker.
 * 
 * @author sajayjk
 * 
 */
public abstract class AbstractManagedTask implements Runnable
{
    protected CatsResponseEvent responseEvent;

    /**
     * Get Identifier
     * 
     * @return Identifier
     */
    public abstract Object getIdentifier();

    /**
     * Get Response event
     * 
     * @return {@linkplain CatsResponseEvent}
     */
    public CatsResponseEvent getCatsResponseEvent()
    {
        return responseEvent;
    }

    /**
     * Set Response event
     * 
     * @param responseEvent {@linkplain CatsResponseEvent}
     */
    public void setCatsResponseEvent( CatsResponseEvent responseEvent )
    {
        this.responseEvent = responseEvent;
    }
}
