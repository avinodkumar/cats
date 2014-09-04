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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A reboot monitor that detects reboot specific to Barcelona RNG boxes.
 * 
 * @author skurup00c
 */
public class BarcelonaRebootMonitor extends SNMPRebootMonitor
{
    protected final static String   BARCELONA_COMMUNITY_STRING    = "hDaFHJG7";
    protected final static String   BARCELONA_REBOOT_OID          = ".1.3.6.1.2.1.1.3.0";
    protected final static String   REBOOT_DETECTION_REGEX_STRING = "days?[&&,]";
    protected final static String[] EXPECTED_DATE_FORMATS         = { "H:mm:ss.S" };
    public final static String PREFERRED_SCHEDULE_CRON_EXPRESSION = "0 0/2 * 1/1 * ? *";

    private Logger                  logger                        = LoggerFactory.getLogger( BarcelonaRebootMonitor.class );

    public BarcelonaRebootMonitor()
    {
        setCommunity( BARCELONA_COMMUNITY_STRING );
        setRebootOID( BARCELONA_REBOOT_OID );
    }

    @Override
    public void setState( Object stateObject )
    {
        if ( stateObject instanceof Calendar )
        {
            super.setState( stateObject );
        }
        else
        {
            logger.warn( "State object obtained is not what is expected by BarcelonaRebootMonitor " );
            throw new IllegalArgumentException(
                    "State object obtained is not what is expected by BarcelonaRebootMonitor " );
        }
    }

    /**
     * Parse the snmp result to detect reboot.
     * 
     * Examples of expected results : "18:54:41.36", "2 days, 18:54:41.36",
     * "1 day, 18:54:41.36"
     */
    @Override
    protected void parseRebootInfo( String snmpQueryResult )
    {
        logger.debug( "snmpQueryResult " + snmpQueryResult );

        long upTime = 0;
        String upTimeDays = null;
        String upTimehours;
        try
        {
            if ( snmpQueryResult != null && !snmpQueryResult.isEmpty() )
            {

                Pattern pattern = Pattern.compile( REBOOT_DETECTION_REGEX_STRING );
                Matcher matcher = pattern.matcher( snmpQueryResult );

                if ( matcher.find() )
                {
                    // seperate the days information and the time information.
                    upTimeDays = StringUtils.substringBefore( snmpQueryResult, matcher.group() ).trim();
                    upTimehours = StringUtils.substringAfter( snmpQueryResult, matcher.group() ).trim();
                }
                else
                {
                    upTimehours = snmpQueryResult.trim();
                }

                upTime = calculateUptime( upTimeDays, upTimehours );

                long timeInterval = calculcateMonitorTimeIntervalInMillis();
                logger.debug( "timeInterval sec " + ( timeInterval / ( 1000 ) ) );
                logger.debug( "upTime sec " + ( upTime ) / 1000 );

                if ( timeInterval > upTime )
                {
                    logger.debug( "Reboot Happened" );
                    RebootStatistics stats = new RebootStatistics( new Date() );
                    stats.setMonitorType( REBOOT_TYPE );
                    stats.setMessage(  "UP Time " + snmpQueryResult, settop.getHostMacAddress() );
                    stats.setUptime( upTime/1000 );
                    alarm(stats);
                }
                else
                {
                    logger.trace( "NO Reboot Happened" );
                }
            }
            else
            {
                logger.debug( "SNMP detection failed : No response from settop " + snmpQueryResult );
            }
        }
        catch ( ParseException e )
        {
            logger.trace( "Result not in an expected format : " + e.getMessage() );
        }
        catch ( NumberFormatException e )
        {
            logger.trace( "Result not in an expected format : " + e.getMessage() );
        }

        ((Calendar)getState()).setTime( new Date() );
    }

    /**
     * Calculates the uptime of the box.
     */
    private long calculateUptime( String upTimeDays, String upTimehours ) throws ParseException, NumberFormatException
    {
        Date actualUptime = DateUtils.parseDate( upTimehours, EXPECTED_DATE_FORMATS );

        if ( upTimeDays != null )
        {
            actualUptime = DateUtils.addDays( actualUptime, Integer.parseInt( upTimeDays ) );
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime( actualUptime );
        cal.getTimeInMillis();

        long upTimemillis = ( cal.get( Calendar.HOUR_OF_DAY ) * 60 * 60 * 1000 )
                + ( cal.get( Calendar.MINUTE ) * 60 * 1000 ) + ( cal.get( Calendar.SECOND ) * 1000 )
                + ( cal.get( Calendar.MILLISECOND ) );

        return upTimemillis;
    }

    /**
     * Calculate time interval between SNMP queries.
     * 
     * @return time difference in millis
     */
    private long calculcateMonitorTimeIntervalInMillis()
    {
        Calendar currentTime = Calendar.getInstance();
        Calendar lastMonitoredTime = ( Calendar ) getState();
        return currentTime.getTimeInMillis() - lastMonitoredTime.getTimeInMillis();
    }
}
