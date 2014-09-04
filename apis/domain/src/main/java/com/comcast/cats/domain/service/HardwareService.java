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

import com.comcast.cats.domain.HardwareDevice;
import com.comcast.cats.domain.HardwareType;
import com.comcast.cats.domain.Location;
import com.comcast.cats.domain.Rack;

/**
 * Service interface for {@linkplain HardwareDevice}
 * 
 * @author subinsugunan
 * 
 */
@Deprecated
public interface HardwareService extends DomainService< HardwareDevice >
{

    /**
     * Finds a set of {@link HardwareDevice} of a specified {@link HardwareType}
     * 
     * @param hardwareType
     *            - {@linkplain Enum} - {@linkplain HardwareType} ,which
     *            specifies the type of {@link HardwareDevice}
     * @return A {@linkplain List} of {@linkplain HardwareDevice}
     */
    List< HardwareDevice > findByType( HardwareType hardwareType );

    /**
     * Finds a set of {@link HardwareDevice} of a specified {@link HardwareType}
     * 
     * @param hardwareType
     *            - {@linkplain Enum} - {@linkplain HardwareType} ,which
     *            specifies the type of {@link HardwareDevice}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain HardwareDevice}
     */
    List< HardwareDevice > findByType( HardwareType hardwareType, Integer offset, Integer count );

    /**
     * Finds a set of {@link HardwareDevice} of a specified {@link HardwareType}
     * in a given {@linkplain Location}
     * 
     * @param hardwareType
     *            - {@linkplain Enum} - {@linkplain HardwareType} ,which
     *            specifies the type of {@link HardwareDevice}
     * @param location
     *            - The {@linkplain Location}
     * @return A {@linkplain List} of {@linkplain HardwareDevice}
     */
    List< HardwareDevice > findByType( HardwareType hardwareType, Location location );

    /**
     * Finds a set of {@link HardwareDevice} of a specified {@link HardwareType}
     * in a given {@linkplain Location}
     * 
     * @param hardwareType
     *            - {@linkplain Enum} - {@linkplain HardwareType} ,which
     *            specifies the type of {@link HardwareDevice}
     * @param location
     *            - The {@linkplain Location}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain HardwareDevice}
     */
    List< HardwareDevice > findByType( HardwareType hardwareType, Location location, Integer offset, Integer count );

    /**
     * Finds a set of {@link HardwareDevice} of a specified {@link HardwareType}
     * in a given {@linkplain Rack}
     * 
     * @param hardwareType
     *            - {@linkplain Enum} - {@linkplain HardwareType} ,which
     *            specifies the type of {@link HardwareDevice}
     * @param rack
     *            - The {@linkplain Rack}
     * @return A {@linkplain List} of {@linkplain HardwareDevice}
     */
    List< HardwareDevice > findByType( HardwareType hardwareType, Rack rack );

    /**
     * Finds a set of {@link HardwareDevice} of a specified {@link HardwareType}
     * in a given {@linkplain Rack}
     * 
     * @param hardwareType
     *            - {@linkplain Enum} - {@linkplain HardwareType} ,which
     *            specifies the type of {@link HardwareDevice}
     * @param rack
     *            - The {@linkplain Rack}
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain HardwareDevice}
     */
    List< HardwareDevice > findByType( HardwareType hardwareType, Rack rack, Integer offset, Integer count );
}
