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
package com.comcast.cats.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * This service represents the way most users will interact with the settop
 * boxes in the lab.
 * 
 * @author cfrede001
 */
@WebService(targetNamespace = SettopConstants.NAMESPACE)
public interface SettopService {
	/**
	 * Get a settop for user identified by userToken with the specified mac
	 * address. If box was found and it can be allocated to the user then a
	 * SettopToken will be created and returned.
	 * 
	 * @param mac
	 *            - The MAC Address.
	 * @param user
	 *            - The username requesting the reservation.
	 * @return Settop token if this box can be allocated for a particular
	 *         Reservation.
	 * 
	 * @throws SettopNotFoundException
	 */
	@WebMethod
	SettopToken getSettop(@WebParam(name = "mac") String mac,
			@WebParam(name = "userToken") String userToken)
			throws SettopNotFoundException;

	/**
	 * Create a reservation based on the mac address.
	 * 
	 * @param settop
	 *            - SettopToken to retrieve the settop information.
	 * @param user
	 *            - The username requesting the reservation.
	 * @return Settop specific information for the given.
	 * 
	 * @throws SettopNotFoundException
	 */
	@WebMethod
	SettopDesc getSettopInfo(@WebParam(name = "Settop") SettopToken settop)
			throws SettopNotFoundException;

	/**
	 * Send a remote control IR command to the STB.
	 * 
	 * @param mac
	 *            The MAC address of the STB
	 * @param c
	 *            The remote button to push
	 */
	@WebMethod
	SettopServiceReturnMessage pressKey(
			@WebParam(name = "Settop") SettopToken settop,
			@WebParam(name = "command") RemoteCommand command);

	/**
	 * Perform direct tune with subtle delay between each digit (~5ms).
	 * 
	 * @param channel
	 *            - String to represent channel to be tuned. Must contain digits
	 *            [0-9] and a length 0 &lt; length &lt;=3.
	 * @param autoTuneEnabled
	 *            if (!autoTuneEnabled) Send IR "SELECT" else //Do Nothing
	 */
	@WebMethod
	SettopServiceReturnMessage tune(
			@WebParam(name = "Settop") SettopToken settop,
			@WebParam(name = "channel") String channel,
			@WebParam(name = "autoTuneEnabled") boolean autoTuneEnabled);

	/**
	 * Send a remote control IR command to the STB.
	 * 
	 * @param mac
	 *            The MAC address of the STB
	 * @param c
	 *            The remote button to push
	 * @param count
	 *            The number of times this button will be held.
	 */
	@WebMethod
	SettopServiceReturnMessage pressKeyAndHold(
			@WebParam(name = "Settop") SettopToken settop,
			@WebParam(name = "command") RemoteCommand command,
			@WebParam(name = "count") Integer count);

	/**
	 * Sends a sequence of keys with specified repeat counts and specified
	 * intervals in between. The number of remote commands, repeat counts and
	 * sleep times should be the same. A repeat count greater than 0 would have
	 * the same behavior as a pressAndHold on that remote command.
	 * 
	 * @param settop
	 * @param keySeq
	 *            - A string with a comma separated remote command sequence.
	 *            Eg:"EIGHT,EXIT,GUIDE"
	 * @param repCount
	 *            - A comma separated string with the repeat counts of for each
	 *            of the remote commands Eg:"30,0,0" A count greater than 0
	 *            would imply a pressAndHold behavior and a 0 would imply a
	 *            pressKey. The above sequence means pressAndHold(EIGHT,30),
	 *            pressKey(EXIT) and pressKey(GUIDE) will be executed.
	 * @param sleepTime
	 *            - A comma separated string with the sleep times between each
	 *            remote command. Eg:1000,2000,1000. This sequence means
	 *            pressAndHold(EIGHT,30), sleep(1sec),pressKey(EXIT), sleep(2
	 *            sec), pressKey(GUIDE) sleep(1sec) will be executed.
	 * 
	 * @return
	 */
	@WebMethod
	SettopServiceReturnMessage pressKeySequence(
			@WebParam(name = "Settop") SettopToken settop,
			@WebParam(name = "commandSequence") String keySeq,
			@WebParam(name = "repeatCount") String repCount,
			@WebParam(name = "sleepTime") String sleepTime);

	/**
	 * @param mac
	 *            The MAC address of the STB
	 * @return a URL where an stream of video can be retrieved (generally this
	 *         will be an AXIS device URL)
	 */
	@WebMethod
	SettopServiceReturnMessage streamVideo(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Communicate with the power strip the STB is powered by and tell the power
	 * strip to switch off the outlet.
	 * 
	 * @param mac
	 *            The MAC address of the STB
	 */
	@WebMethod
	SettopServiceReturnMessage hardPowerOff(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Communicate with the power strip the STB is powered by and tell the power
	 * strip to switch on the outlet.
	 * 
	 * @param mac
	 *            The MAC address of the STB
	 */
	@WebMethod
	SettopServiceReturnMessage hardPowerOn(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Communicate with the power strip the STB is powered by and tell the power
	 * strip to toggle the outlet.
	 * 
	 * @param mac
	 *            The MAC address of the STB
	 */
	@WebMethod
	SettopServiceReturnMessage hardPowerToggle(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Communicate with the power strip the STB is powered by and determine if
	 * the outlet is currently powered on or powered off.
	 * 
	 * @param mac
	 *            The MAC address of the STB
	 * @return The power state returned by the WTI or Netboost power strips.
	 */
	@WebMethod
	SettopServiceReturnMessage getPowerState(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Remove a reservation on a box.
	 * 
	 * @param reservation
	 *            - Current reservation being used.
	 */
	@WebMethod
	SettopServiceReturnMessage releaseSettop(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Check whether the settop is locked or not.
	 * 
	 * @param settop
	 * @return
	 */
	@WebMethod
	SettopServiceReturnMessage isLocked(
			@WebParam(name = "Settop") SettopToken settop);

	/**
	 * Retrieves the last error by user token.
	 * 
	 * @param settop
	 *            settop token
	 * @return The last error message
	 */
	@WebMethod
	SettopServiceReturnMessage getLastError(
			@WebParam(name = "Settop") SettopToken settop);
}