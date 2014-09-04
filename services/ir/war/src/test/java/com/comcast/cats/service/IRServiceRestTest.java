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
package com.comcast.cats.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;

public class IRServiceRestTest
{
    private NetworkIRServiceRest         irServiceRest;

    private String                host        = "192.168.120.102";
    private Integer               port        = 4;
    private String                type        = "gc100";
    private String                keySet      = "COMCAST";
    private String                command     = "GUIDE";

    private String                commands    = "GUIDE,MENU,EXIT";
    private String                delays      = "20,20,20";
    private String                counts      = "2,2,2";
    private Integer               delay       = 20;
    private Integer               count       = 2;

    private List< RemoteCommand > commandList = Arrays.asList( RemoteCommand.parse( "GUIDE" ),
                                                      RemoteCommand.parse( "MENU" ), RemoteCommand.parse( "EXIT" ) );

    private List< Integer >       delayList   = Arrays.asList( 20, 20, 20 );
    private List< Integer >       countList   = Arrays.asList( 2, 2, 2 );

    private String                irCode      = "";

    @BeforeMethod
    public void setUp()
    {
        irServiceRest = new NetworkIRServiceRest();
    }

    /**
     * Gets the mocked IR service.
     * 
     * @return the mocked IR service
     */
    private IRService getMockedIRService()
    {
        IRService irService = EasyMock.createMock( IRService.class );
        Whitebox.setInternalState( irServiceRest, "irService", irService );
        return irService;
    }

    @Test
    public void testPressKey() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.pressKey( path, keySet, RemoteCommand.parse( command ) ) ).andReturn( true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.pressKey( keySet, command ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testPressKeyFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.pressKey( path, keySet, RemoteCommand.parse( command ) ) ).andReturn( false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.pressKey( keySet, command ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testPressKeys() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.pressKeys( path, keySet, commandList, delay ) ).andReturn( true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.pressKeys( keySet, commands, delay ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testPressKeysFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.pressKeys( path, keySet, commandList, delay ) ).andReturn( false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.pressKeys( keySet, commands, delay ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testPressKeyAndHold() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.pressKeyAndHold( path, keySet, RemoteCommand.parse( command ), count ) ).andReturn(
                true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.pressKeyAndHold( keySet, command, String.valueOf( count ) ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testPressKeyAndHoldFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.pressKeyAndHold( path, keySet, RemoteCommand.parse( command ), count ) ).andReturn(
                false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.pressKeyAndHold( keySet, command, String.valueOf( count ) ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testCustomKeySeq() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.enterCustomKeySequence( path, keySet, commandList, countList, delayList ) )
                .andReturn( true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.enterCustomKeySequence( keySet, commands, delays, counts ) );
        EasyMock.verify( irService );
    }

    /*
     * @Test //Some issue in the assertion. to be checked public void
     * testRemoteCommandSeq() throws URISyntaxException{ IRService irService =
     * getMockedIRService(); URI path = new URI(type + "://" + host + "/?port="
     * + port); List<RemoteCommandSequence> commandSeq = new
     * ArrayList<RemoteCommandSequence>(); commandSeq.add(new
     * RemoteCommandSequence(RemoteCommand.parse("GUIDE"),2,20));
     * commandSeq.add(new
     * RemoteCommandSequence(RemoteCommand.parse("MENU"),3,10));
     * commandSeq.add(new
     * RemoteCommandSequence(RemoteCommand.parse("EXIT"),1,30));
     * EasyMock.expect(irService.enterRemoteCommandSequence(path, keySet,
     * commandSeq)).andReturn(true); EasyMock.replay( irService); List<String>
     * commands = Arrays.asList("GUIDE,2,20","MENU,3,10","EXIT,1,30");
     * Assert.assertTrue(irServiceRest.enterRemoteCommandSequence( keySet,
     * commands)); EasyMock.verify( irService ); }
     */

    @Test
    public void testCustomKeySeqFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.enterCustomKeySequence( path, keySet, commandList, countList, delayList ) )
                .andReturn( false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.enterCustomKeySequence( keySet, commands, delays, counts ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testTune() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.tune( path, keySet, "1000", false, 10 ) ).andReturn( true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.tune( keySet, "1000", "false", "10" ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testTuneFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.tune( path, keySet, "1000", false, 10 ) ).andReturn( false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.tune( keySet, "1000", "false", "10" ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testSendText() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.sendText( path, keySet, "12345678" ) ).andReturn( true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.sendText( keySet, "12345678" ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testSendTextFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.sendText( path, keySet, "12345678" ) ).andReturn( false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.sendText( keySet, "12345678" ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testSendIR() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.sendIR( path, irCode ) ).andReturn( true );
        EasyMock.replay( irService );
        Assert.assertTrue( irServiceRest.sendIR( irCode ) );
        EasyMock.verify( irService );
    }

    @Test
    public void testSendIRFailure() throws URISyntaxException
    {
        IRService irService = getMockedIRService();
        URI path = new URI( type + "://" + host + "/?port=" + port );
        EasyMock.expect( irService.sendIR( path, irCode ) ).andReturn( false );
        EasyMock.replay( irService );
        Assert.assertFalse( irServiceRest.sendIR( irCode ) );
        EasyMock.verify( irService );
    }

}
