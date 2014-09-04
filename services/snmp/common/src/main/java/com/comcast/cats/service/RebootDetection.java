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

import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootHostStatus;
import com.comcast.cats.reboot.RebootInfo;

public interface RebootDetection
{

    @GET
    @Path( value = "/version" )
    public String version();

    @POST
    @Path( value = "/add" )
    public void add( @QueryParam( "ip" )
    String ip, @QueryParam( "ecm" )
    String ecmMacAddress, @QueryParam( "status" )
    RebootHostStatus status );

    @POST
    @Path( value = "/update" )
    public void update( @QueryParam( "ip" )
    String ip, @QueryParam( "ecm" )
    String ecmMacAddress, @QueryParam( "status" )
    RebootHostStatus status );

    @DELETE
    @Path( value = "/delete" )
    public void delete();

    @GET
    @Path( value = "/current" )
    @Produces(
        { "application/xml", "application/json" } )
    public MonitorTarget current();

    @GET
    @Path( value = "/count" )
    public Integer count();

    @GET
    @Path( value = "/listAll" )
    @Produces(
        { "application/xml", "application/json" } )
    public List< RebootInfo > listAll();

    @GET
    @Path( value = "/list" )
    @Produces(
        { "application/xml", "application/json" } )
    public List< RebootInfo > list( @QueryParam( "offset" )
    Integer offset, @QueryParam( "max" )
    Integer max, @QueryParam( "startDt" )
    Date start, @QueryParam( "endDt" )
    Date end );
}
