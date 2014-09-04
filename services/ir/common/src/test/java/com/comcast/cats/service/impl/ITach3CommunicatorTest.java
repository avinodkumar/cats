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
import java.net.UnknownHostException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class ITach3CommunicatorTest.
 * 
 * @Author : Deepa
 * @since : 31st Oct 2012
 * @Description : The Class ITach3CommunicatorTest is the unit test of
 *              {@link ITach3Communicator}.
 */
public class ITach3CommunicatorTest
{
    /* The test object. */
    ITach3Communicator ITach3Comm;
    /* The host. */
    private String                host    = "1.1.1.1";
    
    
    
    @BeforeMethod
    public void setUp() throws Exception
    {
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testCreateOutlet()
    {
        int port = 1;
        InetAddress inetAddress;
        try
        {
            inetAddress = InetAddress.getByName(host);
            ITach3Comm = new ITach3Communicator( inetAddress ) ;
            String result =  ITach3Comm.createOutlet( port );
            Assert.assertEquals( result, "1:1" );
        }
        catch ( UnknownHostException e )
        {
            Assert.fail( "Exception:"+ e );
        }
        
    }
    
    @Test( expectedExceptions = IllegalArgumentException.class )
    public void testNegativeCreateOutlet()
    {
        int port = 9;
        InetAddress inetAddress;
        try
        {
            inetAddress = InetAddress.getByName(host);
            ITach3Comm = new ITach3Communicator( inetAddress ) ;
            ITach3Comm.createOutlet( port );
        }
        catch ( UnknownHostException e )
        {
            Assert.fail( "Exception:"+ e );
        }
        
    }

    @Test( expectedExceptions = IllegalArgumentException.class )
    public void testITach3Communicator()
    {
        InetAddress inetAddress = null;
        ITach3Comm = new ITach3Communicator( inetAddress );
    }

}
