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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerDeviceConnectionNP16S extends PowerDeviceConnection {
    private final Logger log = LoggerFactory.getLogger(PowerDeviceConnectionNP16S.class);

    public PowerDeviceConnectionNP16S(String host, int port) {
        super(host, port);
    }

    /**
     * This method reads a character at a time from the socket input stream.  The
     * input stream "ready" method is used to work around issues reading data
     * from the new NP-16(S) NetBooter devices.
     *
     * The read never returns -1 so the only way to break out of the while loop
     * is via time out.  The socket is always ready for reading, maybe because
     * NetBooter never flushes the output stream.
     *
     * @param timeout - amount of time to block in milliseconds before returning
     * @return String - the string that it read from the socket or an empty string
     */
    @Override
    public String read(final int timeout) {
        StringBuilder buff = new StringBuilder();
        setSoTimeout(timeout);
        try {
            int c;
            while (inFromServer.ready()) {
                c = inFromServer.read();
                if (-1 == c) {
                    break;
                }
                buff.append((char) c);
            }
        } catch (java.net.SocketTimeoutException ste) {
            log.debug("SocketTimeoutException (likely expected): " + ste.getMessage());
        } catch (IOException ioe) {
            log.error("IOException: " + ioe.getMessage());
        }
        return buff.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String waitForString(String string, final int timeout) {
        StringBuilder buff = new StringBuilder();
        setSoTimeout(timeout);
        try {
            int c;
            while (inFromServer.ready()) {
                c = inFromServer.read();
                if (-1 == c) {
                    break;
                }
                buff.append((char) c);
                if (buff.indexOf(string) != -1) {
                    break;
                }
            }
        } catch (java.net.SocketTimeoutException ste) {
            log.error(ste.getClass().getName() + " caught: " + ste.getMessage());
        } catch (IOException ioe) {
            log.error(ioe.getClass().getName() + " caught: " + ioe.getMessage());
        }
        return buff.toString();
    }
}
