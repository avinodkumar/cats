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
package com.comcast.cats.service.power;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.easymock.EasyMock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class PowerDeviceConnectionNP16STest.
 */
public class PowerDeviceConnectionNP16STest
{
    
    /** The power device. */
    private PowerDeviceConnectionNP16S powerDevice;
    
    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        String host = "1.1.1.1";
        int port = 5000;
        powerDevice = new PowerDeviceConnectionNP16S(host, port);
    }

    /**
     * Test read.
     *
     * @throws Exception the exception
     */
    @Test
    public void testRead() throws Exception {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.getSoTimeout() ).andReturn( 500 );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        EasyMock.expect( inFromServer.ready() ).andReturn( true ).times( 2 );
        EasyMock.expect( inFromServer.ready() ).andReturn( false ).once();
        EasyMock.expect(inFromServer.read()).andReturn( 245 ).once();
        EasyMock.expect(inFromServer.read()).andReturn( 675 ).once();
        Whitebox.setInternalState( powerDevice, "socket", socket );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.read( 500 );
        Assert.assertNotNull( mockedResponse);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append( (char)245 ).append( (char)675 );
        Assert.assertTrue( mockedResponse.equals( strBuilder.toString() ) );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test read nothing to read.
     *
     * @throws Exception the exception
     */
    @Test
    public void testReadNothingToRead() throws Exception {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.getSoTimeout() ).andReturn( 500 );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        EasyMock.expect( inFromServer.ready() ).andReturn( true ).once();
        EasyMock.expect(inFromServer.read()).andReturn( -1 ).once();
        Whitebox.setInternalState( powerDevice, "socket", socket );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.read( 500 );
        Assert.assertNotNull( mockedResponse);
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }

    /**
     * Test wait for string.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForString() throws Exception {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.getSoTimeout() ).andReturn( 500 );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        EasyMock.expect( inFromServer.ready() ).andReturn( true );
        EasyMock.expect(inFromServer.read()).andReturn( 245 ).once();
        Whitebox.setInternalState( powerDevice, "socket", socket );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.waitForString( "", 500 );
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append( (char)245 );
        Assert.assertTrue( mockedResponse.equals( strBuilder.toString() ) );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test wait for string not ready.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForStringNotReady() throws Exception {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.getSoTimeout() ).andReturn( 500 );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        EasyMock.expect( inFromServer.ready() ).andReturn( false );
        Whitebox.setInternalState( powerDevice, "socket", socket );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.waitForString( "", 500 );
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append( "" );
        Assert.assertTrue( mockedResponse.equals( strBuilder.toString() ) );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test wait for string null.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForStringNull() throws Exception {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.getSoTimeout() ).andReturn( 500 );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        EasyMock.expect( inFromServer.ready() ).andReturn( true );
        EasyMock.expect(inFromServer.read()).andReturn( -1 ).once();
        Whitebox.setInternalState( powerDevice, "socket", socket );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.waitForString( "", 500 );
        Assert.assertTrue( mockedResponse.isEmpty() );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test read io exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testReadIOException() throws Exception {
        checkExceptionForRead(new IOException());
    }
    
    /**
     * Test read socket timeout exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testReadSocketTimeoutException() throws Exception {
        checkExceptionForRead(new SocketTimeoutException ());
    }

    /**
     * Check exception for read.
     *
     * @param exp the exp
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void checkExceptionForRead(Exception exp) throws IOException
    {
        Socket socket = EasyMock.createMock( Socket.class );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.expect( inFromServer.ready() ).andThrow( exp );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.read( 500 );
        Assert.assertTrue( mockedResponse.isEmpty() );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test wait for string socket timeout exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForStringSocketTimeoutException() throws Exception {
        verifyException(new SocketTimeoutException());
    }
    
    /**
     * Test wait for string socket io exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForStringSocketIOException() throws Exception {
        verifyException(new IOException());
    }

    /**
     * Verify exception.
     *
     * @param excep the excep
     * @throws SocketException the socket exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void verifyException(Exception excep) throws SocketException, IOException
    {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.getSoTimeout() ).andReturn( 500 );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        EasyMock.expect( inFromServer.ready() ).andThrow( excep );
        Whitebox.setInternalState( powerDevice, "socket", socket );
        Whitebox.setInternalState( powerDevice, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = powerDevice.waitForString( "", 500 );
        Assert.assertTrue( mockedResponse.isEmpty() );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
}
