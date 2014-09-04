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

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for all basic APIs in {@link EnvironmentService}.
 * 
 * @author subinsugunan
 * 
 */
public class EnvironmentServiceImplTest extends BaseTestCase
{

    @Inject
    private EnvironmentServiceImpl environmentService;

    @Before
    public void beforeMethod()
    {
        super.beforeMethod();
        environmentService.setRestTemplate( restTemplateMock );
    }

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

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findById() throws DomainNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getEnvironment() );
        replayAll();
        Environment environment = environmentService.findById( testProperties.getEnvironmentId() );
        Assert.assertNotNull( environment );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = DomainNotFoundException.class )
    public void findByIdException() throws DomainNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        environmentService.findById( testProperties.getMacId() );
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
