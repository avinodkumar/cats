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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.SettopConstants;
import com.comcast.cats.domain.AllocationCategory;

/**
 * 
 * @author cfrede001
 * 
 */
@Named
public class CatsProperties extends Properties
{
    /**
     * 
     */
    private static final long  serialVersionUID                 = -1610598488861890712L;
    private static Logger      logger                           = LoggerFactory.getLogger( CatsProperties.class );

    public static final String DEFAULT_CATS_SERVER              = "localhost";

    public static final String CATS_PROPS_FILENAME              = "cats.props";
    public static final String AUTH_TOKEN_PROPERTY              = "cats.user.authToken";
    public static final String SERVER_URL_PROPERTY              = "cats.server.url";
    public static final String CONFIG_SERVER_URL_PROPERTY       = "cats.config.url";
    public static final String USERNAME_PROPERTY                = "cats.user.name";
    public static final String USER_EMAIL_PROPERTY              = "cats.user.email";
    public static final String USER_FULLNAME_PROPERTY           = "cats.user.fullname";
    public static final String SETTOP_URL_PROPERTY              = "settop.url";
    public static final String SETTOP_DEFAULT_PROPERTY          = "cats.settop.default";
    public static final String STRINGS_DM_PROPERTY              = "strings.dm.location";
    public static final String OCR_SERVER_URL_PROPERTY          = "cats.ocr.server.url";
    public static final String ALLOCATION_CATEGORY_PROPERTY     = "cats.settop.allocationCategory";
    public static final String ALLOCATION_DURATION_PROPERTY     = "cats.lock.duration";
    public static final String SERVICE_TIMEOUT_PROPERTY         = "cats.service.timeout";
    public static final String SERVICE_CONNECT_TIMEOUT_PROPERTY = "cats.service.connect.timeout";
    public static final String SERVICE_REQUEST_TIMEOUT_PROPERTY = "cats.service.request.timeout";

    protected CatsHome         catsHome;
    protected String           catsPropsFileName;

    /**
     * Constructor
     * 
     * @param catsHome
     *            {@link CatsHome}
     */
    @Inject
    protected CatsProperties( CatsHome catsHome ) throws IOException
    {
        super();
        this.catsHome = catsHome;
        this.catsPropsFileName = createCatsPropsFileName( catsHome.getCatsHomeDirectory() );
        loadPropertiesFromFile();
    }

    /**
     * This method check if a value was set in the properties file for
     * allocation duration and Category. For Allocaiton duration if a value
     * greater than 4 and a valid integer is set,take the value else default to
     * 30mins. Similarly set an appropriate Allocation category value after
     * validation.
     */
    @PostConstruct
    protected void initDurationAndCategory()
    {
        logger.debug( "Doing init of allocation duratino and allocation category" );
        int duration = SettopConstants.DEFAULT_ALLOCATION_TIME_IN_MINS;
        String durationFromProps = loadProperty( ALLOCATION_DURATION_PROPERTY );
        if ( null != durationFromProps && !durationFromProps.isEmpty() )
        {
            try
            {
                duration = Integer.valueOf( durationFromProps );
                if ( duration < SettopConstants.MIN_ALLOCATION_TIME_IN_MINS )
                {
                    logger.info( "Setting default allocaiton duration, value set in cats.props:" + duration
                            + " is less than min value" );
                    duration = SettopConstants.DEFAULT_ALLOCATION_TIME_IN_MINS;
                }
            }
            catch ( NumberFormatException e )
            {
                logger.error( "Invalid value for cats.lock.duration set in cats.props" + e.getMessage() );
            }
        }
        // set the allocation duration property
        setProperty( ALLOCATION_DURATION_PROPERTY, String.valueOf( duration ) );
        // Set the correct value for Allocation category.
        AllocationCategory allocationCategory = AllocationCategory.UNKNOWN;
        String allocationCategoryPropString = loadProperty( ALLOCATION_CATEGORY_PROPERTY );
        // if the allocation category is not set in the cats.props file default
        // to unknown
        if ( null != allocationCategoryPropString && !( "" ).equalsIgnoreCase( allocationCategoryPropString ) )
        {
            try
            {
                allocationCategory = AllocationCategory.valueOf( allocationCategoryPropString );
            }
            catch ( IllegalArgumentException e )
            {
                logger.error( "invalid string mentioned as allocatoin category" + e.getMessage() );
            }
        }
        // set the allocation category
        setProperty( ALLOCATION_CATEGORY_PROPERTY, String.valueOf( allocationCategory ) );
        logger.info( "AllocationCategory :" + allocationCategory + " Allocation duration:" + duration );
        logger.info( toString() );
    }

    private String createCatsPropsFileName( String catsHomeDir )
    {
        return catsHomeDir + File.separator + CATS_PROPS_FILENAME;
    }

    /**
     * To load properties from properties file.
     */
    protected void loadPropertiesFromFile() throws IOException
    {
        /**
         * Touch the cats props to make sure we have one. If one exists the
         * updated time will be changed.
         */
        logger.info( "Loading cats.props [" + catsPropsFileName + "]" );

        File catsPropsFile = new File( catsPropsFileName );

        try
        {
            // This should not stop execution.
            FileUtils.touch( catsPropsFile );
        }
        catch ( IOException ioException )
        {
            logger.warn( ioException.getMessage() );
        }

        this.load( new FileInputStream( catsPropsFile ) );
    }

    /**
     * To get the cats home path.
     * 
     * @return CatsHome {@link CatsHome}
     */
    public CatsHome getCatsHome()
    {
        return catsHome;
    }

    private void storeProperty( String key, String value )
    {
        if ( null == value )
        {
            logger.warn( "Key[" + key + "] is NULL" );
            // It is important to not set a null value, so just return here.
            return;
        }
        logger.info( "Key[" + key + "] = " + value );
        this.setProperty( key, value );
    }

    /**
     * We need provide option to override cats properties using system
     * properties.
     * 
     * @param key
     * @return
     */
    private String loadProperty( String key )
    {
        String value = System.getProperty( key );

        if ( ( null == value ) || ( value.isEmpty() ) )
        {
            value = this.getProperty( key );
        }

        return value;
    }

    /**
     * To get the server url.
     * 
     * @return String
     */
    public String getServerUrl()
    {
        return loadProperty( SERVER_URL_PROPERTY );
    }

    /**
     * For CATS Vision to work correctly, we need to determine the server
     * address by either the systemProperties or from the cats.props file. The
     * System properties should be favored by default.
     * 
     * @param serverUrl
     *            - Spring determined cats server base url eg.
     *            http://localhost:8080/
     */
    public void setServerUrl( String serverUrl )
    {
        storeProperty( SERVER_URL_PROPERTY, serverUrl );
    }

    /**
     * To get the Config server url.
     * 
     * @return String
     */
    public String getConfigServerUrl()
    {
        return loadProperty( CONFIG_SERVER_URL_PROPERTY );
    }

    /**
     * To set the Config server url.
     * 
     * @param configServerUrl
     */
    public void setConfigServerUrl( String configServerUrl )
    {
        storeProperty( CONFIG_SERVER_URL_PROPERTY, configServerUrl );
    }

    /**
     * To get the Auth token.
     * 
     * @return String
     */
    public String getAuthToken()
    {
        return loadProperty( AUTH_TOKEN_PROPERTY );
    }

    /**
     * See statement for setServerUrl above.
     * 
     * @param authToken
     *            - This token will be used to verify identity of user for requests.
     */
    public void setAuthToken( String authToken )
    {
        storeProperty( AUTH_TOKEN_PROPERTY, authToken );
    }

    /**
     * To get the type of Allocation Category.
     * 
     * @return AllocationCategory {@link AllocationCategory}
     */
    public AllocationCategory getAllocationCategory()
    {
        /**
         * No data validation required here as all the validation is done while
         * setting the value.Extra data validation looks like an overhead here.
         */
        String allocationCategoryPropString = loadProperty( ALLOCATION_CATEGORY_PROPERTY );
        AllocationCategory allocationCategory = AllocationCategory.valueOf( allocationCategoryPropString );
        return allocationCategory;
    }

    /**
     * To set the allocation category.
     * 
     * @param allocationCategory
     */
    public void setAllocationCategory( String allocationCategory )
    {
        storeProperty( ALLOCATION_CATEGORY_PROPERTY, allocationCategory );
    }

    /**
     * To get the Allocation duration.
     * 
     * @return int
     */
    public Integer getAllocationDuration()
    {
        String allocationDuration = loadProperty( ALLOCATION_DURATION_PROPERTY );
        /**
         * No data validation required here as all the validation is done while
         * setting the value.Extra data validation looks like an overhead here.
         */
        int duration = Integer.valueOf( allocationDuration );
        logger.info( "Returning Allocation duratino value" + duration );
        return duration;

    }

    /**
     * Set the allocation duration to properties.
     * 
     * @param duration
     */
    public void setAllocationDuration( String duration )
    {
        logger.info( "Allocation duraiton value set to" + duration );
        storeProperty( ALLOCATION_DURATION_PROPERTY, duration );
    }

    /**
     * To get the User name.
     * 
     * @return String
     */
    public String getUsername()
    {
        return loadProperty( USERNAME_PROPERTY );
    }

    /**
     * To set the User name.
     * 
     * @param name
     */
    public void setUsername( String name )
    {
        storeProperty( USERNAME_PROPERTY, name );
    }

    /**
     * To get the User email.
     * 
     * @return String
     */
    public String getEmail()
    {
        return loadProperty( USER_EMAIL_PROPERTY );
    }

    /**
     * To set the User email.
     * 
     * @param email
     */
    public void setEmail( String email )
    {
        storeProperty( USER_EMAIL_PROPERTY, email );
    }

    /**
     * To get the User full name.
     * 
     * @return String
     */
    public String getFullname()
    {
        return loadProperty( USER_FULLNAME_PROPERTY );
    }

    /**
     * To set the User full name.
     * 
     * @param fullname
     */
    public void setFullname( String fullname )
    {
        storeProperty( USER_FULLNAME_PROPERTY, fullname );
    }

    /**
     * To get the Input file path.
     * 
     * @return String
     */
    public String getInputFilePath()
    {
        return loadProperty( SETTOP_URL_PROPERTY );
    }

    /**
     * To set the input file path.
     * 
     * @param inputFilePath
     */
    public void setInputFilePath( String inputFilePath )
    {
        storeProperty( SETTOP_URL_PROPERTY, inputFilePath );
    }

    /**
     * To get the string DM FilePath.
     * 
     * 
     * @return String
     */
    public String getStringsDMLocation()
    {
        return loadProperty( STRINGS_DM_PROPERTY );
    }

    /**
     * To set the String DM file path.
     * 
     * @param stringDMFilePath
     */
    public void setStringsDMLocation( String stringDMFilePath )
    {
        storeProperty( STRINGS_DM_PROPERTY, stringDMFilePath );
    }

    /**
     * To get the Default SettopMac.
     * 
     * @return String
     */
    public String getDefaultSettopMac()
    {
        return loadProperty( SETTOP_DEFAULT_PROPERTY );
    }

    /**
     * To set the Default SettopMac.
     * 
     * @param settopMac
     */
    public void setDefaultSettopMac( String settopMac )
    {
        storeProperty( SETTOP_DEFAULT_PROPERTY, settopMac );
    }

    /**
     * To get the OcrServer Url.
     * 
     * @return String
     */
    public String getOcrServerUrl()
    {
        return loadProperty( OCR_SERVER_URL_PROPERTY );
    }

    /**
     * For CATS Vision to work correctly, we need to determine the server
     * address by either the systemProperties or from the cats.props file. The
     * System properties should be favored by default.
     * 
     * @param ocrServerUrl
     *            - Spring determined cats ocr server base url
     */
    public void setOcrServerUrl( String ocrServerUrl )
    {
        storeProperty( OCR_SERVER_URL_PROPERTY, ocrServerUrl );
    }

    /**
     * To get the Server Host.
     * 
     * @return String
     * @throws URISyntaxException
     */
    public String getServerHost() throws URISyntaxException
    {
        String host = null;

        if ( null != getServerUrl() )
        {
            URI uri = new URI( getServerUrl() );
            host = uri.getHost();
        }

        return host;
    }

    @Override
    public String toString()
    {
        return "CatsProperties [authToken=" + getAuthToken() + ", serverUrl=" + getServerUrl() + ", configServerUrl="
                + getConfigServerUrl() + ", inputFilePath=" + getInputFilePath() + ", username=" + getUsername()
                + ", fullname=" + getFullname() + ", email=" + getEmail() + ", stringsDMFileLocation="
                + getStringsDMLocation() + ", defaultSettopMac=" + getDefaultSettopMac() + ", ocrServerUrl="
                + getOcrServerUrl() + ", cats.props=" + super.toString() + "]";
    }
}
