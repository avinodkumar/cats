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

import java.util.List;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;

public class CatsEventDispatcherDummy implements CatsEventDispatcher {
	private CatsEventHandler handler;
	
	@Override
	public void addListener(CatsEventHandler handler, CatsEventType type) {
		this.handler = handler;
	}

	@Override
	public void addListener(CatsEventHandler handler, List<CatsEventType> types) {
		this.handler = handler;
	}

	@Override
	public void sendCatsEvent(CatsEvent evt) throws IllegalArgumentException {
		handler.catsEventPerformed(evt);
	}

	@Override
	public void removeListener(CatsEventHandler handler) {
		this.handler = null;
	}

	@Override
	public void addListener(CatsEventHandler arg0, CatsEventType arg1,
			Object arg2) {
		// TODO Auto-generated method stub
		
	}

}
