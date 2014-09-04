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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.AssertJUnit.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.service.IRService;

/**
 * The Class GlobalCache6CommunicatorTest.
 * 
 * @Author : Deepa
 * @since : 10th Sept 2012
 * @Description : The Class GlobalCache6CommunicatorTest is the unit test of
 *              {@link GlobalCache6Communicator}.
 */
public class GlobalCache6CommunicatorTest
{

    /* Test object.*/
    GlobalCache6Communicator gc100;
    InetAddress address;

    @BeforeMethod
    public void setUp() throws UnknownHostException
    {
        address = InetAddress.getByName( "1.1.1.1" );
        gc100 = new GlobalCache6Communicator( address );
    }

    /* Negative test the constructor. */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNegativeGlobalCache6Communicator(){
        gc100 = new GlobalCache6Communicator( null );
    }
    
    /* Test the constructor. */
    @Test
    public void testGlobalCache6Communicator() {
        try
        {
            InetAddress address = InetAddress.getByName( "1.1.1.1" );
            gc100 = new GlobalCache6Communicator( address );
            AssertJUnit.assertEquals( Whitebox.getInternalState( gc100, "m_ip" ), address );
        }
        catch ( UnknownHostException e )
        {
            Assert.fail( "Exception:"+ e );
        }
        
    }
    
    /* TODO Test the initialization of communicator*/
    @Test(enabled = false)
    public void testInit(){
    }
    
    /* TODO Test the finalization of communicator*/
    @Test(enabled = false)
    public void testUnInit(){
        
    }
    
    @Test
    public void tesTransmitIR() throws Exception{
        
        String irCode = "0000 006C 0012 0002";
        String outlet = "2:1";
        int count = 2;
        int offset = 2;
        String response = null;
        
        Socket socket = EasyMock.createMock( Socket.class );
        Whitebox.setInternalState( gc100, "m_socket", socket );  
        BufferedReader buffReader = EasyMock.createMock( BufferedReader.class );
        GlobalCacheCode gcc = EasyMock.createMock( GlobalCacheCode.class );
        OutputStream out = new FileOutputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        Whitebox.setInternalState( gc100, "m_sos", out );
        out.flush();
        EasyMock.expect(buffReader.readLine()).andReturn(response);
        Whitebox.setInternalState( gc100, "m_sis", buffReader );
        EasyMock.expect( socket.getInetAddress() ).andReturn( null );
        EasyMock.replay( socket, buffReader, gcc );
    }
    
    //TODO
    @Test(enabled = false)
    public void TestTransmitIRWithIRCodeAndPort(){
    }
    
    @Test
    public void TestNegativeTransmitIRWithIRCodeAndPort(){
        
        String irCode = null;
        int port = 1;
        Assert.assertFalse(gc100.transmitIR( irCode, port ));
    }
    
    //TODO
    @Test(enabled = false)
    public void TestTransmitIRWithIRCodePortCountOffset(){
    }
    
    @Test
    public void TestNegativeTransmitIRWithCodePortCountOffset(){
        String irCode = null;
        int port = 1;
        int count = 2;
        int offset = 2;
        Assert.assertFalse(gc100.transmitIR( irCode, port, count, offset ));
    }
    
    /* Test creation of outlet string from module and connector*/
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createOutlet(){
        int port = 1;
        Assert.assertEquals( gc100.createOutlet( port ), "2:1" );
        port = 6;
        gc100.createOutlet( port );
    }
    

   
}
