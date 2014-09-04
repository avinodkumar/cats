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

import static com.comcast.cats.RemoteCommand.DOWN;
import static com.comcast.cats.RemoteCommand.EXIT;
import static com.comcast.cats.RemoteCommand.GUIDE;
import static com.comcast.cats.RemoteCommand.UP;
import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * SettopService integration test for IR service .All tests will be disabled in
 * CI. Enable in local development environment.
 * 
 * @author subinsugunan
 * 
 */
public class SettopServiceIrIT extends SettopServiceBaseIT
{
    private static Logger     logger            = Logger.getLogger( SettopServiceIrIT.class );

    private SettopToken       settopToken;
    private static final long KEYPRESS_INTERVAL = 2000;

    @Test
    public void testIr() throws Exception
    {
        try
        {
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

            logger.info( "----------------------------------------------------------------" );

            logger.info( ">> PRESS KEY - GUIDE" );
            settopServiceReturnMessage = settopService.pressKey( settopToken, GUIDE );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );

            logger.info( ">> PRESS KEY - DOWN" );
            settopServiceReturnMessage = settopService.pressKey( settopToken, DOWN );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );

            logger.info( ">> PRESS KEY - DOWN" );
            settopServiceReturnMessage = settopService.pressKey( settopToken, DOWN );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );

            logger.info( ">> PRESS KEY - UP" );
            settopServiceReturnMessage = settopService.pressKey( settopToken, UP );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );

            logger.info( ">> PRESS KEY - EXIT" );
            settopServiceReturnMessage = settopService.pressKey( settopToken, EXIT );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );

            logger.info( ">> TUNE" );
            String channel = "11";
            settopServiceReturnMessage = settopService.tune( settopToken, channel, false );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );
        }
        finally
        {
            if ( null != settopToken )
            {
                logger.info( "----------------------------------------------------------------" );

                logger.info( ">> RELEASE SETTOP" );
                settopServiceReturnMessage = settopService.releaseSettop( settopToken );
                logger.info( settopServiceReturnMessage.getResult() );
                Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            }
        }

    }

    @Test
    public void testPressKeySequence() throws Exception
    {
        try
        {
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

            logger.info( "----------------------------------------------------------------" );

            logger.info( ">> PRESS KEY - GUIDE" );
            String keySeq = "EXIT,DOWN,DOWN,2";
            String repeatCount = "30,0,0,0";
            String delay = "1000,1000,1000,1000";
            settopServiceReturnMessage = settopService.pressKeySequence( settopToken, keySeq, repeatCount, delay );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );

        }
        finally
        {
            if ( null != settopToken )
            {
                logger.info( "----------------------------------------------------------------" );

                logger.info( ">> RELEASE SETTOP" );
                settopServiceReturnMessage = settopService.releaseSettop( settopToken );
                logger.info( settopServiceReturnMessage.getResult() );
                Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            }
        }
    }
}
