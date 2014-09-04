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

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class NetBooter_NP_16S_PowerDeviceTest.
 * 
 * @Author : Aneesh
 * @since : 24th Sept 2012
 * Description : The Class NetBooter_NP_1601D_PowerDeviceTest is the unit test of {@link NetBooter_NP_16S_PowerDevice}.
 */

public class NetBooter_NP_16S_PowerDeviceTest
{
    /** The net booter dvc. */
    NetBooter_NP_16S_PowerDevice netBooterDvc;
    
	InetAddress localhost = null;
	int CONNECT_TIMEOUT = 10000;
	int READ_WAIT = 500;
    String OK = null;
	
    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        netBooterDvc = new NetBooter_NP_16S_PowerDevice(){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };
        
        try {
    		localhost = InetAddress.getLocalHost();
    	} catch (UnknownHostException e) {    		
    		e.printStackTrace();
    	} 
        
        // The following code works with Java 7.
        //READ_WAIT = Whitebox.getInternalState(netBooterDvc, "READ_WAIT", NetBooter_NP_16S_PowerDevice.class);
        //CONNECT_TIMEOUT = Whitebox.getInternalState(netBooterDvc, "CONNECT_TIMEOUT", NetBooter_NP_16S_PowerDevice.class);
        
        Integer r = Whitebox.getInternalState(netBooterDvc, "READ_WAIT", NetBooter_NP_16S_PowerDevice.class);
        READ_WAIT = r.intValue();
        Integer c = Whitebox.getInternalState(netBooterDvc, "CONNECT_TIMEOUT", NetBooter_NP_16S_PowerDevice.class);
        CONNECT_TIMEOUT = c.intValue();
        
        OK = Whitebox.getInternalState(netBooterDvc, "OK", NetBooter_NP_16S_PowerDevice.class);
    }
    
    
    /**
     * Test constructor.
     */
    @Test
    public void testConstructor()
    {
        String ip = "1.1.1.1";
        int port = 5000;
        int numOutlets = 5;
        NetBooter_NP_16S_PowerDevice netBooterDvc = new NetBooter_NP_16S_PowerDevice(ip, port, numOutlets);
        Assert.assertEquals( ip, netBooterDvc.ip );
        Assert.assertEquals( port, netBooterDvc.port );
        Assert.assertEquals( numOutlets, netBooterDvc.numOutlets );
    }
    
    /**
     * Test invalid outlet number.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidOutLetNumber() {
        Whitebox.setInternalState( netBooterDvc, "numOutlets", 4 );
        netBooterDvc.power( "ON", 5 );
    }
    
    /**
     * Test invalid command.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidCommand() {
        Whitebox.setInternalState( netBooterDvc, "numOutlets", 4 );
        netBooterDvc.power( "UNKNOWN", 3 );
    }
    
    /**
     * Test on command correct response.
     */
    @Test
    public void testOnCommandCorrectResponse() {
        String expectedResponse = "$A0RESPONSE>";
        String np_command = "$A3 3 1";
        PowerDeviceConnectionNP16S mockedClient = mockPowerDeviceConnectionNP16S(expectedResponse, np_command, 1);
        Assert.assertTrue( netBooterDvc.power( "ON", 3 ));
        EasyMock.verify( mockedClient );
    }

    /**
     * Test off command correct response.
     */
    @Test
    public void testOffCommandCorrectResponse() {
        String expectedResponse = "$A0RESPONSE>";
        String np_command = "$A3 3 0";
        PowerDeviceConnectionNP16S mockedClient = mockPowerDeviceConnectionNP16S(expectedResponse, np_command, 1);
        Assert.assertTrue( netBooterDvc.power( "OFF", 3 ));
        EasyMock.verify( mockedClient );
    }
    
    /**
     * Test boot command correct response.
     */
    @Test
    public void testBootCommandCorrectResponse() {
        String expectedResponse = "$A0RESPONSE>";
        String np_command = "$A4 3";
        PowerDeviceConnectionNP16S mockedClient = mockPowerDeviceConnectionNP16S(expectedResponse, np_command, 1);
        Assert.assertTrue( netBooterDvc.power( "BOOT", 3 ));
        EasyMock.verify( mockedClient );
    }
    
    /**
     * Test on command in correct response.
     */
    @Test
    public void testONCommandInCorrectResponse() {
        String expectedResponse = "INCORRECT RESPONSE>";
        String np_command = "$A3 3 1";
        PowerDeviceConnectionNP16S mockedClient = mockPowerDeviceConnectionNP16S(expectedResponse, np_command, 1);
        Assert.assertFalse( netBooterDvc.power( "ON", 3 ));
        EasyMock.verify( mockedClient );
    }
    
    /**
     * Mock power device connection n p16 s.
     *
     * @param expectedResponse the expected response
     * @param np_command the np_command
     * @param waitForCount the wait for count
     * @return the power device connection n p16 s
     */
    private PowerDeviceConnectionNP16S mockPowerDeviceConnectionNP16S(String expectedResponse, String np_command, int waitForCount)
    {
    	int numOutlets = 4;
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.read( READ_WAIT ) ).andReturn( "" );
        EasyMock.expect( client.sendCmd( np_command,true ) ).andReturn( true );
        EasyMock.expect( client.waitForString( OK, READ_WAIT ) ).andReturn( expectedResponse ).times( waitForCount );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        return client;
    }
    
    /**
     * Test create power dev conn.
     */
    @Test
    public void testCreatePowerDevConn() 
    {
        Whitebox.setInternalState( netBooterDvc, "ip", "1.1.1.1" );
        Whitebox.setInternalState( netBooterDvc, "port", 5000 );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        netBooterDvc.createPowerDevConn();
        PowerDeviceConnectionNP16S mockedClient = Whitebox.getInternalState( netBooterDvc, "client" );
        EasyMock.replay( client );
        Assert.assertTrue(mockedClient.getInitialCR());
        EasyMock.verify( client );
    }
    
    /**
     * Test set ip.
     */
    @Test
    public void testSetIp()
    {
        PowerDeviceConnectionNP16S mockedClient = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        Whitebox.setInternalState( netBooterDvc, "client", mockedClient );
        EasyMock.expect( mockedClient.isConnected() ).andReturn( true );
        mockedClient.close();
        EasyMock.replay( mockedClient );
        netBooterDvc.setIp( "1.1.1.1" );
        Assert.assertEquals( "1.1.1.1", netBooterDvc.getIp() );
    }
    
    /**
     * Test set ip.
     */
    @Test
    public void testSetPort()
    {
        PowerDeviceConnectionNP16S mockedClient = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        Whitebox.setInternalState( netBooterDvc, "client", mockedClient );
        EasyMock.expect( mockedClient.isConnected() ).andReturn( true );
        mockedClient.close();
        EasyMock.replay( mockedClient );
        netBooterDvc.setPort( 5000 );
        Assert.assertEquals( 5000, netBooterDvc.getPort() );
    }
    
    /**
     * Test destroy.
     */
    @Test
    public void testDestroy() 
    {
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        netBooterDvc.destroy();
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status response not okay.
     */
    @Test
    public void testGetOutletStatusResponseNotOkay()
    {
        int numOutlets = 4;
        
        String status = "UNKNOWN";
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( "$A5",true ) ).andReturn( true );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "INVALID RESPONSE" );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "INVALID RESPONSE" );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status response not okay.
     */
    @Test
    public void testGetOutletStatusSendcmdNotOkay()
    {
        int numOutlets = 4;
        
        String status = "UNKNOWN";
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( "$A5",true ) ).andReturn( false );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "INVALID RESPONSE" );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status response not having first comma.
     */
    @Test
    public void testGetOutletStatusResponseNotHavingFirstComma()
    {
        int numOutlets = 4;
        
        String status = "UNKNOWN";        
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( "$A5",true ) ).andReturn( true );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "$A0RESPONSE" );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "$A0RESPONSE" );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status response not having second comma.
     */
    @Test
    public void testGetOutletStatusResponseNotHavingSecondComma()
    {
        int numOutlets = 4;

        String status = "UNKNOWN";
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( "$A5",true ) ).andReturn( true );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "$A0RESPONSE,RESPONSE" );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "$A0RESPONSE,RESPONSE" );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status valid response ON.
     */
    @Test
    public void testGetOutletStatusValidResponseON()
    {
        String status = "ON";
        String expectedResponse = "$A0RESPONSE,OUTLE1 ,";
        verifyMockedResponse(status, expectedResponse);
    }
    
    /**
     * Test get outlet status valid response not off on.
     */
    @Test
    public void testGetOutletStatusValidResponseNotOffOn()
    {
        String status = "UNKNOWN";
        String expectedResponse = "$A0RESPONSE,OUTLE2 ,";
        verifyMockedResponse(status, expectedResponse);
    }

    /**
     * Verify mocked response.
     *
     * @param status the status
     * @param expectedResponse the expected response
     */
    private void verifyMockedResponse(String status, String expectedResponse)
    {
        int numOutlets = 4;

        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( "$A5",true ) ).andReturn( true );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( expectedResponse );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( expectedResponse );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status when not connected.
     */
    @Test
    public void testGetOutletStatusWhenNotConnected()
    {
    	int numOutlets = 4;
        String status = "UNKNOWN";
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet status valid response off.
     */
    @Test
    public void testGetOutletStatusValidResponseOff()
    {
        int numOutlets = 4;

        String status = "OFF";
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( "$A5",true ) ).andReturn( true );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "$A0,1011,RESPONSE" );
        EasyMock.expect( client.read(READ_WAIT)).andReturn( "$A0,1011,RESPONSE" );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 3 ),status );
    }
    
    /**
     * Test get outlet status when request failed.
     */
    @Test
    public void testGetOutletStatusWhenRequestFailed()
    {
        int numOutlets = 4;
        String status = "UNKNOWN";
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
        PowerDeviceConnectionNP16S client = EasyMock.createMock( PowerDeviceConnectionNP16S.class );
        EasyMock.expect( client.getInetAddress() ).andReturn( localhost);
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        EasyMock.replay( client );
        Whitebox.setInternalState( netBooterDvc, "client", client );
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ),status );
        EasyMock.verify( client );
    }
}
