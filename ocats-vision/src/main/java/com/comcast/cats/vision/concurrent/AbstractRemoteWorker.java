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

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.vision.event.RemoteEvent;
import com.comcast.cats.vision.event.RemoteResponseEvent;

public abstract class AbstractRemoteWorker extends SwingWorker<Boolean, Object> {
	private static final Logger logger = Logger.getLogger(PressKeyWorker.class);
	protected Settop settop;
	protected RemoteEvent evt;
	protected CatsEventDispatcher dispatcher;
	
	AbstractRemoteWorker(Settop settop, RemoteEvent remoteEvent, CatsEventDispatcher dispatcher) {
		this.settop = settop;
		this.evt = remoteEvent;
		this.dispatcher = dispatcher;
	}
	
	@Override
	protected void done() {
		try {
			RemoteResponseEvent responseEvt = new RemoteResponseEvent(evt);
			responseEvt.setResponse(get());
			responseEvt.setSettop(settop);
			dispatcher.sendCatsEvent(responseEvt);
        } catch (Exception ignore) {
        	logger.warn(ignore);
        }
	}
}