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
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author cfrede001
 * 
 */
@Named
public class CatsHome implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID            = 1L;

    private static Logger      logger                      = Logger.getLogger( CatsHome.class );

    public static final String CATS_HOME_RELATIVE_DIR      = "cats";
    public static final String CATS_HOME_TEMP_RELATIVE_DIR = "tmp";
    public static final String CATS_HOME_TEMP_TOUCH        = "touch.txt";
    public static final char   FILE_SEPERATOR              = IOUtils.DIR_SEPARATOR;
    public static final String CATS_HOME_SYSTEM_PROPERTY   = "cats.home";
    
    public static final String CATS_HOME_ENV_PROPERTY   = "CATS_HOME";

    protected String           catsHome;
    protected String           tmpHome;
    protected String           touchFile;

    protected CatsHome()
    {
        displaySystemProperties();
    }
    
    /**
     * Check if the cats home is specified in the env properties.
     * If yes specify those value as cats.home in the system property 
     * If found empty or null, create a default location at user.home/cats.
     * 
     */
    public static void initializeCatsHome() {
        String catsHome = System.getProperty( CATS_HOME_SYSTEM_PROPERTY );
        //logger.info( "catsHome from system "+catsHome );
        
        if(null == catsHome || "".equals(catsHome)){
            String catsHomeEnv = System.getenv( CATS_HOME_ENV_PROPERTY );
            //logger.info( "catsHome from Env "+catsHomeEnv );
            if(null != catsHomeEnv && !"".equals(catsHomeEnv)){
                //logger.info( "Setting catsHome as "+catsHomeEnv );
                System.setProperty( CATS_HOME_SYSTEM_PROPERTY, catsHomeEnv );
            }else{
                //logger.info( "Creating a temp catsHome " );
                String defaultHome = "";
                String userHome = System.getProperty( "user.home" );
                if ( null != userHome )
                {         
                    //logger.info( "user.home is "+userHome );
                    defaultHome = userHome;
                }else{
                    //logger.info( "user.home not found!" );
                }
                defaultHome += FILE_SEPERATOR + CATS_HOME_RELATIVE_DIR;
                System.setProperty( CATS_HOME_SYSTEM_PROPERTY, defaultHome );
            }
        }
    }

    @Value( "#{(systemProperties['cats.home'] != null) ?" + "systemProperties['cats.home'] : "
            + "systemEnvironment['CATS_HOME']}" )
    protected void setSystemCatsHome( String catsHome )
    {
        logger.info( "SetSystemCatsHome[" + catsHome + "]" );
        this.catsHome = catsHome;
    }

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

    public String getCatsHomeDirectory()
    {
        return catsHome;
    }

    protected void configureCatsTemp()
    {
        this.tmpHome = catsHome + FILE_SEPERATOR + CATS_HOME_TEMP_RELATIVE_DIR;
    }

    public String getCatsTempDirectory()
    {
        return tmpHome;
    }

    @PostConstruct
    protected void configure() throws IOException
    {
        if ( catsHome == null )
        {
            catsHome = getDefaultCatsHome();
            logger.info( "Setting DefaultCatsHome [" + catsHome + "]" );
        }
        setCatsHomeSystemProperty();

        configureCatsTemp();

        configureTouchFile();

        createDirectoryStructure();

        logger.info( toString() );
    }

    /**
     * Create a touch file that will represent the deepest directory structure
     * for cats.home.
     */
    protected void configureTouchFile()
    {
        touchFile = getCatsTempDirectory() + FILE_SEPERATOR + CATS_HOME_TEMP_TOUCH;
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
     * This system property should be set for consistency.
     */
    protected void setCatsHomeSystemProperty()
    {
        System.setProperty( CATS_HOME_SYSTEM_PROPERTY, catsHome );
    }

    protected String getDefaultCatsHome()
    {
        String defaultHome = "";
        String userHome = System.getProperty( "user.home" );
        if ( null != userHome )
        {
            logger.info( "user.home not found!" );
            defaultHome = userHome;
        }
        defaultHome += FILE_SEPERATOR + CATS_HOME_RELATIVE_DIR;
        return defaultHome;
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
        return "CatsHome[" + catsHome + "]\n" + "TmpHome[" + tmpHome + "]\n";
    }
}