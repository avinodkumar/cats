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
package com.comcast.cats.provider;

import java.net.URI;
import java.net.URISyntaxException;

import com.comcast.cats.service.IRService;

public class CiscoDTARemote extends CiscoDTABaseClass {
	EnhancedRemote remote;
	String irPath = "gc100://192.168.120.2/?port=3";
	String keyset = "XR2";
	
	public CiscoDTARemote() throws URISyntaxException {
		super();
		IRService irService = ctx.getBean(IRService.class);
		remote = new EnhancedRemote(irService, new URI(irPath), keyset);
	}
	
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}