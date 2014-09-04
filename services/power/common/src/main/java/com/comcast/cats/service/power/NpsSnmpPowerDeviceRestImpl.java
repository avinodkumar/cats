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
import static com.comcast.cats.info.SnmpServiceConstants.CATS_SNMP_PROXY_IP;
import static com.comcast.cats.service.power.util.PowerConstants.OID_PLUG_ACTION;
import static com.comcast.cats.service.power.util.PowerConstants.OID_PLUG_STATUS;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON_SNMP_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.Snmp;
import com.comcast.cats.SnmpServiceProxy;

/**
 * The class implements the abstract class NpsSnmpAbstractPowerDevice sends SNMP
 * commands in the REST way. It uses the SNMP v1/v2c.
 * 
 * @author aswathyann
 * 
 */
public class NpsSnmpPowerDeviceRestImpl extends NpsSnmpAbstractPowerDevice
{

    private Snmp                snmp;

    private String              communityName;

    private String              userName;

    private String              authenticatePassword;

    private String              privacyPassword;

    private String              deviceIP;

    private String              hostServerIP;

    private SnmpServiceProxy    snmpServiceProxy;

    private final static Logger LOGGER    = LoggerFactory.getLogger( NpsSnmpPowerDeviceRestImpl.class );

    private static final String EMPTY_STR = "";

    public NpsSnmpPowerDeviceRestImpl( String deviceIP, Integer snmpPort, String communityName, String userName,
            String authenticatePassword, String privacyPassword )
    {
        this.deviceIP = deviceIP;

        this.communityName = communityName;

        this.userName = userName;

        this.authenticatePassword = authenticatePassword;

        this.privacyPassword = privacyPassword;

        this.hostServerIP = System.getProperty( CATS_SNMP_PROXY_IP );
              
        snmpServiceProxy = new SnmpServiceProxy( hostServerIP, deviceIP, snmpPort );

        snmp = snmpServiceProxy.getProxy();

    }

    /**
     * Method to get current status of power device.
     * 
     * @return true if outlet is ON and false if outlet is OFF.
     */
    @Override
    protected boolean getCurrentStatus( int outlet )
    {

        LOGGER.debug( "get powerStatus called for outlet:" + outlet + " on device IP:" + deviceIP );

        boolean currentStatus = false;

        String outletOID = OID_PLUG_STATUS + outlet;

        SnmpServiceReturnMesage returnValue = null;

        if ( null == snmp )
        {
            throw new PowerStatusException( "snmp is null" );
        }

        returnValue = snmp.get( outletOID, nullCheck( communityName ), nullCheck( userName ),
                nullCheck( authenticatePassword ), nullCheck( privacyPassword ) );

        if ( null == returnValue )
        {
            LOGGER.error( "SNMP service returned null value " );

            throw new PowerStatusException( " Could not retrieve the current state of the device" );
        }

        LOGGER.debug( "SnmpServiceReturnMesage  ResultObject =" + returnValue.getResultObject() + "for outlet:"
                + outlet );

        if ( POWER_ON_SNMP_VALUE.equals( returnValue.getResultObject() ) )
        {
            currentStatus = true;
        }
        LOGGER.debug( "getPowerStatus returned currentStatus:" + currentStatus );

        return currentStatus;

    }

    private String nullCheck( String str )
    {
        return ( str == null ) ? EMPTY_STR : str;
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

        if ( null == snmp )
        {
            throw new PowerStatusException( "snmp is null" );
        }

        SnmpServiceReturnMesage msg = snmp.set( outletOID, nullCheck( communityName ), command, OID_TYPE_INTEGER,
                nullCheck( userName ), nullCheck( authenticatePassword ), nullCheck( privacyPassword ) );

        LOGGER.debug( "Power status changed to :" + command );

        LOGGER.debug( "SnmpServiceReturnMesage ResultObject=" + msg.getResultObject() );
    }

    @Override
    public void createPowerDevConn()
    {
        // NO implementation

    }

    @Override
    public void destroy()
    {
        LOGGER.debug( "NpsSnmpPowerDeviceRestImpl : Nothing to do in destroy" );

    }

}
