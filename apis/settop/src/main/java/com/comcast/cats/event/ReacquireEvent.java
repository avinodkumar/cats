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
 * Event generated when reacquired lock is modified.
 * 
 * @author bobyemmanuvel
 * 
 */
public class ReacquireEvent extends AllocationEvent
{
    /**
     * Serial version id
     */
    private static final long serialVersionUID = -8417962612541629040L;

    /**
     * Event generated when reacquired lock is modified.
     * 
     * @param settopId
     * @param allocationId
     * @param broken
     *            - true for broken allocation. false otherwise.
     * @param source
     *            - Object
     */
    public ReacquireEvent( String settopId, String allocationId, boolean broken, Object source )
    {
        super( settopId, allocationId, broken, source );
        this.setType( CatsEventType.REACQUIRE_EVENT );
    }
}
