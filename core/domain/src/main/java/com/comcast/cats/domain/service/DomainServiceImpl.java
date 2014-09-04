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

import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.exception.DomainInstantiationException;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainUpdateException;

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
    /**
     * Persists a new record .
     * 
     * @param domain
     *            - The {@linkplain Domain} instance
     * @return The {@linkplain Domain} class
     * @throws DomainInstantiationException
     */
    @Override
    public Type create( Type domain ) throws DomainInstantiationException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds a record for a particular user based on the id specified.
     * 
     * @param id
     *            - The id of {@linkplain Domain} instance
     * @return The {@linkplain Domain} class
     * @throws DomainNotFoundException
     */
    @Override
    public Type findById( String id ) throws DomainNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user.The default implementation will
     * ignore reservation/allocation completely. This can be overridden at
     * domain service level.
     * 
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > find()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records in a specified range for a particular user. The default
     * implementation will ignore reservation/allocation completely. This can be
     * overridden at domain service level.
     * 
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > find( Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findActive()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List< Type > findActive( Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user based on a preconfigured query
     * with variable no of parameters.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @param queryName
     *            - A preconfigured query which is stored in database.
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > findByNamedQuery( String queryName )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user based on a preconfigured query
     * with variable no of parameters.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @param queryName
     *            - A preconfigured query which is stored in database.
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > findByNamedQueryOffset( String queryName, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user based on a preconfigured query
     * with variable no of parameters.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @param queryName
     *            - A preconfigured query which is stored in database.
     * @param params
     *            - Parameters for the query
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > findByNamedQuery( String queryName, Object... params )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user based on a preconfigured query
     * with variable no of parameters.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @param queryName
     *            - A preconfigured query which is stored in database.
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @param params
     *            - Parameters for the query
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > findByNamedQueryWithOffset( String queryName, Integer offset, Integer count, Object... params )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user based on a preconfigured query
     * with name-value parameters.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @param queryName
     *            - A preconfigured query which is stored in database.
     * @param params
     *            - Parameters for the query
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > findByNamedQueryAndNamedParams( String queryName, Map< String, Object > params )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Finds all records for a particular user based on a preconfigured query
     * with name-value parameters.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @param queryName
     *            - A preconfigured query which is stored in database.
     * @param params
     *            - Parameters for the query
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    @Override
    public List< Type > findByNamedQueryAndNamedParams( String queryName, Map< String, Object > params, Integer offset,
            Integer count )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Update the given domain.
     * 
     * @param domain
     *            - The {@linkplain Domain} instance
     * @return The updated {@linkplain Domain} class
     * @throws DomainUpdateException
     */
    @Override
    public Type update( Type domain ) throws DomainUpdateException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Delete the record based on the id specified.
     * 
     * @param id
     *            - The id of {@linkplain Domain} instance
     * @throws DomainNotFoundException
     */
    @Override
    public void delete( String id ) throws DomainNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Delete the record based on the type specified.
     * 
     * @param domain
     *            - The {@linkplain Domain} instance
     * @throws DomainNotFoundException
     */
    @Override
    public void delete( Type domain ) throws DomainNotFoundException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Get the count of total records.The default implementation will ignore
     * reservation/allocation completely. This can be overridden at domain
     * service level.
     * 
     * @return The total count of total records
     */
    @Override
    public Integer count()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public Integer countActive()
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    /**
     * Get the class of the domain.
     * 
     * @return The class
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public Class< Type > getDomainClass()
    {
        return ( Class< Type > ) this.getClass();
    }
}
