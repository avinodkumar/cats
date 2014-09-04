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

import java.nio.channels.IllegalSelectorException;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for {@link Reservation} id based search APIs in
 * {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnReservationIdTest extends BaseSettopDomainServiceTest
{
    public void findAllByReservationIdNull()
    {
        settopDomainService.findAllByReservationId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByReservationIdEmpty()
    {
        settopDomainService.findAllByReservationId( "" );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAllByInvalidReservationId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( DataProvider.INVALID_ID );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByReservationId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId() );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByReservationIdShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId(),true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByReservationIdShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId(),false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByReservationIdOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId(),
                offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByReservationIdOffsetShalowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId(),
                offset, count,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByReservationIdOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId(),
                offset, count,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationIdNull()
    {
        settopDomainService.findAvailableByReservationId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationIdEmpty()
    {
        settopDomainService.findAvailableByReservationId( "" );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAvailableByInvalidReservationId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByReservationId( DataProvider.INVALID_ID );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByReservationId( testProperties
                .getReservationId() );
        logResult( settops.size() );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationIdShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByReservationId( testProperties
                .getReservationId(),true );
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationIdShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByReservationId( testProperties
                .getReservationId(),false );
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationIdOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByReservationId(
                testProperties.getReservationId(), offset, count );
        logResult( settops.size() );
    }
    
    @SuppressWarnings(
            { "rawtypes", "unchecked" } )
        @Test
        public void findAvailableByReservationIdOffsetShallowTrue()
        {
            EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                    .andReturn( dataProvider.getSettopDescList(true) );
            replayAll();

            List< SettopDesc > settops = settopDomainService.findAvailableByReservationId(
                    testProperties.getReservationId(), offset, count,true );
            logResult( settops.size() );
            Assert.assertNull("shallow object contains hardware info",
                    settops.get(0).getHardwareInterfaceByType(HardwarePurpose.POWER));
        }
        
    @SuppressWarnings(
            { "rawtypes", "unchecked" } )
        @Test
        public void findAvailableByReservationIdOffsetShallowFalse()
        {
            EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                    .andReturn( dataProvider.getSettopDescList(false) );
            replayAll();

            List< SettopDesc > settops = settopDomainService.findAvailableByReservationId(
                    testProperties.getReservationId(), offset, count,false );
            logResult( settops.size() );
            Assert.assertNotNull(" object does not  contains hardware info",
                    settops.get(0).getHardwareInterfaceByType(HardwarePurpose.POWER));
        }
        
    
    
}
