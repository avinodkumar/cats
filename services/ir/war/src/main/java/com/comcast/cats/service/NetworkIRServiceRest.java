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
package com.comcast.cats.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.keymanager.domain.Remote;

@Path( "/{type}/{host}/{port}" )
public class NetworkIRServiceRest implements IR
{
    private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    @EJB( mappedName = IRServiceConstants.MAPPED_NAME )
    private IRService    irService;

    @PathParam( "host" )
    private String       host;

    @PathParam( "port" )
    private String       port;

    @PathParam( "type" )
    private String       type;

    public NetworkIRServiceRest()
    {
        LOGGER.info( "IR REST interface initialized for [host = " + host + "][port = " + port + "][type = " + type
                + "]" );
    }

    public String get()
    {
        return "[IR][host = " + host + "][port = " + port + "][type = " + type + "]";
    }

    public String version()
    {
        return irService.getVersion();
    }

    public List< Remote > getRemotes()
    {
        return irService.getRemotes();
    }

    @Override
    public List< RemoteLayout > getRemoteLayout( String remoteType )
    {
        return irService.getRemoteLayout( remoteType );
    }

    public Boolean pressKey( String keySet, String command )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException uriSynExc )
        {
            return false;
        }
        RemoteCommand rCommand = RemoteCommand.parse( command );
        return irService.pressKey( path, keySet, rCommand );
    }

    public Boolean pressKeys( String keySet, String commandList, Integer delay )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException uriSynExc )
        {
            return false;
        }

        List< RemoteCommand > rCommandList = geRemoteCommandList( commandList );
        return irService.pressKeys( path, keySet, rCommandList, delay );
    }

    public Boolean pressKeyAndHold( String keySet, String command, String holdTime )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException e )
        {
            return false;
        }
        RemoteCommand rCommand = RemoteCommand.parse( command );

        return irService.pressKeyAndHold( path, keySet, rCommand, new Integer( holdTime ) );
    }

    public Boolean enterCustomKeySequence( String keySet, String commandList, String delay, String repeatCount )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException uriSynExc )
        {
            return false;
        }
        List< RemoteCommand > rCommandList = geRemoteCommandList( commandList );
        List< Integer > delayList = getIntegerList( delay );
        List< Integer > countList = getIntegerList( repeatCount );
        return irService.enterCustomKeySequence( path, keySet, rCommandList, countList, delayList );
    }

    public Boolean enterRemoteCommandSequence( String keySet, List< String > commandList )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException uriSynExc )
        {
            return false;
        }
        List< RemoteCommandSequence > rCommandSeqList = getRemoteCommandSeqList( commandList );
        return irService.enterRemoteCommandSequence( path, keySet, rCommandSeqList );

    }

    public Boolean tune( String keySet, String channel, String autoTuneEnabled, String delayInMillis )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException e )
        {
            return false;
        }
        return irService.tune( path, keySet, channel, Boolean.parseBoolean( autoTuneEnabled ),
                Integer.parseInt( delayInMillis ) );
    }

    public Boolean sendText( String keySet, String stringToBeEntered )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException e )
        {
            return false;
        }
        return irService.sendText( path, keySet, stringToBeEntered );
    }

    public Boolean sendIR( String irCode )
    {
        URI path;
        try
        {
            path = irURI();
        }
        catch ( URISyntaxException e )
        {
            return false;
        }
        return irService.sendIR( path, irCode );
    }

    private URI irURI() throws URISyntaxException
    {
        String str = type + "://" + host + "/?port=" + port;
        URI path = new URI( str );
        return path;
    }

    private List< RemoteCommand > geRemoteCommandList( String commands )
    {
        StringTokenizer strTok = new StringTokenizer( commands, "," );

        List< RemoteCommand > rCommandList = new ArrayList< RemoteCommand >();
        while ( strTok.hasMoreTokens() )
        {
            RemoteCommand rCommand = RemoteCommand.parse( strTok.nextToken() );
            rCommandList.add( rCommand );
        }
        return rCommandList;
    }

    private List< RemoteCommandSequence > getRemoteCommandSeqList( List< String > commands )
    {
        List< RemoteCommandSequence > rCommandSeqList = new ArrayList< RemoteCommandSequence >();
        for ( String cmd : commands )
        {
            StringTokenizer strTok = new StringTokenizer( cmd, "," );
            RemoteCommand rCommand = RemoteCommand.parse( strTok.nextToken() );
            int repeatCount = Integer.parseInt( strTok.nextToken() );
            int delay = Integer.parseInt( strTok.nextToken() );
            rCommandSeqList.add( new RemoteCommandSequence( rCommand, repeatCount, delay ) );
        }
        return rCommandSeqList;
    }

    private List< Integer > getIntegerList( String integerList )
    {
        StringTokenizer strTok = new StringTokenizer( integerList, "," );
        List< Integer > intList = new ArrayList< Integer >();
        while ( strTok.hasMoreTokens() )
        {
            Integer intVal = new Integer( strTok.nextToken() );
            intList.add( intVal );
        }
        return intList;
    }

}
