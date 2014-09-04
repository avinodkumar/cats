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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Produces( "application/xml" )
public interface Power
{
    Integer DEFAULT_NPS_PORT     = 23;
    Integer DEFAULT_WTI1600_PORT = 161;

    @GET
    @Path( "/" )
    @Produces( "text/plain" )
    String get();

    @GET
    @Path( "/version" )
    @Produces( "text/plain" )
    String version();

    @POST
    @Path( "/off" )
    @Produces( "text/plain" )
    Boolean off();

    @POST
    @Path( "/on" )
    @Produces( "text/plain" )
    Boolean on();

    @POST
    @Path( "/reboot" )
    @Produces( "text/plain" )
    Boolean reboot();

    @GET
    @Path( "/status" )
    @Produces( "text/plain" )
    String status();
}
