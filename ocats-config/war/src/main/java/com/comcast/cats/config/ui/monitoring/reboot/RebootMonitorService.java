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
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.config.ui.monitoring.MonitoringConstants;
import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootHostStatus;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.service.RebootDetection;

/**
 * Making this call @Named to get injected will not work for the case of
 * RebootHistoryLazyDataModel, since it is instantiated by the
 * UptimeAndRebootStatusBean. A simple approach is to make all methods static.
 * 
 * @author skurup00c
 * 
 */
@Named
public class RebootMonitorService
{
    private static final String MAC_ARG  = "<mac>";
    private static Logger       logger   = LoggerFactory.getLogger( RebootMonitorService.class );
    private static String       restPath = null;

    @PostConstruct
    public void init()
    {
        String hostname = System.getProperty( MonitoringConstants.SNMP_SERVICE_HOST );
        try
        {
            if ( hostname == null || hostname.isEmpty() )
            {
                hostname = AuthController.getHostAddress();
            }
            logger.trace( "UpTimeMonitor " + hostname );
            if ( hostname == null )
            {
                logger.warn( "System property " + MonitoringConstants.SNMP_SERVICE_HOST + " may not be set properly" );
            }
            else
            {
                restPath = "http://" + hostname + "/snmp-service-reboot/rest/reboot/detection/" + MAC_ARG + "/";
            }
        }
        catch ( Exception e )
        {
            logger.warn( e.getMessage() );
        }
        logger.info( "restPath " + restPath );
    }

    public synchronized MonitorTarget getUptime( String macAddress )
    {
        MonitorTarget monitorTarget = null;
        logger.trace( "getUptime macAddress " + macAddress );
        logger.debug( "restPath " + restPath );
        if ( restPath != null && macAddress != null && !macAddress.isEmpty() )
        {
            try
            {
                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    monitorTarget = rebootDetectionService.current();
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "getUptime Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }

        logger.info( "getUptime macAddress " + macAddress + " uptime " + monitorTarget );
        return monitorTarget;
    }

    public synchronized List< RebootInfo > listAllReboots( String macAddress )
    {
        List< RebootInfo > rebootInfoList = null;
        logger.trace( "listAllReboots macAddress " + macAddress );
        logger.debug( "restPath " + restPath );

        if ( restPath != null && macAddress != null && !macAddress.isEmpty() )
        {

            try
            {
                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    rebootInfoList = rebootDetectionService.listAll();
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "listReboots Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }
        return rebootInfoList;
    }

    public synchronized List< RebootInfo > listAllReboots( String macAddress, Date startDate, Date endDate )
    {
        List< RebootInfo > rebootInfoList = null;
        logger.trace( "listAllReboots macAddress " + macAddress );
        logger.debug( "restPath " + restPath );

        if ( restPath != null && macAddress != null && !macAddress.isEmpty() && startDate != null && endDate != null )
        {

            try
            {
                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    List< RebootInfo > reboots = rebootDetectionService.listAll();
                    if ( reboots != null )
                    {
                        rebootInfoList = new ArrayList< RebootInfo >();
                        for ( RebootInfo rebootInfo : reboots )
                        {
                            if ( rebootInfo.getExecutionDate().after( endDate )
                                    && rebootInfo.getExecutionDate().before( startDate ) )
                            {
                                rebootInfoList.add( rebootInfo );
                            }
                        }
                    }
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "listReboots Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }
        return rebootInfoList;
    }

    public int getRebootCount( String macAddress )
    {
        int rebootCount = -1;
        logger.trace( "getRebootCount ecmMacAddress " + macAddress );
        logger.debug( "restPath " + restPath );

        if ( restPath != null && macAddress != null && !macAddress.isEmpty() )
        {
            try
            {

                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    rebootCount = rebootDetectionService.count();
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "getRebootCount Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }
        logger.info( "listReboots ecmMacAddress " + macAddress + " rebootCount " + rebootCount );
        return rebootCount;
    }

    public void startWatching( String macAddress, String ecmMacAddress, String ipAddress )
    {
        if ( restPath != null && macAddress != null && !macAddress.isEmpty() && ipAddress != null
                && !ipAddress.isEmpty() && ecmMacAddress != null && !ecmMacAddress.isEmpty())
        {
            try
            {

                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    rebootDetectionService.add( ipAddress, ecmMacAddress, RebootHostStatus.ENABLED );
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "getRebootCount Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }
    }

    public void stopWatching( String macAddress, String ecmMacAddress, String ipAddress )
    {
        if ( restPath != null && macAddress != null && !macAddress.isEmpty() && ipAddress != null
                && !ipAddress.isEmpty() && ecmMacAddress != null && !ecmMacAddress.isEmpty())
        {
            try
            {
                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    rebootDetectionService.update( ipAddress, ecmMacAddress, RebootHostStatus.DISABLED );
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "getRebootCount Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }
    }
    

    public void updateSettopInMonitor(String macAddress, String ecmMacAddress, String ipAddress)
    {
        if ( restPath != null && macAddress != null && !macAddress.isEmpty() && ipAddress != null
                && !ipAddress.isEmpty() && ecmMacAddress != null && !ecmMacAddress.isEmpty())
        {
            try
            {
                RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                        restPath.replace( MAC_ARG, macAddress ) );
                if ( rebootDetectionService != null )
                {
                    rebootDetectionService.update( ipAddress, ecmMacAddress, RebootHostStatus.ENABLED );
                }
            }
            catch ( Exception e )
            { // catch any exceptions due to any reason like snmp service not
              // found etc
                logger.error( "getRebootCount Error occurred restPath " + restPath + " " + e.getMessage() );
            }
        }
        
    }

    public boolean isSNMPRebootServiceIsReachable()
    {
        boolean isReachable = false;
        try
        {
            RebootDetection rebootDetectionService = ProxyFactory.create( RebootDetection.class,
                    restPath.replace( MAC_ARG, "mac" ) ); // remove < & > as
                                                          // they are illegal
            if ( rebootDetectionService != null )
            {
                rebootDetectionService.count();
                isReachable = true;
            }
        }
        catch ( Exception e )
        { // catch any exceptions due to any reason like snmp service not found
          // etc
            logger.error( "checkIfSNMPRebootServiceIsReachable Error occurred restPath " + restPath + " "
                    + e.getMessage() );
        }

        return isReachable;
    }

}
