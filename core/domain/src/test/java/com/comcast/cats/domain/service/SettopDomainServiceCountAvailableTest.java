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

import org.easymock.EasyMock;
import org.junit.Test;

import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for count-available APIs in {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceCountAvailableTest extends BaseSettopDomainServiceTest
{
    @Test( expected = IllegalArgumentException.class )
    public void countAvailableByRackIdNull()
    {
        settopDomainService.countAvailableByRackId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAvailableByRackIdEmpty()
    {
        settopDomainService.countAvailableByRackId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countAvailableByRackId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = settopDomainService.countAvailableByRackId( testProperties.getRackId() );
        logResult( count );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAvailableByReservationIdNull()
    {
        settopDomainService.countAvailableByReservationId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAvailableByReservationIdEmpty()
    {
        settopDomainService.countAvailableByReservationId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countAvailableByReservationId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = settopDomainService.countAvailableByReservationId( testProperties.getReservationId() );
        logResult( count );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAvailableBySettopGroupIdNull()
    {
        settopDomainService.countAvailableBySettopGroupId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void countAvailableBySettopGroupIdEmpty()
    {
        settopDomainService.countAvailableBySettopGroupId( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void countAvailableBySettopGroupId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( DataProvider.DUMMY_COUNT );
        replayAll();

        int count = settopDomainService.countAvailableBySettopGroupId( testProperties.getSettopGroupId() );
        logResult( count );
    }
}
