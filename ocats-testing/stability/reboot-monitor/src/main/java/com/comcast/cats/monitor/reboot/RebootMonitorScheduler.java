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

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.comcast.cats.Settop;
import com.comcast.cats.monitor.AbstractScheduler;
import com.comcast.cats.monitor.SettopJobFactory;
import com.comcast.cats.monitor.exception.UnsupportedSettopTypeException;

/**
 * Responsible for scheduling reboot monitoring jobs against the scheduler.
 * 
 * @author cfrede001
 * 
 */
@Named
public class RebootMonitorScheduler extends AbstractScheduler
{
    private static Logger    logger = LoggerFactory.getLogger( RebootMonitorScheduler.class );
    private SettopJobFactory settopJobFactory;

    public RebootMonitorScheduler() throws SchedulerException
    {
        super();
    }

    public RebootMonitorScheduler( SettopJobFactory settopJobFactory ) throws SchedulerException
    {
        super();
        this.settopJobFactory = settopJobFactory;
    }

    @Inject
    public void setSettopJobFactory( @Named( "RebootMonitorFactory" )
    SettopJobFactory settopJobFactory )
    {
        this.settopJobFactory = settopJobFactory;
    }

    public SettopJobFactory getSettopJobFactory()
    {
        return settopJobFactory;
    }

    public void schedule( Settop settop ) throws SchedulerException, UnsupportedSettopTypeException
    {
        List< JobDetail > jobs = settopJobFactory.createJobs( settop );

        boolean isSupportedSettop = false;

        if ( jobs != null && !jobs.isEmpty() )
        {
            for ( JobDetail job : jobs )
            {
                // TODO: Externalize this. Read from a properties file.
                if ( job.getJobClass() == BarcelonaRebootMonitor.class )
                {
                    scheduleJobs( jobs, BarcelonaRebootMonitor.PREFERRED_SCHEDULE_CRON_EXPRESSION, settop.getId() );
                    isSupportedSettop = true;
                }

                if ( job.getJobClass() == TraceRebootMonitor.class )
                {
                    scheduleJobs( jobs, TraceRebootMonitor.PREFERRED_SCHEDULE_CRON_EXPRESSION, settop.getId() );
                    isSupportedSettop = true;
                }

                if ( !isSupportedSettop )
                {
                    throw new UnsupportedSettopTypeException(
                            "Scheduler does not know how to auto schedule this settop" );
                }
            }
        }
        else
        {
            logger.warn( "No Monitoring Jobs found: " + settop );
            throw new UnsupportedSettopTypeException( "Scheduler does not know how to auto schedule this settop" );
        }
    }

    /**
     * Schedules the jobs with the given Quartz triggers.
     * 
     * @param jobs
     * @param trigger
     * @throws SchedulerException
     */
    public void schedule( List< JobDetail > jobs, Trigger trigger ) throws SchedulerException
    {
        if ( jobs != null )
        {
            for ( JobDetail job : jobs )
            {
                schedule( job, trigger );
            }
        }
        else
        {
            logger.warn( "No Monitoring Jobs found: " );
        }
    }

    /**
     * Schedules the job with the given Quartz triggers.
     * 
     * @param job
     * @param trigger
     * @throws SchedulerException
     */
    public void schedule( JobDetail job, Trigger trigger ) throws SchedulerException
    {
        logger.trace( "Scheduling Job: " + job + " Trigger: " + trigger );
        if ( job != null )
        {
            scheduler.start();
            scheduler.scheduleJob( job, trigger );
        }
        else
        {
            logger.warn( "No Monitoring Jobs found " );
        }

    }

    /**
     * Creates and schedules monitors for a Settop with the given cron
     * expression.
     * 
     * @param settop
     * @param cronExpression
     * @throws SchedulerException
     */
    public void schedule( Settop settop, String cronExpression ) throws SchedulerException
    {
        List< JobDetail > jobs = settopJobFactory.createJobs( settop );
        scheduleJobs( jobs, cronExpression, settop.getId() );
    }

    private void scheduleJobs( List< JobDetail > jobs, String cronExpression, String settopId )
            throws SchedulerException
    {
        if ( jobs != null && !jobs.isEmpty() )
        {
            for ( JobDetail job : jobs )
            {
                Trigger trigger = newTrigger().withIdentity( "CronTrigger:" + settopId + ":" + job.getKey() )
                        .withPriority( 6 ).forJob( job )
                        .withSchedule( CronScheduleBuilder.cronSchedule( cronExpression ) ).build();
                schedule( job, trigger );
            }
        }
        else
        {
            logger.warn( "No Monitoring Jobs found: " );
        }
    }
}
