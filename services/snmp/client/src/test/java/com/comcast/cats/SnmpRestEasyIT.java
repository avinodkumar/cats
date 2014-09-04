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

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.Snmp;
import com.comcast.cats.service.exceptions.SNMPException;

/**
 * Class for testing SNMP REST
 * 
 * @author aswathyann
 * 
 */

public class SnmpRestEasyIT
{

    private Snmp                 snmp;

    public static final String   OID              = ".1.3.6.1.2.1.1.5.0";

    /**
     * The community name of the V1/V2 snmp agents.
     */
    private static final String  COMMUNITY_NAME   = "public";
    private static final String  SNMP_SERVICE_IP  = "192.168.160.201:8080";
    private static final String  DEVICE_IP        = "192.168.160.201";
    private static final Integer SNMP_PORT        = 161;
    private final static Logger  LOGGER           = LoggerFactory.getLogger( SnmpRestEasyIT.class );

    private SnmpServiceProxy     snmpServiceProxy = new SnmpServiceProxy( SNMP_SERVICE_IP, DEVICE_IP, SNMP_PORT );

    public SnmpRestEasyIT()
    {
        snmp = snmpServiceProxy.getProxy();
    }

    @Test
    public void testSnmpGet()
    {
        Assert.assertNotNull( snmp );

        SnmpServiceReturnMesage msg = new SnmpServiceReturnMesage();

        msg = snmp.get( OID, COMMUNITY_NAME, "", "", "" );
        System.out.println(msg.getResultObject());

        LOGGER.info( msg.getResultObject() );
    }

    @Test
    public void testSnmpSet()
    {/*
      * 
      * String OID_STATUS = ".1.3.6.1.4.1.2634.3.100.200.1.3.4";
      * 
      * String OID_ACTION = ".1.3.6.1.4.1.2634.3.100.200.1.4.4";
      * 
      * 
      * Assert.assertNotNull( snmp );
      * 
      * SnmpServiceReturnMesage msg = new SnmpServiceReturnMesage();
      * 
      * msg = snmp.get( OID_STATUS, COMMUNITY_NAME, "", "", "" );
      * 
      * System.out.println( "Get 1 = " + msg.getResultObject() ); msg =
      * snmp.set( OID_ACTION, COMMUNITY_NAME, "5", "Integer", "", "", "" );
      * 
      * System.out.println( "Set = " + msg.getResultObject() );
      * 
      * msg = snmp.get( OID_STATUS, COMMUNITY_NAME, "", "", "" );
      * 
      * System.out.println( "Get 2 = " + msg.getResultObject() );
      */
    }
}