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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.monitor.RebootMonitoringContext;

public class BarcelonaRebootMonitorIT
{

    public final String    TEST_MAC_ID           =  "54:d4:6f:7e:06:62"; //10.255.111.31 get hru diag screen //"00:19:47:25:AD:7E"; 
                                                                        // //"54:D4:6F:96:DE:7C";
                                                                        // //
    private final long     TEST_RUN_TIME         = 15 * 60 * 1000;
    private final int      MONITOR_INTERVAL_SECS = 60;
    public Settop          settop;
    public String          ipAddr;
    RebootMonitorScheduler scheduler;

    SettopFactory          settopFactory;
    RebootMonitorFactory   factory;

    @BeforeTest
    public void setUp()
    {
        try
        {
            CatsFramework framework = new CatsFramework( new CatsContext() );
            settopFactory = framework.getSettopFactory();
            settop = settopFactory.findSettopByHostMac( TEST_MAC_ID );
            ipAddr = settop.getSettopInfo().getHostIpAddress();
            System.out.println( "Settop " + settop.getHostMacAddress() );
            System.out.println( "getHostIpAddress " + settop.getHostIpAddress() );
            System.out.println( "getHostIp4Address " + settop.getHostIp4Address() );
            System.out.println( "getHostIp6Address " + settop.getHostIp6Address() );
            
            System.out.println( "ipAddr " + ipAddr );

            RebootMonitoringContext context = new RebootMonitoringContext();
            context.refresh();
            scheduler = context.getBean( RebootMonitorScheduler.class );

            factory = ( RebootMonitorFactory ) scheduler.getSettopJobFactory();
            System.out.println( "factory " + factory );
        }
        catch ( SettopNotFoundException e )
        {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown()
    {
        settop = null;
        ipAddr = null;
    }
    
    @Test
    public void testWithCustomTrigge()
    {
        
    }

    @Test
    public void testWithCustomTrigger()
    {
        try
        {
            List< JobDetail > jobs = factory.createJobs( settop );
            Trigger trigger = newTrigger().withIdentity( TEST_MAC_ID, TEST_MAC_ID ).startNow()
                    .withSchedule( simpleSchedule().withIntervalInSeconds( MONITOR_INTERVAL_SECS ).repeatForever() )
                    .build();
            scheduler.schedule( jobs, trigger );

            Thread.sleep( TEST_RUN_TIME );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        try
        {
            scheduler.shutdown();
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }

    }

    @Test
    public void testWithCronExp()
    {
        System.out.println( "Test 2" );
        try
        {
            scheduler.schedule( settop, "0 0/2 * 1/1 * ? *" );
            Thread.sleep( TEST_RUN_TIME );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        try
        {
            scheduler.shutdown();
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testSettop()
    {
        System.out.println( "Test 3" );
        try
        {
            scheduler.schedule( settop );
            Thread.sleep( TEST_RUN_TIME );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        try
        {
            scheduler.shutdown();
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }
}
