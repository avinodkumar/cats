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
package com.comcast.cats.service.impl;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;



/**
 * The Class IRCommunicatorTest.
 * 
 * @Author : Deepa
 * @since : 1st Nov 2012
 * @Description : The Class IRCommunicatorTest is the unit test of
 *              {@link IRCommunicator}.
 */
public class IRCommunicatorTest extends IRCommunicator
{
    /* Test object. */
    IRCommunicatorTest irComm;
    @BeforeMethod
    public void setUp() throws Exception
    {
        irComm = new IRCommunicatorTest();
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
    }

    @Override
    public boolean init()
    {
        return false;
    }

    @Override
    public void uninit()
    {
    }

    @Override
    public boolean transmitIR( String irCode, int port )
    {
        return false;
    }

    @Override
    public boolean transmitIR( String irCode, int port, int count, int offset )
    {
        return false;
    }
    
    /**
     * Test the getter and setter methods.
     */
    @Test
    public void testGetAndSetBeanValues()
    {
        String name = "dummy";
        irComm.setName( name );
        Assert.assertEquals( name, irComm.getName() );
        
    }

}
