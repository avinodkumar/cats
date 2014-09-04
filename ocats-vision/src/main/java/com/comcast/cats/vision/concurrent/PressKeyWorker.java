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
package com.comcast.cats.vision.concurrent;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.vision.event.RemoteEvent;

public class PressKeyWorker extends AbstractRemoteWorker {
	
	public PressKeyWorker(Settop settop, RemoteEvent remoteEvent,
			CatsEventDispatcher dispatcher) {
		
		super(settop, remoteEvent, dispatcher);
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		RemoteEvent remoteEvent = (RemoteEvent) super.evt;
		return settop.pressKey(remoteEvent.getRemoteCommand());
	}
}