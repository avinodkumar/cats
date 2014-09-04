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
package com.comcast.cats.service.settop;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.PowerProviderServiceImpl;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.RemoteProviderServiceImpl;
import com.comcast.cats.service.DeviceSearchService;
import com.comcast.cats.service.IRService;
import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceConstants;
import com.comcast.cats.service.SettopAllocationService;

/**
 * This is an alternative implementation of the SettopFactory for the
 * application Server (JBoss).
 * 
 * @author subinsugunan
 * 
 */
@Remote( SettopFactory.class )
@Singleton( mappedName = "cats/services/SettopFactory" )
public class SettopFactoryImpl implements SettopFactory
{
    //@EJB( mappedName = ConfigServiceConstants.DEVICE_SEARCH_SERVICE_MAPPED_NAME )
    private static DeviceSearchService deviceSearchService;

 //   @EJB( mappedName = PowerServiceConstants.MAPPED_NAME )
    private static PowerService        powerService;

//    @EJB( mappedName = IRServiceConstants.MAPPED_NAME )
    private static IRService           irService;
    
    static
    {
        try
        {
            InitialContext ctx = new InitialContext();
            deviceSearchService = ( ( DeviceSearchService ) ctx.lookup( "java:global/OCATS/DeviceSearchServiceImpl!com.comcast.cats.service.DeviceSearchService" ) );
            powerService = ( ( PowerService ) ctx.lookup( "java:module/PowerServiceImpl!com.comcast.cats.service.PowerService" ) );
            irService = ( ( IRService ) ctx.lookup( "java:module/IRServiceWSImpl!com.comcast.cats.service.IRService" ) );
        }
        catch ( NamingException e )
        {
        }
    }

    @Override
    public Settop findSettopByHostIpAddress( String arg0 ) throws SettopNotFoundException
    {
        return null;
    }

    /**
     * This method will use {@link DeviceSearchService} , which in turn call
     * REST APIs to get metadata to create {@link SettopDesc} object. Any
     * wrong input will result in a {@link javax.xml.ws.soap.SOAPFaultException}
     * . Other error condition will result in {@link DeviceSearchException},
     * currently which is internally handled in this method.
     */
    @Override
    public Settop findSettopByHostMac( String mac ) throws SettopNotFoundException
    {
        SettopDesc settopDesc = null;
        Settop settop = null;
        settopDesc = deviceSearchService.findByMacId( mac );

        if ( null != settopDesc )
        {
            settop = createSettop( settopDesc );
        }

        return settop;
    }

    @Override
    public Settop findSettopByUnitAddress( String arg0 ) throws SettopNotFoundException
    {
        return null;
    }

    private Settop createSettop( SettopDesc settopDesc )
    {
        SettopImpl settop = new SettopImpl();

        settop.setSettopInfo( settopDesc );
        settop.setPower( createPowerProvider( settopDesc ) );
        settop.setRemote( createRemoteProvider( settopDesc ) );

        return settop;
    }

    /**
     * Helper method to instantiate a RemoteProvider for this settop.
     * 
     * @param settopDesc
     *            - Settop description which includes reference to the Remote
     *            path.
     * @param autoTuneEnabled
     * @return RemoteProvider implementation
     */
    private RemoteProvider createRemoteProvider( SettopDesc settopDesc )
    {
        RemoteProviderServiceImpl remoteProvider = new RemoteProviderServiceImpl( irService,
                settopDesc.getRemotePath(), settopDesc.getRemoteType() );
        return remoteProvider;
    }

    /**
     * Helper method to instantiate a PowerProvider for this settop.
     * 
     * @param settopDesc
     *            - Settop description which includes reference to the Power
     *            path.
     * @return PowerProvider implementation
     */
    private PowerProvider createPowerProvider( SettopDesc settopDesc )
    {
        PowerProviderServiceImpl powerProvider = new PowerProviderServiceImpl( powerService, settopDesc.getPowerPath() );
        return powerProvider;
    }

    @Override
    public List< Settop > findAllSettopByCriteria( Map< String, String > arg1 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllSettopByCriteria not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllSettopByModel( String arg0 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllSettopByModel not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllSettopByPropertyValue( String arg0, String arg1 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllSettopByPropertyValue not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllSettopByPropertyValues( String arg0, String[] arg1 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllSettopByPropertyValues not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllSettopByRemoteType( String arg0 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllSettopByRemoteType not supported yet for app server." );
    }

    @Override
    public List< Settop > findSettopByHostMac( List< String > arg0 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findSettopByHostMac not supported yet for app server." );
    }

    @Override
    public List< Settop > findSettopByHostMac( List< String > arg0, boolean arg1 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findSettopByHostMac not supported yet for app server." );
    }

    @Override
    public String getSettopCreationError()
    {
        throw new UnsupportedOperationException( "getSettopCreationError not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllSettopByGroupName( String arg0 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllSettopByGroupName not supported yet for app server." );
    }

    @Override
    public List< Settop > findAvailableSettopByGroupName( String arg0 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAvailableSettopByGroupName not supported yet for app server." );
    }

    @Override
    public Settop findSettopByHostMac( String arg0, boolean arg1 ) throws SettopNotFoundException, AllocationException
    {
        throw new UnsupportedOperationException(
                "Overloaded version of findSettopByHostMac is not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllAllocatedSettop() throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllAllocatedSettop not supported yet for app server." );
    }

    @Override
    public List< Settop > findAllAvailableSettop() throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "findAllAvailableSettop not supported yet for app server." );
    }

}
