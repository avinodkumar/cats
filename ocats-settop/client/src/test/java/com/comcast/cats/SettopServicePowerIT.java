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

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * SettopService integration test for Power service. All tests will be disabled
 * in CI. Enable in local development environment.
 * 
 * @author subinsugunan
 * 
 */
public class SettopServicePowerIT extends SettopServiceBaseIT
{
    private static Logger       logger            = Logger.getLogger( SettopServicePowerIT.class );

    private static final String POWER_STATE_ON    = "ON";
    private static final String POWER_STATE_OFF   = "OFF";
    private static final long   KEYPRESS_INTERVAL = 45000;

    @Test
    public void testPower() throws Exception
    {
        logger.info( ">> GET SETTOP" );
        logger.info( "Mac address [" + macId + "] Auth Token [" + authToken + "]" );
        SettopToken settopToken = settopService.getSettop( macId, authToken );
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

        String status = settopService.getPowerState( settopToken ).getStatus();

        logger.info( "Settop is in [" + status + "] state" );

        if ( POWER_STATE_OFF.equalsIgnoreCase( status ) )
        {
            logger.info( ">> POWER ON" );
            settopServiceReturnMessage = settopService.hardPowerOn( settopToken );
            logger.info( settopServiceReturnMessage.getResult() );
            Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
            Thread.sleep( KEYPRESS_INTERVAL );
        }

        Assert.assertEquals( POWER_STATE_ON, settopService.getPowerState( settopToken ).getStatus() );

        logger.info( ">> POWER OFF" );
        settopServiceReturnMessage = settopService.hardPowerOff( settopToken );
        logger.info( settopServiceReturnMessage.getResult() );
        Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
        Assert.assertEquals( POWER_STATE_OFF, settopService.getPowerState( settopToken ).getStatus() );
        Thread.sleep( KEYPRESS_INTERVAL );

        logger.info( ">> POWER ON" );
        settopServiceReturnMessage = settopService.hardPowerOn( settopToken );
        logger.info( settopServiceReturnMessage.getResult() );
        Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
        Assert.assertEquals( POWER_STATE_ON, settopService.getPowerState( settopToken ).getStatus() );
        Thread.sleep( KEYPRESS_INTERVAL );

        logger.info( ">> POWER TOGGLE" );
        settopServiceReturnMessage = settopService.hardPowerToggle( settopToken );
        logger.info( settopServiceReturnMessage.getResult() );
        Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertEquals( POWER_STATE_ON, settopService.getPowerState( settopToken ).getStatus() );

        logger.info( "----------------------------------------------------------------" );

        logger.info( ">> RELEASE SETTOP" );
        settopServiceReturnMessage = settopService.releaseSettop( settopToken );
        logger.info( settopServiceReturnMessage.getResult() );
        Assert.assertEquals( WebServiceReturnEnum.SUCCESS, settopServiceReturnMessage.getResult() );

    }
}
