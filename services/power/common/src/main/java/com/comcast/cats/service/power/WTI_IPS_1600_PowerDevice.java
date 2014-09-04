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

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerStatistics;


/**
 * A PowerControllerDevice that controls a WTI_IPS_1600 power bar.
 */
public class WTI_IPS_1600_PowerDevice extends PowerControllerDevice {

   PowerDeviceConnection client;

   /**
    * On message. Used to turn on the device.
    */
   public static final String ON = "/ON";
   
   /**
    * Off message. Used to turn off the device.
    */
   public static final String OFF = "/OFF";
   
   /**
    * Boot message. Used to power toggle the device.
    */
   public static final String BOOT = "/BOOT";
   
   /**
    * Prompt message.
    */
   public static final String PROMPT = "IPS";
   
   /**
    * Disconnect message.
    */
   public static final String DISCONNECT = "/X";
   
   /**
    * Plug status message.
    */
   public static final String PLUGSTATUS = "/S";

   private int responseTime = 5000;
   private static final int CONNECT_TIMEOUT = 5000;

   private final Logger log = LoggerFactory.getLogger(WTI_IPS_1600_PowerDevice.class);
   private PowerInfo powerInfo = null;
   public WTI_IPS_1600_PowerDevice() {
   }

   public WTI_IPS_1600_PowerDevice(String ip, int port, int numOutlets) {
       this.ip = ip;
       this.port = port;
       this.numOutlets = numOutlets;
   }
   
  /**
    * Creates a power device connection based on the set IP and Port.
    */
   @Override
   public void createPowerDevConn() {
      client = null;
      client = new PowerDeviceConnection(getIp(), getPort());
      client.setInitialCR(true);
   }
  
   /**
    * {@inheritDoc}
    */
   @Override
   public synchronized boolean powerOn(final int outlet) {
	   boolean ret = power(ON, outlet);
       updateStatistics(outlet, ON, ret);
       return ret;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public synchronized boolean powerOff(final int outlet) {
	   boolean ret = power(OFF, outlet);
	   updateStatistics(outlet, OFF , ret);
       return ret;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public synchronized boolean powerToggle(int outlet) {
      boolean ret = power(BOOT, outlet);
      System.out.println(" Inside BOOT in WTI_IPS_1600_Device");
      updateStatistics(outlet, BOOT, ret);
      return ret;
   }
   
   /**
    * Sends the specified command to the specified outlet.
    * A valid IP and Port must be set before using this command otherwise it will fail.
    * @param cmd The command. Must be /OFF, /ON, or /BOOT
    * @param outlet The outlet. Must be > 0 and <= the number of outlets.
    * @return <b>true</b> on success.
    */
   public boolean power(String cmd, final int outlet) {
      if (getState().equals(PowerControllerDevice.OFF)) {
         log.warn("This device is currently disabled.");
         return false;
      }

      cmd = cmd.trim();
      if (!cmd.equalsIgnoreCase(OFF) && !cmd.equalsIgnoreCase(ON)
            && !cmd.equalsIgnoreCase(BOOT)) {
         throw new java.lang.IllegalArgumentException(
               "Invalid command argument: " + cmd);
      }
      if (outlet < 0 || outlet > getNumOutlets()) {
         throw new java.lang.IllegalArgumentException("Invalid outlet number: "
               + outlet);
      }
      cmd = cmd + " " + outlet;
     
      if (client.connect(CONNECT_TIMEOUT)) {
         String response = client.waitForString(PROMPT, responseTime);
         if (response.indexOf(PROMPT) != -1) {
            response = "";
            if (client.sendCmd(cmd, true)) {
               response = client.waitForString(PROMPT, responseTime);
               try {
                  Thread.sleep(500);
               } catch (InterruptedException e) {
                  log.error("Sleeping was interrupted");
               }
               if (response.indexOf(PROMPT) != -1) {
                  client.close();
                  return true;
               }
            }
         }
      }
      client.close();
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public synchronized String getOutletStatus(int outlet) {
      String status = "UNKNOWN";
      if (client.connect(CONNECT_TIMEOUT) && client.sendCmd(PLUGSTATUS, false)) {
         final int WAIT = 1000;  // don't need much time for this.
         String response = client.read(WAIT);
         if (null != response && !response.isEmpty()) {
            String[] tableArray = response.split("-----\\+------------------\\+-------------\\+--------\\+-----------------\\+---------\\+");
            if (tableArray.length != 3) {
              // client.close();
               log.error("Unknown table format, cannot parse");
               return status;
            } else {
               /* The second element of the split table should be
                * a table of each outlet status.
                * Format is as follows:
                * Plug | Name             | Password    | Status | Boot/Seq. Delay | Default |
                * 
                * We are interested in the plug (outlet #) and the status only.
                */
               response = tableArray[1];
            }
            
            final int COL_COUNT = 6;
            final int OUTLET_COL = 0;
            final int STATUS_COL = 3;
            
            String[] splitRows = response.trim().split("\\|");
            int rowCount = splitRows.length / COL_COUNT;
            for (int row = 0; row <= rowCount; ++row) {
               String outletStr = "";
               String statusStr = "";
               try {
                  outletStr = splitRows[OUTLET_COL + (row * COL_COUNT)].trim();
                  statusStr = splitRows[STATUS_COL + (row * COL_COUNT)].trim();
                  
                  if (outlet == Integer.parseInt(outletStr)) {
                     if (statusStr.equals(PowerControllerDevice.OFF) || statusStr.equals(PowerControllerDevice.ON)) {
                        status = statusStr;
                        break;
                     } else {
                        log.info("Invalid status found: " + statusStr);
                        break;
                     }
                  }
               } catch (NumberFormatException npe) {
                  log.error("NumberFormatException parsing " + outletStr + " row format may have changed.");
               } catch (IndexOutOfBoundsException iob) {
                  log.error("IndexOutOfBoundsException table format may have changed.");
               }
            }
         }
      } else {
         if (!client.isConnected()) {
            log.warn("Could not connect to client.");         
         } else {
            log.warn("Status request failed.");
         }
      }
      client.close();
      return status;
   }

   /**
    * Set the IP address.
    * This also calls createPowerDevConn() to create a connection with the new IP.
    * @param ip the ip to set.
    */
   @Override
   public void setIp(String ip) {
      super.setIp(ip);
      createPowerDevConn();
   }

   /**
    * Sets the port.
    * This also calls createPowerDevConn() to create a connection with the new port.
    * @param port the port to set.
    */
    @Override
    public void setPort(int port) {
       super.setPort(port);
       createPowerDevConn();
    }
   

    @Override
	public void destroy() {
		log.info("Destroying the connection for"+getIp());
		client.close();
    }
 }
