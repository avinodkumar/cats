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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public abstract class NetBooter_PowerDevice extends PowerControllerDevice {
    PowerDeviceConnection client;

    private static final Logger log = LoggerFactory.getLogger(NetBooter_PowerDevice.class);

    /**
     * Method to initialize the power device connection.
     */
    public void createPowerDevConn() {
        log.debug("Creating the connection to the NP-16 power device.");
        client = null;
        client = new PowerDeviceConnection(getIp(), getPort());
        client.setInitialCR(true);
    }

    /**
     * Setter method for the device IP.
     *
     * @param ip
     *           The ip string representation for the device.
     */
    public void setIp(String ip) {
        super.setIp(ip);

        if (null != client && client.isConnected()) {
            client.close();
        }

        createPowerDevConn();
    }

    /**
     * Setter method for the device port.
     *
     * @param port
     *           The port number for the device.
     */
    public void setPort(int port) {
        super.setPort(port);

        if (null != client && client.isConnected()) {
            client.close();
        }

        createPowerDevConn();
    }

      public void destroy() {
          client.close();
      }

    /**
     * The command used to power toggle the outlet.
     *
     * @param outlet
     *           The outlet number to power toggle.
     * @return true if power toggling was successful, false otherwise.
     */
    public synchronized boolean powerToggle(int outlet) {
        return power(BOOT, outlet);
    }

    /**
     * The command used to power ON the outlet.
     *
     * @param outlet
     *           The outlet number to power ON.
     * @return true if powering ON of outlet was successful, false otherwise.
     */
    public synchronized boolean powerOn(final int outlet) {
        return power(ON, outlet);
    }

    /**
     * The command used to power OFF the outlet.
     *
     * @param outlet
     *           The outlet number to power OFF.
     * @return true if powering OFF of outlet was successful, false otherwise.
     */
    public synchronized boolean powerOff(final int outlet) {
        return power(OFF, outlet);
    }

    /**
     * The main power command used for changing the outlet power state.
     *
     * @param cmd
     *           The command to perform (ON, OFF, or BOOT).
     * @param outlet
     *           The outlet number to change power state for.
     * @return true if the power state was changed successfully, false otherwise.
     */
    public abstract boolean power(String cmd, final int outlet);

    @Override
    public abstract String getOutletStatus(int outlet);

    protected void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Thread was interrupted");
        }
    }
}
