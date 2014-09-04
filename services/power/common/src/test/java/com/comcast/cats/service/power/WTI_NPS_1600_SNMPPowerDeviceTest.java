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

import java.io.IOException;

import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import com.comcast.cats.service.SnmpCoreService;
import com.comcast.cats.service.exceptions.SNMPException;
import com.comcast.cats.service.power.WTI_NPS_1600_SNMPPowerDevice;
import static com.comcast.cats.service.power.util.PowerConstants.OID_PLUG_STATUS;
import static com.comcast.cats.service.power.util.PowerConstants.OID_PLUG_ACTION;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON_SNMP_VALUE;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_OFF_SNMP_VALUE;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_SNMP_SWITCH_ON_COMMAND;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_SNMP_SWITCH_OFF_COMMAND;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_SNMP_SWITCH_BOOT_COMMAND;
import static com.comcast.cats.info.SnmpServiceConstants.DEFAULT_SNMP_READ_COMMUNITY_STRING;
import static com.comcast.cats.info.SnmpServiceConstants.DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING;
import static com.comcast.cats.info.SnmpServiceConstants.OID_TYPE_INTEGER;

public class WTI_NPS_1600_SNMPPowerDeviceTest
{

    private static final int    OUTLET = 5;

    private static final String HOST   = "192.168.120.102";

    private static final int    PORT   = 161;

    private static final Logger log    = LoggerFactory.getLogger( WTI_NPS_1600_SNMPPowerDeviceTest.class );

    /*
     * Test script for positive condition. Expected the power ON status to be
     * true.
     */
    @Test
    public void testPowerOnPass() throws IOException
    {
        SnmpCoreService snmpCoreService = EasyMock.createMock( SnmpCoreService.class );
        try
        {
            Assert.assertNotNull( snmpCoreService );

            EasyMock.expect(
                    snmpCoreService.get( OID_PLUG_STATUS + OUTLET, DEFAULT_SNMP_READ_COMMUNITY_STRING, HOST, PORT,
                            null, null, null ) ).andReturn( POWER_ON_SNMP_VALUE ).anyTimes();
        }
        catch ( SNMPException e )
        {
            log.error( "SNMPException : " + e );
        }
        EasyMock.replay( snmpCoreService );

        WTI_NPS_1600_SNMPPowerDevice pwrTestObj = new WTI_NPS_1600_SNMPPowerDevice( HOST, PORT,
                DEFAULT_SNMP_READ_COMMUNITY_STRING, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING ){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };
       
        WTI_NPS_1600_SNMPPowerDevice.setSnmpCoreService( snmpCoreService );

        Assert.assertTrue( "Expected power to be ON", pwrTestObj.powerOn( OUTLET ) );

    }

    /*
     * Test script for negative condition. Expected the power ON status to be
     * false.
     */
    @Test
    public void testPowerOnFail() throws IOException
    {
        SnmpCoreService snmpCoreService = EasyMock.createMock( SnmpCoreService.class );
        try
        {
            Assert.assertNotNull( snmpCoreService );

            EasyMock.expect(
                    snmpCoreService.get( OID_PLUG_STATUS + OUTLET, DEFAULT_SNMP_READ_COMMUNITY_STRING, HOST, PORT,
                            null, null, null ) ).andReturn( POWER_OFF_SNMP_VALUE ).anyTimes();

            snmpCoreService.set( OID_PLUG_ACTION + OUTLET, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING, HOST, PORT,
                    NPS_SNMP_SWITCH_ON_COMMAND, OID_TYPE_INTEGER, null, null, null );

            EasyMock.expectLastCall();
        }
        catch ( SNMPException e )
        {
            log.error( "SNMPException : " + e );
        }
        EasyMock.replay( snmpCoreService );

        WTI_NPS_1600_SNMPPowerDevice pwrTestObj = new WTI_NPS_1600_SNMPPowerDevice( HOST, PORT,
                DEFAULT_SNMP_READ_COMMUNITY_STRING, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING ){
        		@Override
        		protected void updateStatistics(int outlet, String cmd, boolean ret){
    		
        		}
        };

        WTI_NPS_1600_SNMPPowerDevice.setSnmpCoreService( snmpCoreService );

        Assert.assertFalse( "Expected power to be OFF", pwrTestObj.powerOn( OUTLET ) );

    }

    /*
     * Test script for positive condition. Expected the power OFF status to be
     * true.
     */
    @Test
    public void testPowerOffPass() throws IOException
    {
        SnmpCoreService snmpCoreService = EasyMock.createMock( SnmpCoreService.class );
        try
        {
            Assert.assertNotNull( snmpCoreService );

            EasyMock.expect(
                    snmpCoreService.get( OID_PLUG_STATUS + OUTLET, DEFAULT_SNMP_READ_COMMUNITY_STRING, HOST, PORT,
                            null, null, null ) ).andReturn( POWER_OFF_SNMP_VALUE ).anyTimes();
        }
        catch ( SNMPException e )
        {
            log.error( "SNMPException : " + e );
        }
        EasyMock.replay( snmpCoreService );

        WTI_NPS_1600_SNMPPowerDevice pwrTestObj = new WTI_NPS_1600_SNMPPowerDevice( HOST, PORT,
                DEFAULT_SNMP_READ_COMMUNITY_STRING, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING ){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };

        WTI_NPS_1600_SNMPPowerDevice.setSnmpCoreService( snmpCoreService );

        Assert.assertTrue( "Expected power to be OFF", pwrTestObj.powerOff( OUTLET ) );

    }

    /*
     * Test script for negative condition. Expected the power OFF status to be
     * false.
     */
    @Test
    public void testPowerOffFail() throws IOException
    {
        SnmpCoreService snmpCoreService = EasyMock.createMock( SnmpCoreService.class );
        try
        {
            Assert.assertNotNull( snmpCoreService );

            EasyMock.expect(
                    snmpCoreService.get( OID_PLUG_STATUS + OUTLET, DEFAULT_SNMP_READ_COMMUNITY_STRING, HOST, PORT,
                            null, null, null ) ).andReturn( POWER_ON_SNMP_VALUE ).anyTimes();

            snmpCoreService.set( OID_PLUG_ACTION + OUTLET, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING, HOST, PORT,
                    NPS_SNMP_SWITCH_OFF_COMMAND, OID_TYPE_INTEGER, null, null, null );

            EasyMock.expectLastCall();
        }
        catch ( SNMPException e )
        {
            log.error( "SNMPException : " + e );
        }
        EasyMock.replay( snmpCoreService );

        WTI_NPS_1600_SNMPPowerDevice pwrTestObj = new WTI_NPS_1600_SNMPPowerDevice( HOST, PORT,
                DEFAULT_SNMP_READ_COMMUNITY_STRING, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING ){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        }
        ;

        WTI_NPS_1600_SNMPPowerDevice.setSnmpCoreService( snmpCoreService );

        Assert.assertFalse( "Expected power to be ON", pwrTestObj.powerOff( OUTLET ) );

    }

    /*
     * Test script for positive condition. Expected the power BOOT status to be
     * true.
     */
    @Test
    public void testPowerBootPass() throws IOException
    {
        SnmpCoreService snmpCoreService = EasyMock.createMock( SnmpCoreService.class );
        try
        {
            Assert.assertNotNull( snmpCoreService );

            EasyMock.expect(
                    snmpCoreService.get( OID_PLUG_STATUS + OUTLET, DEFAULT_SNMP_READ_COMMUNITY_STRING, HOST, PORT,
                            null, null, null ) ).andReturn( POWER_ON_SNMP_VALUE ).anyTimes();

            snmpCoreService.set( OID_PLUG_ACTION + OUTLET, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING, HOST, PORT,
                    NPS_SNMP_SWITCH_BOOT_COMMAND, OID_TYPE_INTEGER, null, null, null );

            EasyMock.expectLastCall();
        }
        catch ( SNMPException e )
        {
            log.error( "SNMPException : " + e );
        }
        EasyMock.replay( snmpCoreService );

        WTI_NPS_1600_SNMPPowerDevice pwrTestObj = new WTI_NPS_1600_SNMPPowerDevice( HOST, PORT,
                DEFAULT_SNMP_READ_COMMUNITY_STRING, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING ){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };

        WTI_NPS_1600_SNMPPowerDevice.setSnmpCoreService( snmpCoreService );

        Assert.assertTrue( "Power OFF failed", pwrTestObj.powerToggle( OUTLET ) );

    }

    /*
     * Test script for negative condition. Expected the power BOOT status to be
     * false.
     */
    @Test
    public void testPowerBootFail() throws IOException
    {
        SnmpCoreService snmpCoreService = EasyMock.createMock( SnmpCoreService.class );
        try
        {
            Assert.assertNotNull( snmpCoreService );

            EasyMock.expect(
                    snmpCoreService.get( OID_PLUG_STATUS + OUTLET, DEFAULT_SNMP_READ_COMMUNITY_STRING, HOST, PORT,
                            null, null, null ) ).andReturn( POWER_OFF_SNMP_VALUE ).anyTimes();

            snmpCoreService.set( OID_PLUG_ACTION + OUTLET, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING, HOST, PORT,
                    NPS_SNMP_SWITCH_BOOT_COMMAND, OID_TYPE_INTEGER, null, null, null );

            EasyMock.expectLastCall();
        }
        catch ( SNMPException e )
        {
            log.error( "SNMPException : " + e );
        }
        EasyMock.replay( snmpCoreService );

        WTI_NPS_1600_SNMPPowerDevice pwrTestObj = new WTI_NPS_1600_SNMPPowerDevice( HOST, PORT,
                DEFAULT_SNMP_READ_COMMUNITY_STRING, DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING ){
        	@Override
        	protected void updateStatistics(int outlet, String cmd, boolean ret){
        		
        	}
        };

        WTI_NPS_1600_SNMPPowerDevice.setSnmpCoreService( snmpCoreService );

        Assert.assertFalse( "Power OFF failed", pwrTestObj.powerToggle( OUTLET ) );

    }
}