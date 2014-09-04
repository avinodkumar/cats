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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.config.ui.SlotConnectionBean;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

public class SlotConnectionBeanTest
{
    SlotConnectionBean slotConnection;
    Rack rack ;
    Slot slot; 
    SettopDesc settop;
    @Before
    public void setup(){
        settop = new SettopDesc();
        settop.setId( "1" );
        rack = new Rack("rack",1);
        slot = new Slot(1);
        slot.setRack( rack );
        slotConnection = new SlotConnectionBean();
        slotConnection.setSettop( settop );
        slotConnection.setRack(rack );
        slotConnection.setSlot( slot );

    }
    
    @After
    public void tearDown(){
        slotConnection = null;
    }
    
    @Test
    public void equals()
    {
        assertTrue( slotConnection.equals( slotConnection ));
        
        SettopDesc settop = new SettopDesc();
        settop.setId( "1" );
        SlotConnectionBean slotConnection2 = new SlotConnectionBean();
        slotConnection2.setSettop( settop );
        slotConnection2.setRack( new Rack() );
        slotConnection2.setSlot( new Slot() );
        assertTrue( slotConnection.equals( slotConnection2 ));
        
        SettopDesc settop2 = new SettopDesc();
        settop2.setId( "2" );
        SlotConnectionBean slotConnection3 = new SlotConnectionBean();
        slotConnection3.setSettop( settop2 );
        slotConnection3.setRack( new Rack() );
        slotConnection3.setSlot( new Slot() );
        
        assertFalse( slotConnection.equals( slotConnection3 ));
    }

    @Test
    public void getListableSlots()
    {
        assertNotNull( slotConnection.getListableSlots() );
    }

    @Test
    public void getRack()
    {
        assertEquals( rack, slotConnection.getRack() );
    }

    @Test
    public void getSettop()
    {
        assertEquals( settop, slotConnection.getSettop() );
    }

    @Test
    public void getSlot()
    {
        assertEquals( slot, slotConnection.getSlot() );
    }


    @Test
    public void toStringTest()
    {
        assertNotNull( slotConnection.toString() );
        assertFalse( slotConnection.toString().isEmpty() );
    }
}