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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.monitor.util.FileSearchUtil;
import com.comcast.cats.monitor.util.FtpSearchUtil;
import com.comcast.cats.monitor.util.RebootConfigUtil;

/**
 * Represents all Reboot Monitors that use Trace to detect reboot.
 *
 * @author SSugun00c
 *
 */
public class TraceRebootMonitor extends AbstractRebootMonitor
{
    // Every 4 hrs
    //public static final String PREFERRED_SCHEDULE_CRON_EXPRESSION = "0 0 0/4 1/1 * ? *";
    // Every Mins
     public static final String PREFERRED_SCHEDULE_CRON_EXPRESSION =
     "0 0/1 * 1/1 * ? *";
    public static final String MONITOR_TYPE_TRACE                 = "[TRACE-REBOOT-MONITOR]";

    @Override
    public void detect()
    {
        TraceRebootMonitorState monitorState = ( TraceRebootMonitorState ) getState();

        int previousHits = monitorState.getHits();
        int totalHits = 0;

        try
        {
            String traceFilePath = RebootConfigUtil.getTraceFilepath( settop );

            if ( logger.isDebugEnabled() )
            {
                logger.debug( "[TraceFilePath][" + traceFilePath + "]" );
            }

            totalHits = FileSearchUtil.countHitsByRegexList( traceFilePath, RebootConfigUtil.getAllRegex() );

            //CMD200 support is disabled we per Tahmina's suggestion. We'll enable this once DNCS IP informations are available in CHIMPS.
           /* if ( isCmd2000Compatible( settop ) )
            {
                totalHits += FtpSearchUtil.countHitsByRegexList( RebootConfigUtil.getCmd2000Host( settop ),
                        RebootConfigUtil.getCmd2000FtpUsername(), RebootConfigUtil.getCmd2000FtpPassword(),
                        RebootConfigUtil.getCmd2000LogFileDirectory(), RebootConfigUtil.getCmd2000LogFileName(),
                        RebootConfigUtil.getAllCMD2000Regex() );
            }*/

            int currentHits = totalHits - previousHits;

            if ( currentHits > 0 )
            {
                // Update State
                monitorState.setHits( totalHits );
                setState( monitorState );

                // Send Alarm
                RebootStatistics stats = new RebootStatistics( new Date() );
                stats.setMonitorType( MONITOR_TYPE_TRACE );
                stats.setMessage( "Previously detected reboots  [" + previousHits + "]. New reboots detected ["
                        + currentHits + "]. Total reboots detected from [" + monitorState.getCreated() + "] is ["
                        + totalHits + "]", settop.getHostMacAddress() );

                alarm( stats );
            }
            else if ( logger.isDebugEnabled() )
            {
                logger.debug( "No reboot detected for " + MONITOR_TYPE_TRACE + " [" + settop.getHostMacAddress()
                        + "]. Total reboots detected from [" + monitorState.getCreated() + "] is [" + totalHits
                        + "]. Last reboot detection at [" + monitorState.getLastUpdated() + "]" );
            }
        }
        catch ( IOException e )
        {
            logger.error( e.getMessage() );
        }
    }

    /**
     * Make sure its a Cisco legacy box and we can FTP to DNCS server.
     *
     * @param settop
     * @return
     */
    private boolean isCmd2000Compatible( Settop settop )
    {
        boolean isCmd200Compactible = false;

        List< String > cIscoLegacySettopTypesList = RebootConfigUtil.getCiscoLegacySettopTypes();
        String componentType = ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType();

        if ( cIscoLegacySettopTypesList.contains( componentType ) )
        {
            if ( ( null != RebootConfigUtil.getCmd2000Host( settop ) )
                    && ( null != RebootConfigUtil.getCmd2000FtpUsername() )
                    && ( null != RebootConfigUtil.getCmd2000FtpPassword() )
                    && ( null != RebootConfigUtil.getCmd2000LogFileDirectory() )
                    && ( null != RebootConfigUtil.getCmd2000LogFileName() )
                    && ( null != RebootConfigUtil.getAllCMD2000Regex() )
                    && ( !RebootConfigUtil.getAllCMD2000Regex().isEmpty() ) )
            {
                isCmd200Compactible = true;
            }
        }

        return isCmd200Compactible;
    }

}
