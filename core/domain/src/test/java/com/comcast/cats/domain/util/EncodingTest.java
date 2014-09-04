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
package com.comcast.cats.domain.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.domain.SettopDesc;

@ContextConfiguration( locations={ "classpath:application-config-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
public class EncodingTest
{
    @Inject
    RestTemplate         restTemplate;

    protected final Logger  LOGGER     = LoggerFactory.getLogger( getClass() );

    public static String authToken  = "17a8602a-d50d-11e0-a350-005056b400d2";

    public static String url        = "https://localhost/rest/cats/settop/settopGroup/list-available?count=1&token="
                                            + authToken + "&name=CATS Development & Test Group&offset=0";
    public static String urlEncoded = "https://localhost/rest/cats/settop/settopGroup/list-available?count=1&token="
                                            + authToken + "&name=CATS%20Development%20%26%20Test%20Group&offset=0";

    /**
     * 
     * No encoding will be performed, expected URISyntaxException.
     * 
     * @throws RestClientException
     * 
     * @throws URISyntaxException
     */
    @Test( expected = URISyntaxException.class )
    public void getForObjectURI() throws RestClientException, URISyntaxException
    {
        SettopDesc settopDesc = restTemplate.getForObject( new URI( url ), SettopDesc.class );
        LOGGER.info( settopDesc.toString() );
    }

    @Test( expected = URISyntaxException.class )
    public void getForObjectURL() throws MalformedURLException, RestClientException, URISyntaxException
    {
        URL tmpUrl = new URL( url );
        SettopDesc settopDesc = restTemplate.getForObject( tmpUrl.toURI(), SettopDesc.class );
        LOGGER.info( settopDesc.toString() );
    }

    @Test
    public void getForObjectMap() throws Exception
    {
        String nameVal = URLEncoder.encode( "CATS Development & Test Group", "UTF-8" );

        Map< String, String > params = new HashMap< String, String >();
        params.put( "count", "1" );
        params.put( "token", authToken );
        params.put( "name", nameVal );
        params.put( "offset", "0" );

        SettopDesc settopDesc = restTemplate
                .getForObject(
                        "https://localhost/rest/cats/settop/settopGroup/list-available?count={count}&token={token}&name={name}offset={offset}",
                        SettopDesc.class, params );
        LOGGER.info( settopDesc.toString() );
    }

}
