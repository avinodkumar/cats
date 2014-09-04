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

import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.EnvironmentNotFoundException;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Implementation of {@link EnvironmentService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class EnvironmentServiceImpl extends DomainServiceImpl< Environment > implements EnvironmentService
{

	/**
     * Returns all Environment in a given environmentId.
     * 
     * @param environmentId
     * @return Environment
     * @throws DomainNotFoundException
     */
    @Override
    public Environment findById( String environmentId ) throws DomainNotFoundException
    {
        AssertUtil.isNullOrEmpty( environmentId, "Cannot search environment. environmentId cannot be null or empty" );
        String requestUrl = getBaseUrl( ENVIRONMENT ) + SHOW
                + CommonUtil.getNameValuePair( getParamMapById( environmentId, 0, 0 ) );

        Environment environment = null;
        try
        {
            environment = getResponseAsDomain( requestUrl, Environment.class );
        }
        catch ( DomainServiceException e )
        {
            throw new EnvironmentNotFoundException( e );
        }

        return environment;
    }

    /**
     * Returns all {@linkplain Server} in a given {@linkplain Environment}.
     * 
     * @param environmentId
     * @return List of {@linkplain Server}
     */
    @Override
    public List< Server > findServersByEnvironmentId( String environmentId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Returns all {@linkplain Service} in a given {@linkplain Environment}.
     * 
     * @param environmentId
     * @return List of {@linkplain Service}
     */
    @Override
    public List< Service > findServicesByEnvironmentId( String environmentId )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

}
