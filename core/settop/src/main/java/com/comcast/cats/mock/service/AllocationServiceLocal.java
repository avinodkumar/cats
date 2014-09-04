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

import java.util.List;
import java.util.Random;

import javax.inject.Named;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.AllocationCategory;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.service.AllocationService;
import com.comcast.cats.domain.service.DomainServiceImpl;

/**
 * Alternative implementation of {@link AllocationService} without any
 * dependency with configuration management system. This class should used for
 * testing purpose only.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class AllocationServiceLocal extends DomainServiceImpl< Allocation > implements AllocationService
{

    /**
     * Create allocation by component id.
     * 
     * @param componentId
     * @param duration
     * @return {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation createByComponentId( String componentId, Integer duration )
            throws AllocationInstantiationException
    {
        Allocation allocation = new Allocation();
        Random random = new Random();
        allocation.setId( componentId + random.nextInt() );
        return allocation;
    }

    /**
     * to verify.
     * 
     * @param arg0
     */
    @Override
    public Boolean verify( String arg0 )
    {
        return true;
    }

    /**
     * to release.
     * 
     * @param arg0
     * @throws AllocationNotFoundException
     */
    @Override
    public void release( String arg0 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    /**
     * to release.
     * 
     * @param arg0
     *            {@linkplain Allocation}
     * @throws AllocationNotFoundException
     */
    @Override
    public void release( Allocation arg0 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    /**
     * to release by component id.
     * 
     * @param arg0
     * @throws AllocationNotFoundException
     */
    @Override
    public void releaseByComponentId( String arg0 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    /**
     * to find allocation by component id.
     * 
     * @param arg0
     * @return {@linkplain Allocation}
     * @throws AllocationNotFoundException
     */
    @Override
    public Allocation findByComponentId( String arg0 ) throws AllocationNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to count active allocations.
     * 
     * @return Integer
     */
    @Override
    public Integer countActive()
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to find active allocations.
     * 
     * @return List of {@linkplain Allocation}
     */
    @Override
    public List< Allocation > findActive()
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to find active allocations.
     * 
     * @param arg0
     * @param arg1
     * @return List of {@linkplain Allocation}
     */
    @Override
    public List< Allocation > findActive( Integer arg0, Integer arg1 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to update allocations.
     * 
     * @param arg0
     * @param arg1
     * @return {@linkplain Allocation}
     * @throws AllocationNotFoundException
     */
    @Override
    public Allocation update( String arg0, Integer arg1 ) throws AllocationNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to create allocation by component id.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation createByComponentId( String arg0, Integer arg1, String arg2 )
            throws AllocationInstantiationException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to release allocation.
     * 
     * @param arg0
     * @param arg1
     * @throws AllocationNotFoundException
     */
    @Override
    public void release( String arg0, String arg1 ) throws AllocationNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to update allocation.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return {@linkplain Allocation}
     * @throws AllocationNotFoundException
     */
    @Override
    public Allocation update( String arg0, Integer arg1, String arg2 ) throws AllocationNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to verify allocation.
     * 
     * @param arg0
     * @param arg1
     * @return Boolean
     */
    @Override
    public Boolean verify( String arg0, String arg1 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to create allocation by component id.
     * 
     * @param componentId
     * @param duration
     * @param authToken
     * @param allocationCategory
     *            {@linkplain AllocationCategory}
     * @return {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation createByComponentId( String componentId, Integer duration, String authToken,
            AllocationCategory allocationCategory ) throws AllocationInstantiationException
    {

        Allocation allocation = new Allocation();
        Random random = new Random();
        allocation.setId( componentId + random.nextInt() );
        return allocation;
    }

    /**
     * to reacquire allocation by component id.
     * 
     * @param componentId
     * @return {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation reacquireByComponentId( String componentId ) throws AllocationInstantiationException
    {
        Allocation allocation = new Allocation();
        Random random = new Random();
        allocation.setId( componentId + random.nextInt() );
        return allocation;
    }

    /**
     * to reacquire allocation by component id.
     * 
     * @param componentId
     * @param authToken
     * @param allocationCategory
     *            {@linkplain AllocationCategory}
     * @return {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation reacquireByComponentId( String componentId, String authToken,
            AllocationCategory allocationCategory ) throws AllocationInstantiationException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to create allocation by component id.
     * 
     * @param componentId
     * @return {@linkplain Allocation}
     * @throws AllocationInstantiationException
     */
    @Override
    public Allocation createByComponentId( String componentId ) throws AllocationInstantiationException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * to update allocation.
     * 
     * @param allocationId
     * @return {@linkplain Allocation}
     * @throws AllocationNotFoundException
     */
    @Override
    public Allocation update( String allocationId ) throws AllocationNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

}
