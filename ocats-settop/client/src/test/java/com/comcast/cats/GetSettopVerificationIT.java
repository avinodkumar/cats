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

import com.comcast.cats.service.SettopService;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * <pre>
 * Integration test to test allocation verification in both getsettop() and
 * presskey()
 * 
 * We need to set the below system properties to a minimun to test this.
 * 
 * </pre>
 * 
 * <br /><br />
 * <b>Steps</b>
 * <hr />
 * <ol>
 * <li>Execute this test case.</li>
 * <li>Break the allocation</li>
 * <li>The script should continue without failing</li>
 * <ol>
 * <br />
 * @author SSugun00c
 * 
 */
public class GetSettopVerificationIT extends TestCase
{
    private SettopServiceEndpoint      settopServiceEndpoint;
    private SettopService              settopService;
    private String                     serverBase        = "http://192.168.160.201:8080/";
    private String                     macId             = "12:BE:01:4F:14:82";
    private String                     authToken         = "183bb0fa-d50d-11e0-a350-005056b400d2";
    private SettopServiceReturnMessage settopServiceReturnMessage;
    private SettopToken                settopToken;
    private static final long          KEYPRESS_INTERVAL = 2000;

    private static Logger              logger            = Logger.getLogger( GetSettopVerificationIT.class );

    @Before
    protected void setUp() throws MalformedURLException
    {
        settopServiceEndpoint = new SettopServiceEndpoint( new URL( serverBase
                + SettopConstants.SETTOP_SERVICE_WSDL_LOCATION ) );
        settopService = settopServiceEndpoint.getPort();
    }

    @Test
    public void testGetSettopPressKey() throws Exception
    {
        assertNotNull( settopServiceEndpoint );
        assertNotNull( settopService );

        int count = 0;
        while ( true )
        {
            logger.info( ">> GET SETTOP" );
            logger.info( "Mac address [" + macId + "] Auth Token [" + authToken + "]" );
            settopToken = settopService.getSettop( macId, authToken );
            logger.info( "SUCCESS Allocation id [" + settopToken.getAllocationId() + "]" );

            logger.info( ">> PRESS KEY - INFO " + count++ );
            settopServiceReturnMessage = settopService.pressKey( settopToken, INFO );
            logger.info( settopServiceReturnMessage );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            logger.info( "----------------------------------------------------------------" );

            Thread.sleep( KEYPRESS_INTERVAL );
        }
    }
}
