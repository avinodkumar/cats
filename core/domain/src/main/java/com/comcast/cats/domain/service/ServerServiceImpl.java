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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;

import org.springframework.http.HttpMethod;

import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.ServerNotFoundException;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Implementation of {@link ServerService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class ServerServiceImpl extends DomainServiceImpl< Server > implements ServerService
{
    /**
     * Finds a CATS {@linkplain Server} based on macId of
     * {@linkplain SettopDesc}
     * 
     * @param macId
     *            - MAC address of the settop box
     * @return The {@linkplain Server}
     * @throws ServerNotFoundException
     */
    @Override
    public Server findByMacId( String macId ) throws ServerNotFoundException
    {
        AssertUtil.isNullOrEmpty( macId, "Cannot search server. macId cannot be null or empty" );
        String requestUrl = getBaseUrl( SERVER ) + SHOW + CommonUtil.getNameValuePair( getParamMapByMacId( macId ) );

        Server server = null;
        try
        {
            server = getResponseAsDomain( requestUrl, Server.class );
        }
        catch ( DomainServiceException e )
        {
            throw new ServerNotFoundException( e.getMessage() );
        }

        return server;
    }

    /**
     * Return all {@linkplain Service} from a given {@linkplain Server}
     * 
     * @param serverId
     *            - Id of the {@linkplain Server}
     * @return A {@linkplain List} of {@linkplain Service} of the specified
     *         {@linkplain Server}
     */
    @Override
    public List< Service > findServices( String serverId )
    {
        AssertUtil.isNullOrEmpty( serverId, "Cannot search services. serverId cannot be null or empty" );
        String requestUrl = getBaseUrl( SERVER ) + SERVICE + BACK_SLASH + LIST
                + CommonUtil.getNameValuePair( getParamMapById( serverId, 0, 0 ) );
        logger.debug( requestUrl );

        List< Service > services = new ArrayList< Service >();

        try
        {
            services = getResponseAsDomainList( requestUrl );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve List< Service >. " + e.getMessage() );
        }

        return services;
    }

    @SuppressWarnings( "unchecked" )
    private List< Service > getResponseAsDomainList( String requestUrl ) throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.GET, requestUrl );
        List< Service > responselist = Collections.emptyList();

        try
        {
            responselist = ( List< Service > ) restTemplateProducer.getRestTemplate().getForObject( requestUrl, Service.class );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.GET, requestUrl );
        }
        return responselist;
    }

}
