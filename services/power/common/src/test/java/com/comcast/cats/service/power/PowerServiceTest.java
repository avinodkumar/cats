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
package com.comcast.cats.service.power;

import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.MarkerFactory;
import org.slf4j.LoggerFactory;
import junit.framework.Assert;
import org.junit.Test;

import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.power.PowerServiceImpl;

/**
 * Power tests
 * 
 * Note:
 * These tests can be brittle and brake if the hardware pointed to by the
 * URL/URI goes away or changes.
 *  
 * @author jtyrre001
 */
public class PowerServiceTest
{
	static public final String WTI_TEST="wti1600://192.168.120.102:23/?outlet=3";
    /**
     * Power on the outlet. 
     */
    @Test
    public void testPowerOn1()
    {
        try {
            PowerService ps = new PowerServiceImpl();
            boolean pOn = ps.hardPowerOn(new URI(WTI_TEST));
            Assert.assertTrue("Power outlet ON status passed", pOn);
        } catch (URISyntaxException ex) {
            LoggerFactory.getLogger(PowerServiceTest.class.getName()).error( MarkerFactory.getMarker("SEVERE"), "{}", ex );
        }
    }
   
    /**
     * Get the state of the power outlet which should now be on.
     */
    @Test
    public void testPowerStatusOn()
    {
        try {
            PowerService ps = new PowerServiceImpl();
            String state = ps.powerStatus(new URI(WTI_TEST));
            Assert.assertTrue("Get Power ON status passed", state.equals("ON"));
        } catch (URISyntaxException ex) {
        	LoggerFactory.getLogger(PowerServiceTest.class.getName()).error( MarkerFactory.getMarker("SEVERE"),"{}", ex );
        }
    }
    
    /**
     * Power off the outlet. 
     */
    @Test
    public void testPowerOff()
    {
        try {
            PowerService ps = new PowerServiceImpl();
            boolean pOn = ps.hardPowerOff(new URI(WTI_TEST));
            Assert.assertTrue("Power outlet OFF status passed", pOn);
        } catch (URISyntaxException ex) {
        	LoggerFactory.getLogger(PowerServiceTest.class.getName()).error( MarkerFactory.getMarker("SEVERE"), "{}", ex );
        }
    }

    /**
     * Get the state of the power outlet which should now be off.
     */
    @Test
    public void testPowerStatusOff()
    {
        try {
            PowerService ps = new PowerServiceImpl();
            String state = ps.powerStatus(new URI(WTI_TEST));
            Assert.assertTrue("Get Power OFF status passed", state.equals("OFF"));
        } catch (URISyntaxException ex) {
        	LoggerFactory.getLogger(PowerServiceTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
        }
    }
    
    /**
     * Power on the outlet. 
     */
    @Test
    public void testPowerOn2()
    {
        try {
            PowerService ps = new PowerServiceImpl();
            boolean pOn = ps.hardPowerOn(new URI(WTI_TEST));
            Assert.assertTrue("Power outlet ON status passed", pOn);
        } catch (URISyntaxException ex) {
        	LoggerFactory.getLogger(PowerServiceTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
        }
    }
    
    /**
     * Toggle the outlet off then on.
     */
    @Test
    public void testPowerToggle()
    {
        try {
            PowerService ps = new PowerServiceImpl();
            boolean tStatus = ps.hardPowerToggle(new URI(WTI_TEST));
            Assert.assertTrue("Power Toggle OFF then ON passed", tStatus);
        } catch (URISyntaxException ex) {
        	LoggerFactory.getLogger(PowerServiceTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
        }
    }
}
