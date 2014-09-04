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
package com.comcast.cats.reboot;

/**
 * Helper class for formatting and other required operations.
 * 
 * @author cfrede001
 * 
 */
public class RebootUtil
{
    public static final String TIME_TICKS_FORMAT = "%02dd:%02dh:%02dm:%02ds.%02d";

    public static final Long   TICKS_PER_DAY     = 8640000L;
    public static final Long   TICKS_PER_HOUR    = 360000L;
    public static final Long   TICKS_PER_MINUTE  = 6000L;
    public static final Long   TICKS_PER_SECOND  = 100L;

    /**
     * Given a specified format like "%02dd:%02dh:%02dm:%02ds.%02d", output will
     * follow for days, hours, minutes, seconds, ticks in that order.
     * 
     * @param upTime
     *            - Time in ticks to be represented.
     * @param format
     *            - desired output format.
     * @return
     */
    public static String formatUptime( Long upTime, String format )
    {
        String value = "";
        if ( upTime >= 0 )
        {
            Long remaining = upTime;
            Long days = upTime / TICKS_PER_DAY;
            remaining %= TICKS_PER_DAY;
            Long hours = remaining / TICKS_PER_HOUR;
            remaining %= TICKS_PER_HOUR;
            Long minutes = remaining / TICKS_PER_MINUTE;
            remaining %= TICKS_PER_MINUTE;
            Long seconds = remaining / TICKS_PER_SECOND;
            remaining %= TICKS_PER_SECOND;
            Long ticks = remaining;
            value = String.format( format, days, hours, minutes, seconds, ticks );
        }
        return value;
    }

    /**
     * Default format version.
     * 
     * @param upTime
     *            - Time in ticks.
     * @return String representing the uptime.
     */
    public static String formatUptime( Long upTime )
    {
        return RebootUtil.formatUptime( upTime, TIME_TICKS_FORMAT );
    }
}
