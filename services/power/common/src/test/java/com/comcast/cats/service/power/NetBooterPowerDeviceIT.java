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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Integration test for the NetBooter power devices.
 * 
 * @author aingog000
 *
 */
public class NetBooterPowerDeviceIT {
    // Set these as appropriate (typically only IP and OUTLET change).
    private final String                   IP = "192.168.120.102";
    private final int                      OUTLET = 8;
    private final int                      PORT = 23;
    private final int                      NUM_OUTLETS = 16;

    // Select one of the next two lines depending on the type of NetBooter.
    // private NetBooter_NP_1601D_PowerDevice pwr;
    private NetBooter_NP_16S_PowerDevice   pwr;

    private static Logger logger = LoggerFactory.getLogger(NetBooterPowerDeviceIT.class);

    /**
     * Test the power-off, power-on, and "get outlet status" commands of NP-16
     * and NP-16S NetBooter power devices.
     */
    //@Test(timeOut = 60000)
    public void testCommandOffThenOn() {
        // Select one of the next two lines.
        // pwr = new NetBooter_NP_1601D_PowerDevice(IP, PORT, NUM_OUTLETS);
        pwr = new NetBooter_NP_16S_PowerDevice(IP, PORT, NUM_OUTLETS);

        pwr.createPowerDevConn();

        String outletStatus = pwr.getOutletStatus(OUTLET);
        logger.info("Outlet status: [" + outletStatus + "]");
        Assert.assertEquals(PowerControllerDevice.ON, outletStatus);

        Assert.assertTrue(pwr.powerOff(OUTLET));

        outletStatus = pwr.getOutletStatus(OUTLET);
        logger.info("Outlet status after power-off: [" + outletStatus + "]");
        Assert.assertEquals(PowerControllerDevice.OFF, outletStatus);

        // Wait a few seconds before powering the box back on.
        Assert.assertTrue(sleepMe(5000));
        Assert.assertTrue(pwr.powerOn(OUTLET));

        outletStatus = pwr.getOutletStatus(OUTLET);
        logger.info("Outlet status after power-on: [" + outletStatus + "]");
        Assert.assertEquals(PowerControllerDevice.ON, outletStatus);
    }

    /**
     * Test the boot and "get outlet status" commands of NP-16 and NP-16S
     * NetBooter power devices.
     */
    //@Test(timeOut = 60000)
    public void testCommandToggle() {
        // Select one of the next two lines.
        // pwr = new NetBooter_NP_1601D_PowerDevice(IP, PORT, NUM_OUTLETS);
        pwr = new NetBooter_NP_16S_PowerDevice(IP, PORT, NUM_OUTLETS);

        pwr.createPowerDevConn();

        String outletStatus = pwr.getOutletStatus(OUTLET);
        logger.info("Outlet status: [" + outletStatus + "]");
        Assert.assertEquals(PowerControllerDevice.ON, outletStatus );

        Assert.assertTrue(pwr.powerToggle(OUTLET));

        outletStatus = pwr.getOutletStatus(OUTLET);
        logger.info("Outlet status immediately after toggle: [" + outletStatus + "]");
        Assert.assertTrue(PowerControllerDevice.OFF.equals(outletStatus));
        // After a short delay the box should be back on.
        Assert.assertTrue(sleepMe(20000));
        outletStatus = pwr.getOutletStatus(OUTLET);
        logger.info("Outlet status after a short delay: [" + outletStatus + "]");
        Assert.assertTrue(PowerControllerDevice.ON.equals(outletStatus));
    }

    private boolean sleepMe(long time) {
        try {
            Thread.sleep(time);
            return true;
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted.");
            return false;
        }
    }
}
