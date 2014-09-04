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

import static com.comcast.cats.vision.panel.configuration.ConfigConstants.MAC_ID_END_INDEX;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.MAC_ID_START_INDEX;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import com.comcast.cats.domain.SettopReservationDesc;

/**
 * Model for ConfigPanel
 * 
 * @author aswathyann
 * 
 */
@Named
public class ConfigModel
{
    /**
     * macIdSet holds the mac ids highlighted by the user
     */
    private Set< String >                 highlightedRowIds           = new LinkedHashSet< String >();

    /**
     * highlightedRowMap holds the <search keyword> and the <highlighted settop+
     * reservation name> as <key, value> pair
     */
    private Map< String, Set< String >>   highlightedRowMap           = new LinkedHashMap< String, Set< String >>();
   
    /**
     * Holds AvailabeSettops hostmacAddress and SettopReservationDesc 
     */
    private Map< String, SettopReservationDesc>   availableSettops    = new HashMap< String, SettopReservationDesc>();
    
    /**
     * Holds AllocatedSettops hostmacAddress and SettopReservationDesc
     */
    private Map< String, SettopReservationDesc>   allocatedSettops    = new HashMap< String, SettopReservationDesc>();

    public Map< String, Set< String >> getHighlightedRowMap()
    {
        return highlightedRowMap;
    }

    public void setHighlightedRowMap( final Map< String, Set< String >> highlightedRowMap )
    {
        this.highlightedRowMap = highlightedRowMap;
    }

    public Set< String > getHighlightedRowIds()
    {
        highlightedRowIds.clear();
        for ( String key : highlightedRowMap.keySet() )
        {
            highlightedRowIds.addAll( highlightedRowMap.get( key ) );
        }
        return highlightedRowIds;
    }

    public Set< String > getHighlightedMacIds()
    {
        Set< String > highlightedRowIds = getHighlightedRowIds();

        Set< String > highlightedMacIds = new LinkedHashSet< String >();

        for ( String highlightedRowId : highlightedRowIds )
        {

            highlightedMacIds.add( highlightedRowId.substring( MAC_ID_START_INDEX, MAC_ID_END_INDEX ) );
        }

        return highlightedMacIds;
    }
  
    /**
     * Split each SettopReservationDesc in the list if it has more than one
     * ActiveReservations.
     * 
     * @param reservationDescs
     *            List of SettopReservationDesc
     * @return List of SettopReservationDesc
     */
    public List< SettopReservationDesc > splitSettopReservationDescBasedOnActiveReservations(
            final List< SettopReservationDesc > reservationDescs )
    {
        /*
       List< SettopReservationDesc > settopReservationDescList = new ArrayList< SettopReservationDesc >();

        for ( SettopReservationDesc settopReservationDesc : reservationDescs )
        {
            if ( settopReservationDesc.getActiveReservationList().size() == 1 )
            {
                settopReservationDescList.add( settopReservationDesc );
            }
            else if ( settopReservationDesc.getActiveReservationList().size() >= 2 )
            {
                for ( Reservation reservation : settopReservationDesc.getActiveReservationList() )
                {
                    SettopReservationDesc stbReservationDesc = new SettopReservationDesc( settopReservationDesc );

                    List< Reservation > activeReservationList = new ArrayList< Reservation >();
                    activeReservationList.add( reservation );
                    stbReservationDesc.setActiveReservationList( activeReservationList );

                    settopReservationDescList.add( stbReservationDesc );

                }
            }
        }*/
        
        return reservationDescs;
    }
  
    public void setAvailableSettops( Map< String, SettopReservationDesc> availableSettops )
    {
        this.availableSettops = availableSettops;
    }

    public Map< String, SettopReservationDesc> getAvailableSettops()
    {
        return availableSettops;
    }

    public void setAllocatedSettops( Map< String, SettopReservationDesc> allocatedSettops )
    {
        this.allocatedSettops = allocatedSettops;
    }

    public Map< String, SettopReservationDesc> getAllocatedSettops()
    {
        return allocatedSettops;
    }  
}
