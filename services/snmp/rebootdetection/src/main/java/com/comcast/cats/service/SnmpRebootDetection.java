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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootHostStatus;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.reboot.service.RebootDetectionService;

@Path( "/reboot/detection/{mac}/" )
public class SnmpRebootDetection implements RebootDetection
{
    protected static Logger logger = LoggerFactory.getLogger( SnmpCoreService.class );

    @Inject
    RebootDetectionService  rebootData;

    @PathParam( "mac" )
    String                  macAddress;

    @Override
    public String version()
    {
        logger.trace( "/{}/version", macAddress );
        return "0.1";
    }

    @Override
    public MonitorTarget current()
    {
        logger.trace( "/{}/current", macAddress );
        return DTOHelper.getMonitorTarget( rebootData.getHostByMac( macAddress ) );
    }

    @Override
    public List< RebootInfo > listAll()
    {
        logger.trace( "/{}/listAll", macAddress );
        return DTOHelper.getRebootInfo( rebootData.getDetectionInfo( macAddress ) );
    }

    @Override
    public Integer count()
    {
        logger.trace( "/{}/count", macAddress );
        return rebootData.getRebootDetectionCount( macAddress );
    }

    @Override
    public List< RebootInfo > list( Integer offset, Integer max, Date start, Date end )
    {
        logger.trace( "/{}/list", macAddress );
        return null;
    }

    @Override
    public void add( String ip, String ecmMacAddress, RebootHostStatus status )
    {
        logger.trace( "/{}/add", macAddress );
        RebootHostStatus tmp = status;
        if ( tmp == null )
        {
            tmp = RebootHostStatus.ENABLED;
        }
        rebootData.saveOrUpdate( macAddress, ip, ecmMacAddress, tmp );
        // return status;
    }

    @Override
    public void update( String ip, String ecmMacAddress, RebootHostStatus status )
    {
        logger.trace( "/{}/update", macAddress );
        rebootData.saveOrUpdate( macAddress, ip, ecmMacAddress, status );
        // return status;
    }

    @Override
    public void delete()
    {
        rebootData.delete( macAddress );
    }
}
