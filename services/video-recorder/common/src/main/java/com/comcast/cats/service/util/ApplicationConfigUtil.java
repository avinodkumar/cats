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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;

/**
 * Application level configuration utility.
 * 
 * @author SSugun00c
 * 
 */
public final class ApplicationConfigUtil
{
    /**
     * Logger instance for ApplicationConfigUtil.
     */
    private static Logger  logger               = LoggerFactory.getLogger( ApplicationConfigUtil.class );

    /**
     * flag to check whether ApplicationConfigUtil is happening only once.
     */
    private static boolean initialized;

    private static String  dbUserId;
    private static String  dbPassword;
    private static String  dbConnectionUrl;
    private static String  dbDriverClassName;
    private static int     telnetPortRangeStart = VideoRecorderServiceConstants.DEFAULT_TELNET_PORT_RANGE_START;
    private static int     telnetPortRangeEnd   = VideoRecorderServiceConstants.DEFAULT_TELNET_PORT_RANGE_END;

    /**
     * Singleton enforcer.
     */
    private ApplicationConfigUtil()
    {
        // Singleton enforcer.
    }

    public static void initialize()
    {
        if ( logger.isInfoEnabled() )
        {
            logger.info( "INITILIZING APPLICATION CONFIG - CATS RECORDER SERVICE" );
        }
        // making sure only once this method is called.
        if ( !initialized )
        {
            getSystemProperties();
        }
    }

    private static void getSystemProperties()
    {
        dbUserId = ( null != System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_USER_ID ) ) ? System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_USER_ID ).trim() : null;
        dbPassword = ( null != System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_PASSWORD ) ) ? System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_PASSWORD ).trim() : null;
        dbConnectionUrl = ( null != System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_CONNECTION_URL ) ) ? System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_CONNECTION_URL ).trim() : null;
        dbDriverClassName = ( null != System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_DRIVER_CLASS_NAME ) ) ? System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_DRIVER_CLASS_NAME ).trim()
                : null;
        telnetPortRangeStart = ( null != System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_START ) ) ? Integer
                .parseInt( System.getProperty(
                        VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_START ).trim() )
                : VideoRecorderServiceConstants.DEFAULT_TELNET_PORT_RANGE_START;
        telnetPortRangeEnd = ( null != System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_END ) ) ? Integer
                .parseInt( System.getProperty(
                        VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_END ).trim() )
                : VideoRecorderServiceConstants.DEFAULT_TELNET_PORT_RANGE_END;

        initialized = true;

        return;
    }

    public static String getDbUserId()
    {
        if ( null == dbUserId )
        {
            dbUserId = "";
        }
        return dbUserId;
    }

    public static String getDbPassword()
    {
        if ( null == dbPassword )
        {
            dbPassword = "";
        }
        return dbPassword;
    }

    public static String getDbConnectionUrl()
    {
        return dbConnectionUrl;
    }

    public static String getDbDriverClassName()
    {
        return dbDriverClassName;
    }

    public static int getTelnetPortRangeStart()
    {
        return telnetPortRangeStart;
    }

    public static int getTelnetPortRangeEnd()
    {
        return telnetPortRangeEnd;
    }
}
