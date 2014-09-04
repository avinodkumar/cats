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
package com.comcast.cat.domain.service.it;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * Integrations test to check whether properties of {@link SettopDesc} is
 * getting trimmed or not.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServicePropertiesTrimIT extends BaseDomainIT
{
    private final String  LEADING_AND_TRAILING_SPACE_PATTERN = "^\\s+|\\s+$";
    private final Pattern PATTERN                            = Pattern.compile( LEADING_AND_TRAILING_SPACE_PATTERN );

    @Test
    public void testSerialNumberProperty()
    {
        try
        {
            SettopDesc settopDesc = settopDomainService.findByMacId( MAC_ID );
            Assert.assertNotNull( settopDesc );
            Assert.assertEquals( MAC_ID, settopDesc.getHostMacAddress() );
            Assert.assertNotNull( settopDesc.getEnvironmentId() );
            LOGGER.debug( settopDesc );

            LOGGER.debug( "Serial No :" + settopDesc.getSerialNumber() + ", Length of serial No: "
                    + settopDesc.getSerialNumber().length() );

            Assert.assertTrue( isTrimApplied( settopDesc.getMCardSerialNumber(), "MCardSerialNumber" ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getSerialNumber(), "SerialNumber" ) );

        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testAllProperties()
    {
        try
        {
            SettopDesc settopDesc = settopDomainService.findByMacId( MAC_ID );
            Assert.assertNotNull( settopDesc );
            Assert.assertEquals( MAC_ID, settopDesc.getHostMacAddress() );
            Assert.assertNotNull( settopDesc.getEnvironmentId() );
            LOGGER.debug( settopDesc );

            Assert.assertTrue( isTrimApplied( settopDesc.getHostMacAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getComponentType() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getContent() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getEnvironmentId() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getFirmwareVersion() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHardwareRevision() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHostIp4Address() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHostIp6Address() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHostIpAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getId() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getMake() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getManufacturer() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getMcardMacAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getMCardSerialNumber() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getModel() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getName() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getRackId() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getRemoteType() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getSerialNumber() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getUnitAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getAudioPath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getClickstreamPath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getClusterPath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getExtraProperties() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHostIp4InetAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHostIp6InetAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getHostIpInetAddress() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getPowerPath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getRemotePath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getTracePath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getVideoPath() ) );
            Assert.assertTrue( isTrimApplied( settopDesc.getVideoSelectionPath() ) );

        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    private boolean isTrimApplied( InetAddress inetAddress )
    {
        if ( null == inetAddress )
        {
            LOGGER.error( "Input InetAddresss is NULL" );
            return true;
        }
        return isTrimApplied( inetAddress.toString() );
    }

    private boolean isTrimApplied( Inet4Address inet4Address )
    {
        if ( null == inet4Address )
        {
            LOGGER.error( "Input Inet4Address is NULL" );
            return true;
        }
        return isTrimApplied( inet4Address.toString() );
    }

    private boolean isTrimApplied( Inet6Address inet6Address )
    {
        if ( null == inet6Address )
        {
            LOGGER.error( "Input Inet6Address is NULL" );
            return true;
        }
        return isTrimApplied( inet6Address.toString() );
    }

    private boolean isTrimApplied( URI uri )
    {
        if ( null == uri )
        {
            LOGGER.error( "Input URI is NULL" );
            return true;
        }
        return isTrimApplied( uri.toString() );
    }

    private boolean isTrimApplied( Map< String, String > extraProperties )
    {
        for ( String key : extraProperties.keySet() )
        {
            if ( !( isTrimApplied( extraProperties.get( key ) ) ) )
            {
                return false;
            }
        }
        return true;
    }

    private boolean isTrimApplied( String inputStr )
    {
        return isTrimApplied( inputStr, "The input string" );
    }

    private boolean isTrimApplied( String inputStr, String message )
    {
        if ( null == inputStr )
        {
            LOGGER.error( message + " is NULL" );
            return true;
        }
        return !( PATTERN.matcher( inputStr ).find() );
    }
}
