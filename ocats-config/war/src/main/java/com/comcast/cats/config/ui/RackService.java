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
package com.comcast.cats.config.ui;

import java.util.List;

import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

/**
 * Rack Service
 * 
 * @author skurup00c
 * 
 */
public interface RackService
{
    /**
     * The filename of the yaml file
     */
    String RACK_CONFIG = "rackconfig.catsrack";

    /**
     * Get Racks list of all Racks in this deployment.
     * 
     * @return
     */
    List< Rack > getAllRacks();

    /**
     * Get a slot from a rack
     * 
     * @param rackName
     * @param slot
     * @return
     */
    Slot findSlotByRack( String rackName, Integer slot );

    /**
     * Find a rack by rack name.
     * 
     * @param name
     * @return
     */
    Rack findRack( String name );

    /**
     * Refresh rack list.
     */
    void refresh();

    /**
     * Save Rack configuration
     */
    void saveRackConfig( List< Rack > rackList );

    /**
     * Edit a slot
     * 
     * @param rackName
     * @param slotId
     * @param newSlot
     */
    void editSlot( String rackName, Integer slotId, Slot newSlot );
}
