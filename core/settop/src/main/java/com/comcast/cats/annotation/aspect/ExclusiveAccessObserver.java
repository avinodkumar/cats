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
package com.comcast.cats.annotation.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import com.comcast.cats.Settop;
import com.comcast.cats.provider.BaseProvider;
import com.comcast.cats.provider.exceptions.ExclusiveAccessException;
import com.comcast.cats.domain.exception.SettopNotFoundException;

@Aspect
public class ExclusiveAccessObserver
{
    private static Logger logger = LoggerFactory.getLogger( ExclusiveAccessObserver.class );

    /**
	 * Constructor
	 * 
	 */
    public ExclusiveAccessObserver()
    {
        logger.trace( "Instantiating ExclusiveAccessObserver()" );
    }

    /**
     * Using this aspect we will be called for every method annotated with
     * com.comcast.cats.annotation.ExclusiveAccess
     * 
     * @param target
     * @throws ExclusiveAccessException
     */
    @Before( "@annotation(com.comcast.cats.annotation.ExclusiveAccess) && target(target)" )
    public void verifyExclusiveAccess( Object target ) throws ExclusiveAccessException
    {
        logger.info( "verifyExclusiveAccess " + target );

        Settop settop = null;
        try
        {
            settop = retrieveSettop( target );
        }
        catch ( SettopNotFoundException e )
        {
            throw new ExclusiveAccessException( e.getMessage() );
        }
        if ( null != settop && !settop.isLocked() )
        {
            throw new ExclusiveAccessException( "Settop must be locked to perform this operation." );
        }

    }

    /**
     * Checks to see if object being checked is a Settop or BaseProvider that
     * contains a parent of Settop.
     * 
     * @param obj
     *            - Object being checked.
     * @return {@linkplain Settop} - object from class.
     * @throws SettopNotFoundException
     *             - If settop object is unattainable by BaseProvider call.
     */
    private Settop retrieveSettop( Object obj ) throws SettopNotFoundException
    {
        Settop settop = null;
        /**
         * We now need to determine if this is a Settop object through either
         * the BaseProvider reference or if the action was performed directly on
         * the Settop. Move from general Settop to more specific BaseProvider.
         */
        if ( obj instanceof Settop )
        {
            logger.debug( "retrieveSettop object instanceof Settop" );
            settop = ( Settop ) obj;
        }
        else if ( obj instanceof BaseProvider )
        {
            logger.debug( "retrieveSettop object instanceof BaseProvider" );
            BaseProvider bp = ( BaseProvider ) obj;
            if ( bp != null && ( bp.getParent() instanceof Settop ) )
            {
                settop = ( Settop ) bp.getParent();
            }
            else
            {
                throw new SettopNotFoundException( "Settop Not Found" );
            }
        }
        else
        {
            logger.trace( "Object instanceof " + obj.getClass() );
            throw new SettopNotFoundException();
        }
        return settop;
    }

}