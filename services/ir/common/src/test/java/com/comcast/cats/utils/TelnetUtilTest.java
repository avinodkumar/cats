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
package com.comcast.cats.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.comcast.cats.telnet.TelnetConnection;

public class TelnetUtilTest
{

    
    @Test
    public void connectTelnetNullTest()
    {
        assertFalse( TelnetUtil.connectTelnet( null ));
    }
    
    @Test
    public void connectTelnetTest()
    {
        MockTelnetConnection telnetConnection = new MockTelnetConnection( "x.x.x.x", 1, "");
        telnetConnection.simulateRespone( MockTelnetConnection.THROW_IOEXCEPTION );
        assertFalse(TelnetUtil.connectTelnet( telnetConnection ));
        
        telnetConnection.simulateRespone( MockTelnetConnection.THROW_SOCKETEXCEPTION );
        assertFalse(TelnetUtil.connectTelnet( telnetConnection ));
        
        telnetConnection.simulateRespone( MockTelnetConnection.RETURN_TRUE );
        assertTrue(TelnetUtil.connectTelnet( telnetConnection ));
        
        telnetConnection.simulateRespone( MockTelnetConnection.RETURN_FALSE );
        assertFalse(TelnetUtil.connectTelnet( telnetConnection ));
    }
    
    @Test
    public void sendTelnetCommandNullTest()
    {
        MockTelnetConnection telnetConnection = new MockTelnetConnection( "x.x.x.x", 1, "");
        assertTrue( TelnetUtil.sendTelnetCommand( null, "command","" ).startsWith( TelnetUtil.ERROR_STRING ));
        assertTrue( TelnetUtil.sendTelnetCommand( telnetConnection, null,"" ).startsWith( TelnetUtil.ERROR_STRING ));
    }
    
    @Test
    public void sendTelnetCommandTest()
    {
        MockTelnetConnection telnetConnection = new MockTelnetConnection( "x.x.x.x", 1, "");
        telnetConnection.simulateRespone( MockTelnetConnection.THROW_IOEXCEPTION );
        assertTrue( TelnetUtil.sendTelnetCommand( telnetConnection, "command","" ).startsWith( TelnetUtil.ERROR_STRING ));
        
        telnetConnection.simulateRespone( MockTelnetConnection.RETURN_NULL );
        assertTrue( TelnetUtil.sendTelnetCommand( telnetConnection, "command","" ).startsWith( TelnetUtil.ERROR_STRING ));
        
        telnetConnection.simulateRespone( MockTelnetConnection.RETURN_TRUE );
        assertFalse( TelnetUtil.sendTelnetCommand( telnetConnection, "command","" ).startsWith( TelnetUtil.ERROR_STRING ));
        
        telnetConnection.simulateRespone( MockTelnetConnection.RETURN_FALSE );
        assertFalse( TelnetUtil.sendTelnetCommand( telnetConnection, "command","" ).startsWith( TelnetUtil.ERROR_STRING ));
    }
}