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

/**
 * Class for Cable Card details
 * 
 * @author cfrede001
 */
public interface CableCardInfo
{

    /**
     * To get the Cable card serial number
     * 
     * @return String
     */
    public String getCableCardSerialNumber();

    /**
     * To get the Cable card Unit Address
     * 
     * @return String
     */
    public String getCableCardUnitAddress();

    /**
     * To get the Cable card Inet Address
     * 
     * @return InetAddress
     */
    public InetAddress getCableCardIpAddress();

    /**
     * To get the Cable card Inet4 Address
     * 
     * @return Inet4Address
     */
    public Inet4Address getCableCardIp4Address();

    /**
     * To get the Cable card Ip6 address
     * 
     * @return Inet6Address
     */
    public Inet6Address getCableCardIp6Address();
}
