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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.service.IRServiceEndpoint;
import static com.comcast.cats.RemoteCommand.*;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : Deepa
 * @since : 20th Sept 2012 Description : The Class RemoteProviderServiceImplTest
 *        is the unit test of {@link RemoteProviderServiceImpl}.
 */
public class RemoteProviderServiceImplTest
{
    private static final String endPointStr = "http://192.168.160.201:8080/ir-service/IRService?wsdl";
    private static URI          path;
    private static String       keySet      = "COMCAST";
    private RemoteProvider      provider;

    public RemoteProviderServiceImplTest() throws URISyntaxException
    {
        path = new URI( "gc100://192.168.160.201/?port=2" );
    }

    @BeforeMethod
    public void instantiateProvider() throws MalformedURLException
    {
        IRServiceEndpoint endpoint = new IRServiceEndpoint( new URL( endPointStr ) );
        provider = new RemoteProviderServiceImpl( endpoint.getIRServiceImplPort(), path, keySet );
    }

    @Test
    public void runRemoteCommands()
    {
        try
        {
            provider.pressKey( GUIDE );
            Thread.sleep( 500 );
            provider.pressKey( DOWN );
            Thread.sleep( 500 );
            provider.pressKey( DOWN );
            Thread.sleep( 500 );
            provider.pressKey( DOWN );
            Thread.sleep( 500 );
            provider.pressKey( EXIT );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }
}
