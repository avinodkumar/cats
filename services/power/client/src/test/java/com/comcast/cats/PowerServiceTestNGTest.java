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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceEndpoint;

/**
 * Stress test for powerService
 * 
 * @author bemman01c
 * 
 */
public class PowerServiceTestNGTest {

    private static Logger       logger            = LoggerFactory.getLogger( PowerServiceTestNGTest.class );

    private static final String endPointStr       = "http://192.168.120.102:8080/power-service/PowerService?wsdl";
    private static final String POWER_STATE_ON    = "ON";
    private static final String POWER_STATE_OFF   = "OFF";
    private static final String POWER_STATE_UNKNOWN  = "UNKNOWN";
    private static String       SETTOP_POWER      = "wti1600://192.168.120.102:23/?outlet=";

    PowerService powerServiceEndpoint = null;

    /**
	 * Default constructor of the TestNG test.
	 * 
	 * @throws URISyntaxException
	 */
    
    public PowerServiceTestNGTest()   
    {
    }
    
    @BeforeTest
    public void setup() throws URISyntaxException, MalformedURLException {
    	PowerServiceEndpoint endpoint = new PowerServiceEndpoint( new URL( endPointStr ) );
	    powerServiceEndpoint = endpoint.getPowerServiceImplPort();
    }
          
	@DataProvider(name = "outletData", parallel=true)
	public Object[][] dpOutletData() {
	 return new Object[][] {
			 {new Integer(1)},
			 {new Integer(3)},
			 {new Integer(6)}
	 };
	}

	@Test(dataProvider = "outletData", invocationCount = 5)
	public void testPowerService(Integer outletNum) throws URISyntaxException {
		URI path = new URI( SETTOP_POWER+outletNum.intValue());
		logger.debug("Going to execute OFF command on " + path);
		Assert.assertNotNull(powerServiceEndpoint, "powerServiceEndpoint is null");
    	String powerStatus = powerServiceEndpoint.powerStatus( path );
    	Assert.assertNotSame(powerStatus, POWER_STATE_UNKNOWN, "Power status returned invalid status");
    	if(POWER_STATE_OFF.equals(powerStatus)){
    	    boolean temp = powerServiceEndpoint.hardPowerOn( path ) ;
    		Assert.assertTrue(temp, "Hard power on failed");
    		powerStatus = powerServiceEndpoint.powerStatus( path );
        	Assert.assertEquals( powerStatus, POWER_STATE_ON, "Power ON status check failed"); 
    	}
    	boolean temp = powerServiceEndpoint.hardPowerOff( path ) ;
    	Assert.assertTrue(temp, "Power off returned false" );
    	powerStatus = powerServiceEndpoint.powerStatus( path );
    	Assert.assertEquals( powerStatus, POWER_STATE_OFF, "Power OFF status check failed ");
    }
}
