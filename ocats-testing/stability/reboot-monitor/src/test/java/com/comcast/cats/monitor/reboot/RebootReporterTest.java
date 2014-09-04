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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

public class RebootReporterTest
{

    Settop              settop;
    RebootReporter      reporter;
    RebootStatistics    stats;
    RebootStatistics    stats1;
    public final String TEST_MAC_ID = "XX:XX:XX:XX:XX:XX";

    @BeforeMethod
    public void setUp()
    {
        SettopDesc desc = new SettopDesc();
        desc.setId( "EmptyId" );
        desc.setMake( "RNG" );
        desc.setHostMacAddress( TEST_MAC_ID );
        settop = new SettopImpl( desc );
        reporter = new RebootReporter( settop );
        stats = new RebootStatistics();
        stats1 = new RebootStatistics();
        System.setProperty( "cats.home", "" );
    }

    @AfterMethod
    public void tearDown()
    {
        settop = null;
        reporter = null;
        stats = null;
        stats1 = null;
    }

    @Test
    public void constructorTest()
    {
        reporter.report( stats );
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void constructorTest1()
    {
        reporter = new RebootReporter( null );
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void constructorTest11()
    {
        reporter = new RebootReporter( new Date(), null );
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void constructorTest12()
    {
        reporter = new RebootReporter( null, settop );
    }

    @Test( )
    public void constructorTest2()
    {
        reporter = new RebootReporter( new Date(), settop );
    }

    @Test( )
    public void testReport()
    {
        reporter.report( stats );
    }

    @Test( )
    public void testReport1()
    {
        reporter.report( null );
    }

    @Test( )
    public void testGetLastRebootDetail()
    {
        reporter.report( stats );
        assertEquals( stats, reporter.getLastRebootDetail() );
    }

    @Test( )
    public void testGetLastRebootDetail1()
    {
        reporter.report( null );
        assertEquals( null, reporter.getLastRebootDetail() );
    }

    @Test( )
    public void testGetLastRebootDetail2()
    {
        reporter.report( stats );
        reporter.report( null );
        assertEquals( stats, reporter.getLastRebootDetail() );
    }

    @Test( )
    public void testGetRebootCount()
    {
        reporter.report( stats );
        reporter.report( null );
        assertEquals( 1, reporter.getRebootCount().intValue() );
    }

    @Test( )
    public void testGetRebootCount1()
    {
        reporter.report( stats );
        reporter.report( stats1 );
        assertEquals( 2, reporter.getRebootCount().intValue() );
    }

    @Test( )
    public void testGetRebootCount2()
    {
        reporter.report( null );
        reporter.report( null );
        assertEquals( 0, reporter.getRebootCount().intValue() );
    }

    @Test( )
    public void testGetRebootCount3()
    {
        assertEquals( 0, reporter.getRebootCount().intValue() );
    }

    @Test( )
    public void testGetReboots()
    {
        reporter.report( stats );
        reporter.report( null );
        assertNotNull( reporter.getReboots() );
        assertEquals( reporter.getReboots().size(), 1 );
        assertEquals( stats, reporter.getReboots().get( 0 ) );
    }

    @Test( )
    public void testGetReboots1()
    {
        reporter.report( stats );
        reporter.report( stats1 );
        assertNotNull( reporter.getReboots() );
        assertEquals( reporter.getReboots().size(), 2 );
        assertEquals( stats, reporter.getReboots().get( 0 ) );
        assertEquals( stats1, reporter.getReboots().get( 1 ) );
    }

    @Test( )
    public void testGetReboots3()
    {
        reporter.report( null );
        reporter.report( null );
        assertNotNull( reporter.getReboots() );
        assertEquals( reporter.getReboots().size(), 0 );
    }

    @Test( )
    public void testGetReboots4()
    {
        assertNotNull( reporter.getReboots() );
        assertEquals( reporter.getReboots().size(), 0 );
    }

    @Test( )
    public void testSetReboots()
    {
        List< RebootStatistics > list = new ArrayList< RebootStatistics >();
        reporter.setReboots( list );
        assertNotNull( reporter.getReboots() );
        assertEquals( list, reporter.getReboots() );
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void testSetReboots1()
    {
        List< RebootStatistics > list = new ArrayList< RebootStatistics >();
        reporter.setReboots( null );
    }

    @Test( )
    public void testToString()
    {
        reporter.toString();
    }

}
