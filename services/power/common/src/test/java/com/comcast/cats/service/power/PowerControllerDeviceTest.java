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

import java.util.HashMap;
import junit.framework.Assert;

import org.testng.annotations.Test;
import com.comcast.cats.service.PowerStatistics;

/**
 * The Class PowerControllerDeviceTest.
 * 
 * @Author : Aneesh
 * @since : 20th Sept 2012
 * Description : The Class PowerControllerDeviceTest is the unit test of {@link PowerControllerDevice}.
 */

public class PowerControllerDeviceTest extends PowerControllerDevice
{

    /* (non-Javadoc)
     * @see com.comcast.cats.service.power.PowerControllerDevice#createPowerDevConn()
     */
    @Override
    public void createPowerDevConn()
    {
        
    }

    /* (non-Javadoc)
     * @see com.comcast.cats.service.power.PowerControllerDevice#destroy()
     */
    @Override
    public void destroy()
    {
        
    }

    /* (non-Javadoc)
     * @see com.comcast.cats.service.power.PowerControllerDevice#getOutletStatus(int)
     */
    @Override
    public String getOutletStatus( int outlet )
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.comcast.cats.service.power.PowerControllerDevice#powerOff(int)
     */
    @Override
    public boolean powerOff( int outlet )
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.comcast.cats.service.power.PowerControllerDevice#powerOn(int)
     */
    @Override
    public boolean powerOn( int outlet )
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.comcast.cats.service.power.PowerControllerDevice#powerToggle(int)
     */
    @Override
    public boolean powerToggle( int outlet )
    {
        return false;
    }
    
    /**
     * Test getters setters.
     */
    @Test
    public void testGettersSetters() {
        PowerControllerDeviceTest pwerDevice = new PowerControllerDeviceTest();
        String ip = "1.1.1.1";
        String mdl = "MOTOROLA";
        String name = "BOX1";
        int num = 5;
        int port = 5000;
        String state = "ON";
        pwerDevice.setIp( ip );
        pwerDevice.setModel( mdl );
        pwerDevice.setName( name );
        pwerDevice.setNumOutlets( num );
        pwerDevice.setPort( port );
        pwerDevice.setState( state );
        Assert.assertEquals( pwerDevice.getIp(), ip );
        Assert.assertEquals( pwerDevice.getModel(), mdl);
        Assert.assertEquals( pwerDevice.getName(), name);
        Assert.assertEquals( pwerDevice.getNumOutlets(), num);
        Assert.assertEquals( pwerDevice.getState(), state);
        Assert.assertEquals( pwerDevice.getPort(), port );
    }
}
