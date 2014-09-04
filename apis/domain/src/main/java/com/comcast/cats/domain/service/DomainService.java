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

import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.DomainInstantiationException;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainUpdateException;

/**
 * Common service interface. An implicit parameter called 'AuthToken' [A string
 * which uniquely identifies a {@linkplain User}] should be used with every call
 * to the target system. The 'AuthToken' should be used for authentication and
 * authorization in the target system. The usage of 'AuthToken' will be
 * different for different {@linkplain Domain} type.
 * 
 * @author subinsugunan
 * 
 * @param <Type>
 *            - A {@linkplain Domain} class
 */
public interface DomainService< Type >
{
    /**
     * Persists a new record .
     * 
     * @param domain
     *            - The {@linkplain Domain} instance
     * @return The {@linkplain Domain} class
     * @throws DomainInstantiationException
     */
    Type create( Type domain ) throws DomainInstantiationException;

    /**
     * Finds a record for a particular user based on the id specified.
     * 
     * @param id
     *            - The id of {@linkplain Domain} instance
     * @return The {@linkplain Domain} class
     * @throws DomainNotFoundException
     */
    Type findById( String id ) throws DomainNotFoundException;

    /**
     * Finds all records. Not tied to a {@link User}.
     * 
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    List< Type > find();

    /**
     * Find all active records. Not tied to a {@link User}.
     * 
     * @return
     */
    List< Type > findActive();

    /**
     * Finds all records. Not tied to a {@link User}.
     * 
     * @param offset
     *            - The start position
     * @param count
     *            - The max result
     * @return A {@linkplain List} of {@linkplain Domain} instances
     */
    List< Type > find( Integer offset, Integer count );

    /**
     * Find all active records. Not tied to a {@link User}.
     * 
     * @param offset
     * @param count
     * @return
     */
    List< Type > findActive( Integer offset, Integer count );

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
    List< Type > findByNamedQuery( String queryName );

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
    List< Type > findByNamedQueryOffset( String queryName, Integer offset, Integer count );

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
    List< Type > findByNamedQuery( String queryName, Object... params );

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
    List< Type > findByNamedQueryWithOffset( String queryName, Integer offset, Integer count, Object... params );

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
    List< Type > findByNamedQueryAndNamedParams( String queryName, Map< String, Object > params );

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
    List< Type > findByNamedQueryAndNamedParams( String queryName, Map< String, Object > params, Integer offset,
            Integer count );

    /**
     * Update the given domain.
     * 
     * @param domain
     *            - The {@linkplain Domain} instance
     * @return The updated {@linkplain Domain} class
     * @throws DomainUpdateException
     */
    Type update( Type domain ) throws DomainUpdateException;

    /**
     * Delete the record based on the id specified.
     * 
     * @param id
     *            - The id of {@linkplain Domain} instance
     * @throws DomainNotFoundException
     */
    void delete( String id ) throws DomainNotFoundException;

    /**
     * Delete the record based on the type specified.
     * 
     * @param type
     *            - The {@linkplain Domain} instance
     * @throws DomainNotFoundException
     */
    void delete( Type domain ) throws DomainNotFoundException;

    /**
     * Count all records. Not tied to a {@link User}.
     * 
     * @return The total count of total records
     */
    Integer count();

    /**
     * Count all active records. Not tied to a {@link User}.
     * 
     * @return
     */
    Integer countActive();

    /**
     * Get the class of the domain.
     * 
     * @return The class
     */
    Class< Type > getDomainClass();

}
