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
package com.comcast.cats.service.ir.redrat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.keymanager.domain.IrDeviceDTO;
import com.comcast.cats.service.IrPort;
import com.comcast.cats.service.KeyManagerProxy;
import com.comcast.cats.service.RedRatManager;
import com.comcast.cats.service.impl.KeyManagerProxyProviderRest;
import com.comcast.cats.telnet.TelnetConnection;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class RedRatManagerImpl implements RedRatManager
{

    List< RedRatDevice >                irDevices         = new ArrayList< RedRatDevice >();

    private static Map<RedRatDevice, RedRatHub> irNetBoxHubMap = new HashMap<RedRatDevice, RedRatHub>();
    
    private static Map<String, RedRatHub> redratHubMap = new HashMap<String, RedRatHub>();

    private Provider< KeyManagerProxy > keyManagerProxyProvider;

    private static final Logger         logger            = LoggerFactory.getLogger( RedRatManagerImpl.class );

    private static final int            IRNETBOX_MAXPORTS = 16;
    
    public RedRatManagerImpl(){
        keyManagerProxyProvider = new KeyManagerProxyProviderRest();
        logger.debug( "Creating default instance RedRatManagerImpl keymanagerProxy "+keyManagerProxyProvider);
        init();
    }
    
    public RedRatManagerImpl(Provider< KeyManagerProxy > keymanagerProxy){
        logger.debug ( "Creating overriden RedRatManagerImpl keymanagerProxy "+keymanagerProxy );
        keyManagerProxyProvider = keymanagerProxy;
        init();
    }

    public void init()
    {
        try{
    		logger.trace( "RedRatManager init" );
    		refresh();
        }catch(Exception e){
           logger.warn( "Redratmanager init exception "+e.getMessage() );
        }
    }

    @Override
    public List< RedRatDevice > getIrDevices()
    {
        return irDevices;
    }

    @Override
    @AccessTimeout(value=TelnetConnection.DEFAULT_READ_TIMEOUT,unit=TimeUnit.MILLISECONDS)
    public RedRatDevice getIrDevice( String ip )
    {
        synchronized(new Object()){
            RedRatDevice retVal = null;
            logger.info( "RedRatManager getIrDevice irDevices " + irDevices );
            if (ip != null && irDevices != null )
            {
                for ( RedRatDevice irDevice : irDevices )
                {
                    if ( irDevice instanceof IrNetBoxPro )
                    {
                        if ( ( ( IrNetBoxPro ) irDevice ).getIpAddress().equals( ip ) )
                        {
                            retVal = irDevice;
                        }
                    }
                }
            }
            return retVal;
        }
    }

    @SuppressWarnings( "unchecked" )
    @Schedule( second = "0", minute = "*/5", hour = "*", persistent = false )
    @AccessTimeout(value=TelnetConnection.DEFAULT_READ_TIMEOUT,unit=TimeUnit.MILLISECONDS)
    public synchronized void refresh()
    {
        // TODO: Once MDS in CATS 4 is available this should obtain data from
        // MDS.
        // For now, it will be made available through keymanager web app.
        logger.info( "RedRatManager refresh " );
        KeyManagerProxy kmProxy = keyManagerProxyProvider.get();
        logger.info( "RedRatManager refresh  kmProxy "+kmProxy );
        if ( kmProxy != null )
        {
            List< IrDeviceDTO > irDevicesDTOList = kmProxy.getIrDevices();
            logger.info( "RedRatManager refresh  irDevicesDTOList "+irDevicesDTOList );
            List< RedRatDevice > irDevicesNewList = processDTO( irDevicesDTOList );
            logger.info( "RedRatManager refresh " + irDevicesNewList );

            if ( irDevicesNewList != null )
            {
                List< RedRatDevice > removedIrDevices = new ArrayList< RedRatDevice >( irDevices );
                Collection< RedRatDevice > retainedDevicesCollection = null;
                if ( !irDevices.isEmpty() )
                {
                    retainedDevicesCollection = CollectionUtils.intersection( irDevices, irDevicesNewList );
                    logger.info( "RedRatManager refresh retainedDevicesCollection " + retainedDevicesCollection );
                    removedIrDevices.removeAll( retainedDevicesCollection );

                }
                logger.info( "RedRatManager refresh removedIrDevices " + removedIrDevices );
                boolean response = false; 
                if(removedIrDevices != null && !removedIrDevices.isEmpty())
                {
                    for(RedRatDevice removedDevice : removedIrDevices){
                    	RedRatHub redratHub = irNetBoxHubMap.get(removedDevice);
                    	response = redratHub.blackListRedRat( removedDevice ); 
                    }
                }
                logger.info( "RedRatManager blacklist response " + response );

                //lets whitelist all boxes all times
                //else if we restart the hub (to accommodate a new keyset
                // we'll have no way of whitelisting already added boxes unless we redploy.
                if(irDevicesNewList != null && !irDevicesNewList.isEmpty()){
                	 for(RedRatDevice redratDevice : irDevicesNewList){
                     	RedRatHub redratHub = irNetBoxHubMap.get(redratDevice);
                     	response = redratHub.whiteListRedRat( redratDevice ); 
                     }
                }
                logger.info( "RedRatManager whitelist response " + response );
                irDevices = irDevicesNewList;
            }
        }

    }

    private List< RedRatDevice > processDTO( List< IrDeviceDTO > irDevicesDTOList )
    {
        List< RedRatDevice > irDeviceList = null;

        if ( irDevicesDTOList != null )
        {
            irDeviceList = new ArrayList< RedRatDevice >();
            for ( IrDeviceDTO irDeviceDTO : irDevicesDTOList )
            {
                if(irDeviceDTO.getDeviceType() != null){
                    switch ( irDeviceDTO.getDeviceType() )
                    {
                    case IRNETBOXPRO3:
                        IrNetBoxPro irNetBoxPro = new IrNetBoxPro( irDeviceDTO.getId(), irDeviceDTO.getIpAdress() );
                        logger.debug("Redratmanager processDTO irNetBoxPro "+irNetBoxPro.getIpAddress());
                        irNetBoxPro.setIrPorts( getPorts( irNetBoxPro ) );
                        irDeviceList.add( irNetBoxPro );
                        
                        RedRatHub prevRedRatHub = irNetBoxHubMap.get(irNetBoxPro);
                        if(prevRedRatHub != null && !prevRedRatHub.getRedratHubHost().equals(irDeviceDTO.getRedRatHubIp())){
                        	// redrat moved to another hub; blacklist in first
                        	logger.info("Blackisting redrat that moved from hub "+prevRedRatHub.getRedratHubHost()+" to hub "+irDeviceDTO.getIpAdress());
                        	prevRedRatHub.blackListRedRat(irNetBoxPro);
                        }
                        
                        RedRatHub redratHub = redratHubMap.get(irDeviceDTO.getRedRatHubIp());
                        if(redratHub == null){
                        	redratHub = new RedRatHub(irDeviceDTO.getRedRatHubIp(), irDeviceDTO.getRedRatHubPort());
                        	redratHubMap.put(irDeviceDTO.getRedRatHubIp(), redratHub);
                        }
                        logger.debug("RedRatHub for IrnetBox "+irNetBoxPro.getIpAddress()+" is "+redratHub.getRedratHubHost());
                        irNetBoxHubMap.put(irNetBoxPro, redratHub);
                        break;
                    }
                }
            }
        }
        return irDeviceList;
    }

    private List< IrPort > getPorts( IrNetBoxPro irNetBox )
    {
        List< IrPort > ports = new ArrayList< IrPort >();
        for ( int i = 1; i <= IRNETBOX_MAXPORTS; i++ )
        {
            IrNetBoxProPort port = new IrNetBoxProPort( i, irNetBox );
            ports.add( port );
        }
        return ports;
    }
    
    public static RedRatHub getRedRatHub(RedRatDevice redratDevice){
    	 logger.debug("getRedRatHub "+redratDevice);
    	return irNetBoxHubMap.get(redratDevice);
    }
}
