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
package com.comcast.cats.domain.configuration;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for setting and modify the Cats home info
 * 
 * @author cfrede001
 * 
 */
@Named
public class CatsHome implements Serializable
{
    private static final long  serialVersionUID            = 1L;
    private static Logger      logger                      = LoggerFactory.getLogger( CatsHome.class );

    public static final String CATS_HOME_RELATIVE_DIR      = "cats";
    public static final String CATS_HOME_TEMP_RELATIVE_DIR = "tmp";
    public static final String CATS_HOME_TEMP_TOUCH        = "touch.txt";
    public static final String CATS_HOME_SYSTEM_PROPERTY   = "cats.home";
    public static final String CATS_HOME_ENV_PROPERTY      = "CATS_HOME";

    protected String           catsHome;
    protected String           tmpHome;
    protected String           touchFile;

    public CatsHome()
    {
        initializeSystemProperties();
        displaySystemProperties();
    }

    /**
     * To configure cats home
     * 
     * @throws IOException
     */
    @PostConstruct
    protected void configure() throws IOException
    {
        configureCatsHome();
        configureCatsTempHome();
        configureTouchFile();
        createDirectoryStructure();

        logger.info( toString() );
    }

    /**
     * make suer cats.home system property is set.
     * 
     */
    public static void initializeSystemProperties()
    {
        logger.info( "Initializing System Properties" );

        String catsHomeSysVar = System.getProperty( CATS_HOME_SYSTEM_PROPERTY );
        logger.info( "[CatsHome System Variable][" + catsHomeSysVar + "]" );

        if ( null == catsHomeSysVar || catsHomeSysVar.isEmpty() )
        {
            logger.info( " Retrieving CatsHome Environment Variable" );

            String catsHomeEnvVar = System.getenv( CATS_HOME_ENV_PROPERTY );
            logger.info( "[CatsHome Environment Variable][" + catsHomeEnvVar + "]" );

            if ( null != catsHomeEnvVar && !( catsHomeEnvVar.isEmpty() ) )
            {
                logger.info( "Setting CatsHome System Variable to [" + catsHomeEnvVar + "]" );
                System.setProperty( CATS_HOME_SYSTEM_PROPERTY, catsHomeEnvVar );
            }
            else
            {
                logger.info( "Both CatsHome System Variable and Environment Variable found null. Setting CatsHome to default under System user home ["
                        + getDefaultCatsHome() + "]" );
                System.setProperty( CATS_HOME_SYSTEM_PROPERTY, getDefaultCatsHome() );
            }
        }
    }

    /**
     * To log all the system properties
     */
    public static void displaySystemProperties()
    {
        Properties props = System.getProperties();

        logger.info( "Found " + Integer.toString( props.size() ) + " System Properties!" );

        Iterator< Entry< Object, Object > > iterator = props.entrySet().iterator();
        Entry< Object, Object > entry;

        while ( iterator.hasNext() )
        {
            entry = iterator.next();
            logger.info( entry.getKey() + "=" + entry.getValue() );
        }
    }

    /**
     * Configure cats home directory
     * 
     */
    protected void configureCatsHome()
    {
        logger.info( "Configuring CatsHome" );
        this.catsHome = System.getProperty( CATS_HOME_SYSTEM_PROPERTY );
        logger.info( "CatsHome set to [" + this.catsHome + "]" );
    }

    /**
     * Configure cats temporary home directory
     * 
     */
    protected void configureCatsTempHome()
    {
        logger.info( "Configuring CatsTemp" );
        this.tmpHome = catsHome + File.separator + CATS_HOME_TEMP_RELATIVE_DIR;
        logger.info( "CatsTemp set to [" + this.tmpHome + "]" );
    }

    /**
     * Get the cats home path
     * 
     * @return catsHome
     */
    public String getCatsHomeDirectory()
    {
        return catsHome;
    }

    /**
     * Get default cats home path
     * 
     * @return catsHome path
     */
    protected static String getDefaultCatsHome()
    {
        String defaultHome = "";
        String userHome = System.getProperty( "user.home" );

        if ( null != userHome )
        {
            logger.info( "user.home not found!" );
            defaultHome = userHome;
        }

        defaultHome += File.separator + CATS_HOME_RELATIVE_DIR;
        return defaultHome;
    }

    /**
     * Get the temporary cats home path
     * 
     * @return tmpHome
     */
    public String getCatsTempDirectory()
    {
        return tmpHome;
    }

    /**
     * Create a touch file that will represent the deepest directory structure
     * for cats.home.
     */
    protected void configureTouchFile()
    {
        logger.info( "Configuring TouchFile" );
        this.touchFile = getCatsTempDirectory() + File.separator + CATS_HOME_TEMP_TOUCH;
        logger.info( "TouchFile set to [" + this.touchFile + "]" );
    }

    /**
     * The touch file
     * 
     * @return - Our touch file.
     */
    public String getTouchFile()
    {
        return touchFile;
    }

    /**
     * Make sure these directories exist by "touching" a temporary file inside
     * tmp.
     * 
     * @throws IOException
     */
    protected void createDirectoryStructure() throws IOException
    {
        FileUtils.touch( new File( getTouchFile() ) );
    }

    @Override
    public String toString()
    {
        return "[CatsHome][" + catsHome + "] [TmpHome][" + tmpHome + "]";
    }
}