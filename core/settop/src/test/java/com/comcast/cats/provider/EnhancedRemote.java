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

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.service.IRService;

public class EnhancedRemote extends RemoteProviderServiceImpl {
	private Long keyDelay = 0L;
	public static Integer MAX_KEY_DELAY = 30000; //30 seconds
	
	public EnhancedRemote(IRService irService, URI irPath, String remoteType) {
		super(irService, irPath, remoteType);
	}

	public void pressKeys(RemoteCommand ...commands) {
		for(RemoteCommand c : commands) {
			this.pressKey(c);
			defaultSleep();
		}
	}
	
	public void pressKeys(Integer times, RemoteCommand ...commands) {
		for(int i=0; i<times;i++) {
			for(RemoteCommand c : commands) {
				this.pressKey(c);
				defaultSleep();
			}
		}
	}

	private void defaultSleep() {
		sleep(keyDelay);
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Long getKeyDelay() {
		return keyDelay;
	}

	public void setKeyDelay(Integer keyDelayInt) {
		setKeyDelay(keyDelayInt.longValue());
	}
	
	public void setKeyDelay(Long keyDelay) {
		if(keyDelay < 0 || keyDelay > MAX_KEY_DELAY ) {
			throw new IllegalArgumentException("Invalid Key Delay of " + keyDelay + " value must be between 0 <= " + MAX_KEY_DELAY + " milliseconds");
		}
		this.keyDelay = keyDelay;
	}
	
	public void pressKeyWithDelay(RemoteCommand command) {
		pressKey(command, keyDelay);
	}
	
	public void pressKey(RemoteCommand command, Long delay) {
		pressKey(command);
		sleep(delay);
	}
}
