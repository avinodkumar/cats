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
package com.comcast.cats.vision.event;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;
import com.comcast.cats.vision.concurrent.AbstractRemoteWorker;
import com.comcast.cats.vision.concurrent.PressKeyWorker;

/**
 * Class is responsible for receiving request events and executing them in a thread pool
 * for each of the active settops.
 * @author cfrede001
 *
 */
public class ActiveSettopEventProcessor implements CatsEventHandler {
	private SettopExclusiveAccessEnforcer 	activeSettopHolder;
	private ExecutorService 				executor;
	private CatsEventDispatcher 			dispatcher;
	
	
	public ActiveSettopEventProcessor(SettopExclusiveAccessEnforcer settopAccessEnforcer, 
			ExecutorService executor,
			CatsEventDispatcher dispatcher) {
		this.activeSettopHolder = settopAccessEnforcer;
		this.executor = executor;
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void catsEventPerformed(CatsEvent evt) {
		List<Settop> settops = getActiveSettops();
		
		for(Settop s : settops) {
			AbstractRemoteWorker worker = null;
			
			/**
			 * It would be preferred to move this out into a static helper method.
			 */
			if(evt instanceof RemoteEvent) {
				RemoteEvent remoteEvt = (RemoteEvent) evt;
				switch(remoteEvt.getActionType()) {
					case PRESS:
						worker = new PressKeyWorker(s, remoteEvt, dispatcher);
					break;
				}
				if(remoteEvt.getActionType().equals(ActionType.PRESS)) {
					
				}
			}
			if(worker != null) {
				executor.execute(worker);
			}
		}
	}
	
	private List<Settop> getActiveSettops() {
		return activeSettopHolder.getActiveSettops();
	}
}