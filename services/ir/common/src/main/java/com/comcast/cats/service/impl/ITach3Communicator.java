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
 * Represents a small adjustment over the standard GC100-6 device.
 * Specification for the iTach can be found here:
 * 	http://www.globalcache.com/files/docs/API-iTach.pdf
 * 
 * @author cfrede001
 *
 */
public class ITach3Communicator extends GlobalCache6Communicator {
	private static String[] portMappings = {"1:1", "1:2", "1:3"};
	
	public ITach3Communicator(InetAddress ip) {
		super(ip);		
	}
	
	/**
	* Create outlet string from module and connector.
	* @param port - IR port to be referenced for GC100 device.
	* @return outlet string combining the module and connector.
	*/
	@Override
	protected String createOutlet(int port) {
      if (port < 0 || port > 3) {
         throw new IllegalArgumentException("Module value must be >= 0");
      }
      return portMappings[port - 1];
   }   

}
