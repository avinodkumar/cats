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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import com.comcast.cats.monitor.SettopJobFactory;

import com.comcast.cats.Settop;

/**
 * Responsible for generating an appropriate JobDetail for a given settop based
 * on its type. If the types of monitors becomes unwieldy for this class, then
 * sub factories responsible for determine JobDetails for a given class of
 * Settop might be a better approach.
 * 
 * @author cfrede001
 * 
 */
@Named( "RebootMonitorFactory" )
public class RebootMonitorFactory implements SettopJobFactory
{
    private Logger                   logger             = LoggerFactory.getLogger( RebootMonitorFactory.class );

    private List< SettopJobFactory > settopJobFactories = new ArrayList< SettopJobFactory >();
    @Inject
    protected ReportAggregator       reportAggregator;

    public RebootMonitorFactory()
    {
        logger.trace( "Creating RebootMonitorFactory" );
    }

    /**
     * Holds a report aggregator to aggregate all reports provided by monitors
     * created from this factory. The factory may produce TraceMonitors,
     * SNMPMonitors etc for a settop and the report aggregator would be
     * responsible for aggregating all reports from the different monitors.
     * 
     * @param reportAggregator
     */
    public void setReportAggregator( ReportAggregator reportAggregator )
    {
        this.reportAggregator = reportAggregator;
    }

    public ReportAggregator getReportAggregator()
    {
        return reportAggregator;
    }

    /**
     * Delegates creation of monitors to child factory that have registered
     * themselves.
     * 
     * @param settop
     * @return
     * @throws SchedulerException
     */
    @Override
    public List< JobDetail > createJobs( Settop settop ) throws SchedulerException
    {
        List< JobDetail > jobDetails = null;
        logger.trace( "Determine RebootMonitor(s) for Settop " + settop );

        if ( validateSettop( settop ) )
        {

            jobDetails = new ArrayList< JobDetail >();
            for ( SettopJobFactory jobFactory : settopJobFactories )
            {
                try
                {
                    List< JobDetail > jobs = jobFactory.createJobs( settop );
                  
                    if ( ( null != jobs ) && ( !jobs.isEmpty() ) )
                    {
                        // add common metadata to jobs.
                        configureJobs( jobDetails, jobs, settop );
                    }
                }
                catch ( IllegalArgumentException iae )
                {
                    logger.warn( "{}", iae );
                }
                catch ( SchedulerException se )
                {
                    logger.warn( "{}", se );
                }
            }
        }
        return jobDetails;
    }

    /**
     * Validates if a settop contains all necessary information so that its
     * reboots can be detected.
     * 
     * @param settop
     */
    protected boolean validateSettop( Settop settop )
    {
        boolean isValid = true;

        if ( settop == null || settop.getId() == null )
        {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Adds additional common metadata to jobs.
     * 
     * @param currentJobs
     * @param newJobs
     * @param settop
     */
    protected void configureJobs( List< JobDetail > currentJobs, List< JobDetail > newJobs, Settop settop )
    {
        for ( JobDetail job : newJobs )
        {
            configureJob( currentJobs, job, settop );
        }
    }

    protected void configureJob( List< JobDetail > currentJobs, JobDetail job, Settop settop )
    {
        JobDataMap dataMap = job.getJobDataMap();
        dataMap.put( AbstractRebootMonitor.SETTOP_KEY, settop );
        /*
         * Retrive an existing RebootReporter for this settop or get a new one.
         */
        dataMap.put( AbstractRebootMonitor.REPORTER_KEY, reportAggregator.getReporter( settop ) );
        currentJobs.add( job );
    }

    @Override
    public void register( SettopJobFactory... settopJobFactories )
    {
        for ( SettopJobFactory jobFactory : settopJobFactories )
        {
            logger.trace( "Registering new SettopJobFactory" );
            this.settopJobFactories.add( jobFactory );
        }
    }

    @Override
    public List< SettopJobFactory > getRegisteredJobFactories()
    {
        return settopJobFactories;
    }
}
