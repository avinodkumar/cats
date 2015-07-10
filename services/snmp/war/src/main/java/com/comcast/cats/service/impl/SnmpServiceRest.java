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
package com.comcast.cats.service.impl;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.Snmp;
import com.comcast.cats.service.SnmpService;
import com.comcast.cats.service.exceptions.SNMPException;

import static com.comcast.cats.info.SnmpServiceConstants.SNMP_GET;
import static com.comcast.cats.info.SnmpServiceConstants.SNMP_SET;
import static com.comcast.cats.info.SnmpServiceConstants.JBOSS_URL_PKG_PREFIXES;

@Path( "/{deviceIP}/{snmpPort}" )
public class SnmpServiceRest implements Snmp
{

    private final static Logger LOGGER = LoggerFactory.getLogger( SnmpServiceRest.class );

    // @EJB(lookup
    // ="ejb:/snmp-service//SnmpServiceImpl!com.comcast.cats.service.SnmpService")
    private static SnmpService  snmpService;

    @PathParam( "deviceIP" )
    private String              deviceIP;

    @PathParam( "snmpPort" )
    @DefaultValue( "161" )
    private Integer             snmpPort;

    static
    {
        try
        {
            LOGGER.trace( "Inside SnmpServiceRest" );

            snmpService = lookupSnmpService();

        }
        catch ( NamingException e )
        {
            LOGGER.error( "Lookup of SNMP service failed :" + e );
        }
    }

    private static SnmpService lookupSnmpService() throws NamingException
    {

        final Hashtable< String, String > jndiProperties = new Hashtable< String, String >();
        jndiProperties.put( Context.URL_PKG_PREFIXES, JBOSS_URL_PKG_PREFIXES );
        final Context context = new InitialContext( jndiProperties );
        
        /*
         * This is the module name of the deployed EJBs on the server. This is
         * typically the jar/war name of the EJB deployment, without the
         * .jar/.war suffix, but can be overridden via the ejb-jar.xml
         */
        final String moduleName = "snmp-service";
        String lookupString = "java:app/"+moduleName+"/"+SnmpServiceImpl.class.getSimpleName()+"!"+SnmpService.class.getName();

        return ( SnmpService ) context.lookup( lookupString );
    }

    @Override
    @GET
    @Path( SNMP_GET )
    public SnmpServiceReturnMesage get( @QueryParam( "oID" )
    String oID, @DefaultValue( "public" )
    @QueryParam( "communityName" )
    String communityName, @DefaultValue( "" )
    @QueryParam( "userName" )
    String userName, @DefaultValue( "" )
    @QueryParam( "authenticatePassword" )
    String authenticatePassword, @DefaultValue( "" )
    @QueryParam( "privacyPassword" )
    String privacyPassword )
    {
        LOGGER.info( "Snmp get() : oId =" + oID + ", communityName= " + communityName + ", deviceIP =" + deviceIP
                + ", snmpPort=" + snmpPort + ", userName=" + userName + ", authenticatePassword="
                + authenticatePassword + ", privacyPassword=" + privacyPassword );

        SnmpServiceReturnMesage result = snmpService.get( oID, communityName, deviceIP, snmpPort, userName,
                authenticatePassword, privacyPassword );

        LOGGER.info( "result =" + result.getMessage() );
        
        return result;
    }

    @Override
    @PUT
    @Path( SNMP_SET )
    public SnmpServiceReturnMesage set( @QueryParam( "oID" )
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
    String privacyPassword )
    {

        LOGGER.info( "Snmp set() : oId =" + oID + ", communityName= " + communityName + ", deviceIP =" + deviceIP
                + ", snmpPort=" + snmpPort + ", userName=" + userName + ", authenticatePassword="
                + authenticatePassword + ", privacyPassword=" + privacyPassword + ", value =" + value + " , type = "
                + type );

        SnmpServiceReturnMesage result = snmpService.set( oID, communityName, deviceIP, snmpPort, value, type,
                userName, authenticatePassword, privacyPassword );
        return result;

    }
}
