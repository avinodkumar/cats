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
package com.comcast.cats.config.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;

import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.SettopAllocationService;

/**
 * A service interface to get access to Settops. Services include but not
 * limited to lock, unlock and verify access to Settops.
 * 
 * @author subinsugunan
 */
@Remote( SettopAllocationService.class )
@WebService( name = ConfigServiceConstants.ALLOCATION_SERVICE_NAME, portName = ConfigServiceConstants.ALLOCATION_SERVICE_PORT_NAME, targetNamespace = ConfigServiceConstants.NAMESPACE, endpointInterface = ConfigServiceConstants.ALLOCATION_SERVICE_ENDPOINT_INTERFACE )
@Stateless
public class SettopAllocationServiceImpl implements SettopAllocationService
{
    /**
     * No-args constructor as required by EJB 3 SLSB contract.
     */
    
    static Map<String, String> allocationMap = new HashMap< String, String>();
    
    public SettopAllocationServiceImpl()
    {
    }

    @Override
    public String create( String componentUUID, String authToken, Integer minutes ) throws AllocationException
    {
        String allocationUUID = UUID.randomUUID().toString();
        allocationMap.put( authToken, allocationUUID );
        return allocationUUID;
    }

    @Override
    public void release( String allocationUUID, String authToken ) throws AllocationException
    {
        allocationMap.remove( authToken );
    }

    @Override
    public void update( String allocationUUID, String authToken, Integer minutes ) throws AllocationException
    {

    }

    @Override
    public boolean verify( String allocationUUID, String authToken ) throws AllocationException
    {
        return allocationMap.containsKey( authToken );
    }
}
