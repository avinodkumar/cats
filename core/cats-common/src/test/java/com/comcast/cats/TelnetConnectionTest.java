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
package com.comcast.cats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.comcast.cats.telnet.TelnetConnection;

public class TelnetConnectionTest
{
    TelnetConnection telnetConnecton;
    String host = "192.168.100.2";
    Integer port = 3000;
    String defaultPromptString = ">";
    String command = "command";
    private Boolean enableMock = true;
    private TelnetClient     telnetClient;
    private InputStream is;
    private OutputStream os;
    
    @Before
    public void setUp() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        telnetConnecton = new TelnetConnection( host, port, defaultPromptString );
       
        if(enableMock ){
            telnetClient = EasyMock.createMock( TelnetClient.class );
            is =  EasyMock.createMock( InputStream.class );
            os =  EasyMock.createMock( OutputStream.class );
            
            Field field = telnetConnecton.getClass().getDeclaredField("telnetClient");
            field.setAccessible(true);
            field.set(telnetConnecton, telnetClient);
            
            
            System.out.println("telnetClient setUp "+telnetClient);
        }
    }
    
    @After
    public void tearDown() throws IOException{
        telnetConnecton.disconnect();
        telnetConnecton = null;
    }
    
    @Test
    public void connectTest(){
        connectTelnet(true, null);
    }
    
    @Test
    public void connect1Test(){
        connectTelnet(false,null);
    }
    
    @Test
    public void connectWithPasswordTest(){
        connectTelnet(false,"password");
    }   
    
    @Test
    public void disconnectTest() throws IOException{
        connectTelnet(true, null);
        telnetConnecton.disconnect();
    }   
    
    @Test
    public void sendCommandTest(){
        try
        {
            assertNull( telnetConnecton.sendCommand( command ));// no connection established
            connectTelnet(false,null);       
            assertNotNull( telnetConnecton.sendCommand( command ));   
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void sendCommandNullTest(){
        connectTelnet(false,null);       
        try
        {
            assertNull(telnetConnecton.sendCommand( null ));
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }   
    }

    private void connectTelnet(Boolean isEnterRequired,String password){
        try
        {
            if(enableMock ){
                telnetClient.connect( host, port );
                EasyMock.expectLastCall().atLeastOnce();
                telnetClient.setSoTimeout( TelnetConnection.DEFAULT_READ_TIMEOUT );
                
                EasyMock.expectLastCall().anyTimes();
                EasyMock.expect(telnetClient.getInputStream()).andReturn( is ).anyTimes();
                EasyMock.expect(telnetClient.getOutputStream()).andReturn( os ).anyTimes();
                EasyMock.expect(telnetClient.isConnected()).andReturn( true ).anyTimes();
                telnetClient.disconnect();
                EasyMock.expectLastCall().anyTimes();
                
                EasyMock.replay( telnetClient );
                
                is.close();
                EasyMock.expectLastCall().atLeastOnce();
                EasyMock.expect(is.read()).andReturn( 62 ).anyTimes();
                EasyMock.replay( is );
                               
            }
            
            if(password == null){
                telnetConnecton.connect( isEnterRequired );
            }else{
                telnetConnecton.connectWithPassword( password, ">",  isEnterRequired );
            }
        }
        catch ( SocketException e )
        {   
            e.printStackTrace();
            fail();

        }
        catch ( IOException e )
        {
            e.printStackTrace();
            fail();

        }
    }
}
