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

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.AllocationCategory;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.service.AllocationService;

public abstract class AbstractAllocationServiceImpl extends DomainServiceImpl< Allocation > implements AllocationService
{

    @Override
    public Integer countActive()
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< Allocation > findActive()
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Allocation findByComponentId( String arg0 ) throws AllocationNotFoundException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Boolean verify( String arg0, String arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }
    

    @Override
    public Allocation reacquireByComponentId( String componentId ) throws AllocationInstantiationException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Allocation reacquireByComponentId( String componentId, String authToken,
            AllocationCategory allocationCategory ) throws AllocationInstantiationException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

}
