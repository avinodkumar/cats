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
import static junit.framework.Assert.assertNotNull;

import java.util.Date;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

public class RebootStatisticsTest
{

    Settop              settop;
    RebootReporter      reporter;
    RebootStatistics    stats;
    public final String TEST_MAC_ID = "XX:XX:XX:XX:XX:XX";

    @BeforeMethod
    public void setUp()
    {
        SettopDesc desc = new SettopDesc();
        desc.setId( "EmptyId" );
        desc.setMake( "RNG" );
        desc.setHostMacAddress( TEST_MAC_ID );
        settop = new SettopImpl( desc );
        stats = new RebootStatistics();
    }

    @AfterMethod
    public void tearDown()
    {
        settop = null;
        reporter = null;
        stats = null;
    }

    @Test
    public void constructorTest()
    {
        stats = new RebootStatistics();
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( null, stats.getRebootDetectedTime() );
        assertEquals( null, stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void constructorTest1()
    {
        stats = new RebootStatistics( null );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( null, stats.getRebootDetectedTime() );
        assertEquals( RebootStatistics.DEFAULT_MESSAGE, stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void constructorTest2()
    {
        stats = new RebootStatistics();
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( null, stats.getRebootDetectedTime() );
        assertEquals( null, stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void constructorTest4()
    {
        stats = new RebootStatistics( null, null, null );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( null, stats.getRebootDetectedTime() );
        assertNotNull( stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void constructorTest5()
    {
        stats = new RebootStatistics( null, new Object(), new Object() );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( null, stats.getRebootDetectedTime() );
        assertNotNull( stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void constructorTest6()
    {
        Date date = new Date();
        stats = new RebootStatistics( date, new Object(), new Object() );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( date, stats.getRebootDetectedTime() );
        assertNotNull( stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void constructorTest7()
    {
        Date date = new Date();
        stats = new RebootStatistics( date, -1, "", "" );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        assertEquals( date, stats.getRebootDetectedTime() );
        assertNotNull( stats.getMessage() );
        assertEquals( -1, stats.getUptime().longValue() );
        stats.toString();
    }

    @Test
    public void setRebootDetectedTimeTest()
    {
        Date date = new Date();
        stats.setRebootDetectedTime( date );
        assertEquals( date, stats.getRebootDetectedTime() );
        stats.toString();
    }

    @Test
    public void setRebootDetectedTimeTest1()
    {
        stats.setRebootDetectedTime( null );
        assertEquals( null, stats.getRebootDetectedTime() );
        stats.toString();
    }

    @Test
    public void setMonitorTypeTest1()
    {
        String testString = "SNMP";
        stats.setMonitorType( testString );
        assertEquals( testString, stats.getMonitorType() );
        stats.toString();
    }

    @Test
    public void setMonitorTypeTest2()
    {
        String testString = "";
        stats.setMonitorType( testString );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        stats.toString();
    }

    @Test
    public void setMonitorTypeTest3()
    {
        String testString = null;
        stats.setMonitorType( testString );
        assertEquals( RebootStatistics.DEFAULT_MONITOR_TYPE, stats.getMonitorType() );
        stats.toString();
    }

    @Test
    public void setMessageTest()
    {
        String testString = "Reboot Detected";
        stats.setMessage( testString );
        assertNotNull( stats.getMessage() );
        assertEquals( testString, stats.getMessage() );
        stats.toString();
    }

    @Test
    public void setMessageTest1()
    {
        stats.setMessage( null, null );
        assertEquals( RebootStatistics.DEFAULT_MESSAGE, stats.getMessage() );
        stats.toString();
    }

    @Test
    public void setUpTime()
    {
        stats.setUptime( -1L );
        assertEquals( -1L, stats.getUptime().longValue() );
        stats.toString();
    }

}
