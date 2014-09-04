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
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

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
    private static final long  serialVersionUID           = -1610598488861890712L;
    private static Logger      logger                     = Logger.getLogger( CatsProperties.class );

    public static final String DEFAULT_CATS_SERVER        = "localhost";

    public static final String CATS_PROPS_FILENAME        = "cats.props";
    public static final String AUTH_TOKEN_PROPERTY        = "cats.user.authToken";
    public static final String SERVER_URL_PROPERTY        = "cats.server.url";
    public static final String CONFIG_SERVER_URL_PROPERTY = "cats.config.url";
    public static final String USERNAME_PROPERTY          = "cats.user.name";
    public static final String USER_EMAIL_PROPERTY        = "cats.user.email";
    public static final String USER_FULLNAME_PROPERTY     = "cats.user.fullname";
    public static final String SETTOP_URL_PROPERTY        = "settop.url";
    public static final String SETTOP_DEFAULT_PROPERTY    = "cats.settop.default";
    public static final String STRINGS_DM_PROPERTY        = "strings.dm.location";
    public static final String OCR_SERVER_URL_PROPERTY    = "cats.ocr.server.url";

    protected CatsHome         catsHome;
    protected String           catsPropsFileName;

    @Inject
    protected CatsProperties( CatsHome catsHome ) throws IOException
    {
        super();
        this.catsHome = catsHome;
        this.catsPropsFileName = createCatsPropsFileName( catsHome.getCatsHomeDirectory() );
        /*
         * We must load the properties in the contructor so they can be
         * overridden by Spring dependency injection of @Value.
         */
        loadPropertiesFromFile();
    }
    
    @PostConstruct
    public void initDummyProperties(){
        setAuthToken( UUID.randomUUID().toString() );
    }

    private String createCatsPropsFileName( String catsHomeDir )
    {
        return catsHomeDir + CatsHome.FILE_SEPERATOR + CATS_PROPS_FILENAME;
    }

    protected void loadPropertiesFromFile() throws IOException
    {
        /**
         * Touch the cats props to make sure we have one. If one exists the
         * updated time will be changed.
         */
        logger.info( "Loading cats.props [" + catsPropsFileName + "]" );
        File catsPropsFile = new File( catsPropsFileName );

        FileUtils.touch( catsPropsFile );
        this.load( new FileInputStream( catsPropsFile ) );
    }

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

    private String loadProperty( String key )
    {
        return this.getProperty( key );
    }

    public String getServerUrl()
    {
        return loadProperty( SERVER_URL_PROPERTY );
    }

    public String getConfigServerUrl()
    {
        return loadProperty( CONFIG_SERVER_URL_PROPERTY );
    }

    /**
     * This is a tricky Spring SPEL statement. For CATS Vision to work
     * correctly, we need to determine the server address by either the
     * systemProperties or from the cats.props file. The System properties
     * should be favored by default.
     * 
     * @param serverUrl
     *            - Spring determined cats server base url eg.
     */
    @Value( "#{systemProperties['" + SERVER_URL_PROPERTY + "']}" )
    public void setServerUrl( String serverUrl )
    {
        storeProperty( SERVER_URL_PROPERTY, serverUrl );
    }

    public String getAuthToken()
    {
        return loadProperty( AUTH_TOKEN_PROPERTY );
    }

    /**
     * See statement for setServerUrl above.
     * 
     * @param authToken
     *            - This token
     *            will be used to verify identity of user for requests.
     */
    @Value( "#{systemProperties['" + AUTH_TOKEN_PROPERTY + "']}" )
    public void setAuthToken( String authToken )
    {
        storeProperty( AUTH_TOKEN_PROPERTY, authToken );
    }

    @Value( "#{systemProperties['" + CONFIG_SERVER_URL_PROPERTY + "']}" )
    public void setConfigServerUrl( String configServerUrl )
    {
        storeProperty( CONFIG_SERVER_URL_PROPERTY, configServerUrl );
    }

    public String getUsername()
    {
        return loadProperty( USERNAME_PROPERTY );
    }

    @Value( "#{systemProperties['" + USERNAME_PROPERTY + "']}" )
    public void setUsername( String name )
    {
        storeProperty( USERNAME_PROPERTY, name );
    }

    public String getEmail()
    {
        return loadProperty( USER_EMAIL_PROPERTY );
    }

    @Value( "#{systemProperties['" + USER_EMAIL_PROPERTY + "']}" )
    public void setEmail( String email )
    {
        storeProperty( USER_EMAIL_PROPERTY, email );
    }

    public String getFullname()
    {
        return loadProperty( USER_FULLNAME_PROPERTY );
    }

    @Value( "#{systemProperties['" + USER_FULLNAME_PROPERTY + "']}" )
    public void setFullname( String fullname )
    {
        storeProperty( USER_FULLNAME_PROPERTY, fullname );
    }

    @Value( "#{systemProperties['" + SETTOP_URL_PROPERTY + "']}" )
    public void setInputFilePath( String inputFilePath )
    {
        storeProperty( SETTOP_URL_PROPERTY, inputFilePath );
    }

    public String getInputFilePath()
    {
        return loadProperty( SETTOP_URL_PROPERTY );
    }

    @Value( "#{systemProperties['" + STRINGS_DM_PROPERTY + "']}" )
    public void setStringsDMLocation( String stringDMFilePath )
    {
        storeProperty( STRINGS_DM_PROPERTY, stringDMFilePath );
    }

    public String getStringsDMLocation()
    {
        return loadProperty( STRINGS_DM_PROPERTY );
    }

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

    @Value( "#{systemProperties['" + SETTOP_DEFAULT_PROPERTY + "']}" )
    public void setDefaultSettopMac( String settopMac )
    {
        storeProperty( SETTOP_DEFAULT_PROPERTY, settopMac );
    }

    public String getDefaultSettopMac()
    {
        return loadProperty( SETTOP_DEFAULT_PROPERTY );
    }

    @PostConstruct
    private void init() throws IOException
    {
        logger.info( toString() );
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

    public String getOcrServerUrl()
    {
        return loadProperty( OCR_SERVER_URL_PROPERTY );
    }

    /**
     * This is a tricky Spring SPEL statement. For CATS Vision to work
     * correctly, we need to determine the server address by either the
     * systemProperties or from the cats.props file. The System properties
     * should be favored by default.
     * 
     * @param ocrServerUrl
     *            - Spring determined cats ocr server base url
     */
    @Value( "#{systemProperties['" + OCR_SERVER_URL_PROPERTY + "']}" )
    public void setOcrServerUrl( String ocrServerUrl )
    {
        storeProperty( OCR_SERVER_URL_PROPERTY, ocrServerUrl );
    }
}
