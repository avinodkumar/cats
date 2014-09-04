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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.info.ConfigServiceConstants;

/**
 * A service interface to get access to Settops. Services include but not
 * limited to lock, unlock and verify access to Settops.
 * 
 * @author cfrede001
 */
@WebService( name = ConfigServiceConstants.ALLOCATION_SERVICE_NAME, targetNamespace = ConfigServiceConstants.NAMESPACE )
public interface SettopAllocationService
{
    /**
     * Try to allocate a device for use with a specified componentId.
     * 
     * Conditions: If the reservationId is specified and the device referenced
     * is not currently reserved then the allocation should fail.
     * 
     * If the device is currently allocated the allocation should fail.
     * 
     * 
     * @param componentUUID
     * @param authToken
     * @param minutes
     * @return allocation UUID on success null otherwise.
     * @throws AllocationException
     */
    @WebMethod
    public String create( @WebParam( name = "componentUUID" )
    String componentUUID, @WebParam( name = "authToken" )
    String authToken, @WebParam( name = "minutes" )
    Integer minutes ) throws AllocationException;

    /**
     * Check to see if the device is currently allocated.
     * 
     * 
     * @param allocationUUID
     * @param authToken
     * @return
     * @throws AllocationException
     */
    @WebMethod
    public boolean verify( @WebParam( name = "allocationUUID" )
    String allocationUUID, @WebParam( name = "authToken" )
    String authToken ) throws AllocationException;

    /**
     * Update the specified allocation by extending the end time from now by the
     * specified number of minutes.
     * 
     * 
     * @param allocationUUID
     * @param authToken
     * @param minutes
     *            Number of minutes to set end to from now.
     * @return True if allocated false otherwise.
     * @throws AllocationException
     */
    @WebMethod
    public void update( @WebParam( name = "allocationUUID" )
    String allocationUUID, @WebParam( name = "authToken" )
    String authToken, @WebParam( name = "minutes" )
    Integer minutes ) throws AllocationException;

    /**
     * Free this allocation based on the allocationId specified.
     * 
     * 
     * @param allocationUUID
     * @param authToken
     * @return True of successfully released ,false otherwise.
     * @throws AllocationException
     */
    @WebMethod
    public void release( @WebParam( name = "allocationUUID" )
    String allocationUUID, @WebParam( name = "authToken" )
    String authToken ) throws AllocationException;

}