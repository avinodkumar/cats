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
import java.net.UnknownHostException;
import java.util.Map;

import com.comcast.cats.service.impl.IRCommunicator;


public interface IRManager {
	
	/**
	 * Currently only supports gc100 devices.  Lookup device in Map and determine
	 * if the host is there.  If not create a new IRCommunicator and add it to the
	 * map for future lookups.
	 * @param path
	 * @return
	 * @throws UnknownHostException
	 */
	public IRCommunicator retrieveIRCommunicator(final URI path) throws UnknownHostException;
	
	/**
	 * Retrieve all the active IRCommunicators for display purposes or what have you.
	 * @return
	 */
	public Map<String, IRCommunicator> getIRCommunicators();
}
