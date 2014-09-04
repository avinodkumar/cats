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

import org.quartz.SchedulerException;

import com.comcast.cats.monitor.SettopJobFactory;

public abstract class BaseRebootMonitorSchedulerTest {
	protected RebootMonitorScheduler scheduler;
	
	public BaseRebootMonitorSchedulerTest() throws SchedulerException {
		ReportAggregator reports = new ReportAggregator();
		SettopJobFactory mockJobFactory1 = new MockJobFactory();
		SettopJobFactory mockJobFactory2 = new MockJobFactory("MOCK_MONITOR_GROUP_2");
		SettopJobFactory monitorFactory = new RebootMonitorFactory();
		((RebootMonitorFactory)monitorFactory).setReportAggregator( reports );
		monitorFactory.register(mockJobFactory1
			/*, mockJobFactory2*/);
		scheduler = new RebootMonitorScheduler(monitorFactory);
	}
}
