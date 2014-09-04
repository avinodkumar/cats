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
/**
 * @author TATA
 * enum declaration for data types that can be used for SNMP SET.
 *
 */
public enum SetValueTypes
{
    /**
     * Snmp Integer data type.
     */
    INTEGER("Integer"),
    /**
     *  Snmp 32-bit counter data type.
     */
    COUNTER32("Counter32"),
    /**
     *  Snmp 64-bit counter data type.
     */
    COUNTER64("Counter64"),
    /**
     *  Snmp 32-bit gauge data type. Which represents a 64bit unsigned integer type.
     */
    GAUGE32("Gauge32"),
    /**
     * Snmp unsigned 32-bit integer data type.
     */
    UNSIGNEDINTEGER32("UnsignedInteger32"),
    /**
     * Snmp time ticks data type. It represents the time in 1/100 seconds since some epoch.
     */
    TIMETICKS("TimeTicks"),
    /**
     * String data type.
     */
    STRING("String"),
    /**
     * Snmp object identifier data type.
     */
    OID("OID"),
    /**
     * Snmp ip address data type.
     */
    IPADDRESS("IpAddress"),
    /**
     * TCP/IP transport address format.
     */
    TCPADDRESS("TcpAddress"),
    /**
     * The UDP/IP transport address format.
     */
    UDPADDRESS("UdpAddress");

    /**
     * The variable holding value of the Enum constant.
     */
    private final String value;

    /**
     * The constructor for creating the Enum constants.
     * @param value The value of Enum constant.
     */
    private SetValueTypes(final String value)
    {
        this.value = value;
    }
}
