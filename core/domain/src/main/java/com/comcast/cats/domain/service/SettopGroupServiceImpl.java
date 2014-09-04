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

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.SettopGroupInstantiationException;
import com.comcast.cats.domain.exception.SettopGroupNotFoundException;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Implementation of {@link SettopGroupService}.
 * 
 * @author aswathyann
 * 
 */

@Named
public class SettopGroupServiceImpl extends DomainServiceImpl< SettopGroup > implements SettopGroupService
{
    @Override
    public SettopGroup create( List< SettopDesc > settopDescList, String name )
            throws SettopGroupInstantiationException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public SettopGroup findByName( String settopGroupName ) throws SettopGroupNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public SettopGroup add( String settopGroupId, SettopDesc settopDesc ) throws SettopGroupNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public SettopGroup add( SettopGroup settopGroup, SettopDesc settopDesc ) throws SettopGroupNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public SettopGroup remove( String settopGroupId, String settopDesc ) throws SettopGroupNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public SettopGroup remove( SettopGroup settopGroup, String settopDesc ) throws SettopGroupNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public void removeAll( String settopGroupId ) throws SettopGroupNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }


    /**
     * Return all {@link SettopGroup}
     * 
     * @return A {@linkplain List} of {@link SettopGroup}
     */
    @Override
    public List< SettopGroup > findAllSettopGroups()
    {
        String requestUrl = getBaseUrl( SETTOP_GROUPS );

        List< SettopGroup > settopGroups = getResponseAsSettopGroupList( requestUrl );

        return settopGroups;
    }

    /**
     * Return all {@link SettopGroup}
     * 
     * @return A {@linkplain List} of {@link SettopGroup}
     */
    @Override
    public List< SettopGroup > findAvailableSettopGroups()
    {

        String requestUrl = getBaseUrl( SETTOP_GROUPS ) + CommonUtil.getNameValuePair( getParamMapByToken() );

        List< SettopGroup > settopGroups = getResponseAsSettopGroupList( requestUrl );

        return settopGroups;
    }

    private List< SettopGroup > getResponseAsSettopGroupList( String requestUrl )
    {
        List< SettopGroup > settopGroups = new ArrayList< SettopGroup >();

        try
        {
            settopGroups = getResponseAsDomainList( requestUrl, SettopGroup.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve List< SettopGroup >. " + e.getMessage() );
        }
        return settopGroups;
    }

    @Override
    public Integer count()
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + SETTOP_GROUPS ) + COUNT;

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
    public List< SettopGroup > find( Integer offset, Integer count )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + SETTOP_GROUPS ) + offset + BACK_SLASH + count;

        List< SettopGroup > settopGroups = new ArrayList< SettopGroup >();
        try
        {
            settopGroups = getResponseAsDomainList( requestUrl, SettopGroup.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve settopGroup  list. " + e.getMessage() );
        }

        return settopGroups;
    }
}
