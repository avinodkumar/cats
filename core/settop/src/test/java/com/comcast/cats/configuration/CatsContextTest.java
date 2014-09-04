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
package com.comcast.cats.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.configuration.CatsProperties;

/**
 * 
 * @author subinsugunan
 * 
 */
public class CatsContextTest
{
    private static final String SERVER_BASE = "http://cats-dev:8080/";
    private static final String AUTH_TOKEN  = "8520";
    private static CatsContext  ctx;

    @BeforeClass
    public static void setup()
    {
        System.setProperty( CatsProperties.SERVER_URL_PROPERTY, SERVER_BASE );
        System.setProperty( CatsProperties.AUTH_TOKEN_PROPERTY, AUTH_TOKEN );

        ctx = new CatsContext();
        assertNotNull( ctx );
        ctx.refresh();
    }

    @Test
    public void testContext()
    {
        assertNotNull( ctx.getBean( SettopFactory.class ) );
        assertNotNull( ctx.getBean( CatsProperties.class ) );
    }
}
