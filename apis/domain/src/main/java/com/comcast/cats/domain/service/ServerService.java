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

import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.ServerNotFoundException;

/**
 * Service interface for {@linkplain Server} and {@linkplain Service}
 * 
 * @author subinsugunan
 * 
 */
public interface ServerService extends DomainService< Server >
{

    /**
     * Finds a CATS {@linkplain Server} based on macId of
     * {@linkplain SettopDesc}
     * 
     * @param macId
     *            - MAC address of the settop box
     * @return The {@linkplain Server}
     * @throws ServerNotFoundException
     */
    Server findByMacId( String macId ) throws ServerNotFoundException;

    /**
     * Return all {@linkplain Service} from a given {@linkplain Server}
     * 
     * @param serverId
     *            - Id of the {@linkplain Server}
     * @return A {@linkplain List} of {@linkplain Service} of the specified
     *         {@linkplain Server}
     */
    List< Service > findServices( String serverId ) throws DomainNotFoundException;
}
