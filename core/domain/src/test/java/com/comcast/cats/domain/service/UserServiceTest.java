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

import javax.inject.Inject;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.UserNotFoundException;

/**
 * Test cases for all basic APIs in {@link UserService}.
 * 
 * @author subinsugunan
 * 
 */
public class UserServiceTest extends BaseTestCase
{

    @Inject
    private UserServiceImpl userService;

    @Before
    public void beforeMethod()
    {
        super.beforeMethod();
        userService.setRestTemplate( restTemplateMock );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findUser() throws UserNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getUser() );
        replayAll();
        User user = userService.findUser();
        Assert.assertNotNull( user );
        Assert.assertNotNull( user.getFirstName() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = UserNotFoundException.class )
    public void findUserNotFoundException() throws UserNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        userService.findUser();
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = UserNotFoundException.class )
    public void findUserInvalidRequestException() throws UserNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.BAD_REQUEST ) );
        replayAll();
        userService.findUser();
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = UserNotFoundException.class )
    public void findUserInvalidException() throws UserNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.BAD_GATEWAY ) );
        replayAll();
        userService.findUser();
    }
}
