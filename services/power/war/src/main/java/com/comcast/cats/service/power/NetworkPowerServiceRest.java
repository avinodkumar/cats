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
package com.comcast.cats.service.power;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.Power;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceConstants;

@Path( "/{type}/{host}/{port}/{outlet}" )
public class NetworkPowerServiceRest implements Power
{
    private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    @EJB( mappedName = PowerServiceConstants.MAPPED_NAME )
    private PowerService powerService;

    @PathParam( "host" )
    private String       host;

    @PathParam( "port" )
    @DefaultValue( "0" )
    private Integer      port;

    @PathParam( "type" )
    private String       type;

    @PathParam( "outlet" )
    private Integer      outlet;

    public NetworkPowerServiceRest()
    {
        LOGGER.info( "POWER REST interface initialized for [host = " + host + "][port = " + port + "][outlet = "
                + outlet + "][type = " + type + "]" );
    }

    public String get()
    {
        return "[POWER][host = " + host + "][port = " + port + "][outlet = " + outlet + "][type = " + type + "]";
    }

    public String version()
    {
        return powerService.getVersion();
    }

    public Boolean off()
    {
        URI path;

        try
        {
            path = powerInfoToURI();
        }
        catch ( URISyntaxException e )
        {
            // Figure out how to return invalid request via 500, 404, etc.
            return false;
        }
        return powerService.hardPowerOff( path );
    }

    public Boolean on()
    {
        URI path;
        try
        {
            path = powerInfoToURI();
        }
        catch ( URISyntaxException e )
        {
            // Figure out how to return invalid request via 500, 404, etc.
            return false;
        }
        return powerService.hardPowerOn( path );
    }

    public Boolean reboot()
    {
        URI path;
        try
        {
            path = powerInfoToURI();
        }
        catch ( URISyntaxException e )
        {
            // Figure out how to return invalid request via 500, 404, etc.
            return false;
        }
        return powerService.hardPowerToggle( path );
    }

    public String status()
    {
        URI path;
        try
        {
            path = powerInfoToURI();
        }
        catch ( URISyntaxException e )
        {
            return "<result>FAILURE - Illegal Path was created</result>";
        }
        return powerService.powerStatus( path );
    }

    private URI powerInfoToURI() throws URISyntaxException
    {
        if ( port == 0 )
        {
            if ( type.equalsIgnoreCase( "wti1600" ) )
            {
                port = 23;
            }
            else if ( type.equalsIgnoreCase( "nps1600" ) )
            {
                port = 161;
            }
        }
        return new URI( type + "://" + host + ":" + port + "/?connectionport=" + outlet );

    }
}
