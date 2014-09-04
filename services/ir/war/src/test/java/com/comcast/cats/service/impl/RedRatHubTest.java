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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.comcast.cats.service.ir.redrat.HubConnectionPool;
import com.comcast.cats.service.ir.redrat.IrNetBoxPro;
import com.comcast.cats.service.ir.redrat.RedRatDevice;
import com.comcast.cats.service.ir.redrat.RedRatHub;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.*;
import com.comcast.cats.telnet.TelnetConnection;

@RunWith(PowerMockRunner.class) 
@PrepareForTest(RedRatHub.class)
public class RedRatHubTest
{ 
    RedRatHub redRatHub;
    TelnetConnection hubConnection;
    
    Collection<RedRatDevice> irNetBoxList = new ArrayList<RedRatDevice>();
    IrNetBoxPro irNetBoxPro;
    private boolean enableWait = false;
    
    @Before
    public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        redRatHub = new RedRatHub("1.2.3.4",10);
        hubConnection= new MockTelnetConnection( "1.2.3.4", 10, "" );
        
        Field field = redRatHub.getClass().getDeclaredField("hubTelnetConnection");
        field.setAccessible(true);
        field.set(redRatHub, hubConnection);
        
        for(int i = 0; i< 5 ;i++){
            irNetBoxPro =  new MockIrNetBox( i, "1.2.3."+i );
            irNetBoxList.add( irNetBoxPro );
        }
    }
    
    @Test
    public void blackListAllIrNetBoxesTest(){
        redRatHub.blackListAllIrNetBoxes();
    }
    
    @Test
    public void whiteListIrNetBoxesTest(){
        redRatHub.whiteListRedRats( irNetBoxList );
    }
    
    @Test
    public void whiteListIrNetBoxesNullTest(){
        redRatHub.whiteListRedRats( null );
    }
    
    @Test
    public void blackListIrNetBoxesTest(){
        redRatHub.blackListRedRats( irNetBoxList );
    }
    
    @Test
    public void blackListIrNetBoxes(){
        redRatHub.blackListRedRats( null );
    }
    
    @Test
    public void getConnectionTest() throws Exception{
        PowerMock.createMock(TelnetConnection.class);
        PowerMock.expectNew(TelnetConnection.class,DEFAULT_REDRAT_HOST, DEFAULT_REDRAT_PORT,"\n").andReturn(new MockTelnetConnection( "", 1, "" )).anyTimes();
        PowerMock.replayAll();
        
        TelnetConnection telnetConnection1 = redRatHub.getConnection( irNetBoxPro, 0 );
        TelnetConnection telnetConnection2 = redRatHub.getConnection( irNetBoxPro, 0 );
        
        assertNotNull( telnetConnection1 );
        assertNotNull( telnetConnection2 );
        assertTrue( telnetConnection1.isConnected() );
        assertTrue( telnetConnection2.isConnected() );
        assertEquals( telnetConnection1, telnetConnection2 );
    }
    
    @Test
    public void getConnectionNullTest() throws Exception{
        TelnetConnection telnetConnection1 = redRatHub.getConnection( null, 0 );
        assertNull( telnetConnection1 );
        
        TelnetConnection telnetConnection2 = redRatHub.getConnection( irNetBoxPro, -10 );
        assertNull( telnetConnection2 );
    }
    
    @Test
    public void getConnectionNegativeTest() throws Exception{
        
        MockTelnetConnection notConnectiongConnection = new MockTelnetConnection( "", 1, "" );
        notConnectiongConnection.simulateRespone( MockTelnetConnection.RETURN_FALSE );
        
        PowerMock.createMock(TelnetConnection.class);
        PowerMock.expectNew(TelnetConnection.class,DEFAULT_REDRAT_HOST,DEFAULT_REDRAT_PORT,"\n").andReturn(notConnectiongConnection).anyTimes();
        PowerMock.replayAll();
        
        TelnetConnection telnetConnection1 = redRatHub.getConnection( irNetBoxPro, 7 ); //Since activeConnections is static use a port that hasnt been used yet.
        assertNull( telnetConnection1 );
    }
}