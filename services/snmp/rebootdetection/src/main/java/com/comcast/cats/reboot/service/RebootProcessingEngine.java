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
package com.comcast.cats.reboot.service;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.reboot.SettopSnmpUptimeRequestHandler;
import com.comcast.cats.reboot.model.Host;

/**
 * This class will check for "watched" devices and gather their reboot
 * information on a pre-configured time interval.
 * 
 * @author cfrede001
 * 
 */
@Singleton
@Startup
public class RebootProcessingEngine
{
    protected static Logger logger = LoggerFactory.getLogger( RebootProcessingEngine.class );

    @Inject
    RebootDetectionService  service;

    @Schedule( second = "0", minute = "*/1", hour = "*", persistent = false )
    public void run()
    {
        logger.trace( "run" );
        List< Host > hosts = service.getEnabledHosts();
        for ( Host h : hosts )
        {
            /*
             * Enclose this in a large try/catch to prevent single host
             * exception from preventing reboot detection for remainder of
             * devices.
             */
            try
            {
                SettopSnmpUptimeRequestHandler handler = new SettopSnmpUptimeRequestHandler( h.getMacAddress(),
                        h.getIpAddress(), h.getEcm() );
                Long uptime = handler.retrieveUptime();
                logger.info( "{} = {}", h, uptime );
                service.processUptime( h, uptime, handler.getRebootDetectionStatus() );
            }
            catch ( Exception e )
            {
                logger.warn( "Unknown Exception Caught processing hosts", e );
            }
        }
    }

    @PostConstruct
    public void init()
    {
        logger.trace( "RebootProcessingEngine is alive!" );
    }

}
