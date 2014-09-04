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
package com.comcast.cats.domain.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.ServiceType;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.service.EnvironmentService;
import com.comcast.cats.info.ConfigServiceConstants;

/**
 * Dummy implementation of {@link EnvironmentService} to support
 * EnvironmentFactory Factory for OCATS.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class EnvironmentServiceImpl extends DomainServiceImpl< Environment > implements EnvironmentService
{
    private static final String IR_SERVICE_WSDL_LOCATION    = "ir-service/IRService?wsdl";
    private static final String POWER_SERVICE_WSDL_LOCATION = "power-service/PowerService?wsdl";

    @Inject
    CatsProperties              catsProperties;

    @Override
    public Environment findById( String environmentId ) throws DomainNotFoundException
    {
        //
        Environment environment = new Environment();
        environment.setId( ConfigServiceConstants.CATS_VIRTUAL_ENVIRONMENT );
        environment.setName( ConfigServiceConstants.CATS_VIRTUAL_ENVIRONMENT );

        environment.setServers( getServers() );
        return environment;
    }

    private List< Server > getServers()
    {
        List< Server > servers = new ArrayList< Server >();
        Server server = new Server();
        server.setServices( getServices() );
        servers.add( server );
        return servers;
    }

    private List< Service > getServices()
    {
        List< Service > services = new ArrayList< Service >();

        Service irService = new Service( ServiceType.IR );
        Service powerService = new Service( ServiceType.POWER );
        Service recordingService = new Service( ServiceType.RECORDER );
        try
        {
            irService.setPath( new URL( catsProperties.getServerUrl() + IR_SERVICE_WSDL_LOCATION ) );
            powerService.setPath( new URL( catsProperties.getServerUrl() + POWER_SERVICE_WSDL_LOCATION ) );
            recordingService.setPath( new URL(catsProperties.getServerUrl()) );
        }
        catch ( MalformedURLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        services.add( irService );
        services.add( powerService );
        services.add( recordingService );
        logger.info( "EnvironmentServiceImpl services "+services );
        return services;
    }

    @Override
    public List< Server > findServersByEnvironmentId( String environmentId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Service > findServicesByEnvironmentId( String environmentId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

}
