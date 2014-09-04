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

import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopGroupService;

/**
 * Integrations test cases for all basic APIs in {@link SettopGroupService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.  
 * 2. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   7410
 * 
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopGroupServiceIT extends BaseDomainIT
{
    @Inject
    protected SettopGroupService settopGroupService;

    @Override
    @Test
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( settopGroupService );
    }

    @Test
    public void testCount()
    {
        int count = settopGroupService.count();
        logResult( count );
    }

    @Test
    public void findAvailableSettopGroups()
    {
        List< SettopGroup > settopGroups = settopGroupService.findAvailableSettopGroups();

        logResult( settopGroups.size() );

        for ( SettopGroup settopGroup : settopGroups )
        {
            LOGGER.info( settopGroup.toString() );
            LOGGER.info( "----------" );
        }
    }
}
