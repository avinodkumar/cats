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


/**
 * This class allows for power toggling of the older NETBOOTER type powerbars.
 * <b>Note</b>: The hardware seems to be a little picky about sending commands
 * to it quickly.  Therefore, there is a small delay between power commands.
 * @see PAUSE_BETWEEN_CMD
 */
public class NetBooter_NP_1601D_PowerDevice extends NetBooter_PowerDevice {

    /**
     * Small delay used between connection and sending a command to the hardware.
     */
    public static final int PAUSE_BETWEEN_CMD = 200;

    /**
     * The device prompt.
     */
    public static final String PROMPT = ">";

    /**
     * The power command used for outlet state control.
     */
    public static final String PWRCMD = "pset";

    /**
     * The reboot command used for outlet toggling.
     */
    public static final String REBOOT = "rb";

    /**
     * The command to display outlet status.
     */
    public static final String OUTLETSTATUS = "pshow";

    /**
     * The ON state for the outlet.
     */
    public static final String ONSTATE = "1";

    /**
     * The OFF state for the outlet.
     */
    public static final String OFFSTATE = "0";

    private static final int responseTime = 500;
    private static final int CONNECT_TIMEOUT = 10000;

   private final Logger log = LoggerFactory.getLogger(NetBooter_NP_1601D_PowerDevice.class);

  
   public NetBooter_NP_1601D_PowerDevice() {
        
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
        
        

        log.info("Sending command: [" + np_command + "] to " + this.getIp());
             
		if (client.connect(CONNECT_TIMEOUT)) {
			String response = client.read(responseTime);

			if (client.sendCmd(np_command, true)) {
				response = client.read(responseTime);

				if (response.indexOf("Invalid command") == -1) {
					updateStatistics(outlet, cmd, true);
					client.close();
					return true;
				}
			}

		}
        updateStatistics(outlet, cmd, false);
        client.close();
        return false;
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getOutletStatus(int outlet) {
        String status = "UNKNOWN";

        if (client.connect(CONNECT_TIMEOUT)) {
            String response = client.waitForString(PROMPT, responseTime);
            sleep(PAUSE_BETWEEN_CMD);

            if (!client.sendCmd(OUTLETSTATUS, false)) {
                client.close();
                return status;
            }

            response = client.read(responseTime);

            if (null != response && !response.isEmpty()) {

				// NP16:
				// "-----+------------+---------+-------------|---------|----------";
				// New type:
				// "----+----------------+------+----------------+--------";
				String maker = "----.*--------\n";
				String[] tableArray = response.split(maker);

                if (tableArray.length != 2) {
                    client.close();
                    log.error("Unknown table format, cannot parse, response: " + response);
                    return status;
                }
                else {
                    /* The second element of the split table should be
                     * a table of each outlet status.
                     * Format is as follows:
                     * | Plug | Name             | Status    | Reserved By | Timer | AutoPing |
                     * 
                     * We are interested in the plug (outlet #) and the status only.
                     */
                    response = tableArray[1];
                }

                
                final int OUTLET_COL = 0;
                final int STATUS_COL = 2;

                String[] rows = response.trim().split("\\n\\r");
                for (int row = 0; row <= rows.length; ++row) {
                	String[] cols = rows[row].split("\\|");
                
                    String outletStr = "";
                    String statusStr = "";

                    try {
                        outletStr = cols[OUTLET_COL].trim();
                        statusStr = cols[STATUS_COL].trim();

                        if (outlet == Integer.parseInt(outletStr)) {
                            if (statusStr.equalsIgnoreCase(PowerControllerDevice.OFF)) {
                                status = PowerControllerDevice.OFF;
                            }
                            else if (statusStr.equalsIgnoreCase(PowerControllerDevice.ON)) {
                                status = PowerControllerDevice.ON;
                            }
                            else {
                                log.info("Invalid status found: [" + statusStr + "]");
                            }
                            break;
                        }
                    }
                    catch (NumberFormatException npe) {
                        log.error("NumberFormatException parsing [" + outletStr + "] row format may have changed.");
                    }
                    catch (IndexOutOfBoundsException iob) {
                        log.error("IndexOutOfBoundsException table format may have changed.");
                    }
                }
            }
        }
        else {
            if (!client.isConnected()) {
                log.warn("Could not connect to client.");
            }
            else {
                log.warn("Status request failed.");
            }
        }

        client.close();
        return status;
    }
}
