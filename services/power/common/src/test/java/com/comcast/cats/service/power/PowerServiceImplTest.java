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

import java.lang.reflect.Method;
import java.net.URI;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class PowerServiceImplTest is the unit test of {@link PowerServiceImpl}.
 * @Author : Aneesh
 * @since : 26th Sept 2012
 */
public class PowerServiceImplTest {
    
    /** The pwr service impl. */
    PowerServiceImpl pwrServiceImpl;

    /**
     * Make the test setup.
     */
    @BeforeMethod
    public void setUp() {
        pwrServiceImpl = new PowerServiceImpl();
    }

    /**
     * Test power locator.
     */
    @Test
    public void testPowerLocator () {
        //TBD : The method pwrServiceImpl.powerLocator() just returns a null string. Not sure the developer's intention behind this.
        //For now , just assert the return value of the original method.
        Assert.assertTrue( pwrServiceImpl.powerLocator() == null );
    }

    //testPowerStatus() method should not get called using an instance of PowerServiceImpl.java. A call of this should throw an exception.
    //This suppose to be called from a sub class

    /**
     * Test power status.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testPowerStatus() throws Exception {
        URI uri = new URI("");
        pwrServiceImpl.powerStatus( uri );
    }

    //hardHardPowerOff() method should not get called using an instance of PowerServiceImpl.java. A call of this should throw an exception.
    //This suppose to be called from a sub class

    /**
     * Test hard power off.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testHardPowerOff() throws Exception {
        URI uri = new URI("");
        pwrServiceImpl.hardPowerOff( uri );
    }

    //testHardPowerOn() method should not get called using an instance of PowerServiceImpl.java. A call of this should throw an exception.
    //This suppose to be called from a sub class

    /**
     * Test hard power on.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testHardPowerOn() throws Exception {
        URI uri = new URI("");
        pwrServiceImpl.hardPowerOn(  uri );
    }

    //testHardPowerToggle() method should not get called using an instance of PowerServiceImpl.java. A call of this should throw an exception.
    //This suppose to be called from a sub class

    /**
     * Test hard power toggle.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testHardPowerToggle() throws Exception {
        URI uri = new URI("");
        pwrServiceImpl.hardPowerToggle( uri );
    }

    /**
     * Test parse outlet.
     *
     * @throws Exception the exception
     */
    @Test
    public void testParseOutlet() throws Exception {
        String ip = "1.1.1.";
        int port = 23;
        String outLet ="/?connectionport=";
        int numOfOutlets = 16;
        URI path = new URI( "nps1600://" + ip + ":" + port + outLet +  numOfOutlets );
        Method method = Whitebox.getMethod( PowerServiceImpl.class, "parseOutlet", URI.class );
        Object ret = method.invoke( pwrServiceImpl, path );
        int returned = new Integer( ret.toString() );
        Assert.assertEquals( numOfOutlets, returned );
    }

    /**
     * Test parse outlet when outlet number not specied.
     *
     * @throws Exception the exception
     */
    @Test
    public void testParseOutletWhenOutletNumberNotSpecied() throws Exception {
        String ip = "1.1.1.";
        int port = 23;
        String outLet ="/?connectionport=";
        URI path = new URI( "nps1600://" + ip + ":" + port + outLet);
        String message = "The power outlet must be specified";
        Method method = Whitebox.getMethod( PowerServiceImpl.class, "parseOutlet", URI.class );
        try {
            method.invoke( pwrServiceImpl, path );
        } catch(Exception exp ) {
            IllegalArgumentException exception = (IllegalArgumentException)exp.getCause();
            Assert.assertTrue( exception.getMessage().equals( message ) );
        }
    }
}
