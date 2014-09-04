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

import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

/**
 * Unit test cases for {@link TraceRebootMonitorFactory}.
 * 
 * @author SSugun00c
 * 
 */
public class TraceRebootMonitorFactoryTest
{
    Settop settop;

    @BeforeMethod
    public void setUp()
    {
        settop = new SettopImpl( new SettopDesc() );
    }

    @Test
    public void constructorTest()
    {
        TraceRebootMonitorFactory factory = new TraceRebootMonitorFactory();
        assertNotNull( factory );
    }

    @Test( expectedExceptions =
        { UnsupportedOperationException.class } )
    public void registerTest()
    {
        TraceRebootMonitorFactory factory = new TraceRebootMonitorFactory();
        factory.register();
    }

    @Test( expectedExceptions =
        { UnsupportedOperationException.class } )
    public void getRegisteredJobFactoriesTest()
    {
        TraceRebootMonitorFactory factory = new TraceRebootMonitorFactory();
        factory.getRegisteredJobFactories();
    }

    @Test
    public void createJobsTest()
    {
        TraceRebootMonitorFactory factory = new TraceRebootMonitorFactory();
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
}
