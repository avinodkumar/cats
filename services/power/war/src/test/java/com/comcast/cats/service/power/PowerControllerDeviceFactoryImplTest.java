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

import static com.comcast.cats.service.power.util.PowerConstants.NP16_SCHEME;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/*import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;*/
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerStatistics;
import com.comcast.cats.service.power.util.PowerConstants;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : Aneesh
 * @since : 17th Sept 2012
 * Description : The class PowerControllerDeviceFactoryImplTest is the unit test of {@link PowerControllerDeviceFactoryImpl}
 */

public class PowerControllerDeviceFactoryImplTest
{

    /** The ip. */
    private String ip = "192.168.120.102";

    /** The port. */
    private int port = 23;

    /** The number of outlets. */
    private int numOfOutlets = 16;

    /** The out let. */
    private String outLet ="/?outlet=";

    /** The path. */
    private URI path = null;

    /** The power controller device implementation reference. */
    private PowerControllerDeviceFactoryImpl pwrControllerDeviceImpl;

    /** The controller map. */
    private static HashMap<String, PowerControllerDevice> controllerMap = new HashMap<String,PowerControllerDevice>();

    PowerControllerDeviceFactoryImpl powerControllerFactory;

    @BeforeMethod
    public void init(){
        powerControllerFactory=new PowerControllerDeviceFactoryImpl();
        pwrControllerDeviceImpl = new PowerControllerDeviceFactoryImpl();
        powerControllerFactory.init();
    }

    /**
     *  Test the creation of WTI1600 power controller device.
     */

    @Test
    public void testCreatePowerControllerDeviceWTI1600() 
    {
        try
        {
            path = new URI( "wti1600://" + ip + ":" + port + outLet +  numOfOutlets);
        }
        catch ( URISyntaxException e )
        {
            Assert.fail( "URI Initialization Failed ... " );
        }
        Assert.assertNotNull( path );
        pwrControllerDeviceImpl.init();
        PowerControllerDevice pwrControlDevice = pwrControllerDeviceImpl.createPowerControllerDevice( path );
        commonAssert( pwrControlDevice );
        Assert.assertTrue( pwrControlDevice instanceof WTI_IPS_1600_PowerDevice );
    }

    /**
     *  Test the creation of NPS1600 power controller device.
     */

    @Test
    public void testCreatePowerControllerDeviceNPS1600() 
    {/*
        PowerControllerDeviceFactoryImpl pwrControllerDeviceImpl = new PowerControllerDeviceFactoryImpl();
        try
        {
            path = new URI( "nps1600://" + ip + ":" + port + outLet +  numOfOutlets );
        }
        catch ( URISyntaxException e )
        {
            Assert.fail( "URI Initialization Failed ... " );
        }
        Assert.assertNotNull( path );
        pwrControllerDeviceImpl.init();
        PowerControllerDevice pwrControlDevice = pwrControllerDeviceImpl.createPowerControllerDevice( path );
        commonAssert( pwrControlDevice );
        Assert.assertTrue( pwrControlDevice instanceof NpsSnmpPowerDeviceRestImpl );
    */}

    /**
     * Test the creation of synaccess power controller device.
     */

    @Test
    public void testCreatePowerControllerDeviceSynaccess() 
    {
        PowerControllerDeviceFactoryImpl pwrControllerDeviceImpl = new PowerControllerDeviceFactoryImpl();
        try
        {
            path = new URI( "synaccess://" + ip + ":" + port + outLet +  numOfOutlets );
        }
        catch ( URISyntaxException e )
        {
            Assert.fail( "URI Initialization Failed ... " );
        }
        Assert.assertNotNull( path );
        pwrControllerDeviceImpl.init();
        PowerControllerDevice pwrControlDevice = pwrControllerDeviceImpl.createPowerControllerDevice( path );
        commonAssert( pwrControlDevice );
        Assert.assertTrue( pwrControlDevice instanceof NetBooter_NP_1601D_PowerDevice );
    }

    /**
     * Assert basic stuffs. This is the private method to assert common stuffs applicable for all type of power devices.
     *
     * @param pwrControlDevice the power control device reference.
     */

    private void commonAssert( PowerControllerDevice pwrControlDevice )
    {
        Assert.assertNotNull( pwrControlDevice );
        Assert.assertEquals( pwrControlDevice.getIp() , ip );
        Assert.assertEquals( pwrControlDevice.getPort() , port );
        Assert.assertEquals( pwrControlDevice.getNumOutlets() , numOfOutlets );
        Assert.assertEquals( pwrControlDevice.getState() , PowerConstants.POWER_ON );
        controllerMap.put(pwrControlDevice.getIp(), pwrControlDevice );
    }

    /**
     * Test destroy all controllers behaviour.
     */
    @Test
    @PrepareForTest(PowerControllerDeviceFactoryImpl.class)
    public void testDestroyAllControllers() {
        Assert.assertTrue( controllerMap.size() > 0 );
        Whitebox.setInternalState( PowerControllerDeviceFactoryImpl.class, controllerMap );
        pwrControllerDeviceImpl.destroyAllControllers();
        Assert.assertTrue( controllerMap.isEmpty() );
    }

    /**
     * Test creation of Power Control Device when a PowerControllerDevice reference is already there in HashMap. In such cases,
     * system should return the same reference.
     */
    @Test
    @PrepareForTest(PowerControllerDeviceFactoryImpl.class)
    public void testCreatePwerCntlDeviceAlreadyExist() {
        PowerControllerDeviceFactoryImpl pwrControllerDeviceImpl = new PowerControllerDeviceFactoryImpl();
        try
        {
            path = new URI( "np16://" + ip + ":" + port + outLet +  numOfOutlets);
        }
        catch ( URISyntaxException e )
        {
            Assert.fail( "URI Initialization Failed ... " );
        }
        PowerControllerDevice powerControllerDevice = new NetBooter_NP_1601D_PowerDevice();
        powerControllerDevice.setScheme(NP16_SCHEME);
        powerControllerDevice.setPort( port );
        powerControllerDevice.setNumOutlets( numOfOutlets );
        powerControllerDevice.setState( PowerConstants.POWER_ON );
        powerControllerDevice.setIp(ip);
        PowerInfo powerInfo = new PowerInfo(powerControllerDevice.getScheme(),
        		ip, port, new ArrayList<PowerStatistics>());
        powerInfo.setNumOfOutlets(powerControllerDevice.getNumOutlets());
        powerControllerDevice.setPowerInfo(powerInfo);
        controllerMap.put(ip, powerControllerDevice);
        Whitebox.setInternalState( PowerControllerDeviceFactoryImpl.class, controllerMap );
        PowerControllerDevice pwrControlDeviceRef = pwrControllerDeviceImpl.createPowerControllerDevice( path );
        Assert.assertNotNull( pwrControlDeviceRef );
        Assert.assertSame(pwrControlDeviceRef, powerControllerDevice);
    }

    /**
     * Test the creation of synaccess power controller device when port number is not specified in the URI. In this situation, power device
     * should be initialised with default port number.
     */
    @Test
    public void testCreatePowerControllerDeviceSynaccessDefaultPort() 
    {
        PowerControllerDeviceFactoryImpl pwrControllerDeviceImpl = new PowerControllerDeviceFactoryImpl();
        try
        {
            path = new URI( "synaccess://" + ip + ":" + outLet +  numOfOutlets );
        }
        catch ( URISyntaxException e )
        {
            Assert.fail( "URI Initialization Failed ... " );
        }
        Assert.assertNotNull( path );
        pwrControllerDeviceImpl.init();
        PowerControllerDevice pwrControlDevice = pwrControllerDeviceImpl.createPowerControllerDevice( path );
        Assert.assertNotNull( pwrControlDevice );
        Assert.assertEquals( pwrControlDevice.getIp() , ip );
        Assert.assertEquals( (Integer)pwrControlDevice.getPort() , PowerConstants.DEFAULT_PORT );
        Assert.assertEquals( pwrControlDevice.getNumOutlets() , numOfOutlets );
        Assert.assertEquals( pwrControlDevice.getState() , PowerConstants.POWER_ON );
        Assert.assertTrue( pwrControlDevice instanceof NetBooter_NP_1601D_PowerDevice );
    }
    
    @Test
    public void testCreatePowerDevice() throws URISyntaxException {
        PowerControllerDevice device = powerControllerFactory.createPowerControllerDevice(new URI("wti1600://192.168.160.202:23/?outlet=1"));
        System.out.println("Device Port = " + device.getPort());
        Assert.assertEquals(device.getPort(),23);
    }
    
    @Test
    public void testCreatePowerDeviceDefault() throws URISyntaxException {
        //Test default port.
        PowerControllerDevice device = powerControllerFactory.createPowerControllerDevice(new URI("wti1600://192.168.160.202/?outlet=1"));
        System.out.println("Device Port = " + device.getPort());
        Assert.assertEquals(device.getPort(),23);
    }
    
    @Test
    public void testCreatePowerDeviceExplicitDifferentPort() throws URISyntaxException {
        //Test nostandard port port.
        PowerControllerDevice device = powerControllerFactory.createPowerControllerDevice(new URI("wti1600://192.168.160.202:35/?outlet=1"));
        System.out.println("Device Port = " + device.getPort());
        Assert.assertEquals(device.getPort(),35);
    }
}
