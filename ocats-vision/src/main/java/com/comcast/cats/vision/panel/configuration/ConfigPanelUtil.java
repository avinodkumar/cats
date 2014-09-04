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
package com.comcast.cats.vision.panel.configuration;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.service.SettopDomainService;

/**
 * A helper class to obtain the data from the allocation client.
 * 
 * @author ajith
 * 
 */
@Named
public class ConfigPanelUtil
{

    /**
     * Reference of the SettopDomainService service.
     */
    private SettopDomainService settopDomainService;

    @Inject
    public ConfigPanelUtil( SettopDomainService settopDomainService )
    {
        this.settopDomainService = settopDomainService;
    }

    /**
     * Helper method to obtain a list of allocated settops wrapped in the
     * SettopEntry class. The user against which the allocated settops are taken
     * is the user that launched CATSVision.
     * 
     * @return A List of settopEntry objects that corresponds to allocated STBs.
     */
    public List< SettopReservationDesc > getAllAllocatedSettops()
    {
        return settopDomainService.findAllAllocated();
    }

    /**
     * Returns the list of settopEntry Objects that are not allocated and
     * currently available to the user against which CATSVision is launched.
     * 
     * @return List of settopEntry Objects that corresponds to reserved, but not
     *         allocated STBs.
     */
    public List< SettopReservationDesc > getAllAvailableSettops()
    {
        return settopDomainService.findAllAvailable();        
    }
}
