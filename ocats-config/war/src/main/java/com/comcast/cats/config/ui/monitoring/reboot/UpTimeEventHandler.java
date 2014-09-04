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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.SettopCreateEvent;
import com.comcast.cats.config.ui.SettopDeleteEvent;
import com.comcast.cats.config.ui.SettopEditEvent;
import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.reboot.MonitorTarget;

@ManagedBean
@ApplicationScoped
public class UpTimeEventHandler
{

    /**
     * Map that holds latest RebootInfo for a settop mac address.
     * 
     * Note: Even though the bean is application scoped, this map should be
     * static because it seems like multiple instances of this Bean is being
     * created by @Observes.
     */
    private static Map< String, UpTimeBean > upTimeMap              = new HashMap< String, UpTimeBean >();

    private static final String              UNKNOWN_UPTIME_MESSAGE = "Could not retrieve uptime";

    private static Logger                    logger                 = LoggerFactory
                                                                            .getLogger( UpTimeEventHandler.class );

    @Inject
    SettopSlotConfigService                  settopSlotService;

    @Inject
    RackService                              rackService;

    @Inject
    RebootMonitorService                     rebootMonitorService;

    @PostConstruct
    public void init()
    {
        // since @Observer creates a new instance for each event fired, we
        // need to make sure that we control when refreshListing is called.
        if ( upTimeMap.isEmpty() )
        {
            refreshListing();
        }
    }

    public void refreshListing()
    {
        List< Rack > racks = rackService.getAllRacks();
        logger.trace( "UpTimeEventHandler " + racks );
        if ( racks != null )
        {
            for ( Rack rack : racks )
            {
                startWatching( rack.getName() );
            }
        }
    }

    private void startWatching( String rackName )
    {
        logger.debug( "rackName" + rackName );
        List< SettopReservationDesc > settops = settopSlotService.getAllSettopsByRack( rackName );
        if ( settops != null )
        {
            for ( SettopReservationDesc settop : settops )
            {
                logger.info( "startWatching settop " + settop.getHostMacAddress() );
                logger.info( "startWatching settop " + settop.getName() );
                if ( upTimeMap.get( settop.getHostMacAddress() ) == null )
                {
                    UpTimeBean upTimeBean = createUpTimeBeanForSettop( settop );
                    upTimeMap.put( settop.getHostMacAddress(), upTimeBean );
                    rebootMonitorService.startWatching( settop.getHostMacAddress(), settop.getMcardMacAddress(),
                            settop.getHostIp4Address() );
                }
            }
        }
    }

    private UpTimeBean createUpTimeBeanForSettop( SettopDesc settop )
    {
        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopId( settop.getId() );
        upTimeBean.setSettopMCardMac( settop.getMcardMacAddress() );
        upTimeBean.setSettopIP( settop.getHostIp4Address() );
        upTimeBean.setSettopMac( settop.getHostMacAddress() );
        upTimeBean.setSettopName( settop.getName() );
        upTimeBean.setSettopType( settop.getComponentType() );
        upTimeBean.setUpTime( UNKNOWN_UPTIME_MESSAGE );
        return upTimeBean;
    }

    /**
     * Get the uptime for a settop.
     * 
     * @param macAddress
     * @return upTime or appropriate error message
     */
    public String getUptime( String macAddress )
    {
        String retVal = UNKNOWN_UPTIME_MESSAGE;
        UpTimeBean upTimeBean = upTimeMap.get( macAddress );
        if ( upTimeBean != null )
        {
            retVal = upTimeBean.getUpTime();
        }

        logger.debug( "getUptime() macAddress " + macAddress + " uptime " + retVal );
        return retVal;
    }

    /**
     * Handles uptime events generated by {@link UpTimeAndRebootMonitor}.
     * 
     * @param uptimeEvent
     */
    public void handleUptimeEvent( @Observes
    UpTimeEvent uptimeEvent )
    {
        logger.trace( "Uptime event recieved " + uptimeEvent );
        // Add or replace rebootInfo for the box.
        MonitorTarget monitorTarget = uptimeEvent.getMonitorTarget();
        UpTimeBean uptimeBean = upTimeMap.get( monitorTarget.getHostMacAddress() );

        if ( uptimeBean != null )
        {
            if(monitorTarget.getUpTime() > 0){
                uptimeBean.setUpTime( monitorTarget.getFormattedUpTime());
            }else{
                uptimeBean.setUpTime(UNKNOWN_UPTIME_MESSAGE);
            }
        }
    }

    public List< UpTimeBean > getUpTimes()
    {
        logger.trace( "Uptime getUpTimes " );
        Collection< UpTimeBean > upTimeBeans = upTimeMap.values(); // returning
                                                                   // collestions.values
                                                                   // as is does
                                                                   // not work
                                                                   // well with
                                                                   // Primefaces.
        List< UpTimeBean > upTimeList = new ArrayList< UpTimeBean >();

        for ( UpTimeBean upTimeBean : upTimeBeans )
        {
            upTimeList.add( upTimeBean );
        }
        logger.debug( "Uptime getUpTimes upTimeList " + upTimeList );
        return upTimeList;
    }

    public void handleSettopEditEvent( @Observes
    SettopEditEvent settopEditEvent )
    {
        /*
         * If mac is edited, stop watching old one and start a new one. If ecm
         * or IP address is changed, update the existing one.
         */
        SettopDesc settop = settopEditEvent.getSettop();
        if ( upTimeMap.get( settop.getHostMacAddress() ) == null )
        {
            logger.info( "looks like mac has been edited" );
            // looks like mac has been edited. Find the settop that has changed.
            Collection< UpTimeBean > values = new ArrayList<>( upTimeMap.values());
            if ( values != null && !values.isEmpty() )
            {
                boolean settopFound = false;
                for ( UpTimeBean upTimeBean : values )
                {
                    if ( settop.getId().equals( upTimeBean.getSettopId() ) )
                    {
                        upTimeMap.remove( upTimeBean.getSettopMac() );
                        rebootMonitorService.stopWatching( upTimeBean.getSettopMac(), upTimeBean.getSettopMCardMac(),
                                upTimeBean.getSettopIP() );
                        logger.info( "Stopped watching " + upTimeBean.getSettopMac() );
                        logger.info( "Start watching " + settop.getHostMacAddress() );
                        upTimeMap.put( settop.getHostMacAddress(), createUpTimeBeanForSettop( settop ) );
                        rebootMonitorService.startWatching( settop.getHostMacAddress(), settop.getMcardMacAddress(),
                                settop.getHostIp4Address() );
                        settopFound = true;
                        break;
                    }

                    if ( !settopFound )
                    {
                        // An already added settop with no mac, got a mac now]
                        logger.info( "An already added settop with no mac, got a mac now" );
                        refreshListing();
                    }
                }
            }

        }
        else
        {
            logger.info( "looks like another property has changed." );
            // looks like another property has changed. //create a new one and replace since its easier to find and edit each of existing one.
            UpTimeBean upTimeBean = createUpTimeBeanForSettop( settop );
            upTimeMap.put( settop.getHostMacAddress(), upTimeBean );
            rebootMonitorService.updateSettopInMonitor( upTimeBean.getSettopMac(), upTimeBean.getSettopMCardMac(),
                    upTimeBean.getSettopIP() );
            logger.info( "Update watching " + upTimeBean.getSettopMac() );
        }
    }

    public void handleSettopCreateEvent( @Observes
    SettopCreateEvent settopCreatedEvent )
    {
        logger.info( "UpTimeEventhandler handleSettopCreateEvent" );
        refreshListing();
    }

    public void handleSettopDeleteEvent( @Observes
    SettopDeleteEvent settopDeleteEvent )
    {
        logger.info( "UpTimeEventhandler handleSettopDeleteEvent" );
        if ( settopDeleteEvent != null && settopDeleteEvent.getSettop() != null )
        {
            upTimeMap.remove( settopDeleteEvent.getSettop().getHostMacAddress() );
            rebootMonitorService.stopWatching( settopDeleteEvent.getSettop().getHostMacAddress(), settopDeleteEvent
                    .getSettop().getMcardMacAddress(), settopDeleteEvent.getSettop().getHostIp4Address() );
        }
    }
}
