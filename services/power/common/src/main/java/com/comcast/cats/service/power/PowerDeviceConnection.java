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
package com.comcast.cats.service.power;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.comcast.cats.service.power.util.PowerConstants.NEWLINE;


/**
 * This class is designed to provide remote connections to the
 * power devices using a simple socket.
 *
 */
public class PowerDeviceConnection {
    private String              host;
    private InetAddress         inetAddress;
    private int                 port;
    private Socket              socket;
    protected BufferedReader    inFromServer;
    protected OutputStream      outToServer;
   private final Logger log = LoggerFactory.getLogger(PowerDeviceConnection.class);

    /**
     * Used for determining if to send an initial NEWLINE upon a successful socket connection to remote host
     */
    private boolean             initialCR = false;   

    /**
     * The constructor to create the non-connected object,
     * based on the remote address and port.
     *
     * @param host - the InetAddress of the remote device
     * @param port - the port of the remote device
     */
    public PowerDeviceConnection(InetAddress host, int port) {
       this(host.toString(), port);
    }

    /**
     * The constructor to create the non-connected object,
     * based on remote address and port.
     *
     * @param host - the string address representation of the remote device
     * @param port - the port of the remote device
     */
    public PowerDeviceConnection(String host, int port) {
        this.port = port;
        this.host = host;
        this.socket = new Socket();
    }

    /**
     * Attempts to create connection with remote devices.
     * If a ConnectException is thrown, this will try to
     * connect again up to three times.
     * @param timeout The timeout in milliseconds.
     * @param currTime Current number of seconds.
     * @return <b>true</b> on success.
     */
    private boolean connectTry(final int timeout, int currTime) {
        long start = System.currentTimeMillis();
        try {
            this.socket = new Socket();
            inetAddress = InetAddress.getByName(host);
            this.socket.connect(new InetSocketAddress(inetAddress, port), timeout);
            try {
                outToServer = socket.getOutputStream();
                inFromServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            } catch (IOException ioe) {
                close();
            }
            if (initialCR) {
                sendCmd(NEWLINE, true);
            }
            return isConnected();
        } catch (java.net.SocketTimeoutException ste) {
            log.error("Connection timed out; waited "
                    + timeout + " milliseconds.");
        } catch (java.net.UnknownHostException uhe) {
            log.error("UnknownHostException: " + uhe.getMessage());
        } catch (java.net.ConnectException cone) {
            // This is probably because a client is already connected to the
            // power device and in middle of sending a command lets retry
            // until we hit the timeout.
            retrySleep();
            log.error("ConnectException, about to retry: " + cone.getMessage());
            currTime += System.currentTimeMillis() - start;
            if (currTime < timeout) {
                return connectTry(timeout, currTime);
            }
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
        return false;
    }

    private void retrySleep() {
        try {
            Thread.sleep(300);
        }catch(Exception e) {
            log.error("Retry sleep");
        }
    }

    /**
     * This method tries to establish a connection with the remote device
     * based on three criteria: successful creation of an InetAddress, the
     * success of the connection and the establishment of the input and output
     * data streams. If any of the above fail, the connection returns FALSE
     *
     * @param timeout - the amount of time in millisec. to wait for the connection.
     * @return boolean - true if all criteria was met and connection was made, false otherwise
     */
    public boolean connect(final int timeout) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must be > 0");
        }
        return connectTry(timeout, 0);
    }

    /**
     * This method writes out a string as an array of bytes to the remote host.
     * Before it writes it verifies that the string terminates with NEWLINE
     * characters, if not, it appends them to the string before transmission.
     * These characters can be changed by modifying the NEWLINE field.
     *
     * @param cmd - the string to transmit to the remote host
     * @param echo - set true if you want the string to be echoed back to the console, or false for quiet mode
     * @return boolean - true if the string was successfully transmitted, false otherwise
     */
    public boolean sendCmd(String cmd, final boolean echo) {
        if (!cmd.endsWith(NEWLINE)) {
            cmd += NEWLINE;
        }
        try {
            if (null != this.outToServer) {
                this.outToServer.write(cmd.getBytes());
                if (echo) {
                    log.info("Writing to " + host + ": ["
                            + cmd.replaceAll("(\\r|\\n)", "") + "]");
                }
                this.outToServer.flush();
                return true;
            }
        } catch (IOException ioe) {
            log.error("IOException: " + ioe.getMessage());
        }
        return false;
    }

    /**
     * This method sets the socket timeout for the amount of time to wait for blocking
     * methods, such as reading from the socket input stream
     *
     * @param timeout - amount of time to block in milliseconds
     *
     */
    protected void setSoTimeout(final int timeout) {
        try {
            if (timeout == this.socket.getSoTimeout()) {
                return;
            }
            this.socket.setSoTimeout(timeout);
        } catch (java.net.SocketException se) {
            log.error("SocketException: " + se.getMessage());
        }
    }

    /**
     * This method reads a character at a time from the socket input stream until EOF is reached,
     * or stream is closed. This is a blocking method until -1 is returned by inputStream, for more
     * information refer to the socket api
     *
     * @param timeout - amount of time to block in milliseconds before raising a java.net.SocketTimeoutException
     * @return String - the string that it reads from the socket input stream
     */
    public String read(final int timeout) {
        StringBuilder buff = new StringBuilder();
        setSoTimeout(timeout);
        try {
            int c = inFromServer.read();
            while (-1 != c) {
                buff.append((char) c);
                c = inFromServer.read();
            }
        } catch (java.net.SocketTimeoutException ste) {
            log.debug("SocketTimeoutException (likely expected): " + ste.getMessage());
        } catch (IOException ioe) {
            log.error("IOException: " + ioe.getMessage());
        }
        return buff.toString();
    }

    /**
     * This method reads a character at a time from the socket input stream until EOF is reached,
     * or the substring of the search string is found. Once string is found the input stream is interrupted
     * Immediately and returns
     *
     * @param string - the specific string that the user is waiting on from the socket input stream
     * @param timeout - amount of time to block in milliseconds before raising a java.net.SocketTimeoutException
     * @return  String - the string that it reads from the socket input stream up to the search string,
     *                   everything after is truncated
     */
    public String waitForString(String string, final int timeout) {
        StringBuilder buff = new StringBuilder();
        setSoTimeout(timeout);
        try {
            int c = inFromServer.read();
            while (-1 != c) {
                buff.append((char) c);
                if (buff.indexOf(string) != -1) {
                    break;
                }
                c = inFromServer.read();
            }
        } catch (java.net.SocketTimeoutException ste) {
            log.error(ste.getClass().getName() + " caught: " + ste.getMessage());
        } catch (IOException ioe) {
            log.error(ioe.getClass().getName() + " caught: " + ioe.getMessage());
        }
        return buff.toString();
    }

    /**
     * This method is used to request that an initial carriage return is sent right after a
     * connection to the remote host has been established.
     *
     * @param   initialCR - true if you want the initial NEWLINE to be sent when a connection is made,
     *                      false otherwise
     */
    public void setInitialCR(final boolean initialCR) {
        this.initialCR = initialCR;
    }

    /**
     * This method retrieve the initialCR value.
     *
     * @return boolean - true if initial carriage return at connection has been set, false otherwise
     */
    public boolean getInitialCR() {
        return this.initialCR;
    }

    /**
     * This method verifies if the input stream was established.
     *
     * @return boolean - true if socket input stream has been established, false otherwise
     */
    public boolean hasInputStream() {
        return (null != inFromServer);
    }

    /**
     * This method verifies that the connection has been established and is alive.
     *
     * @return boolean - true if socket is connected and not closed, false otherwise
     */
    public boolean isConnected() {
        return (this.socket.isConnected() && (!this.socket.isClosed()));
    }

    /**
     * This method retrieves the remote host address as String.
     *
     * @return String - the host address that socket connection was requested to
     */
    public String getHost() {
        return this.host;
    }

    /**
     * This method retrieves the remote host address as a InetAddress object.
     *
     * @return InetAddress - the host address that socket connection was requested to
     */
    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    /**
     * This method retrieves the remote host port.
     *
     * @return int - the remote host port the connection was requested for
     */
    public int getPort() {
        return this.port;
    }

    /**
     * This method cleans up by closing socket and any socket input/output
     * streams that have been opened.
     *
     */
    public void close() {
        try {
            this.outToServer = null;
            this.inFromServer = null;
            if (null != socket) {
                this.socket.close();
                if (!this.socket.isClosed()) {
                    log.warn("Failed to close socket");
                }
            }
        } catch (Exception e) {
            log.error(e.getClass().getName() + " caught: " + e.getMessage());
        }
    }
}
