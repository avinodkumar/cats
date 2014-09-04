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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import org.apache.commons.net.telnet.TelnetClient;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.service.power.util.PowerConstants;

/**
 * The Class WTI_NPS_1600_PowerDeviceTest.
 * 
 * @Author : Aneesh
 * @since : 18th Sept 2012
 * Description : The class WTI_NPS_1600_PowerDeviceTest is the unit test of {@link WTI_NPS_1600_PowerDevice}
 */

public class WTI_NPS_1600_PowerDeviceTest
{
    
    /** The nps power device. */
    private WTI_NPS_1600_PowerDevice npsPowerDevice;
    
    /** The user name. */
    String userName = "";
    
    /** The password. */
    String password = "";
    
    /** The host. */
    String host = "";
    
    /** The port. */
    int port = 0;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        npsPowerDevice = new WTI_NPS_1600_PowerDevice(host, port, userName, password);
    }

    /**
     * Test wt i_ np s_1600_ power device constructor.
     */
    @Test
    public void testWTI_NPS_1600_PowerDeviceConstructor() 
    {
        Assert.assertNotNull( npsPowerDevice );
        Assert.assertEquals( host, Whitebox.getInternalState( npsPowerDevice, "host" ) );
        Assert.assertEquals( port, Whitebox.getInternalState( npsPowerDevice, "port" ) );
        Assert.assertEquals( userName, Whitebox.getInternalState( npsPowerDevice, "username" ) );
        Assert.assertEquals( password, Whitebox.getInternalState( npsPowerDevice, "password" ) );
    }

    /**
     * Test send cmd.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSendCmd() throws Exception {
        //Creating a fileoutput stream in order to test the behaviour of sendCmd().
        OutputStream out = new FileOutputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        Whitebox.setInternalState( npsPowerDevice, "out", out );
        Assert.assertTrue(npsPowerDevice.sendCmd( "ON", true ));
    }
    
    @Test
    public void testSendCmdEndWithNewLine() throws Exception {
        //Creating a fileoutput stream in order to test the behaviour of sendCmd().
        OutputStream out = new FileOutputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        Whitebox.setInternalState( npsPowerDevice, "out", out );
        Assert.assertTrue(npsPowerDevice.sendCmd( "ON\r\n", false ));
    }

    /**
     * Test send cmd stream null.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSendCmdStreamNull() throws Exception {
        OutputStream out = null;
        Whitebox.setInternalState( npsPowerDevice, "out", out );
        Assert.assertFalse(npsPowerDevice.sendCmd( "ON", true ));
    }
    

    /**
     * Test create power dev conn.
     *
     * @throws Exception the exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test(expectedExceptions = UnableToCreatePowerControllerDevice.class)
    @PrepareForTest(WTI_NPS_1600_PowerDevice.class)
    public void testCreatePowerDevConn() throws Exception, IOException {
        TelnetClient client = EasyMock.createMock( TelnetClient.class );
        client.connect( "1.1.1.1", 5000 );
        EasyMock.expectLastCall();
        Whitebox.setInternalState( npsPowerDevice, "client", client );
        EasyMock.replay( client );
        PowerMock.expectNew(TelnetClient.class, EasyMock.anyObject(TelnetClient.class)).andReturn(client).times(4);
        npsPowerDevice.createPowerDevConn();
    }

    /**
     * Test logout.
     *
     * @throws Exception the exception
     */
    @Test
    public void testLogout() throws Exception {
        TelnetClient client = EasyMock.createMock( TelnetClient.class );
        client.disconnect();
        EasyMock.expectLastCall();
        Whitebox.setInternalState( npsPowerDevice, "client", client );
        EasyMock.replay( client );
        Assert.assertTrue( npsPowerDevice.logout());
        EasyMock.verify( client );
    }

    /**
     * Test get outlet status.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetOutletStatus() throws Exception {
        //Creating a fileoutput stream in order to test the behaviour of sendCmd().
        initilizeStreams();
        npsPowerDevice.getOutletStatus( 2 );
    }

    /**
     * Initilize streams.
     *
     * @throws FileNotFoundException the file not found exception
     * @throws URISyntaxException the uRI syntax exception
     */
    private void initilizeStreams() throws FileNotFoundException, URISyntaxException
    {
        OutputStream out = new FileOutputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        InputStream in = new FileInputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        Whitebox.setInternalState( npsPowerDevice, "out", out );
        Whitebox.setInternalState( npsPowerDevice, "in", in );
    }

    /**
     * Test power on.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPowerOn() throws Exception {
        InputStream in = new FileInputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        Whitebox.setInternalState( npsPowerDevice, "in", in );
        npsPowerDevice.powerOn( 1 );
    }

    /**
     * Test power off false return.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPowerOffFalseReturn() throws Exception {
        initilizeStreams();
        npsPowerDevice.powerOff( 1 );

    }

    /**
     * Test power toggle false return.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPowerToggleFalseReturn() throws Exception {
        initilizeStreams();
        npsPowerDevice.powerToggle( 1 );
    }

    /**
     * Test login false return.
     *
     * @throws Exception the exception
     */
    @Test
    public void testLoginFalseReturn() throws Exception {
        OutputStream out = new FileOutputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        InputStream in = EasyMock.createMock( BufferedInputStream.class );
        Whitebox.setInternalState( npsPowerDevice, "out", out );
        Whitebox.setInternalState( npsPowerDevice, "in", in );
        TelnetClient client = EasyMock.createMock( TelnetClient.class );
        client.connect( host );
        EasyMock.expectLastCall();
        Whitebox.setInternalState( npsPowerDevice, "client", client );
        EasyMock.replay( client );
        EasyMock.expect( in.read()).andReturn( 36 ).times( 2 );
        EasyMock.expect( in.read()).andReturn( -1 ).times( 3 );
        EasyMock.replay( in );
        Assert.assertFalse( npsPowerDevice.login());
    }
    
    @Test
    public void testLogin() throws Exception {
        OutputStream out = new FileOutputStream( new File(Thread.currentThread().getContextClassLoader().getResource( "Output.txt" ).toURI() ) );
        String buff = new String(PowerConstants.LOGIN);
        byte[] loginBytes = buff.getBytes();
        InputStream in = EasyMock.createMock( BufferedInputStream.class );
        Whitebox.setInternalState( npsPowerDevice, "out", out );
        Whitebox.setInternalState( npsPowerDevice, "in", in );
        TelnetClient client = EasyMock.createMock( TelnetClient.class );
        client.connect( host );
        EasyMock.expectLastCall();
        Whitebox.setInternalState( npsPowerDevice, "client", client );
        EasyMock.replay( client );
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[0] ).once();
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[1] ).once();
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[2] ).once();
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[3] ).once();
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[4] ).once();
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[5] ).once();
        EasyMock.expect( in.read()).andReturn( (int)loginBytes[6] ).once();
        EasyMock.expect( in.read()).andReturn( -1 ).times( 3 );
        EasyMock.replay( in );
        Assert.assertFalse( npsPowerDevice.login());
    }

}
