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
package com.comcast.cats.test;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.configuration.CatsHome;
import com.comcast.cats.monitor.RebootMonitoringContext;
import com.comcast.cats.monitor.exception.UnsupportedSettopTypeException;
import com.comcast.cats.monitor.reboot.RebootMonitorScheduler;

public abstract class CatsTestWithMonitoring extends AbstractSettopTest
{
    private static final long      serialVersionUID = 1L;
    protected static final Logger            logger;
    protected static RebootMonitorScheduler scheduler;
    private static RebootMonitoringContext context;

    static{
    	CatsHome.initializeCatsHome();
    	logger           = LoggerFactory.getLogger( CatsTestWithMonitoring.class );
        context = new RebootMonitoringContext();
        context.refresh();
    }
    public CatsTestWithMonitoring()
    { 	
    	
    }
    
    public CatsTestWithMonitoring( Settop settop )
    {
        super( settop );
        scheduler = context.getBean( RebootMonitorScheduler.class );
        try
        {
            if(scheduler!= null){   
                scheduler.schedule(settop);
            }
        }
        catch ( SchedulerException e )
        {
            logger.error("Reboot Monitoring Scheduler failed "+e.getMessage());
        }
        catch ( UnsupportedSettopTypeException e )
        {
            logger.error("Reboot Monitoring not supported for settop "+settop);
        }
    }

    @AfterSuite
    public void afterSuite()
    {
        try
        {
            if(scheduler!= null){   
                scheduler.shutdown();
            }
        }
        catch ( SchedulerException e )
        {
            e.printStackTrace();
        }
    }
}