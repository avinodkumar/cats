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

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integration Test cases for {@link SettopGroup} id based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.settop.group.id        :   A valid {@link SettopGroup} id in the configuration management system. 
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.settop.group.id        =   d44adabd-5f1d-4268-b2e7-1043e9ed03d6
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
public class SettopDomainServiceBasedOnSettopGroupIdIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupIdNull()
    {
        settopDomainService.findAllBySettopGroupId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupIdEmpty()
    {
        settopDomainService.findAllBySettopGroupId( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findAllByInvalidSettopGroupId()
    {
        List< SettopDesc > settops = settopDomainService.findAllBySettopGroupId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAllBySettopGroupId()
    {
        List< SettopDesc > settops = settopDomainService.findAllBySettopGroupId( testProperties.getSettopGroupId() );
        logResult( settops.size() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupIdNull()
    {
        settopDomainService.findAvailableBySettopGroupId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupIdEmpty()
    {
        settopDomainService.findAvailableBySettopGroupId( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findAvailableByInvalidSettopGroupId()
    {
        List< SettopDesc > settops = settopDomainService
                .findAvailableBySettopGroupId( DataProvider.INVALID_ID );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableBySettopGroupId()
    {
        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroupId( testProperties
                .getSettopGroupId() );
        logResult( settops.size() );
    }
}
