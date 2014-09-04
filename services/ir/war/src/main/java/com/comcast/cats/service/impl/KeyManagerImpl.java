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

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Provider;

import com.comcast.cats.RemoteLayout;
import com.comcast.cats.keymanager.domain.Remote;
import com.comcast.cats.service.KeyManager;
import com.comcast.cats.service.impl.KeyManagerProxyProviderRest;
import com.comcast.cats.service.KeyManagerProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class uses a proxy to obtain keys from central KeyManagerProxy service.
 * @author cfrede001
 *
 */
@Startup
@Singleton
public class KeyManagerImpl implements KeyManager {
	Date lastSuccessfulRefresh;

	private static final Logger logger = LoggerFactory.getLogger(KeyManagerImpl.class);
	
	/**
	 * This data object holds references to remotes and keys retreived from
	 * KeyManagerProxy.
	 * TODO - Might be worth using dependency injection here, but issues have prevented this thus far.
	 */
	private RemoteLookup 				remoteLookup;
	/**
	 * This data object holds references to remote types and its layout
	 * TODO - would be a good idea to combine this data structure with RemoteLookup.
	 */
	private RemoteLayoutLookup          remoteLayoutLookup;
	private Provider<KeyManagerProxy> 	keyManagerProxyProvider;
	
	@PostConstruct
	public void postConstruct() {
		refresh();
	}
	
	public KeyManagerImpl() {
		this.remoteLookup = new RemoteLookup();
		this.keyManagerProxyProvider = new KeyManagerProxyProviderRest();
		this.remoteLayoutLookup=new RemoteLayoutLookup();
	}
	
	//@Inject
	public KeyManagerImpl(RemoteLookup remoteLookup, Provider<KeyManagerProxy> proxyProvider,
			RemoteLayoutLookup remoteLayoutLookup) {
		this.remoteLookup = remoteLookup;
		this.keyManagerProxyProvider = proxyProvider;
		this.remoteLayoutLookup=remoteLayoutLookup;
		logger.info("KeyManager(RemoteLookup) being created.");
	}

	@Override
	public String getIrCode(String remote, String format, String key) {
		if(remote == null) {
			throw new IllegalArgumentException("Remote can't be null");
		}
		if(key == null) {
			throw new IllegalArgumentException("Key can't be null");
		}
		
		/**
		 * If we have an empty list, then try and refresh the remotes.  This could certainly
		 * add a lot of load on the system if refresh fails, but it is absolutely critical
		 * we have IR data available.
		 */
		if(remoteLookup.isEmpty()) {
			refresh();
		}
		/**
		 * For now, all IR codes have the same format "PRONTO".
		 */
		return remoteLookup.findIrCode(remote, key);
	}
	
	/**
	 * Let's schedule all remote updates for every 5 minutes.
	 * WARNING!!!
	 * You must keep persistent=false otherwise the timer
	 * will be added to the DB each time the service is redeployed or the server is
	 * restarted.  This will cause the method to be invoked N times at the timeout where
	 * N is the number of restarts + deployments.
	 * WARNING!!!
	 * 
	*/
	@Override
	@Schedule(second="0", minute="*/5", hour="*", persistent=false)
	public void refresh() {
		KeyManagerProxy kmProxy = keyManagerProxyProvider.get();
		if(kmProxy != null) {
			try {
				List<Remote> remotes=kmProxy.remotes();
				remoteLookup.processRemotes(remotes);
				//Now iterate each remote type and get the remote layout  for that type.
				for(Remote remote:remotes){
					String remoteName=remote.getName();
					logger.debug(" Going to retrieve remote layout for: {} ",remoteName);
					List<RemoteLayout> layout=kmProxy.getRemoteLayout(remoteName);
					remoteLayoutLookup.processRemoteLayout(remoteName,layout);
				}
				lastSuccessfulRefresh = new Date();
			} catch(IllegalArgumentException e) {
				logger.error(e.getMessage());
			}
		} 
		else {
			logger.warn("KeyManagerProxy is null couldn't process keys");
		}
	}

	@Override
	public List<Remote> getRemotes() {
		return remoteLookup.getRemotes();
	}
	@Override
	public List<RemoteLayout> getRemoteLayout(String remoteType) {
		return remoteLayoutLookup.getRemoteLayout(remoteType);
	}
	
	public Date getLastRefreshed() {
		return this.lastSuccessfulRefresh;
	}
}
