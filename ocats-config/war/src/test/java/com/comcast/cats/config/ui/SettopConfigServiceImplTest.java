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
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.RackServiceImpl;
import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.config.ui.SettopSlotConfigServiceImpl;
import com.comcast.cats.config.ui.SlotConnectionBean;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

public class SettopConfigServiceImplTest
{

    SettopSlotConfigServiceImpl settopSlotConfigService;

    String                      yamlFilePath  = "src/test/resources";
    String                      RACK_1        = "04-04-R09";
    String                      RACK_2        = "dummyrack";
    SettopReservationDesc       settop;
    Slot                        slot;
    Rack                        rack;
    String                      MAC_ADDRESS   = "12:23:34:45:45:45";
    String                      SETTOP_NAME   = "Settop";
    String                      ID            = "0";

    SettopReservationDesc       settop2;
    Slot                        slot2;
    Rack                        rack2;
    String                      MAC_ADDRESS_2 = "12:23:34:45:45:46";
    String                      SETTOP_NAME_2 = "Settop2";
    String                      ID_2          = "1";

    @Before
    public void setUp()
    {
        System.setProperty( ConfigServiceConstants.CONFIG_PATH, yamlFilePath );

        settopSlotConfigService = new SettopSlotConfigServiceImpl();
        settopSlotConfigService.init();

        settop = new SettopReservationDesc();
        settop.setId( ID );
        settop.setHostMacAddress( MAC_ADDRESS );
        settop.setName( SETTOP_NAME );

        settop2 = new SettopReservationDesc();
        settop2.setId( ID_2 );
        settop2.setHostMacAddress( MAC_ADDRESS_2 );
        settop2.setName( SETTOP_NAME_2 );

        String[] rackNames =
            { "04-04-R09", "dummyrack" };
        int[] noOfSlots =
            { 4, 4 };
        DevRack devRack = new DevRack();
        devRack.dumpRacksToFile( devRack.create( rackNames, noOfSlots ),
                yamlFilePath + System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );

        RackServiceImpl rackService = new RackServiceImpl();
        rackService.init();
        rack = rackService.findRack( RACK_1 );
        slot = rackService.findSlotByRack( RACK_1, 1 );

        rack2 = rackService.findRack( RACK_2 );
        slot2 = rackService.findSlotByRack( RACK_2, 2 );
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

            File file2 = new File( yamlFilePath + System.getProperty( "file.separator" )
                    + SettopSlotConfigService.SLOT_MAPPING_CONFIG );
            PrintWriter writer2 = new PrintWriter( file2 );
            writer2.print( "" );
            writer2.close();
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveSlotConnection()
    {
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        slotConnection.setRack( rack );
        slotConnection.setSettop( settop );
        slotConnection.setSlot( slot );
        settopSlotConfigService.saveSlotConnection( slotConnection );

        assertEquals( slotConnection, settopSlotConfigService.getSlotConnection( slot ) );
        assertEquals( slotConnection, settopSlotConfigService.getSlotConnection( ID ) );
    }

    @Test
    public void testSaveSlotConnections()
    {
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        slotConnection.setRack( rack );
        slotConnection.setSettop( settop );
        slotConnection.setSlot( slot );
        settopSlotConfigService.saveSlotConnection( slotConnection );

        SlotConnectionBean slotConnection2 = new SlotConnectionBean();
        slotConnection2.setRack( rack2 );
        slotConnection2.setSettop( settop2 );
        slotConnection2.setSlot( slot2 );

        settopSlotConfigService.saveSlotConnection( slotConnection2 );

        assertEquals( slotConnection2, settopSlotConfigService.getSlotConnection( slot2 ) );
        assertEquals( slotConnection2, settopSlotConfigService.getSlotConnection( ID_2 ) );
        List< SlotConnectionBean > settopConnections = settopSlotConfigService.getAllConnectedSlots();
        assertTrue( settopConnections.contains( slotConnection ) );
        assertTrue( settopConnections.contains( slotConnection2 ) );

        List< SettopReservationDesc > settops = settopSlotConfigService.getAllSettops();
        assertTrue( settops.contains( settop ) );
        assertTrue( settops.contains( settop2 ) );

        assertEquals( settop, settopSlotConfigService.findSettopByMac( settop.getHostMacAddress() ) );
        assertEquals( settop2, settopSlotConfigService.findSettopByMac( settop2.getHostMacAddress() ) );

        assertEquals( settop, settopSlotConfigService.findSettopByName( settop.getName() ) );
        assertEquals( settop2, settopSlotConfigService.findSettopByName( settop2.getName() ) );

        assertTrue( settopSlotConfigService.isMacAlreadyUsed( MAC_ADDRESS ) );
        assertTrue( settopSlotConfigService.isMacAlreadyUsed( MAC_ADDRESS_2 ) );
        assertFalse( settopSlotConfigService.isMacAlreadyUsed( "12:21:12:12" ) );

        assertTrue( settopSlotConfigService.isSettopNameAlreadyUsed( SETTOP_NAME ) );
        assertTrue( settopSlotConfigService.isSettopNameAlreadyUsed( SETTOP_NAME_2 ) );
        assertFalse( settopSlotConfigService.isSettopNameAlreadyUsed( "Sett" ) );

        List< Slot > slots = settopSlotConfigService.getAllEmptySlots( rack );
        assertEquals( 3, slots.size() );

        settopSlotConfigService.deleteSettopAndConnection( slotConnection2 );
        settopSlotConfigService.deleteSettopAndConnection( slotConnection );

        assertTrue( settopSlotConfigService.getAllConnectedSlots().isEmpty() );
        assertEquals( 4, settopSlotConfigService.getAllEmptySlots( rack2 ).size() );
    }
}