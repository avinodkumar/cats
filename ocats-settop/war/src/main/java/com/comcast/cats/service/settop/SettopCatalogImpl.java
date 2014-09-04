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

import static com.comcast.cats.info.SnmpServiceConstants.SNMP_CORE_SERVICE_MAPPED_NAME;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.SettopAllocationService;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.SnmpCoreService;
import com.comcast.cats.service.util.SettopApplicationConfigUtil;

/**
 * Class that stores {@linkplain Settop} to be retrieved by other areas within
 * the App Server.
 * 
 * @author cfrede001
 * @since 2.0.0
 * 
 */
@Remote( SettopCatalog.class )
@Singleton( mappedName = "cats/services/SettopCatalog" )
public class SettopCatalogImpl implements SettopCatalog
{
    private Map< String, Settop >   settops = new ConcurrentHashMap< String, Settop >();

   // @EJB( mappedName = "cats/services/SettopFactory" )
    private static SettopFactory           settopFactory;

    //@EJB( mappedName = ConfigServiceConstants.ALLOCATION_SERVICE_MAPPED_NAME )
    private static SettopAllocationService allocationService;

    private static Logger           logger  = Logger.getLogger( SettopCatalogImpl.class );
    
    static
    {
        try
        {
            InitialContext ctx = new InitialContext();
            allocationService = ( ( SettopAllocationService ) ctx.lookup( "java:global/OCATS/SettopAllocationServiceImpl!com.comcast.cats.service.SettopAllocationService" ) );
            settopFactory = ( ( SettopFactory ) ctx.lookup( "java:module/SettopFactoryImpl!com.comcast.cats.SettopFactory"  ) );
        }
        catch ( NamingException e )
        {
        	logger.error( "Lookup of AllocationService failed :" + e );
        }
    }

    private SettopToken getToken( String id, String userToken, String allocationId )
    {
        SettopToken token = new SettopToken();
        token.setCreated( new Date() );
        token.setAuthToken( userToken );
        token.setLastAccessed( new Date() );
        token.setSettopId( id );
        token.setAllocationId( allocationId );

        return token;
    }

    @Override
    public SettopToken obtainSettopByMac( String mac, String userToken ) throws SettopNotFoundException
    {
        SettopToken settopToken = SettopCatalogHelper.lookForExistingAllocation( mac, userToken );

        if ( ( null != settopToken ) )
        {
            try
            {	
            	System.out.println("allocationService "+allocationService);
                if ( !( allocationService.verify( settopToken.getAllocationId(), settopToken.getAuthToken() ) ) )
                {
                    settopToken = removeOldAllocationAndCreateNew( mac, userToken );
                }
            }
            catch ( AllocationException e )
            {
                settopToken = removeOldAllocationAndCreateNew( mac, userToken );
            }
        }
        else
        {
            settopToken = createNewAllocation( mac, userToken );
        }

        return settopToken;
    }

    private SettopToken removeOldAllocationAndCreateNew( String mac, String userToken ) throws SettopNotFoundException
    {
        SettopCatalogHelper.removeSettopToken( mac, userToken );
        return createNewAllocation( mac, userToken );
    }

    private SettopToken createNewAllocation( String mac, String userToken ) throws SettopNotFoundException
    {
        logger.info( "Creating new allocation for [" + mac + "] for user [" + userToken + "]" );
        
        SettopToken settopToken = null;
        try
        {
            Settop settop = settopFactory.findSettopByHostMac( mac );
            String allocationId = allocationService.create( settop.getId(), userToken,
                    SettopApplicationConfigUtil.getDefaultAllocationTimeInMins() );
            settops.put( allocationId, settop );
            settopToken = getToken( settop.getId(), userToken, allocationId );
            SettopCatalogHelper.putSettopToken( mac, settopToken, userToken );
        }
        catch ( AllocationException allocationException )
        {
            logAndThrowSettopError( userToken, allocationException.getMessage() );
        }

        return settopToken;
    }

    @Override
    public Settop lookupSettop( SettopToken token ) throws SettopNotFoundException
    {
        Settop settop = null;

        if ( settops.containsKey( token.getAllocationId() ) )
        {
            settop = settops.get( token.getAllocationId() );
        }
        else
        {
            logAndThrowSettopError( token.getAuthToken(), "No Settop with allocationId = " + token.getAllocationId()
                    + " found" );
        }
        return settop;
    }

    @Override
    public void removeSettop( SettopToken token )
    {
        SettopCatalogHelper.removeSettopToken( settops.get( token.getAllocationId() ).getHostMacAddress(),
                token.getAuthToken() );
        settops.remove( token.getAllocationId() );
    }

    @Override
    public String getLastError( SettopToken token ) throws SettopNotFoundException
    {
        return SettopCatalogHelper.gettSettopError( token.getAuthToken() );
    }

    @Override
    public void putSettopError( String token, String errorMsg )
    {
        SettopCatalogHelper.putSettopError( token, errorMsg );
    }

    private void logAndThrowSettopError( String userToken, String message ) throws SettopNotFoundException
    {
        putSettopError( userToken, message );
        throw new SettopNotFoundException( message );

    }

}