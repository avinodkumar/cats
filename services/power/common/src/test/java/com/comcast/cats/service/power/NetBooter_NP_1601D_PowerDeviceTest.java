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

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.comcast.cats.service.power.util.PowerConstants.NP16_SCHEME;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerStatistics;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : Aneesh
 * @since : 20th Sept 2012
 * Description : The Class NetBooter_NP_1601D_PowerDeviceTest is the unit test of {@link NetBooter_NP_1601D_PowerDevice}.
 */
@Test
public class NetBooter_NP_1601D_PowerDeviceTest
{
    
    /** The Constant CONNECT_TIMEOUT. */
    private static final int CONNECT_TIMEOUT = 10000;
    
    /** The net booter dvc. */
    NetBooter_NP_1601D_PowerDevice netBooterDvc;
    
    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        netBooterDvc = new NetBooter_NP_1601D_PowerDevice(){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };
        netBooterDvc.setIp("1.1.1.1");
        netBooterDvc.setPort(5000);
        netBooterDvc.setNumOutlets(16);
        PowerInfo pInfo = new PowerInfo(NP16_SCHEME, "1.1.1.1", 5000, new ArrayList<PowerStatistics>());
        pInfo.setNumOfOutlets(16);
        netBooterDvc.setPowerInfo(pInfo);
    }
    
    /**
     * Test create power dev conn.
     */
    @Test
    public void testCreatePowerDevConn() 
    {
        Whitebox.setInternalState( netBooterDvc, "ip", "1.1.1.1" );
        Whitebox.setInternalState( netBooterDvc, "port", 5000 );
        netBooterDvc.createPowerDevConn();
        Assert.assertNotNull( netBooterDvc.client );
        Assert.assertTrue( netBooterDvc.client.getInitialCR() );
    }
    
    /**
     * Test set ip.
     */
    @Test
    public void testSetIp()
    {
        PowerDeviceConnection client = mockPowerDeviceConnection();
        netBooterDvc.setIp( "1.1.1.1" );
        EasyMock.verify( client );
        Assert.assertEquals( "1.1.1.1", netBooterDvc.getIp() );
    }
    
    /**
     * Mock power device connection.
     *
     * @return the power device connection
     */
    private PowerDeviceConnection mockPowerDeviceConnection()
    {
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.isConnected() ).andReturn( true );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        return client;
    }

    /**
     * Test set port.
     */
    @Test
    public void testSetPort() 
    {
        PowerDeviceConnection client = mockPowerDeviceConnection();
        netBooterDvc.setPort( 1000 );
        EasyMock.verify( client );
        Assert.assertEquals( 1000, netBooterDvc.getPort() );
    }
    
    /**
     * Test destroy.
     */
    @Test
    public void testDestroy() 
    {
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        netBooterDvc.destroy();
        EasyMock.verify( client );
    }
    
    
    /**
     * Test power for invalid command.
     */
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testPowerForInvalidCommand() 
    {
        Whitebox.setInternalState( netBooterDvc, "state", PowerControllerDevice.ON );
        netBooterDvc.power( "UNKNOWN", 3 );
    }
    
    /**
     * Test power for invalid out let.
     */
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testPowerForInvalidOutLet() 
    {
        Whitebox.setInternalState( netBooterDvc, "state", PowerControllerDevice.ON );
        netBooterDvc.power( "1", -3 );
    }
    
    /**
     * Test power when device state is on.
     */
    @Test
    public void testPowerForOFFCommand() 
    {
        String cmd = "OFF";
        String mockedPset = "pset 2 0";
        successfulConnection(cmd, mockedPset);
    }
    
    /**
     * Test Power For ON Command.
     */
    @Test
    public void testPowerForONCommand() 
    {
        String cmd = "ON";
        String mockedPset = "pset 2 1";
        successfulConnection(cmd, mockedPset);
    }
    
    /**
     * Test Power For Reboot Command.
     */
    @Test
    public void testPowerForRebootCommand() 
    {
        String cmd = "BOOT";
        String mockedPset = "rb 2";
        successfulConnection(cmd, mockedPset);
    }

    /**
     * Successful connection.
     *
     * @param cmd the cmd
     * @param mockedPset the mocked pset
     */
    private void successfulConnection(String cmd, String mockedPset)
    {
        int responseTime = 500;
        String prompt = ">";
        String response = "RESPONSE>";
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.read(responseTime ) ).andReturn( response ).times( 2 );
        EasyMock.expect( client.sendCmd( mockedPset,true ) ).andReturn( true );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertTrue( netBooterDvc.power( cmd, 2 ) );
        EasyMock.verify( client );
    }
    
    /**
     * Test power when device connection false.
     */
    @Test
    public void testPowerWhenDeviceConnectionFalse() 
    {
        String cmd = "OFF";
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertFalse( netBooterDvc.power( cmd, 2 ) );
        EasyMock.verify( client );
    }
    
    /**
     * Sets the private fields for mocking.
     *
     * @param numOutlets the number of outlets
     */
    private void setPrivateFieldForMocking( int numOutlets )
    {
        Whitebox.setInternalState( netBooterDvc, "state", PowerControllerDevice.ON );
        Whitebox.setInternalState( netBooterDvc, "numOutlets", numOutlets );
    }
    
    /**
     * Test get outlet status when it's unknown.
     */
    @Test
    public void testGetOutletStatusUnknown() 
    {
        int responseTime = 500;
        String prompt = ">";
        String response = "RESPONSE>";
        String plugStatus = "pshow";
        String status = "UNKNOWN";
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( false );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet send cmd true invalid response.
     */
    @Test
    public void testGetOutletSendCmdTrueInvalidResponse() 
    {
        int responseTime = 500;
        String prompt = ">";
        String response = "RESPONSE>";
        String plugStatus = "pshow";
        String status = "UNKNOWN";
        String mockResponse = "Invalid Response---";
        int WAIT = 500;
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( true );
        EasyMock.expect( client.read(WAIT) ).andReturn( mockResponse );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet send cmd true null response.
     */
    @Test
    public void testGetOutletSendCmdTrueNullResponse() 
    {
        String status = "UNKNOWN";
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        EasyMock.expect( client.isConnected() ).andReturn( false );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet send cmd true null response but connected.
     */
    @Test
    public void testGetOutletSendCmdTrueNullResponseButConnected() 
    {
        String status = "UNKNOWN";
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( false );
        EasyMock.expect( client.isConnected() ).andReturn( true );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet send cmd true wrong table format.
     */
    @Test
    public void testGetOutletSendCmdTrueWrongTableFormat() 
    {
        int responseTime = 500;
        String prompt = ">";
        String response = "RESPONSE>";
        String plugStatus = "pshow";
        String status = "UNKNOWN";
        String mockResponse = "Plug1+Plug1Sample1+OFF      +TEL USER     |TIME     |AUTOPING  ";
        int WAIT = 500;
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( true );
        EasyMock.expect( client.read(WAIT) ).andReturn( mockResponse );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }
    
    /**
     * Test get outlet valid table format.
     */
    @Test
    public void testGetOutletIndexOutOfBoundsException() 
    {
        int responseTime = 500;
        String prompt = ">";
        String response = "RESPONSE>";
        String plugStatus = "pshow";
        String status = "UNKNOWN";
        //To do : Need to mock a valid response
       // String mockResponse = "Plug1|Plug1Sample1|OFF      |TEL USER     |TIME     |AUTOPING  |Plug1|Plug1Sample1|OFF      |TEL USER     |TIME     |AUTOPING  ";
        
        String mockResponse = "AAA-----+------------+---------+-------------|---------|----------BB-----+------------+---------+-------------|---------|----------";
        int WAIT = 500;
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.waitForString( prompt, responseTime ) ).andReturn( response );
        EasyMock.expect( client.sendCmd( plugStatus,false ) ).andReturn( true );
        EasyMock.expect( client.read(WAIT) ).andReturn( mockResponse );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
        Assert.assertEquals( netBooterDvc.getOutletStatus( 2 ) , status);
        EasyMock.verify( client );
    }
    
    /**
     * Test power on.
     */
    @Test
    public void testPowerOn() {
        String mockedPset = "pset 2 1";
        PowerDeviceConnection client = getMockedPowerDeviceConnection(mockedPset);
        Assert.assertTrue( netBooterDvc.powerOn( 2 ) );
        EasyMock.verify( client );
    }

    /**
     * Assign mock.
     *
     * @param mockedPset the mocked pset
     * @return the power device connection
     */
    private PowerDeviceConnection getMockedPowerDeviceConnection(String mockedPset)
    {
        int responseTime = 500;
        String prompt = ">";
        String response = "RESPONSE>";
        int numOutlets = 3;
        setPrivateFieldForMocking( numOutlets );
        PowerDeviceConnection client = EasyMock.createMock( PowerDeviceConnection.class );
        EasyMock.expect( client.connect( CONNECT_TIMEOUT ) ).andReturn( true );
        EasyMock.expect( client.read(responseTime ) ).andReturn( response ).times( 2 );
        EasyMock.expect( client.sendCmd( mockedPset,true ) ).andReturn( true );
        client.close();
        EasyMock.expectLastCall();
        EasyMock.replay( client );
        netBooterDvc.client = client;
       
        return client;
    }
    
    /**
     * Test power off.
     */
    @Test
    public void testPowerOff() {
        String mockedPset = "pset 2 0";
        PowerDeviceConnection client = getMockedPowerDeviceConnection(mockedPset);
        Assert.assertTrue( netBooterDvc.powerOff( 2 ) );
        EasyMock.verify( client );
    }
}
