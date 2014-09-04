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

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.UserNotFoundException;
import com.comcast.cats.domain.service.UserService;

/**
 * Integrations test cases for all basic APIs in {@link UserService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have cats.props file under $CATS_HOME directory with at least the following entries.
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
public class UserServiceIT extends BaseDomainIT
{

    @Inject
    private UserService userService;

    @Test
    @Override
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( userService );
    }

    @Test
    public void findUser() throws UserNotFoundException
    {
        User user = userService.findUser();
        Assert.assertNotNull( user );
        Assert.assertNotNull( user.getFirstName() );
        LOGGER.info( user.toString());
    }
}
