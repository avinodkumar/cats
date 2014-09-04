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
package com.comcast.cats;

import org.junit.Test;

import com.comcast.cats.configuration.OCatsContext;
import com.comcast.cats.domain.exception.SettopNotFoundException;

public class OCatsFrameworkStartupIT {
	
	@Test
	public void wirePcvCatsFramework() throws SettopNotFoundException {
		CatsFramework framework = new CatsFramework(new OCatsContext());
		SettopFactory settopFactory = framework.getSettopFactory();
		Settop settop = settopFactory.findSettopByHostMac("00:17:EE:30:73:E2");
		//System.out.println(settop);
	}
}