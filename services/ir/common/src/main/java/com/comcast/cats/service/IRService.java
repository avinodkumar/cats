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

import java.net.URI;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.keymanager.domain.Remote;

@WebService(name="IRService", targetNamespace=IRServiceConstants.NAMESPACE)
public interface IRService extends CatsWebService {
	/**
     * Send an IR command to a STB.
     * @param path
     *            The path to the device that will actually generate the IR request
     * @param irKeySet
     *            Which irKeySet to use.
     * @param command
     *            The key press to send to the STB
     * @return returns true if keyPress succeeded.
     */
	@WebMethod
    public boolean pressKey(@WebParam(name="path") final URI path,
    		@WebParam(name="irKeySet") final String irKeySet,
    		@WebParam(name="command") final RemoteCommand command);

    /**
     * Send an IR command to a STB with a hold.
     * @param path
     *            The path to the device that will actually generate the IR request
     * @param irKeySet
     *            Which irKeySet to use.
     * @param command
     *            The key press to send to the STB
     * @param count
     * 			  Number of times to send this key.
     * @return returns true if keyPress succeeded.
     */
	@WebMethod
    public boolean pressKeyAndHold(@WebParam(name="path") final URI path,
    		@WebParam(name="irKeySet") final String irKeySet,
    		@WebParam(name="command") final RemoteCommand command,
    		@WebParam(name="count") final Integer count);

	/**
     * Send multiple IR commands to a STB.
     * @param path
     *            The path to the device that will actually generate the IR request
     * @param irKeySet
     *            Which irKeySet to use.
     * @param commands
     *            The key press to send to the STB
	 * @param delayMillis
	 *			  Number of milliseconds to delay after key press.
	 * @return returns true if keyPress succeeded.
     */
	@WebMethod
	public boolean pressKeys(@WebParam(name="path") final URI path,
			@WebParam(name="irKeySet") final String irKeySet,
			@WebParam(name="commands") final List<RemoteCommand> commands,
			@WebParam(name="delayMillis") final int delayMillis);

	/**
	 * Tune to a particular channel.
	 * @param path - Path to the device.
     * @param irKeySet
     *            Which irKeySet to use.
	 * @param channel - Channel to be changed to.
	 * @param autoTuneEnabled - Flag if auto tune is set or not.
	 * @param delayMillis - Number of ms to delay between each key press.
	 */
	@WebMethod
	public boolean tune(@WebParam(name="path") final URI path,
			@WebParam(name="irKeySet") final String irKeySet,
			@WebParam(name="channel") final String channel,
			@WebParam(name="autoTuneEnabled") final boolean autoTuneEnabled,
			@WebParam(name="delayMillis") final int delayMillis);
	/**
	 * This will return all the remote commands associated with a key set.	 
     * @param irKeySet
     *            Which irKeySet to use.
	 * @return list of RemoteLayout
	 */
	@WebMethod
	public List<RemoteLayout> getRemoteCommands(@WebParam(name="keySet") final String irKeySet);

	/**
	 * Send Raw IR code base on path
	 * @param URI of the path to the device that will actually generate the IR request.
	 * @param irCode 
	 * @return true if the operation is successfully. false if failure.
	 */
	@WebMethod
	public boolean sendIR(@WebParam(name="path") final URI path,
			@WebParam(name="irCode") final String irCode);


	/**
	 *
	 * This implementation helps to invoke a specified key sequence in  a particular
	 * order with specified delay in between. Specific use case is for entering diag screen.
	 * @param path-Path to the device
	 * @param irKeySet-which irKeySet to use
	 * @param commands-the list of commands to be executed
	 * @param repeatCount -
	 * 			this value specifies the number of times a particular key has to be repeated.
	 * 			A value greater than Zero would have he same behavior as the pressAndHoldKey implementation
	 * 			for that key.
	 * @param delay- Delay between each operation.
	 * @return true if all the keys have been successfully send. false at the first failure.
	 */
	public boolean enterCustomKeySequence(@WebParam(name="path") final URI path,
    		@WebParam(name="irKeySet") final String irKeySet,
    		@WebParam(name="commands") final List<RemoteCommand> commands,
    		@WebParam(name="repeatCount") final List<Integer> repeatCount,
    		@WebParam(name="delay") final List<Integer> delay);

	/**
	 *
	 * This implementation helps to invoke a specified key sequence in  a particular
	 * order with specified delay in between. Specific use case is for entering diag screen.
	 * @param path-Path to the device
	 * @param irKeySet-which irKeySet to use
	 * @param commands-the list of commands to be executed with corresponding repeat counts and the delay
	 * to be inserted after each command execution
	 * @return true if all the keys have been successfully send. false at the first failure.
	 */
	public boolean enterRemoteCommandSequence(@WebParam(name="path") final URI path,
    		@WebParam(name="irKeySet") final String irKeySet,
    		@WebParam(name="commandsSequence") final List<RemoteCommandSequence> commands);

	/**
	 *
	 * Text entry for Astro remote. The string would be parsed and the individual characters would be
	 * transformed to corresponding key codes in the Astro remote.
	 *
	 * @param path  Path to the device
	 * @param irKeySet  which irKeySet to use
	 * @param stringToBeEntered the string to be send to the specified settop
	 */
	@WebMethod
	public boolean sendText(@WebParam(name="path") final URI path,
   		@WebParam(name="irKeySet") final String irKeySet,
   		@WebParam(name="text") final String stringToBeEntered);

	/**
	 * Get all remote Types available
	 * @return list of remotes.
	 */
	@WebMethod
	public List<Remote> getRemotes();
	/**
	 * Get all keys corresponding to a remote type
	 * 
	 * @param String name of remote type
	 * @return list of {@link RemoteLayout}
	 */
	@WebMethod
	public List<RemoteLayout> getRemoteLayout(String remoteType);
}


