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
package com.comcast.cats.testng;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;

/**
 * Settop Provider for TestNG projects.
 * 
 * @author subinsugunan
 * 
 */
public class SettopProvider
{
    public static final CatsFramework                 catsFramework;
    public static final SettopFactory                 settopFactory;
    public static final SettopExclusiveAccessEnforcer settopExclusiveAccessEnforcer;
    public static final CatsProperties                catsProperties;
    public static final String                        SETTOP_DATAPROVIDER = "settops";
    private static Logger                             logger              = LoggerFactory.getLogger( SettopProvider.class );

    static
    {
        catsFramework = CatsFramework.getInstance();
        settopFactory = catsFramework.getSettopFactory();
        settopExclusiveAccessEnforcer = catsFramework.getSettopLocker();
        catsProperties = catsFramework.getCatsProperties();
    }

    /**
     * This method will return the list of successfully allocated settops.
     * 
     * @return array of settop objects
     * @throws Exception
     */

    @DataProvider( name = SETTOP_DATAPROVIDER )
    public static Object[][] getSettops() throws Exception
    {
        List< Settop > settopList = new ArrayList< Settop >();
        SettopXmlDataParser dataParser = new SettopXmlDataParser();

        if ( null != catsProperties.getInputFilePath() )
        {
            dataParser.parseTestData( catsProperties.getInputFilePath() );
        }
        else
        {
            throw new IllegalArgumentException(
                    "Invalid input file path. Please check \'settop.url\' in cats.props under CATS_HOME. Current "
                            + catsProperties.toString() );
        }

        for ( String mac : dataParser.getSettopMap().keySet() )
        {
            Settop settop = null;

            try
            {
                settop = settopFactory.findSettopByHostMac( mac );
                // FIXME: Do a contetType check before allocating.
                settopExclusiveAccessEnforcer.lock( settop );
                settopList.add( settop );
            }
            catch ( Exception e )
            {
                logger.error( "An exception occured while trying to allocate [" + mac
                        + "]. The actual error message was: " + e.getMessage() );
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
}