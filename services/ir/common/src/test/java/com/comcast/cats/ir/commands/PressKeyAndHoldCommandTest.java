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
package com.comcast.cats.ir.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.RemoteCommand;

import static org.junit.Assert.*;

/**
 * This class will test PressKeyandHold, PressKey and IrCommand.
 * @author skurup00c
 *
 */
public class PressKeyAndHoldCommandTest
{
    PressKeyAndHoldCommand pressKeyandHoldCommand;
    String          irKeySet = "DTA";
    RemoteCommand rCommand = RemoteCommand.GUIDE;
    Integer count = 35;

    @Before
    public void setup()
    {
        pressKeyandHoldCommand = new PressKeyAndHoldCommand(rCommand,irKeySet,count);
    }

    @After
    public void tearDown()
    {
        pressKeyandHoldCommand = null;
    }

    @Test
    public void pressKeyandHoldCommandTest(){
        assertEquals( count, pressKeyandHoldCommand.getCount());
        assertEquals( irKeySet, pressKeyandHoldCommand.getIrKeySet());
        assertEquals( rCommand, pressKeyandHoldCommand.getRemoteCommand());
        
        irKeySet = "DTA-HD";
        rCommand = RemoteCommand.MENU;
        count = -32;
        pressKeyandHoldCommand.setCount( count );
        pressKeyandHoldCommand.setIrKeySet( irKeySet );
        pressKeyandHoldCommand.setRemoteCommand( rCommand );
        
        assertEquals( new Integer(0), pressKeyandHoldCommand.getCount());
        assertEquals( irKeySet, pressKeyandHoldCommand.getIrKeySet());
        assertEquals( rCommand, pressKeyandHoldCommand.getRemoteCommand());
    }
}
