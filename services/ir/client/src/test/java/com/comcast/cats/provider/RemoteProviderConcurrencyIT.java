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
package com.comcast.cats.provider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;
import com.comcast.cats.service.IRService;
import com.comcast.cats.service.IRServiceEndpoint;

/**
 * The Class RemoteProviderConcurrencyIT.
 * 
 * @Author : 
 * @since : 
 * Description : The Class RemoteProviderConcurrencyIT is the unit test of {@link RemoteProvider}.
 */
public class RemoteProviderConcurrencyIT
{
    private static final String             endPointStr   = "http://192.168.160.201:8080/ir-service/IRService?wsdl";
    public static final String              GC100_URI     = "gc100://192.168.160.201/?port=";
    private final String[]                  paths         =
                                                              {
            GC100_URI + "1",
            GC100_URI + "2",
            GC100_URI + "3",
            GC100_URI + "4",
            GC100_URI + "5",
            GC100_URI + "6"                                  };

    private final String[]                  alias         =
                                                              {
            "00:00:00:00:00:01",
            "00:00:00:00:00:02",
            "00:00:00:00:00:03",
            "00:00:00:00:00:04",
            "00:00:00:00:00:05",
            "00:00:00:00:00:06",                             };

    private static String[]                 keySets       =
                                                              {
            "COMCAST",
            "COMCAST",
            "COMCAST",
            "COMCAST",
            "COMCAST",
            "COMCAST"                                        };

    List< RemoteProviderConcurrencyThread > remoteThreads = new ArrayList< RemoteProviderConcurrencyThread >();

    public RemoteProviderConcurrencyIT() throws URISyntaxException, MalformedURLException
    {

        for ( int i = 0; i < paths.length; i++ )
        {
            IRServiceEndpoint endpoint = new IRServiceEndpoint( new URL( endPointStr ) );
            IRService irService = endpoint.getIRServiceImplPort();
            // IRService irService = null;
            URI locator = new URI( paths[ i ] );
            createThread( irService, locator, keySets[ i ], alias[ i ] );
        }

        // Create a bunch of dummy locators to test EJB pooling.
        for ( Integer test = 1; test <= 20; test++ )
        {
            for ( Integer port = 1; port <= 6; port++ )
            {
                IRServiceEndpoint endpoint = new IRServiceEndpoint( new URL( endPointStr ) );
                IRService irService = endpoint.getIRServiceImplPort();
                String path = "test://1.1.1" + test + "/?port=" + port;
                URI locator = new URI( path );
                createThread( irService, locator, "COMCAST", "" );
            }
        }
    }

    protected void createThread( IRService irService, URI locator, String keySet, String alias )
    {
        RemoteProvider remote = new RemoteProviderServiceImpl( irService, locator, keySet );
        remoteThreads.add( new RemoteProviderConcurrencyThread( remote ) );
    }

    @Test
    public void runRemoteThreads()
    {
        start();
        join();
    }

    protected void start()
    {
        for ( Thread t : remoteThreads )
        {
            t.start();
        }
    }

    protected void join()
    {
        Integer totalErrors = 0;
        for ( Thread t : remoteThreads )
        {
            try
            {
                t.join();
                RemoteProviderConcurrencyThread remoteThread = ( RemoteProviderConcurrencyThread ) t;
                totalErrors += remoteThread.errors;
            }
            catch ( InterruptedException e )
            {                
                e.printStackTrace();
            }
        }

        System.out.println( "Total Errors = " + totalErrors );
    }
}
