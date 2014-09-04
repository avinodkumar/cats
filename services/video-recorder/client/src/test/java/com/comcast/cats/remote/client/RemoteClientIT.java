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
package com.comcast.cats.remote.client;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests to verify {@link InitialContext} initialization in JBoss
 * AS7. This will serve as base class for all tests which requires JNDI look up.
 * 
 * @author SSugun00c
 * 
 */
public class RemoteClientIT
{
    protected static final String JBOSS_URL_PKG_PREFIXES = "org.jboss.ejb.client.naming";
    protected static final String MODULE_NAME            = "video-recorder-service";
    protected static final String EJB_JNDI_PREFIX        = "ejb:/";

    protected Logger              logger                 = LoggerFactory.getLogger( getClass() );
    protected InitialContext      context;

    @Before
    public void setup() throws NamingException
    {
        Hashtable< String, String > envs = new Hashtable< String, String >();
        envs.put( Context.URL_PKG_PREFIXES, JBOSS_URL_PKG_PREFIXES );
        context = new InitialContext( envs );
    }
}