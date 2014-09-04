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
package com.comcast.cats.utils;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CatsUtils
{
    private static Logger logger = LoggerFactory.getLogger( CatsUtils.class );

    /**
     * Check if the time difference between the current time and specified time
     * exceeds the timeInterval.
     * 
     * @param time
     * @param timeInterval
     * @return true if it exceeds.
     */
    public static synchronized Boolean hasTimeElapsed( Date time, long timeInterval )
    {

        Boolean hasTimeElapsed = false;
        logger.trace( "CatsUtils hasTimeElapsed time "+time+" timeInterval "+timeInterval );
        if ( time != null && timeInterval >= 0)
        {
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            calendar.setTime( time );
            long lastActiveTimeinMillis = calendar.getTimeInMillis();
            long idlePeriod = ( currentTime - lastActiveTimeinMillis );
            logger.debug( "CatsUtils hasTimeElapsed idlePeriod "+idlePeriod+" timeInterval "+timeInterval );
            if ( idlePeriod > timeInterval )
            {
                hasTimeElapsed = true;
            }
        } else {
            throw new IllegalArgumentException("Time should not be null :time "+time+" and timeInterval should be gretaer than 0. timeInterval: "+timeInterval);
        }
        return hasTimeElapsed;
    }

}
