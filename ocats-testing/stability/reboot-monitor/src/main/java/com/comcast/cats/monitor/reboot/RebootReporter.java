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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.xml.DOMConfigurator;

import com.comcast.cats.Settop;

/**
 * Reports all reboots for a settop.
 * 
 * @author cfrede001
 * 
 */
public class RebootReporter
{
    protected Logger           rebootDetectionLogger;
    private Date                     start;
    private RebootStatistics         lastReboot;
    private Settop                   settop;
    private List< RebootStatistics > reboots  = Collections.synchronizedList( new ArrayList< RebootStatistics >() );
    private ReportAggregator         reportAggregator;

    public RebootReporter( Settop settop )
    {
        this( settop, null );
    }

    public RebootReporter( Date start, Settop settop )
    {
        this( start, settop, null );
    }

    public RebootReporter( Date start, Settop settop, ReportAggregator reportAggregator )
    {
        this( settop, reportAggregator );
        if ( start == null )
        {
            throw new IllegalArgumentException( "Start_date/Settop cannot be null" );
        }
        this.start = start;
    }

    public RebootReporter( Settop settop, ReportAggregator reportAggregator )
    {
        if ( settop == null )
        {
            throw new IllegalArgumentException( "settop cannot be null" );
        }
        this.start = new Date();
        this.settop = settop;
        setAppenderForLogger();
        this.reportAggregator = reportAggregator;
    }

    private void setAppenderForLogger()
    {

       String cleanMac = settop.getLogDirectory();

        if ( cleanMac == null )
        {
            cleanMac = "UnknownMac";
        }
		Map<String,String> mdcMap = new HashMap<String,String>();
    	mdcMap.put("SettopMac", cleanMac);
    	mdcMap.put("LogFileName", "RebootDetection.log");
        MDC.setContextMap(mdcMap);
        URL logFileUrl = getClass().getResource("/reboot-report-log4j.xml" );
        DOMConfigurator.configure( logFileUrl );
		rebootDetectionLogger = LoggerFactory.getLogger("RebootDetection");
    }

    public void report( RebootStatistics detail )
    {
        if ( detail != null )
        {
            this.lastReboot = detail;
            reboots.add( detail );
            Map<String,String> mdcMap = new HashMap<String,String>();
        	mdcMap.put("SettopMac", settop.getLogDirectory());
        	mdcMap.put("LogFileName", "RebootDetection.log");
            MDC.setContextMap(mdcMap);
            URL logFileUrl = getClass().getResource( "/reboot-report-log4j.xml" );
            DOMConfigurator.configure( logFileUrl );
            rebootDetectionLogger.info( "{}", detail );
            if ( reportAggregator != null )
            {
                reportAggregator.aggregateReport( detail );
            }
        }
    }

    public RebootStatistics getLastRebootDetail()
    {
        return lastReboot;
    }

    public Integer getRebootCount()
    {
        return reboots.size();
    }

    public List< RebootStatistics > getReboots()
    {
        return reboots;
    }

    public void setReboots( List< RebootStatistics > reboots )
    {
        if ( reboots == null )
        {
            throw new IllegalArgumentException( "Reboots List cannot be null" );
        }
        this.reboots = reboots;
    }

    @Override
    public String toString()
    {
        return "MonitorStatistics [settop = " + settop.getHostMacAddress() + " start=" + start + ", lastReboot="
                + lastReboot + ", reboots=" + reboots + "]";
    }
}