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

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.exception.RackNotFoundException;

/**
 * Service interface for {@linkplain Rack}
 * 
 * @author subinsugunan
 * 
 */
public interface RackService extends DomainService< Rack >
{
    int MAX_PAGE_SIZE = 10;

    /**
     * Returns a {@linkplain Rack} based on its name.
     * 
     * @param rackName
     *            - Name of the {@linkplain Rack}
     * @return The {@linkplain Rack}
     * @throws RackNotFoundException
     */
    Rack findByName( String rackName ) throws RackNotFoundException;

    /**
     * Returns a {@linkplain Rack} of a {@linkplain HardwareDevice}
     * 
     * @param hardwareDeviceId
     *            - Id of the Device
     * @return The {@linkplain Rack}
     * @throws RackNotFoundException
     */
    Rack findByHardwareDeviceId( String hardwareDeviceId ) throws RackNotFoundException;

}
