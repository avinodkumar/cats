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
package com.comcast.cats;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.DeviceSearchService;

/**
 * Test case for {@link DeviceSearchServiceEndpoint}.
 * 
 * @author ssugun00c
 */
public class DeviceSearchServiceEndpointIT
{
    private static final Logger LOGGER                = Logger.getLogger( DeviceSearchServiceEndpointIT.class );

    public static final String  INVALID_MAC_ID        = "00:00:00:00:00:00";
    public static final String  INVALID_MAC_ID_FORMAT = "1234567890";
    private static final String MAC_ID                = "54:D4:6F:96:CD:78";
    private static final String BASE_URL              = "http://localhost/";
    private DeviceSearchService deviceSearchService;

    public DeviceSearchServiceEndpointIT() throws MalformedURLException
    {
        DeviceSearchServiceEndpoint deviceSearchServiceEndpoint = new DeviceSearchServiceEndpoint( new URL( BASE_URL
                + ConfigServiceConstants.DEVICE_SEARCH_SERVICE_WSDL_LOCATION ) );
        Assert.assertNotNull( deviceSearchServiceEndpoint );

        deviceSearchService = deviceSearchServiceEndpoint.getPort();
        Assert.assertNotNull( deviceSearchService );
    }

    @Test
    public void findByMacIdTest()
    {
        SettopDesc settopDesc;

        try
        {
            settopDesc = deviceSearchService.findByMacId( MAC_ID );
            Assert.assertNotNull( settopDesc );
            Assert.assertEquals( MAC_ID, settopDesc.getHostMacAddress() );
            LOGGER.info( "Settop received : " + settopDesc.getRemotePath() );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void findByMacIdNullTest()
    {
        try
        {
            deviceSearchService.findByMacId( null );
        }
        catch ( Exception e )
        {
            Assert.assertEquals( "java.lang.IllegalArgumentException: MAC ID cannot be null or empty", e.getMessage() );
        }
    }

    @Test
    public void findByMacIdEmptyTest()
    {
        try
        {
            deviceSearchService.findByMacId( "" );
        }
        catch ( Exception e )
        {
            Assert.assertEquals( "java.lang.IllegalArgumentException: MAC ID cannot be null or empty", e.getMessage() );
        }
    }

    @Test
    public void findByMacIdInvalidTest()
    {
        try
        {
            deviceSearchService.findByMacId( INVALID_MAC_ID );
        }
        catch ( Exception e )
        {
            Assert.assertEquals( "No settop found with [MAC-ID][" + INVALID_MAC_ID + "]", e.getMessage() );
        }
    }

    @Test
    public void findByMacIdInvalidFormatTest()
    {
        try
        {
            deviceSearchService.findByMacId( INVALID_MAC_ID_FORMAT );
        }
        catch ( Exception e )
        {
            Assert.assertEquals( "java.lang.IllegalArgumentException: Invalid MAC ID", e.getMessage() );
        }
    }

    @Test( expected = SOAPFaultException.class )
    public void findAllAvailableTest()
    {
        List< SettopDesc > settops;

        settops = deviceSearchService.findAllAvailable();
        Assert.assertNotNull( settops );
        LOGGER.info( settops.size() + " settops are available." );
    }

    @Test( expected = SOAPFaultException.class )
    public void findAllAllocatedTest()
    {
        List< SettopDesc > settops;

        settops = deviceSearchService.findAllAllocated();
        Assert.assertNotNull( settops );
        LOGGER.info( settops.size() + " settops  are allocated." );
    }

    @Test
    public void findAllAvailableSettopReservationDescTest()
    {
        List< SettopReservationDesc > settops;

        settops = deviceSearchService.findAllAvailableSettopReservationDesc();
        Assert.assertNotNull( settops );
        LOGGER.info( settops.size() + " settops are available." );
    }

    @Test( expected = SOAPFaultException.class )
    public void findAllAllocatedSettopReservationDescTest()
    {
        List< SettopReservationDesc > settops;

        settops = deviceSearchService.findAllAllocatedSettopReservationDesc();

        for ( SettopReservationDesc reservationDesc : settops )
        {
            Assert.assertTrue( reservationDesc instanceof SettopReservationDesc );
        }

        Assert.assertNotNull( settops );
        LOGGER.info( settops.size() + " settops  are allocated." );
    }
}
