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
import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.exception.DomainInstantiationException;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.DomainUpdateException;
import com.comcast.cats.domain.exception.RackNotFoundException;

/**
 * Implementation of {@link ServerService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class RackServiceImpl extends DomainServiceImpl< Rack > implements RackService
{

    @Override
    public Rack create( Rack domain ) throws DomainInstantiationException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Rack update( Rack domain ) throws DomainUpdateException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete( Rack domain ) throws DomainNotFoundException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Rack findByName( String rackName ) throws RackNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Rack findByHardwareDeviceId( String hardwareDeviceId ) throws RackNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer count()
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + RACKS ) + COUNT;

        int count = 0;

        try
        {
            count = getResponseAsNumber( requestUrl );
        }
        catch ( Exception e )
        {
            logger.error( "Failed to retrieve settop count. " + e.getMessage() );
        }

        return count;
    }

    @Override
    public List< Rack > find( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + RACKS ) + offset + BACK_SLASH + count;

        List< Rack > racks = new ArrayList< Rack >();

        try
        {
            racks = getResponseAsDomainList( requestUrl, Rack.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve rack  list. " + e.getMessage() );
        }

        return racks;
    }
}
