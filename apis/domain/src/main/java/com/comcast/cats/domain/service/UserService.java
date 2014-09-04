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

import java.util.Date;
import java.util.List;

import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.UserNotFoundException;

/**
 * Service interface for {@linkplain User}
 * 
 * @author subinsugunan
 * 
 */
public interface UserService extends DomainService< User >
{

    /**
     * Get information of currently logged in user.
     * 
     * @return The {@linkplain User}
     * @throws UserNotFoundException
     */
    User findUser() throws UserNotFoundException;

    Integer countActiveUsersByUserGroup( String userGroupId );

    List< User > findActiveUsersByUserGroup( String userGroupId, Integer offset, Integer count );

    List< User > findActiveUsersCreatedAfterDateByUserGroup( String userGroupId, Date createdAfter, Integer offset,
            Integer count );
}
