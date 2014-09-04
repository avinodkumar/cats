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

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.monitor.RebootMonitoringContext;

/**
 * Integration test for {@link TraceRebootMonitorFactory}.
 * 
 * @author SSugun00c
 * 
 */
public class TraceRebootMonitorFactoryIT
{
    private static final String SUPPORTED_MAC        = "E4:48:C7:A8:1B:04";
    private static final String UNSUPPORTED_MAC      = "00:19:47:25:AD:7E";
    private static final int    INITIAL_REBOOT_COUNT = 0;

    public Settop               settop;

    SettopFactory               settopFactory;
    TraceRebootMonitorFactory   factory;

    @BeforeTest
    public void setUp()
    {
        CatsFramework framework = new CatsFramework( new CatsContext() );
        settopFactory = framework.getSettopFactory();
        RebootMonitoringContext context = new RebootMonitoringContext();
        context.refresh();
        factory = context.getBean( TraceRebootMonitorFactory.class );
    }

    @AfterTest
    public void tearDown()
    {
        settop = null;
    }

    @Test
    public void createJobsSupportedTest()
    {
        try
        {
            List< JobDetail > jobs = factory.createJobs( getSupportedSetttop() );
            assertNotNull( jobs );
            Assert.assertFalse( jobs.isEmpty() );
            for ( JobDetail job : jobs )
            {
                assertEquals( job.getJobDataMap().get( TraceRebootMonitor.SETTOP_KEY ), settop );
                // assertNotNull( job.getJobDataMap().get(
                // TraceRebootMonitor.REPORTER_KEY ) );
                assertNotNull( job.getJobDataMap().get( TraceRebootMonitor.STATE_KEY ) );
                assertEquals( job.getJobDataMap().get( TraceRebootMonitor.STATE_KEY ), INITIAL_REBOOT_COUNT );
            }
        }
        catch ( SchedulerException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void createJobsUnSupportedTest()
    {
        try
        {
            List< JobDetail > jobs = factory.createJobs( getUnsupportedSetttop() );
            assertNotNull( jobs );
            Assert.assertTrue( jobs.isEmpty() );
        }
        catch ( SchedulerException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    // Utility Methods------------
    private Settop getSupportedSetttop()
    {
        Settop settop = null;
        try
        {
            settop = settopFactory.findSettopByHostMac( SUPPORTED_MAC, false );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
        return settop;
    }

    private Settop getUnsupportedSetttop()
    {
        Settop settop = null;
        try
        {
            settop = settopFactory.findSettopByHostMac( UNSUPPORTED_MAC, false );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
        return settop;
    }
}
