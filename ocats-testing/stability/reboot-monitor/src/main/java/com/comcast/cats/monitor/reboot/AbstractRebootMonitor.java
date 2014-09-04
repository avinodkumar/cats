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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.comcast.cats.Settop;

/**
 * Class generalization to abstract away retrieving information from
 * JobExecutionContext.
 * 
 * @author cfrede001
 */

// This is to make jobs stateful in Quartz. - sjk
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public abstract class AbstractRebootMonitor implements Job
{

    protected static Logger         logger       = LoggerFactory.getLogger( AbstractRebootMonitor.class );

    protected JobExecutionContext context;
    protected Settop              settop;
    protected RebootReporter      rebootReporter;
    protected Object              stateObject;

    public static final String    SETTOP_KEY   = "settop";
    public static final String    REPORTER_KEY = "rebootReporter";
    public static final String    STATE_KEY    = "state";

    /**
     * Execute method will be called by Quartz.
     */
    @Override
    public void execute( JobExecutionContext context ) throws JobExecutionException
    {
        // Save reference to context in case caller needs additional
        // information.
        if ( !isValidContext() )
        {
            throw new JobExecutionException( "JobExecutionContext is not Valid" );
        }

        this.context = context;
        detect();
    }

    /**
     * Checks if the reboot monitor has obtained Settop and a reporter.
     * These values will be filled in by Quartz by calling the corresponding setters.
     *
     * @return true, if all necessary information required by the monitor is obtained.
     */
    protected boolean isValidContext()
    {
        boolean rtn = true;
        if ( settop == null )
        {
            logger.warn( "Settop Object must not be NULL" );
            rtn = false;
        }
        if ( rebootReporter == null )
        {
            logger.warn( "Reboot Reporter must not be NULL" );
            rtn = false;
        }
        return rtn;
    }

    /**
     * Get the settop associated with the monitor.
     * @return
     */
    public Settop getSettop()
    {
        return settop;
    }

    /**
     * Settop object will be auto-populated based on JobDetail.JobDataMap.
     * 
     * @param settop
     */
    public void setSettop( Settop settop )
    {
        logger.trace( "Settop being retrieved from context " + settop );
        this.settop = settop;
    }

    /**
     * Get the Reboot Reporter instance associated with the monitor.
     * @return
     */
    public RebootReporter getRebootReporter()
    {
        return rebootReporter;
    }

    /**
     * RebootReporter will be auto-populated based on JobDetail.JobDataMap.
     * 
     * @param rebootStats
     */
    public void setRebootReporter( RebootReporter rebootReporter )
    {
        this.rebootReporter = rebootReporter;
    }
    
    /**
     * State will be auto-populated based on JobDetail.JobDataMap.
     * State is used to hold any state that occurs as a result of the previous execution
     * and needs to be passed to the current execution.
     * 
     * @param stateObject
     */
    public void setState( Object stateObject )
    {
        this.stateObject = stateObject;
    }
    
    /**
     * Get the state associated with the monitor.
     * @return
     */
    public Object getState()
    {
        return stateObject;
    }

    /**
     * Method all RebootMonitors must implement for reboot detection specific
     * details.
     * 
     * Implementations of detect can call alarm() method to abstract reporting
     * details.
     */
    public abstract void detect();

    /**
     * Announce that a reboot was detected so that it can be reported
     * appropriately. 
     */
    protected void alarm()
    {
        RebootStatistics stats = new RebootStatistics( new Date() );
        stats.setMessage( settop );
        alarm( stats );
    }

    protected void alarm( String message )
    {
        RebootStatistics stats = new RebootStatistics( new Date() );
        stats.setMessage( message, settop );
        alarm( stats );
    }

    protected void alarm( RebootStatistics stats )
    {
        logger.trace( "REBOOT ALARM DETECTED: " + stats );
        rebootReporter.report( stats );
    }
}
