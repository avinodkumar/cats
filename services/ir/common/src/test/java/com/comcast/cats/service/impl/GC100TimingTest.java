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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.testng.annotations.Test;

/**
 * The Class GC100TimingTest.
 * 
 * @Author : cfrede001
 */
public class GC100TimingTest
{
    private static final int    GC_100_PORT          = 4998;
    private static final int    GC100_SOCKET_TIMEOUT = 5000;
    private static final String ipString             = "192.168.160.201";

    @Test
    public void SocketTimeTest()
    {
        try
        {
            InetAddress inetAddress = InetAddress.getByName( ipString );
            InetSocketAddress addr = new InetSocketAddress( inetAddress, GC_100_PORT );
            Socket m_socket = new Socket();
            m_socket.setSoTimeout( GC100_SOCKET_TIMEOUT );
            m_socket.connect( addr, GC100_SOCKET_TIMEOUT );
            BufferedReader m_sis = new BufferedReader( new InputStreamReader( m_socket.getInputStream() ) );
            OutputStream m_sos = m_socket.getOutputStream();
            int loops = 3;
            do
            {
                if ( m_socket.isConnected() )
                {
                    System.out.println( "Socket Still connected!" );
                }
                else
                {
                    System.out.println( "Socket disconnected!" );
                }

                if ( m_socket.isInputShutdown() )
                {
                    System.out.println( "Input Shutdown!" );
                }
                else
                {
                    System.out.println( "Input Open!" );
                }

                if ( m_socket.isOutputShutdown() )
                {
                    System.out.println( "Output Shutdown!" );
                }
                else
                {
                    System.out.println( "Output Open!" );
                }

                Thread.sleep( 300000 );
            } while ( loops-- > 0 );
        }
        catch (InterruptedException ex) 
		{
			LoggerFactory.getLogger(GC100TimingTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
		} catch (UnknownHostException ex) 
		{
			LoggerFactory.getLogger(GC100TimingTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
		} catch (SocketException ex) 
		{
			LoggerFactory.getLogger(GC100TimingTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
		} catch (IOException ex){
		
			LoggerFactory.getLogger(GC100TimingTest.class.getName()).error(MarkerFactory.getMarker("SEVERE"), "{}", ex );
		} 
    }
}
