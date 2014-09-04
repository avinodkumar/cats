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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerStatistics;

import static com.comcast.cats.service.power.util.PowerConstants.DEFAULT_PORT;
import static com.comcast.cats.service.power.util.PowerConstants.SYNACCESS_SCHEME;
import static com.comcast.cats.service.power.util.PowerConstants.NP16_SCHEME;
import static com.comcast.cats.service.power.util.PowerConstants.NP16S_SCHEME;
import static com.comcast.cats.service.power.util.PowerConstants.WTI_NPS_SCHEME;
import static com.comcast.cats.service.power.util.PowerConstants.WTI_SCHEME;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON;
import static com.comcast.cats.info.SnmpServiceConstants.DEFAULT_SNMP_READ_COMMUNITY_STRING;
import static com.comcast.cats.info.SnmpServiceConstants.DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING;

/**
 * Factory class for creating PowerControllerDevice
 * 
 * @author aswathyann
 * @author bobyemmanuvel modified to do connection caching.
 * @author nishapk modified the connection caching to validate the scheme
 * 
 */
@Startup
@Singleton
public class PowerControllerDeviceFactoryImpl implements PowerControllerDeviceFactory
{
    private final Logger                                   log = LoggerFactory
                                                                       .getLogger( PowerControllerDeviceFactoryImpl.class );
    public static HashMap< String, PowerControllerDevice > controllerMap;

    @PostConstruct
    public void init()
    {
        log.info( "\n\n instatiating PowerControllerDeviceFactoryImpl\n\n" );
        controllerMap = new HashMap< String, PowerControllerDevice >();
    }

    public synchronized PowerControllerDevice createPowerControllerDevice( final URI path )
    {
        PowerControllerDevice powerControllerDevice = null;
        /*
         * Check if a connection has already been made to this Power device.
         */
        powerControllerDevice = controllerMap.get( path.getHost() );
        String scheme = path.getScheme();

        if ( powerControllerDevice != null )
        {

            /*
             * This is to check whether the current power controller device
             * present in the hash map is the actual one. For ex. if a WTI power
             * controller device is added to the hash map earlier due to
             * mis-configuration and now it is configured as an NPS power
             * controller device, this will check whether the already created
             * object is of type WTI or NPS and then perform actions
             * accordingly. If it is of same type, then the object is returned,
             * else the object is removed from the hash map and the new power
             * controller device is created and added to the hash map.
             */
            if ( powerControllerDevice.getScheme().equalsIgnoreCase( scheme ) )
            {
                return powerControllerDevice;
            }
            else
            {
                controllerMap.remove( path.getHost() );
                powerControllerDevice = null;
            }
        }

        if ( WTI_SCHEME.equalsIgnoreCase( scheme ) )
        {

            powerControllerDevice = new WTI_IPS_1600_PowerDevice();

            powerControllerDevice.setScheme( WTI_SCHEME );

        }
        else if ( SYNACCESS_SCHEME.equalsIgnoreCase( scheme ) || NP16_SCHEME.equalsIgnoreCase( scheme ) )
        {

            powerControllerDevice = new NetBooter_NP_1601D_PowerDevice();

            powerControllerDevice.setScheme( NP16_SCHEME );

        }
        else if ( NP16S_SCHEME.equalsIgnoreCase( scheme ) )
        {

            powerControllerDevice = new NetBooter_NP_16S_PowerDevice();

            powerControllerDevice.setScheme( NP16S_SCHEME );

        }
        else if ( WTI_NPS_SCHEME.equalsIgnoreCase( scheme ) )
        {

            powerControllerDevice = new NpsSnmpPowerDeviceRestImpl( path.getHost(), path.getPort(),
                    DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING, null, null, null );
            
            powerControllerDevice.setScheme( WTI_NPS_SCHEME );
        }
        powerControllerDevice = setPowerDeviceProperties( powerControllerDevice, path );

        return powerControllerDevice;
    }

    private PowerControllerDevice setPowerDeviceProperties( final PowerControllerDevice powerControllerDevice,
            final URI path )
    {
        Integer port = DEFAULT_PORT;

        if ( powerControllerDevice != null )
        {
            String ipAddr = path.getHost();

            powerControllerDevice.setIp( ipAddr );

            if ( path.getPort() >= 0 )
            {
                port = path.getPort();
            }

            powerControllerDevice.setPort( port );

            powerControllerDevice.setNumOutlets( 16 );

            powerControllerDevice.setState( POWER_ON );

            powerControllerDevice.createPowerDevConn();

            // Create PowerInfo object and set it in the PowerControllerDevice.

            PowerInfo powerInfo = new PowerInfo( powerControllerDevice.getScheme(), ipAddr, port,
                    new ArrayList< PowerStatistics >() );

            powerInfo.setNumOfOutlets( powerControllerDevice.getNumOutlets() );

            powerControllerDevice.setPowerInfo( powerInfo );

            controllerMap.put( ipAddr, powerControllerDevice );
        }
        return powerControllerDevice;
    }

    @PreDestroy
    public void destroyAllControllers()
    {
        log.info( "\n\n destroying PowerControllerDeviceFactoryImpl" );
        Collection< PowerControllerDevice > controllers = controllerMap.values();
        Iterator< PowerControllerDevice > iter = controllers.iterator();
        while ( iter.hasNext() )
        {
            PowerControllerDevice controller = iter.next();
            controller.destroy();
        }
        controllers.clear();
    }

    /**
     * Get all the information regarding power devices.
     * 
     * @return ArrayList<PowerInfo>
     */
    @Override
    public ArrayList< PowerInfo > getAllPowerDevicesInfo()
    {

        ArrayList< PowerInfo > powerInfoList = new ArrayList< PowerInfo >();
        Collection< PowerControllerDevice > powerControllerDeviceList = controllerMap.values();
        Iterator< PowerControllerDevice > iter = powerControllerDeviceList.iterator();
        while ( iter.hasNext() )
        {
            PowerControllerDevice powerControllerDevice = iter.next();
            powerInfoList.add( powerControllerDevice.getPowerInfo() );
        }

        return powerInfoList;
    }

    /**
     * Removing the power device.
     * 
     * @param ip
     */
    @Override
    public void removePowerDevice( String ip )
    {
        log.info( "\n\n removing Power Device [" + ip + "]" );
        // Remove PowerControllerDevice
        controllerMap.remove( ip );
    }
}
