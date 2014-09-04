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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.annotation.aspect.ExclusiveAccessObserver;
import com.comcast.cats.provider.exceptions.ExclusiveAccessException;

/**
 * Bridge singleton that is utilized as concrete class from within the Aspect
 * "ExclusiveAccessObserver" which delegates down to a concrete class with
 * implementation ExclusiveAccessProvider. If a ExclusiveAccessProvider is not
 * provided it will be ignored and locking enforcement will not occur.
 * 
 * @see ExclusiveAccessProvider
 * @see ExclusiveAccessObserver
 * @author cfrede001
 * 
 */
@Named
public class ExclusiveAccessBridge implements ExclusiveAccessProvider
{
    private static Logger                      logger = LoggerFactory.getLogger( ExclusiveAccessObserver.class );
    private static final ExclusiveAccessBridge access = new ExclusiveAccessBridge();
    private ExclusiveAccessProvider            accessProvider;

    /**
     * To get instance.
     * @return ExclusiveAccessBridge
     */
    public static ExclusiveAccessBridge getInstance()
    {
        return access;
    }

    /**
     * Make this private so nobody can instantiate it.
     */
    private ExclusiveAccessBridge()
    {
        logger.trace( "ExclusiveAccessBridge Constructor" );
    }

    /**
     * To check exclusive access.
     * @param obj
     * @return boolean
     * @throws ExclusiveAccessException
     */
    @Override
    public boolean checkExclusiveAccess( Object obj ) throws ExclusiveAccessException
    {
        logger.trace( "ExclusiveAccessBridge.checkExclusiveAccess" );
        if ( accessProvider == null )
        {
            logger.warn( "No AccessProvider provided." );
            return true;
        }
        return accessProvider.checkExclusiveAccess( obj );
    }

    /**
     * 
     * @return ExclusiveAccessProvider implementation chosen by implementation
     *         code.
     */
    public ExclusiveAccessProvider getExclusiveAccessProvider()
    {
        return accessProvider;
    }

    /**
     * Set the ExclusiveAccessProvider implementation class that will be called
     * by
     * 
     * @see ExclusiveAccessObserver
     * @see ExclusiveAccessProvider
     * @param provider
     */
    @Inject
    public void setExclusiveAccessProvider( final ExclusiveAccessProvider provider )
    {
        logger.trace( "SettingExclusiveAccessProvider" );
        this.accessProvider = provider;
    }
}
