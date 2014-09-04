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

import org.testng.annotations.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import com.comcast.cats.service.impl.GlobalCache12Communicator;
import com.comcast.cats.service.impl.GlobalCache6Communicator;
import com.comcast.cats.service.impl.IRCommunicator;
import com.comcast.cats.service.impl.IRManagerImpl;
import com.comcast.cats.service.impl.ITach3Communicator;
import com.comcast.cats.service.impl.TestCommunicator;

/**
 * The Class IRManagerTest.
 * 
 * @Author : Deepa
 * @since : 20th Sept 2012
 * Description : The Class IRManagerTest is the unit test of {@link IRManager}.
 */


public class IRManagerTest {

	private IRManager irManager = new IRManagerImpl();
	@Test
	public void testIRVariants() throws UnknownHostException, URISyntaxException {
		IRCommunicator comm = irManager.retrieveIRCommunicator(new URI("gc100://1.1.1.1/?port=1"));
		assert(comm instanceof GlobalCache12Communicator);
		comm = irManager.retrieveIRCommunicator(new URI("gc100-12://1.1.1.2/?port=1"));
		assert(comm instanceof GlobalCache12Communicator);
		comm = irManager.retrieveIRCommunicator(new URI("gc100-6://1.1.1.3/?port=1"));
		assert(comm instanceof GlobalCache6Communicator);
		comm = irManager.retrieveIRCommunicator(new URI("itach://1.1.1.4/?port=1"));
		assert(comm instanceof ITach3Communicator);
		comm = irManager.retrieveIRCommunicator(new URI("test://1.1.1.5/?port=1"));
		assert(comm instanceof TestCommunicator);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void negativeRetrieveIRCommuniatorNullPathTest() throws UnknownHostException {
		irManager.retrieveIRCommunicator(null);
	}
	
	@Test( enabled = false, expectedExceptions=UnknownHostException.class)
	public void negativeRetrieveIRCommunicatorUnknownHost() throws UnknownHostException, URISyntaxException {
		irManager.retrieveIRCommunicator(new URI("gc100://unkownhost/"));
	}
	
	/**
	 * Test that if an invalid scheme is requested that an IllegalArgumentException is thrown.
	 * @throws URISyntaxException 
	 * @throws UnknownHostException 
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void negativeRetrieveIRCommunicatorInvalidScheme() throws UnknownHostException, URISyntaxException {
		irManager.retrieveIRCommunicator(new URI("invalidscheme://localhost/"));
	}
	
}
