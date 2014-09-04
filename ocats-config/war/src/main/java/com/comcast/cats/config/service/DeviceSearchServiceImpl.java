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

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.DeviceSearchService;
import com.comcast.cats.utils.AssertUtil;


@Remote( DeviceSearchService.class )
@WebService( name = ConfigServiceConstants.DEVICE_SEARCH_SERVICE_NAME, portName = ConfigServiceConstants.DEVICE_SEARCH_SERVICE_PORT_NAME, targetNamespace = ConfigServiceConstants.NAMESPACE, endpointInterface = ConfigServiceConstants.DEVICE_SEARCH_SERVICE_ENDPOINT_INTERFACE )
@Stateless
public class DeviceSearchServiceImpl implements DeviceSearchService
{
    @Inject
    private SettopSlotConfigService settopSlotConfigService;

    @Override
    public SettopDesc findByMacId( String hostMacAddress ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( hostMacAddress, "MAC ID cannot be null or empty" );
        AssertUtil.isValidMacId( hostMacAddress, "Invalid MAC ID" );

        SettopDesc settop = settopSlotConfigService.findSettopByMac( hostMacAddress );

        if ( null == settop )
        {
            throw new SettopNotFoundException( "No settop found with [MAC-ID][" + hostMacAddress + "]" );
        }

        return settop;
    }

    @Override
    public List< SettopReservationDesc > findAllAvailableSettopReservationDesc()
    {
        return settopSlotConfigService.getAllSettops();
    }

    @Override
    public List< SettopReservationDesc > findAllAllocatedSettopReservationDesc()
    {
        throw new UnsupportedOperationException( "Not supported in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllAllocated()
    {
        throw new UnsupportedOperationException( "Not supported in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllAvailable()
    {
        throw new UnsupportedOperationException( "Not supported in OCATS" );
    }

}