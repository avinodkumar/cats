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
package com.comcast.cats.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteLayout;
import com.comcast.cats.keymanager.domain.*;

/**
 * Helper class responsible for simplifying getting the remote layout 
 * corresponding to a remote type.
 * @author bemman01c
 *
 */
/*
 * TODO: Maintaining two different classes to handle remote lookup and
 * remote layout lookup seems very inefficient.
 * 
 * Going ahead with this implementation for the time being. 
 * This need to be re looked.
 */
public class RemoteLayoutLookup extends ConcurrentHashMap<String,List <RemoteLayout> >{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3781621809170239268L;
	public static Integer DEFAULT_SIZE = 512;
	private static final Logger logger = LoggerFactory.getLogger(RemoteLayoutLookup.class);
	
	public RemoteLayoutLookup() {
		super(DEFAULT_SIZE);
	}
	
	public RemoteLayoutLookup(int capacity) {
		super(capacity);
	}
	
	/**
	 * Add the remoteLayout into the local hashmap against its name.
	 */
	public void processRemoteLayout(String remoteName,List<RemoteLayout> remoteLayout) {
		if(remoteLayout == null) {
			logger.error("Remote Layout object for remote type : {} is null",remoteName);
			throw new IllegalArgumentException("Remote Layout object for remote type:"+remoteName+" is null");
		}
		this.put(remoteName,remoteLayout);
	}
	
	/**
	 * Convenience method to clear remotes if this behavior is required.
	 */
	public void clear() {
		super.clear();
	}
	
	public List<RemoteLayout> getRemoteLayout(String remoteType) {
		return this.get(remoteType);
	}
}