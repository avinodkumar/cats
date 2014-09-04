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
package com.comcast.cats.service.power;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Path;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceConstants;
import com.comcast.cats.service.PowerStat;
import com.comcast.cats.service.PowerStatistics;

/**
 * 
 * @author SSugun00c
 * 
 */
@Path( "/statistics" )
public class PowerStatRest implements PowerStat
{
    @EJB( mappedName = PowerServiceConstants.MAPPED_NAME )
    private PowerService powerService;

    public List< PowerInfo > getAllPowerInfo()
    {
        return powerService.getAllPowerDevicesInfo();
    }

    public List< PowerStatistics > getPowerStatisticsForDevice( String host )
    {
        return powerService.getPowerStatisticsPerDevice( host );
    }

    public PowerStatistics getPowerStatistics( String host, Integer outlet )
    {
        PowerStatistics powerStats = powerService.getPowerOutletStatistics( host, outlet );
        return powerStats;
    }
}
