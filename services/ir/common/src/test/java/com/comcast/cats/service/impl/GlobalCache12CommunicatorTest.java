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

import java.net.InetAddress;
import java.net.URI;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class GlobalCache12CommunicatorTest.
 * 
 * @Author : Deepa
 * @since : 31st Oct 2012
 * Description : The class GlobalCache12CommunicatorTest is the unit test of {@link GlobalCache12Communicator}
 */
public class GlobalCache12CommunicatorTest
{
    /* The test object. */
    GlobalCache12Communicator GC12Comm;
    /* The InetAddress. */
    InetAddress address;

    @BeforeMethod
    public void setUp() throws Exception
    {
       address = InetAddress.getByName( new URI("gc100-12://1.1.1.3/?port=1").getHost() ); 
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        GC12Comm = null;
    }

    /**
     * Test for creating outlet string from module and connector
     */
    @Test
    public void testCreateOutlet()
    {
        int port = 1;
       GC12Comm = new GlobalCache12Communicator( address ) ;
      String result =  GC12Comm.createOutlet( port );
      Assert.assertEquals( result, "4:1" );
    }
    
    /**
     * Negative test for creating outlet string from module and connector
     */
    @Test( expectedExceptions = IllegalArgumentException.class )
    public void testNegativeCreateOutlet()
    {
        int port = -1;
       GC12Comm = new GlobalCache12Communicator( address ) ;
       GC12Comm.createOutlet( port );
    }

    /* Negative test for constructor. */
    @Test( expectedExceptions = IllegalArgumentException.class )
    public void testNegativeGlobalCache12Communicator()
    {
        address = null;
        GC12Comm = new GlobalCache12Communicator( address );
    }

   
    
}
