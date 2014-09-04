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
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.exception.ServerNotFoundException;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for all basic APIs in {@link ServerService}.
 * 
 * @author subinsugunan
 * 
 */
public class ServerServiceTest extends BaseTestCase
{

    @Inject
    private ServerServiceImpl serverService;

    @Before
    public void beforeMethod()
    {
        super.beforeMethod();
        serverService.setRestTemplate( restTemplateMock );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdNull() throws ServerNotFoundException
    {
        serverService.findByMacId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdEmpty() throws ServerNotFoundException
    {
        serverService.findByMacId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findByMacId() throws ServerNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getServer() );
        replayAll();
        Server server = serverService.findByMacId( testProperties.getMacId() );
        Assert.assertNotNull( server );
        Assert.assertNotNull( server.getHost() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = ServerNotFoundException.class )
    public void findByMacIdException() throws ServerNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        serverService.findByMacId( testProperties.getMacId() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findServicesByServerIdNull()
    {
        serverService.findServices( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findServicesByServerIdEmpty()
    {
        serverService.findServices( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findServicesByServerId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getServerList() );
        replayAll();
        List< Service > services = serverService.findServices( testProperties.getServerId() );
        Assert.assertNotNull( services );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findServicesByServerIdException()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        List< Service > services = serverService.findServices( testProperties.getServerId() );
        Assert.assertNotNull( services );
        // FIXME: uncomment this
        // Assert.assertEquals( 0, services.size() );
    }

}
