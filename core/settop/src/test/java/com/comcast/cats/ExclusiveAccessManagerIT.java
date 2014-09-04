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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.junit.Test;

import com.comcast.cats.provider.ExclusiveAccessManager;
import com.comcast.cats.provider.RemoteProvider;

/**
 * Integration test for {@link ExclusiveAccessManager} <br>
 * <br>
 * 
 * @author subinsugunan
 * 
 */
public class ExclusiveAccessManagerIT extends CatsAbstarctTestCase
{
    @Inject
    private SettopFactory          settopFactory;

    @Inject
    private ExclusiveAccessManager exclusiveAccessManager;

    private static Settop          settop;

    @Test
    public void testContext()
    {
        assertNotNull( settopFactory );
        assertNotNull( exclusiveAccessManager );
    }

    @Test
    public void testLockAndRelease()
    {

        try
        {
            settop = settopFactory.findSettopByHostMac( dataProvider.getMacId() );
            assertNotNull( settop );
            assertEquals( settop.getHostMacAddress(), dataProvider.getMacId() );

            LOGGER.trace( "Power Locator: " + settop.getPowerLocator() );
            LOGGER.trace( "Remote Locator: " + settop.getRemoteLocator() );

            exclusiveAccessManager.lock( settop );

            RemoteProvider remote = settop.getRemote();
            assertNotNull( "settop.getRemote() cannot be null", remote );

            exclusiveAccessManager.release( settop );

        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

}
