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

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.ServerNotFoundException;
import com.comcast.cats.domain.service.ServerService;
import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Integrations test cases for all basic APIs in {@link ServerService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   test.mac.id                :   MacId of an available {@link SettopDesc} in the configuration management system. 
 *   test.server.id             :   A valid CATS Application {@link Server} id in the configuration management system. 
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   test.mac.id                =   12:BE:20:AF:7F:D0
 *   test.server.id             =   f89c730c-200b-165464889-bef5-bcee8dca1d8f
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class ServerServiceIT extends BaseDomainIT
{

    @Inject
    private ServerService serverService;

    @Test
    @Override
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( serverService );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdNull() throws ServerNotFoundException
    {
        serverService.findByMacId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdEmpty() throws ServerNotFoundException
    {
        serverService.findByMacId( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findByMacId() throws DomainNotFoundException
    {
        Server server = serverService.findByMacId( testProperties.getMacId() );
        Assert.assertNotNull( server );
        Assert.assertNotNull( server.getId() );
        Assert.assertNotNull( server.getHost() );
        List< Service > services = serverService.findServices( server.getId() );
        Assert.assertNotNull( services );
        Assert.assertTrue( AssertUtil.isOneOrMore( services.size() ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findServicesByServerIdNull() throws DomainNotFoundException
    {
        serverService.findServices( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findServicesByServerIdEmpty() throws DomainNotFoundException
    {
        serverService.findServices( DataProvider.EMPTY_STRING );
    }
}
