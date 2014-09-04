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
package com.comcast.cats.service.impl;

import java.net.InetAddress;

/**
 * The only difference between 
 * @author cfrede001
 *
 */
public class GlobalCache12Communicator extends GlobalCache6Communicator {
	private static String[] portMappings = {"4:1", "4:2", "4:3", "5:1", "5:2", "5:3"};
	private static final int MAX_PORTS = 6;
	
	public GlobalCache12Communicator(InetAddress ip) {
		super(ip);
	}
	
	/**
	* Create outlet string from module and connector.
	* @param port - IR port to be referenced for GC100 device.
	* @return outlet string combining the module and connector.
	*/
	@Override
	protected String createOutlet(int port) {
      if (port < 0 || port > MAX_PORTS) {
         throw new IllegalArgumentException("Module value must be >= 0");
      }
      return portMappings[port - 1];
   }   
}
