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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/**
 * A very general dispatcher implementation for settop events.  This
 * can probably be refined and added the core API and Settop implementation
 * later.
 * @author cfrede001
 * @since 2.0.0.M1
 */
@Named
public class SettopEventDispatcherImpl implements SettopEventDispatcher {
	private List<SettopEventListener> listeners = new ArrayList<SettopEventListener>();

	@Override
	public void dispatchSettopChangeEvent(SettopEvent settopEvent) {
		for(SettopEventListener listener : listeners) {
			listener.settopEvent(settopEvent);
		}
	}

	@Override
	public void registerSettopChangeListener(SettopEventListener listener) {
		if(listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeSettopChangeListener(SettopEventListener listener) {
		if(listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
}
