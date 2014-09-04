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
package com.comcast.cats.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.comcast.cats.service.util.SettopApplicationConfigUtil;

/**
 * Application context listener.
 * 
 * @author subinsugunan
 */
public class DefaultContextListener implements ServletContextListener
{
    /**
     * The log4j logger instance for this class.
     */
    private static final Logger logger = Logger.getLogger( DefaultContextListener.class );

    /**
     * Context destruction.
     * 
     * @param arg0
     *            ServletContextEvent
     */
    @Override
    public void contextDestroyed( final ServletContextEvent arg0 )
    {
        if ( logger.isInfoEnabled() )
        {
            logger.info( "Settop Service default context destroyed" );
        }
    }

    /**
     * Context Initialization.
     * 
     * @param arg0
     *            ServletContextEvent
     */
    @Override
    public void contextInitialized( final ServletContextEvent arg0 )
    {

        SettopApplicationConfigUtil.initialize();

        if ( logger.isInfoEnabled() )
        {
            logger.info( "Settop Service default context Initialized" );
        }
    }

}
