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
package com.comcast.cats.service.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.comcast.cats.SettopConstants;

/**
 * Stores the application level configuration information.
 * 
 * @author subinsugunan
 * 
 */
public final class SettopApplicationConfigUtil
{
    /**
     * Logger instance for AllocationConfigUtil.
     */
    private static Logger                logger                          = Logger.getLogger( SettopApplicationConfigUtil.class );
    /**
     * flag to check whether AllocationConfigUtil is happening only once.
     */
    private static boolean               initialized;

    public static final String           DEFAULT_ALLOCATION_TIME_IN_MINS = "cats.settop.allocation.time";

    private static int                   allocationTimeInMins            = SettopConstants.DEFAULT_ALLOCATION_TIME_IN_MINS;

    /**
     * Map to hold all configurations.
     */
    private static Map< String, String > settings                        = new LinkedHashMap< String, String >();

    /**
     * Singleton enforcer.
     */
    private SettopApplicationConfigUtil()
    {
        // Singleton enforcer.
    }

    public static void initialize()
    {
        if ( logger.isInfoEnabled() )
        {
            logger.info( "INITILIZING APPLICATION CONFIG - SETTOP SERVICE" );
        }
        // making sure only once this method is called.

        if ( !initialized )
        {
            getProperties();
        }

    }

    private static void getProperties()
    {
        readProperties();

        settings.put( DEFAULT_ALLOCATION_TIME_IN_MINS, String.valueOf( getDefaultAllocationTimeInMins() ) );

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "CATS SETTOP SERVCIE PROPERTIES" );
            logger.debug( "----------------------------------------" );
            for ( String key : getSettings().keySet() )
            {
                logger.debug( key + " = " + getSettings().get( key ) );
            }
            logger.debug( "----------------------------------------" );
        }

        initialized = true;

        return;
    }

    private static void readProperties()
    {
        allocationTimeInMins = ( null != System.getProperty( DEFAULT_ALLOCATION_TIME_IN_MINS ) ) ? Integer
                .parseInt( System.getProperty( DEFAULT_ALLOCATION_TIME_IN_MINS ).trim() )
                : SettopConstants.DEFAULT_ALLOCATION_TIME_IN_MINS;
    }

    public static Map< String, String > getSettings()
    {
        return settings;
    }

    public static int getDefaultAllocationTimeInMins()
    {
        return allocationTimeInMins;
    }

}
