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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.comcast.cats.service.exceptions.SNMPException;

/**
 * CATS SNMP service that can perform basic GET and SET operations on a managed
 * device.
 * 
 * @author TATA
 */
@WebService
public interface SnmpCoreService extends CatsWebService
{
    /**
     * Method to perform basic GET operation on a managed device.
     * 
     * @param oId
     *            Object identifier
     * @param communityName
     *            Community name
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number
     * @param userName
     *            The security name of the user. Optional, applicable to SNMP
     *            version 3 only.
     * @param authenticatePassword
     *            The authentication password. Optional, applicable to SNMP
     *            version 3 only.
     * @param privacyPassword
     *            The privacy password. Optional, applicable to SNMP version 3
     *            only.
     * @return The value corresponding to the Object Identifier
     * @throws SNMPException
     */
    @WebMethod
    String get( @WebParam( name = "objectId" ) final String oId,
            @WebParam( name = "communityName" ) final String communityName,
            @WebParam( name = "targetIP" ) final String targetIP,
            @WebParam( name = "portNumber" ) final int portNumber,
            @WebParam( name = "userName" ) final String userName,
            @WebParam( name = "authenticatePassword" ) final String authenticatePassword,
            @WebParam( name = "privacyPassword" ) final String privacyPassword ) throws SNMPException;

    /**
     * Method to perform basic SET operation on a managed device.
     * 
     * @param oId
     *            Object identifier
     * @param communityName
     *            Community name
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number
     * @param value
     *            Value to be set
     * @param type
     *            type to the value to be set
     * @param userName
     *            The security name of the user. Optional, applicable to SNMP
     *            version 3 only.
     * @param authenticatePassword
     *            The authentication password. Optional, applicable to SNMP
     *            version 3 only.
     * @param privacyPassword
     *            The privacy password. Optional, applicable to SNMP version 3
     *            only.
     * @throws SNMPException
     */
    @WebMethod
    void set( @WebParam( name = "objectId" ) final String oId,
            @WebParam( name = "communityName" ) final String communityName,
            @WebParam( name = "targetIP" ) final String targetIP,
            @WebParam( name = "portNumber" ) final int portNumber, @WebParam( name = "value" ) final String value,
            @WebParam( name = "type" ) final String type, @WebParam( name = "userName" ) final String userName,
            @WebParam( name = "authenticatePassword" ) final String authenticatePassword,
            @WebParam( name = "privacyPassword" ) final String privacyPassword ) throws SNMPException;
}
