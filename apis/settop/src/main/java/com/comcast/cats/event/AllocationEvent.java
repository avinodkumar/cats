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

public class AllocationEvent extends CatsEvent
{
    /**
     * serial Version ID
     */
    private static final long serialVersionUID = 3247882173121695013L;

    private final String      settopId;
    private final String      allocationId;
    private final boolean     broken;

    /**
     * Constructor
     * 
     * @param settopId
     * @param allocationId
     * @param broken
     *            - boolean
     * @param source
     */
    public AllocationEvent( String settopId, String allocationId, boolean broken, Object source )
    {
        super( 0, "", source, CatsEventType.ALLOCATION );
        this.settopId = settopId;
        this.allocationId = allocationId;
        this.broken = broken;
    }

    /**
     * to get settop Id
     * 
     * @return String
     */
    public String getSettopId()
    {
        return settopId;
    }

    /**
     * to get allocation Id
     * 
     * @return String
     */
    public String getAllocationId()
    {
        return allocationId;
    }

    /**
     * to get broken status
     * 
     * @return true if broken, false otherwise.
     */
    public boolean isBroken()
    {
        return broken;
    }
}
