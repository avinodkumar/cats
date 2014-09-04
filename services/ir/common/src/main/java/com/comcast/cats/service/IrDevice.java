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
package com.comcast.cats.service;

import java.util.List;

/**
 * Represents a IR Device like GC100 or RedRat irNetBoxPro etc.
 * 
 * @author skurup00c
 * 
 */
public interface IrDevice
{
    /**
     * Get device Id.
     * 
     * @return id;
     */
    public long getId();

    /**
     * Get a list of all {@link IRPort} on this device.
     * 
     * @return list of all ports.
     */
    public List< IrPort > getIrPorts();

    /**
     * Get a port corresponding to the port number.
     * 
     * @param portNumber
     * 
     * @return the {@link IRPort}
     */
    public IrPort getPort( int portNumber );

    /**
     * Send a command to this IRDevice.
     * 
     * @param command
     * 
     * @return the response of the command sent.
     */
    public String sendCommand( String command );
}
