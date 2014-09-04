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
package com.comcast.cats.decorator;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.SettopConstants;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.provider.exceptions.ProviderCreationException;

/**
 * The main intend of this class is to create a {@link Map} with
 * {@link SettopDesc} component type as key and the qualifier of
 * {@link SettopDecorator} as value.
 * 
 * This class will read the settop family to component type mapping file from
 * http://cats-server/CATS_CONFIG_LOCATION and creates the required {@link Map}
 * to be used in {@link SettopDecorator}.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class SettopFamilyResolver
{
    @Inject
    CatsProperties                       catsProperties;

    /**
     * KEY = Settop component type, VALUE = {@link SettopDecorator} qualifier
     * for Spring lookup
     */
    private static Map< String, String > decoratorMap                    = new ConcurrentHashMap< String, String >();

    private static Properties            properties                      = new Properties();

    private static final Logger          LOGGER                          = LoggerFactory.getLogger( SettopFamilyResolver.class );

    public static final String           SETTOP_MAPPPING_CONFIG_LOCATION = "/cats/config/settop-type-mapping.properties";

    /**
     * To get the decorator map.
     * 
     * @return map.
     */
    public static Map< String, String > getDecoratorMap()
    {
        return decoratorMap;
    }

    /**
     * This will be called only once in a life time.
     * 
     * @throws ProviderCreationException
     */
    @SuppressWarnings( "unused" )
    @PostConstruct
    private void setupDecoratormap() throws ProviderCreationException
    {
        setupSystemProperties();
        procesSystemPropertiesAndCreateDecoratorMap();
    }

    /**
     * Reads property file from CATS server and loads it to a {@link Properties}
     * instance.
     */
    private void setupSystemProperties()
    {
        URL catsConfigUrl = null;
        try
        {
            catsConfigUrl = new URL( getCatsConfigLocation() );
            properties.load( catsConfigUrl.openStream() );
        }
        catch ( Exception e )
        {
            LOGGER.error( "SettopFamilyResolver failed to load properties from CATS config file. Is " + catsConfigUrl
                    + " is a valid url ?. More info : " + e.getMessage() );
        }
    }

    /**
     * Returns the absolute path to the mapping file.
     * 
     * @return
     * @throws ProviderCreationException
     */
    private String getCatsConfigLocation() throws ProviderCreationException
    {
        String host = null;

        try
        {
            if ( null != catsProperties )
            {
                host = catsProperties.getServerHost();
            }
        }
        catch ( URISyntaxException e )
        {
            LOGGER.warn( "SettopFamilyResolver failed to retieve configuration properties. Invalid cats.server.url. Using default ="
                    + CatsProperties.DEFAULT_CATS_SERVER );
        }

        if ( null == host )
        {
            host = CatsProperties.DEFAULT_CATS_SERVER;
        }

        return "http://" + host + SETTOP_MAPPPING_CONFIG_LOCATION;
    }

    /**
     * Initializes decoratorMap with Key = settop component type and Value =
     * Spring bean qualifier name
     * 
     * @throws ProviderCreationException
     */
    private void procesSystemPropertiesAndCreateDecoratorMap() throws ProviderCreationException
    {
        String propsValue = null;
        StringTokenizer stringTokenizer = null;

        for ( String propsKey : getSettopFamilyMap().keySet() )
        {
            propsValue = properties.getProperty( propsKey );

            if ( null == propsValue )
            {
                LOGGER.warn( "No value found for property " + propsKey + ". Is an entry available for " + propsKey
                        + "  in " + getCatsConfigLocation() );
            }
            else
            {
                stringTokenizer = new StringTokenizer( propsValue, "," );

                while ( stringTokenizer.hasMoreElements() )
                {
                    decoratorMap.put( stringTokenizer.nextElement().toString().trim(),
                            getSettopFamilyMap().get( propsKey ) );
                }
            }

        }

    }

    /**
     * This is an internal map for the system which will hold the mapping
     * between settop family name with actual Spring beans
     * 
     * @return
     */
    private static Map< String, String > getSettopFamilyMap()
    {
        Map< String, String > settopFamilyMap = new HashMap< String, String >();

        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_SAMSUNG, SettopConstants.SETTOP_DECORATOR_SAMSUNG );
        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_RNG, SettopConstants.SETTOP_DECORATOR_RNG );
        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_DTA, SettopConstants.SETTOP_DECORATOR_DTA );
        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_HD_DTA, SettopConstants.SETTOP_DECORATOR_HD_DTA );
        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_MOTOROLA_LEGACY,
                SettopConstants.SETTOP_DECORATOR_MOTOROLA_LEGACY );
        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_CISCO_LEGACY, SettopConstants.SETTOP_DECORATOR_CISCO_LEGACY );
        settopFamilyMap.put( SettopConstants.SETTOP_FAMILY_PARKER_X1, SettopConstants.SETTOP_DECORATOR_PARKER_X1 );

        return settopFamilyMap;
    }

    /**
     * To get the properties.
     * 
     * @return Properties.
     */
    public Properties getProperties()
    {
        return properties;
    }
}
