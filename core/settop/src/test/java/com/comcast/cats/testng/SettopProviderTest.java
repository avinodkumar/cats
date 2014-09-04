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
package com.comcast.cats.testng;

import static junit.framework.Assert.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;

/**
 * Testcase for {@link SettopProvider}
 * 
 * @author subinsugunan
 * 
 */
public class SettopProviderTest
{
    protected final Logger                LOGGER                        = LoggerFactory.getLogger( getClass() );
    private SettopExclusiveAccessEnforcer settopExclusiveAccessEnforcer = null;
    private Settop                        settop                        = null;

    /**
     * Make suer you have "CATS_HOME" environment variable set and the
     * "cats.props" is available under CATS_HOME. "cats.props" should have the
     * following entries.
     * 
     * <br>
     * <br>
     * <br>
     * e.g.
     * 
     * <pre>
     *    cats.server.url=http://192.168.160.202:8080/
     *    cats.user.authToken=8520
     *    settop.url=file:/E:/comcast-cats/evc-cats/settop-list.xml
     * </pre>
     * 
     */
    @Before
    public void setUp()
    {
        // Initialize CatsFramework
        CatsFramework catsFramework = new CatsFramework();
        assertNotNull( "catsFramework not initialized properly", catsFramework );

        settopExclusiveAccessEnforcer = SettopProvider.settopExclusiveAccessEnforcer;
    }

    @After
    public void tearDown() throws AllocationException
    {
        // exclusiveAccessManager.release( settop );
    }

    @Test
    @Ignore
    public void testGetSettops() throws Exception
    {
        Object[][] obj = SettopProvider.getSettops();

        if ( obj.length <= 0 )
        {
            // fail(
            // "Please make sure you have CATS_HOME/cats.pros and CATS_HOME/settop-list.xml are available and CATS_HOME/settop-list.xml contains valid entries"
            // );
        }
        else
        {
            settop = ( Settop ) SettopProvider.getSettops()[ 0 ][ 0 ];
            assertNotNull( "settop is NULL", settop );
            assertNotNull( "settopExclusiveAccessEnforcer is NULL", settopExclusiveAccessEnforcer );

            // exclusiveAccessManager.lock( settop );

            LOGGER.info( "Host Mac Address: " + settop.getHostMacAddress() );
            LOGGER.info( "Power Locator   : " + settop.getPowerLocator() );
            LOGGER.info( "Remote Locator  : " + settop.getRemoteLocator() );
            LOGGER.info( "Video Path      : " + settop.getVideoPath() );
        }

    }
}
