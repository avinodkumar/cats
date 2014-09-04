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
package com.comcast.cats.domain.service;

import javax.inject.Inject;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.test.PropertyUtil;

/**
 * Base test class for all service test classes. {@link EasyMockSupport} and
 * logger will be available to all child classes.
 * 
 * @author subinsugunan
 * 
 */
@ContextConfiguration( locations =
    { "classpath:application-config-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
public class BaseTestCase extends EasyMockSupport
{
    protected final Logger    LOGGER = LoggerFactory.getLogger( getClass() );

    @Inject
    protected PropertyUtil testProperties;

    @Inject
    protected DataProvider dataProvider;

    protected RestTemplate restTemplateMock;
    protected String       property;
    protected int          offset;
    protected int          count;

    @Before
    public void beforeMethod()
    {
        restTemplateMock = createMock( RestTemplate.class );
        offset = testProperties.getOffset();
        count = testProperties.getCount();
    }

    @Test
    public void testContext()
    {
        Assert.assertNotNull( dataProvider );
        Assert.assertNotNull( testProperties );
        Assert.assertNotNull( testProperties.getMacId() );
        Assert.assertNotNull( testProperties.getReservationId() );
    }

    protected void logResult( int size )
    {
        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.debug( size + " settops found" );
        }
    }
}
