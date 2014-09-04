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
package com.comcast.cats.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;

import com.comcast.cats.domain.configuration.CatsHome;

@Named( )
@DependsOn( value = "catsHome" )
/**
 * Scan for the logger after we have a reference to catsHome. This class
 * uses apache log4j explicitly as this loads the log4j configuration file.
 */
public class CatsLogger
{

    public static final String  CATS_HOME_CONF_RELATIVE_DIR = "conf";
    public static final String  CATS_LOG_CONFIGURATION_XML  = System.getProperty( CatsHome.CATS_HOME_SYSTEM_PROPERTY )
                                                                    + System.getProperty( "file.separator" )
                                                                    + CATS_HOME_CONF_RELATIVE_DIR
                                                                    + System.getProperty( "file.separator" )
                                                                    + "cats-logging.xml";

    private static final Logger logger                      = LoggerFactory.getLogger( CatsLogger.class );

    /**
     * Scan for the logger file definition and configure it.
     */
    public CatsLogger()
    {
        File destFile = new File( CATS_LOG_CONFIGURATION_XML );
        if ( !destFile.exists() || !destFile.isFile() )
        {
            placeLogFileinCatsHome( destFile );
        }

        DOMConfigurator.configureAndWatch( destFile.getPath(), 30000L );
    }

    private void placeLogFileinCatsHome( File destFile )
    {
        byte[] store = new byte[ 1024 ];
        OutputStream os = null;
        InputStream is = getClass().getResourceAsStream( "/cats-logging.xml" );        
        try
        {
            FileUtils.touch( destFile );
            os = new FileOutputStream( destFile );
            int c;
            while ( ( c = is.read( store, 0, store.length ) ) != -1 )
            {
                os.write( store, 0, c );
            }

            logger.info( "Log configuration file is placed in to location: " + destFile.getPath() );
        }
        catch ( IOException e )
        {
            logger.error( "Error placing log configuration file in to catshome: " + e );
        }
        finally
        {
            try
            {
                if ( null != is )
                {
                    is.close();
                }
                if ( null != os )
                {
                    os.close();
                }
            }
            catch ( IOException e )
            {
                logger.error( "Error closing input/ouptput stream: " + e );
            }
        }
    }
}
