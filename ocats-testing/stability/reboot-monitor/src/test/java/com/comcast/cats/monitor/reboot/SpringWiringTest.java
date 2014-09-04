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

import javax.inject.Inject;
import javax.inject.Named;

import org.quartz.SchedulerException;
import org.testng.annotations.Test;
import static junit.framework.Assert.*;

import com.comcast.cats.monitor.RebootMonitoringContext;

@Named
public class SpringWiringTest {

    @Inject
    RebootMonitorScheduler scheduler;
    
	@Test
	public void wiringTest(){
	    RebootMonitoringContext context = new RebootMonitoringContext();
	    context.refresh();
	    RebootMonitorScheduler scheduler = context.getBean( RebootMonitorScheduler.class );
	    assertNotNull( scheduler );
	    assertNotNull( scheduler.getSettopJobFactory() );
	    assertNotNull( ((RebootMonitorFactory)scheduler.getSettopJobFactory()).reportAggregator );
	    assertNotNull( ((RebootMonitorFactory)scheduler.getSettopJobFactory()).getRegisteredJobFactories());
	    assertNotNull( ((RebootMonitorFactory)scheduler.getSettopJobFactory()).getRegisteredJobFactories().get( 0 ));
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
