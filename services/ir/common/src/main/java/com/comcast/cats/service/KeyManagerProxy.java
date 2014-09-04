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

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.comcast.cats.keymanager.domain.IrDeviceDTO;

@Remote
public interface KeyManagerProxy {

    @GET
    @Path(KeyManagerConstants.REMOTES_PATH)
    @Produces("application/xml")
	public List<com.comcast.cats.keymanager.domain.Remote> remotes();
    
    @GET
    @Path(KeyManagerConstants.REMOTES_LATOUT_PATH)
    @Produces("application/xml")
	public List<com.comcast.cats.RemoteLayout> getRemoteLayout(@QueryParam("remoteType") String remote);
    @GET
    @Path(KeyManagerConstants.IR_DEVICES_PATH)
    @Produces("application/xml")
    public List< IrDeviceDTO > getIrDevices();
    
}