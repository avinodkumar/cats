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

import java.net.URI;

import org.easymock.EasyMock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.service.PowerService;

/**
 * The Class PowerServiceRestTest.
 * 
 * @Author : Aneesh
 * @since : 4th Oct 2012 Description : The Class PowerServiceRestTest is the
 *        unit test of {@link NetworkPowerServiceRest}.
 */

public class PowerServiceRestTest
{

    /** The pwr service rest. */
    private NetworkPowerServiceRest pwrServiceRest;

    /** The mocked uri. */
    private String           mockedURI    = "wti-ips://:23/?outlet=5000";

    /** The exception msg. */
    private String           exceptionMsg = "<result>FAILURE - Illegal Path was created</result>";

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp()
    {
        pwrServiceRest = new NetworkPowerServiceRest();
    }

    /**
     * Test status.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testStatus() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.powerStatus( path ) ).andReturn( "ON" );
        EasyMock.replay( pwrService );
        Assert.assertEquals( pwrServiceRest.status(), "ON" );
        EasyMock.verify( pwrService );
    }

    /**
     * Test on success.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOnSuccess() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.hardPowerOn( path ) ).andReturn( true );
        EasyMock.replay( pwrService );
        Assert.assertTrue( pwrServiceRest.on() );
        EasyMock.verify( pwrService );
    }

    /**
     * Test on failure.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOnFailure() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.hardPowerOn( path ) ).andReturn( false );
        EasyMock.replay( pwrService );
        Assert.assertFalse( pwrServiceRest.on() );
        EasyMock.verify( pwrService );
    }

    /**
     * Test off success.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOffSuccess() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.hardPowerOff( path ) ).andReturn( true );
        EasyMock.replay( pwrService );
        Assert.assertTrue( pwrServiceRest.off() );
        EasyMock.verify( pwrService );
    }

    /**
     * Test off failure.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOffFailure() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.hardPowerOff( path ) ).andReturn( false );
        EasyMock.replay( pwrService );
        Assert.assertFalse( pwrServiceRest.off() );
        EasyMock.verify( pwrService );
    }

    /**
     * Test reboot success.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testRebootSuccess() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.hardPowerToggle( path ) ).andReturn( true );
        EasyMock.replay( pwrService );
        Assert.assertTrue( pwrServiceRest.reboot() );
        EasyMock.verify( pwrService );
    }

    /**
     * Gets the mocked power service.
     * 
     * @return the mocked power service
     */
    private PowerService getMockedPowerService()
    {
        PowerService pwrService = EasyMock.createMock( PowerService.class );
        Whitebox.setInternalState( pwrServiceRest, "powerService", pwrService );
        return pwrService;
    }

    /**
     * Test reboot failure.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testRebootFailure() throws Exception
    {
        PowerService pwrService = getMockedPowerService();
        URI path = new URI( mockedURI );
        EasyMock.expect( pwrService.hardPowerToggle( path ) ).andReturn( false );
        EasyMock.replay( pwrService );
        Assert.assertFalse( pwrServiceRest.reboot() );
        EasyMock.verify( pwrService );
    }

    /**
     * Test reboot uri syntax exception.
     */
    @Test
    public void testRebootURISyntaxException()
    {
        Assert.assertFalse( pwrServiceRest.reboot() );
    }

    /**
     * Test on uri syntax exception.
     */
    @Test
    public void testOnURISyntaxException()
    {
        Assert.assertFalse( pwrServiceRest.on() );
    }

    /**
     * Test off uri syntax exception.
     */
    @Test
    public void testOffURISyntaxException()
    {
        Assert.assertFalse( pwrServiceRest.off() );
    }

    /**
     * Test status uri syntax exception.
     */
    @Test
    public void testStatusURISyntaxException()
    {
        Assert.assertEquals( pwrServiceRest.status(), exceptionMsg );
    }
}
