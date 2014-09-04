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

import org.junit.Test;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.service.EnvironmentService;
import com.comcast.cats.domain.service.EnvironmentServiceImpl;
import com.comcast.cats.info.ConfigServiceConstants;

/**
 * Integrations test cases for all basic APIs in {@link EnvironmentService}.
 * 
 * @author subinsugunan
 * 
 */
public class EnvironmentServiceImplIT extends BaseDomainIT
{
    @Inject
    private EnvironmentServiceImpl environmentService;

    @Test
    @Override
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( environmentService );
    }

    @Test
    public void findByIdNull() throws DomainNotFoundException
    {
        environmentService.findById( null );
    }

    @Test
    public void findByIdEmpty() throws DomainNotFoundException
    {
        environmentService.findById( EMPTY_STRING );
    }

    @Test
    public void findById() throws DomainNotFoundException
    {
        Environment environment = environmentService.findById( ConfigServiceConstants.CATS_VIRTUAL_ENVIRONMENT );
        Assert.assertNotNull( environment );
        Assert.assertNotNull( environment.getServers() );
        for ( Server server : environment.getServers() )
        {
            Assert.assertNotNull( server.getServices() );

            for ( Service service : server.getServices() )
            {
                Assert.assertNotNull( service.getPath() );
            }
        }
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindServersByEnvironmentId()
    {
        environmentService.findServersByEnvironmentId( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindServicesByEnvironmentId()
    {
        environmentService.findServicesByEnvironmentId( null );
    }
}
