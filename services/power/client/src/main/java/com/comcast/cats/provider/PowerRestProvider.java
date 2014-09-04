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
package com.comcast.cats.provider;

import java.net.URI;
import java.net.URISyntaxException;

import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.annotation.ExclusiveAccess;
import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.service.Power;

public class PowerRestProvider implements PowerProvider
{
    private Logger            logger           = LoggerFactory.getLogger( getClass() );
    private static final long serialVersionUID = 1L;
    private final Power       power;
    private Object            parent;
    private final URI         path;

    public PowerRestProvider( String powerServiceURL, String host, Integer hostPort, Integer outlet, String type )
                                                                                                              throws URISyntaxException
    {
        powerServiceURL = powerServiceURL + type + "/" + host + "/" + hostPort + "/" + outlet;

        logger.info( "[POWER Service REST URL][" + powerServiceURL + "]" );
       
        this.power = ProxyFactory.create( Power.class, powerServiceURL );
        this.parent = null;
        this.path = new URI( type + "//" + host + ":" + hostPort + "/?port=" + outlet );
    }

    public PowerRestProvider( Power power, String host, Integer port, Integer outlet, String type )
                                                                                                   throws URISyntaxException
    {
        this( power, host, port, outlet, type, null );
    }

    public PowerRestProvider( Power power, String host, Integer hostPort, Integer outlet, String type, Object parent )
                                                                                                                  throws URISyntaxException
    {
        super();
        this.power = power;
        this.parent = parent;
        this.path = new URI( type + "//" + host + ":" + hostPort + "/?port=" + outlet );
        
        
    }

    @Override
    public Object getParent()
    {
        return parent;
    }

    @Override
    public URI getPowerLocator()
    {
        return path;
    }

    @Override
    @ExclusiveAccess
    public void powerOn() throws PowerProviderException
    {
        handleResponse( power.on(), "ON" );
    }

    @Override
    @ExclusiveAccess
    public void powerOff() throws PowerProviderException
    {
        handleResponse( power.off(), "OFF" );
    }

    @Override
    @ExclusiveAccess
    public void reboot() throws PowerProviderException
    {
        handleResponse( power.reboot(), "REBOOT" );        
    }

    @Override
    @ExclusiveAccess
    public String getPowerStatus()
    {
        return power.status();
    }

    protected void handleResponse( Boolean rtn, String operation ) throws PowerProviderException
    {
        if ( !rtn )
        {
            throw new PowerProviderException( "PowerRest " + operation + " operation failed" );
        }
    }
}
