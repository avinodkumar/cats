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
package com.comcast.cats;

import com.comcast.cats.info.SnmpServiceReturnMesage;

/**
 * An interface defining commands available for interaction with the SNMP
 * devices.
 *
 * @author TATA
 *
 */
public interface SnmpManager
{
    /**
     * Holding the default port number 161.
     */
    int    DEFAULT_PORT_NUMBER    = 161;
    /**
     * Holding the default retries 1.
     */
    int    DEFAULT_RETRIES        = 1;
    /**
     * Holding the default time out 1000.
     */
    int    DEFAULT_TIMEOUT        = 1000;
    /**
     * Holding the invalid port number value -1.
     */
    int    INVALID_VALUE          = -1;
    /**
     * Holding the default community name public.
     */
    String DEFAULT_COMMUNITY_NAME = "public";

    /**
     * GET operation for V1 and V2 versions of SNMP messages.
     * Retrieve the value of an SNMP object specified by the oId from an SNMP V1/V2 Agent.
     * @param oId
     *            Object identifier representing the functionality
     * @param communityName
     *            Community Name (by default public)
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @return SnmpServiceReturnMesage
     *            represents the return value and status
     */
    SnmpServiceReturnMesage get(String oId, String communityName, String targetIP, int portNumber);

    /**
     * GET operation for V3 version of SNMP messages.
     * Retrieve the value of an SNMP object specified by the oId from an SNMP V3 Agent.
     *
     * @param oId
     *            Object identifier representing the functionality.
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @param userName
     *            the security name of the user
     * @param authenticatePassword
     *            the authentication password
     * @param privacyPassword
     *            the privacy password
     * @return SnmpServiceReturnMesage
     *            represents the return value and status
     */
    SnmpServiceReturnMesage get(String oId, String targetIP, int portNumber,
            String userName, String authenticatePassword, String privacyPassword);

    /**
     * SET operation for V1 and V2 versions of SNMP messages.
     * Sets the value of an SNMP object specified by the oId from an SNMP V1/V2 Agent.
     *
     * @param oId
     *            Object identifier representing the functionality to be set.
     * @param communityName
     *             Community Name (by default public)
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @param value
     *            Value to be set
     * @param type
     *            type to the value to be set
     * @return SnmpServiceReturnMesage
     *            represents the value that is set and status of operation
     */
    SnmpServiceReturnMesage set(String oId, String communityName, String targetIP, int portNumber, String value, String type);

    /**
     * SET operation for V3 version of SNMP messages.
     * Sets the value of an SNMP object specified by the oId from an SNMP V3
     * Agent.
     *
     * @param oId
     *            Object identifier representing the functionality to be set.
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @param value
     *            Value to be set
     * @param type
     *            type to the value to be set
     * @param userName
     *            the security name of the user
     * @param authenticatePassword
     *            the authentication password
     * @param privacyPassword
     *            the privacy password
     * @return SnmpServiceReturnMesage
     *            represents the value that is set and status of operation
     */
    SnmpServiceReturnMesage set(String oId, String targetIP, int portNumber, String value, String type,
            String userName, String authenticatePassword, String privacyPassword);

    /**
     * Performs a V1/V2c snmpWalk. The result will be available by
     * SnmpServiceReturnMessage.getComplexResult(). Sample output in XML is
     * 
     * <pre>
     * 
     * <snmpServiceReturnMesage>
     *     <resultCode>SUCCESS</resultCode>
     *     <result>1.3.6.1.2.1.2.2.1.6.1 = 54:d4:6f:7e:06:62</result>
     *     <result>1.3.6.1.2.1.2.2.1.6.3 = 56:d4:6f:7e:06:62</result>
     *     <result>1.3.6.1.2.1.2.2.1.6.7 = </result>
     *     <serviceCode>SNMP_SERVICE_SUCCESS</serviceCode>
     * </snmpServiceReturnMesage>
     * 
     * </pre>
     * @param oId
     *            Object identifier representing the functionality
     * @param communityName
     *            Community Name (by default public)
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @return SnmpServiceReturnMesage
     *            represents the return value and status
     */
    SnmpServiceReturnMesage walk(String oId, String communityName, String targetIP, int portNumber);
}
