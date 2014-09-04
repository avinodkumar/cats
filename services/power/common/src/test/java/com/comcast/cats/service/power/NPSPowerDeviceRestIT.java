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
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_OFF;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_POWER_DEVICE_USER_NAME;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_POWER_DEVICE_PASSWORD;

public class NPSPowerDeviceRestIT
{
    private static final String  DEVICE_IP      = "192.168.120.102"; //TODO this ip
                                                                  // needs to be
                                                                  // changed.

    private static final Integer OUTLET         = 4;

    private static final int     SNMP_PORT      = 161;

    private static final String  COMMUNITY_NAME = "public";

    @Test
    public void proxySnmpPowerCreation() throws URISyntaxException
    {
        Assert.assertNotNull( new NpsSnmpPowerDeviceRestImpl( DEVICE_IP, SNMP_PORT, COMMUNITY_NAME,
                NPS_POWER_DEVICE_USER_NAME, NPS_POWER_DEVICE_PASSWORD, null ) );
    }

    @Test( timeOut = 300000 )
    public void testCommandOn() throws InterruptedException, SocketException, IOException
    {/*

        NpsSnmpPowerDeviceRestImpl pwr = new NpsSnmpPowerDeviceRestImpl( DEVICE_IP, SNMP_PORT, COMMUNITY_NAME, null,
                null, null );
        pwr.createPowerDevConn();
        Assert.assertTrue( pwr.powerOn( OUTLET ) );

        String outletStatus = pwr.getOutletStatus( OUTLET );

        Assert.assertTrue( POWER_ON.equals( outletStatus ), "OutletStatus must be 'ON' , but is '" + outletStatus
                + "'." );

    */}

    @Test( timeOut = 300000 )
    public void testCommandOff() throws InterruptedException, SocketException, IOException
    {/*
        NpsSnmpPowerDeviceRestImpl pwr = new NpsSnmpPowerDeviceRestImpl( DEVICE_IP, SNMP_PORT, COMMUNITY_NAME, null,
                null, null );
        pwr.createPowerDevConn();

        Assert.assertTrue( pwr.powerOff( OUTLET ) );

        String outletStatus = pwr.getOutletStatus( OUTLET );

        Assert.assertTrue( POWER_OFF.equals( outletStatus ), "OutletStatus must be 'OFF' , but is '" + outletStatus
                + "'." );

    */}

    @Test( timeOut = 1200000 )
    public void testCommandBoot() throws InterruptedException, SocketException, IOException
    {/*

        NpsSnmpPowerDeviceRestImpl pwr = new NpsSnmpPowerDeviceRestImpl( DEVICE_IP, SNMP_PORT, COMMUNITY_NAME, null,
                null, null );

        pwr.createPowerDevConn();

        Assert.assertTrue( pwr.powerToggle( OUTLET ) );

        String outletStatus = pwr.getOutletStatus( OUTLET );

        Assert.assertTrue( POWER_ON.equals( outletStatus ), "OutletStatus must be '" + POWER_ON + "' , but is '"
                + outletStatus + "'." );

    */}
}
