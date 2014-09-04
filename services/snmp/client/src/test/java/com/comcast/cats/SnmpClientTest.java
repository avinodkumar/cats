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

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import com.comcast.cats.SnmpClient;
import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.SnmpService;

/**
 * Test class for Web service client.
 * 
 * @author TATA
 * 
 */
public class SnmpClientTest extends TestCase
{
    /**
     * The log4j logger instance for this class.
     */
    private static Logger       log            = LoggerFactory.getLogger( SnmpClientTest.class );

    /**
     * The community name of the V1/V2 snmp agents.
     */
    private static final String COMMUNITY_NAME = "public";
    /**
     * IP address of the target machine.
     */
    private static final String IP             = "192.168.161.82";
    /**
     * Port number.
     */
    private static final int    PORT           = 8001;
    /**
     * Object identifier representing the functionality.
     */
    private static final String OID_SYS_DESCR  = ".1.3.6.1.2.1.1.5.0";

    /**
     * Dummy.
     */
    public void testDummy()
    {

    }

    /**
     * TODO: As good as using main method. Need to use easymock.
     * 
     * @throws Exception
     *             MalformedURLException
     */
    @Test
    public void testSnmpClient() throws Exception
    {
        try
        {
            final SnmpClient snmpClient = new SnmpClient();
            final SnmpService port = snmpClient.getProxy();
            if ( port != null )
            {
                final SnmpServiceReturnMesage snmpServiceReturnMesage = port.get( OID_SYS_DESCR, COMMUNITY_NAME, IP,
                        PORT, null, null, null );

                if ( log.isInfoEnabled() )
                {
                    log.info( snmpServiceReturnMesage.getResultObject() );
                }
            }

        }
        catch ( Exception e )
        {
            log.warn( " The default WSDL doesn't exist " );
        }
    }
}
