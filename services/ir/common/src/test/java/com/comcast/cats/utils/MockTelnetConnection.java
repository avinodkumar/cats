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

import java.io.IOException;
import java.net.SocketException;

import com.comcast.cats.telnet.TelnetConnection;

public class MockTelnetConnection extends TelnetConnection
{
    
    public static final int THROW_IOEXCEPTION = 1;
    public static final int THROW_SOCKETEXCEPTION = 2;
    public static final int RETURN_TRUE =3;
    public static final int RETURN_FALSE = 4;
    public static final int RETURN_NULL = 5;
    
    int responseType = RETURN_TRUE;

    public MockTelnetConnection( String host, Integer port, String defaultPromptString )
    {
        super( host, port, defaultPromptString );
    }
    
    public synchronized boolean connect(boolean isEnterRequired) throws SocketException, IOException
    {
        if(responseType == THROW_IOEXCEPTION){
            throw new IOException();
        }else if(responseType == THROW_SOCKETEXCEPTION){
            throw new SocketException();
        }else if(responseType == RETURN_TRUE){
            return true;
        }else if(responseType == RETURN_FALSE){
            return false;
        }
        return true;
    }
    
    public void simulateRespone(int responseType){
        this.responseType = responseType;
    }
    
    public synchronized String sendCommand( String command ) throws IOException
    {
        if(responseType == THROW_IOEXCEPTION){
            throw new IOException();
        }else if(responseType == RETURN_NULL){
            return null;
        }else if(responseType == RETURN_TRUE){
            return "Dummy";
        }else if(responseType == RETURN_FALSE){
            return "";
        }
        
        return "Dummy";
    }

}
