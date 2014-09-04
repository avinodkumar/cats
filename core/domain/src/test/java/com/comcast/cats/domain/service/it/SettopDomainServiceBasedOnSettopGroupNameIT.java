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
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integration Test cases for {@link SettopGroup} name based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.settop.group.name      :   A valid {@link SettopGroup} name in the configuration management system. 
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.settop.group.name      =   CATS test group
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
 *   cats.user.authToken        =   8520
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnSettopGroupNameIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupNull() throws SettopNotFoundException
    {
        settopDomainService.findAllBySettopGroup( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupNameNull() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( null );
        settopDomainService.findAllBySettopGroup( settopGroup );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupNameEmpty() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAllBySettopGroup( settopGroup );
    }

    @Test
    public void findAllByInvalidSettopGroupName() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAllBySettopGroupName() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup );
        logResult( settops.size() );
    }

    @Test
    public void findAlleBySettopGroupNameOffset() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );
        count = 100;
        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup, offset, count );
        logResult( settops.size() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupNull()
    {
        settopDomainService.findAvailableBySettopGroup( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupNameNull()
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( null );
        settopDomainService.findAvailableBySettopGroup( settopGroup );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupNameEmpty()
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAvailableBySettopGroup( settopGroup );
    }

    @Test
    public void findAvailableByInvalidSettopGroupName() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableBySettopGroupName()
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableBySettopGroupNameOffset()
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );
        count = 100;
        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup, offset, count );
        logResult( settops.size() );
    }
}
