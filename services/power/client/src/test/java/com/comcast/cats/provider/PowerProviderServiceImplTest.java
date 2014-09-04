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

import java.net.URI;
import java.net.URISyntaxException;


import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceEndpoint;

/**
 * The Class PowerProviderServiceImplTest.
 * 
 * @Author : Aneesh
 * @since : 5th Oct 2012
 * Description : The Class PowerProviderServiceImplTest is the unit test of {@link PowerProviderServiceImpl}.
 */

public class PowerProviderServiceImplTest
{
    
    /** The pwd pvr impl. */
    PowerProviderServiceImpl pwdPvrImpl;
    
    /** The pw service. */
    PowerService pwService;
    
    /**
     * Test constructor1.
     *
     * @throws Exception the exception
     */
    @Test
    public void testConstructor1() throws Exception {
        PowerServiceEndpoint pwrEndPoint = EasyMock.createMock( PowerServiceEndpoint.class );
        URI uri = new URI( "" );
        PowerService pwService = EasyMock.createMock( PowerService.class );
        EasyMock.expect( pwrEndPoint.getPowerServiceImplPort() ).andReturn( pwService );
        EasyMock.replay( pwrEndPoint );
        pwdPvrImpl = new PowerProviderServiceImpl(pwrEndPoint, uri);
        Assert.assertNotNull( pwdPvrImpl );
        Assert.assertEquals( pwdPvrImpl.getPowerLocator(), uri );
        EasyMock.verify( pwrEndPoint );
    }
    
    /**
     * Test power off.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPowerOff() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.hardPowerOff(pwPath)).andReturn( true );
        EasyMock.replay( pwService );
        pwdPvrImpl.powerOff();
        EasyMock.verify( pwService );
    }
    
    /**
     * Test power off exception.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = PowerProviderException.class)
    public void testPowerOffException() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.hardPowerOff(pwPath)).andReturn( false );
        EasyMock.replay( pwService );
        pwdPvrImpl.powerOff();
        EasyMock.verify( pwService );
    }
    
    /**
     * Test power on.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPowerOn() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.hardPowerOn(pwPath)).andReturn( true );
        EasyMock.replay( pwService );
        pwdPvrImpl.powerOn();
        EasyMock.verify( pwService );
    }
    
    /**
     * Test power on exception.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = PowerProviderException.class)
    public void testPowerOnException() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.hardPowerOn(pwPath)).andReturn( false );
        EasyMock.replay( pwService );
        pwdPvrImpl.powerOn();
        EasyMock.verify( pwService );
    }

    /**
     * Test power reboot.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPowerReboot() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.hardPowerToggle(pwPath)).andReturn( true );
        EasyMock.replay( pwService );
        pwdPvrImpl.reboot();
        EasyMock.verify( pwService );
    }
    
    /**
     * Test power reboot exception.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = PowerProviderException.class)
    public void testPowerRebootException() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.hardPowerToggle(pwPath)).andReturn( false );
        EasyMock.replay( pwService );
        pwdPvrImpl.reboot();
        EasyMock.verify( pwService );
    }
    
    /**
     * Test get power status.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetPowerStatus() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        EasyMock.expect( pwService.powerStatus(pwPath)).andReturn( "ON" );
        EasyMock.replay( pwService );
        Assert.assertEquals( pwdPvrImpl.getPowerStatus(), "ON");
        EasyMock.verify( pwService );
    }
    
    /**
     * Sets the constructor.
     *
     * @param pwPath the new constructor
     * @throws URISyntaxException the uRI syntax exception
     */
    private void setConstructor(URI pwPath) throws URISyntaxException
    {
        pwService = EasyMock.createMock( PowerService.class );
        pwdPvrImpl = new PowerProviderServiceImpl(pwService, pwPath);
        Assert.assertNotNull( pwdPvrImpl );
        Assert.assertEquals( pwdPvrImpl.getPowerLocator(), pwPath );
    }
    
    /**
     * Test set power locator.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetPowerLocator() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        pwdPvrImpl.setPowerLocator( pwPath );
        Assert.assertEquals( pwdPvrImpl.getPowerLocator(), pwPath );
    }
    
    /**
     * Test set get parent.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSetGetParent() throws Exception {
        URI pwPath = new URI( "" );
        setConstructor(pwPath);
        Object parent = new Object();
        pwdPvrImpl.setParent( parent );
        Assert.assertEquals( pwdPvrImpl.getParent(), parent );
    }
}
