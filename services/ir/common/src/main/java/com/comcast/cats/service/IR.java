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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.comcast.cats.RemoteLayout;
import com.comcast.cats.keymanager.domain.Remote;

@Produces( "application/xml" )
public interface IR
{
    @GET
    @Path( "/" )
    @Produces( "text/plain" )
    String get();

    @GET
    @Path( "/version" )
    @Produces( "text/plain" )
    String version();

    @GET
    @Path( "/remotes" )
    @Produces( "application/xml" )
    List< Remote > getRemotes();

    @GET
    @Path( "/remoteLayout" )
    @Produces( "application/xml" )
    List< RemoteLayout > getRemoteLayout( @QueryParam( "remoteType" )
    String remoteType );

    @POST
    @Path( "/pressKey" )
    @Produces( "text/plain" )
    Boolean pressKey( @QueryParam( "keySet" )
    String keySet, @QueryParam( "command" )
    String command );

    @POST
    @Path( "/pressKeys" )
    @Produces( "text/plain" )
    Boolean pressKeys( @QueryParam( "keySet" )
    String keySet, @QueryParam( "commandList" )
    String commandList, @QueryParam( "delayInMillis" )
    Integer delay );

    @POST
    @Path( "/pressKeyAndHold" )
    @Produces( "text/plain" )
    Boolean pressKeyAndHold( @QueryParam( "keySet" )
    String keySet, @QueryParam( "command" )
    String command, @QueryParam( "holdTime" )
    String holdTime );

    @POST
    @Path( "/customKeySeq" )
    @Produces( "text/plain" )
    Boolean enterCustomKeySequence( @QueryParam( "keySet" )
    String keySet, @QueryParam( "commandList" )
    String commandList, @QueryParam( "delayInMillis" )
    String delay, @QueryParam( "repeatCount" )
    String repeatCount );

    @POST
    @Path( "/remoteCommandSeq" )
    @Produces( "text/plain" )
    Boolean enterRemoteCommandSequence( @QueryParam( "keySet" )
    String keySet, @QueryParam( "command" )
    List< String > commandList );

    @POST
    @Path( "/tune" )
    @Produces( "text/plain" )
    Boolean tune( @QueryParam( "keySet" )
    String keySet, @QueryParam( "channel" )
    String channel, @QueryParam( "autoTune" )
    String autoTuneEnabled, @QueryParam( "delayInMillis" )
    String delayInMillis );

    @POST
    @Path( "/sendText" )
    @Produces( "text/plain" )
    Boolean sendText( @QueryParam( "keySet" )
    String keySet, @QueryParam( "string" )
    String stringToBeEntered );

    @POST
    @Path( "/sendIR" )
    @Consumes( "text/plain" )
    @Produces( "text/plain" )
    Boolean sendIR( String irCode );
}
