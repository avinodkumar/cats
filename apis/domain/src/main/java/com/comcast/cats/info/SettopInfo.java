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
package com.comcast.cats.info;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.Map;

import com.comcast.cats.domain.Controller;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.RFPlant;

/**
 * Settop information interface.
 * 
 * @author cfrede001
 */
public interface SettopInfo
{
    public String getId();

    public String getName();

    public String getHostIpAddress();

    public String getHostIp4Address();

    public String getHostIp6Address();

    public InetAddress getHostIpInetAddress();

    public Inet4Address getHostIp4InetAddress();

    public Inet6Address getHostIp6InetAddress();

    public String getHostMacAddress();

    public String getMcardMacAddress();

    public String getManufacturer();

    public String getModel();

    public String getMake();

    public String getFirmwareVersion();

    public String getSerialNumber();

    public String getMCardSerialNumber();

    public String getUnitAddress();

    public String getHardwareRevision();

    public String getRemoteType();

    public String getContent();

    /**
     * Example: wti1600://<ip>:<port>/?outlet=x
     * 
     * @return
     */
    public URI getPowerPath();

    /**
     * 
     * Examples: gc100-12://<ip>:<port>/?port=x gc100-6://<ip>:<port>/?port=x
     * gc100://<ip>:<port>/?port=x itach://<ip>:<port>/?port=x
     * 
     * @return
     */
    public URI getRemotePath();

    /**
     * 
     * Example: For the chromamxx device we're using the left and right RCA
     * connectors independently, so that must be specified. In this case if no
     * connector was specified both would be used.
     * chromamxx://<ip>:<port>/?port=3&connector=L
     * chromamxx://<ip>:<port>/?port=3&connector=R
     * 
     * @return
     */
    public URI getAudioPath();

    /**
     * Place holder for clickstream.
     * 
     * @return
     */
    public URI getClickstreamPath();

    /**
     * Example: When utilizing the existing TraceServer.
     * traceserver://<ip>:<port>/ General trace information about serial
     * information.
     * trace://<ip>:<port>/?port=COM1&rate=115200&data=8&parity=none
     * &stop=1&flow=none
     * 
     * @return
     */
    public URI getTracePath();

    /**
     * Example: axis://<ip>:<port>/?camera=1&version=
     * 
     * @return
     */
    public URI getVideoPath();

    /**
     * Example: This is currently being implemented using a GC100 device.
     * gc100://<ip>:<port>/?port=x
     * 
     * @return
     */
    public URI getVideoSelectionPath();

    /**
     * Place holder for specifying which Admin server this settop is
     * communicating with.
     * 
     * @return
     */
    public URI getClusterPath();

    /**
     * @return All properties we failed to consider when defining this class. It
     *         is used for properties that people in test want to associate with
     *         a settop, but that we have not accounted for in advance.
     */
    public Map< String, String > getExtraProperties();

    /**
     * Find the extra property value for the given key. The find routine should
     * ignore whitespace and be case insensitive when searching against the list
     * of extra properties.
     * 
     * @param key
     * @return - Value corresponding to key or null otherwise.
     */
    public String findExtraProperty( String key );

    public String getRackId();

    public String getEnvironmentId();

    public RFPlant getRFPlant();

    public Controller getController();

    /**
     * Get the hardware device for a type that is connected to this settop.
     * 
     * @param hardwarePurpose
     * @return the HardwareInterface that is connected to this settop.
     */
    public HardwareInterface getHardwareInterfaceByType( HardwarePurpose hardwarePurpose );
}
