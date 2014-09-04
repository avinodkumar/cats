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

/**
 * Simple test IRCommunicator to be used to test EJB thread pooling of IRCommunicator devices.
 * @author cfrede001
 *
 */
public class TestCommunicator extends IRCommunicator {

	public TestCommunicator(String ip) {
		super.setName(ip);
	}
	
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void uninit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized boolean transmitIR(String irCode, int port) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean transmitIR(String irCode, int port, int count, int offset) {
		return transmitIR(irCode, port);
	}
}
