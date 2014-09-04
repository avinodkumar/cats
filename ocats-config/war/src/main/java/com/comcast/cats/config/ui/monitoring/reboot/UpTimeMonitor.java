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
package com.comcast.cats.config.ui.monitoring.reboot;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.reboot.MonitorTarget;

/**
 * Monitor Uptime for all settops.
 */
@Startup
@Singleton
public class UpTimeMonitor
{
    
    @Inject
    SettopSlotConfigService settopService;
    
    @Inject 
    Event< UpTimeEvent > upTimeNotifier;
    
    @Inject
    RebootMonitorService rebootMonitorService;

    static Logger logger = LoggerFactory.getLogger( UpTimeMonitor.class );
    
    @PostConstruct
    public void init(){
        getUpTimes();
    }

    @Schedule( second = "0", minute = "*/5", hour = "*", persistent = false )
    public synchronized void getUpTimes()
    {
        List<SettopReservationDesc> settops = settopService.getAllSettops();
        if(settops != null){
            for(SettopReservationDesc settop : settops){
                MonitorTarget monitorTarget = rebootMonitorService.getUptime(settop.getHostMacAddress());
                logger.trace( "UptimeMonitor getUpTimes "+monitorTarget );
                if(monitorTarget != null){
                    UpTimeEvent uptimeEvent = new UpTimeEvent( monitorTarget );
                    upTimeNotifier.fire( uptimeEvent );
                }
            }
        }
    }
}
