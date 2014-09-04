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

import com.comcast.cats.service.ir.redrat.IrNetBoxPro;
import com.comcast.cats.service.ir.redrat.RedRatHubConnection;
import com.comcast.cats.telnet.TelnetConnection;

public class MockIrNetBox extends IrNetBoxPro{

    boolean normalState = true;
    
    public MockIrNetBox( long id, String ipAddress )
    {
        super( id, ipAddress );
    }
    
    public void setErrorState(){
        normalState = false;
    }
    
    public void setNormalState(){
        normalState = true;
    }
    
    @Override
    public RedRatHubConnection getConnection( int portNumber )
    {
        MockTelnetConnection mockTelnet= new MockTelnetConnection( "1.2.3.4", 1, "" );
        if(!normalState){
            mockTelnet.simulateRespone(MockTelnetConnection.THROW_IOEXCEPTION);
        }
        return mockTelnet; 
    }
}