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
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;

import com.comcast.cats.Settop;
import com.comcast.cats.monitor.SettopJobFactory;

/**
 * This factory class determines if it can provide monitors for reboot detection
 * for a settop. Specifically, this class will provides reboot detection
 * monitors for all settops that support Barcelona. It provides
 * SNMPRebootMonitors for all barcelona boxes.
 * 
 * The BarcelonaRebootMonitorFactory registers itself to the parent
 * RebootMonitorFactory. RebootMonitorFactory then delegates monitor creation to
 * this factory.
 * 
 * @author skurup00c
 * 
 */
@Named( "BarcelonaRebootMonitorFactory" )
public class BarcelonaRebootMonitorFactory implements SettopJobFactory
{
    protected static final String JOB_GROUP                = "BARCELONA_MONITOR_GROUP";
    protected static final String BARCELONA_BOX_IDENTIFIER = "RNG";

    private SettopJobFactory      parentFactory;

    public BarcelonaRebootMonitorFactory()
    {
    }

    public BarcelonaRebootMonitorFactory( SettopJobFactory parent )
    {
        if ( parent == null )
        {
            throw new IllegalArgumentException( "Parent factory cannot be null" );
        }
        parentFactory = parent;
        parentFactory.register( this );
    }

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
     * Create specific job details for this settop.
     */
    @Override
    public List< JobDetail > createJobs( Settop settop ) throws SchedulerException
    {
        if ( settop == null )
        {
            throw new IllegalArgumentException( "Settop cannot be null" );
        }

        ArrayList< JobDetail > jobs = new ArrayList< JobDetail >();

        if ( !validateSettop( settop ) )
        {
            return jobs;
        } 
        else
        {

            JobDetail job = newJob( BarcelonaRebootMonitor.class ).withIdentity( settop.getHostMacAddress(), JOB_GROUP )
                    .storeDurably( false ).build();

            // initial value
            job.getJobDataMap().put( BarcelonaRebootMonitor.STATE_KEY, Calendar.getInstance() );
            jobs.add( job );
        }

        return jobs;
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

        
        if ( settop.getMake() == null || !settop.getMake().contains( BARCELONA_BOX_IDENTIFIER ))
        {
            if ( settop.getModel() == null || !settop.getModel().contains( BARCELONA_BOX_IDENTIFIER ) )
            {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Register additional SettopJobFactory class that may also want to
     * contribute JobDetails for this settop.
     */
    @Override
    public void register( SettopJobFactory... settopJobFactories )
    {
        throw new UnsupportedOperationException( "Cannot register child factories to BarcelonaMonitorFactory" );
    }

    @Override
    public List< SettopJobFactory > getRegisteredJobFactories()
    {
        throw new UnsupportedOperationException( "BarcelonaMonitorFactory does not register child factories" );
    }
}
