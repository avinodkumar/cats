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

import javax.inject.Provider;

import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.KeyManagerConstants;
import com.comcast.cats.service.KeyManagerProxy;

/**
 * Provider responsible for getting reference to the remote KeyManagerProxy via REST.
 * 
 * Note: for this implementation to work on JBoss AS 6, we need to do the workaround as suggested in
 * https://issues.jboss.org/browse/RESTEASY-526. 
 * 
 * This ticket issue states that it is not planned to be fixed in Jboss6 verions
 * https://issues.jboss.org/browse/JBAS-8841?page=com.atlassian.jira.plugin.system.issuetabpanels:all-tabpanel
 * 
 * @authorskurup00c
 */
public class KeyManagerProxyProviderRest implements Provider<KeyManagerProxy> {

	/**
	 * Provider method responsible for looking up the KeyManagerProxy interface.
	 * @return
	 */
    
    String hostname = null;
    private static final Logger logger = LoggerFactory.getLogger(KeyManagerProxyProviderRest.class);
    
    public KeyManagerProxyProviderRest(){
        logger.trace( "KeyManagerProxyProviderRest default constructor" );
    }
    
    public KeyManagerProxyProviderRest(String hostname){
        this.hostname = hostname;
        logger.trace( "KeyManagerProxyProviderRest constructor "+hostname );
    }
    
	@Override
	public KeyManagerProxy get() {
	    KeyManagerProxy keyManagerProxy = null;
	    try{
	        if(hostname == null || hostname.isEmpty()){
	            hostname = System.getProperty( KeyManagerConstants.KEY_MANAGER_PROXY_IP_NAME );
	        }
	        logger.trace( "KeyManagerProxyProviderRest get() "+hostname );
	        if(hostname != null){
			    String restUrl="http://"+hostname+"/keymanager-service"+KeyManagerConstants.APPLICATION_PATH+KeyManagerConstants.KEYMANAGER_PATH;
				logger.info("Rest interface to keymanager service constructed "+restUrl);
	            keyManagerProxy = ProxyFactory.create(KeyManagerProxy.class,restUrl);
	        }else{
	            logger.warn( "System property "+KeyManagerConstants.KEY_MANAGER_PROXY_IP_NAME+" may not be set properly" );
	        }
	        logger.trace( "KeyManagerProxyProviderRest keyManagerProxy "+keyManagerProxy );
	    }catch(Exception e){
	        logger.warn( e.getMessage() );
	    }
	    return keyManagerProxy;
	}
}
