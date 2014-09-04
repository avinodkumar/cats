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

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for count-all APIs in {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceCountAllTest extends BaseSettopDomainServiceTest
{
    @Test( expected = IllegalArgumentException.class )
    public void countAllByRackIdNull()
    {
        settopDomainService.countAllByRackId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAllByRackIdEmpty()
    {
        settopDomainService.countAllByRackId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countAllByRackId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = settopDomainService.countAllByRackId( testProperties.getRackId() );
        logResult( count );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAllByReservationIdNull()
    {
        settopDomainService.countAllByReservationId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAllByReservationIdEmpty()
    {
        settopDomainService.countAllByReservationId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countAllByReservationId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = settopDomainService.countAllByReservationId( testProperties.getReservationId() );
        logResult( count );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAllBySettopGroupIdNull()
    {
        settopDomainService.countAllBySettopGroupId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAllBySettopGroupIdEmpty()
    {
        settopDomainService.countAllBySettopGroupId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countAllBySettopGroupId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = settopDomainService.countAllBySettopGroupId( testProperties.getSettopGroupId() );
        logResult( count );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countException()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();

        int count = settopDomainService.countAllBySettopGroupId( testProperties.getSettopGroupId() );
        Assert.assertEquals( 0, count );
    }
}
