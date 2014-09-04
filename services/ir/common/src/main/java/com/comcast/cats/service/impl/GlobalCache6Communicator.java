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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of IRCommunicator for Global Cache GC-100 devices. Infos about
 * the GC-100 can be found at
 * <href>http://http://www.globalcache.com/files/Documentation
 * /GC-100_DataSheet.pdf</href>.
 * <href>http://www.globalcache.com/files/docs/API-GC-100.pdf</href>.
 */
public class GlobalCache6Communicator extends IRCommunicator {
	private static final int GC_100_PORT = 4998;
	private static final int GC100_SOCKET_TIMEOUT = 10000;
	private static String[] portMappings = { "2:1", "2:2", "2:3" };
	private static final int MAX_PORTS = 3;
	public static final Integer RETRY_COUNT = 1;
	private long lastIRTransmission = 0L;

	/**
	 * Max duration in milliseconds since last ir transmission before resetting
	 * the GC100 connection. The initial socket acquiring seems to take longer
	 * (~3 seconds) than expected, but should be acceptable under normal
	 * operating scenarios. This avoids trying to perform an IR retransmission
	 * after a timeout.
	 */
	// private static final long MAX_IDLE_DURATION = 30L * 60000L;
	// TODO - CEF set MAX_IDLE_DURATION to 15 seconds for testing.
	private static final long MAX_IDLE_DURATION = 30L * 60000L;

	// This is static because it should be unique for all (concurrent) requests
	// to the GC-100
	// Should be accessed only through getNextRequestId().
	private int m_id;

	// Network stuff
	private InetAddress m_ip;
	private Socket m_socket = null;
	private BufferedReader m_sis;
	private OutputStream m_sos;

	private final Logger log = LoggerFactory.getLogger(GlobalCache6Communicator.class);

	/**
	 * Creates a new instance of this communicator.
	 * 
	 * @param ip
	 *            The IP address of the GC-100.
	 * @param port
	 *            IR port to be used.
	 */
	public GlobalCache6Communicator(final InetAddress ip) {
		if (ip == null) {
			throw new IllegalArgumentException("IP cannot be null");
		}
		m_id = 0;
		m_ip = ip;
		m_socket = null;
	}

	/**
	 * Initializes this communicator.
	 * 
	 * @return <code>True</code> if the communicator was successfully
	 *         initialized. <code>False</code> otherwise.
	 */
	public synchronized boolean init() {
		boolean retVal = false;
		long tsBeforeConnect = 0;
		long tsAfterConnect = 0;
		// Close existing connection, if any.
		if (null != m_socket) {
			log.warn("Closing existing connection via uninit()");
			uninit();
		}

		// Open network connection to port 4998.
		try {
			InetSocketAddress addr = new InetSocketAddress(m_ip, GC_100_PORT);
			m_socket = new Socket();
			/*
			 * This connection should "always" be available for sending data so
			 * set keep alive to true.
			 */
			if (log.isTraceEnabled()) {
				tsBeforeConnect = System.currentTimeMillis();
			}

			//m_socket.setKeepAlive(true);
			m_socket.setSoTimeout(GC100_SOCKET_TIMEOUT);
			m_socket.connect(addr, GC100_SOCKET_TIMEOUT);

			if (log.isTraceEnabled()) {
				tsAfterConnect = System.currentTimeMillis();
				log.trace("Connection established to  " + m_ip + ":"
						+ GC_100_PORT + " took "
						+ Long.toString(tsAfterConnect - tsBeforeConnect));
			}
			m_sis = new BufferedReader(new InputStreamReader(
					m_socket.getInputStream()));
			m_sos = m_socket.getOutputStream();
			retVal = true;
		} catch (IOException ioe) {
			log.error("Failed to connect to " + m_ip + ":" + GC_100_PORT);
			log.error(ioe.getMessage());
			m_socket = null;
		}
		return retVal;
	}

	/**
	 * Finalizes this communicator.
	 */
	public void uninit() {
		// Close network connection.
		if (null != m_socket) {
			try {
				log.info("Socket currently exist, attempt to close it.");
				m_sis.close();
				m_sos.close();
				m_socket.close();
			} catch (IOException ioe) {
				log.error("Failed to close socket to " + m_ip + ": "
						+ GC_100_PORT);
				log.error(ioe.getMessage());
			} finally {
				m_sis = null;
				m_sos = null;
				m_socket = null;
			}
		}
	}

	/**
	 * Translates an IR code from the PRONTO format into the GC-100 format and
	 * transmits the code.
	 * 
	 * @param irCode
	 *            The IR code in PRONTO format.
	 * @param port
	 *            IR port on GC100 to send the code to.
	 * @return <code>True</code> if the code was transmitted successfully;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public synchronized boolean transmitIR(String irCode, int port) {
		String outlet = createOutlet(port);
		return transmitIR(irCode, outlet, 1, 1);
	}

	@Override
	public synchronized boolean transmitIR(String irCode, int port, int count,
			int offset) {
		String outlet = createOutlet(port);
		return transmitIR(irCode, outlet, count, offset);
	}

	/**
	 * Create outlet string from module and connector.
	 * 
	 * @param port
	 *            - IR port to be referenced for GC100 device.
	 * @return outlet string combining the module and connector.
	 */
	protected String createOutlet(int port) {
		if (port < 0 || port > MAX_PORTS) {
			throw new IllegalArgumentException("Module value must be >= 0");
		}
		return portMappings[port - 1];
	}

	/**
	 * Create the next available request ID within the range of 0 ... 65535.
	 * 
	 * @return The next id.
	 */
	private synchronized int getNextRequestId() {
		int result = m_id;
		m_id = (m_id + 1) & 0xFFFF;
		return result;
	}

	/**
	 * Base implementation to transmit the IR code.
	 * 
	 * @param irCode
	 *            - The PRONTO based IR code to send.
	 * @param outlet
	 *            - The outlet where this code should be sent to.
	 * @param count
	 *            - Number of times to send ir key.
	 * @param offset
	 *            - Offset that should be repeated.
	 * @return True if code was sent false otherwise.
	 */
	protected synchronized boolean transmitIR(String irCode, String outlet,
			int count, int offset) {
		
		if (null == irCode) {
			log.error("IR code can't be null");
			return false;
		}
		
		if (null == m_socket) {
			// Try initialization
			int retries = 0;
			do {
				init();
			} while (null == m_socket && retries++ < 3);

			// Still a problem?
			if (null == m_socket) {
				log.error("GlobalCacheCommunicator could not be initialized");
				return false;
			}
		}

		log.trace("Sending PRONTO code: " + irCode);
		// The UIRT codes can start with a zone selector "Z[0-3]. Remove it.
		String code = (irCode.startsWith("Z")) ? irCode.substring(2) : irCode;

		try {
			GlobalCacheCode gcCode = new GlobalCacheCode(code);

			int requestId = getNextRequestId();
			long startDecode = System.currentTimeMillis();
			String gcCodeStr = gcCode.getSendIRCommand(count, offset,
					requestId, outlet);
			long endDecode = System.currentTimeMillis();
			// Send the code and wait for response.
			log.debug("Sending IR request: " + gcCodeStr);

			String response = "";
			Integer retries = 0;
			// Let's loop to handle unkown commands.
			do {
				if (retries > 0) {
					log.warn("Unkown Command Found Retrying");
				}
				m_sos.write(gcCodeStr.getBytes());
				m_sos.flush();
				long endSend = System.currentTimeMillis();
				response = m_sis.readLine();
				log.debug("Received: " + response);
				if (null == response) {
					log.error("Received null response from IR requestid"+requestId+ " dest GC "+m_socket.getInetAddress());
					return false;
				}
				long endReceive = System.currentTimeMillis();

				log.info("IP[" + m_ip.toString() + "]Outlet[" + outlet
						+ "]:Decode[" + Long.toString(endDecode - startDecode)
						+ "ms]:Send[" + Long.toString(endSend - endDecode)
						+ "ms]:Read[" + Long.toString(endReceive - endSend)
						+ "ms]:RequestId[" + requestId + "]");
				lastIRTransmission = System.currentTimeMillis();
			} while (GlobalCacheCode.isUnkownCommand(response)
					&& retries < RETRY_COUNT);

			return GlobalCacheCode.verifyCompleteIRCommand(response, requestId,
					outlet);
		} catch (IOException ioe) {
			log.error("Failed to send IR code", ioe);
			log.error(ioe.getMessage());

			// Try re-initialized the connection for the next request.
			uninit();
			init();

			return false;
		} catch (NumberFormatException nfe) {
			log.error("Unable to convert PRONTO code: + " + code + ": "
					+ nfe.getMessage());
			return false;
		}
	}

	/*
	 * private boolean resetStaleConnection() { long currentTime =
	 * System.currentTimeMillis(); if((currentTime - lastIRTransmission) >=
	 * MAX_IDLE_DURATION) { log.info("Stale connection found!"); return true; }
	 * return false; }
	 */
}
