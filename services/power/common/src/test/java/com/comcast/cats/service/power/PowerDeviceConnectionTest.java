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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.easymock.EasyMock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class PowerDeviceConnectionTest.
 * 
 * @Author : Aneesh
 * @since : 21th Sept 2012
 * Description : The class PowerDeviceConnectionTest is the unit test of {@link PowerDeviceConnection}
 */

public class PowerDeviceConnectionTest {

    /** The pdc. */
    private PowerDeviceConnection pdc     = null;

    /** The host. */
    private String                host    = "1.1.1.1";

    /** The port. */
    private int                   port    = 5000;

    /** The Constant TIMEOUT. */
    private static final int      TIMEOUT = 5000;

    /**
     * Per test initialization.
     */
    @BeforeMethod
    public void init() 
    {
        pdc = new PowerDeviceConnection(host, port);
    }

    /**
     * Test valid constructor (String host).
     */
    @Test 
    public void testPowerDeviceConnectionConstructor() 
    {
        Assert.assertTrue( pdc instanceof PowerDeviceConnection, "Failed to get a valid instance of PowerDeviceConnection");
    }

    /**
     * Test valid constructor (InetAdddress host).
     */
    @Test 
    public void testPowerDeviceConnectionInetAddress() 
    {
        InetAddress address = null;
        try 
        {
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e) 
        {
            Assert.fail("Failed to get InetAddress for " + host + ".");
        }
        pdc = new PowerDeviceConnection(address, port);
        Assert.assertTrue(pdc instanceof PowerDeviceConnection, "Failed to get a valid instance of PowerControllerDeviceConnection");
    }


    /**
     * Test connection to an invalid host.
     */
    //@Test 
    public void testInvalidHostConnect() 
    {
        pdc = new PowerDeviceConnection("blah", 23);
        Assert.assertFalse( pdc.connect(TIMEOUT),"Failed to return false for invalid host.");
        pdc.close();
    }

    /**
     * Test connection to an invalid port.
     */
    @Test 
    public void testInvalidPortConnect() 
    {
        pdc = new PowerDeviceConnection(host, -1);
        Assert.assertFalse(pdc.connect(TIMEOUT), "Failed to return false for invalid port.");
        pdc.close();
    }

    /**
     * Test send cmd.
     */
    @Test
    public void testSendCmd() 
    {
        String cmd = "1";
        OutputStream stream= EasyMock.createMock(  OutputStream.class );
        Whitebox.setInternalState( pdc, "outToServer", stream );
        Assert.assertTrue( pdc.sendCmd( cmd, true ));
    }

    /**
     * Test send cmd end with new line echo false.
     */
    @Test
    public void testSendCmdEndWithNewLineEchoFlase() 
    {
        String cmd = "1\n";
        OutputStream stream= EasyMock.createMock(  OutputStream.class );
        Whitebox.setInternalState( pdc, "outToServer", stream );
        Assert.assertTrue( pdc.sendCmd( cmd, false ));
    }

    /**
     * Test send cmd when stream is null.
     */
    @Test
    public void testSendCmdStreamNull() 
    {
        String cmd = "1";
        OutputStream stream= null;
        Whitebox.setInternalState( pdc, "outToServer", stream );
        Assert.assertFalse( pdc.sendCmd( cmd, true ));
    }

    /**
     * Test has input stream.
     */
    @Test
    public void testHasInputStream() {
        BufferedReader stream= EasyMock.createMock(  BufferedReader.class );
        Whitebox.setInternalState( pdc, "inFromServer", stream );
        Assert.assertTrue( pdc.hasInputStream() );
    }

    /**
     * Test has input stream null.
     */
    @Test
    public void testHasInputStreamNull() {
        BufferedReader stream = null;
        Whitebox.setInternalState( pdc, "inFromServer", stream );
        Assert.assertFalse( pdc.hasInputStream() );
    }

    /**
     * Test is connected.
     */
    @Test
    public void testIsConnected() {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.isConnected() ).andReturn( true );
        EasyMock.expect( socket.isClosed() ).andReturn( false );
        Whitebox.setInternalState( pdc, "socket", socket );
        EasyMock.replay( socket );
        Assert.assertTrue( pdc.isConnected() );
    }

    /**
     * Test get host.
     */
    @Test
    public void testGetHost() {
        Assert.assertTrue( pdc.getHost().equals( host ) );
    }

    /**
     * Test get port.
     */
    @Test
    public void testGetPort() {
        Assert.assertTrue( pdc.getPort() == port );
    }

    /**
     * Test get inet address.
     */
    @Test
    public void testGetInetAddress() {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            Whitebox.setInternalState( pdc, "inetAddress", inetAddress );
            Assert.assertEquals( inetAddress, pdc.getInetAddress() );
        } catch (Exception e) {
            Assert.fail( "Exception........" );
        }
    }

    /**
     * Test is connected false.
     */
    @Test
    public void testIsConnectedFalse() {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.isConnected() ).andReturn( false );
        Whitebox.setInternalState( pdc, "socket", socket );
        EasyMock.replay( socket );
        Assert.assertFalse( pdc.isConnected() );
        EasyMock.verify( socket );
    }

    /**
     * Test is closed false.
     */
    @Test
    public void testIsClosedFalse() {
        Socket socket = EasyMock.createMock( Socket.class );
        EasyMock.expect( socket.isConnected() ).andReturn( false );
        Whitebox.setInternalState( pdc, "socket", socket );
        EasyMock.replay( socket );
        Assert.assertFalse( pdc.isConnected() );
        EasyMock.verify( socket );
    }

    /**
     * Test close.
     *
     * @throws Exception the exception
     */
    @Test
    public void testClose() throws Exception {
        Socket socket = mockSocket();
        EasyMock.expect( socket.isClosed() ).andReturn( true );
        EasyMock.replay( socket );
        pdc.close();
        EasyMock.verify( socket );
    }
    
    
    /**
     * Test close false.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCloseFalse() throws Exception {
        Socket socket = mockSocket();
        EasyMock.expect( socket.isClosed() ).andReturn( false );
        EasyMock.replay( socket );
        pdc.close();
        EasyMock.verify( socket );
    }
    
    /**
     * Test close socket null.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCloseSocketNull() throws Exception {
        Socket socket = null;
        BufferedReader stream= EasyMock.createMock(  BufferedReader.class );
        Whitebox.setInternalState( pdc, "inFromServer", stream );
        OutputStream streamOut = EasyMock.createMock(  OutputStream.class );
        Whitebox.setInternalState( pdc, "outToServer", streamOut );
        Whitebox.setInternalState( pdc, "socket", socket );
        pdc.close();
    }

    /**
     * Mock socket.
     *
     * @return the socket
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private Socket mockSocket() throws IOException
    {
        Socket socket = EasyMock.createMock( Socket.class );
        socket.close();
        EasyMock.expectLastCall();
        BufferedReader stream= EasyMock.createMock(  BufferedReader.class );
        Whitebox.setInternalState( pdc, "inFromServer", stream );
        OutputStream streamOut = EasyMock.createMock(  OutputStream.class );
        Whitebox.setInternalState( pdc, "outToServer", streamOut );
        Whitebox.setInternalState( pdc, "socket", socket );
        return socket;
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
        EasyMock.expect(inFromServer.read()).andReturn( 245 ).once();
        Whitebox.setInternalState( pdc, "socket", socket );
        Whitebox.setInternalState( pdc, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = pdc.waitForString( "", 500 );
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append( (char)245 );
        Assert.assertTrue( mockedResponse.equals( strBuilder.toString() ) );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test read io exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForStringIOException() throws Exception {
        checkExceptionForWaitForString(new IOException());
    }
    
    /**
     * Test read socket timeout exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testWaitForStringSocketTimeoutException() throws Exception {
        checkExceptionForWaitForString(new SocketTimeoutException ());
    }

    /**
     * Check exception for read.
     *
     * @param exp the exp
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void checkExceptionForWaitForString(Exception exp) throws IOException
    {
        Socket socket = EasyMock.createMock( Socket.class );
        BufferedReader inFromServer = EasyMock.createMock( BufferedReader.class );
        Whitebox.setInternalState( pdc, "inFromServer", inFromServer );
        EasyMock.expect( inFromServer.read() ).andThrow( exp );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = pdc.waitForString( "A", 500 );
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
        Whitebox.setInternalState( pdc, "inFromServer", inFromServer );
        EasyMock.expect( inFromServer.read() ).andThrow( exp );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = pdc.read(  500 );
        Assert.assertTrue( mockedResponse.isEmpty() );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }

    /**
     * Testset so timeout socket exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void testsetSoTimeoutSocketException() throws Exception {
        Socket socket = EasyMock.createMock( Socket.class );
        Whitebox.setInternalState( pdc, "socket", socket );
        EasyMock.expect( socket.getSoTimeout() ).andThrow( new SocketException() );
        EasyMock.replay( socket );
        pdc.setSoTimeout( 500 );
        EasyMock.verify( socket );
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
        EasyMock.expect(inFromServer.read()).andReturn( 245 ).once();
        EasyMock.expect(inFromServer.read()).andReturn( 675 ).once();
        EasyMock.expect(inFromServer.read()).andReturn( -1 ).once();
        Whitebox.setInternalState( pdc, "socket", socket );
        Whitebox.setInternalState( pdc, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = pdc.read( 500 );
        Assert.assertNotNull( mockedResponse);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append( (char)245 ).append( (char)675 );
        Assert.assertTrue( mockedResponse.equals( strBuilder.toString() ) );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
    
    /**
     * Test connect illegal argument exception.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConnectIllegalArgumentException() {
        pdc.setSoTimeout( -10 );
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
        EasyMock.expect(inFromServer.read()).andReturn( -1 ).once();
        Whitebox.setInternalState( pdc, "socket", socket );
        Whitebox.setInternalState( pdc, "inFromServer", inFromServer );
        EasyMock.replay( socket );
        EasyMock.replay( inFromServer );
        String mockedResponse = pdc.waitForString( "", 500 );
        Assert.assertTrue( mockedResponse.isEmpty() );
        EasyMock.verify( socket );
        EasyMock.verify( inFromServer );
    }
}
