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

import static org.quartz.JobBuilder.newJob;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import com.comcast.cats.Settop;
import com.comcast.cats.monitor.SettopJobFactory;


public class MockJobFactory implements SettopJobFactory {
	protected String group = "MOCK_MONITOR_GROUP";

	public MockJobFactory() {
		//Do nothing constructor
	}
	
	public MockJobFactory(String group) {
		this.group = group;
	}
	
	
	

	public MockJobFactory(SettopJobFactory parent) {
		parent.register(this);
	}
	
	/**
	 * Create specific job details for this settop.
	 */
	@Override
	public List<JobDetail> createJobs(Settop settop) throws SchedulerException {
		if(!settop.getMake().equalsIgnoreCase("MOCK")) {
			return null;
		}
		JobDetail job = newJob(MockRebootMonitor.class)
    		.withIdentity(settop.getId(), group)
    		.storeDurably(false)
    		.build();
		
		List<JobDetail> jobs = new ArrayList<JobDetail>(1);
		jobs.add(job);
		return jobs;
	}

	/**
	 * Register additional SettopJobFactory class that may also want to contribute JobDetails for this settop.
	 */
	@Override
	public void register(SettopJobFactory... settopJobFactory) {
		throw new UnsupportedOperationException("MockJobFactory does not support registering additional SettopJobFactories");	
	}

    @Override
    public List< SettopJobFactory > getRegisteredJobFactories()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
