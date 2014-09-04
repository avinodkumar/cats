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

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.SettopGroupInstantiationException;
import com.comcast.cats.domain.exception.SettopGroupNotFoundException;

/**
 * Service interface for {@linkplain SettopGroup}
 * 
 * @author subinsugunan
 * 
 */
public interface SettopGroupService extends DomainService< SettopGroup >
{
    /**
     * Create a new {@link SettopGroup}.
     * 
     * @param settopDesc
     *            - List of {@linkplain SettopDesc} to be added to the
     *            {@linkplain SettopGroup}
     * @param name
     *            - Name of the {@linkplain SettopGroup}
     * @return The {@linkplain SettopGroup}
     * @throws SettopGroupInstantiationException
     */
    SettopGroup create( List< SettopDesc > settopDescList, String name ) throws SettopGroupInstantiationException;

    /**
     * Retrieve a {@link SettopGroup} by s specified settopGroupName.
     * 
     * @param settopGroupName
     *            - Name of the {@linkplain SettopGroup}
     * @return The {@linkplain SettopGroup}
     * @throws SettopGroupNotFoundException
     */
    SettopGroup findByName( String settopGroupName ) throws SettopGroupNotFoundException;

    /**
     * Add a new {@link SettopDesc} to an existing {@link SettopGroup}.
     * 
     * @param settopGroupId
     *            - Id of the parent {@linkplain SettopGroup}
     * @param settopDesc
     *            - {@link SettopDesc} to be added to the
     *            {@linkplain SettopGroup}
     * @return The resultant {@linkplain SettopGroup}
     * @throws SettopGroupNotFoundException
     */
    SettopGroup add( String settopGroupId, SettopDesc settopDesc ) throws SettopGroupNotFoundException;

    /**
     * Add a new {@link SettopDesc} to an existing {@link SettopGroup}.
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @param settopDesc
     *            - {@link SettopDesc} to be added to the
     *            {@linkplain SettopGroup}
     * @return The resultant {@linkplain SettopGroup}
     * @throws SettopGroupNotFoundException
     */
    SettopGroup add( SettopGroup settopGroup, SettopDesc settopDesc ) throws SettopGroupNotFoundException;

    /**
     * Removes a {@link SettopDesc} from an existing {@link SettopGroup}.
     * 
     * @param settopGroupId
     *            - Id of the parent {@linkplain SettopGroup}
     * @param settopDesc
     *            - {@link SettopDesc} to be removed to the
     *            {@linkplain SettopGroup}
     * @return The resultant {@linkplain SettopGroup}
     * @throws SettopGroupNotFoundException
     */
    SettopGroup remove( String settopGroupId, String settopDesc ) throws SettopGroupNotFoundException;

    /**
     * Removes a {@link SettopDesc} from an existing {@link SettopGroup}.
     * 
     * @param settopGroup
     *            - The {@linkplain SettopGroup}
     * @param settopDesc
     *            - {@link SettopDesc} to be removed to the
     *            {@linkplain SettopGroup}
     * @return The resultant {@linkplain SettopGroup}
     * @throws SettopGroupNotFoundException
     */
    SettopGroup remove( SettopGroup settopGroup, String settopDesc ) throws SettopGroupNotFoundException;

    /**
     * Removes all {@link SettopDesc} from an existing {@link SettopGroup}.
     * 
     * @param settopGroupId
     *            - Id of the {@linkplain SettopGroup}
     * @throws SettopGroupNotFoundException
     */
    void removeAll( String settopGroupId ) throws SettopGroupNotFoundException;

    /**
     * Return all {@link SettopGroup}
     * 
     * @return A {@linkplain List} of {@link SettopGroup}
     */
    List< SettopGroup > findAllSettopGroups();

    /**
     * Return all {@link SettopGroup}
     * 
     * @return A {@linkplain List} of {@link SettopGroup}
     */
    List< SettopGroup > findAvailableSettopGroups();
}
