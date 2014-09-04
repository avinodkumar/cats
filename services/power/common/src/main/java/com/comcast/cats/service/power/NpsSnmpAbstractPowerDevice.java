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

import static com.comcast.cats.service.power.util.PowerConstants.NPS_SNMP_SWITCH_BOOT_COMMAND;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_SNMP_SWITCH_OFF_COMMAND;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_SNMP_SWITCH_ON_COMMAND;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_OFF;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON;
import static com.comcast.cats.service.power.util.PowerConstants.STATUS_UNKNOWN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for the interface PowerControllerDevice
 * 
 * @author bemman01c, aswathyann
 * 
 */
public abstract class NpsSnmpAbstractPowerDevice extends PowerControllerDevice
{

    /** CONSTANTS **/

    private static final int    READ_DELAY   = 1000;

    private static final int    TOGGLE_DELAY = 1500;

    /** End of CONSTANT definition **/

    /**
     * Logger
     */
    private static final Logger LOGGER       = LoggerFactory.getLogger( NpsSnmpAbstractPowerDevice.class );

    /**
     * Method to get current status of power device.
     * 
     * @return true if outlet is ON and false if outlet is OFF.
     */
    protected abstract boolean getCurrentStatus( int outlet );

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

    protected abstract void executeCommand( String command, int outlet );

    /* There is slight delay in updating SNMP power status OID */
    private void sleep( int delay )
    {
        try
        {
            Thread.sleep( delay );
        }
        catch ( InterruptedException e )
        {
            LOGGER.error( "InterruptedException in sleep :" + e );
        }
    }

    @Override
    public synchronized boolean powerOn( int outlet )
    {

        LOGGER.info( "NPS SNMP command for power ON called" );

        boolean returnStatus = true;

        boolean requestedStatus = true;

        try
        {
            returnStatus = getCurrentStatus( outlet );

            if ( returnStatus )
            {
                LOGGER.debug( "Currently in ON status" );

            }
            else
            {
                // switch on
                executeCommand( NPS_SNMP_SWITCH_ON_COMMAND, outlet );

                returnStatus = getCurrentStatus( outlet );
            }
            sleep( READ_DELAY );
            validateStatus( requestedStatus, outlet );

        }
        catch ( PowerStatusException e )
        {
            LOGGER.error( "Outlet status change operation failed :" + e );
            returnStatus = false;
        }
        updateStatistics( outlet, ON, returnStatus );
        return returnStatus;
    }

    @Override
    public synchronized boolean powerOff( int outlet )
    {
        LOGGER.info( "NPS SNMP command for power off called" );

        boolean returnStatus = true;

        boolean requestedStatus = false;

        try
        {
            returnStatus = getCurrentStatus( outlet );

            if ( !returnStatus )
            {
                LOGGER.debug( "Currently in OFF status" );
                returnStatus = true;
            }
            else
            {
                // switch off
                executeCommand( NPS_SNMP_SWITCH_OFF_COMMAND, outlet );

                //returnStatus = getCurrentStatus( outlet );

            }
            sleep( READ_DELAY );
            validateStatus( requestedStatus, outlet );
        }
        catch ( PowerStatusException e )
        {
            LOGGER.error( "Outlet status change operation failed: " + e );

            returnStatus = false;
        }
        updateStatistics( outlet, OFF, returnStatus );
        return returnStatus;
    }

    @Override
    public synchronized boolean powerToggle( int outlet )
    {
        LOGGER.info( "NPS SNMP command for power toggle called" );

        boolean returnStatus = true;

        // After boot the device should return to ON status
        boolean requestedStatus = true;

        try
        {
            executeCommand( NPS_SNMP_SWITCH_BOOT_COMMAND, outlet );

            //returnStatus = getCurrentStatus( outlet );

            sleep( TOGGLE_DELAY );

            validateStatus( requestedStatus, outlet );
        }
        catch ( PowerStatusException e )
        {
            LOGGER.error( "Outlet status operation failed: " + e );

            returnStatus = false;
        }
        updateStatistics( outlet, BOOT, returnStatus );
        return returnStatus;
    }

    @Override
    public synchronized String getOutletStatus( int outlet )
    {
        LOGGER.info( "NPS SNMP command to get powerStatus called" );

        try
        {
            boolean returnStatus = true;

            returnStatus = getCurrentStatus( outlet );

            if ( returnStatus )
            {
                return POWER_ON;

            }
            else
            {
                return POWER_OFF;
            }
        }
        catch ( PowerStatusException e )
        {

            LOGGER.error( "Outlet status read operation failed: " + e );

            return STATUS_UNKNOWN;
        }
    }

    /**
     * Validates if the current status of the power outlet matches the expected
     * value.
     * 
     * @param expectedStatus
     *            expected status,ON-true ,OFF-false
     * @param outlet
     * @throws Exception
     *             if expectation does not match actual an exception exception
     *             is thrown.
     */
    private void validateStatus( boolean expectedStatus, int outlet )
    {
        boolean outletStatus = getCurrentStatus( outlet );

        if ( expectedStatus != outletStatus )
        {
            throw new PowerStatusException( "Outlet status did not match Expected status" );
        }
    }
}
