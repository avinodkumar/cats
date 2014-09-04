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

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class PowerServiceWSImplTest.
 * 
 * @Author : Aneesh
 * @since : 4th Oct 2012
 * Description : The Class PowerServiceWSImplTest is the unit test of {@link PowerServiceWSImpl}.
 */

public class PowerServiceWSImplTest
{
    
    /** The pwr service impl. */
    PowerServiceWSImpl pwrServiceImpl;
    
    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        pwrServiceImpl = new PowerServiceWSImpl();
    }
    
    /**
     * Test create device.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCreateDevice() throws Exception {
        PowerControllerDeviceFactory powerControllerFactory = EasyMock.createMock( PowerControllerDeviceFactory.class );
        PowerControllerDevice pwrCtlDevice = EasyMock.createMock( PowerControllerDevice.class );
        URI uri = new URI("");
        Whitebox.setInternalState( pwrServiceImpl, "powerControllerFactory", powerControllerFactory );
        EasyMock.expect( powerControllerFactory.createPowerControllerDevice(uri)).andReturn( pwrCtlDevice );
        EasyMock.replay( powerControllerFactory );
        Assert.assertEquals( pwrServiceImpl.createDevice( uri ), pwrCtlDevice);
        EasyMock.verify( powerControllerFactory );
    }
    
    /**
     * Test create device power controller device factory null.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCreateDevicePowerControllerDeviceFactoryNull() throws Exception {
        PowerControllerDeviceFactory powerControllerFactory = null;
        Whitebox.setInternalState( pwrServiceImpl, "powerControllerFactory", powerControllerFactory );
        URI uri = new URI("");
        Assert.assertEquals( pwrServiceImpl.createDevice( uri ), null);
    }
}
