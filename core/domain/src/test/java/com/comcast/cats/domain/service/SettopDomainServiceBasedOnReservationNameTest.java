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
 * Test cases for {@link Reservation} name based search APIs in
 * {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnReservationNameTest extends BaseSettopDomainServiceTest
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllByReservationNull()
    {
        settopDomainService.findAllByReservation( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByReservationNameNull()
    {
        Reservation reservation = new Reservation();
        reservation.setName( null );
        settopDomainService.findAllByReservation( reservation );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByReservationNameEmpty()
    {
        Reservation reservation = new Reservation();
        reservation.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAllByReservation( reservation );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAllByInvalidReservationName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByReservationName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByReservationNameShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByReservationNameShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation,false );
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByReservationNameOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation, offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByReservationNameOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation, offset, count,true);
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByReservationNameOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation, offset, count,false);
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationNull()
    {
        settopDomainService.findAvailableByReservation( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationNameNull()
    {
        Reservation reservation = new Reservation();
        reservation.setName( null );
        settopDomainService.findAvailableByReservation( reservation );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationNameEmpty()
    {
        Reservation reservation = new Reservation();
        reservation.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAvailableByReservation( reservation );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAvailableByInvalidReservationName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationNameShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationNameShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation ,false);
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationNameOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation, offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationNameOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation, offset, count,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByReservationNameOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation, offset, count,false );
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

}
