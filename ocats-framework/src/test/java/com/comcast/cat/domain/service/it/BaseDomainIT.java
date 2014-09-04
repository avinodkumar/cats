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
package com.comcast.cat.domain.service.it;

import javax.inject.Inject;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.comcast.cats.domain.service.SettopDomainService;

/**
 * Base test case for all integration tests.
 * 
 * @author subinsugunan
 * 
 */
@ContextConfiguration( locations =
    { "classpath:application-config-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
public class BaseDomainIT
{
    protected final Log           LOGGER                = LogFactory.getLog( getClass() );
    protected static final String EMPTY_STRING          = "";
    protected static final String INVALID_MAC_ID        = "00:00:00:00:00:00";
    protected static final String INVALID_MAC_ID_FORMAT = "1234567890";
    protected static final String MAC_ID                = "54:D4:6F:96:CD:78";

    @Inject
    protected SettopDomainService settopDomainService;

    @Before
    public void setup()
    {
    }

    @Test
    public void testContext()
    {
        Assert.assertNotNull( settopDomainService );
    }

    protected void logResult( int size )
    {
        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.debug( size + " objects found" );
        }
    }
}
