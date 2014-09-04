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
import java.util.Map;

import javax.inject.Named;

import com.comcast.cats.domain.exception.DomainInstantiationException;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainUpdateException;
import com.comcast.cats.domain.service.DomainService;

/**
 * Implementation of {@link DomainService}.
 * 
 * @author subinsugunan
 * 
 * @param <Type>
 */
@Named
public class DomainServiceImpl< Type > extends AbstractService< Type > implements DomainService< Type >
{
    @Override
    public Type create( Type domain ) throws DomainInstantiationException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Type findById( String id ) throws DomainNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > find()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findByNamedQuery( String queryName )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findByNamedQuery( String queryName, Object... params )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findByNamedQueryAndNamedParams( String queryName, Map< String, Object > params )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Type update( Type domain ) throws DomainUpdateException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public void delete( String id ) throws DomainNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public void delete( Type domain ) throws DomainNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Integer count()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Class< Type > getDomainClass()
    {
        return ( Class< Type > ) this.getClass();
    }

    @Override
    public Integer countActive()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > find( Integer arg0, Integer arg1 )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findActive()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findActive( Integer arg0, Integer arg1 )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findByNamedQueryAndNamedParams( String arg0, Map< String, Object > arg1, Integer arg2,
            Integer arg3 )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findByNamedQueryOffset( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findByNamedQueryWithOffset( String arg0, Integer arg1, Integer arg2, Object... arg3 )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }
}
