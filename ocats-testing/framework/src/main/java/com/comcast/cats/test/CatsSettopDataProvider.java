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
package com.comcast.cats.test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopInstantiationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;

public class CatsSettopDataProvider
{
    public static final String      SETTOP_LIST_PROVIDER         = "SettopListProvider";
    public static final String      SETTOP_LIST_PROVIDER_NO_LOCK = "SettopListProviderNoLock";
    public static final String      SETTOP_GROUP_PROVIDER        = "SettopGroupProvider";
    protected static final String   CATS_SETTOP_LIST_PROP        = "cats.settop.list";
    protected static final String     CATS_SETTOP_GROUP_PROP       = "cats.settop.group";
    protected static String         macList;

    protected static List< Settop > settopList                   = new CopyOnWriteArrayList< Settop >();
    protected static CatsProperties                catsProperties;
    protected static SettopExclusiveAccessEnforcer settopLocker;
    protected static SettopFactory                 settopFactory;

    private static final Logger        LOGGER                       = LoggerFactory.getLogger( CatsSettopDataProvider.class );
    protected static String           settop_group;
    protected static CatsFramework catsFramework;
    
    public static CatsFramework getCatsFramework()
    {
        return catsFramework;
    }

    public static void setCatsFramework( CatsFramework framework )
    {
        catsFramework = framework;
        settopFactory = catsFramework.getSettopFactory();
        settopLocker = catsFramework.getSettopLocker();
        catsProperties = catsFramework.getCatsProperties();
        macList = catsProperties.getProperty( CATS_SETTOP_LIST_PROP );
        settop_group = catsProperties.getProperty( CATS_SETTOP_GROUP_PROP );
    }

    @DataProvider( name = SETTOP_LIST_PROVIDER, parallel = true )
    public static Object[][] getSettops()
    {
        validateCatsFramework();
        if ( settopList.isEmpty() )
        {
            if ( ( null != macList ) && !( macList.isEmpty() ) )
            {
                String[] macs = macList.split( "," );
                
                for ( String mac : macs )
                {
                    Settop settop = null;
                    if ( mac != null )
                    {
                        try
                        {
                            mac = mac.trim();
                            settop = settopFactory.findSettopByHostMac( mac );

                            if ( ( null == settop.getRemote() ) || ( null == settop.getPower() ) )
                            {
                                throw new SettopInstantiationException( "Settop creation failed for mac = " + mac );
                            }

                            // FIXME: Do a contetType check before allocating.
                            // CVS
                            // requirement
                            settopLocker.lock( settop );
                            settopList.add( settop );
                        }
                        catch ( Exception e )
                        {
                            LOGGER.error( "An exception occured while trying to allocate [" + mac
                                    + "]. The actual error message was: " + e.getMessage() );
                        }
                    }
                }
            }
        }

        Object[][] settops = new Object[ settopList.size() ][ 1 ];

        int index = 0;
        for ( Settop settop : settopList )
        {
            settops[ index++ ][ 0 ] = settop;
        }
        return settops;
    }


    private static void validateCatsFramework()
    {   
        if(catsFramework == null){
            throw new IllegalStateException("CATSFramework not set.");
        }
    }

    /**
     * Retrieves settops without actually locking it.
     * 
     * @param m
     * @return
     */
    @DataProvider( name = SETTOP_LIST_PROVIDER_NO_LOCK, parallel = true )
    public static Object[][] getSettopsWithoutLock( Method m )
    {
        validateCatsFramework();
        if ( settopList.isEmpty() )
        {
            String[] macs = macList.split( "," );
            for ( String mac : macs )
            {
                Settop settop = null;
                if ( mac != null )
                {
                    try
                    {
                        mac = mac.trim();
                        settop = settopFactory.findSettopByHostMac( mac );
                        if ( ( null == settop.getRemote() ) || ( null == settop.getPower() ) )
                        {
                            throw new SettopInstantiationException( "Settop creation failed for mac = " + mac );
                        }
                        settopList.add( settop );
                    }
                    catch ( Exception e )
                    {
                        LOGGER.error( "An exception occured while trying to initilize [" + mac
                                + "]. The actual error message was: " + e.getMessage() );
                    }
                }
            }
        }

        Object[][] settops = new Object[ settopList.size() ][ 1 ];

        int index = 0;
        for ( Settop settop : settopList )
        {
            settops[ index++ ][ 0 ] = settop;
        }
        return settops;
    }

    @DataProvider( name = SETTOP_GROUP_PROVIDER, parallel = true )
    public static Object[][] getSettopsByGroup( Method m )
    {
        validateCatsFramework();
        if ( settopList.isEmpty() && settop_group != null )
        {
            settop_group = settop_group.trim();
            try
            {
                settopList = settopFactory.findAllSettopByGroupName( settop_group );
            }
            catch ( SettopNotFoundException e1 )
            {
                LOGGER.error( "An exception occurred while trying to retrieve settops from a group " + e1.getMessage() );
            }

            for ( Settop settop : settopList )
            {
                try
                {
                    if ( ( settop == null ) || ( null == settop.getRemote() ) || ( null == settop.getPower() ) )
                    {
                        throw new SettopInstantiationException( "Settop creation failed for settop group = "
                                + settop_group );
                    }
                    settopLocker.lock( settop );
                }
                catch ( Exception e )
                {
                    LOGGER.error( "An exception occured while trying to allocate settops from [" + settop_group
                            + "]. The actual error message was: " + e.getMessage() );
                }
            }
        }

        Object[][] settops = new Object[ settopList.size() ][ 1 ];

        int index = 0;
        for ( Settop settop : settopList )
        {
            settops[ index++ ][ 0 ] = settop;
        }
        return settops;
    }

    public static void releaseSettops()
    {
        validateCatsFramework();
        for ( Settop s : settopList )
        {
            try
            {
                settopLocker.release( s );
            }
            catch ( AllocationException e )
            {
                e.printStackTrace();
            }
            settopList.remove( s );
        }
    }

}
