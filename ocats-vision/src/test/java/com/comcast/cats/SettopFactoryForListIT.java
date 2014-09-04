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
package com.comcast.cats;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.CatsAbstarctIT;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * Integration test for {@link SettopFactory} <br>
 * <br>
 * <br> 
 * @author subinsugunan
 * 
 */
public class SettopFactoryForListIT extends CatsAbstarctIT
{
    private SettopFactory settopFactory;

    public SettopFactoryForListIT()
    {
        settopFactory = catsFramework.getSettopFactory();
    }

    @Test
    public void testContext() throws SettopNotFoundException
    {
        assertNotNull( settopFactory );
    }

    @Test
    @Ignore
    public void testFindAllAvailableSettop()
    {
        List< Settop > availableSettops = Collections.emptyList();

        try
        {
            availableSettops = settopFactory.findAllAvailableSettop();
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
        LOGGER.info( "Available Settops = " + availableSettops.size() );
        LOGGER.info( "--------------------------------------" );
        for ( Settop settop : availableSettops )
        {
            LOGGER.info( settop );
            LOGGER.info( ( ( SettopDesc ) settop.getSettopInfo() ).getHardwareInterfaces() );
        }
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testFindAllAllAllocatedSettop()
    {
        List< Settop > allocatedSettops = Collections.emptyList();
        try
        {
            allocatedSettops = settopFactory.findAllAllocatedSettop();
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
        LOGGER.info( "Allocated Settops = " + allocatedSettops.size() );
        LOGGER.info( "--------------------------------------" );
        for ( Settop settop : allocatedSettops )
        {
            LOGGER.info( settop );
            LOGGER.info( ( ( SettopDesc ) settop.getSettopInfo() ).getHardwareInterfaces() );
        }

    }
}
