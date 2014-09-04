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
package com.comcast.cats.service.ir.redrat;

import static com.comcast.cats.service.ir.redrat.RedRatCommands.ADD_IRNETBOX;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.BLACKLIST_ALL_IRNETBOXES;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.BLACKLIST_IRNETBOX;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.IPADDRESS_ARGUMENT;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.REMOVE_IRNETBOX;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.WHITLELIST_IRNETBOX;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.REDRAT_PROMPT_STRING_1;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.REDRAT_PROMPT_STRING_2;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.telnet.TelnetConnection;
import com.comcast.cats.utils.TelnetUtil;

/**
 * Represents the RedRat Hub. The hub manages all connections to the IrNetBox
 * Devices and redrat devices and is responsible for identifying and managing
 * the redrat devices as well.
 * 
 * @author skurup00c
 * 
 */
public class RedRatHub {

	private String redratHubHost = null;
	private int redratHubPort = -1;

	private TelnetConnection hubTelnetConnection;
	private static Collection<TelnetConnection> activeConnections = new ArrayList<TelnetConnection>();

	private static final Logger logger = LoggerFactory
			.getLogger(RedRatHub.class);

	HubConnectionPool hubConnectionPool;

	public RedRatHub(String redratHubHost, Integer redratHubPort) {
		this.redratHubHost = redratHubHost;
		this.redratHubPort = redratHubPort;

		hubConnectionPool = new HubConnectionPool(redratHubHost, redratHubPort);
	}

	public String getRedratHubHost() {
		return redratHubHost;
	}

	public void setRedratHubHost(String redratHubHost) {
		this.redratHubHost = redratHubHost;
	}

	public int getRedratHubPort() {
		return redratHubPort;
	}

	public void setRedratHubPort(int redratHubPort) {
		this.redratHubPort = redratHubPort;
	}

	/**
	 * Blacklist all IrNetBoxes.
	 */
	public void blackListAllIrNetBoxes() {
		boolean isConnected = establishTelnetConnection();
		logger.info("blackListAllIrNetBoxes isConnected " + isConnected);
		if (isConnected) {
			TelnetUtil.sendTelnetCommand(hubTelnetConnection,
					BLACKLIST_ALL_IRNETBOXES, REDRAT_PROMPT_STRING_2);
		}
	}

	/**
	 * Whitelist known IRNetBoxes.
	 * 
	 * @param irNetBoxProList
	 */
	public boolean whiteListRedRats(Collection<RedRatDevice> redratList) {
		boolean retVal = true;
		logger.info("whiteListIrNetBoxes list " + redratList);
		if (redratList != null && !redratList.isEmpty()) {
			boolean isConnected = establishTelnetConnection();
			logger.debug("whiteListIrNetBoxes isConnected " + isConnected);
			if (isConnected) {
				for (RedRatDevice device : redratList) {
					retVal = retVal & whiteListRedRat(device);
				}
			}
		}

		return retVal;
	}

	/**
	 * Whitelist known IRNetBoxes.
	 * 
	 * @param irNetBoxProList
	 */
	public boolean whiteListRedRat(RedRatDevice redratDevice) {
		boolean retVal = true;
		logger.info("whiteListIrNetBox redratDevice " + redratDevice);
		if (redratDevice != null) {
			if (redratDevice instanceof IrNetBoxPro) {
				boolean isConnected = establishTelnetConnection();
				logger.debug("whiteListIrNetBox isConnected " + isConnected);
				if (isConnected) {
					String response = null;
					response = TelnetUtil.sendTelnetCommand(
							hubTelnetConnection, ADD_IRNETBOX
									.replace(IPADDRESS_ARGUMENT,
											((IrNetBoxPro) redratDevice)
													.getIpAddress()),
							REDRAT_PROMPT_STRING_2);
					logger.info("RedratHub Add IRNETBOX response " + response);
					if (!response.startsWith(TelnetUtil.ERROR_STRING)) {
						response = TelnetUtil.sendTelnetCommand(
								hubTelnetConnection, WHITLELIST_IRNETBOX
										.replace(IPADDRESS_ARGUMENT,
												((IrNetBoxPro) redratDevice)
														.getIpAddress()),
								REDRAT_PROMPT_STRING_2);
						logger.info("RedratHub WHITLELIST_IRNETBOX response "
								+ response);
					}

					retVal = retVal
							&& !response.startsWith(TelnetUtil.ERROR_STRING);
				}
			}
		}

		return retVal;
	}

	/**
	 * Blacklist a list of IRNetBoxes.
	 * 
	 * @param irNetBoxList
	 */
	public boolean blackListRedRats(Collection<RedRatDevice> irNetBoxList) {
		boolean retVal = true;
		logger.info("blackListIrNetBoxes list " + irNetBoxList);
		if (irNetBoxList != null && !irNetBoxList.isEmpty()) {
			boolean isConnected = establishTelnetConnection();
			logger.debug("blackListIrNetBoxes isConnected " + isConnected);
			if (isConnected) {
				for (RedRatDevice device : irNetBoxList) {
					retVal = retVal & blackListRedRat(device);
				}
			}
		}
		return retVal;
	}

	/**
	 * Blacklist a list of IRNetBoxes.
	 * 
	 * @param irNetBoxList
	 */
	public boolean blackListRedRat(RedRatDevice device) {
		boolean retVal = true;
		logger.info("blackListIrNetBoxes list " + device);
		if (device != null) {
			if (device instanceof IrNetBoxPro) {
				boolean isConnected = establishTelnetConnection();
				logger.debug("blackListIrNetBox isConnected " + isConnected);
				if (isConnected) {
					if (!hubTelnetConnection.isConnected()) {
						TelnetUtil.connectTelnet(hubTelnetConnection);
					}
					String response = null;
					response = TelnetUtil.sendTelnetCommand(
							hubTelnetConnection, BLACKLIST_IRNETBOX.replace(
									IPADDRESS_ARGUMENT,
									((IrNetBoxPro) device).getIpAddress()),
							REDRAT_PROMPT_STRING_2);
					if (!response.startsWith(TelnetUtil.ERROR_STRING)) {
						response = TelnetUtil.sendTelnetCommand(
								hubTelnetConnection, REMOVE_IRNETBOX.replace(
										IPADDRESS_ARGUMENT,
										((IrNetBoxPro) device).getIpAddress()),
								REDRAT_PROMPT_STRING_2);
					}

					retVal = retVal
							&& !response.startsWith(TelnetUtil.ERROR_STRING);
				}
			}
		}
		return retVal;
	}

	/**
	 * Get a connection that can be used by the port to send messages.
	 * 
	 * @param irNetBoxPro
	 * @param portNumber
	 * @return
	 */
	public RedRatHubConnection getConnection(IrNetBoxPro irNetBoxPro,
			int portNumber) {
		logger.trace("getConnection irNetBoxPro " + irNetBoxPro + " port "
				+ portNumber);
		RedRatHubConnection portConnection = null;
		if (irNetBoxPro != null && portNumber >= 0) {
			portConnection = hubConnectionPool.getConnection();
			logger.debug("Redrathub  " + portConnection);
			if (portConnection != null) {
				activeConnections.add(portConnection);
			}
		}

		return portConnection;
	}

	private synchronized boolean establishTelnetConnection() {
		if (hubTelnetConnection == null) {
			hubTelnetConnection = new TelnetConnection(redratHubHost,
					redratHubPort, REDRAT_PROMPT_STRING_1);
		}
		boolean isConnected = TelnetUtil.connectTelnet(hubTelnetConnection);

		logger.debug("establishTelnetConnection isConnected " + isConnected);
		return isConnected;
	}

	public Collection<TelnetConnection> getActiveConnections() {
		return activeConnections;
	}
}
