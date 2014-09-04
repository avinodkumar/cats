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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.Assert.*;

import org.junit.Test;

public class VideoControllerTest extends BaseVideo {

	public VideoControllerTest() throws URISyntaxException {
		super();
	}

	@Test
	public void testCameraParameters() throws MalformedURLException, URISyntaxException {
		VideoController vc = new VideoController(this.dispatcher, new URI("axis://192.168.160.202/?camera=1"));
		assert(vc.getCamera() == 1);
		
		vc = new VideoController(this.dispatcher, new URI("axis://192.168.160.202/?camera=4"));
		assert(vc.getCamera() == 4);
		
		//Test for no camera specified.
		vc = new VideoController(this.dispatcher, new URI("axis://192.168.160.202/"));
		assert(vc.getCamera() == 1);
	}
}
