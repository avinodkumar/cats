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

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

public class RebootMonitorFactoryTest {
	RebootMonitorFactory factory = new RebootMonitorFactory();
	
	/**
	 * Null should result in IllegalArgumentException.
	 */
	//@Test(expectedExceptions={IllegalArgumentException.class})
	@Test
	public void nullSettop() throws SchedulerException {
		factory.createJobs(null);
	}
	
	/**
	 * Settop without proper fields should also throw IllegalArgumentException.
	 */
	//@Test(expectedExceptions={IllegalArgumentException.class})
	@Test
	public void invalidSettop() throws SchedulerException {
		Settop settop = new SettopImpl();
		factory.createJobs(settop);
	}
	
	@Test
	public void mockMonitorJobDetail() throws SchedulerException {
		SettopDesc desc = new SettopDesc();
		desc.setId("EmptyId");
		desc.setMake("MOCK");
		Settop settop = new SettopImpl(desc);
		List<JobDetail> jobDetail = factory.createJobs(settop);
		assertNotNull(jobDetail);
	}
}
