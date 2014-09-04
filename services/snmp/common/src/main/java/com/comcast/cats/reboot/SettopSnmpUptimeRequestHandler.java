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
package com.comcast.cats.reboot;

import com.comcast.cats.SnmpManager;
import com.comcast.cats.SnmpManagerImpl;
import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.info.SnmpWalkResult;

public class SettopSnmpUptimeRequestHandler
{
    public static final String        COMMUNITY   = "communityPassString";
    public static final String        NETWORK_OID = ".1.3.6.1.2.1.2.2.1.6";
    public static final String        UPTIME_OID  = ".1.3.6.1.2.1.1.3.0";
    public static final int           SNMP_PORT   = 161;

    final protected String            macAddress;
    final protected String            ipAddress;
    final protected String            ecm;

    protected SnmpServiceReturnMesage networkInfo = null;
    protected SnmpServiceReturnMesage uptime      = null;

    protected RebootDetectionStatus   status      = RebootDetectionStatus.UNKOWN; ;

    final protected SnmpManager       snmp;

    public SettopSnmpUptimeRequestHandler( String macAddress, String ipAddress, String ecm )
    {
        this( macAddress, ipAddress, ecm, new SnmpManagerImpl() );
    }

    public SettopSnmpUptimeRequestHandler( String macAddress, String ipAddress, String ecm, SnmpManager snmp )
    {
        super();
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.ecm = ecm;
        this.snmp = snmp;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public String getEcm()
    {
        return ecm;
    }

    protected void requestUptime()
    {
        uptime = snmp.get( UPTIME_OID, COMMUNITY, ipAddress, SNMP_PORT );
    }

    protected void requestNetworkInformation()
    {
        networkInfo = snmp.walk( NETWORK_OID, COMMUNITY, ipAddress, SNMP_PORT );
    }

    protected Long processUptimeResponse()
    {
        return Long.valueOf( uptime.getResultObject() );
    }

    public Long retrieveUptime()
    {
        Long response = -1L;
        requestNetworkInformation();
        if ( validate() )
        {
            requestUptime();
            response = processUptimeResponse();
        }
        return response;
    }

    protected boolean validate()
    {
        if ( networkInfo == null || networkInfo.getComplexResultObject().isEmpty() )
        {
            status = RebootDetectionStatus.COMMUNICATION_ERROR;
            return false;
        }
        for ( SnmpWalkResult response : networkInfo.getComplexResultObject() )
        {
            if ( response.getValue().equalsIgnoreCase( ecm ) )
            {
                return true;
            }
        }
        // Something else could be a problem here, but chances are good there is
        // just a MAC mismatch.
        status = RebootDetectionStatus.IP_MAC_MISMATCH;
        return false;
    }

    public RebootDetectionStatus getRebootDetectionStatus()
    {
        return this.status;
    }
}
