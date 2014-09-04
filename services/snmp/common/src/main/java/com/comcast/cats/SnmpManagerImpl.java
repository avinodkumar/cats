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

import static com.comcast.cats.service.WebServiceReturnEnum.FAILURE;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.comcast.cats.info.SetValueTypes;
import com.comcast.cats.info.SnmpServiceReturnEnum;
import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.info.SnmpWalkResult;

/**
 * Snmp4j specific implementation of the SnmpManager. Supports V1,V2 & V3
 * versions of SNMP get and set operations.
 *
 * @author TATA
 * @bemman01c : modified to add info log messages for set and get operations.
 * processResponse() method has been modified to handle SNMP errors.
 *
 */
public class SnmpManagerImpl implements SnmpManager
{
    /**
     * Holds the snmp engine boot up count for snmp V3.
     */
    private static final int DEFAULT_ENGINE_REBOOTS = 0;

    /**
     * The log4j logger instance for this class.
     */
    private static Logger    logger                 = LoggerFactory.getLogger( SnmpManagerImpl.class );

    /**
     * The snmp object that performs the underlying set and get operation.
     */
    private Snmp             snmp;

    /**
     * GET operation for V1 and V2 versions of SNMP messages.This will return
     * error message when oId or target IP is null and otherwise returns the
     * correct value.
     *
     * @param oId
     *            Object identifier representing the functionality
     * @param communityName
     *            Community Name (by default public)
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @return SnmpServiceReturnMesage represents the return value and status
     */
    @Override
    public SnmpServiceReturnMesage get( final String oId, String communityName, final String targetIP, int portNumber )
    {
        ResponseEvent responseEvent = null;
        final boolean isVersion3Message = false;
        SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpServiceReturnMesage();
        if ( ( null == oId ) || ( null == targetIP ) || ( !SnmpUtil.isValidIp( targetIP ) )
                || ( !SnmpUtil.isValidOid( oId ) ) )
        {
            snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT );
            snmpServiceReturnMessage.setResult( FAILURE );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Either IP or OID is invalid: IP=" + targetIP + ", OID=" + oId );
            }
        }
        else
        {
            try
            {
                // Set default community name and port number
                if ( null == communityName || communityName.isEmpty() )
                {
                    communityName = DEFAULT_COMMUNITY_NAME;
                }
                if ( portNumber <= INVALID_VALUE )
                {
                    portNumber = DEFAULT_PORT_NUMBER;
                }
                final Target target = setUpTarget( communityName, targetIP, portNumber );
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Sending SNMP V1/V2 GET request" );
                }
                final PDU pdu = createGetPDU( oId, isVersion3Message );
                responseEvent = snmp.get( pdu, target );
                snmp.close();
                snmpServiceReturnMessage = processResponse( responseEvent );
            }
            catch ( IOException e )
            {
                logger.error("{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
        }
        logger.info("Get request for OID"+oId+", target:"+targetIP+"  Result:"+snmpServiceReturnMessage.getResult()
        		+" Result message:"+snmpServiceReturnMessage.getMessage());
        return snmpServiceReturnMessage;
    }

    /**
     * GET operation for V3 version of SNMP messages.This will return error
     * message when oId or target IP or userName or authenticatePassword or
     * privacyPassword is null and otherwise returns the correct value.
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
     *            he privacy password
     * @return SnmpServiceReturnMesage represents the return value and status
     */
    @Override
    public SnmpServiceReturnMesage get( final String oId, final String targetIP, int portNumber, final String userName,
            final String authenticatePassword, final String privacyPassword )
    {
        ResponseEvent responseEvent = null;
        final boolean isVersion3Message = true;
        SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpServiceReturnMesage();

        if ( ( null == oId ) || ( null == targetIP ) || ( null == userName ) || ( null == authenticatePassword )
                || ( null == privacyPassword ) || ( userName.isEmpty() ) || ( privacyPassword.isEmpty() )
                || ( authenticatePassword.isEmpty() ) || ( !SnmpUtil.isValidIp( targetIP ) )
                || ( !SnmpUtil.isValidOid( oId ) ) || ( !SnmpUtil.isValidPassword( authenticatePassword ) )
                || ( !SnmpUtil.isValidPassword( privacyPassword ) ) )
        {
            snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT );
            snmpServiceReturnMessage.setResult( FAILURE );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "One of the input parameters is invalid: IP=" + targetIP + ", OID=" + oId
                        + ", User Name=" + userName + ", Authentication Password=" + authenticatePassword
                        + ", Privacy Password=" + privacyPassword );
            }
        }
        else
        {
            try
            {
                // Set port number
                if ( portNumber <= INVALID_VALUE )
                {
                    portNumber = DEFAULT_PORT_NUMBER;
                }

                final UserTarget userTarget = setUpTarget( targetIP, portNumber, userName, authenticatePassword,
                        privacyPassword );
                final PDU pdu = createGetPDU( oId, isVersion3Message );
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Sending SNMP V3 GET request" );
                }
                responseEvent = snmp.get( pdu, userTarget );
                snmp.close();
                snmpServiceReturnMessage = processResponse( responseEvent );
            }
            catch ( IOException e )
            {
                logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );

            }
        }
        logger.info("Get request for OID"+oId+", target:"+targetIP+"  Result:"+snmpServiceReturnMessage.getResult()
        		+" Result message:"+snmpServiceReturnMessage.getMessage());
        return snmpServiceReturnMessage;
    }
    
    @Override
    public SnmpServiceReturnMesage walk(final String oId, String communityName, final String targetIP, int portNumber) {
        logger.debug( " Walk request started for {} ", oId );
        ResponseEvent responseEvent = null;
        final boolean isVersion3Message = false;
        SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpServiceReturnMesage();
        if ( ( null == oId ) || ( null == targetIP ) || ( !SnmpUtil.isValidIp( targetIP ) )
                || ( !SnmpUtil.isValidOid( oId ) ) )
        {
            snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT );
            snmpServiceReturnMessage.setResult( FAILURE );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Either IP or OID is invalid: IP=" + targetIP + ", OID=" + oId );
            }
        }
        else
        {
            try
            {
                // Set default community name and port number
                if ( null == communityName || communityName.isEmpty() )
                {
                    communityName = DEFAULT_COMMUNITY_NAME;
                }
                if ( portNumber <= INVALID_VALUE )
                {
                    portNumber = DEFAULT_PORT_NUMBER;
                }
                final Target target = setUpTarget( communityName, targetIP, portNumber );
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Sending SNMP V1/V2 GET request" );
                }
                
                boolean isWalkComplete = false;
                PDU pdu = null;
                PDU responsePDU = null;
                pdu = createGetNextPDU(oId, isVersion3Message );
                VariableBinding vb = null;
                OID targetOID = new OID(oId);
                
                List<SnmpWalkResult> resultData = new ArrayList< SnmpWalkResult >(); 
                String message = "";
                
                while ( !isWalkComplete )
                {

                    responseEvent = snmp.getNext( pdu, target );
                    responsePDU = responseEvent.getResponse();
                    if ( responsePDU != null )
                    {
                        vb = responsePDU.get( 0 );
                    }

                    if ( responsePDU == null )
                    {
                        message = "Empty Response";
                        isWalkComplete = true;
                    }
                    else if ( responsePDU.getErrorStatus() != 0 )
                    {
                        message = responsePDU.getErrorStatusText();
                        isWalkComplete = true;
                    }
                    else if ( vb.getOid() == null )
                    {
                        message = "Empty OID";
                        isWalkComplete = true;
                    }
                    else if ( vb.getOid().size() < targetOID.size() )
                    {
                        message = "Response OID length shorter than target OID";
                        isWalkComplete = true;
                    }
                    else if ( targetOID.leftMostCompare( targetOID.size(), vb.getOid() ) != 0 )
                    {
                        message = "Base OID changes";
                        isWalkComplete = true;
                    }
                    else if ( Null.isExceptionSyntax( vb.getVariable().getSyntax() ) )
                    {
                        message = " Variable Syntax Error";
                        isWalkComplete = true;
                    }
                    else if ( vb.getOid().compareTo( targetOID ) <= 0 )
                    {
                        message = " Received variable is not successor of requested";
                        isWalkComplete = true;

                    }
                    else
                    {
                        SnmpWalkResult snmpWalkResult = new SnmpWalkResult();
                        snmpWalkResult.setOid( vb.getOid().toString() );
                        snmpWalkResult.setValue( vb.toValueString() );
                        resultData.add(snmpWalkResult );
                        // Reinitialise the PDU.
                        pdu.setRequestID( new Integer32( 0 ) );
                        pdu.set( 0, vb );
                    }

                }
                
                if ( null != resultData && !resultData.isEmpty() )
                {
                    snmpServiceReturnMessage.setComplexResultObject( resultData );
                }
                else 
                {
                    snmpServiceReturnMessage.setMessage( message );
                    snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                }
                snmp.close();
            }
            catch ( IOException e )
            {
                logger.error("{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
        }
        logger.info("Walk request for OID"+oId+", target:"+targetIP+"  Result:"+snmpServiceReturnMessage.getComplexResultObject()
                +" Result message:"+snmpServiceReturnMessage.getMessage());
        return snmpServiceReturnMessage;
    }

    /**
     * SET operation for V1 and V2 versions of SNMP messages.This will return
     * error message when oId or target IP is null otherwise returns the newly
     * set value and status of operation.
     *
     * @param oId
     *            Object identifier representing the functionality to be set.
     * @param communityName
     *            Community Name (by default public)
     * @param targetIP
     *            IP address of target machine
     * @param portNumber
     *            Port number (by default 161)
     * @param value
     *            Value to be set
     * @param type
     *            type to the value to be set
     * @return SnmpServiceReturnMesage represents the value that is set and
     *         status of operation
     */
    @Override
    public SnmpServiceReturnMesage set( final String oId, String communityName, final String targetIP, int portNumber,
            final String value, final String type )
    {
        ResponseEvent responseEvent = null;
        final boolean isVersion3Message = false;
        SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpServiceReturnMesage();

        if ( ( null == oId ) || ( null == targetIP ) || ( !SnmpUtil.isValidIp( targetIP ) )
                || ( !SnmpUtil.isValidOid( oId ) ) )
        {
            snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT );
            snmpServiceReturnMessage.setResult( FAILURE );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Either IP or OID is invalid: IP=" + targetIP + ", OID=" + oId );
            }
        }
        else
        {
            try
            {
                // Set default community name and port number
                if ( null == communityName || communityName.isEmpty() )
                {
                    communityName = DEFAULT_COMMUNITY_NAME;
                }
                if ( portNumber <= INVALID_VALUE )
                {
                    portNumber = DEFAULT_PORT_NUMBER;
                }
                final Target target = setUpTarget( communityName, targetIP, portNumber );
                final PDU pdu = createSetPDU( oId, value, type, isVersion3Message );
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Sending SNMP V1/V2 SET request" );
                }
                responseEvent = snmp.send( pdu, target );
                snmp.close();
                snmpServiceReturnMessage = processResponse( responseEvent );
            }
            catch ( NumberFormatException e )
            {
                logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
            catch ( IllegalArgumentException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
            catch ( UnknownHostException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
            catch ( IOException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
        }
        logger.info("Set request for OID"+oId+", target ip:"+targetIP+"value"+value+", Result:"+snmpServiceReturnMessage.getResult()
        		+" Result message:"+snmpServiceReturnMessage.getMessage());
        return snmpServiceReturnMessage;
    }

    /**
     * SET operation for V3 version of SNMP messages.This will return error
     * message when oId or target IP or userName or authenticatePassword or
     * privacyPassword is null otherwise returns the newly set value and status
     * of operation.
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
     * @return SnmpServiceReturnMesage represents the value that is set and
     *         status of operation
     */
    @Override
    public SnmpServiceReturnMesage set( final String oId, final String targetIP, int portNumber, final String value,
            final String type, final String userName, final String authenticatePassword, final String privacyPassword )
    {
        ResponseEvent responseEvent = null;
        final boolean isVersion3Message = true;
        SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpServiceReturnMesage();

        if ( ( null == oId ) || ( null == targetIP ) || ( null == userName ) || ( null == authenticatePassword )
                || ( null == privacyPassword ) || ( userName.isEmpty() ) || ( privacyPassword.isEmpty() )
                || ( authenticatePassword.isEmpty() ) || ( !SnmpUtil.isValidIp( targetIP ) )
                || ( !SnmpUtil.isValidOid( oId ) ) || ( !SnmpUtil.isValidPassword( authenticatePassword ) )
                || ( !SnmpUtil.isValidPassword( privacyPassword ) ) )
        {
            snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT );
            snmpServiceReturnMessage.setResult( FAILURE );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "One of the input parameters is invalid: IP=" + targetIP + ", OID=" + oId
                        + ", User Name=" + userName + ", Authentication Password=" + authenticatePassword
                        + ", Privacy Password=" + privacyPassword );
            }
        }
        else
        {
            try
            {
                // Set port number
                if ( portNumber <= INVALID_VALUE )
                {
                    portNumber = DEFAULT_PORT_NUMBER;
                }
                final UserTarget userTarget = setUpTarget( targetIP, portNumber, userName, authenticatePassword,
                        privacyPassword );
                final PDU pdu = createSetPDU( oId, value, type, isVersion3Message );
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Sending SNMP V3 SET request" );
                }
                responseEvent = snmp.send( pdu, userTarget );
                snmp.close();
                snmpServiceReturnMessage = processResponse( responseEvent );
            }
            catch ( NumberFormatException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
            catch ( IllegalArgumentException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
            catch ( UnknownHostException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
            catch ( IOException e )
            {
            	logger.error( "{}", e );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( e.getMessage() );
            }
        }
        logger.info("Set request for OID"+oId+", target ip:"+targetIP+"value"+value+", Result:"+snmpServiceReturnMessage.getResult()
        		+" Result message:"+snmpServiceReturnMessage.getMessage());
        return snmpServiceReturnMessage;
    }

    /**
     * Helper method that initializes the snmp target to listening mode. This
     * method is explicitly for V1 and V2 messages. This method will create and
     * set up the TransportMapping and target for SNMP V1 and V2. It creates a
     * TransportMapping and puts it in the listening mode. Also Creates
     * CommunityTarget object and sets SNMP target properties.
     *
     * @param communityName
     *            Community name of the target
     * @param targetIP
     *            IP address of Target machine
     * @param portNumber
     *            Port number
     * @return The Target created
     * @throws IOException
     *             IOException
     */
    private Target setUpTarget( final String communityName, final String targetIP, final int portNumber )
            throws IOException
    {
        final InetAddress inetAddress = InetAddress.getByName( targetIP );
        final Address address = new UdpAddress( inetAddress, portNumber );
        final OctetString community = new OctetString( communityName );
        final TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp( transport );
        snmp.listen();

        // Creating the communityTarget object and setting its properties
        final CommunityTarget communityTarget = new CommunityTarget();
        communityTarget.setCommunity( community );
        // TODO Needs to check also for v2 messages
        communityTarget.setVersion( SnmpConstants.version1 );
        communityTarget.setAddress( address );
        // TODO Need to confirm, whether this value needs to be configures
        communityTarget.setRetries( SnmpManager.DEFAULT_RETRIES );
        // TODO Need to confirm, whether this value needs to be configures
        communityTarget.setTimeout( SnmpManager.DEFAULT_TIMEOUT );
        return communityTarget;
    }

    /**
     * Helper method that initializes the snmp target to listening mode. This
     * method is explicitly for V3 messages. This method will create and set up
     * the TransportMapping and target for SNMP V3. It creates a
     * TransportMapping and puts it in the listening mode. Also Creates
     * CommunityTarget object and sets SNMP target properties.
     *
     * @param targetIP
     *            IP address of Target machine
     * @param portNumber
     *            Port number
     * @param userName
     *            The security name of the user
     * @param authenticatePassword
     *            The authentication password
     * @param privacyPassword
     *            The privacy password
     * @return The created UserTarget
     * @throws IOException
     *             IOException
     */
    private UserTarget setUpTarget( final String targetIP, final int portNumber, final String userName,
            final String authenticatePassword, final String privacyPassword ) throws IOException
    {

        // Creates a TransportMapping and puts the transport mapping in to
        // listen mode
        final TransportMapping transportMapping = new DefaultUdpTransportMapping();
        snmp = new Snmp( transportMapping );
        transportMapping.listen();

        // Creating a USM with the support for the supplied security protocols
        final SecurityProtocols securityProtocols = SecurityProtocols.getInstance();
        securityProtocols.addDefaultProtocols();
        final OctetString engineId = new OctetString( MPv3.createLocalEngineID() );
        final USM usm = new USM( securityProtocols, engineId, DEFAULT_ENGINE_REBOOTS );
        SecurityModels.getInstance().addSecurityModel( usm );
        final OctetString username = new OctetString( userName );
        final OctetString authenticationPassphrase = new OctetString( authenticatePassword );
        final OctetString privacyPassphrase = new OctetString( privacyPassword );

        // Creating UsmUser and adds the UsmUser to the internal user name table
        // TODO Need to confirm, whether AuthMD5 and PrivDES needs to be changed
        final UsmUser usmuser = new UsmUser( username, AuthMD5.ID, authenticationPassphrase, PrivDES.ID,
                privacyPassphrase );
        snmp.getUSM().addUser( username, usmuser );

        // Create a target for a user based security model target and setting
        // its properties
        final UserTarget userTarget = new UserTarget();
        final InetAddress inetAddress = InetAddress.getByName( targetIP );
        final Address address = new UdpAddress( inetAddress, portNumber );
        userTarget.setAddress( address );
        // TODO Need to confirm, whether this value needs to be configures
        userTarget.setRetries( SnmpManager.DEFAULT_RETRIES );
        // TODO Need to confirm, whether this value needs to be configures
        userTarget.setTimeout( SnmpManager.DEFAULT_TIMEOUT );
        userTarget.setVersion( SnmpConstants.version3 );
        // TODO Need to confirm, whether this value needs to be configures
        userTarget.setSecurityLevel( SecurityLevel.AUTH_PRIV );
        userTarget.setSecurityName( username );

        return userTarget;
    }

    /**
     *
     * This method is creating and returning a GET PDU. This method creates
     * ScopedPDU object if the SNMP Version is 3 else PDU object and sets the
     * PDU type as GET. It also creates a VariableBinding with the supplied
     * Object Identifier and a Null value, and adding it to the PDU.
     *
     * @param oId
     *            Object identifier
     * @param isSNMPV3
     *            True for SNMP Version 3 else false
     * @return The created GET PDU
     * @throws IOException
     *             IOException
     */
    private PDU createGetPDU( final String oId, final boolean isSNMPV3 ) throws IOException
    {
        PDU pdu = null;
        final OID oid = new OID( oId );
        if ( isSNMPV3 )
        {
            pdu = new ScopedPDU();
        }
        else
        {
            pdu = new PDU();
        }

        pdu.setType( PDU.GET );
        final VariableBinding varBinding = new VariableBinding( oid );
        pdu.add( varBinding );

        return pdu;
    }
    
    /**
    *
    * This method is creating and returning a GET PDU. This method creates
    * ScopedPDU object if the SNMP Version is 3 else PDU object and sets the
    * PDU type as GET. It also creates a VariableBinding with the supplied
    * Object Identifier and a Null value, and adding it to the PDU.
    *
    * @param oId
    *            Object identifier
    * @param isSNMPV3
    *            True for SNMP Version 3 else false
    * @return The created GET PDU
    * @throws IOException
    *             IOException
    */
   private PDU createGetNextPDU( final String oId, final boolean isSNMPV3 ) throws IOException
   {
       PDU pdu = null;
       final OID oid = new OID( oId );
       if ( isSNMPV3 )
       {
           pdu = new ScopedPDU();
       }
       else
       {
           pdu = new PDU();
       }

       pdu.setType( PDU.GETNEXT );
       final VariableBinding varBinding = new VariableBinding( oid );
       pdu.add( varBinding );

       return pdu;
   }


    /**
     * This method is creating and returning a SET PDU. It creates ScopedPDU
     * object if the SNMP Version is 3 else PDU object and sets the PDU type as
     * SET. It also creates a VariableBinding with the supplied Object
     * Identifier and value,depending on the type of the value to be set and
     * adding it to the PDU.
     *
     * @param oId
     *            Object identifier
     * @param value
     *            The value to be set
     * @param type
     *            The Type of the value to be set as per rfc2570
     *            [INTEGER,COUNTER32
     *            ,COUNTER64,GAUGE32,UNSIGNEDINTEGER32,TIMETICKS,
     *            OID,IPADDRESS,TCPADDRESS,UDPADDRESS,STRING]
     * @param isSNMPV3
     *            True for SNMP Version 3 else false
     * @return The created SET PDU
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private PDU createSetPDU( final String oId, final String value, final String type, final boolean isSNMPV3 )
            throws IOException
    {
        VariableBinding varBinding = null;
        PDU pdu = null;
        final OID oid = new OID( oId );
        final Long valuetoset;
        // Taking the value of the type of the value to be set as enum constant
        // for each type the value will be bind to the OID in different manner
        SetValueTypes setValueType = SetValueTypes.valueOf( type.toUpperCase() );
        switch ( setValueType )
        {
        case INTEGER:
            final Integer intValue = Integer.parseInt( value );
            final Integer32 setInteger32Value = new Integer32( intValue );
            varBinding = new VariableBinding( oid, setInteger32Value );
            break;
        case COUNTER32:
            valuetoset = Long.parseLong( value );
            final Counter32 setCounter32Value = new Counter32( valuetoset );
            varBinding = new VariableBinding( oid, setCounter32Value );
            break;
        case COUNTER64:
            valuetoset = Long.parseLong( value );
            final Counter64 setCounter64Value = new Counter64( valuetoset );
            varBinding = new VariableBinding( oid, setCounter64Value );
            break;
        case GAUGE32:
            valuetoset = Long.parseLong( value );
            final Gauge32 setGauge32Value = new Gauge32( valuetoset );
            varBinding = new VariableBinding( oid, setGauge32Value );
            break;
        case UNSIGNEDINTEGER32:
            valuetoset = Long.parseLong( value );
            final UnsignedInteger32 setUnsignedInteger32Value = new UnsignedInteger32( valuetoset );
            varBinding = new VariableBinding( oid, setUnsignedInteger32Value );
            break;
        case TIMETICKS:
            valuetoset = Long.parseLong( value );
            final TimeTicks setTimeTicksValue = new TimeTicks( valuetoset );
            varBinding = new VariableBinding( oid, setTimeTicksValue );
            break;
        case STRING:
            final OctetString setValue = new OctetString( value );
            varBinding = new VariableBinding( oid, setValue );
            break;
        case OID:
            final OID setOIDValue = new OID( value );
            varBinding = new VariableBinding( oid, setOIDValue );
            break;
        case IPADDRESS:
            final IpAddress setIpAddressValue = new IpAddress( value );
            varBinding = new VariableBinding( oid, setIpAddressValue );
            break;
        case TCPADDRESS:
            final TcpAddress setTcpAddressValue = new TcpAddress( value );
            varBinding = new VariableBinding( oid, setTcpAddressValue );
            break;
        case UDPADDRESS:
            final UdpAddress setUdpAddressValue = new UdpAddress( value );
            varBinding = new VariableBinding( oid, setUdpAddressValue );
            break;
        default:
            final OctetString setdefaultValue = new OctetString( value );
            varBinding = new VariableBinding( oid, setdefaultValue );
            break;
        }
        if ( isSNMPV3 )
        {
            pdu = new ScopedPDU();
        }
        else
        {
            pdu = new PDU();
        }

        pdu.setType( PDU.SET );
        pdu.add( varBinding );

        return pdu;
    }

    /**
     * Helper method for creating SnmpServiceReturnMesage from the response got
     * from the snmp device. This method will process the ResponseEvent and
     * returns the SnmpServiceReturnMesage object with the operation result or
     * appropriate error information. The ResponseEvent will be null if the sent
     * PDU is an unconfirmed PDU(notification, response, or report) and
     * ResponseEvent.getResponse() will be null if the request is timed out.
     *
     * @param responseEvent
     *            ResponseEvent got from the snmp set/get operations
     * @return SnmpServiceReturnMesage Represents the status of operation
     */
    private SnmpServiceReturnMesage processResponse( final ResponseEvent responseEvent )
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpServiceReturnMesage();
        boolean isResponded = false;
        if ( null != responseEvent )
        {
            final PDU resultPdu = responseEvent.getResponse();
            if ( null != resultPdu )
            {
            	//check if there is any error in the SNMP operation and return error immediately
            	if(resultPdu.getErrorStatus()!=SnmpConstants.SNMP_ERROR_SUCCESS){
            		snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
                    snmpServiceReturnMessage.setResult( FAILURE );
                    snmpServiceReturnMessage.setSnmpErrorCode(resultPdu.getErrorStatus());
                    snmpServiceReturnMessage.setMessage( resultPdu.getErrorStatusText());
                    //NOTE: if there is an error we will return from here.
                    return snmpServiceReturnMessage;
            	}
                final Vector< VariableBinding > variableBindings = resultPdu.getVariableBindings();
				if ((null != variableBindings) && !variableBindings.isEmpty()) {
					// Getting the first VariableBinding, since we are attaching
					// a single VariableBinding with the set/get PDU.
					final VariableBinding varBinding = variableBindings
							.firstElement();
					// Getting the variable value associated with the OID
					if (varBinding != null) {							
						String result = "";						
						switch (varBinding.getSyntax()) {
						case SMIConstants.SYNTAX_TIMETICKS:	
							result += varBinding.getVariable().toLong();
							logger.debug(result);
							break;
						default:
							result = varBinding.getVariable().toString();
							break;
						}

						snmpServiceReturnMessage.setResultObject(result);
						isResponded = true;
					}
				}
				if (!isResponded) {
					snmpServiceReturnMessage
							.setServiceCode(SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE);
					snmpServiceReturnMessage.setResult(FAILURE);
					snmpServiceReturnMessage
							.setMessage("Response received from device is not valid");
					snmpServiceReturnMessage
							.setSnmpErrorCode(SnmpConstants.SNMP_ERROR_GENERAL_ERROR);

				}
            }
            else
            {
                logger.error( "Request timed out" );
                snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_TIME_OUT );
                snmpServiceReturnMessage.setResult( FAILURE );
                snmpServiceReturnMessage.setMessage( "Request timed out" );
                snmpServiceReturnMessage.setSnmpErrorCode(SnmpConstants.SNMP_ERROR_GENERAL_ERROR);

            }
        }
        else
        {
            logger.error( "Invalid PDU" );
            snmpServiceReturnMessage.setServiceCode( SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE );
            snmpServiceReturnMessage.setResult( FAILURE );
            snmpServiceReturnMessage.setMessage( "Invalid PDU" );
            snmpServiceReturnMessage.setSnmpErrorCode(SnmpConstants.SNMP_ERROR_GENERAL_ERROR);

        }
        return snmpServiceReturnMessage;
    }
    
}
