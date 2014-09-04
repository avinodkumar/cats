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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

public class BarcelonaMonitorFactoryTest
{

    Settop              settop;
    public final String TEST_MAC_ID = "XX:XX:XX:XX:XX:XX";

    @BeforeMethod
    public void setUp()
    {
        SettopDesc desc = new SettopDesc();
        desc.setId( "EmptyId" );
        desc.setMake( "RNG" );
        desc.setHostMacAddress( TEST_MAC_ID );
        settop = new SettopImpl( desc );
    }

    @AfterMethod
    public void tearDown()
    {
        settop = null;
    }

    @Test
    public void constructorTest()
    {
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory();
        try
        {
            List< JobDetail > jobs = factory.createJobs( settop );
            assertNotNull( jobs );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test( expectedExceptions =
        { UnsupportedOperationException.class } )
    public void constructorTest1()
    {
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory();
        factory.register( factory, factory );
    }

    @Test
    public void constructorTest3()
    {
        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        try
        {
            List< JobDetail > jobs = factory.createJobs( settop );
            assertNotNull( jobs );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test( expectedExceptions =
        { UnsupportedOperationException.class } )
    public void constructorTest4()
    {
        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        factory.register( factory, factory );
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void constructorTest5()
    {
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( null );
    }

    @Test
    public void createJobsTest()
    {

        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        parentFactory.setReportAggregator( new ReportAggregator() );
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        try
        {
            List< JobDetail > jobs = parentFactory.createJobs( settop );
            assertNotNull( jobs );
            for ( JobDetail job : jobs )
            {
                assertEquals( job.getJobDataMap().get( BarcelonaRebootMonitor.SETTOP_KEY ), settop );
                assertNotNull( job.getJobDataMap().get( BarcelonaRebootMonitor.REPORTER_KEY ) );
                assertNotNull( job.getJobDataMap().get( BarcelonaRebootMonitor.STATE_KEY ) );
            }
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test( expectedExceptions =
        { IllegalArgumentException.class } )
    public void createJobsTest1()
    {
        BarcelonaRebootMonitorFactory parentFactory = new BarcelonaRebootMonitorFactory();
        try
        {
            List< JobDetail > jobs = parentFactory.createJobs( null );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test( enabled = false )
    public void createJobsTest2()
    {
        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        try
        {
            SettopDesc desc = new SettopDesc();
            desc.setId( "EmptyId" );
            desc.setMake( "DTA" );
            desc.setHostMacAddress( TEST_MAC_ID );
            Settop dtaSettop = new SettopImpl( desc );
            List< JobDetail > jobs = parentFactory.createJobs( dtaSettop );
            assertNotNull( jobs );
            assertTrue( jobs.isEmpty() );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test
    public void createJobsTest3()
    {
        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        try
        {
            List< JobDetail > jobs = parentFactory.createJobs( null );
            assertNull( jobs );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test( enabled = false )
    public void createJobsTest4()
    {
        SettopDesc desc = new SettopDesc();
        desc.setId( "EmptyId" );
        desc.setHostMacAddress( TEST_MAC_ID );
        Settop settop = new SettopImpl( desc );

        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        try
        {
            List< JobDetail > jobs = parentFactory.createJobs( settop );
            assertNull( jobs );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test
    public void createJobsTest5()
    {

        SettopDesc desc = new SettopDesc();
        desc.setHostMacAddress( TEST_MAC_ID );
        Settop settop = new SettopImpl( desc );

        RebootMonitorFactory parentFactory = new RebootMonitorFactory();
        BarcelonaRebootMonitorFactory factory = new BarcelonaRebootMonitorFactory( parentFactory );
        try
        {
            List< JobDetail > jobs = parentFactory.createJobs( settop );
            assertNull( jobs );
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

}
