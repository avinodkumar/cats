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
package com.comcast.cats.monitor.reboot;

import com.comcast.cats.SnmpManagerImpl;
import com.comcast.cats.info.SnmpServiceConstants;
import com.comcast.cats.info.SnmpServiceReturnMesage;

/**
 * Represents all Reboot Monitors that use SNMP to detect reboot.
 * @author skurup00c
 *
 */
public abstract class SNMPRebootMonitor extends AbstractRebootMonitor
{

    public static final int DEFAULT_PORT_NUMBER = 161;
    private static final int SNMP_RETRIES        = 4;
    private static final int RETRY_INTERVAL      = 30 * 1000; //30 seconds

    protected String        community           = SnmpServiceConstants.DEFAULT_SNMP_READ_COMMUNITY_STRING;
    protected String        rebootOID;
    protected SnmpManagerImpl         snmpManager;

    private int                     portNumber          = DEFAULT_PORT_NUMBER;
    public static final String REBOOT_TYPE = "SNMP Reboot Monitor";

    public SNMPRebootMonitor()
    {
        snmpManager = new SnmpManagerImpl();
    }

    public void setPortNumber( int portNumber )
    {
        this.portNumber = portNumber;
    }

    public void setCommunity( String community )
    {
        this.community = community;
    }

    public void setRebootOID( String rebootOID )
    {
        this.rebootOID = rebootOID;
    }

    public int getPortNumber()
    {
        return portNumber;
    }

    public String getCommunity()
    {
        return community;
    }

    public String getRebootOID()
    {
        return rebootOID;
    }

    public void detect()
    {
        String ipAddr = settop.getHostIpAddress();

        if ( ipAddr != null )
        {
            SnmpServiceReturnMesage message;
            int retries = 0;
            do
            {
                message = snmpManager.get( rebootOID, community, ipAddr, portNumber );
                retries++;
                if ( message.getResultObject() != null )
                {
                    parseRebootInfo( message.getResultObject() );
                    break;
                }
                try
                {
                    Thread.sleep( RETRY_INTERVAL );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            } while ( retries < SNMP_RETRIES );
        }
        else
        {
            logger.debug("Ip Address not found for Settop "+settop );
        }
    }

    protected abstract void parseRebootInfo( String snmpQueryResult );
}
