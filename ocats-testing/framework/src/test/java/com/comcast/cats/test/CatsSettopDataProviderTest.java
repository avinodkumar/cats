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

import org.apache.log4j.Logger;
import org.testng.annotations.Factory;

import com.comcast.cats.Settop;
import comcast.cats.annotation.CatsTestCase;

/**
 * Simple test case to make sure DataProvider is working
 * 
 * @author ssugun00c
 * 
 */
@CatsTestCase( name = "CatsSettopDataProviderTest" )
public class CatsSettopDataProviderTest extends CatsTestWithMonitoring
{
    private static final long   serialVersionUID = 1L;
    private static final Logger LOGGER           = Logger.getLogger( CatsSettopDataProviderTest.class );

    @Factory( dataProvider = CatsSettopDataProvider.SETTOP_LIST_PROVIDER, dataProviderClass = CatsSettopDataProvider.class )
    public CatsSettopDataProviderTest( Settop settop )
    {
        super( settop );
        LOGGER.info( settop );
    }
}