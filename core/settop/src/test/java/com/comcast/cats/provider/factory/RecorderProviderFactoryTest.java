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
package com.comcast.cats.provider.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.SettopImpl;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.impl.RecorderProviderFactoryImpl;

/**
 * 
 * @author sajayjk
 * 
 */
public class RecorderProviderFactoryTest
{

    RecorderProviderFactoryImpl providerFactory;
    String                      videoSeverHost = "xxxx";
    SettopImpl                  settop;

    @Before
    public void setUp()
    {
        providerFactory = new RecorderProviderFactoryImpl( videoSeverHost );
        settop = new SettopImpl();

    }

    @After
    public void tearDown()
    {
        providerFactory = null;
    }

    @Test( expected = ProviderCreationException.class )
    public void getProviderWithoutVideoTest() throws ProviderCreationException
    {
        providerFactory.getProvider( settop );

    }

    @Test( expected = ProviderCreationException.class )
    @Ignore
    public void getProviderWithNullServerURLTest() throws ProviderCreationException
    {
        providerFactory = new RecorderProviderFactoryImpl( null );
        providerFactory.getProvider( settop );

    }

    @Test( expected = ProviderCreationException.class )
    @Ignore
    public void getProviderWithoutServerURLTest() throws ProviderCreationException
    {
        providerFactory = new RecorderProviderFactoryImpl( "" );
        providerFactory.getProvider( settop );
    }

    @Test
    public void getProviderSuccessTest() throws ProviderCreationException
    {
        try
        {
            SettopImpl mockSettop = EasyMock.createMock( SettopImpl.class );
            EasyMock.expect( mockSettop.getVideoPath() ).andReturn( new URI( "axis://192.168.120.2/?camera=2" ) );
            EasyMock.expect( mockSettop.getHostMacAddress() ).andReturn( "XX:XX:XX:XX:XX:XX" );
            EasyMock.replay( mockSettop );
            assertNotNull( providerFactory.getProvider( mockSettop ) );
        }
        catch ( URISyntaxException e )
        {
            fail();
        }

    }

}
