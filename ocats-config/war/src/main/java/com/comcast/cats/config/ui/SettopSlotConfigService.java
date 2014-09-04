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

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.SettopType;
import com.comcast.cats.local.domain.Slot;

/**
 * To obtain settop information in deployed system.
 * 
 * @author skurup00c
 * 
 */
public interface SettopSlotConfigService
{

    /**
     * Rack Config YAML file
     */
    String SLOT_MAPPING_CONFIG        = "settops.catsrack";
    /**
     * Settop Type YAML file.
     */
    String SETTOP_TYPE_MAPPING_CONFIG = "settopTypes.catsrack";

    /**
     * Get the SlotConnectionBean for this settop.
     * 
     * @param settopId
     * @return
     */
    SlotConnectionBean getSlotConnection( String settopId );

    /**
     * Get the SlotConnectionBean for this Slot.
     */
    SlotConnectionBean getSlotConnection( Slot slot );

    /**
     * Save slotConnection information to YAML file
     */
    void saveSlotConnection( SlotConnectionBean slotConnection );

    /**
     * Save slotConnection information to YAML file
     */
    void saveSlotConnection( List< SlotConnectionBean > slotConnections );

    /**
     * Get all settops in the current deployment
     */
    List< SlotConnectionBean > getAllConnectedSlots();

    /**
     * Get all settops
     * 
     * @return
     */
    List< SettopReservationDesc > getAllSettops();

    /**
     * Find settop by macId
     */
    SettopDesc findSettopByMac( String hostMacAddress );

    /**
     * Find Settop by Name
     */
    SettopDesc findSettopByName( String name );

    /**
     * Is macAddress already used by another settop
     * 
     * @param macAddress
     * @return
     */
    boolean isMacAlreadyUsed( String macAddress );

    /**
     * Is settop name already used by another settop
     */
    boolean isSettopNameAlreadyUsed( String name );

    /**
     * getAllEmptySlots for a rack
     */
    List< Slot > getAllEmptySlots( Rack rack );

    /**
     * refresh data from YAML file
     */
    void refresh();

    /**
     * delete a slot connection.
     * 
     * @param slotConnection
     */
    void deleteSettopAndConnection( SlotConnectionBean slotConnection );

    /**
     * delete a list of slot connections
     * 
     * @param slotConnections
     */
    void deleteSettopAndConnections( List< SlotConnectionBean > slotConnections );

    /**
     * get all settop types.
     * 
     * @return
     */
    List< SettopType > getAllSettopTypes();

    /**
     * Get settop type by name.
     * 
     * @param name
     * @return
     */
    SettopType getSettopTypeByName( String name );

    /**
     * Get settops by name.
     *
     * @param rackName
     * @return
     */
    List< SettopReservationDesc > getAllSettopsByRack( String rackName );
}
