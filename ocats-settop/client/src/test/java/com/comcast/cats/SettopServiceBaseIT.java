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

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import com.comcast.cats.service.SettopService;
import com.comcast.cats.service.SettopServiceReturnMessage;

/**
 * Base test case.
 * 
 * @author subinsugunan
 * 
 */
public class SettopServiceBaseIT extends TestCase
{
    private final String                 serverBase               = "http://192.168.160.201:8080/";
    private SettopServiceEndpoint        settopServiceEndpoint;

    protected SettopService              settopService;
    protected final String               macId                    = "00:19:47:25:AC:A8";
    protected final String               authToken                = "183bb0fa-d50d-11e0-a350-005056b400d2";
    protected final String               authToken_dtbuildaccount = "ec18d884-12e0-4d64-b58a-43127c164732";
    protected SettopServiceReturnMessage settopServiceReturnMessage;

    public SettopServiceBaseIT()
    {
        super();
        try
        {
            settopServiceEndpoint = new SettopServiceEndpoint( new URL( serverBase
                    + SettopConstants.SETTOP_SERVICE_WSDL_LOCATION ) );
        }
        catch ( MalformedURLException e )
        {
            fail( e.getMessage() );
        }
        settopService = settopServiceEndpoint.getPort();
        assertNotNull( settopService );
    }

}
