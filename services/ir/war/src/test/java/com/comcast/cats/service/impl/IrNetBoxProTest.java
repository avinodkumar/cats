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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.service.IrPort;
import com.comcast.cats.service.ir.redrat.IrNetBoxPro;
import com.comcast.cats.service.ir.redrat.IrNetBoxProPort;

public class IrNetBoxProTest
{ 
   IrNetBoxPro irNetBox;
   String ipAddress ="1.2.3.4";
   List<IrPort> irPorts = new ArrayList<IrPort>();
   String redratHubHostString = "2.3.4.5";
   int redratHubPort = 40000;
    
    @Before
    public void setup() throws URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        irNetBox = new IrNetBoxPro( 0, ipAddress );
        
        for(int i=0; i<16;i++){
            irPorts.add( new IrNetBoxProPort( i, irNetBox ) );
        }
        
        irNetBox.setIrPorts( irPorts );
    }

    @Test
    public void setGetTest()
    {
        irNetBox.setIrPorts( irPorts );
        assertEquals( irPorts, irNetBox.getIrPorts() );
        
        irNetBox.setRedratHubHost( redratHubHostString );
        assertEquals( redratHubHostString, irNetBox.getRedratHubHost() );
        
        irNetBox.setRedratHubPort( redratHubPort );
        assertEquals( redratHubPort, irNetBox.getRedratHubPort() );
    }
    
    @Test
    public void getPortTest()
    {
        assertNotNull( irNetBox.getPort( 4 ) );
        
        irNetBox.setIrPorts( null );
        assertNull( irNetBox.getPort( 0 ));
        
        irNetBox.setIrPorts( new ArrayList<IrPort>() );
        assertNull( irNetBox.getPort( 0 ));
        
        irNetBox.setIrPorts( new ArrayList<IrPort>() );
        assertNull( irNetBox.getPort( 999 ));

    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void sendCommandTest()
    {
        irNetBox.sendCommand( "command" );
    }
    
    @Test
    public void getConnectionTest()
    {
        assertNull( irNetBox.getConnection( 999 ));
    }
 
    @Test
    public void equalsTest()
    {
        IrNetBoxPro newDevice = new IrNetBoxPro( 0, ipAddress );
        assertTrue( irNetBox.equals( newDevice ));
        IrNetBoxPro newDevice1 = new IrNetBoxPro( 2, ipAddress );
        assertFalse( irNetBox.equals( newDevice1 ));
        IrNetBoxPro newDevice2 = new IrNetBoxPro( 0, "x.x.x.x" );
        assertFalse( irNetBox.equals( newDevice2 ));
        
      
        assertFalse( irNetBox.equals( null ));
    }
 
}
