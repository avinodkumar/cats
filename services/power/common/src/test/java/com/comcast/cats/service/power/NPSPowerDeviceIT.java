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

import java.io.IOException;
import java.net.SocketException;

import junit.framework.Assert;


import org.junit.Test;

import static com.comcast.cats.service.power.util.PowerConstants.NPS_POWER_DEVICE_USER_NAME;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_POWER_DEVICE_PASSWORD;

public class NPSPowerDeviceIT
{
    private static final String      IP     = "192.168.120.102";

    private static final Integer     OUTLET = 4;
    
    private static final  int DEFAULT_PORT = 161;
    /*
    private WTI_NPS_1600_PowerDevice pwr    = null;

    @BeforeClass
    public void createPowerDevice()
    {
        pwr = new WTI_NPS_1600_PowerDevice( IP, DEFAULT_PORT, NPS_POWER_DEVICE_USER_NAME, NPS_POWER_DEVICE_PASSWORD );
        pwr.createPowerDevConn();
    }

    @AfterClass
    public void exitPowerDevice()
    {
        pwr.logout();
        pwr = null;
    }*/

    
    @Test( timeout = 300000 )
    public void testCommandOn() throws InterruptedException, SocketException, IOException
    {
        WTI_NPS_1600_SNMPPowerDevice pwr = new WTI_NPS_1600_SNMPPowerDevice( IP, DEFAULT_PORT, NPS_POWER_DEVICE_USER_NAME, NPS_POWER_DEVICE_PASSWORD );
        pwr.createPowerDevConn();
        Assert.assertTrue( pwr.powerOn( OUTLET ) );

        String outletStatus = pwr.getOutletStatus( OUTLET );


        Assert.assertEquals( "OutletStatus must be 'ON' , but is '" + outletStatus + "'.", "ON", outletStatus );
        
    }

    @Test( timeout = 300000 )
    public void testCommandOff() throws InterruptedException, SocketException, IOException
    {
       
        WTI_NPS_1600_SNMPPowerDevice pwr = new WTI_NPS_1600_SNMPPowerDevice( IP, DEFAULT_PORT, NPS_POWER_DEVICE_USER_NAME, NPS_POWER_DEVICE_PASSWORD );
        pwr.createPowerDevConn();
                
        Assert.assertTrue( pwr.powerOff( OUTLET ) );
        
        String outletStatus = pwr.getOutletStatus( OUTLET );
        
        Assert.assertEquals( "OutletStatus must be 'OFF' , but is '" + outletStatus + "'.", "OFF", outletStatus );
        
    }
    
    @Test( timeout = 1200000 )
    public void testCommandBoot() throws InterruptedException, SocketException, IOException
    {
       
        WTI_NPS_1600_SNMPPowerDevice pwr = new WTI_NPS_1600_SNMPPowerDevice( IP, DEFAULT_PORT, NPS_POWER_DEVICE_USER_NAME, NPS_POWER_DEVICE_PASSWORD );        
        pwr.createPowerDevConn();
        String outletStatusBeforeToggle = pwr.getOutletStatus( OUTLET );

        String outletStatusExpected = null;

        Assert.assertTrue( pwr.powerToggle( OUTLET ) );

        String outletStatus = pwr.getOutletStatus( OUTLET );

        if ( outletStatusBeforeToggle == "ON" )
        {
            outletStatusExpected = "OFF";
        }
        else if ( outletStatusBeforeToggle == "OFF" )
        {
            outletStatusExpected = "ON";
        }

        Assert.assertEquals( "OutletStatus must be '" + outletStatusExpected + "' , but is '" + outletStatus + "'.",
                outletStatusExpected, outletStatus );
              

    }
}
