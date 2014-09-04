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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integration Test cases for {@link Reservation} name based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.reservation.name       :   A valid {@link Reservation} name in the configuration management system. 
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.reservation.name       =   CATS Acceptance Reservation
 *    test.offset                 =   0
 *    test.count                  =   1
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/8520
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnReservationNameIT extends BaseSettopDomainServiceIT
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

    @Test
    public void findAllByInvalidReservationName()
    {
        Reservation reservation = new Reservation();
        reservation.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAllByReservationName()
    {
        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation );
        logResult( settops.size() );
    }

    @Test
    public void findAlleByReservationNameOffset()
    {
        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );
        count = 100;
        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation, offset, count );
        logResult( settops.size() );
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

    @Test
    public void findAvailableByInvalidReservationName()
    {
        Reservation reservation = new Reservation();
        reservation.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByReservation( reservation );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableByReservationName()
    {
        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableByReservationNameOffset()
    {
        Reservation reservation = new Reservation();
        reservation.setName( testProperties.getReservationName() );

        count = 100;
        List< SettopDesc > settops = settopDomainService.findAvailableByReservation( reservation, offset, count );
        logResult( settops.size() );
    }
}
