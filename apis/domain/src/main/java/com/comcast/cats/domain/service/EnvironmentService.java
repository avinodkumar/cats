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

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.exception.DomainNotFoundException;

/**
 * Service interface for {@linkplain Environment}
 * 
 * @author subinsugunan
 * 
 */
public interface EnvironmentService extends DomainService< Environment >
{
    /**
     * Returns all {@linkplain Server} in a given {@linkplain Environment}.
     * 
     * @param environmentId
     * @return List of {@linkplain Server}
     */
    List< Server > findServersByEnvironmentId( String environmentId ) throws DomainNotFoundException;

    /**
     * Returns all {@linkplain Service} in a given {@linkplain Environment}.
     * 
     * @param environmentId
     * @return List of {@linkplain Service}
     */
    List< Service > findServicesByEnvironmentId( String environmentId );
}
