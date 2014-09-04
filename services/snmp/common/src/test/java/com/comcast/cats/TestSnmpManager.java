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

/**
 * @author TATA
 */

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import sun.misc.IOUtils;

import com.comcast.cats.info.SnmpServiceReturnEnum;
import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Test class to be run with PowerMockRunner instead of the default JUnit
 * runner.
 */
@RunWith(PowerMockRunner.class)
/**
 * PrepareForTest includes final classes, classes with final, private, static or
 * native methods that should be mocked and also classes that should be return a
 * mock object upon instantiation.
 */
@PrepareForTest(
    { InetAddress.class, SnmpManagerImpl.class, MPv3.class, SecurityProtocols.class, SecurityModels.class })
/**
 * The test class for SnmpManagerImpl class.
 * Powermock and Easymock are used for mocking the dependent classes.
 */
public class TestSnmpManager
{
    /**
     * Holds the engine boot up count for snmp V3.
     */
    private static final int DEFAULT_ENGINE_REBOOTS = 0;
    /**
     * The object of the class to be tested.
     */
    private SnmpManagerImpl  snmpManagerImpl;
    /**
     * The IP address of the target machine.
     */
    private String           targetIP               = "192.168.160.83";
    /**
     * The community name of the V1/V2 snmp agents.
     */
    private String           communityName          = "test";
    /**
     * The object identifier.
     */
    private String           oId                    = ".1.3.6.1.3.1.1.1.0";
    /**
     * The  string value used for the snmp set operations.
     */
    private String           setValue               = "Set Test Name";
    /**
     * The  integer value used for the snmp set operations.
     */
    private String           setIntValue               = "2";
    /**
     * The  OID value used for the snmp set operations.
     */
    private String           setOIDValue               = ".1.3.6.1.3.1.1.2.1";
    /**
     * The  IP value used for the snmp set operations.
     */
    private String           setIPValue               = "192.160.168.90";
    /**
     * The string value used for the snmp get operations.
     */
    private String           getValue               = "Test Name";
    /**
     * The value used as the target IP to make IOException.
     */
    private String           invalidTargetIP        = "0000";
    /**
     * The security name of the user for snmp V3 agents.
     */
    private String           userName               = "newUser";
    /**
     * The authentication password for snmp V3 agents.
     */
    private String           authenticationId       = "abc12345";
    /**
     * The privacy password for snmp V3 agents.
     */
    private String           privacyId              = "abc12345";
    /**
     * Invalid password, it contains less than 8 characters.
     */
    private String invalidAuthenticationId = "abc";
    /**
     * Invalid OID.
     */
    private String invalidOid = ".q.w.2.3";
    /**
     * The description of SnmpServiceReturnEnum SNMP_SERVICE_SUCCESS.
     */
    private String successEnumDescription = "General Snmp service success";

    /**
     * The byte array size.
     */
    private int              arraySize              = 10;
    /**
     * The value of SnmpServiceReturnEnum SNMP_SERVICE_SUCCESS.
     */
    private int successEnumValue = 0;
    /**
     * String as the Type of the value to be set.
     */
    private String typeString = "String";
    /**
     * integer as the Type of the value to be set.
     */
    private String typeInt = "integer";
    /**
     * counter32 as Type of the value to be set.
     */
    private String typeCounter32 = "counter32";
    /**
     * counter64 as Type of the value to be set.
     */
    private String typeCounter64 = "counter64";
    /**
     * GAUGE32 as Type of the value to be set.
     */
    private String typeGAUGE32 = "GAUGE32";
    /**
     * UNSIGNEDINTEGER32 as Type of the value to be set.
     */
    private String typeUNSIGNEDINTEGER32 = "UNSIGNEDINTEGER32";
    /**
     * TIMETICKS as Type of the value to be set.
     */
    private String typeTIMETICKS = "TIMETICKS";
    /**
     * OID as Type of the value to be set.
     */
    private String typeOID = "oid";
    /**
     * IPADDRESS as Type of the value to be set.
     */
    private String typeIPADDRESS = "IPADDRESS";
    /**
     * Method which executes before each test method. It initializes
     * SnmpManagerImpl object and enabling the logger debug level.
     * @throws Exception
     *             Throws IllegalArgumentException and IllegalAccessException
     */
    @Before
    public void setUp() throws Exception
    {
        /**
         * Creating an object of SnmpManagerImpl class
         */
        snmpManagerImpl = new SnmpManagerImpl();
        /**
         * Enabling the logger with default access level
         */
        final Field logger = Whitebox.getField(SnmpManagerImpl.class, "logger");
        try
        {
            final Logger logger2 = (Logger) logger.get(null);
            //logger2.setLevel(Level.DEBUG);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method which executes after each test method. It assigns null value to
     * the SnmpManagerImpl object created.
     */
    @After
    public void tearDown()
    {
        snmpManagerImpl = null;
    }

    /**
     * Method for testing snmp V1/V2 get operation while all the input
     * parameters are valid. The method validates the value got after the snmp
     * get operation against the expected value.
     */
    @Test
    public void testGetV1V2()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);
            final OctetString community = new OctetString(communityName);

            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.GET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));
            final OID oid = PowerMock.createMock(OID.class, oId);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid);
            pdu.add(variableBinding);
            EasyMock.expectLastCall();

            snmp.close();
            EasyMock.expectLastCall();
            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.get(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();

            final Variable variable = new OctetString(getValue);
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);
            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.get(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }        
        catch (Exception e)
        {
        	e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 get operation while the parameter oId is
     * null. The method validates whether the SNMP_SERVICE_INVALID_INPUT service
     * code is returned.
     */
    @Test
    public void testGetV1V2NullOId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(null, communityName, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
    }

    /**
     * Method for testing snmp V1/V2 get operation while the parameter target IP
     * is null. The method validates whether the SNMP_SERVICE_INVALID_INPUT
     * service code is returned.
     */
    @Test
    public void testGetV1V2NullTargetIP()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, communityName, null,
                SnmpManager.DEFAULT_PORT_NUMBER);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
    }

    /**
     * Method for testing snmp V1/V2 get operation while the parameter community
     * is null and port number is invalid(ie -1). The method validates whether
     * the get operation is performed successfully with the
     * DEFAULT_COMMUNITY_NAME and DEFAULT_PORT_NUMBER, by checking the return
     * value.
     */
    @Test
    public void testGetV1V2InvalidCommunityAndPortNumber()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);
            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);
            final OctetString community = new OctetString(SnmpManager.DEFAULT_COMMUNITY_NAME);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);

            PowerMock.replay(InetAddress.class);
            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.GET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));
            final OID oid = PowerMock.createMock(OID.class, oId);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid);
            pdu.add(variableBinding);
            EasyMock.expectLastCall();

            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.get(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();

            final Variable variable = new OctetString(getValue);
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);

            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);
            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage snmpServiceReturnMessage = new SnmpManagerImpl().get(oId, null, targetIP,
                    SnmpManager.INVALID_VALUE);
            assertEquals(variable.toString(), snmpServiceReturnMessage.getResultObject());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 get operation while the parameter target IP
     * is an invalid value. The method validates whether the
     * SNMP_SERVICE_FAILURE service code is returned.
     */
    @Test
    public void testGetV1V2InvalidTargetIP()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, communityName,
                invalidTargetIP, SnmpManager.DEFAULT_PORT_NUMBER);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid. The method validates the value got after the snmp
     * set operation against the set value.
     */
    @Test
    public void testSetV1V2()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final OctetString variable = new OctetString(setValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();

            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);
        
            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while input parameter oId is
     * null. The method validates whether the SNMP_SERVICE_INVALID_INPUT service
     * code is returned.
     */
    @Test
    public void testSetV1V2NullOId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(null, communityName, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue,typeString);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
    }

    /**
     * Method for testing snmp V1/V2 set operation while input parameter target
     * IP is null. The method validates whether the SNMP_SERVICE_INVALID_INPUT
     * service code is returned.
     */
    @Test
    public void testSetV1V2NullTargetIP()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, communityName, null,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue,typeString);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
    }

    /**
     * Method for testing snmp V1/V2 set operation while input parameter
     * community name is null and port number is invalid (ie -1). The method
     * validates whether the set operation is performed successfully with the
     * DEFAULT_COMMUNITY_NAME and DEFAULT_PORT_NUMBER, by checking the return
     * value against the set value.
     */
    @Test
    public void testSetV1V2InvalidCommunityPortNumber()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(SnmpManager.DEFAULT_COMMUNITY_NAME);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final OctetString variable = new OctetString(setValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();
            PowerMock.replay(oid, OID.class);

            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(3).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("AUTHENITCATION_FAILURE").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);

            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);
            PowerMock.replay(variableBinding, VariableBinding.class);

            
            final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, null, targetIP,
                    SnmpManager.INVALID_VALUE, setValue,typeString);
            assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_FAILURE, snmpServiceReturnMessage.getServiceCode());
            assertEquals(new Integer(3), snmpServiceReturnMessage.getSnmpErrorCode());

        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while input parameter target
     * IP is invalid. The method validates whether the SNMP_SERVICE_FAILURE
     * service code is returned.
     */
    @Test
    public void testSetV1V2InvalidTargetIP()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, communityName,
                invalidTargetIP, SnmpManager.DEFAULT_PORT_NUMBER, setValue,typeString);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
    }

    /**
     * Method for testing snmp V3 get operation while all the input parameters
     * are valid. The method validates the value got after the snmp get
     * operation against the expected value.
     */
    @Test
    public void testGetV3()
    {
        try
        {
            final TransportMapping transport = PowerMock.createMockAndExpectNew(DefaultUdpTransportMapping.class);
            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transport);
            transport.listen();
            EasyMock.expectLastCall();

            PowerMock.mockStaticNice(SecurityProtocols.class);
            final SecurityProtocols securityProtocols = EasyMock.createMock(SecurityProtocols.class);
            EasyMock.expect(SecurityProtocols.getInstance()).andReturn(securityProtocols);
            securityProtocols.addDefaultProtocols();

            PowerMock.mockStaticNice(MPv3.class);
            final MPv3 mpv3 = EasyMock.createMock(MPv3.class);
            final byte[] localEngineId = new byte[ arraySize ];
            EasyMock.expect(MPv3.createLocalEngineID()).andReturn(localEngineId);

            final OctetString engineId = new OctetString(localEngineId);
            PowerMock.replay(mpv3, MPv3.class);
            PowerMock.replay(transport, DefaultUdpTransportMapping.class);

            final USM usm = PowerMock.createMockAndExpectNew(USM.class, securityProtocols, engineId,
                    DEFAULT_ENGINE_REBOOTS);
            PowerMock.replay(securityProtocols, SecurityProtocols.class);
            PowerMock.mockStaticNice(SecurityModels.class);
            final SecurityModels securityModels = EasyMock.createMock(SecurityModels.class);
            EasyMock.expect(SecurityModels.getInstance()).andReturn(securityModels);
            securityModels.addSecurityModel(usm);
            PowerMock.replay(securityModels, SecurityModels.class);

            final OctetString newUserName = new OctetString(userName);
            final OctetString newAuthenticatePasphrase = new OctetString(authenticationId);
            final OctetString newPrivacyPasphrase = new OctetString(privacyId);

            final UsmUser usmUser = PowerMock.createMockAndExpectNew(UsmUser.class, newUserName, AuthMD5.ID,
                    newAuthenticatePasphrase, PrivDES.ID, newPrivacyPasphrase);

            EasyMock.expect(snmp.getUSM()).andReturn(usm);
            usm.addUser(newUserName, usmUser);
            PowerMock.replay(usm, USM.class);
            PowerMock.replay(usmUser, UsmUser.class);

            final UserTarget userTarget = PowerMock.createMockAndExpectNew(UserTarget.class);
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);
            final Address address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress, 161);
            PowerMock.replay(inetAddress, InetAddress.class);
            PowerMock.replay(address, UdpAddress.class);

            userTarget.setAddress(address);
            userTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            userTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            userTarget.setVersion(SnmpConstants.version3);
            userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            userTarget.setSecurityName(newUserName);
            PowerMock.replay(userTarget, UserTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(ScopedPDU.class);
            pdu.setType(PDU.GET);
            EasyMock.expectLastCall();
            final OID oid = new OID(oId);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid);
            pdu.add(variableBinding);

            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.get(pdu, userTarget)).andReturn(responseEvent);
            snmp.close();

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            PowerMock.replay(pdu, ScopedPDU.class);
            final Variable variable = new OctetString(getValue);
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            EasyMock.replay(responseEvent);
            PowerMock.replay(variableBinding, VariableBinding.class);

            PowerMock.replay(snmp, Snmp.class);

            
            final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, targetIP, 161, userName,
                    authenticationId, privacyId);
            assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_SUCCESS, snmpServiceReturnMessage.getServiceCode());
            assertEquals(WebServiceReturnEnum.SUCCESS, snmpServiceReturnMessage.getResult());
            assertEquals(variable.toString(), snmpServiceReturnMessage.getResultObject());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V3 get operation while parameter oId is null. The
     * method validates whether the SNMP_SERVICE_INVALID_INPUT service code is
     * returned.
     */
    @Test
    public void testGetV3NullOId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(null, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, userName, authenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 get operation while parameter target IP is
     * null. The method validates whether the SNMP_SERVICE_INVALID_INPUT service
     * code is returned.
     */
    @Test
    public void testGetV3NullTargetIP()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, null,
                SnmpManager.DEFAULT_PORT_NUMBER, userName, authenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

     /**
     * Method for testing snmp V3 get operation while parameter user name is
     * null. The method validates whether the SNMP_SERVICE_INVALID_INPUT service
     * code is returned.
     */
    @Test
    public void testGetV3NullUserName()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, null, authenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 get operation while parameter Authentication
     * Id is null. The method validates whether the SNMP_SERVICE_INVALID_INPUT
     * service code is returned.
     */
    @Test
    public void testGetV3NullAuthenticationId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, userName, null, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 get operation while parameter Privacy Id is
     * null. The method validates whether the SNMP_SERVICE_INVALID_INPUT service
     * code is returned.
     */
    @Test
    public void testGetV3NullPrivacyId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, userName, authenticationId, null);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 get operation while all parameters are null.
     * The method validates whether the SNMP_SERVICE_INVALID_INPUT service code
     * is returned.
     */
    @Test
    public void testGetV3NullAllParameters()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(null, null,
                SnmpManager.DEFAULT_PORT_NUMBER, null, null, null);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 set operation while all the input parameters
     * are valid. The method validates the value got after the snmp set
     * operation against the expected value.
     */
    @Test
    public void testSetV3()
    {
        try
        {
            final TransportMapping transport = PowerMock.createMockAndExpectNew(DefaultUdpTransportMapping.class);
            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transport);
            transport.listen();
            EasyMock.expectLastCall();

            PowerMock.mockStaticNice(SecurityProtocols.class);
            final SecurityProtocols securityProtocols = EasyMock.createMock(SecurityProtocols.class);
            EasyMock.expect(SecurityProtocols.getInstance()).andReturn(securityProtocols);
            securityProtocols.addDefaultProtocols();

            PowerMock.mockStaticNice(MPv3.class);
            final MPv3 mpv3 = EasyMock.createMock(MPv3.class);
            final byte[] localEngineId = new byte[ arraySize ];
            EasyMock.expect(MPv3.createLocalEngineID()).andReturn(localEngineId);

            final OctetString engineId = new OctetString(localEngineId);
            PowerMock.replay(mpv3, MPv3.class);
            PowerMock.replay(transport, DefaultUdpTransportMapping.class);

            final USM usm = PowerMock.createMockAndExpectNew(USM.class, securityProtocols, engineId,
                    DEFAULT_ENGINE_REBOOTS);
            PowerMock.replay(securityProtocols, SecurityProtocols.class);
            PowerMock.mockStaticNice(SecurityModels.class);
            final SecurityModels securityModels = EasyMock.createMock(SecurityModels.class);
            EasyMock.expect(SecurityModels.getInstance()).andReturn(securityModels);
            securityModels.addSecurityModel(usm);
            PowerMock.replay(securityModels, SecurityModels.class);

            final OctetString newUserName = new OctetString(userName);
            final OctetString newAuthenticatePasphrase = new OctetString(authenticationId);
            final OctetString newPrivacyPasphrase = new OctetString(privacyId);

            final UsmUser usmUser = PowerMock.createMockAndExpectNew(UsmUser.class, newUserName, AuthMD5.ID,
                    newAuthenticatePasphrase, PrivDES.ID, newPrivacyPasphrase);
            EasyMock.expect(snmp.getUSM()).andReturn(usm);
            usm.addUser(newUserName, usmUser);
            PowerMock.replay(usm, USM.class);
            PowerMock.replay(usmUser, UsmUser.class);

            final UserTarget userTarget = PowerMock.createMockAndExpectNew(UserTarget.class);
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);
            final Address address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            PowerMock.replay(inetAddress, InetAddress.class);
            PowerMock.replay(address, UdpAddress.class);

            userTarget.setAddress(address);
            userTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            userTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            userTarget.setVersion(SnmpConstants.version3);
            userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            userTarget.setSecurityName(newUserName);
            PowerMock.replay(userTarget, UserTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(ScopedPDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            final OID oid = new OID(oId);
            final OctetString value = new OctetString(setValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid, value);
            pdu.add(variableBinding);

            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, userTarget)).andReturn(responseEvent);
            snmp.close();

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();

            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            PowerMock.replay(pdu, ScopedPDU.class);
            EasyMock.expect(variableBinding.getVariable()).andReturn(value);

            EasyMock.replay(responseEvent);
            PowerMock.replay(variableBinding, VariableBinding.class);

            PowerMock.replay(snmp, Snmp.class);
            final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString, userName, authenticationId, privacyId);
            assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_SUCCESS, snmpServiceReturnMessage.getServiceCode());
            assertEquals(WebServiceReturnEnum.SUCCESS, snmpServiceReturnMessage.getResult());
            assertEquals(setValue.toString(), snmpServiceReturnMessage.getResultObject());

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V3 set operation while the input parameter oId is
     * null. The method validates whether the SNMP_SERVICE_INVALID_INPUT service
     * code is returned.
     */
    @Test
    public void testSetV3NullOid()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(null, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString, userName, authenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 set operation while the input parameter Target
     * Ip is null. The method validates whether the SNMP_SERVICE_INVALID_INPUT
     * service code is returned.
     */
    @Test
    public void testSetV3NullTargetIp()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, null,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString, userName, authenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 set operation while the input parameter User
     * Name is null. The method validates whether the SNMP_SERVICE_INVALID_INPUT
     * service code is returned.
     */
    @Test
    public void testSetV3NullUserName()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString, null, authenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 set operation while the input parameter
     * Authentication Id is null. The method validates whether the
     * SNMP_SERVICE_INVALID_INPUT service code is returned.
     */
    @Test
    public void testSetV3NullAuthenticationId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString, userName, null, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 set operation while the input parameter
     * Privacy Id is null. The method validates whether the
     * SNMP_SERVICE_INVALID_INPUT service code is returned.
     */
    @Test
    public void testSetV3NullPrivacyId()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, setValue, typeString, userName, authenticationId, null);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing snmp V3 set operation while all the input parameters
     * are null. The method validates whether the SNMP_SERVICE_INVALID_INPUT
     * service code is returned.
     */
    @Test
    public void testSetV3AllParametersNull()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.set(null, null,
                SnmpManager.DEFAULT_PORT_NUMBER, null, typeString, null, null, null);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing whether the password is validated in the case of Snmp V3.
     * The method should return the SNMP_SERVICE_INVALID_INPUT service code.
     */
    @Test
    public void testGetV3InvalidPassword()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(oId, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, userName, invalidAuthenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }

    /**
     * Method for testing whether the OID is validated in the case of Snmp V1/V2.
     * The method should return the SNMP_SERVICE_INVALID_INPUT service code.
     */
    @Test
    public void testGetV1V2InvalidOID()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = snmpManagerImpl.get(invalidOid, targetIP,
                SnmpManager.DEFAULT_PORT_NUMBER, userName, invalidAuthenticationId, privacyId);
        assertEquals(SnmpServiceReturnEnum.SNMP_SERVICE_INVALID_INPUT, snmpServiceReturnMessage.getServiceCode());
        assertEquals(WebServiceReturnEnum.FAILURE, snmpServiceReturnMessage.getResult());
    }
    /**
     * The method for testing the argument constructor of SnmpServiceReturnMessage.
     * Checks whether the constructor creates a SnmpServiceReturnMessage
     * instance by retrieving the value and description of the passed
     * SnmpServiceReturnEnum (The argument to the constructor).
     */
    @Test
    public void testSnmpServiceReturnMessageArgumentConstructor()
    {
        final SnmpServiceReturnMesage snmpServiceReturnMessage = new
            SnmpServiceReturnMesage(SnmpServiceReturnEnum.SNMP_SERVICE_SUCCESS);
        assertEquals(successEnumDescription, snmpServiceReturnMessage.getServiceCode().getDescription());
        assertEquals(successEnumValue, snmpServiceReturnMessage.getServiceCode().getValue());
    }
    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an integer value.The
     * method validates the value got after the snmp set operation against the
     * set value.
     */
    @Test
    public void testSetV1V2IntegerValue()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final Integer intValue = Integer.parseInt(setIntValue);
            final Integer32 variable = new Integer32(intValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIntValue, typeInt);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an COUNTER64 value.The
     * method validates the value got after the snmp set operation against the
     * set value.
     */
    @Test
    public void testSetV1V2COUNTER32Value()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final Long intValue = Long.parseLong(setIntValue);
            final Counter32 variable = new Counter32(intValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIntValue, typeCounter32);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an GAUGE32 value.The
     * method validates the value got after the snmp set operation against the
     * set value.
     */
    @Test
    public void testSetV1V2GAUGE32Value()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final Long intValue = Long.parseLong(setIntValue);
            final Counter64 variable = new Counter64(intValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIntValue, typeCounter64);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an COUNTER32 value.The
     * method validates the value got after the snmp set operation against the
     * set value.
     */
    @Test
    public void testSetV1V2COUNTER64Value()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final Long intValue = Long.parseLong(setIntValue);
            final Gauge32 variable = new Gauge32(intValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIntValue, typeGAUGE32);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an UNSIGNEDINTEGER32
     * value.The method validates the value got after the snmp set operation
     * against the set value.
     */
    @Test
    public void testSetV1V2UNSIGNEDINTEGER32Value()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final Long intValue = Long.parseLong(setIntValue);
            final UnsignedInteger32 variable = new UnsignedInteger32(intValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIntValue, typeUNSIGNEDINTEGER32);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an TIMETICKS value.The
     * method validates the value got after the snmp set operation against the
     * set value.
     */
    @Test
    public void testSetV1V2TIMETICKSValue()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final Long intValue = Long.parseLong(setIntValue);
            final TimeTicks variable = new TimeTicks(intValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIntValue, typeTIMETICKS);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an OID value.The method
     * validates the value got after the snmp set operation against the set
     * value.
     */
    @Test
    public void testSetV1V2OIDValue()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final OID variable = new OID(setOIDValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            

            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setOIDValue, typeOID);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method for testing snmp V1/V2 set operation while all the input
     * parameters are valid.Here the value to be set is an IPADDRESS value.The
     * method validates the value got after the snmp set operation against the
     * set value.
     */
    @Test
    public void testSetV1V2IPADDRESSValue()
    {
        try
        {
            PowerMock.mockStaticNice(InetAddress.class);
            final InetAddress inetAddress = EasyMock.createMock(InetAddress.class);
            EasyMock.expect(InetAddress.getByName(targetIP)).andReturn(inetAddress);

            final UdpAddress address = PowerMock.createMockAndExpectNew(UdpAddress.class, inetAddress,
                    SnmpManager.DEFAULT_PORT_NUMBER);
            final TransportMapping transportMappings = PowerMock
                    .createMockAndExpectNew(DefaultUdpTransportMapping.class);

            final OctetString community = new OctetString(communityName);
            EasyMock.replay(inetAddress);
            PowerMock.replay(address, UdpAddress.class);
            PowerMock.replay(transportMappings, DefaultUdpTransportMapping.class);
            PowerMock.replay(InetAddress.class);

            final Snmp snmp = PowerMock.createMockAndExpectNew(Snmp.class, transportMappings);
            snmp.listen();
            EasyMock.expectLastCall();

            final CommunityTarget communityTarget = PowerMock.createMockAndExpectNew(CommunityTarget.class);
            communityTarget.setCommunity(community);
            EasyMock.expectLastCall();
            communityTarget.setVersion(SnmpConstants.version1);
            EasyMock.expectLastCall();
            communityTarget.setAddress(address);
            EasyMock.expectLastCall();
            communityTarget.setRetries(SnmpManager.DEFAULT_RETRIES);
            EasyMock.expectLastCall();
            communityTarget.setTimeout(SnmpManager.DEFAULT_TIMEOUT);
            EasyMock.expectLastCall();
            PowerMock.replay(communityTarget, CommunityTarget.class);

            final PDU pdu = PowerMock.createMockAndExpectNew(PDU.class);
            pdu.setType(PDU.SET);
            EasyMock.expectLastCall();
            pdu.add(EasyMock.createMock(VariableBinding.class));

            final OID oid = PowerMock.createMock(OID.class, oId);
            final IpAddress variable = new IpAddress(setIPValue);
            final VariableBinding variableBinding = PowerMock.createMockAndExpectNew(VariableBinding.class, oid,
                    variable);

            pdu.add(variableBinding);
            EasyMock.expectLastCall();
            snmp.close();
            EasyMock.expectLastCall();

            PowerMock.replay(oid, OID.class);
            final ResponseEvent responseEvent = EasyMock.createMock(ResponseEvent.class);
            EasyMock.expect(snmp.send(pdu, communityTarget)).andReturn(responseEvent);

            EasyMock.expect(responseEvent.getResponse()).andReturn(pdu).anyTimes();
            Vector< VariableBinding > variableBindings = new Vector< VariableBinding >();
            variableBindings.add(variableBinding);
            EasyMock.expect(pdu.getVariableBindings()).andReturn(variableBindings).anyTimes();
            EasyMock.expect(pdu.getErrorStatus()).andReturn(0).anyTimes();
            EasyMock.expect(pdu.getErrorStatusText()).andReturn("SNMP_ERROR_SUCCESS").anyTimes();
            
            EasyMock.expect(variableBinding.getVariable()).andReturn(variable);
            PowerMock.replay(snmp, Snmp.class);
            PowerMock.replay(pdu, PDU.class);
            EasyMock.replay(responseEvent);

            PowerMock.replay(variableBinding, VariableBinding.class);

            final SnmpServiceReturnMesage retnMsgActual = snmpManagerImpl.set(oId, communityName, targetIP,
                    SnmpManager.DEFAULT_PORT_NUMBER, setIPValue, typeIPADDRESS);

            assertEquals(variable.toString(), retnMsgActual.getResultObject());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
//    @Test
    public void snmpWalkIT()
    {
        try
        {
            SnmpManager snmpManager = new SnmpManagerImpl();
            SnmpServiceReturnMesage snmpServiceReturnMesage = snmpManager.walk( ".1.3.6.1.2.1.2.2.1.6", "public",
                    "192.168.160.48", 161 );
            System.out.println( snmpServiceReturnMesage.getComplexResultObject() );
            JAXBContext jaxbContext = JAXBContext.newInstance( SnmpServiceReturnMesage.class );
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal( snmpServiceReturnMesage, stringWriter );
            System.out.println( stringWriter );

            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader( stringWriter.getBuffer().toString() );
            snmpServiceReturnMesage = ( SnmpServiceReturnMesage ) unMarshaller.unmarshal( stringReader );
            System.out.println( snmpServiceReturnMesage.getComplexResultObject() );
        }
        catch ( JAXBException e )
        {
            e.printStackTrace();
        }

    }
}
