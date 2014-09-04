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

import com.comcast.cats.domain.Slot;
import com.comcast.cats.domain.service.SlotService;

/**
 * 
 * @author SSugun00c
 * 
 */
public class SlotServiceIT extends BaseDomainIT
{
    @Inject
    protected SlotService slotService;

    @Override
    @Test
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( slotService );
    }

    @Test
    public void findAllByRackName()
    {
        String rackName = "MyRack";

        List< Slot > slots = slotService.findAllByRackName( rackName );

        logResult( slots.size() );

        for ( Slot slot : slots )
        {
            LOGGER.info( slot.toString() );
            LOGGER.info( "----------" );
        }
    }

    @Test
    public void findAllByRackId()
    {
        String rackId = "01b4015a-50ac-4bde-9ca8-554773e0b15c";

        List< Slot > slots = slotService.findAllByRackId( rackId );

        logResult( slots.size() );

        for ( Slot slot : slots )
        {
            LOGGER.info( slot.toString() );
            LOGGER.info( "----------" );
        }
    }
}
