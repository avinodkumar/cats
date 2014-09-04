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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.comcast.cats.info.SnmpServiceReturnMesage;

import static com.comcast.cats.info.SnmpServiceConstants.SNMP_GET;
import static com.comcast.cats.info.SnmpServiceConstants.SNMP_SET;

@Produces( MediaType.APPLICATION_XML )
public interface Snmp
{

    @GET
    @Path( SNMP_GET )
    @Produces( MediaType.APPLICATION_XML )
    SnmpServiceReturnMesage get( @QueryParam( "oID" )
    String oID, @DefaultValue( "public" )
    @QueryParam( "communityName" )
    String communityName, @DefaultValue( "" )
    @QueryParam( "userName" )
    String userName, @DefaultValue( "" )
    @QueryParam( "authenticatePassword" )
    String authenticatePassword, @DefaultValue( "" )
    @QueryParam( "privacyPassword" )
    String privacyPassword );

    @PUT
    @Path( SNMP_SET )
    @Produces( MediaType.APPLICATION_XML )
    SnmpServiceReturnMesage set( @QueryParam( "oID" )
    String oID, @DefaultValue( "public" )
    @QueryParam( "communityName" )
    String communityName, @QueryParam( "value" )
    String value, @QueryParam( "type" )
    String type, @DefaultValue( "" )
    @QueryParam( "userName" )
    String userName, @DefaultValue( "" )
    @QueryParam( "authenticatePassword" )
    String authenticatePassword, @DefaultValue( "" )
    @QueryParam( "privacyPassword" )
    String privacyPassword );
}
