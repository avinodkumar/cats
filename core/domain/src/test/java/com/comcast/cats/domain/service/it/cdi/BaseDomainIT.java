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
package com.comcast.cats.domain.service.it.cdi;

import javax.enterprise.inject.Instance;

import junit.framework.Assert;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;

import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.test.PropertyUtil;

/**
 * Base test case for all integration tests using Weld.
 * 
 * @author subinsugunan
 * 
 */
public class BaseDomainIT
{
    protected final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    private Weld           weld;
    private WeldContainer  container;

    protected PropertyUtil testProperties;
    protected DataProvider dataProvider;

    protected int          offset;
    protected int          count;

    @Before
    public void setup()
    {
        weld = new Weld();
        container = weld.initialize();

        testProperties = new PropertyUtil();
        dataProvider = DataProvider.getInstance();

        offset = testProperties.getOffset();
        count = testProperties.getCount();
    }

    protected Instance< Object > container()
    {
        return container.instance();
    }

    @Test
    public void testContext()
    {
        Assert.assertNotNull( testProperties );
        Assert.assertNotNull( testProperties.getMacId() );
        Assert.assertNotNull( testProperties.getReservationId() );
    }

    protected void logResult( int size )
    {
        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.debug( size + " objects found" );
        }
    }

    @AfterMethod
    public void tearDown()
    {
        weld.shutdown();
    }
}
