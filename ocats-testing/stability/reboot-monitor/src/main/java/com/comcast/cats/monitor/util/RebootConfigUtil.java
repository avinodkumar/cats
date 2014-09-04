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
package com.comcast.cats.monitor.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.decorator.SettopFamilyResolver;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.provider.exceptions.ProviderCreationException;

/**
 * Application configuration utility.
 * 
 * @author SSugun00c
 * 
 */
public class RebootConfigUtil
{
    private static final Logger          LOGGER                           = LoggerFactory.getLogger( RebootConfigUtil.class );

    public static final String           CONFIG_FILE_LOCATION             = "/cats/config/reboot-config.properties";
    public static final String           TRCAE_FILE_NAME                  = "trace.log";
    public static final String           CATS_HOME                        = "CATS_HOME";

    private static final String          HYPHEN                           = ":";
    private static final String          EMPTY_STRING                     = "";

    private static final String          CMD2000                          = ".cmd2000";
    private static final String          SERIAL                           = ".serial";
    private static final String          CMD2000_LOG_FILE_DIRECTORY       = "cmd2000.log.file.directory";
    private static final String          CMD2000_FTP_USER                 = "cmd2000.ftp.user";
    private static final String          CMD2000_FTP_PASSWORD             = "cmd2000.ftp.password";
    private static final String          CMD2000_DATE_FORMAT              = "yyMMdd";
    private static final String          CMD2000_FILE_PREFIX              = "cmd2000_log_";
    private static final String          CMD2000_FILE_EXTENSION           = ".txt";
    private static final String          CONTROLLER                       = "Controller";

    private static Properties            properties                       = new Properties();

    private static Map< String, String > serialLogRegexMap                = new HashMap< String, String >();
    private static Map< String, String > cmd2000LogRegexMap               = new HashMap< String, String >();

    private static String                cmd2000LogFileDirectory          = null;
    private static String                cmd2000FtpUser                   = null;
    private static String                cmd2000FtpPassword               = null;

    private static List< String >        ciscoLegacySettopTypes           = new ArrayList< String >();

    static
    {
        readRebootConfigFile();
        processConfigProperties();
        processSettopFamilyProperties();
    }

    private RebootConfigUtil()
    {
    }

    /**
     * Get the absolute path of trace file.
     * 
     * @param settop
     * @return
     */
    public static String getTraceFilepath( Settop settop )
    {
        File traceDir = new File( getCatsHomeDirectory(), getSettopDirectory( settop.getHostMacAddress() ) );
        File traceFile = new File( traceDir, TRCAE_FILE_NAME );

        return traceFile.getPath();
    }

    /**
     * Get the absolute path of CATS_HOME.
     * 
     * @return
     */
    public static String getCatsHomeDirectory()
    {

        return System.getenv().get( CATS_HOME );
    }

    /**
     * Get the directory name of {@link Settop} under CATS_HOME.
     * 
     * @param hostMacAddress
     * @return
     */
    public static String getSettopDirectory( String hostMacAddress )
    {
        return hostMacAddress.replaceAll( HYPHEN, EMPTY_STRING );
    }

    /**
     * Reads property file from CATS server and loads it to a {@link Properties}
     * instance.
     */
    private static void readRebootConfigFile()
    {
        URL configUrl = null;
        try
        {
            LOGGER.info( "Reading regex mapping file" );
            configUrl = new URL( getRebootConfigFileLocation() );
            properties.load( configUrl.openStream() );
            LOGGER.info( "Reading regex mapping file completed: " + properties );
        }
        catch ( Exception e )
        {
            LOGGER.error( "RebootConfigUtil failed to load properties from config file. Is " + configUrl
                    + " is a valid url ?. More info : " + e.getMessage() );
        }
    }

    /**
     * Returns the absolute path to the config file.
     * 
     * @return
     * @throws ProviderCreationException
     */
    public static String getRebootConfigFileLocation()
    {
        return "http://" + getServerHost() + CONFIG_FILE_LOCATION;
    }

    /**
     * Returns the absolute path to the settop mapping file.
     * 
     * @return
     * @throws ProviderCreationException
     */
    public static String getSettopFamilyMappingConfigLocation()
    {
        return "http://" + getServerHost() + SettopFamilyResolver.SETTOP_MAPPPING_CONFIG_LOCATION;
    }

    /**
     * Returns host name or IP.
     * 
     * @return
     * @throws URISyntaxException
     */
    public static String getServerHost()
    {
        String host = CatsProperties.DEFAULT_CATS_SERVER;

        String serverUrl = getServerUrl();

        if ( null != serverUrl && !serverUrl.isEmpty() )
        {
            URI uri;
            try
            {
                uri = new URI( serverUrl );

                if ( null != uri )
                {
                    host = uri.getHost();
                }
            }
            catch ( URISyntaxException e )
            {
                LOGGER.warn( "RebootConfigUtil failed to retieve settop controller mapping file. Invalid host. More infor : "
                        + e.getMessage() );
            }
        }

        if ( null == host )
        {
            // Reset
            host = CatsProperties.DEFAULT_CATS_SERVER;
        }

        return host;
    }

    /**
     * Reads system property cats.server.url.
     * 
     * @return
     */
    public static String getServerUrl()
    {
        return System.getProperty( CatsProperties.SERVER_URL_PROPERTY );
    }

    /**
     * Setup the regular expression mapping file. [key=settop_type,value=regex].
     */
    private static void processConfigProperties()
    {
        String regexKey = null;
        for ( Object key : properties.keySet() )
        {
            regexKey = ( String ) key;

            if ( regexKey.endsWith( CMD2000 ) )
            {
                cmd2000LogRegexMap.put( regexKey, ( String ) properties.get( key ) );
            }
            else if ( regexKey.endsWith( SERIAL ) )
            {
                serialLogRegexMap.put( regexKey, ( String ) properties.get( key ) );
            }
        }

        cmd2000LogFileDirectory = properties.getProperty( CMD2000_LOG_FILE_DIRECTORY );
        cmd2000FtpUser = properties.getProperty( CMD2000_FTP_USER );
        cmd2000FtpPassword = properties.getProperty( CMD2000_FTP_PASSWORD );

    }

    /**
     * Process settop family mapping.
     */
    private static void processSettopFamilyProperties()
    {
        Properties properties = new Properties();
        URL catsConfigUrl = null;
        try
        {
            catsConfigUrl = new URL( getSettopFamilyMappingConfigLocation() );
            properties.load( catsConfigUrl.openStream() );

            String ciscoLegacySettopTypesCSV = ( String ) properties.get( SettopConstants.SETTOP_FAMILY_CISCO_LEGACY );
            StringTokenizer stringTokenizer = new StringTokenizer( ciscoLegacySettopTypesCSV, "," );

            while ( stringTokenizer.hasMoreElements() )
            {
                ciscoLegacySettopTypes.add( stringTokenizer.nextElement().toString().trim() );
            }
        }
        catch ( Exception e )
        {
            LOGGER.error( "RebootConfigUtil failed to load properties from Settop type mapping file. Is "
                    + catsConfigUrl + " is a valid url ?. More info : " + e.getMessage() );
        }
    }

    /**
     * Returns all regular expression as a {@link List}
     * 
     * @return
     */
    public static List< String > getAllRegex()
    {
        List< String > regexList = new ArrayList< String >( serialLogRegexMap.values() );
        regexList.addAll( cmd2000LogRegexMap.values() );

        return regexList;
    }

    /**
     * Returns CMD2000 log's regular expression as a {@link List}
     * 
     * @return
     */
    public static List< String > getAllCMD2000Regex()
    {
        List< String > regexList = new ArrayList< String >( cmd2000LogRegexMap.values() );
        return regexList;
    }

    /**
     * Returns Serial log's regular expression as a {@link List}
     * 
     * @return
     */
    public static List< String > getAllSerialRegex()
    {
        List< String > regexList = new ArrayList< String >( serialLogRegexMap.values() );
        return regexList;
    }

    public static String getCmd2000LogFileDirectory()
    {
        return cmd2000LogFileDirectory;
    }

    public static String getCmd2000FtpUsername()
    {
        return cmd2000FtpUser;
    }

    public static String getCmd2000FtpPassword()
    {
        return cmd2000FtpPassword;
    }

    public static List< String > getCiscoLegacySettopTypes()
    {
        return ciscoLegacySettopTypes;
    }

    public static String getCmd2000LogFileName()
    {
        String filename = CMD2000_FILE_PREFIX + new SimpleDateFormat( CMD2000_DATE_FORMAT ).format( new Date() )
                + CMD2000_FILE_EXTENSION;

        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.debug( "Generated CMD2000 log file name [" + filename + "]" );
        }

        return filename;
    }

    /**
     * This is part of
     * https://evc.io.comcast.net/cats/trunk/services/digital-controller
     * /dncs/war /src/main/java/com/comcast/cats/service/impl/
     * ControllerAddressMapperImpl .java
     * 
     * We need to move this to a common place until the Plant information is
     * available in CHIMPS.
     * 
     * @param settop
     * @return
     */
    public static String getCmd2000Host( Settop settop )
    {
        // FIXME: How to do this. For the time being I'm hard coding this
        String cmd200Host = null;

        String controllerName = settop.getExtraProperties().get( CONTROLLER );

        if ( null != controllerName && !controllerName.isEmpty() )
        {
            for ( String key : getAddressMap().keySet() )
            {
                if ( controllerName.toUpperCase().contains( key ) )
                {
                    cmd200Host = getAddressMap().get( controllerName.toUpperCase() );
                }
            }
        }

        return cmd200Host;
    }

    /**
     * hard coded data. We will keep this until the Plant information is
     * available in CHIMPS.
     * 
     * @return
     */
    private static Map< String, String > getAddressMap()
    {
        Map< String, String > addressMap = new HashMap< String, String >();
        /**
         * List of DNCS Controllers.
         */
        addressMap.put( "DNCS1", "10.253.228.131" );
        addressMap.put( "DNCS2", "10.252.150.86" );
        addressMap.put( "DNCS6", "10.253.63.84" );
        addressMap.put( "DNCS7", "10.252.150.66" );
        addressMap.put( "DNCS8", "10.252.150.78" );
        /**
         * List of BOSS servers for DTACS controllers.
         */
        addressMap.put( "DTACS6", "10.253.63.85" );
        return addressMap;
    }

}
