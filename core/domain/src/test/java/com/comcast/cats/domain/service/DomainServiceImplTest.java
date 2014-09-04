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

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.DomainInstantiationException;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.DomainUpdateException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Test cases for basic APIs in {@link DomainServiceImpl}.
 * 
 * @author subinsugunan
 * 
 */
public class DomainServiceImplTest extends BaseTestCase
{
    @Inject
    @Qualifier( "domainServiceImpl" )
    private DomainServiceImpl< SettopDesc > domainService;

    private RestTemplate                    restTemplateMock;

    @Test( expected = UnsupportedOperationException.class )
    public void testCreate() throws DomainInstantiationException
    {
        domainService.create( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindById() throws DomainNotFoundException
    {
        domainService.findById( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFind()
    {
        domainService.find();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindOffset()
    {
        domainService.find( 0, 0 );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindByNamedQuery()
    {
        domainService.findByNamedQuery( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindByNamedQueryOffset()
    {
        domainService.findByNamedQuery( null, 0, 0 );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindByNamedQueryvarArgs()
    {
        domainService.findByNamedQuery( null, null, null );
    }

    @Test( expected = UnsupportedOperationException.class )
    @Ignore
    public void testFindByNamedQueryOffsetVarArgs()
    {
        // domainService.findByNamedQuery( null, 0, 0, null, null, null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindByNamedQueryAndNamedParams()
    {
        domainService.findByNamedQueryAndNamedParams( null, null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindByNamedQueryAndNamedParamsOffset()
    {
        domainService.findByNamedQueryAndNamedParams( null, null, 0, 0 );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testUpdate() throws DomainUpdateException
    {
        domainService.update( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testDelete() throws DomainNotFoundException
    {
        domainService.delete( "" );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testDeleteByDomain() throws DomainNotFoundException
    {
        domainService.delete( new SettopDesc() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testCount()
    {
        domainService.count();
    }

    @Test
    public void testGetDomainClass()
    {
        @SuppressWarnings( "rawtypes" )
        Class clazz = domainService.getDomainClass();
        Assert.assertNotNull( clazz );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testGetResponseObject() throws SettopNotFoundException
    {
        restTemplateMock = createMock( RestTemplate.class );
        domainService.setRestTemplate( restTemplateMock );

        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDesc() );
        replayAll();

        Object settop = null;
        try
        {
            settop = domainService.getResponseAsDomain( DataProvider.SERVER_URL, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            Assert.fail( e.getMessage() );
        }
        Assert.assertNotNull( settop );
    }

    @Test( expected = RuntimeException.class )
    public void testGetResponseObjectException() throws SettopNotFoundException
    {
        domainService = new DomainServiceImpl< SettopDesc >();
        try
        {
            domainService.getResponseAsDomain( DataProvider.INVALID_URL, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void testGetResponseCount()
    {
        restTemplateMock = createMock( RestTemplate.class );
        domainService.setRestTemplate( restTemplateMock );

        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = 0;
        try
        {
            count = domainService.getResponseAsNumber( DataProvider.SERVER_URL );
        }
        catch ( DomainServiceException e )
        {
            Assert.fail( e.getMessage() );
        }
        Assert.assertTrue( AssertUtil.isOneOrMore( count ) );
    }

    @Test( expected = RuntimeException.class )
    public void testGetResponseCountException() throws SettopNotFoundException
    {
        domainService = new DomainServiceImpl< SettopDesc >();
        try
        {
            domainService.getResponseAsNumber( DataProvider.INVALID_URL );
        }
        catch ( DomainServiceException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void testGetResponseList()
    {
        restTemplateMock = createMock( RestTemplate.class );
        domainService.setRestTemplate( restTemplateMock );

        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = null;
        try
        {
            settops = domainService.getResponseAsDomainList( DataProvider.SERVER_URL, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            Assert.fail( e.getMessage() );
        }
        Assert.assertNotNull( settops );
    }

    @Test( expected = RuntimeException.class )
    public void testGetResponseListException() throws SettopNotFoundException
    {
        domainService = new DomainServiceImpl< SettopDesc >();
        try
        {
            domainService.getResponseAsDomainList( DataProvider.INVALID_URL, SettopDesc.class );
        }
        catch ( DomainServiceException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void testLogRequestEmpty()
    {
        domainService.validateAndLogRequest( HttpMethod.GET, DataProvider.EMPTY_STRING );
    }

    @Test
    public void testLogRequest()
    {
        domainService.validateAndLogRequest( HttpMethod.GET, DataProvider.STB_PROP_MANUFACTURER );
    }

    @Test
    public void testGetBaseUrl()
    {
        Assert.assertNotNull( domainService.getBaseUrl( DataProvider.EMPTY_STRING ) );
    }

}
