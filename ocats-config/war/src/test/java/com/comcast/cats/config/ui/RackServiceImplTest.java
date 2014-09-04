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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.RackServiceImpl;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

public class RackServiceImplTest
{

    RackServiceImpl rackService;
    String          yamlFilePath = "src/test/resources/";
    String          RACK_1       = "04-04-R09";
    String          RACK_2       = "dummyrack";

    @Before
    public void setUp()
    {
        System.setProperty( ConfigServiceConstants.CONFIG_PATH, yamlFilePath );

        String[] rackNames =
            { RACK_1, RACK_2 };
        int[] noOfSlots =
            { 4, 4 };
        DevRack devRack = new DevRack();
        devRack.dumpRacksToFile( devRack.create( rackNames, noOfSlots ),
                yamlFilePath + System.getProperty( "file.separator" ) + RackServiceImpl.RACK_CONFIG );
        rackService = new RackServiceImpl();
        rackService.init();
        // rackService.refresh();
    }

    @After
    public void tearDown()
    {
        try
        {
            File file = new File( yamlFilePath + System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );
            PrintWriter writer;
            writer = new PrintWriter( file );
            writer.print( "" );
            writer.close();
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllRacks()
    {
        List< Rack > racks = rackService.getAllRacks();
        assertNotNull( racks );
        assertFalse( racks.isEmpty() );
        for ( Rack rack : racks )
        {
            assertTrue( rack.getName().equals( RACK_1 ) || rack.getName().equals( RACK_2 ) );
        }
    }

    @Test
    public void testFindSlotByRack()
    {
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        assertNotNull( slot );
        assertEquals( 1, slot.getNumber().intValue() );
    }

    @Test
    public void testFindRack()
    {
        Rack rack = rackService.findRack( RACK_1 );
        assertNotNull( rack );
        assertEquals( RACK_1, rack.getName() );
    }

    @Test
    public void refresh()
    {
        rackService.refresh();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSaveRackConfig()
    {
        rackService.saveRackConfig( new ArrayList< Rack >() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testEditSlot()
    {
        rackService.editSlot( RACK_1, 0, new Slot() );
    }

}