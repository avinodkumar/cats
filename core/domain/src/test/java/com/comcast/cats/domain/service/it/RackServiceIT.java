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
package com.comcast.cats.domain.service.it;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.service.RackService;

/**
 * 
 * @author subinsugunan
 * 
 */
public class RackServiceIT extends BaseDomainIT
{
    @Inject
    protected RackService rackService;

    @Override
    @Test
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( rackService );
    }

    @Test
    public void testCount()
    {
        int count = rackService.count();
        logResult( count );
    }

    @Test
    public void findRack()
    {
        List< Rack > racks = rackService.find(0,25);

        logResult( racks.size() );

        for ( Rack rack : racks )
        {
            LOGGER.info( rack.toString() );
            LOGGER.info( "----------" );
        }
    }
}
