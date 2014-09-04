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
package com.comcast.cats.vision.panel.videogrid.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import com.comcast.cats.Settop;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.vision.panel.videogrid.VideoPanel;

/**
 * Data model for handling Settops in CatsVision.
 * 
 * @author minu
 * 
 */
@Named
public class GridDataModel
{
    Map< String, CatsVisionSettop > settopsMap = new HashMap< String, CatsVisionSettop >();

    /**
     * Method to get all the settops in the model.
     * 
     * @return Set of Settops
     */
    public Set< Settop > getSettops()
    {
        Set< Settop > settops = new HashSet< Settop >();
        for ( CatsVisionSettop settop : settopsMap.values() )
        {
            settops.add( settop.getSettop() );
        }
        return settops;
    }

    /**
     * Clearing the existing model and adding new settops to model.
     * 
     * @param settops
     */
    public void setSettops( Set< Settop > settops )
    {
        settopsMap.clear();

        for ( Settop settop : settops )
        {
            settopsMap.put( settop.getHostMacAddress(), new CatsVisionSettop( settop ) );
            setAllocated( settop, true );
        }
    }

    /**
     * Add new settops to model without clearing the existing model
     * 
     * @param settops
     */
    public void addSettops( Set< Settop > settops )
    {
        for ( Settop settop : settops )
        {
            settopsMap.put( settop.getHostMacAddress(), new CatsVisionSettop( settop ) );
            setAllocated( settop, true );
        }
    }

    /**
     * Clearing the model
     */
    public void clearModel()
    {
        settopsMap.clear();
    }

    /**
     * To get the allocated settops.
     * 
     * @return Set of allocated Settops
     */
    public Set< Settop > getAllocatedSettops()
    {
        Set< Settop > allocatedSettops = new HashSet< Settop >();
        for ( CatsVisionSettop settop : settopsMap.values() )
        {
            if ( settop.isAllocated() )
            {
                allocatedSettops.add( settop.getSettop() );
            }
        }
        return allocatedSettops;
    }

    /**
     * To get the allocated and selected settops.
     * 
     * @return Set of allocated and selected Settops
     */
    public Set< Settop > getAllocatedAndSelectedSettops()
    {
        Set< Settop > allocatedAndSelectedSettops = new HashSet< Settop >();
        for ( CatsVisionSettop settop : settopsMap.values() )
        {
            if ( ( settop.isAllocated() ) && ( settop.isSelected() ) )
            {
                allocatedAndSelectedSettops.add( settop.getSettop() );
            }
        }
        return allocatedAndSelectedSettops;
    }

    /**
     * Adds Settop to the model
     * 
     * @param settop
     *            Settop instance
     */
    public void addSettop( Settop settop )
    {
        settopsMap.put( settop.getHostMacAddress(), new CatsVisionSettop( settop ) );
        setAllocated( settop, true );
    }

    public void removeSettop( Settop settop )
    {
        settopsMap.remove( settop.getHostMacAddress() );
    }

    /**
     * Method to get the VideoProviders of all settops in the model.
     * 
     * @return List of VideoProvider
     */
    public List< VideoProvider > getVideoProviders()
    {
        List< VideoProvider > videoProviders = new ArrayList< VideoProvider >();
        for ( CatsVisionSettop settop : settopsMap.values() )
        {
            videoProviders.add( settop.getSettop().getVideo() );
        }
        return videoProviders;
    }

    /**
     * Method to get the Settop instance corresponding to the hostMacAddress.
     * 
     * @param macID
     *            String hostMacAddress
     * @return Settop instance if there is a Settop present in the model with
     *         this macID. Else null.
     */
    public Settop getSettop( String macID )
    {
        Settop settop = null;

        for ( CatsVisionSettop stb : settopsMap.values() )
        {
            String settopMac = stb.getSettop().getHostMacAddress();
            if ( ( macID != null ) && ( macID.contains( settopMac ) ) )
            {
                settop = stb.getSettop();
                break;
            }
        }
        return settop;
    }

    /**
     * Method to get the names of all allocated Settops.
     * 
     * @return List of Settop Names
     */
    public List< String > getAllocatedSettopNames()
    {
        List< String > settopNameList = new LinkedList< String >();

        for ( Settop settop : getAllocatedSettops() )
        {
            settopNameList.add( settop.getHostMacAddress() );
        }

        return settopNameList;
    }

    /**
     * To get the allocated and selected settop names.
     * 
     * @return List of allocated and selected Settop names
     */
    public List< String > getAllocatedAndSelectedSettopNames()
    {
        List< String > settopNameList = new LinkedList< String >();

        for ( Settop settop : getAllocatedAndSelectedSettops() )
        {
            settopNameList.add( settop.getHostMacAddress() );
        }
        return settopNameList;
    }

    /**
     * Method to get the names of all launched Settops.
     * 
     * @return List of Settop Names
     */
    public List< String > getLaunchedSettopNames()
    {
        List< String > settopNameList = new LinkedList< String >();

        for ( Settop settop : getSettops() )
        {
            settopNameList.add( settop.getHostMacAddress() );
        }

        return settopNameList;
    }

    /**
     * Method to get the names of all given Settops.
     * 
     * @return List of Settop Names
     */
    public List< String > getSettopNames( Set< Settop > settops )
    {
        List< String > settopNameList = new LinkedList< String >();

        for ( Settop settop : settops )
        {
            settopNameList.add( settop.getHostMacAddress() );
        }

        return settopNameList;
    }

    /**
     * Method to get all allocated Settops which are having audio.
     * 
     * @return Set of Settop
     */
    public Set< Settop > getAllocatedSettopsWithAudio()
    {
        Set< Settop > settopsWithAudio = new HashSet< Settop >();

        for ( Settop settop : getAllocatedSettops() )
        {
            if ( settop.getAudio() != null )
            {
                settopsWithAudio.add( settop );
            }
        }
        return settopsWithAudio;
    }

    /**
     * Method to get all allocated Settops which are having trace.
     * 
     * @return Set of Settop
     */
    public Set< Settop > getAllocatedSettopsWithTrace()
    {
        Set< Settop > settopsWithTrace = new HashSet< Settop >();

        for ( Settop settop : getAllocatedSettops() )
        {
            if ( settop.getTrace() != null )
            {
                settopsWithTrace.add( settop );
            }
        }
        return settopsWithTrace;
    }

    /**
     * Method to mark a Settop's allocation status.
     * 
     * @param settop
     *            Settop instance
     * @param isAllocated
     *            True if is to be marked as allocated else false.
     */
    public void setAllocated( Settop settop, boolean isAllocated )
    {
        CatsVisionSettop catsVisionSettop = settopsMap.get( settop.getHostMacAddress() );
        catsVisionSettop.setAllocated( isAllocated );
    }

    /**
     * Method to get the allocation status of a Settop.
     * 
     * @param settop
     *            Settop instance
     * @return True if the Settop is allocated else False.
     */
    public boolean isAllocated( Settop settop )
    {
        CatsVisionSettop catsVisionSettop = settopsMap.get( settop.getHostMacAddress() );

        return catsVisionSettop.isAllocated();
    }

    /**
     * Method to get the selection status of a Settop.
     * 
     * @param settop
     *            Settop instance
     * @return true if the Settop is selected, else false.
     */
    public boolean isSelected( Settop settop )
    {
        CatsVisionSettop catsVisionSettop = settopsMap.get( settop.getHostMacAddress() );

        return catsVisionSettop.isSelected();
    }

    /**
     * Method to mark a Settop's selection status.
     * 
     * @param settop
     *            Settop instance
     * @param isSelected
     *            true if is to be marked as allocated, else false.
     */
    public void setSelected( Settop settop, boolean isSelected )
    {
        CatsVisionSettop catsVisionSettop = settopsMap.get( settop.getHostMacAddress() );
        catsVisionSettop.setSelected( isSelected );
    }


    /**
     * Method to get the VideoPanel for the Settop.
     * 
     * @param settop
     *            Settop
     * @return VideoPanel
     */
    public VideoPanel getVideoPanel( Settop settop )
    {
        CatsVisionSettop catsVisionSettop = settopsMap.get( settop.getHostMacAddress() );
        return catsVisionSettop.getVideoPanel();
    }

    /**
     * Method to set the VideoPanel for a Settop.
     * 
     * @param settop
     *            Settop
     * @param VideoPanel
     *            VideoPanel
     */
    public void setVideoPanel( Settop settop, VideoPanel videoPanel )
    {
        CatsVisionSettop catsVisionSettop = settopsMap.get( settop.getHostMacAddress() );
        catsVisionSettop.setVideoPanel( videoPanel );
    }

    /**
     * Method to get the VideoPanels of all settops in the model.
     * 
     * @return List of VideoPanel
     */
    public List< VideoPanel > getVideoPanels()
    {
        List< VideoPanel > videoPanels = new LinkedList< VideoPanel >();
        for ( CatsVisionSettop settop : settopsMap.values() )
        {
            videoPanels.add( settop.getVideoPanel() );
        }
        return videoPanels;
    }

    /**
     * Method to clear all the allocations on Settops.
     */
    public void clearAllSettopAllocations()
    {
        for ( CatsVisionSettop settop : settopsMap.values() )
        {
            settop.setAllocated( false );
        }
    }
}
