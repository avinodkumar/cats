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

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integration Test cases for {@link Rack} name based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.rack.name              :   A valid {@link Rack} name in the configuration management system. 
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.rack.name              =   
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
 *   cats.user.authToken        =   6551
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnRackNameIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackNull()
    {
        settopDomainService.findAllByRack( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackNameNull()
    {
        Rack rack = new Rack();
        rack.setName( null );
        settopDomainService.findAllByRack( rack );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackNameEmpty()
    {
        Rack rack = new Rack();
        rack.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAllByRack( rack );
    }

    @Test
    public void findAllByInvalidRackName()
    {
        Rack rack = new Rack();
        rack.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAllByRackName()
    {
        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack );
        logResult( settops.size() );
    }

    @Test
    public void findAlleByRackNameOffset()
    {
        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );
        count = 100;
        List< SettopDesc > settops = settopDomainService.findAllByRack( rack, offset, count );
        logResult( settops.size() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackNull()
    {
        settopDomainService.findAvailableByRack( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackNameNull()
    {
        Rack rack = new Rack();
        rack.setName( null );
        settopDomainService.findAvailableByRack( rack );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackNameEmpty()
    {
        Rack rack = new Rack();
        rack.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAvailableByRack( rack );
    }

    @Test
    public void findAvailableByInvalidRackName()
    {
        Rack rack = new Rack();
        rack.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack );
        Assert.assertEquals( 0, settops.size() );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableByRackName()
    {
        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableByRackNameOffset()
    {
        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );
        count = 100;
        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack, offset, count );
        logResult( settops.size() );
    }
}
