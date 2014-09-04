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

import java.util.List;

import com.comcast.cats.domain.UserGroup;

/**
 * Service interface for {@linkplain UserGroup}
 * 
 * @author subinsugunan
 * 
 */
public interface UserGroupService extends DomainService< UserGroup >
{
    /**
     * Find a {@linkplain UserGroup} by name.
     * 
     * @param userGroupName
     *            - Name of the {@linkplain UserGroup}
     * @return The {@linkplain UserGroup}
     */
    UserGroup findByName( String userGroupName );

    Integer countAll();

    List< UserGroup > find( Integer offset, Integer count );
}
