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

import static com.comcast.cats.info.SnmpServiceConstants.OID_TYPE_INTEGER;
import static com.comcast.cats.info.SnmpServiceConstants.SNMP_CORE_SERVICE_MAPPED_NAME;
import static com.comcast.cats.service.power.util.PowerConstants.OID_PLUG_ACTION;
import static com.comcast.cats.service.power.util.PowerConstants.OID_PLUG_STATUS;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON_SNMP_VALUE;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.SnmpCoreService;
import com.comcast.cats.service.exceptions.SNMPException;

/**
 * This is the implementation class for NPS 1600 using SNMP commands. This
 * implementation uses the SNMP v1/v2c.
 * 
 * This class is no longer used because of the SNMP JNDI look up issue.
 * NpsSnmpPowerDeviceRestImpl is used instead of this.
 * 
 * @author bemman01c
 * 
 */

public class WTI_NPS_1600_SNMPPowerDevice extends NpsSnmpAbstractPowerDevice
{
    private static SnmpCoreService snmpCoreService = null;
    /**
     * TODO could not get EJB injection to work. Need further analysis
     */
    // @EJB(mappedName= SNMP_SERVICE_MAPPED_NAME)
    // SnmpService snmpService;

    /** End of CONSTANT definition **/

    /**
     * Logger
     */
    private static final Logger    log             = LoggerFactory.getLogger( WTI_NPS_1600_SNMPPowerDevice.class );
    /**
     * ip address of the WTI_NPS power device to be controlled
     */
    private String                 host;
    /**
     * The port to which SNMP commands are to be send. Usually this is the SNMP
     * port 161
     * 
     */
    private Integer                port;

    /**
     * The read community string that is to be used for the SNMP operation. By
     * default this would be "public".
     */
    private String                 readCommunityString;
    /**
     * The read write community string that is to be used for the SNMP. By
     * default this would be "public".
     */
    private String                 readWriteCommunityString;

    static
    {
        try
        {
            log.trace( "Inside WTI_NPS_1600_SNMPPowerDevice" );

            InitialContext ctx = new InitialContext();

            setSnmpCoreService( ( SnmpCoreService ) ctx.lookup( SNMP_CORE_SERVICE_MAPPED_NAME ) );

        }
        catch ( NamingException e )
        {
            log.error( "Lookup of SNMP service failed :" + e );
        }
    }

    /**
     * 
     * @param host
     *            IP address of the WTI power device
     * @param port
     *            port to which SNMP commands are to be send, usually this is
     *            161 which is the SNMP port.
     * @param readCommunityString
     *            this is the read community string
     * @param readWriteCommunityString
     *            this is the read write community string
     */
    public WTI_NPS_1600_SNMPPowerDevice( String host, Integer port, String readCommunityString,
            String readWriteCommunityString )
    {
        this.host = host;
        this.port = port;
        this.readCommunityString = readCommunityString;
        this.readWriteCommunityString = readWriteCommunityString;

    }

    @Override
    /**
     * This function does not have an implementation with SNMP
     */
    public void createPowerDevConn()
    {
        // NO implementation

    }

    /**
     * Method to get current status of power device.
     * 
     * @return true if outlet is ON and false if outlet is OFF.
     */
    @Override
    protected boolean getCurrentStatus( int outlet )
    {

        log.debug( "get powerStatus called for outlet:" + outlet + " on host:" + host );

        boolean currentStatus = false;

        String outletOID = OID_PLUG_STATUS + outlet;

        String returnValue = null;

        if ( null == snmpCoreService )
        {
            System.out.println( "getCurrentStatus :: " + "snmpCoreService is null" );
            throw new PowerStatusException( "snmpCoreService is null" );
        }
        try
        {
            returnValue = snmpCoreService.get( outletOID, readCommunityString, host, port, null, null, null );
        }
        catch ( SNMPException e )
        {
            log.debug( "SNMPException : " + e );
            System.out.println( "SNMPException : " + e );

        }

        log.debug( "snmp service returned status :" + returnValue + "for outlet:" + outlet );

        if ( null == returnValue )
        {
            log.error( "SNMP service returned null value " );

            throw new PowerStatusException( " Could not retrieve the current state of the device" );
        }
        if ( POWER_ON_SNMP_VALUE.equals( returnValue ) )
        {

            currentStatus = true;
        }
        log.debug( "getPowerStatus returned currentStatus:" + currentStatus );

        return currentStatus;

    }

    /**
     * Changes the power status of the outlet based on the requested status
     * 
     * @param command
     *            It can be one of the values( SNMP_SWITCH_ON_COMMAND,
     *            SNMP_SWITCH_OFF_COMMAND, SNMP_BOOT_COMMAND)
     * @param outlet
     *            id of the outlet
     * @return true if success and false if failure
     */
    @Override
    protected void executeCommand( String command, int outlet )
    {

        String outletOID = OID_PLUG_ACTION + outlet;

        if ( null == snmpCoreService )
        {
            throw new PowerStatusException( "snmpCoreService is null" );
        }
        try
        {
            snmpCoreService.set( outletOID, readWriteCommunityString, host, port, command, OID_TYPE_INTEGER, null,
                    null, null );
        }
        catch ( SNMPException e )
        {
            log.debug( "SNMPException : " + e );
            throw new PowerStatusException( "Change status operation failed" );

        }
        log.debug( "Power status changed to :" + command );
    }

    public static SnmpCoreService getSnmpCoreService()
    {
        return snmpCoreService;
    }

    public static void setSnmpCoreService( SnmpCoreService snmpCoreService )
    {
        WTI_NPS_1600_SNMPPowerDevice.snmpCoreService = snmpCoreService;

    }

    @Override
    public void destroy()
    {
        log.debug( "WTI_NPS_1600_SNMPPowerDevice: Nothing to do in destroy" );

    }
}
