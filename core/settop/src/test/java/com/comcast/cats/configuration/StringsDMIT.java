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
package com.comcast.cats.configuration;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Integration tests for Strings DM support that will pull a strings.dm file off the cats-dev server.
 * @author cfrede001
 *
 */
public class StringsDMIT {
	
	@Test
	public void getStringDMFromServer() throws URISyntaxException, IOException {
		String dummyCatsHome = System.getProperty("user.dir") + "/target/test-classes/stringsdm/";
		ApplicationContext ctx = StringsDMTest.getApplicationContext(dummyCatsHome);
		StringsDMHandler stringDMLocator = ctx.getBean(StringsDMHandler.class);
		String path = stringDMLocator.retrieveStringsDMFile();
		assertTrue(path != null && !path.isEmpty());
	}
}
