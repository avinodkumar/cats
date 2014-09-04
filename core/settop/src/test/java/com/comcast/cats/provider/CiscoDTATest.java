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

import java.net.URISyntaxException;
import org.junit.Test;
import static com.comcast.cats.RemoteCommand.*;

public class CiscoDTATest extends CiscoDTARemote {
	
	
	public CiscoDTATest() throws URISyntaxException {
		super();
		remote.setKeyDelay(100);
	}

	@Test
	public void pressKeyAndHold() {
		remote.pressKeyAndHold(CHUP, 10);
	}
	
	/*
	@Test
	public void channel() {
		remote.pressKeys(5, CHUP);
		remote.pressKeys(5, CHDN);	
	}
	
	@Test
	public void volume() {
		remote.pressKeys(5, VOLUP);
		remote.pressKeys(5, VOLDN);
	}
	
	@Test
	public void special() {
		remote.pressKeys(4, MUTE);
		remote.pressKeys(4, LANGUAGE);
		remote.pressKeys(4, LAST);
		remote.pressKeys(FOUR, FOUR, ENTER);
		remote.pressKeys(4, INFO);
	}
		
	@Test
	public void numbers() {
		remote.pressKeys(NINE, NINE);
		sleep(2000);
		remote.pressKeys(EIGHT, EIGHT);
		sleep(2000);
		remote.pressKeys(SEVEN, SEVEN);
		sleep(2000);
		remote.pressKeys(SIX, SIX);
		sleep(2000);
		remote.pressKeys(FIVE, FIVE);
		sleep(2000);
		remote.pressKeys(FOUR, FOUR);
		sleep(2000);
		remote.pressKeys(THREE, THREE);
		sleep(2000);
		remote.pressKeys(TWO, TWO);
		sleep(2000);
		remote.pressKeys(ONE, ONE);
		sleep(2000);
		remote.pressKeys(ZERO, ZERO);
	}
	*/
}