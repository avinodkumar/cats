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
 * Integration Test cases for {@link Reservation} id based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.reservation.id         :   A valid {@link Reservation} id in the configuration management system. 
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.reservation.id         =   b55912a1-f693-407f-9e46-a00bbe6c3c57
 *    test.offset                 =   0
 *    test.count                  =   1
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   852
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnReservationIdIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllByReservationIdNull()
    {
        settopDomainService.findAllByReservationId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByReservationIdEmpty()
    {
        settopDomainService.findAllByReservationId( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findAllByInvalidReservationId()
    {
        List< SettopDesc > settops = settopDomainService.findAllByReservationId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAllByReservationId()
    {
        List< SettopDesc > settops = settopDomainService.findAllByReservationId( testProperties.getReservationId() );
        logResult( settops.size() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationIdNull()
    {
        settopDomainService.findAvailableByReservationId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByReservationIdEmpty()
    {
        settopDomainService.findAvailableByReservationId( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findAvailableByInvalidReservationId()
    {
        List< SettopDesc > settops = settopDomainService.findAvailableByReservationId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableByReservationId()
    {
        List< SettopDesc > settops = settopDomainService.findAvailableByReservationId( testProperties
                .getReservationId() );
        logResult( settops.size() );
    }
}
