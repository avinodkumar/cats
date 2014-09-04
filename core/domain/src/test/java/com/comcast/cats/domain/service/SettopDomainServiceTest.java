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

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for basic APIs in {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceTest extends BaseSettopDomainServiceTest
{
    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdNull()
    {
        try
        {
            settopDomainService.findByMacId( null );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdEmpty()
    {
        try
        {
            settopDomainService.findByMacId( DataProvider.EMPTY_STRING );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByInvalidMacIdSyntax()
    {
        try
        {
            settopDomainService.findByMacId( DataProvider.INVALID_MAC_ID_FORMAT );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findByMacId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDesc() );
        replayAll();

        try
        {
            SettopDesc settopDesc = settopDomainService.findByMacId( testProperties.getMacId() );
            Assert.assertNotNull( settopDesc );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }
    
  
    

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test( expected = SettopNotFoundException.class )
    public void findByMacIdException() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();

        settopDomainService.findByMacId( testProperties.getMacId() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllAllocated()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAllocated();
        Assert.assertNotNull( settopDescList );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllAllocatedShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopResDescList( true ) );
        replayAll();
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAllocated( true );
        Assert.assertNotNull( settopDescList );
        Assert.assertNull( "shallow object contains hardware info",
                settopDescList.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllAllocatedShallowFlase()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopResDescList( false ) );
        replayAll();
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAllocated( false );
        Assert.assertNotNull( settopDescList );
        Assert.assertNotNull( " object does not  contains hardware info",
                settopDescList.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllAvailable()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAvailable();
        Assert.assertNotNull( settopDescList );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllAvailableShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopResDescList( true ) );
        replayAll();
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAvailable( true );
        Assert.assertNotNull( settopDescList );
        Assert.assertNull( "shallow object contains hardware info",
                settopDescList.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllAvailableShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopResDescList( false ) );
        replayAll();
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAvailable( false );
        Assert.assertNotNull( settopDescList );
        Assert.assertNotNull( " object does not  contains hardware info",
                settopDescList.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }
}
