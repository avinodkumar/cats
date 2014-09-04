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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;

/**
 * This class provides the service interface to allow searching for devices that
 * have been previously configured.
 * 
 * @author ssugun00c
 * 
 */
@WebService( name = ConfigServiceConstants.DEVICE_SEARCH_SERVICE_NAME, targetNamespace = ConfigServiceConstants.NAMESPACE )
public interface DeviceSearchService
{
    /**
     * Return {@link Settop} for a given macId.
     * 
     * @param macId
     *            - The MAC address of the settop box
     * @return The {@link SettopDesc}
     * @throws SettopNotFoundException
     */
    @WebMethod
    public SettopDesc findByMacId( @WebParam( name = "macId" )
    String macId ) throws SettopNotFoundException;

    /**
     * Return all allocated {@link SettopDesc} for a given user.
     * 
     * @return
     */
    @WebMethod
    public List< SettopDesc > findAllAllocated();

    /**
     * Return all available {@link SettopDesc} for a given user.
     * 
     * @return
     */
    @WebMethod
    public List< SettopDesc > findAllAvailable();

    /**
     * Return all allocated {@link SettopReservationDesc} for a given user.
     * 
     * @return
     */
    @WebMethod
    public List< SettopReservationDesc > findAllAllocatedSettopReservationDesc();

    /**
     * Return all available {@link SettopReservationDesc} for a given user.
     * 
     * @return
     */
    @WebMethod
    public List< SettopReservationDesc > findAllAvailableSettopReservationDesc();

}
