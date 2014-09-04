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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.service.util.YAMLUtils;

/**
 * Rack Service Impl
 * 
 * @author skurup00c
 * 
 */
@Named
@Singleton
public class RackServiceImpl implements RackService
{

    private static Logger logger = LoggerFactory.getLogger( RackServiceImpl.class );
    /**
     * List of racks available in this deployment.
     */
    List< Rack >          racks  = new ArrayList< Rack >();

    /**
     * Location where config information for CATS will be stored. This could
     * be split out into it's own service.
     */
    private String        configPath;
    private String        rackConfigPath;

    public RackServiceImpl()
    {
        configPath = System.getProperty( ConfigServiceConstants.CONFIG_PATH );
        if(configPath == null){
        	configPath = System.getenv(ConfigServiceConstants.CONFIG_ENV);
        }
        rackConfigPath = configPath + System.getProperty( "file.separator" ) + RackService.RACK_CONFIG;
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "rackConfig YAML location " + rackConfigPath );
        }
    }

    @PostConstruct
    public void init()
    {
        try
        {
            readRacksFromDisk();
        }
        catch ( IOException e )
        {
            logger.error( e.getMessage() );
        }
    }

    protected void readRacksFromDisk() throws IOException
    {
        racks = YAMLUtils.loadFromYAML( rackConfigPath, racks, null );
    }

    @Override
    public List< Rack > getAllRacks()
    {
        return racks;
    }

    @Override
    public Rack findRack( String name )
    {
        Rack retVal = null;
        if ( name != null && !name.isEmpty() )
        {
            for ( Rack rack : racks )
            {
                if ( rack.getName().equalsIgnoreCase( name ) )
                {
                    retVal = rack;
                    break;
                }
            }
        }
        return retVal;
    }

    @Override
    public Slot findSlotByRack( String rackName, Integer slotId )
    {
        Slot retVal = null;
        Rack rack = findRack( rackName );
        if ( rack != null && slotId <= rack.getSlots().size() )
        {
            for ( Slot slot : rack.getSlots() )
            {
                if ( slot.getNumber().equals( slotId ) )
                {
                    retVal = slot;
                    break;
                }
            }
        }
        return retVal;
    }

    @Override
    public void refresh()
    {
        try
        {
            readRacksFromDisk();
        }
        catch ( IOException e )
        {
            logger.error( "Rack YAML file missing at " + rackConfigPath + " and could not be created" );
            e.printStackTrace();
        }
    }

    @Override
    public void saveRackConfig( List< Rack > rackList )
    {
        throw new UnsupportedOperationException( "Not supported as of now" );
    }

    @Override
    public synchronized void editSlot( String rackName, Integer slotId, Slot newSlot )
    {
        throw new UnsupportedOperationException( "Not supported as of now" );
    }
}
