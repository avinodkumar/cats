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

import java.util.ArrayList;
import java.util.List;

import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.reboot.model.DetectionInfo;
import com.comcast.cats.reboot.model.Host;

/**
 * Class used to transfor entity beans into DTO (Data Transfer Objects).
 * 
 * @author cfrede001
 * 
 */
public class DTOHelper
{

    public static MonitorTarget getMonitorTarget( Host host )
    {
        if ( host == null )
        {
            return null;
        }
        MonitorTarget target = new MonitorTarget();
        target.setHostMacAddress( host.getMacAddress() );
        target.setEcmMacAddress( host.getEcm() );
        target.setIpAddress( host.getIpAddress() );
        target.setStatus( host.getStatus() );
        target.setUpTime( host.getUptime() );
        target.setExecutionDate( host.getLastModified() );
        return target;
    }

    public static List< MonitorTarget > getMonitorTarget( List< Host > hosts )
    {
        int size = 0;
        if ( hosts != null )
        {
            size = hosts.size();
        }
        List< MonitorTarget > targets = new ArrayList< MonitorTarget >( size );
        for ( Host h : hosts )
        {
            targets.add( getMonitorTarget( h ) );
        }
        return targets;
    }

    public static RebootInfo getRebootInfo( DetectionInfo detection )
    {
        if ( detection == null )
        {
            return null;
        }
        RebootInfo info = new RebootInfo();
        info.setCategory( detection.getCategory() );
        info.setExecutionDate( detection.getExecutionDate() );
        info.setStatus( detection.getStatus() );
        info.setUpTime( detection.getUpTime() );
        return info;
    }

    public static List< RebootInfo > getRebootInfo( List< DetectionInfo > detections )
    {
        int size = 0;
        if ( detections != null )
        {
            size = detections.size();
        }
        List< RebootInfo > infos = new ArrayList< RebootInfo >( size );
        for ( DetectionInfo d : detections )
        {
            infos.add( getRebootInfo( d ) );
        }
        return infos;
    }
}
