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
package com.comcast.cats;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.Snmp;
import static com.comcast.cats.info.SnmpServiceConstants.DEFAULT_HOST_URL;

/**
 * Service Proxy for Snmp Service.
 * 
 * @author aswathyann
 */
public class SnmpServiceProxy
{

    private Snmp                snmp;

    private String              snmpServiceURL;

    private String              deviceIP;

    private Integer             snmpPort;

    private final static Logger LOGGER = LoggerFactory.getLogger( SnmpServiceProxy.class );
    static
    {
        ResteasyProviderFactory.setRegisterBuiltinByDefault( false );
    }

    public SnmpServiceProxy( String hostServerIP, String deviceIP, Integer snmpPort )
    {
        if ( hostServerIP == null )
        {
            hostServerIP = DEFAULT_HOST_URL;
        }

        this.snmpServiceURL = "http://" + hostServerIP + "/snmp-service/rest";

        this.deviceIP = deviceIP;

        this.snmpPort = snmpPort;
    }

    /**
     * Returns the Snmp proxy Object.
     */
    public Snmp getProxy()
    {

        snmpServiceURL = snmpServiceURL + "/" + deviceIP + "/" + snmpPort + "/";

        LOGGER.info( "[SNMP Service REST URL][" + snmpServiceURL + "]" );

        ResteasyProviderFactory factory = new ResteasyProviderFactory();
        RegisterBuiltin.register( factory );
        factory.getClientExecutionInterceptorRegistry().register( new ClientExecutionInterceptor()
        {
            @Override
            public ClientResponse execute( ClientExecutionContext ctx ) throws Exception
            {
                ClientResponse response = ctx.proceed();
                if ( ( "application/xml".equals( response.getHeaders().getFirst( HttpHeaders.CONTENT_TYPE ) ) )
                        || ( "text/plain".equals( response.getHeaders().getFirst( HttpHeaders.CONTENT_TYPE ) ) ) )
                {
                    response.getHeaders().putSingle( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML );
                }
                return response;
            }
        } );

        this.snmp = ProxyFactory.create( Snmp.class, URI.create( snmpServiceURL ), new ApacheHttpClient4Executor(),
                factory );

        // this.snmp = ProxyFactory.create( Snmp.class, snmpServiceURL );

        return snmp;
    }
}
