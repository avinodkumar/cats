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
package com.comcast.cats.domain.configuration;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.comcast.cats.domain.configuration.CatsHome;
import com.comcast.cats.domain.configuration.CatsProperties;

/**
 * 
 * @author cfrede001
 * 
 */
public class CatsPropertiesTest
{
    public static final String CATS_SERVER_URL = "http://localhost:8080/";
    public static final String CATS_AUTH_TOKEN = "DummyAuthToken";

    public CatsPropertiesTest()
    {
        System.setProperty( "cats.server.url", CATS_SERVER_URL );
        System.setProperty( "cats.user.authToken", CATS_AUTH_TOKEN );
    }

    private CatsProperties createCatsProperties()
    {
        AnnotationConfigApplicationContext annCtx = new AnnotationConfigApplicationContext();
        annCtx.register( CatsHome.class );
        annCtx.register( CatsProperties.class );
        annCtx.refresh();
        return ( CatsProperties ) annCtx.getBean( CatsProperties.class );
    }

    @Test
    @Ignore
    public void testProperties()
    {
        CatsProperties props = createCatsProperties();
        assertEquals( props.getServerUrl(), CATS_SERVER_URL );
        assertEquals( props.getAuthToken(), CATS_AUTH_TOKEN );
        Assert.assertNotNull( props.getCatsHome() );
        try
        {
            Assert.assertNotNull( props.getServerHost() );
        }
        catch ( URISyntaxException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testPropertiesWithCatsProps()
    {
        // Set our dummy cats.props
        System.setProperty( "cats.home", "src/test/resources" );

        CatsProperties props = createCatsProperties();

        assertEquals( CATS_SERVER_URL, props.getServerUrl() );
    }

}