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
package com.comcast.cats.service.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.powermock.api.easymock.PowerMock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.service.IRManager;
import com.comcast.cats.service.KeyManager;

/**
 * The Class IRServiceWSImplTest.
 * 
 * @Author : Deepa
 * @since : 20th Sept 2012
 * @Description : The Class IRServiceWSImplTest is the unit test of {@link LegacyIRServiceHandler}.
 */
public class LegacyIRServiceHandlerTest
{
    public static final String REMOTE  = "COMCAST";
    public static final String FORMAT  = "PRONTO";
    public static final String KEY     = "GUIDE";
    public static final String IR_CODE = "DUMMY_CODE";
    public static URI          COMM_PATH;

    KeyManager                 mockKeyManager;
    IRManager                  mockIRManager;
    LegacyIRServiceFacade legacyFacade;

    @BeforeMethod
    public void before() throws URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        COMM_PATH = new URI( "gc100://localhost/?port=1" );
        mockKeyManager = createMock( KeyManager.class );
        mockIRManager = createMock( IRManager.class );

         legacyFacade =  new LegacyIRServiceHandler( mockIRManager, mockKeyManager );

    }

    @Test
    public void pressKey() throws UnknownHostException
    {
        IRCommunicator mockComm = createMock( IRCommunicator.class );
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, KEY ) ).andReturn( IR_CODE );
        expect( mockIRManager.retrieveIRCommunicator( COMM_PATH ) ).andReturn( mockComm );
        expect( mockComm.transmitIR( IR_CODE, 1, 1, 1 ) ).andReturn( true );
        replay( mockKeyManager, mockIRManager, mockComm );
        // Verify system under test.
        boolean rtn = legacyFacade.pressKey( COMM_PATH, REMOTE, RemoteCommand.GUIDE );
        assertTrue( rtn );
        verify( mockKeyManager, mockIRManager, mockComm );
    }

    @Test
    public void pressKeyNegative() throws UnknownHostException
    {
        IRCommunicator mockComm = createMock( IRCommunicator.class );
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, KEY ) ).andReturn( IR_CODE );
        expect( mockIRManager.retrieveIRCommunicator( COMM_PATH ) ).andReturn( mockComm );
        expect( mockComm.transmitIR( IR_CODE, 1, 1, 1 ) ).andReturn( false );
        replay( mockKeyManager, mockIRManager, mockComm );
        // Verify system under test.
        boolean rtn = legacyFacade.pressKey( COMM_PATH, REMOTE, RemoteCommand.GUIDE );
        assertFalse( rtn );
        verify( mockKeyManager, mockIRManager, mockComm );
    }

    @Test
    public void pressKeyNoIrCode()
    {
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, KEY ) ).andReturn( null );
        replay( mockKeyManager );
        // Verify system under test.
        boolean rtn = legacyFacade.pressKey( COMM_PATH, REMOTE, RemoteCommand.GUIDE );
        assertFalse( rtn );
        verify( mockKeyManager );
    }

    @Test
    public void pressKeyAndHold() throws UnknownHostException
    {
        IRCommunicator mockComm = createMock( IRCommunicator.class );
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, KEY ) ).andReturn( IR_CODE );
        expect( mockIRManager.retrieveIRCommunicator( COMM_PATH ) ).andReturn( mockComm );
        expect( mockComm.transmitIR( IR_CODE, 1, 20, 0 ) ).andReturn( true );
        replay( mockKeyManager, mockIRManager, mockComm );
        // Verify system under test.
        boolean rtn = legacyFacade.pressKeyAndHold( COMM_PATH, REMOTE, RemoteCommand.GUIDE, 20 );
        assertTrue( rtn );
        verify( mockKeyManager, mockIRManager, mockComm );
    }

    @Test
    public void pressKeyAndHoldNegative() throws UnknownHostException
    {
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, KEY ) ).andReturn( null );
        replay( mockKeyManager );
        // Verify system under test.
        boolean rtn = legacyFacade.pressKeyAndHold( COMM_PATH, REMOTE, RemoteCommand.GUIDE, 20 );
        assertFalse( rtn );
        verify( mockKeyManager );
    }

    @Test
    public void pressKeys() throws Exception
    {
        Integer count = 3;
        /*
         * Mock the pressKeys method to prevent having to mock the entire
         * interface as demonstrated above. Considering pressKey has already
         * been tested in isolation testing it with pressKeys is unnecessary.
         */
        LegacyIRServiceFacade mockIrService = PowerMock.createPartialMock( LegacyIRServiceHandler.class, "pressKey" );
        mockIrService.pressKey( COMM_PATH, REMOTE, RemoteCommand.GUIDE );
        PowerMock.expectLastCall().andReturn( true ).times( count );
        replay( mockIrService );
        List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
        for ( int i = 0; i < count; i++ )
        {
            commands.add( RemoteCommand.GUIDE );
        }
        boolean rtn = mockIrService.pressKeys( COMM_PATH, REMOTE, commands, 100 );
        assertTrue( "pressKeys should return true", rtn );
        verify( mockIrService );
    }

    @Test
    public void tune() throws Exception
    {
        List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
        commands.add( RemoteCommand.ONE );
        commands.add( RemoteCommand.ZERO );
        commands.add( RemoteCommand.ZERO );
        commands.add( RemoteCommand.ZERO );
        commands.add( RemoteCommand.SELECT );
        LegacyIRServiceFacade mockIrService = PowerMock.createPartialMock( LegacyIRServiceHandler.class, "pressKeys" );
        /* Return true for each pressKey call. */
        PowerMock.expectPrivate( mockIrService, "pressKeys", COMM_PATH, REMOTE, commands, 100 ).andReturn( true );
        replay( mockIrService );
        boolean rtn = mockIrService.tune( COMM_PATH, REMOTE, "1000", false, 100 );
        assertTrue( rtn );
        verify( mockIrService );
    }

    @Test
    public void tuneNegative()
    {
        LegacyIRServiceFacade irService = new LegacyIRServiceHandler();
        assertFalse( irService.tune( null, REMOTE, "1000", true, 0 ) );
        assertFalse( irService.tune( COMM_PATH, null, "1000", true, 0 ) );
    }

    @Test
    public void testSendText() throws Exception
    {
        IRCommunicator mockComm = createMock( IRCommunicator.class );
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, "ONE" ) ).andReturn( IR_CODE );
        expect( mockIRManager.retrieveIRCommunicator( COMM_PATH ) ).andReturn( mockComm );
        expect( mockComm.transmitIR( IR_CODE, 1, 1, 1 ) ).andReturn( true );
        replay( mockKeyManager, mockIRManager, mockComm );
        /* Verify system under test. */
        boolean rtn = legacyFacade.sendText( COMM_PATH, REMOTE, "1" );
        assertTrue( rtn );
        verify( mockKeyManager, mockIRManager, mockComm );
    }

    @Test
    public void testSendText_EmptyText() throws UnknownHostException
    {
        LegacyIRServiceFacade irService = new LegacyIRServiceHandler();
        assertFalse( irService.sendText( COMM_PATH, REMOTE, "" ) );
    }

    @Test
    public void testSendText_NULL() throws UnknownHostException
    {
        LegacyIRServiceFacade irService = new LegacyIRServiceHandler();
        assertFalse( irService.sendText( COMM_PATH, REMOTE, null ) );
    }

    @Test
    public void testEnterRemoteCommandSequence() throws Exception
    {
        ArrayList< RemoteCommandSequence > remoteCommandSeqList = new ArrayList< RemoteCommandSequence >();
        remoteCommandSeqList.add( new RemoteCommandSequence( RemoteCommand.GUIDE, 10, 2 ) );
        /* for repeat count >0 */
        LegacyIRServiceFacade mockIrService = PowerMock.createPartialMock( LegacyIRServiceHandler.class, "pressKeyAndHold" );
        PowerMock.expectPrivate( mockIrService, "pressKeyAndHold", COMM_PATH, REMOTE, RemoteCommand.GUIDE, 10 )
                .andReturn( true );
        // Return true for each pressKeyAndHold call.
        replay( mockIrService );
        boolean result = mockIrService.enterRemoteCommandSequence( COMM_PATH, REMOTE, remoteCommandSeqList );
        assertTrue( result );
        verify( mockIrService );
        /* for no repeat count */
        remoteCommandSeqList.clear();
        remoteCommandSeqList.add( new RemoteCommandSequence( RemoteCommand.GUIDE, 0, 2 ) );
        mockIrService = PowerMock.createPartialMock( LegacyIRServiceHandler.class, "pressKey" );
        PowerMock.expectPrivate( mockIrService, "pressKey", COMM_PATH, REMOTE, RemoteCommand.GUIDE ).andReturn( true );
        // Return true for each pressKey call.
        replay( mockIrService );
        result = mockIrService.enterRemoteCommandSequence( COMM_PATH, REMOTE, remoteCommandSeqList );
        assertTrue( result );
        verify( mockIrService );
    }

    @Test
    public void enterRemoteCommandSequenceNegative()
    {
        List< RemoteCommandSequence > remoteCommandSeqList = new ArrayList< RemoteCommandSequence >();
        remoteCommandSeqList.add( new RemoteCommandSequence( RemoteCommand.NINE, 10, 2 ) );
        /* testing for null */
        LegacyIRServiceFacade irService = new LegacyIRServiceHandler();
        assertFalse( irService.enterRemoteCommandSequence( null, REMOTE, remoteCommandSeqList ) );
        assertFalse( irService.enterRemoteCommandSequence( COMM_PATH, null, remoteCommandSeqList ) );
        assertFalse( irService.enterRemoteCommandSequence( COMM_PATH, REMOTE, null ) );
    }

    @Test
    public void testEnterCustomKeySequence() throws Exception
    {
        List< RemoteCommand > commandsList = new ArrayList< RemoteCommand >();
        commandsList.add( RemoteCommand.GUIDE );
        List< Integer > repeatCount = new ArrayList< Integer >();
        repeatCount.add( 20 );
        List< Integer > delay = new ArrayList< Integer >();
        delay.add( 100 );
        /* for repeatCount >0 */
        LegacyIRServiceFacade mockIrService = PowerMock.createPartialMock( LegacyIRServiceHandler.class, "pressKeyAndHold" );
        PowerMock.expectPrivate( mockIrService, "pressKeyAndHold", COMM_PATH, REMOTE, RemoteCommand.GUIDE, 20 )
                .andReturn( true );
        /* Return true for each pressKeyAndHold call. */
        replay( mockIrService );
        boolean result = mockIrService.enterCustomKeySequence( COMM_PATH, REMOTE, commandsList, repeatCount, delay );
        assertTrue( result );
        verify( mockIrService );
        /* for no repeat count */
        repeatCount.clear();
        repeatCount.add( 0 );
        mockIrService = PowerMock.createPartialMock( LegacyIRServiceHandler.class, "pressKey" );
        PowerMock.expectPrivate( mockIrService, "pressKey", COMM_PATH, REMOTE, RemoteCommand.GUIDE ).andReturn( true )
                .anyTimes();
        /* Return true for each pressKey call. */
        replay( mockIrService );
        result = mockIrService.enterCustomKeySequence( COMM_PATH, REMOTE, commandsList, repeatCount, delay );
        assertTrue( result );
        verify( mockIrService );
    }

    @Test
    public void enterCustomKeySequenceNegative()
    {
        List< RemoteCommand > commandsList = new ArrayList< RemoteCommand >();
        commandsList.add( RemoteCommand.ONE );
        List< Integer > repeatCount = new ArrayList< Integer >();
        repeatCount.add( 20 );
        List< Integer > delay = new ArrayList< Integer >();
        delay.add( 100 );
        /* testing for null */
        LegacyIRServiceFacade irService = new LegacyIRServiceHandler();
        assertFalse( irService.enterCustomKeySequence( null, REMOTE, commandsList, repeatCount, delay ) );
        assertFalse( irService.enterCustomKeySequence( COMM_PATH, null, commandsList, repeatCount, delay ) );
        assertFalse( irService.enterCustomKeySequence( COMM_PATH, REMOTE, null, repeatCount, delay ) );
        assertFalse( irService.enterCustomKeySequence( COMM_PATH, REMOTE, commandsList, null, delay ) );
        assertFalse( irService.enterCustomKeySequence( COMM_PATH, REMOTE, commandsList, repeatCount, null ) );
        repeatCount.add( 0 );
        /* commands size and repeatSize not matching */
        assertFalse( irService.enterCustomKeySequence( COMM_PATH, REMOTE, commandsList, repeatCount, delay ) );
        /* commands size and delay Size not matching */
        delay.add( 100 );
        assertFalse( irService.enterCustomKeySequence( COMM_PATH, REMOTE, commandsList, repeatCount, delay ) );
    }

    @Test
    public void testSendIR() throws UnknownHostException
    {
        IRCommunicator mockComm = createMock( IRCommunicator.class );
        expect( mockKeyManager.getIrCode( REMOTE, FORMAT, KEY ) ).andReturn( IR_CODE ).anyTimes();
        expect( mockIRManager.retrieveIRCommunicator( COMM_PATH ) ).andReturn( mockComm ).anyTimes();
        expect( mockComm.transmitIR( IR_CODE, 1, 1, 1 ) ).andReturn( true ).anyTimes();
        replay( mockKeyManager, mockIRManager, mockComm );
        /* Verify system under test. */
        boolean rtn = legacyFacade.sendIR( COMM_PATH, IR_CODE );
        assertTrue( rtn );
        verify( mockKeyManager, mockIRManager, mockComm );
    }

    @Test
    public void testSendIRNegative() throws UnknownHostException
    {
        boolean rtn = legacyFacade.sendIR( COMM_PATH, null );
        assertFalse( rtn );
    }
    
    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void testGetRemotes() throws UnknownHostException
    {
        legacyFacade.getRemotes();
    }
}
