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
package com.comcast.cats.vision.panel.configuration;

/**
 * The interface ConfigConstants contains common constants used in Configuration
 * module.
 * 
 * @author aswathyann
 * 
 */
public interface ConfigConstants
{

    /**
     * Allocated table headers
     */
    String[] ALLOCATED_TABLE_STRINGS =
                                         {
            "Id",
            "Name",
            "MAC Address",
            "IPAddress",
            "Reservation Name",
            "Rack Name",
            "Environment Name",
            "Content",
            "Model",

                                         };

    /**
     * Available table headers
     */
    String[] AVAILABLE_TABLE_STRINGS =
                                         { 
            "Id", 
            "Name", 
            "MAC Address", 
            "IPAddress",                        
            // "Reservation Name",
           // "Rack Name",
            // "Environment Name",
            "Content",
            "Model"                     };

    /**
     * Settop property table headers
     */
    String[] PROPERTY_VALUE_STRINGS  =
                                         { "Property", "Value" };

    /**
     * Constant for 'Available Settops'
     */
    String   AVAILABLE_SETTOPS       = "Available Settops";

    /**
     * Constant for 'Allocated Settops'
     */
    String   ALLOCATED_SETTOPS       = "Allocated Settops";

    /**
     * Constant for 'Search Settop'
     */
    String   SEARCH_SETTOP           = "Search Settop";

    /**
     * Constant for 'Refresh Settop List'
     */
    String   REFRESH_SETTOP_LIST     = "Refresh table";
    /**
     * Constant for 'Launch Video'
     */
    String   LAUNCH_VIDEO            = "Launch Video";

    /**
     * Constant for 'searchTextField'
     */
    String   SEARCH_TEXT_FIELD       = "searchTextField";

    int      MAC_ID_START_INDEX      = 0;

    int      MAC_ID_END_INDEX        = 17;

}
