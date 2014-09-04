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
package com.comcast.cats.service;

import java.util.List;

import com.comcast.cats.service.ir.redrat.RedRatDevice;

public interface RedRatManager
{

    /**
     * Get a list of all {@link IRDevice} managed by this IRManager.
     * 
     * @return list of all {@link IRDevice}
     */
    public List< RedRatDevice > getIrDevices();

    /**
     * Get {@link IRDevice} with the provided ip.
     * 
     * @param ip
     * 
     * @return {@link IRDevice}
     */
    public RedRatDevice getIrDevice( String ip );

}
