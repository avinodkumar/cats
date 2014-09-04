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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.service.IrPort;
import com.comcast.cats.service.RedRatManager;
import com.comcast.cats.service.ir.redrat.IrNetBoxPro;
import com.comcast.cats.service.ir.redrat.RedRatIRServiceHandler;
import com.comcast.cats.service.ir.redrat.RedRatManagerImpl;


public class RedRatIRServiceHandlerTest
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
    String ip = "10.222.222.222";
    int port = 1;
    
    IrNetBoxPro irnetBox;
    
    RedRatIRServiceHandler irService;
    
    RedRatManager redRatManager;
    
    @Before
    public void setup() throws URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        
        redRatManager = EasyMock.createMock( RedRatManagerImpl.class );
        irService = new RedRatIRServiceHandler();
        
        Field field = irService.getClass().getDeclaredField("redratManager");
        field.setAccessible(true);
        field.set(irService, redRatManager);
        
        path = new URI("irnetboxpro3://"+ip+"/?port="+port);
        commands.add( RemoteCommand.GUIDE );
        commands.add( RemoteCommand.MENU );
        
        repeatCounts.add( 10 );
        repeatCounts.add( 10 );
        
        delays.add( 10 );
        delays.add( 10 );

        irnetBox = new IrNetBoxPro( 0, ip );
        List<IrPort> irPorts = new ArrayList<IrPort>();
        IrPort irPort = new MockIrNetBoxPort( port, irnetBox );
        irPorts.add( irPort );
        irnetBox.setIrPorts( irPorts );
    }

    @Test
    public void pressKeyTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertTrue(irService.pressKey( path, irKeySet, command ));
    }
    
    @Test
    public void pressKeyNegativeTest()
    {       
        assertFalse(irService.pressKey( null, irKeySet, command ));
        assertFalse(irService.pressKey( path, null, command ));
        assertFalse(irService.pressKey( path, irKeySet, null ));
    }
    
    @Test
    public void pressKeyAndHoldTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertTrue(irService.pressKeyAndHold( path, irKeySet, command ,count));
    }
    
    @Test
    public void pressKeyAndHoldNegativeTest()
    {       
        assertFalse(irService.pressKeyAndHold( null, irKeySet, command,count ));
        assertFalse(irService.pressKeyAndHold( path, null, command,count ));
        assertFalse(irService.pressKeyAndHold( path, irKeySet, null,count ));
        assertFalse(irService.pressKeyAndHold( path, irKeySet, command,-1 ));
    }
  
    
    @Test
    public void pressKeysTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertTrue(irService.pressKeys( path, irKeySet, commands, delayMillis ));
    }
    
    @Test
    public void pressKeysNegativeTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertFalse(irService.pressKeys( null, irKeySet, commands, delayMillis ));
        assertFalse(irService.pressKeys( path, null, commands, delayMillis ));
        assertFalse(irService.pressKeys( path, irKeySet, null, delayMillis ));
        assertFalse(irService.pressKeys( path, irKeySet, commands, -1 ));
    }
    
    @Test
    public void tuneTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox ).anyTimes();
        EasyMock.replay( redRatManager );

        assertTrue(irService.tune( path,irKeySet,"101", true,delayMillis));
        assertTrue(irService.tune( path,irKeySet,"101", false,delayMillis));
    }
    
    @Test
    public void tuneNegativeTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );

        assertFalse(irService.tune( null,irKeySet,"101", true,delayMillis));
        assertFalse(irService.tune( path,null,"101", true,delayMillis));
        assertFalse(irService.tune( path,irKeySet,null, true,delayMillis));
        assertFalse(irService.tune( path,irKeySet,"101", true,-1));
    }
    
    @Test(expected= UnsupportedOperationException.class)
    public void sendIrTest()
    {
        irService.sendIR( path, "1001001001" );
    }
    
    @Test
    public void enterCustomKeySequenceTest()
    {
        
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );

        assertTrue(irService.enterCustomKeySequence(path,irKeySet,commands,repeatCounts,delays));
    }
    
    @Test
    public void enterCustomKeySequenceNegativeTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertFalse(irService.enterCustomKeySequence(null,irKeySet,commands,repeatCounts,delays));
        assertFalse(irService.enterCustomKeySequence(path,null,commands,repeatCounts,delays));
        assertFalse(irService.enterCustomKeySequence(path,irKeySet,null,repeatCounts,delays));
        assertFalse(irService.enterCustomKeySequence(path,irKeySet,commands,null,delays));
        assertFalse(irService.enterCustomKeySequence(path,irKeySet,commands,repeatCounts,null));
    }
    
    
    @Test
    public void sendTextTest()
    {
        
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        

        assertTrue(irService.sendText(path, irKeySet, "123"));
    }
    
    @Test
    public void sendTextNegativeTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertFalse(irService.sendText(null, irKeySet, "stringToBeEntered"));
        assertFalse(irService.sendText(path, null, "stringToBeEntered"));
        assertFalse(irService.sendText(path, irKeySet, null));
    }
    
    @Test
    public void enterRemoteCommandSequenceTest()
    {
        
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        commandSequence.add( new RemoteCommandSequence() );
        assertTrue(irService.enterRemoteCommandSequence(path, irKeySet, commandSequence));
    }
    
    @Test
    public void enterRemoteCommandSequenceNegativeTest()
    {
        EasyMock.expect( redRatManager.getIrDevice( ip ) ).andReturn( irnetBox );
        EasyMock.replay( redRatManager );
        
        assertFalse(irService.enterRemoteCommandSequence(null, irKeySet, commandSequence));
        assertFalse(irService.enterRemoteCommandSequence(path, null, commandSequence));
        assertFalse(irService.enterRemoteCommandSequence(path, irKeySet, null));
    }
}
