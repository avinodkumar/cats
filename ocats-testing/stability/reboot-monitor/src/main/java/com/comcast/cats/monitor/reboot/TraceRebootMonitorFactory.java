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

import javax.inject.Inject;
import javax.inject.Named;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;

import com.comcast.cats.Settop;
import com.comcast.cats.monitor.SettopJobFactory;

/**
 * This factory class determines if it can provide monitors for reboot detection
 * for a trace enabled {@link Settop}.
 * 
 * The {@link TraceRebootMonitorFactory} registers itself to the parent
 * {@link RebootMonitorFactory}. {@link RebootMonitorFactory} then delegates
 * monitor creation to this factory.
 * 
 * Qualifier name @Named( "TraceRebootMonitorFactory" ) is important to keep as
 * there is multiple implementation of {@link SettopJobFactory} is being used by
 * the system.
 * 
 * @author SSugun00c
 * 
 */
@Named( "TraceRebootMonitorFactory" )
public class TraceRebootMonitorFactory implements SettopJobFactory
{
    protected static final String JOB_GROUP = "[TRACE_MONITOR_GROUP]";

    private SettopJobFactory      parentFactory;

    public TraceRebootMonitorFactory()
    {
    }

    /**
     * We should use setter injection as JSR 330 doesn't support @Named
     * constructor injection
     * 
     * @param parentFactory
     */
    @Inject
    void setParentFactory( @Named( "RebootMonitorFactory" )
    SettopJobFactory parentFactory )
    {

        if ( parentFactory != null )
        {
            this.parentFactory = parentFactory;
            this.parentFactory.register( this );
        }

    }

    /**
     * Create a job if the {@link Settop} is trace supported.
     */
    @Override
    public List< JobDetail > createJobs( Settop settop ) throws SchedulerException
    {
        if ( settop == null )
        {
            throw new IllegalArgumentException(
                    "createJobs failed for TraceRebootMonitorFactory. Settop cannot be null" );
        }

        ArrayList< JobDetail > jobs = new ArrayList< JobDetail >();

        if ( isSupportedSettop( settop ) )
        {
            JobDetail job = newJob( TraceRebootMonitor.class ).withIdentity( settop.getHostMacAddress(), JOB_GROUP )
                    .storeDurably( false ).build();

            // initial value
            job.getJobDataMap().put( TraceRebootMonitor.STATE_KEY, new TraceRebootMonitorState() );
            jobs.add( job );
        }

        return jobs;
    }

    /**
     * Checks whether the {@link Settop} is supported.
     * 
     * @param settop
     * @return
     */
    private boolean isSupportedSettop( Settop settop )
    {
        boolean isSupported = false;

        if ( null != settop.getTrace() )
        {
            isSupported = true;
        }

        return isSupported;
    }

    /**
     * Register additional SettopJobFactory class that may also want to
     * contribute JobDetails for this settop.
     */
    @Override
    public void register( SettopJobFactory... settopJobFactories )
    {
        throw new UnsupportedOperationException( "Cannot register child factories to TraceRebootMonitorFactory" );
    }

    @Override
    public List< SettopJobFactory > getRegisteredJobFactories()
    {
        throw new UnsupportedOperationException( "TraceRebootMonitorFactory does not register child factories" );
    }

}
