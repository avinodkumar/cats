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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.ir.commands.DelayCommand;
import com.comcast.cats.ir.commands.PressKeyAndHoldCommand;
import com.comcast.cats.ir.commands.PressKeyCommand;
import com.comcast.cats.service.IrPort;
import com.comcast.cats.service.WebServiceReturn;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.ir.redrat.IrNetBoxPro;
import com.comcast.cats.service.ir.redrat.IrNetBoxProPort;


public class IrNetBoxProPortTest
{ 
   IrNetBoxPro irNetBox;
   IrNetBoxProPort irNetBoxPort;
   String ipAddress ="1.2.3.4";
   List<IrPort> irPorts = new ArrayList<IrPort>();
   String redratHubHostString = "2.3.4.5";
   int redratHubPort = 40000;
    
    @Before
    public void setup() throws URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        irNetBox = new MockIrNetBox( 0, ipAddress );
        
        for(int i=0; i<16;i++){
            irPorts.add( new IrNetBoxProPort( i, irNetBox ) );
        }
        
        irNetBox.setIrPorts( irPorts );
        
        irNetBoxPort = (IrNetBoxProPort)irNetBox.getPort( 0 );
    }
    
    @Test
    public void sendCommandNullTest(){
        WebServiceReturn response =  irNetBoxPort.sendCommand( null );
        assertEquals( WebServiceReturnEnum.FAILURE, response.getResult() );
    }
    
    @Test
    public void sendCommandTest(){

        WebServiceReturn response =  irNetBoxPort.sendCommand( new DelayCommand( 1000 ) );
        assertEquals( WebServiceReturnEnum.SUCCESS, response.getResult() );
        
        PressKeyCommand pressKeyCommand = new PressKeyCommand( RemoteCommand.GUIDE , "DTA" );
        response =  irNetBoxPort.sendCommand( pressKeyCommand );
        assertEquals( WebServiceReturnEnum.SUCCESS, response.getResult() );

        ((MockIrNetBox)irNetBox).setErrorState();
        irNetBoxPort = (IrNetBoxProPort)irNetBox.getPort( 0 );
        PressKeyAndHoldCommand pressAndHoldKeyCommand = new PressKeyAndHoldCommand( RemoteCommand.GUIDE , "DTA",10 );
        response =  irNetBoxPort.sendCommand( pressAndHoldKeyCommand );
        assertEquals( WebServiceReturnEnum.FAILURE, response.getResult() );
    }
}


