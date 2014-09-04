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
import com.comcast.cats.keymanager.domain.*;

/**
 * Helper class responsible for simplifying searching against remotes for keys.
 * @author cfrede001
 *
 */
/* We should be able to use dependency injection here, but no luck so far.
 * @Named
 * @ApplicationScoped
 */
public class RemoteLookup extends ConcurrentHashMap<String, Key> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3781621809170239268L;
	public static Integer DEFAULT_SIZE = 512;
	private static final Logger logger = LoggerFactory.getLogger(RemoteLookup.class);
	List<Remote> remotes = null;
	
	public RemoteLookup() {
		super(DEFAULT_SIZE);
	}
	
	public RemoteLookup(int capacity) {
		super(capacity);
	}
	
	public String findIrCode(String remote, String key) {
		String remoteKey=createRemoteKey(remote, key);
		Key hashValue=this.get(remoteKey);
		String value=null;
		if( hashValue==null){	
			logger.warn("Key code:"+key +" is not available for remote:"+remote);
		}else{
			 value=hashValue.getValue();
		}
		return value;
	}
	
	protected String createRemoteKey(String remote, String key) {
		return remote + "_" + key;
	}
	
	/**
	 * Utilize the remote_key as a key to lookup ir codes. We're only overwriting
	 * keys and not clearing them prior to adding the new set.  This should be the
	 * expected behavior.
	 */
	public void processRemotes(List<Remote> remotes) {
		if(remotes == null) {
			throw new IllegalArgumentException("Remotes object is null");
		}
		this.remotes = remotes;
		for(Remote r : remotes) {
			for(Key k : r.getKeys()) {
				this.put(createRemoteKey(r.getName(), k.getName()), k);
				logger.debug("Adding " + r.getName() + "_" + k.getName());
			}
		}
	}
	
	/**
	 * Convenience method to clear remotes if this behavior is required.
	 */
	public void clear() {
		remotes.clear();
		super.clear();
	}
	
	public List<Remote> getRemotes() {
		return remotes;
	}
}