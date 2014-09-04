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

import com.comcast.cats.Settop;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReportAggregator aggregates reports of all settops. It is responsible for
 * assigning a RebootReporter for a Settop.
 * 
 * @author cfrede001
 * 
 */
@SuppressWarnings( "serial" )
@Named
public class ReportAggregator extends ConcurrentHashMap< String, RebootReporter >
{
    protected static final Logger rebootAggregatedLogger;

    static
    {
        String appenderName = "AggregatedRebootReport";
      	rebootAggregatedLogger = LoggerFactory.getLogger(appenderName);
    }

    /**
     * Get a reporter for this settop. Create a new one, if a reporter is not
     * already assigned to a settop, else provide the already assigned reporter.
     * 
     * @param settop
     * @return RebootReporter.
     */
    public synchronized RebootReporter getReporter( Settop settop )
    {
        if ( settop == null || settop.getId() == null )
        {
            throw new IllegalArgumentException( "Settop cant be null nad must have a valid ID" );
        }
        RebootReporter reporter;
        /**
         * If we already have a reporter registered, return it, otherwise create
         * a new reporter and add it the map.
         */
        if ( containsKey( settop.getId() ) )
        {
            reporter = get( settop.getId() );
        }
        else
        {
            reporter = new RebootReporter( settop, this );
            put( settop.getId(), reporter );
        }
        return reporter;
    }

    /**
     * Aggregates reports for all settops.
     */
    public synchronized void aggregateReport(RebootStatistics detail)
    {
         rebootAggregatedLogger.info( "{}", detail );
    }
}
