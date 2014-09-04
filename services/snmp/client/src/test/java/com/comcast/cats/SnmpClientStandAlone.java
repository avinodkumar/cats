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

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.SnmpServiceConstants;
import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.SnmpService;

/**
 * WEBLOGIC DEPLOYMENT
 * SNMP stand alone client. To run , include wlfullclient.jar in Build Path if
 * weblogic is used. 
 * 
 * JBOSS DEPLOYMENT
 * In case of the JBoss server, include jbossall-client.jar
 * and all jar files specified in the jbossall-client.jar's Manifest file in the
 * same directory where jbossall-client.jar is placed. Please provide all other
 * jars in the same directory as that of jbossall-client.jar.. These
 * dependencies are available in $JBOSS_HOME/client directory. To minimize the
 * size of the client jar, these jars not specified in the dependencies session
 * of the pom.xml.
 * 
 * jbossall-client.jar is available in $JBOSS_HOME/client directory. All other
 * jars specified in the Manifest file is also available here. In this case,
 * only put the jbossall-client.jar to the eclipse build path, it will
 * automatically adds the rest of jars. As the latest jbossall-client.jar for
 * Jboss6M5 is not available in any repositories, these are not specified in the
 * pom.xml. Please put this dependencies manually for the execution of this test
 * class.
 * 
 * @author TATA
 * 
 */
public class SnmpClientStandAlone
{
    /**
     * Object identifier representing the functionality.
     */
    private static final String SYS_NAME                = ".1.3.6.1.2.1.1.5.0";
    /**
     * IP address of target machine.
     */
    private static final String IP                      = "192.168.161.82";
    /**
     * Community name of the target machine.
     */
    private static final String    COMMUNITY_NAME       = "public";
    /**
     * Port number.
     */
    private static final int    PORT                    = 8001;

    /**
     * Instance of SnmpService interface.
     */
    private static SnmpService snmpService;

    /**
     * The log4j logger instance for this class.
     */
    private static Logger      log                     = LoggerFactory.getLogger(SnmpClientStandAlone.class);

    /**
     * Added to enforce the check style.
     */
    protected SnmpClientStandAlone()
    {

    }

    /**
     * Main method performs the JNDI lookup of SnmpService interface.
     * @param args String[]
     * @throws IOException NamingException
     */
    public static void main(final String[] args) throws IOException
    {
        try
        {
            final Hashtable<String, String> environment = new Hashtable<String, String>();
            environment.put(Context.INITIAL_CONTEXT_FACTORY, SnmpServiceConstants.INITIAL_CONTEXT_FACTORY);
            environment.put(Context.PROVIDER_URL, SnmpServiceConstants.PROVIDER_URL);
            environment.put(Context.URL_PKG_PREFIXES,SnmpServiceConstants.JNDI_FACTORY_PACKAGE_PREFIX);
            final Context ctx = new InitialContext(environment);

            if (log.isDebugEnabled())
            {
                log.debug("Initial Context created");
            }

            snmpService = (SnmpService) ctx.lookup(SnmpServiceConstants.JNDI_BEAN_NAME);

            if (log.isDebugEnabled())
            {
                log.debug("lookup successful"); 
            }

            final SnmpServiceReturnMesage snmpServiceReturnMesage = snmpService.get(SYS_NAME, COMMUNITY_NAME, IP, PORT, null,
                    null, null);

            if (log.isInfoEnabled())
            {
                log.info(snmpServiceReturnMesage.getResultObject());
            }
        }
        catch (NamingException e)
        {
            e.printStackTrace();
        }
    }
}
