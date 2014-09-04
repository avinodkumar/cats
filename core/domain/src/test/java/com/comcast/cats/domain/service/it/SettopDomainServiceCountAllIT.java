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

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Integrations test cases for count-all APIs in {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.rack.id                :   A valid {@link Rack} id in the configuration management system. 
 *    test.reservation.id         :   A valid {@link Reservation} id in the configuration management system. 
 *    test.settop.group.id        :   A valid {@link SettopGroup} id in the configuration management system. 
 *        
 *    E.g.
 *    test.rack.id                =   f89c730c-200b-4889-bef5-bcee8dca1d8f
 *    test.reservation.id         =   b55912a1-f693-407f-9e46-a00bbe6c3c57
 *    test.settop.group.id        =   d44adabd-5f1d-4268-b2e7-1043e9ed03d6
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   78
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceCountAllIT extends BaseSettopDomainServiceIT
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

    @Test
    public void countAllByRackIdInvalid()
    {
        int count = settopDomainService.countAllByRackId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, count );
    }

    @Test
    public void countAllByRackId()
    {
        int count = settopDomainService.countAllByRackId( testProperties.getRackId() );
        logResult( count );
        Assert.assertTrue( AssertUtil.isOneOrMore(count) );
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

    @Test
    public void countAllBySettopGroupIdInvalid()
    {
        int count = settopDomainService.countAllBySettopGroupId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, count );
    }

    @Test
    public void countAllBySettopGroupId()
    {
        int count = settopDomainService.countAllBySettopGroupId( testProperties.getSettopGroupId() );
        Assert.assertTrue( AssertUtil.isOneOrMore(count) );
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

    @Test
    public void countAllByReservationIdInvalid()
    {
        int count = settopDomainService.countAllByReservationId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, count );
    }

    @Test
    public void countAllByReservationId()
    {
        int count = settopDomainService.countAllByReservationId( testProperties.getReservationId() );
        Assert.assertTrue( AssertUtil.isOneOrMore(count) );
        logResult( count );
    }
}
