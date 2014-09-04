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
package com.comcast.cats.recorder;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;

/**
 * A utility class to manage VLC through telnet interface for video recorder.
 * 
 * @author ssugun00c
 * 
 */
public class TelnetHandler
{
    static
    {
        telnetPassword = System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD );
        telnetHost = System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST );
    }

    private TelnetClient  telnet = new TelnetClient();
    private InputStream   in;
    private PrintStream   out;
    private char          prompt = '>';
    private static String telnetHost;
    private static String telnetPassword;

    private static Logger LOGGER = LoggerFactory.getLogger( TelnetHandler.class );

    /**
     * Connect, authenticate and initialize vlc Telnet session
     * 
     * @param server
     * @param port
     * @param password
     * @throws SocketException
     * @throws IOException
     */
    private void connect( String server, Integer port, String password ) throws SocketException, IOException,
            VideoRecorderConnectionException
    {
        LOGGER.info( "[VLC][Telnet Connect][" + server + "][" + port + "]" );
        // Connect to the specified server
        telnet.connect( server, port );

        // Get input and output stream references
        in = telnet.getInputStream();
        out = new PrintStream( telnet.getOutputStream() );

        readUntil( "Password: " );
        write( password );

        // Advance to a prompt
        readUntil( prompt + " " );
    }

    /**
     * 
     * Reads Telnet response.
     * 
     * @param pattern
     * @return
     */
    private String readUntil( String pattern )
    {
        try
        {
            char lastChar = pattern.charAt( pattern.length() - 1 );
            StringBuffer sb = new StringBuffer();
            char ch = ( char ) in.read();
            while ( true )
            {
                sb.append( ch );
                if ( ch == lastChar )
                {
                    if ( sb.toString().endsWith( pattern ) )
                    {
                        LOGGER.info( "[VLC][Telnet Response][" + sb + "]" );

                        return sb.toString();
                    }
                }
                ch = ( char ) in.read();
            }
        }
        catch ( Exception e )
        {
            LOGGER.info( "[VLC][Telnet Error][" + e.getMessage() + "]" );
        }
        return null;
    }

    /**
     * Sends actual Telnet command.
     * 
     * @param value
     */
    private void write( String value ) throws VideoRecorderConnectionException
    {
        try
        {
            if ( null == out )
            {
                throw new VideoRecorderConnectionException(
                        "No OutputStream(PrintStream) found to send Telent command." );
            }

            LOGGER.info( "[VLC][Telnet Command][" + value + "]" );

            out.println( value );
            out.flush();
        }
        catch ( Exception e )
        {
            LOGGER.info( "[VLC][Telnet Error][" + e.getMessage() + "]" );
        }
    }

    /**
     * Sends a Telnet command and return the response.
     * 
     * @param command
     * @return
     */
    private String sendCommand( String command )
    {
        try
        {
            write( command );
            return readUntil( prompt + " " );
        }
        catch ( Exception e )
        {
            LOGGER.info( "[VLC][Telnet Error][" + e.getMessage() + "]" );
        }
        return null;
    }

    /**
     * Close Telnet session.
     */
    private void disconnect()
    {
        try
        {
            telnet.disconnect();
        }
        catch ( Exception e )
        {
            LOGGER.info( "[VLC][Telnet Error][" + e.getMessage() + "]" );
        }
    }

    public synchronized boolean shutDownVlc( int port ) throws VideoRecorderConnectionException
    {
        LOGGER.info( "[VLC][Telnet] Shutdown request received for port [" + port + "]" );

        if ( ( null == telnetPassword ) || ( telnetPassword.isEmpty() ) || ( null == telnetHost )
                || ( telnetHost.isEmpty() ) )
        {
            throw new VideoRecorderConnectionException( "Cannt connect to Telnet interface of VLC with [" + telnetHost
                    + "][" + port + "]. Make sure Telnet host and Telnet password are not null or empty" );
        }

        boolean isStopped = false;

        try
        {
            connect( telnetHost, port, telnetPassword );
            String output = sendCommand( "shutdown" );

            // String expected = "Shutting down.Bye-bye!>";
            String expected = "bye";

            output = output.replace( "\n", "" ).replace( "\r", "" ).trim();

            /*
             * if ( expected.equalsIgnoreCase( output ) ) { isStopped = true; }
             */

            if ( output.toLowerCase().indexOf( expected.toLowerCase() ) >= 0 )
            {
                isStopped = true;
            }

            disconnect();

        }
        catch ( IOException ioException )
        {
            throw new VideoRecorderConnectionException(
                    "An ioException happened while communicating with Telnet interface of VLC. [" + telnetHost + "]["
                            + port + "]" + ioException.getMessage() );
        }

        return isStopped;

    }
}