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
package com.comcast.cats.mock.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.ServiceType;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.service.DomainServiceImpl;
import com.comcast.cats.domain.service.EnvironmentService;

/**
 * Alternative implementation of {@link EnvironmentService} without any
 * dependency with configuration management system. This class should used for
 * testing purpose only.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class EnvironmentServiceLocal extends DomainServiceImpl< Environment > implements EnvironmentService
{
	/**
     * to find servers by environment id.
     * @param environmentId
     * @return List of {@linkplain Server}
     */
    @Override
    public List< Server > findServersByEnvironmentId( String environmentId )
    {

        String baseUrl = System.getProperty( CatsProperties.SERVER_URL_PROPERTY );

        List< Server > serverList = new ArrayList< Server >();
        List< Service > serviceList = new ArrayList< Service >();

        try
        {
            Server server = new Server();
            Random random = new Random();
          
            server.setId( String.valueOf( random.nextLong() ) );
            server.setName( "Dummy" );
            server.setHost( "Dummy" );

            Service settopService = new Service();

            settopService.setPath( new URL( baseUrl + "settop-service/SettopService?wsdl" ) );

            settopService.setServiceType( ServiceType.SETTOP );
            serviceList.add( settopService );

            Service irService = new Service();
            irService.setPath( new URL( baseUrl + "ir-service/IRService?WSDL" ) );
            irService.setServiceType( ServiceType.IR );
            serviceList.add( irService );

            Service powerService = new Service();
            powerService.setPath( new URL( baseUrl + "power-service/PowerService?WSDL" ) );
            powerService.setServiceType( ServiceType.POWER );
            serviceList.add( powerService );

            Service audioMonitorService = new Service();
            audioMonitorService.setPath( new URL( baseUrl + "audio-monitor-service/AudioMonitorService?wsdl" ) );
            audioMonitorService.setServiceType( ServiceType.AUDIO );
            serviceList.add( audioMonitorService );

            Service traceService = new Service();
            traceService.setPath( new URL( baseUrl + "trace-service/TraceService?wsdl" ) );
            traceService.setServiceType( ServiceType.TRACE );
            serviceList.add( traceService );

            Service easService = new Service();
            easService.setPath( new URL( baseUrl + "eas-service/EasService?wsdl" ) );
            easService.setServiceType( ServiceType.EAS );
            serviceList.add( easService );

            server.setServices( serviceList );
            serverList.add( server );
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
        return serverList;
    }

    /**
     * to find environment by id.
     * @param envId
     * @return {@linkplain Environment}
     * @throws {@link DomainNotFoundException}
     */
    @Override
    public Environment findById( String envId ) throws DomainNotFoundException
    {
        Environment environment = new Environment();
        environment.setId( envId );
        environment.setServers( findServersByEnvironmentId( envId ) );
        return environment;
    }

    /**
     * to find services by environment id.
     * @param environmentId
     * @return List of {@linkplain Service}
     */
    @Override
    public List< Service > findServicesByEnvironmentId( String environmentId )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

}
