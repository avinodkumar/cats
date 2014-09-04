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
package com.comcast.cats.monitor.reboot;

import static junit.framework.Assert.assertEquals;

import java.util.Calendar;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

public class BarcelonaRebootMonitorTest
{

    Settop                 settop;
    RebootReporter         rebootReporter;
    BarcelonaRebootMonitor monitor;
    Calendar               lastmodifiedTime;
    public final String    TEST_MAC_ID = "XX:XX:XX:XX:XX:XX";

    @BeforeMethod
    public void setUp()
    {
        SettopDesc desc = new SettopDesc();
        desc.setId( "EmptyId" );
        desc.setMake( "RNG" );
        desc.setHostMacAddress( TEST_MAC_ID );
        settop = new SettopImpl( desc );
        monitor = new BarcelonaRebootMonitor();
        monitor.setSettop( settop );
        monitor.setRebootReporter( rebootReporter );
        lastmodifiedTime = Calendar.getInstance();
        monitor.setState( lastmodifiedTime );
    }

    @AfterMethod
    public void tearDown()
    {
        settop = null;
    }

    @Test
    public void testConstructor()
    {
        assertEquals( lastmodifiedTime, monitor.getState() );
        assertEquals( rebootReporter, monitor.getRebootReporter() );
        assertEquals( settop, monitor.getSettop() );
        assertEquals( BarcelonaRebootMonitor.DEFAULT_PORT_NUMBER, monitor.getPortNumber() );
        assertEquals( BarcelonaRebootMonitor.BARCELONA_COMMUNITY_STRING, monitor.getCommunity() );
        assertEquals( BarcelonaRebootMonitor.BARCELONA_REBOOT_OID, monitor.getRebootOID() );
    }

    @Test
    public void testParseRebootInfo()
    {
        monitor.parseRebootInfo( "18:54:41.36" );
        monitor.parseRebootInfo( "2 days, 18:54:41.36" );
        monitor.parseRebootInfo( "1 day, 18:54:41.36" );
    }

    @Test( )
    public void testParseRebootInfo1()
    {
        monitor.parseRebootInfo( "99:99:99.99" );
        monitor.parseRebootInfo( "CATS" );
        monitor.parseRebootInfo( "" );
        monitor.parseRebootInfo( null );

    }

    @Test( )
    public void testDetect()
    {
        monitor.detect();
    }
}
