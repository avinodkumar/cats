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

import static com.comcast.cats.domain.ServiceType.AUDIO;
import static com.comcast.cats.domain.ServiceType.IR;
import static com.comcast.cats.domain.ServiceType.OCR;
import static com.comcast.cats.domain.ServiceType.POWER;
import static com.comcast.cats.domain.ServiceType.TRACE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.exception.ServerNotFoundException;
import com.comcast.cats.domain.service.DomainServiceImpl;
import com.comcast.cats.domain.service.ServerService;

/**
 * Alternative implementation of {@link ServerService} without any dependency
 * with configuration management system. This class should used for testing
 * purpose only.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class ServerServiceLocal extends DomainServiceImpl< Server > implements ServerService
{
	/**
     * to find services by server id.
     * @param serverId
     * @return List of {@linkplain Service}
     */
    @Override
    public List< Service > findServices( String serverId )
    {
        List< Service > services = new ArrayList< Service >();

        services.add( new Service( IR ) );
        services.add( new Service( POWER ) );
        services.add( new Service( AUDIO ) );
        services.add( new Service( OCR ) );
        services.add( new Service( TRACE ) );
        return services;
    }

    /**
     * to find server by mac id.
     * @param macId
     * @return {@linkplain Server}
     * @throws ServerNotFoundException
     */
    @Override
    public Server findByMacId( String macId ) throws ServerNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

}
