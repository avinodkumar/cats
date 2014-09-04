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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class PowerStatusExceptionTest.
 * @Author : Aneesh
 * @since : 28th Sept 2012
 * Description : The class WTI_IPS_1600_PowerDeviceTest is the unit test of {@link WTI_IPS_1600_PowerDevice}
 */

public class WTI_IPS_1600_PowerDeviceTest
{
    
    /** The pwr device. */
    private WTI_IPS_1600_PowerDevice pwrDevice;
    /** The Constant CONNECT_TIMEOUT. */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        pwrDevice = new WTI_IPS_1600_PowerDevice(){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };
        Whitebox.setInternalState( pwrDevice, "ip", "1.1.1.1" );
        Whitebox.setInternalState( pwrDevice, "port", 5000 );
    }

    /**
     * Test create power dev conn.
     */
    @Test
    public void testCreatePowerDevConn() 
    {
        pwrDevice.createPowerDevConn();
        Assert.assertNotNull( pwrDevice.client );
        Assert.assertTrue( pwrDevice.client.getInitialCR() );
    }

    /**
     * Test power when device is disabled.
     */
    @Test
    public void testPowerWhenDeviceIsDisabled() {
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.OFF );
        Assert.assertFalse( pwrDevice.power( PowerControllerDevice.ON, 3 ) );
    }

    /**
     * Test power for invalid command.
     */
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testPowerForInvalidCommand() 
    {
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        pwrDevice.power( "UNKNOWN", 3 );
    }

    /**
     * Test power for invalid out let.
     */
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testPowerForInvalidOutLet() 
    {
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        pwrDevice.power( WTI_IPS_1600_PowerDevice.OFF, -3 );
    }

    /**
     * Test power when device connection false.
     */
    @Test
    public void testPowerWhenDeviceConnectionFalse() 
    {
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertFalse( pwrDevice.power( WTI_IPS_1600_PowerDevice.OFF, 2 ) );
        EasyMock.verify( client );
    }

    /**
     * Test get outlet status when it's unknown.
     */
    @Test
    public void testPowerForUnknownResponse() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "RESPONSE>";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertFalse( pwrDevice.power( WTI_IPS_1600_PowerDevice.OFF, 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test power off for valid response.
     */
    @Test
    public void testPowerOffForValidResponse() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "IPSRESPONSE";
        String mockedCmd = "/OFF 2";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        EasyMock.expect( client.sendCmd( mockedCmd, true )).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertTrue( pwrDevice.powerOff( 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test power off for invalid response.
     */
    @Test
    public void testPowerOffForInvalidResponse() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "IPSRESPONSE";
        String mockedCmd = "/OFF 2";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        EasyMock.expect( client.sendCmd( mockedCmd, true )).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( "Invalid" ).once();
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertFalse( pwrDevice.power( WTI_IPS_1600_PowerDevice.OFF, 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test power on for valid response.
     */
    @Test
    public void testPowerOnForValidResponse() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "IPSRESPONSE";
        String mockedCmd = "/ON 2";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        EasyMock.expect( client.sendCmd( mockedCmd, true )).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertTrue( pwrDevice.powerOn( 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test power boot for valid response.
     */
    @Test
    public void testPowerBootForValidResponse() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "IPSRESPONSE";
        String mockedCmd = "/BOOT 2";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        EasyMock.expect( client.sendCmd( mockedCmd, true )).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertTrue( pwrDevice.powerToggle( 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test power send command failed.
     */
    @Test
    public void testPowerSendCommandFailed() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "IPSRESPONSE";
        String mockedCmd = "/OFF 2";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        EasyMock.expect( client.sendCmd( mockedCmd, true )).andReturn( false );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertFalse( pwrDevice.power( WTI_IPS_1600_PowerDevice.OFF, 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test power send invalid response.
     */
    @Test
    public void testPowerSendInvalidResponse() 
    {
        int responseTime = 5000;
        String prompt = "IPS";
        String response = "RESPONSE";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response ).once();
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertFalse( pwrDevice.power( WTI_IPS_1600_PowerDevice.OFF, 2 ));
        EasyMock.verify( client );
    }

    /**
     * Test get outlet status when it's unknown.
     */
    @Test
    public void testGetOutletStatusUnknown() 
    {
        String plugStatus = "/S";
        String status = "UNKNOWN";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( true );
        EasyMock.expect( client.read(1000 )).andReturn( "-----\\+------------------\\+-------------\\+--------\\+-----------------\\+---------\\+" );
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertEquals( pwrDevice.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }


    /**
     * Test get outlet status when not connected.
     */
    @Test
    public void testGetOutletStatusWhenNotConnected() 
    {
        String status = "UNKNOWN";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        EasyMock.expect( client.isConnected() ).andReturn( false );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertEquals( pwrDevice.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }

    /**
     * Test get outlet status when not connected true.
     */
    @Test
    public void testGetOutletStatusWhenNotConnectedTrue() 
    {
        String status = "UNKNOWN";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        EasyMock.expect( client.isConnected() ).andReturn( true );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertEquals( pwrDevice.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }

    /**
     * Test get outlet status response empty.
     */
    @Test
    public void testGetOutletStatusResponseEmpty() 
    {
        String status = "UNKNOWN";
        String plugStatus = "/S";
        int numOutlets = 3;
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( true );
        EasyMock.expect( client.read(1000 )).andReturn( "" );
        client.close();
        EasyMock.expectLastCall();
        pwrDevice.client = client;
        EasyMock.replay( client );
        Assert.assertEquals( pwrDevice.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }

    /**
     * Test get outlet in valid response index out of bounds exception.
     */
    @Test
    public void testGetOutletInValidResponseIndexOutOfBoundsException() 
    {
        String plugStatus = "/S";
        String status = "UNKNOWN";
        int numOutlets = 3;
        String mockedResponse = "AAA-----+------------------+-------------+--------+-----------------+---------+BBB-----+------------------+-------------+--------+-----------------+---------+CCC";
        Whitebox.setInternalState( pwrDevice, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( pwrDevice, "numOutlets", numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( true );
        EasyMock.expect( client.read(1000 )).andReturn( mockedResponse );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.client = client;
        Assert.assertEquals( pwrDevice.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }

    @Test
    public void testDestroy() {
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        Whitebox.setInternalState( pwrDevice, "client", client);
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        pwrDevice.destroy();
        EasyMock.verify( client );
    }
}
