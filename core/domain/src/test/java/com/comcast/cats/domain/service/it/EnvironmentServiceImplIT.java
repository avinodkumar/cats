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
package com.comcast.cats.domain.service.it;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.service.EnvironmentService;
import com.comcast.cats.domain.service.EnvironmentServiceImpl;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integrations test cases for all basic APIs in {@link EnvironmentService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   test.environment.id        :   EnvironmentId of CATS environment in the configuration management system. 
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   test.environment.id        =   ab6a9bad-d318-2363-b8bc-48a720c16e80
 * </pre>
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

    @Test( expected = IllegalArgumentException.class )
    public void findByIdNull() throws DomainNotFoundException
    {
        environmentService.findById( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByIdEmpty() throws DomainNotFoundException
    {
        environmentService.findById( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findById() throws DomainNotFoundException
    {
        Environment environment = environmentService.findById( testProperties.getEnvironmentId() );
        Assert.assertNotNull( environment );
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
