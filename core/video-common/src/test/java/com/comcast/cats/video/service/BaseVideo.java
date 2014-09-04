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
package com.comcast.cats.video.service;

import java.net.URI;
import java.net.URISyntaxException;

import com.comcast.cats.event.CatsEventDispatcher;

public class BaseVideo {
	protected final String axisLocatorString = "axis://192.168.160.202/?camera=1";
	protected final CatsEventDispatcher dispatcher;
	protected final URI axisLocator;
	
	/**
     * Tests setting the Axis server URI.
     * @throws URISyntaxException 
     */
	public BaseVideo() throws URISyntaxException {
		this.axisLocator = new URI(axisLocatorString);
		this.dispatcher = new CatsEventDispatcherDummy();
	}
}
