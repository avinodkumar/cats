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
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.IRHardwareEnum;
import com.comcast.cats.service.IRManager;

/**
 * This implementation should create a proper singleton class for handling all the IRCommunicator instances
 * on the AS at any given time.  
 * @author cfrede001
 *
 */
@Startup
@Singleton
@TransactionAttribute(TransactionAttributeType.NEVER)
public class IRManagerImpl implements IRManager {
	private static final Map<String, IRCommunicator> irGroup = new HashMap<String, IRCommunicator>();
	private static final Logger logger = LoggerFactory.getLogger(IRManagerImpl.class);
	
	public IRManagerImpl() {
		logger.warn("Creating IRManager");
	}
	
	@PostConstruct
	public void contruct() {
		logger.warn("IRManager PostContruct");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public IRCommunicator retrieveIRCommunicator(final URI path) throws UnknownHostException {
		if(path == null) {
			throw new IllegalArgumentException("IRCommunicator path can't be null");
		}
		if(!IRHardwareEnum.validate(path.getScheme())) {
			throw new IllegalArgumentException("IRCommunicator type is unknown");
		}
			
		IRCommunicator ir = null;
		
		if(irGroup.containsKey(path.getHost())) {
			logger.debug("Returning IR device with path = " + path.toString());
			ir = irGroup.get(path.getHost());
			return ir;
		}
		
		InetAddress addr = InetAddress.getByName(path.getHost());
		IRHardwareEnum type = IRHardwareEnum.getByValue(path.getScheme());
		switch(type) {
			case GC100:
			case GC100_12:
				logger.info("GlobalCache12Communicator will be created");
				ir = new GlobalCache12Communicator(addr);
				break;
			case GC100_6:
				logger.info("GlobalCache6Communicator will be created");
				ir = new GlobalCache6Communicator(addr);
				break;
			case ITACH:
				logger.info("ITach3Communicator will be created");
				ir = new ITach3Communicator(addr);
				break;
			case TEST:
				logger.info("TestCommunicator will be created");
				ir = new TestCommunicator(path.getHost());
				break;
			default:
				throw new IllegalArgumentException("IRCommunicator for '" + path.toString() + "' could not be found or created!");
		}
		
		if(ir != null) {
			logger.warn("Adding IR device with path = " + path.toString() + " to map of IR devices");
			ir.setName(path.getHost());
			irGroup.put(path.getHost(), ir);
		}
		return ir;
	}
	
	/**
	 * This should only get called when shutting down the application or Application server.  In the case
	 * this is called during normal operation it could cause some very unwanted behavior.
	 */
	@PreDestroy
	public void destroy() {
		for(IRCommunicator ir : irGroup.values()) {
			logger.warn("Closing IRCommunicator " + ir.getName());
			ir.uninit();
		}
	}

	@Override
	public Map<String, IRCommunicator> getIRCommunicators() {
		return irGroup;
	}
	
	/* Placeholder, this should be filled in.
	@Override
	public String toString() {
		
		String rtn = "";
		for(IRCommunicator ir : irGroup.values()) {
			
		}
		;
	}
	*/
}
