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

public class ReportAggregatorTest
{

    Settop              settop;
    Settop              settop1;
    ReportAggregator    ra;
    public final String TEST_MAC_ID = "XX:XX:XX:XX:XX";

    @BeforeMethod
    public void setUp()
    {
        SettopDesc desc = new SettopDesc();
        desc.setId( "EmptyId" );
        desc.setMake( "RNG" );
        desc.setHostMacAddress( TEST_MAC_ID );
        settop = new SettopImpl( desc );

        SettopDesc desc1 = new SettopDesc();
        desc1.setId( "EmptyId1" );
        desc1.setMake( "RNG" );
        desc1.setHostMacAddress( "YY:YY:YY:YY:YY" );

        settop1 = new SettopImpl( desc1 );
        ra = new ReportAggregator();
    }

    @AfterMethod
    public void tearDown()
    {
        settop = null;
        settop1 = null;
        ra = null;
    }

    @Test
    public void getReporterTest()
    {
        assertNotNull( ra.getReporter( settop ) );
    }

    @Test
    public void getReporterTes1t()
    {
        RebootReporter rep = ra.getReporter( settop );
        assertNotNull( rep );
        assertEquals( rep, ra.getReporter( settop ) );
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void getReporterTest1()
    {
        ra.getReporter( null );
    }

    @Test
    public void aggregateReportsTest()
    {
        ra.aggregateReport( new RebootStatistics( new Date(), "settop 1" ) );
    }

    @Test (enabled = false)
    public void aggregateReportsTest1()
    {
        System.setProperty( "cats.home", "C:\\CATS_HOME" );
        RebootReporter rep = ra.getReporter( settop );
        RebootReporter rep1 = ra.getReporter( settop1 );
        assertEquals( false, rep == rep1 );
        rep.report( new RebootStatistics( new Date(), "settop 1" ) );
        rep1.report( new RebootStatistics( new Date(), "settop 2" ) );
    }

}
