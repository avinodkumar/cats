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

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.SnmpManager;
import com.comcast.cats.SnmpManagerImpl;
import com.comcast.cats.info.SnmpServiceConstants;
import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.SnmpService;

/**
 * Session Bean implementation of SnmpService.
 * 
 * @author TATA
 */
//FIXME: This should talk to SnmpCoreService
@Remote( SnmpService.class )
@Stateless
@WebService( name = SnmpServiceConstants.SNMP_SERVICE_NAME, portName = SnmpServiceConstants.SNMP_SERVICE_PORT_NAME, targetNamespace = SnmpServiceConstants.SNMP_SERVICE_NAMESPACE, endpointInterface = SnmpServiceConstants.SNMP_SERVICE_ENDPOINT_INTERFACE )
public class SnmpServiceImpl implements SnmpService
{
    /**
     * The log4j logger instance for this class.
     */
    private static Logger logger      = LoggerFactory.getLogger( SnmpService.class );
    /**
     * Instantiating the class SnmpManagerImpl.
     */
    private SnmpManager   snmpManager = new SnmpManagerImpl();

    @Override
    public SnmpServiceReturnMesage get( final String oId, final String communityName, final String targetIP,
            final int portNumber, final String userName, final String authenticatePassword, final String privacyPassword )
    {
        SnmpServiceReturnMesage result = null;

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "SnmpService Thread Id = " + Long.toString( Thread.currentThread().getId() ) );
        }

        if ( ( null == userName ) || ( null == authenticatePassword ) || ( null == privacyPassword )
                || ( userName.isEmpty() ) || ( authenticatePassword.isEmpty() ) || ( privacyPassword.isEmpty() ) )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "SNMP V2/V2 GET: oId=" + oId + ", targetIP=" + targetIP + ", portNumber=" + portNumber
                        + ", communityName=" + communityName );
            }
            result = snmpManager.get( oId, communityName, targetIP, portNumber );
        }
        else
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "SNMP V3 GET: userName=" + userName + ", oId=" + oId + ", targetIP=" + targetIP
                        + ", portNumber=" + portNumber + ", communityName=" + communityName );
            }
            result = snmpManager.get( oId, targetIP, portNumber, userName, authenticatePassword, privacyPassword );
        }
        return result;
    }

    @Override
    public SnmpServiceReturnMesage set( final String oId, final String communityName, final String targetIP,
            final int portNumber, final String value, final String type, final String userName,
            final String authenticatePassword, final String privacyPassword )
    {
        SnmpServiceReturnMesage result = null;

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "SnmpService Thread Id = " + Long.toString( Thread.currentThread().getId() ) );
        }

        if ( ( null == userName ) || ( null == authenticatePassword ) || ( null == privacyPassword )
                || ( userName.isEmpty() ) || ( authenticatePassword.isEmpty() ) || ( privacyPassword.isEmpty() ) )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "SNMP V1/V2 SET: oId=" + oId + ", targetIP=" + targetIP + ", portNumber=" + portNumber
                        + ", communityName=" + communityName + ", value=" + value );
            }
            result = snmpManager.set( oId, communityName, targetIP, portNumber, value, type );
        }
        else
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "SNMP V3 SET: userName=" + userName + ", oId=" + oId + ", targetIP=" + targetIP
                        + ", portNumber=" + portNumber + ", communityName=" + communityName + ", value=" + value );
            }
            result = snmpManager.set( oId, targetIP, portNumber, value, type, userName, authenticatePassword,
                    privacyPassword );
        }
        return result;
    }
}
