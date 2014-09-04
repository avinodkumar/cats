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


public class NetBooter_NP_16S_PowerDevice extends NetBooter_PowerDevice {
    private PowerDeviceConnectionNP16S client;

    /**
     * Small delay used between connection and sending a command to the hardware.
     */
    public static final int PAUSE_BETWEEN_CMD = 1000;

    /**
     * The return code of OK.
     */
    public static final String OK = "$A0";

    /**
     * The power command used for outlet state control.
     */
    public static final String PWRCMD = "$A3";

    /**
     * The reboot command used for outlet toggling.
     */
    public static final String REBOOT = "$A4";

    /**
     * The command to display outlet status.
     */
    public static final String OUTLETSTATUS = "$A5";

    /**
     * The ON state for the outlet.
     */
    public static final String ONSTATE = "1";

    /**
     * The OFF state for the outlet.
     */
    public static final String OFFSTATE = "0";

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_WAIT = 500;

    private static final Logger log = LoggerFactory.getLogger(NetBooter_NP_16S_PowerDevice.class);

    public NetBooter_NP_16S_PowerDevice() {
        
    }

    //This constructor is used in testing only. 
    public NetBooter_NP_16S_PowerDevice(String ip, int port, int numOutlets) {
        this.ip = ip;
        this.port = port;
        this.numOutlets = numOutlets;
    }
    
    /**
     * Method to initialize the power device connection.
     */
    @Override
    public void createPowerDevConn() {
        log.debug("Creating the connection to the NP-16S power device.");
        client = null;
        client = new PowerDeviceConnectionNP16S(getIp(), getPort());
        client.setInitialCR(true);
    }

    /**
     * Setter method for the device IP.
     *
     * @param ip
     *           The ip string representation for the device.
     */
    @Override
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
    @Override
    public void setPort(int port) {
        super.setPort(port);

        if (null != client && client.isConnected()) {
            client.close();
        }

        createPowerDevConn();
    }

    @Override
    public void destroy() {
        client.close();
    }

    /**
     * The main power command used for changing the outlet power state.
     *
     * @param cmd
     *           The command to perform (ON, OFF, or BOOT).
     * @param outlet
     *           The outlet number.
     * @return true if the power state was changed successfully, false otherwise.
     */
    public boolean power(String cmd, final int outlet) {
        assert (null != cmd);
        cmd = cmd.trim();

        if (outlet < 0 || outlet > getNumOutlets()) {
            throw new java.lang.IllegalArgumentException("Invalid outlet number: "
                    + outlet);
        }

        String np_command;
       
        if (cmd.equalsIgnoreCase(OFF)) {
            np_command = PWRCMD + " " + outlet + " " + OFFSTATE;
        }
        else if (cmd.equalsIgnoreCase(ON)) {
           np_command = PWRCMD + " " + outlet + " " + ONSTATE;
     	}
        else if (cmd.equalsIgnoreCase(BOOT)) {
            np_command = REBOOT + " " + outlet;
      	}
        else {
            throw new java.lang.IllegalArgumentException(
                    "Invalid command argument: " + cmd);
        }

        log.debug("Sending command: [" + np_command + "]");
              
        if (client.connect(CONNECT_TIMEOUT)) {
        	sleep(PAUSE_BETWEEN_CMD);
            String response = client.read(READ_WAIT);
            client.sendCmd(np_command, true);
            sleep(PAUSE_BETWEEN_CMD);
            response = client.waitForString(OK, READ_WAIT);
            client.close();

            if (response.indexOf(OK) != -1) {
            	updateStatistics(outlet, cmd, true);          	
                return true;
            }
        }
        updateStatistics(outlet, cmd, false);
        return false;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getOutletStatus(int outlet) {
        String status = "UNKNOWN";
        String addr = client.getInetAddress().toString();

        if (client.connect(CONNECT_TIMEOUT)) {
        	sleep(PAUSE_BETWEEN_CMD);
            String response = client.read(READ_WAIT);
			if (!client.sendCmd(OUTLETSTATUS, true)) {
				log.warn("Status request failed." + addr);
				client.close();
				return status;
			}
            sleep(PAUSE_BETWEEN_CMD);
            response = client.read(READ_WAIT);
            client.close();
            log.debug("Power device respones from " + addr + ": [" + response + "]");

            if (-1 == response.indexOf(OK)) {
            	log.error("OK not found: " + addr + ": [" + response + "]");
                return status;
            }

            int firstComma = response.indexOf(",");

            if (-1 == firstComma) {
            	log.error("First comma not found: " + addr + ": [" + response + "]");
                return status;
            }

            int secondComma = response.indexOf(",", firstComma + 1);

            if (-1 == secondComma) {
            	log.error("Second comma not found: " + addr + ": [" + response + "]");
                return status;
            }

            String outletStatus = response.substring(firstComma + 1, secondComma);            
            byte[] outletStatusArray = outletStatus.getBytes();
            // The original status String was in reverse order (outlet 1 on the
            // right).  

            char st = (char) outletStatusArray[outletStatusArray.length - outlet];
            if ('1' == st) {
                return ON;
            }
            else if ('0' == st) {
                return OFF;
            }
            else {
            	log.error("Unknown status: " + addr + ": [" + response + "] status: " + st);
                return status;
            }
        }
        else {

			log.error("Could not connect to client. " + addr);

		}

        return status;
    }
}
