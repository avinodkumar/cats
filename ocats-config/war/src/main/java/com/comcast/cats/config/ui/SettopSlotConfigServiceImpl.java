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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.utility.SlotConnectionConstructor;
import com.comcast.cats.config.utility.SlotConnectionRepresenter;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.SettopType;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.service.util.YAMLUtils;

@Named
@Singleton
public class SettopSlotConfigServiceImpl implements SettopSlotConfigService
{

    private static Logger      logger          = LoggerFactory.getLogger( SettopSlotConfigServiceImpl.class );

    List< SlotConnectionBean > slotConnections = null;
    List< SettopType >         settopTypes     = null;
    private String             configPath;
    private String             settopConfigPath;
    private String             settopTypeConfigPath;

    /**
     * The constructor that builds a slot represented in YAML.
     */
    @Inject
    SlotConnectionConstructor  slotConnectionConstructor;
    /**
     * The representer that determines how a slot should be represented in YAML.
     */
    @Inject
    SlotConnectionRepresenter  slotConnectionRepresenter;

    public SettopSlotConfigServiceImpl()
    {
    }

    @PostConstruct
    public void init()
    {
        configPath = System.getProperty( ConfigServiceConstants.CONFIG_PATH );
        
        if(configPath == null){
        	configPath = System.getenv(ConfigServiceConstants.CONFIG_ENV);
        }
        
        settopConfigPath = configPath + System.getProperty( "file.separator" )
                + SettopSlotConfigServiceImpl.SLOT_MAPPING_CONFIG;
        settopTypeConfigPath = configPath + System.getProperty( "file.separator" )
                + SettopSlotConfigServiceImpl.SETTOP_TYPE_MAPPING_CONFIG;
        try
        {
            readSettopsFromDisk();
        }
        catch ( IOException e )
        {
            logger.warn( "Settop Config File not available and could not be created : " + settopConfigPath + "  "
                    + e.getMessage() );
            slotConnections = new ArrayList< SlotConnectionBean >(); // start a
                                                                     // new map
            saveToConfigYAML(); // create YAML
        }
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "rackConfig YAML location " + settopConfigPath );
        }
    }

    protected synchronized void readSettopsFromDisk() throws IOException
    {
        slotConnections = YAMLUtils.loadFromYAML( settopConfigPath, slotConnections, slotConnectionConstructor );
        if ( slotConnections == null )
        {
            slotConnections = new ArrayList< SlotConnectionBean >(); // start a
                                                                     // new map
        }

        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            Slot slot = slotConnection.getSlot();
            if ( slot != null )
            {
                List< HardwareInterface > connections = new ArrayList< HardwareInterface >();
                Collection< HardwareInterface > hardwareConnections = slot.getConnections().values();

                for ( HardwareInterface connection : hardwareConnections )
                {
                    connections.add( connection );
                }
                slotConnection.getSettop().setHardwareInterfaces( connections );
            }
        }

        settopTypes = YAMLUtils.loadFromYAML( settopTypeConfigPath, settopTypes, null );
        if ( settopTypes == null )
        {
            settopTypes = new ArrayList< SettopType >(); // start a
                                                         // new map
        }
        logger.debug( "Settop Types  " + settopTypes );
    }

    @Override
    public List< SettopType > getAllSettopTypes()
    {
        refreshSettopTypeList();
        List< SettopType > retVal = new ArrayList< SettopType >();
        if ( settopTypes != null )
        {
            retVal.addAll( settopTypes );
        }
        return retVal;
    }

    private void refreshSettopTypeList()
    {
        try
        {
            settopTypes = YAMLUtils.loadFromYAML( settopTypeConfigPath, settopTypes, null );
            if ( settopTypes == null )
            {
                settopTypes = new ArrayList< SettopType >(); // start a
                                                             // new map
            }
        }
        catch ( IllegalArgumentException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public List< SlotConnectionBean > getAllConnectedSlots()
    {
        List< SlotConnectionBean > retVal = new ArrayList< SlotConnectionBean >();
        if ( slotConnections != null )
        {
            retVal.addAll( slotConnections );
        }
        return retVal;
    }

    @Override
    public SettopDesc findSettopByMac( String hostMacAddress )
    {
        SettopDesc retVal = null;
        for ( SlotConnectionBean slotConnection : slotConnections )
        {
        	
            if ( slotConnection.getSettop().getHostMacAddress() != null &&  slotConnection.getSettop().getHostMacAddress().equals( hostMacAddress ) )
            {
                retVal = slotConnection.getSettop();
            }
        }
        return retVal;
    }

    @Override
    public SettopDesc findSettopByName( String name )
    {
        SettopDesc retVal = null;
        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if ( slotConnection.getSettop().getName().equals( name ) )
            {
                retVal = slotConnection.getSettop();
            }
        }
        return retVal;
    }

    @Override
    public List< SettopReservationDesc > getAllSettops()
    {        
        List< SettopReservationDesc > settops = new ArrayList< SettopReservationDesc >();
        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if(slotConnection.getSettop() instanceof SettopReservationDesc)
            {
                settops.add( ( SettopReservationDesc ) slotConnection.getSettop() );
        
            }
        }
        return settops;
    }

    @Override
    public synchronized void refresh()
    {
        try
        {
            readSettopsFromDisk();
        }
        catch ( IOException e )
        {
            logger.error( "Settop Config File not available and could not be created " + settopConfigPath + "  "
                    + e.getMessage() );
        }

    }

    @Override
    public synchronized void saveSlotConnection( SlotConnectionBean slotConnection )
    {
        if ( !slotConnections.contains( slotConnection ) )
        {
            slotConnections.add( slotConnection );
        } // else // Slot connection already available. Just update YAML.
        saveToConfigYAML();
    }

    @Override
    public void saveSlotConnection( List< SlotConnectionBean > slotConnections )
    {
        if ( slotConnections != null )
        {
            for ( SlotConnectionBean slotConnection : slotConnections )
            {
                if ( !this.slotConnections.contains( slotConnection ) )
                {
                    this.slotConnections.add( slotConnection );
                }
            }
            saveToConfigYAML();
        }
    }

    @Override
    public void deleteSettopAndConnections( List< SlotConnectionBean > slotConnections )
    {
        if ( slotConnections != null )
        {
            for ( SlotConnectionBean slotConnection : slotConnections )
            {
                this.slotConnections.remove( slotConnection );
            }
            saveToConfigYAML();
        }
    }

    @Override
    public synchronized void deleteSettopAndConnection( SlotConnectionBean slotConnection )
    {
        slotConnections.remove( slotConnection );
        saveToConfigYAML();
    }

    @Override
    public List< Slot > getAllEmptySlots( Rack rack )
    {
        List< Slot > occuppiedSlots = new ArrayList< Slot >();
        List< Slot > availableSlots = new ArrayList< Slot >();
        if ( rack != null )
        {
            for ( SlotConnectionBean slotConnection : slotConnections )
            {
                if ( rack.equals( slotConnection.getRack() ) && slotConnection.getSlot() != null )
                {
                    occuppiedSlots.add( slotConnection.getSlot() );
                }
            }
            availableSlots = rack.getSlots();
            availableSlots.removeAll( occuppiedSlots );
        }
        return availableSlots;
    }

    @Override
    public boolean isMacAlreadyUsed( String macAddress )
    {
        boolean retVal = false;
        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if( slotConnection.getSettop() != null &&  slotConnection.getSettop().getHostMacAddress() != null){
                if ( slotConnection.getSettop().getHostMacAddress().equals( macAddress ) )
                {
                    retVal = true;
                }
            }
        }
        return retVal;
    }

    @Override
    public boolean isSettopNameAlreadyUsed( String name )
    {
        boolean retVal = false;
        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if ( slotConnection.getSettop().getName().equals( name ) )
            {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override
    public SlotConnectionBean getSlotConnection( String settopId )
    {
        SlotConnectionBean retVal = null;

        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if ( slotConnection.getSettop().getId().equals( settopId ) )
            {
                retVal = slotConnection;
            }
        }
        return retVal;
    }

    @Override
    public SlotConnectionBean getSlotConnection( Slot slot )
    {
        SlotConnectionBean retVal = null;

        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if ( slotConnection.getSlot() != null && slotConnection.getSlot().equals( slot ) )
            {
                retVal = slotConnection;
            }
        }
        return retVal;
    }

    private void saveToConfigYAML()
    {
        try
        {
            YAMLUtils.saveAsYAML( settopConfigPath, slotConnections, slotConnectionRepresenter );
        }
        catch ( FileNotFoundException e )
        {
            logger.error( "Settop config filepath is wrong " + settopConfigPath + " Exception : " + e.getMessage() );
        }
        refresh();
    }

    @Override
    public SettopType getSettopTypeByName( String name )
    {
        SettopType retVal = null;

        List< SettopType > settoptypes = getAllSettopTypes();
        for ( SettopType settopType : settoptypes )
        {
            if ( settopType.getName().equals( name ) )
            {
                retVal = settopType;
                break;
            }
        }

        return retVal;
    }

    @Override
    public List< SettopReservationDesc > getAllSettopsByRack(String rackName)
    {
        List< SettopReservationDesc > settops = new ArrayList< SettopReservationDesc >();
        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            if(slotConnection.getSettop() instanceof SettopReservationDesc 
                    && slotConnection.getRack() != null
                    && slotConnection.getRack().getName().equals( rackName ))
            {
                settops.add( ( SettopReservationDesc ) slotConnection.getSettop() );
            }
        }
        return settops;
    }
}
