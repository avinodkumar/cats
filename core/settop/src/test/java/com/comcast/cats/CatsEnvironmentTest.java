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

import javax.inject.Inject;

import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.provider.exceptions.ProviderCreationException;

/**
 * Test case for {@link CatsEnvironment}
 * 
 * @author subinsugunan
 * 
 */
public class CatsEnvironmentTest extends CatsAbstarctTestCase
{
    public static final String CATS_DEV  = "cats-dev-test-env-id";
    public static final String CATS_STAG = "cats-stag-test-env-id";

    @Inject
    private CatsEnvironment    catsEnvironment;

    @Test
    public void testContext()
    {
        assertNotNull( catsEnvironment );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopNull() throws ProviderCreationException
    {
        catsEnvironment.wireSettop( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopNullSettopInfo() throws ProviderCreationException
    {
        SettopImpl settop = new SettopImpl();
        catsEnvironment.wireSettop( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopNullEnvironmentId() throws ProviderCreationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop = new SettopImpl();
        settopDesc.setEnvironmentId( null );
        settop.setSettopInfo( settopDesc );

        catsEnvironment.wireSettop( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopEmptyEnvironmentId() throws ProviderCreationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop = new SettopImpl();
        settopDesc.setEnvironmentId( DataProvider.EMPTY_STRING );
        settop.setSettopInfo( settopDesc );

        catsEnvironment.wireSettop( settop );
    }

    @Test
    // ( timeout = 4000 )
    public void wireSettopOneSettopOneEnv() throws ProviderCreationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop_dev = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_dev.setSettopInfo( settopDesc );
        catsEnvironment.wireSettop( settop_dev );
    }

    @Test
    // ( timeout = 4000 )
    public void wireSettopTwoSettopOneEnv() throws ProviderCreationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop_dev = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_dev.setSettopInfo( settopDesc );
        catsEnvironment.wireSettop( settop_dev );

        SettopImpl settop_stag = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_stag.setSettopInfo( settopDesc );
        catsEnvironment.wireSettop( settop_stag );
    }

}
