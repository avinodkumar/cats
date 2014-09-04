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

/**
 * Details about a particular reboot.
 * 
 * @author cfrede001
 */
public class RebootStatistics
{
    protected final static String DEFAULT_MONITOR_TYPE ="Unknown";
    protected final static String DEFAULT_MESSAGE ="NO MESSAGE";

    protected Date   rebootDetectedTime = null;
    protected Long   uptimeSec = -1L;
    protected String message = null;
    protected String monitorType =DEFAULT_MONITOR_TYPE;


    public RebootStatistics()
    {
    }
    
    public RebootStatistics( Date rebootDetectedTime, Long uptimeSec, String message, String monitorType )
    {
        this(rebootDetectedTime,message);
        setMonitorType(monitorType);
        this.uptimeSec = uptimeSec;
    }

    public RebootStatistics( Date rebootDetectedTime, Object... objects )
    {
        this.rebootDetectedTime = rebootDetectedTime;
        setMessage( objects );
    }

    public Date getRebootDetectedTime()
    {
        return rebootDetectedTime;
    }

    /**
     * Set the time of reboot detection.
     * 
     * @param date
     */
    public void setRebootDetectedTime( Date rebootDetectedTime )
    {
        this.rebootDetectedTime = rebootDetectedTime;
    }
    
    public String getMonitorType()
    {
        return monitorType;
    }

    public void setMonitorType( String monitorType )
    {
        if(monitorType == null || monitorType.isEmpty()){
            monitorType = DEFAULT_MONITOR_TYPE;
        }
        this.monitorType = monitorType;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        if(message == null || message.isEmpty()){
            this.message = DEFAULT_MESSAGE;
        }
        this.message = message;
    }

    public Long getUptime()
    {
        return uptimeSec;
    }

    public void setUptime( Long uptimeSec )
    {
        this.uptimeSec = uptimeSec;
    }

    public void setMessage( Object... objects )
    {
        this.setMessage( DEFAULT_MESSAGE, objects );
    }

    /**
     * Sets a message by the monitor with any additional metadata the monitor
     * wishes to report.
     * 
     * @param message
     * @param objects
     */
    public void setMessage( String message, Object... objects )
    {
        if(message == null || message.isEmpty()){
            this.message = DEFAULT_MESSAGE;
        }
        if(objects != null){
            StringBuilder str = new StringBuilder( message );
            int i = 1;
            for ( Object obj : objects )
            {
                if ( i == 1 )
                {
                    str.append( "\n\tObject Information[\n" );
                }
                str.append( "\t\t{Object[" ).append( i++ ).append( "] = " ).append( obj ).append( "}\n" );
            }
            if ( i > 1 )
            {
                str.append( "]" );
            }
            this.message = str.toString();
        }
    }

    @Override
    public String toString()
    {
        return "RebootInfo [Reboot Detected at : " + rebootDetectedTime
                + " by monitor " + monitorType + ", message= " + message + "]";
    }
}
