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
package com.comcast.cats.decorator;

import static com.comcast.cats.configuration.ApplicationContextProvider.getApplicationContext;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

/**
 * A simple factory to decorate {@link Settop}.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class DecoratorFactory
{
    private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    /**
     * To decorate {@link Settop}.
     * 
     * @param settop {@linkplain SettopImpl}.
     * @return {@linkplain Settop}.
     */
    public Settop decorate( SettopImpl settop )
    {
        /**
         * Get Component type of settop.
         */
        String componentType = ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType();
        
        String qualifierName = null;
        
        if ( null != componentType )
        {
            /**
             * Get spring qualifier name based on componentType
             */
            qualifierName = SettopFamilyResolver.getDecoratorMap().get( componentType );
        }

        if ( null != qualifierName )
        {
            return ( SettopDiagnostic ) getApplicationContext().getBean( qualifierName, settop );
        }
        else
        {
            LOGGER.warn( "SettopDiagnostic is not supported for componentType="
                    + componentType
                    + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                    + settop );
            return settop;
        }

    }
}
