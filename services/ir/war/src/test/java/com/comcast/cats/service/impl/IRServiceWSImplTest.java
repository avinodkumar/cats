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

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.service.IRManager;
import com.comcast.cats.service.IRServiceProvider;
import com.comcast.cats.service.IRServiceVersionGetter;
import com.comcast.cats.service.KeyManager;


public class IRServiceWSImplTest
{ 
    URI          path;
    RemoteCommand command = RemoteCommand.GUIDE; 
    String irKeySet = "DTA";
    int count = 10;
    int delayMillis = 1000;
    ArrayList< RemoteCommand > commands = new ArrayList<RemoteCommand>();
    ArrayList< Integer > repeatCounts = new ArrayList<Integer>();
    ArrayList< Integer > delays = new ArrayList<Integer>();
    ArrayList< RemoteCommandSequence > commandSequence = new ArrayList<RemoteCommandSequence>();

    KeyManager                 mockKeyManager;
    IRManager                  mockIRManager;

    IRServiceWSImpl irService;

    IRServiceProvider irServiceProvider;
    IRServiceVersionGetter versionGetter;
    LegacyIRServiceFacade legacyFacade;
    
    @Before
    public void setup() throws URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        
        path = new URI("gc100://localhost/?port=1");
        commands.add( RemoteCommand.GUIDE );
        commands.add( RemoteCommand.MENU );
        
        repeatCounts.add( 10 );
        repeatCounts.add( 10 );
        
        delays.add( 10 );
        delays.add( 10 );
        
        irService = new IRServiceWSImpl();
        irServiceProvider = EasyMock.createMock( IRServiceFacadeRetriever.class );
        versionGetter = EasyMock.createMock( IRServiceVersionGetter.class );
        legacyFacade = EasyMock.createMock( LegacyIRServiceHandler.class );
        
        Field field = irService.getClass().getDeclaredField("irServiceProvider");
        field.setAccessible(true);
        field.set(irService, irServiceProvider);
    }

    @Test
    public void pressKeyTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.pressKey( path, irKeySet, command )).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.pressKey( path, irKeySet, command ));
    }
    
    @Test
    public void pressKeyNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.pressKey( path, irKeySet, command ));
    }
    
    @Test
    public void pressKeyAndHoldTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.pressKeyAndHold( path, irKeySet, command, count )).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.pressKeyAndHold( path, irKeySet, command, count ));
    }
    
    @Test
    public void pressKeyAndHoldNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.pressKeyAndHold( path, irKeySet, command,count ));
    }
    
    @Test
    public void pressKeysTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.pressKeys(  path, irKeySet, commands, delayMillis  )).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.pressKeys( path, irKeySet, commands, delayMillis ));
    }
    
    @Test
    public void pressKeysNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.pressKeys( path, irKeySet, commands, delayMillis ));
    }
    
    @Test
    public void tuneTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.tune( path,irKeySet,"101", true,delayMillis)).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.tune( path,irKeySet,"101", true,delayMillis));
    }
    
    @Test
    public void tuneNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.tune( path,irKeySet,"101", true,delayMillis));
    }
    
    @Test
    public void sendIrTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.sendIR( path, "1001001001" )).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.sendIR( path, "1001001001" ));
    }
    
    @Test
    public void sendIrNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.sendIR( path, "1001001001" ));
    }
    
    @Test
    public void enterCustomKeySequenceTest()
    {
        
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.enterCustomKeySequence(path,irKeySet,commands,repeatCounts,delays)).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.enterCustomKeySequence(path,irKeySet,commands,repeatCounts,delays));
    }
    
    @Test
    public void enterCustomKeySequenceNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.enterCustomKeySequence(path,irKeySet,commands,repeatCounts,delays));
    }
    
    
    @Test
    public void sendTextTest()
    {
        
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.sendText(path, irKeySet, "stringToBeEntered")).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.sendText(path, irKeySet, "stringToBeEntered"));
    }
    
    @Test
    public void sendTextNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.sendText(path, irKeySet, "stringToBeEntered"));
    }
    
    @Test
    public void enterRemoteCommandSequenceTest()
    {
        
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( legacyFacade ).anyTimes();
        EasyMock.expect( legacyFacade.enterRemoteCommandSequence(path, irKeySet, commandSequence)).andReturn( true ).anyTimes();

        EasyMock.replay( irServiceProvider );
        EasyMock.replay( legacyFacade );

        assertTrue(irService.enterRemoteCommandSequence(path, irKeySet, commandSequence));
    }
    
    @Test
    public void enterRemoteCommandSequenceNegativeTest()
    {
        EasyMock.expect(irServiceProvider.getIRService(path)).andReturn( null ).anyTimes();
        EasyMock.replay( irServiceProvider );
        
        assertFalse(irService.enterRemoteCommandSequence(path, irKeySet, commandSequence));
    }
}
