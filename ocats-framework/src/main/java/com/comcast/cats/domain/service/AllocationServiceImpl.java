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

import java.util.UUID;

import javax.inject.Named;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.AllocationCategory;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.service.AllocationService;

/**
 * Dummy implementation of {@link AllocationService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class AllocationServiceImpl extends AbstractAllocationServiceImpl
{

    @Override
    public void release( String arg0 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    @Override
    public void release( Allocation arg0 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    @Override
    public void release( String arg0, String arg1 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    @Override
    public void releaseByComponentId( String arg0 ) throws AllocationNotFoundException
    {
        // Do nothing
    }

    @Override
    public Boolean verify( String arg0 )
    {
        return true;
    }

    @Override
    public Allocation createByComponentId( String componentId ) throws AllocationInstantiationException
    {
      //dummy implementation
        return createByComponentId(componentId, 1);
    }

    @Override
    public Allocation update( String allocationId ) throws AllocationNotFoundException
    {
      //dummy implementation
        try
        {
            return createByComponentId(allocationId, 1);
        }
        catch ( AllocationInstantiationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Allocation createByComponentId( String componentId, Integer duration ) throws AllocationInstantiationException
    {
        Allocation allocation = new Allocation();
        allocation.setId( UUID.randomUUID().toString() );
        return allocation;
    }

    @Override
    public Allocation createByComponentId( String componentId, Integer duration, String authToken )
            throws AllocationInstantiationException
    {
        return createByComponentId(componentId, duration);
    }

    @Override
    public Allocation createByComponentId( String componentId, Integer duration, String authToken,AllocationCategory allocationCategory )
            throws AllocationInstantiationException
    {
        return createByComponentId(componentId, duration);
    }

    @Override
    public Allocation update( String allocationId, Integer duration ) throws AllocationNotFoundException
    {
        try
      {
          return createByComponentId(allocationId, duration);
      }
      catch ( AllocationInstantiationException e )
      {
          e.printStackTrace();
      }
      return null;
    }

    @Override
    public Allocation update( String allocationId, Integer arg1, String arg2 ) throws AllocationNotFoundException
    {
        try
      {
          return createByComponentId(allocationId, arg1);
      }
      catch ( AllocationInstantiationException e )
      {
          e.printStackTrace();
      }
      return null;
    }
}