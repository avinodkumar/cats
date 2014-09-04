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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.domain.configuration.CatsProperties;

/**
 * 
 * @author subinsugunan
 * 
 */
public class CatsFrameworkTest
{
    private static final String  SERVER_BASE = "http://localhost:8080/";
    private static final String  AUTH_TOKEN  = "/8520";
    private static CatsFramework catsFramework;

    @BeforeClass
    public static void setup()
    {
        System.setProperty( CatsProperties.SERVER_URL_PROPERTY, SERVER_BASE );
        System.setProperty( CatsProperties.AUTH_TOKEN_PROPERTY, AUTH_TOKEN );
    }

    @Ignore
    @Test( expected = RuntimeException.class )
    public void testFrameworkNull()
    {
        CatsFramework.getInstance();
    }

    @Test
    public void testFramework()
    {
        catsFramework = new CatsFramework();

        assertNotNull( catsFramework.getSettopFactory() );
        assertNotNull( catsFramework.getContext() );
        assertNotNull( catsFramework.getSettopLocker() );
        assertNotNull( catsFramework.getSettopLookupService() );
        assertNotNull( CatsFramework.getInstance() );
    }

    @Test
    public void testFrameworkWithContext()
    {
        catsFramework = new CatsFramework( new CatsContext() );

        assertNotNull( catsFramework.getSettopFactory() );
        assertNotNull( catsFramework.getCatsProperties() );
        assertNotNull( catsFramework.getContext() );
        assertNotNull( catsFramework.getSettopLocker() );
        assertNotNull( catsFramework.getSettopLookupService() );
        assertNotNull( CatsFramework.getInstance() );
    }

    @Ignore
    @Test
    public void testFrameworkExclusiveAccess()
    {

        CatsFramework framework1 = new CatsFramework();
        assertNotNull( framework1 );
        Settop settop1 = null;

        try
        {
            settop1 = framework1.getSettopFactory().findSettopByHostMac( "00:21:1E:EA:7E:CD", true );
            // Expected result : The @Exclusive check should happen when we
            // invoke the below
            // settop operations.
            settop1.getPowerStatus();
            settop1.powerOff();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail();
        }
    }

}
