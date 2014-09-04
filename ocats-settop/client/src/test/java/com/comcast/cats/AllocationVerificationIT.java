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

import static com.comcast.cats.RemoteCommand.INFO;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.service.SettopService;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Integration test to test allocation verification extending a valid
 * allocation.
 * 
 * 
 * @author subinsugunan
 * 
 */
public class AllocationVerificationIT extends TestCase
{
    private SettopServiceEndpoint      settopServiceEndpoint;
    private SettopService              settopService;
    private String                     serverBase        = "http://192.168.160.201:8080/";
    private String                     macId             = "00:22:10:21:A4:B0";
    private String                     authToken         = "183bb0fa-d50d-11e0-a350-005056b400d2";
    private SettopServiceReturnMessage settopServiceReturnMessage;
    private SettopToken                settopToken;
    private static final long          KEYPRESS_INTERVAL = 2000;

    private static Logger              logger            = Logger.getLogger( AllocationVerificationIT.class );

    @Before
    protected void setUp() throws MalformedURLException
    {
        settopServiceEndpoint = new SettopServiceEndpoint( new URL( serverBase
                + SettopConstants.SETTOP_SERVICE_WSDL_LOCATION ) );
        settopService = settopServiceEndpoint.getPort();
    }

    @Test
    public void testAllocationExtend() throws Exception
    {
        assertNotNull( settopServiceEndpoint );
        assertNotNull( settopService );

        logger.info( ">> GET SETTOP" );
        logger.info( "Mac address [" + macId + "] Auth Token [" + authToken + "]" );
        settopToken = settopService.getSettop( macId, authToken );
        logger.info( "SettopToken received" );
        logger.info( "Allocation id [" + settopToken.getAllocationId() + "]" );
        logger.info( "AuthToken [" + settopToken.getAuthToken() + "]" );
        logger.info( "Settop id [" + settopToken.getSettopId() + "]" );

        logger.info( "----------------------------------------------------------------" );

        logger.info( ">> GET SETTOP INFO" );
        SettopDesc settopDesc = settopService.getSettopInfo( settopToken );
        logger.info( "SettopDesc received" );
        logger.info( "SettopDesc [" + settopDesc.toString() + "]" );

        // Call this until u get an error or you are good with the test.
        int count = 0;
        while ( true )
        {
            logger.info( "----------------------------------------------------------------" );

            logger.info( ">> PRESS KRY - INFO " + count++ );

            settopServiceReturnMessage = settopService.pressKey( settopToken, INFO );
            logger.info( settopServiceReturnMessage );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );
        }
    }

}
