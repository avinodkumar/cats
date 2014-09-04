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
import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.UserGroup;
import com.comcast.cats.domain.exception.DomainServiceException;

/**
 * 
 * @author SSugun00c
 * 
 */
@Named
public class UserGroupServiceImpl extends DomainServiceImpl< UserGroup > implements UserGroupService
{

    @Override
    public UserGroup findByName( String userGroupName )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Integer countAll()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }
    
    @Override
    public Integer count()
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + USER_GROUPS ) + COUNT;

        int count = 0;

        try
        {
            count = getResponseAsNumber( requestUrl );
        }
        catch ( Exception e )
        {
            logger.error( "Failed to retrieve userGroup count. " + e.getMessage() );
        }

        return count;
    }
    
    @Override
    public List< UserGroup > find( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + USER_GROUPS ) + offset + BACK_SLASH + count;

        List< UserGroup > userGroups = new ArrayList< UserGroup >();
        try
        {
            userGroups = getResponseAsDomainList( requestUrl, UserGroup.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve UserGroup  list. " + e.getMessage() );
        }

        return userGroups;
    }
}
