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
package com.comcast.cats.vision;

import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.mock.configuration.CatsFrameworkLocal;

/**
 * Simple Testcase with local context
 * 
 * @author subinsugunan
 * 
 */
public abstract class CatsAbstarctTestCase
{
    protected static final String SERVER_BASE = "http://cats-dev:8080/";
    protected static final String AUTH_TOKEN  = "8520";

    protected CatsFramework       framework;

    @Before
    public void init()
    {
        framework = new CatsFrameworkLocal();
    }

    @BeforeClass
    public static void setup()
    {
        System.setProperty( CatsProperties.SERVER_URL_PROPERTY, SERVER_BASE );
        System.setProperty( CatsProperties.AUTH_TOKEN_PROPERTY, AUTH_TOKEN );
    }

    protected ApplicationContext getContext()
    {
        return framework.getContext();
    }
}
