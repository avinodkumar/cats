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
import org.testng.annotations.Test;
import com.comcast.cats.SettopImpl;

public class BasicWiringTest extends BaseRebootMonitorSchedulerTest {

	public BasicWiringTest() throws SchedulerException {
		super();
	}

	@Test
	public void runMe() throws SchedulerException, InterruptedException {
		scheduler.start();
		
		SettopImpl settop = new SettopImpl() {
			
			@Override
			public String getId() {
				return "211";
			}
			
			public String getMake() {
				return "MOCK";
			}
			
			public String getHostMacAddress(){
			    return "XX:XX:XX:XX:XX";
			}
			
			/*
			@Override
			public String toString() {
				return "My Dummy Settop";
			}
			*/
		};
		
		scheduler.schedule(settop, "* * * * * ?");
		Thread.sleep(5000);
		scheduler.shutdown();
	}
}
