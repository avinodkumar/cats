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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.provider.exceptions.ExclusiveAccessException;

public class DummyValidator implements ExclusiveAccessProvider
{

    protected final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    @Override
    public boolean checkExclusiveAccess( Object obj ) throws ExclusiveAccessException
    {
        LOGGER.info( "Object is of class = " + obj.getClass().toString() );
        SettopDummy settop = null;
        if ( obj instanceof BaseProvider )
        {
            LOGGER.info( "Object is BaseProvider" );
            BaseProvider bp = ( BaseProvider ) obj;
            if ( bp != null && bp.getParent() instanceof SettopDummy )
            {
                LOGGER.info( "Object is BaseProvider has a parent of Settop" );
                settop = ( SettopDummy ) bp.getParent();
            }
            else
            {
                return true;
            }
        }
        else if ( obj instanceof SettopDummy )
        {
            settop = ( SettopDummy ) obj;
        }
        else
        {
            return true;
        }
        try
        {
            LOGGER.info( "Made it to customer Validator for Settop = " + settop.toString() );
        }
        catch ( Exception e )
        {
            LOGGER.info( e.toString() );
        }
        return false;
    }

}
