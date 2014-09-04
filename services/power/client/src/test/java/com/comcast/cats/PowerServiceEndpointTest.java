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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceEndpoint;

/**
 * 
 * @author bemman01c
 * 
 */
public class PowerServiceEndpointTest
{
    private static Logger       logger            = LoggerFactory.getLogger( PowerServiceEndpointTest.class );
    // CATS development server
    private static final String endPointStr       = "http://192.168.120.102:8080/power-service/PowerService?wsdl";
    private static final String POWER_STATE_ON    = "ON";
    private static final String POWER_STATE_OFF   = "OFF";
    private static final String POWER_STATE_UNKNOWN  = "UNKNOWN";
    private static final long   KEYPRESS_INTERVAL = 45000;
    private static URI          path              = null;
    private PowerService        powerService      = null;
    private static String       SETTOP_POWER      = "nps1600://192.168.120.102:161/?outlet=5";

    public PowerServiceEndpointTest() throws URISyntaxException
    {
        path = new URI( SETTOP_POWER );
    }

    @Before
    public void instantiateEndpoint() throws MalformedURLException
    {
        PowerServiceEndpoint endpoint = new PowerServiceEndpoint( new URL( endPointStr ) );
        powerService = endpoint.getPowerServiceImplPort();

    }
    @Test
    public void testPowerOn()throws InterruptedException
    {
    	Assert.assertNotNull( powerService );
    	
    	logger.debug("TestPowerOn: Got powerService");
    	
    	String powerStatus=powerService.powerStatus( path );
    	
    	logger.debug("Current power status :"+powerStatus);
    	
    	Assert.assertFalse("Current power status :"+powerStatus,POWER_STATE_UNKNOWN.equals(powerStatus) );
    	
    	if(POWER_STATE_ON.equals(powerStatus)){
    	    
    		logger.debug("Currnet Status is ON so switching off");
    		
    		boolean status=powerService.hardPowerOff( path ) ;
    		
    		logger.debug("power off returned "+status);
    		
    		Assert.assertTrue(status);
    		
    		powerStatus=powerService.powerStatus( path );
    		
    		logger.debug("power status after power off :"+powerStatus);
    		
        	Assert.assertEquals(POWER_STATE_OFF, powerStatus);
    		
    	}
    	boolean status=powerService.hardPowerOn( path ) ;
    	
    	logger.debug("Power On returned :"+status);
    	
    	Assert.assertTrue( status );
    	
    	powerStatus=powerService.powerStatus( path );
    	
    	logger.debug("power status after power on :"+powerStatus);
    	
    	Assert.assertEquals(POWER_STATE_ON, powerStatus);
    }
    
    @Test
    public void testPowerOff()throws InterruptedException
    {
    	Assert.assertNotNull( powerService );
    	
    	logger.debug("TestPowerOFF: Got powerService");
    	
    	String powerStatus=powerService.powerStatus( path );
    	
    	logger.debug("Current power status :"+powerStatus);
    	
    	Assert.assertFalse("Current power status :"+powerStatus,POWER_STATE_UNKNOWN.equals(powerStatus) );
    	
    	if(POWER_STATE_OFF.equals(powerStatus)){
    	    
    		logger.debug("Currnet Status is OFF so switching ON");
    		
    		boolean temp=powerService.hardPowerOn( path ) ;
    		
    		logger.debug("power on returned "+temp);
    		
    		Assert.assertTrue(temp);
    		
    		powerStatus=powerService.powerStatus( path );
    		
    		logger.debug("power status after power on :"+powerStatus);
    		
        	Assert.assertEquals(POWER_STATE_ON, powerStatus);
    		
    	}
    	boolean temp=powerService.hardPowerOff( path ) ;
    	
    	logger.debug("Power off returned :"+temp);
    	
    	Assert.assertTrue( temp );
    	
    	powerStatus=powerService.powerStatus( path );
    	
    	logger.debug("power status after power off :"+powerStatus);
    	
    	Assert.assertEquals(POWER_STATE_OFF, powerStatus);
    }

    @Test
    public void testPowerToggle()throws InterruptedException
    {
    	Assert.assertNotNull( powerService );
    	
    	logger.debug("TestPowerToggle: Got powerService");
    	
    	String powerStatus=powerService.powerStatus( path );
    	
    	logger.debug("Current power status :"+powerStatus);
    	
    	Assert.assertFalse("Current power status :"+powerStatus,POWER_STATE_UNKNOWN.equals(powerStatus) );
    	
    	if(POWER_STATE_OFF.equals(powerStatus)){
    	    
    		logger.debug("Currnet Status is oFF");
    		
    		boolean temp=powerService.hardPowerToggle( path ) ;
    		
    		logger.debug("power toggle returned "+temp);
    		
    		Assert.assertTrue(temp);
    		
    		powerStatus=powerService.powerStatus( path );
    		
    		logger.debug("power status after power toggle :"+powerStatus);
    		
        	Assert.assertEquals(POWER_STATE_ON, powerStatus);
    		
    	}else if(POWER_STATE_ON.equals(powerStatus)){
    	    
    		logger.debug("Currnet Status is ON");
    		
    		boolean temp=powerService.hardPowerToggle( path ) ;
    		
    		logger.debug("power toggle returned "+temp);
    		
    		Assert.assertTrue(temp);
    		
    		powerStatus=powerService.powerStatus( path );
    		
    		logger.debug("power status after power toggle :"+powerStatus);
    		
        	Assert.assertEquals(POWER_STATE_ON, powerStatus);
    	}
    }

    @Test
    @Ignore
    public void testCommands() throws InterruptedException
    {

        Assert.assertNotNull( powerService );

        if ( POWER_STATE_OFF.equals( powerService.powerStatus( path ) ) )
        {
            logger.debug( ">> POWER ON" );
            boolean temp=powerService.hardPowerOn( path ) ;
            logger.debug("hard power on"+temp);
            Assert.assertTrue( temp );
            Thread.sleep( KEYPRESS_INTERVAL );
        }
        logger.debug("power off ");
        String temp=powerService.powerStatus( path );
        logger.debug("Power status"+temp);
        Assert.assertEquals( POWER_STATE_ON, powerService.powerStatus( path ) );

        logger.debug( ">> POWER OFF" );
        boolean temp2=powerService.hardPowerOff( path ) ;
        logger.debug("power off"+temp2);
        Assert.assertTrue(temp2);
        temp=powerService.powerStatus( path );
        logger.debug("power off status"+temp);
        Assert.assertEquals( POWER_STATE_OFF, temp );
        Thread.sleep( KEYPRESS_INTERVAL );

        logger.info( ">> POWER ON" );
        Assert.assertTrue( powerService.hardPowerOn( path ) );
        Assert.assertEquals( POWER_STATE_ON, powerService.powerStatus( path ) );
        Thread.sleep( KEYPRESS_INTERVAL );

        logger.info( ">> POWER TOGGLE" );
        Assert.assertTrue( powerService.hardPowerToggle( path ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertEquals( POWER_STATE_ON, powerService.powerStatus( path ) );
    }
}
