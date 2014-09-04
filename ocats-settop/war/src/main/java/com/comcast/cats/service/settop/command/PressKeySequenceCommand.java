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
package com.comcast.cats.service.settop.command;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.settop.SettopServiceCommand;

/**
 * Executes a sequence of keys with specified repeat counts and delays between
 * each of the remote commands.
 * 
 * @author beamma01c
 * 
 */
public class PressKeySequenceCommand extends SettopServiceBaseCommand implements
		SettopServiceCommand {

	/**
     * 
     */
	private static final long serialVersionUID = 652238202283397718L;
	/**
	 * Holds the comma separated commands to be executed.
	 */
	String commands;
	/**
	 * repeat counts corresponding to each command in comma separated format
	 */
	String repeatCounts;
	/**
	 * the delays between each key press,in comma separated format.
	 */
	String delays;

	/**
	 * Constructor of the command executor
	 * 
	 * @param settopToken
	 * @param commands
	 *            - comma separated sequence of commands.
	 * @param repeatCount
	 *            - comma separated sequence of repeat counts.
	 * @param delay
	 *            - comma separated sequence of delays.
	 */

	public PressKeySequenceCommand(SettopToken settopToken, String commands,
			String repeatCount, String delays) {
		super(settopToken);
		this.commands = commands;
		this.repeatCounts = repeatCount;
		this.delays = delays;
	}

	@Override
	public SettopServiceReturnMessage execute() {
		SettopServiceReturnMessage message = new SettopServiceReturnMessage();
		if(null==commands || null==repeatCounts||null==delays){
			handleException(message, "One of the prameters is null commands:"+commands+" repeatCounts:"+repeatCounts
					+" delays"+delays);
		}
		ArrayList<RemoteCommand> commandList=getRemoteCommands(commands);
		ArrayList<Integer> repeatCount=getTimingArray(repeatCounts);
		ArrayList<Integer> commandDelay=getTimingArray(delays);

		
		Settop settop = null;
		try {
			settop = this.getSettop();
			if (!(settop.getRemote().enterCustomKeySequence(commandList, repeatCount, commandDelay)) ){
				throw new SettopNotFoundException(
						"Failed to send command to the to IR device)");
			}
		} catch (SettopNotFoundException snf) {
			handleException(message, snf.getMessage());
		}

		return message;
	}
	private ArrayList<RemoteCommand> getRemoteCommands(String commands){
		ArrayList<RemoteCommand> remoteCommands=new ArrayList<RemoteCommand>();
		StringTokenizer tocken=new StringTokenizer(commands, ",");
		while(tocken.hasMoreTokens()){
			String tempStringCommand=tocken.nextToken();
			RemoteCommand temp=RemoteCommand.parse(tempStringCommand);
			remoteCommands.add(temp);
		}
		return remoteCommands;
	}
	/**
	 * Parses the comma separated string and creates an Integer Array.
	 * @param command
	 * @return
	 */
	private ArrayList<Integer>getTimingArray(String timing){
		ArrayList<Integer> values=new ArrayList<Integer>();
		StringTokenizer tocken=new StringTokenizer(timing, ",");
		while(tocken.hasMoreTokens()){
			String tempStringCommand=tocken.nextToken();
			Integer temp=Integer.parseInt(tempStringCommand);
			values.add(temp);
		}
		return values;
	}
}
