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
package com.comcast.cats.provider;

import java.net.URISyntaxException;

import org.jboss.resteasy.client.ProxyFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.service.Power;


public class PowerRestEasyIT
{

    private String  powerServiceURL = "http://192.168.120.102:8080/power-service/rest/";
    private String  host            = "192.168.120.102";
    private Integer port            = 161;
    private Integer outlet          = 5;
    private String  type            = "nps1600";

    @Test
    public void proxyPowerCreationDefault() throws URISyntaxException
    {
        Assert.assertNotNull( new PowerRestProvider( powerServiceURL, host, port, outlet, type ) );
    }

    @Test
    public void proxyPowerCreation() throws URISyntaxException
    {
        Assert.assertNotNull( new PowerRestProvider( powerServiceURL, host, port, outlet, type ) );
    }

    @Test
    public void proxyPowerCreation1() throws URISyntaxException
    {
        Power power = ProxyFactory.create( Power.class, powerServiceURL );
        Assert.assertNotNull( power );
        Assert.assertNotNull( new PowerRestProvider( power, host, port, outlet, type ) );
    }

    @Test
    public void proxyPowerOn() throws URISyntaxException, PowerProviderException
    {
        PowerRestProvider powerRest = new PowerRestProvider( powerServiceURL, host, port, outlet, type );
        Assert.assertNotNull( powerRest );
        powerRest.powerOn();
        Assert.assertEquals( "ON", powerRest.getPowerStatus(), "PowerRest on failed" );
    }

    @Test
    public void proxyPowerOff() throws URISyntaxException, PowerProviderException
    {
        PowerRestProvider powerRest = new PowerRestProvider( powerServiceURL, host, port, outlet, type );
        Assert.assertNotNull( powerRest );
        powerRest.powerOff();
        Assert.assertEquals( "OFF", powerRest.getPowerStatus(), "PowerRest off failed" );
    }

    @Test
    public void proxyPowerReboot() throws URISyntaxException, PowerProviderException
    {
        PowerRestProvider powerRest = new PowerRestProvider( powerServiceURL, host, port, outlet, type );
        Assert.assertNotNull( powerRest );
        powerRest.reboot();
        Assert.assertEquals( "ON", powerRest.getPowerStatus(), "PowerRest reboot failed" );
    }

    @Test
    public void proxyPowerStatus() throws URISyntaxException, PowerProviderException
    {
        PowerRestProvider powerRest = new PowerRestProvider( powerServiceURL, host, port, outlet, type );
        Assert.assertEquals( "ON", powerRest.getPowerStatus(), "PowerRest status query failed" );
    }

}