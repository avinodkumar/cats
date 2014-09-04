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

import java.util.Properties;
import javax.inject.Provider;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.KeyManagerConstants;
import com.comcast.cats.service.KeyManagerProxy;

/**
 * Provider responsible for getting reference to the remote KeyManagerProxy EJB.
 * @author cfrede001
 */
/* We should be able to use dependency injection here, but no luck so far.
 * @Named
 * @ApplicationScoped
 */
public class KeyManagerProxyProvider implements Provider<KeyManagerProxy> {
	public static String DEFAULT_PROXY_HOST = "localhost";
	
	private String jnp = DEFAULT_PROXY_HOST;
	
	private static final Logger logger = LoggerFactory.getLogger(KeyManagerProxyProvider.class);
	
	/**
	 * If there is no proxy IP or it is empty, use localhost.
	 */
	protected void lookupKeyManagerProxyIP() {
		String ip = System.getProperty(KeyManagerConstants.KEY_MANAGER_PROXY_IP_NAME);
		if(ip == null || ip.isEmpty()) {
			ip = DEFAULT_PROXY_HOST;
		}
		logger.info("KeyManagerProxyIP = " + ip);
		buildJnp(ip);
	}
	
	protected void buildJnp(String host) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("jnp://").append(host).append(":1099");
		jnp = buffer.toString();
	}
	
	/**
	 * Using JNP string, bind to the context on this server.
	 * @return Context to the server
	 * @throws NamingException - If context can't be created.
	 */
	protected Context getContext() throws NamingException {
		Properties props = new Properties();
		logger.info("Connecting to KeyManagerProxy using " + jnp);
		props.setProperty(Context.PROVIDER_URL, jnp);
    	Context ctx = new InitialContext(props);
    	return ctx;
	}
	
	/**
	 * Provider method responsible for looking up the KeyManagerProxy interface.
	 * @return
	 */
	@Override
	public KeyManagerProxy get() {
		Context ctx = null;
		try {
			lookupKeyManagerProxyIP();
			ctx = getContext();
			if(ctx != null) {
				KeyManagerProxy proxy = (KeyManagerProxy) ctx.lookup(IRServiceConstants.KEY_MANAGER_PROXY_NAME);
				return proxy;
			}
	    } catch (NamingException e) {
			logger.error("NamingException[" + e.getMessage() + "] connecting to [" + jnp + "]");
		} finally {
			if(ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					logger.error("NamingException[" + e.getMessage() + "] closing [" + jnp + "]");
				}
			}
		}
	    return null;
	}

}
