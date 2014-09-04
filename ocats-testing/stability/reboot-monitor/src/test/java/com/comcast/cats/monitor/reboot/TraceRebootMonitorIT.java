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

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.monitor.RebootMonitoringContext;

/**
 * Integration test for {@link TraceRebootMonitor}.
 * 
 * <pre>
 * <b>Prerequisites</b>
 * 
 * 1. You should have a trace log file under CATS_HOME
 * 
 *     E.g. If the MAC id is E4:48:C7:A8:1B:04 , the default trace file location will be 
 *     
 *     CATS_HOME/E448C7A81B04/trace.logs
 * 
 * </pre>
 * 
 * @author SSugun00c
 * 
 */
public class TraceRebootMonitorIT
{
    private static final String    SUPPORTED_MAC         = "E4:48:C7:A8:1B:04";
    private static final String    UNSUPPORTED_MAC       = "00:19:47:25:AD:7E";
    private static final int       REPEAT_COUNT          = 10;

    private final long             ONE_SEC               = 60 * 1000;
    private final int              MONITOR_INTERVAL_SECS = 60;

    private static final String    EVERY_MIN             = " 0 0/1 * 1/1 * ? *";

    private RebootMonitorScheduler scheduler;

    private SettopFactory          settopFactory;
    private RebootMonitorFactory   rebootMonitorFactory;

    @BeforeTest
    public void setUp()
    {
        CatsFramework framework = new CatsFramework( new CatsContext() );
        settopFactory = framework.getSettopFactory();

        Assert.assertNotNull( settopFactory );

        RebootMonitoringContext context = new RebootMonitoringContext();
        context.refresh();
        scheduler = context.getBean( RebootMonitorScheduler.class );
        Assert.assertNotNull( scheduler );

        rebootMonitorFactory = ( RebootMonitorFactory ) scheduler.getSettopJobFactory();
        Assert.assertNotNull( rebootMonitorFactory );

        System.setProperty( CatsProperties.SERVER_URL_PROPERTY, "http://cats-stag.cable.comcast.com:8080/" );
    }

    @Test
    public void testSupportedSettopWithCustomTrigger()
    {
        try
        {
            List< JobDetail > jobs = rebootMonitorFactory.createJobs( getSupportedSetttop() );

            Assert.assertNotNull( jobs );
            Assert.assertFalse( jobs.isEmpty() );

            scheduler.schedule( jobs, getDefaultTrigger() );

            Thread.sleep( 15 * ONE_SEC );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
        finally
        {
            try
            {
                scheduler.shutdown();
            }
            catch ( SchedulerException e )
            {
                Assert.fail( e.getMessage() );
            }
        }
    }

    @Test
    public void testUnSupportedSettopWithCustomTrigger()
    {
        try
        {
            List< JobDetail > jobs = rebootMonitorFactory.createJobs( getUnSupportedSetttop() );

            Assert.assertNotNull( jobs );
            Assert.assertTrue( jobs.isEmpty() );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testSupportedSettopWithCronExp()
    {
        try
        {
            scheduler.schedule( getSupportedSetttop(), EVERY_MIN );
            Thread.sleep( 15 * ONE_SEC );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
        finally
        {
            try
            {
                scheduler.shutdown();
            }
            catch ( SchedulerException e )
            {
                Assert.fail( e.getMessage() );
            }
        }
    }

    @Test
    public void testSupportedSettopWithDefaultTrigger()
    {
        try
        {
            scheduler.schedule( getSupportedSetttop() );
            Thread.sleep( 20 * ONE_SEC );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
        finally
        {
            try
            {
                scheduler.shutdown();
            }
            catch ( SchedulerException e )
            {
                Assert.fail( e.getMessage() );
            }
        }
    }

    // Utility Methods------------

    private Trigger getDefaultTrigger()
    {
        @SuppressWarnings( "static-access" )
        Trigger trigger = newTrigger()
                .withIdentity( SUPPORTED_MAC, SUPPORTED_MAC )
                .startNow()
                .withSchedule(
                        simpleSchedule().withIntervalInSeconds( MONITOR_INTERVAL_SECS ).repeatMinutelyForTotalCount(
                                REPEAT_COUNT ) ).build();

        return trigger;
    }

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

    private Settop getUnSupportedSetttop()
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
