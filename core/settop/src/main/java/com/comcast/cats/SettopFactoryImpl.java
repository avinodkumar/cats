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
package com.comcast.cats;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.comcast.cats.decorator.DecoratorFactory;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.ServiceInstantiationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.domain.util.CommonUtil;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;

/**
 * Implementation of {@link SettopFactory}, which will use the domain API to
 * create {@link SettopDesc}
 * 
 * @author cfrede001,subinsugunan
 * @since 2.0.0
 */
@Named
public class SettopFactoryImpl implements SettopFactory
{
    private final Logger                     LOGGER                     = LoggerFactory.getLogger( getClass() );

    private SettopDomainService           settopDomainService;
    private EnvironmentFactory            environmentFactory;
    private StringBuilder                 settopCreationErrorMessage = new StringBuilder( "" );
    private SettopExclusiveAccessEnforcer settopExclusiveAccessEnforcer;
    private DecoratorFactory              decoratorFactory;

    /**
	 * Constructor
	 * @param settopDomainService {@linkplain SettopDomainService}
	 * @param environmentFactory {@linkplain EnvironmentFactory}
	 * @param settopExclusiveAccessEnforcer {@linkplain SettopExclusiveAccessEnforcer}
	 * @param decoratorFactory {@linkplain DecoratorFactory}
	 */
    @Inject
    public SettopFactoryImpl( SettopDomainService settopDomainService, EnvironmentFactory environmentFactory,
            SettopExclusiveAccessEnforcer settopExclusiveAccessEnforcer, 
            DecoratorFactory decoratorFactory )
    {
        this.settopDomainService = settopDomainService;
        this.environmentFactory = environmentFactory;
        this.settopExclusiveAccessEnforcer = settopExclusiveAccessEnforcer;
        this.decoratorFactory = decoratorFactory;
    }

    /**
	 * To find the settop with host mac id
	 * @param macId
	 * @return {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public Settop findSettopByHostMac( String macId ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( macId, "Cannot search settops. macId cannot be null or empty" );

        if ( !CommonUtil.isValidMacId( macId ) )
        {
            throw new SettopNotFoundException( "A proper macId is required for Settops creation." );
        }

        SettopDesc settopDesc = settopDomainService.findByMacId( macId );
        SettopImpl settop = new SettopImpl();
        settop.setSettopInfo( settopDesc );
        try
        {
            environmentFactory.wireSettop( settop );
        }
        catch ( ServiceInstantiationException e )
        {
            throw new SettopNotFoundException( "Failed to create service interfaces for settop[" + macId + "], "
                    + e.getMessage() );
        }
        settop.setLogger( getSettopLogger( settop.getLogDirectory() ) );

        return decoratorFactory.decorate( settop );
    }

    /**
	 * To find the settop with host mac id
	 * @param macId
	 * @param isAllocationRequired
	 * @return {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public Settop findSettopByHostMac( String macId, boolean isAllocationRequired ) throws SettopNotFoundException,
            AllocationException
    {
        Settop settop = findSettopByHostMac( macId );
        if ( isAllocationRequired )
        {
            settopExclusiveAccessEnforcer.lock( settop );
        }
        return settop;
    }

    /**
     * Convenience method to create a list of Settops from a list of SettopDesc
     * 
     * @param settopDescList
     *            - List of descriptions to be used to create settops.
     * @return - List of instantiated settop objects.
     * @throws SettopNotFoundException
     *             - If SettopDesc list is empty or no settops were added.
     */
    private List< Settop > createSettops( List< SettopDesc > settopDescList ) throws SettopNotFoundException
    {
        if ( null == settopDescList || settopDescList.isEmpty() )
        {
            throw new SettopNotFoundException( "Settop list is NULL or empty" );
        }

        List< Settop > settopList = new ArrayList< Settop >( settopDescList.size() );
        SettopImpl settop = null;
        for ( SettopDesc settopDesc : settopDescList )
        {
            if ( ( null != settopDesc ) && ( null != settopDesc.getEnvironmentId() ) )
            {
                LOGGER.info( "Creating Settop for" + settopDesc.toString() );
                settop = new SettopImpl();
                settop.setSettopInfo( settopDesc );
                try
                {
                    environmentFactory.wireSettop( settop );
                    settopList.add( settop );
                    settop.setLogger( getSettopLogger(settop.getLogDirectory()));
                }
                catch ( ServiceInstantiationException e )
                {
                    throw new SettopNotFoundException( e );
                }
            }
            else
            {
                LOGGER.error( "Settop creation failed for " + settopDesc
                        + " Either SettopDesc was null or settopDesc.getEnvironmentId() was null" );
            }
        }
        if ( settopList.size() <= 0 )
        {
            throw new SettopNotFoundException( "No Settops were able to be created" );
        }
        return settopList;
    }

    private Logger getSettopLogger( String logDirectory )
    {
    
    	Map<String,String> mdcMap = new HashMap<String,String>();
    	mdcMap.put("SettopMac", logDirectory);
    	mdcMap.put("LogFileName", "RunTimeExecution.log");
        MDC.setContextMap(mdcMap);
        URL logFileUrl = getClass().getResource( "/settop-log4j.xml" );
     //   DOMConfigurator.configure( logFileUrl );
        Logger settopLogger = LoggerFactory.getLogger("Settop");
       	return settopLogger;
	}
    
    /**
	 * To find the settop with unit address.
	 * @param unitAddress
	 * @return {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public Settop findSettopByUnitAddress( String unitAddress ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( unitAddress, "Cannot search settops.UnitAddress cannot be null or empty" );

        List< Settop > settops = findAllSettopByPropertyValues( SettopConstants.UNIT_ADDRESS_PROPERTY, new String[]
            { unitAddress } );

        if ( settops.size() <= 0 )
        {
            throw new SettopNotFoundException( "No settop found with the given unitAddress = " + unitAddress );
        }

        return settops.get( 0 );
    }

    /**
	 * To find the settop with host ip address.
	 * @param ip
	 * @return {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public Settop findSettopByHostIpAddress( String ip ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( ip, "Cannot search settops. Ip cannot be null or empty" );

        if ( !CommonUtil.isValidIp( ip ) )
        {
            throw new SettopNotFoundException( "Cannot search settops. invalid IP format" );
        }

        List< Settop > settops = findAllSettopByPropertyValues( SettopConstants.HOST_IP_PROPERTY, new String[]
            { ip } );

        if ( settops.size() <= 0 )
        {
            throw new SettopNotFoundException( "No settop found with the given host ip = " + ip );
        }

        return settops.get( 0 );
    }

    /**
	 * To find all settop by Remote Type.
	 * @param remoteType - String
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAllSettopByRemoteType( String remoteType ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( remoteType, "Cannot search settops. Remote type cannot be null or empty" );

        List< Settop > settops = findAllSettopByPropertyValues( SettopConstants.REMOTE_TYPE_PROPERTY, new String[]
            { remoteType } );

        if ( settops.size() <= 0 )
        {
            throw new SettopNotFoundException( "No settop found with the given remoteType = " + remoteType );
        }

        return settops;
    }

    /**
	 * To find all settop by model.
	 * @param model
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAllSettopByModel( String model ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( model, "Cannot search settops. Model cannot be null or empty" );

        List< Settop > settops = findAllSettopByPropertyValues( SettopConstants.MODEL_PROPERTY, new String[]
            { model } );

        if ( settops.size() <= 0 )
        {
            throw new SettopNotFoundException( "No settop found with the given model = " + model );
        }

        return settops;
    }

    /**
	 * To find all settop with property and values
	 * @param property
	 * @param value - String
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAllSettopByPropertyValue( String property, String value ) throws SettopNotFoundException
    {
        return findAllSettopByPropertyValues( property, new String[]
            { value } );
    }

    /**
	 * To find all settop with property and values
	 * @param property
	 * @param values - String array
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAllSettopByPropertyValues( String property, String[] values )
            throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( property, "Cannot search settops. Property cannot be is null ot empty" );
        AssertUtil.isNullOrEmpty( values, "Cannot search settops. Values cannot be is null ot empty" );

        LOGGER.info( "Searching settops with property [" + property + "] " );
        List< SettopDesc > settopDescList = settopDomainService.findAvailableByProperty( property, values );
        return createSettops( settopDescList );
    }

    /**
	 * To find all the settop with criteria
	 * @param criteria 
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAllSettopByCriteria( Map< String, String > criteria ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmptyMap( criteria, "Cannot search settops. Criteria cannot be is null ot empty" );

        LOGGER.info( "Searching settops with criteria [" + criteria + "]" );
        List< SettopDesc > settopDescList = settopDomainService.findAvailableByCriteria( criteria );
        return createSettops( settopDescList );
    }

    /**
	 * To find the settop with host mac id
	 * @param macIdList - List of String
	 * @param failOnCreateError
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findSettopByHostMac( List< String > macIdList, boolean failOnCreateError )
            throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmptyList( macIdList, "Cannot search settops. macIdList cannot be is null ot empty" );

        List< Settop > settopList = new LinkedList< Settop >();
        StringBuilder settopCreationErrorMessage = new StringBuilder( "" );

        for ( String mac : macIdList )
        {
            try
            {
                settopList.add( findSettopByHostMac( mac ) );
            }
            catch ( SettopNotFoundException exception )
            {
                String error = "Unable to create settop for mac id " + mac + "-" + exception.getMessage() + "\n";
                settopCreationErrorMessage.append( error );
                if ( failOnCreateError )
                {
                    throw new SettopNotFoundException( error );
                }
            }
        }
        // If no settops could be locked throw an exception.
        if ( settopList.size() <= 0 )
        {
            throw new SettopNotFoundException( "Unable to create settops from the list" );
        }
        return settopList;
    }

    /**
	 * To find the settop by host mac id
	 * @param macIdList - List of String
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findSettopByHostMac( List< String > macIdList ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmptyList( macIdList, "Cannot search settops. macIdList cannot be is null ot empty" );

        return findSettopByHostMac( macIdList, false );
    }

    /**
	 * To get settop creation error.
	 * @return String
	 */
    @Override
    public String getSettopCreationError()
    {
        return settopCreationErrorMessage.toString();
    }

    /**
	 * To find all settop with group name.
	 * @param settopGroupName
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAllSettopByGroupName( String settopGroupName ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( settopGroupName, "settopGroupName cannot be null or empty" );

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( settopGroupName );
        List< SettopDesc > settopDescList = settopDomainService.findAllBySettopGroup( settopGroup );

        if ( ( null == settopDescList ) || ( settopDescList.isEmpty() ) )
        {
            throw new SettopNotFoundException( "No settop found for the given settop group : " + settopGroupName );
        }

        return createSettops( settopDescList );
    }

    /**
	 * To find available settop by group id.
	 * @param settopGroupName
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @Override
    public List< Settop > findAvailableSettopByGroupName( String settopGroupName ) throws SettopNotFoundException
    {
        AssertUtil.isNullOrEmpty( settopGroupName, "settopGroupName cannot be null or empty" );

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( settopGroupName );
        List< SettopDesc > settopDescList = settopDomainService.findAvailableBySettopGroup( settopGroup );

        if ( ( null == settopDescList ) || ( settopDescList.isEmpty() ) )
        {
            throw new SettopNotFoundException( "No settop available in the given settop group : " + settopGroupName );
        }

        return createSettops( settopDescList );
    }

    /**
	 * To find all allocated settop.
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @SuppressWarnings( "unchecked" )
    @Override
    public List< Settop > findAllAllocatedSettop() throws SettopNotFoundException
    {
        @SuppressWarnings( "rawtypes" )
        List settops = settopDomainService.findAllAllocated();

        if ( ( null == settops ) || ( settops.isEmpty() ) )
        {
            throw new SettopNotFoundException( "No settop allocated for the current user" );
        }
        else
        {
            return createSettops( settops );
        }
    }

    /**
	 * To find all available settop.
	 * @return List of {@linkplain Settop}
	 * @throws {@linkplain SettopNotFoundException}
	 */
    @SuppressWarnings( "unchecked" )
    @Override
    public List< Settop > findAllAvailableSettop() throws SettopNotFoundException
    {
        @SuppressWarnings( "rawtypes" )
        List settops = settopDomainService.findAllAvailable();

        if ( ( null == settops ) || ( settops.isEmpty() ) )
        {
            throw new SettopNotFoundException( "No settop available for the current user" );
        }
        else
        {
            return createSettops( settops );
        }
    }

}
