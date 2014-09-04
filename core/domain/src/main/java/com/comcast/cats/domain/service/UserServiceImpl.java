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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.UserNotFoundException;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Implementation of {@link UserService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class UserServiceImpl extends DomainServiceImpl< User > implements UserService
{
    /**
     * Get information of currently logged in user.
     * 
     * @return The {@linkplain User}
     * @throws UserNotFoundException
     */
    @Override
    public User findUser() throws UserNotFoundException
    {
        String requestUrl = getBaseUrl( USER ) + CommonUtil.getNameValuePair( getParamMapByToken() );

        User user = null;
        try
        {
            user = getResponseAsDomain( requestUrl, User.class );
        }
        catch ( DomainServiceException e )
        {
            throw new UserNotFoundException( e.getMessage() );
        }

        return user;
    }

    @Override
    public Integer countActiveUsersByUserGroup( String userGroupId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< User > findActiveUsersByUserGroup( String userGroupId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< User > findActiveUsersCreatedAfterDateByUserGroup( String userGroupId, Date createdAfter,
            Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Integer count()
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + USERS ) + COUNT;

        int count = 0;

        try
        {
            count = getResponseAsNumber( requestUrl );
        }
        catch ( Exception e )
        {
            logger.error( "Failed to retrieve user count. " + e.getMessage() );
        }

        return count;
    }

    @Override
    public List< User > find( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + USERS ) + offset + BACK_SLASH + count;

        List< User > users = new ArrayList< User >();

        try
        {
            users = getResponseAsDomainList( requestUrl, User.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve user list. " + e.getMessage() );
        }

        return users;
    }
}
