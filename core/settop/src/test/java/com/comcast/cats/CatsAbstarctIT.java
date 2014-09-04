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
package com.comcast.cats;

import java.io.IOException;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.BeforeClass;

import com.comcast.cats.CatsFramework;

/**
 * Base class for all integration tests.
 * 
 * @author subinsugunan
 * 
 */
public abstract class CatsAbstarctIT
{
    protected final Logger   LOGGER      = LoggerFactory.getLogger( getClass() );

    // protected String macId = "12:BE:20:E5:98:C8";
    protected static final String  SERVER_BASE = "http://locahost:8080/";
    protected static final String  AUTH_TOKEN  = "8520";
    protected static CatsFramework catsFramework;
    protected static PropertyUtil  propertyUtil;

    @BeforeClass
    public static void setup()
    {
        // System.setProperty( CatsProperties.SERVER_URL_PROPERTY, SERVER_BASE
        // );
        // System.setProperty( CatsProperties.AUTH_TOKEN_PROPERTY, AUTH_TOKEN );

        try
        {
            propertyUtil = new PropertyUtil();
        }
        catch ( IOException e )
        {
            Assert.fail( e.getMessage() );
        }
        catsFramework = new CatsFramework();
    }
}
