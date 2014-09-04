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
package com.comcast.cats.reflect;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ReflectionUtil.class );

    /**
     * Verify if this interface exists on any of the class hierarchy of the
     * class.
     * 
     * @param inter
     *            - Interface we're checking for.
     * @param cl
     *            - Class to test for interface against.
     * @return - True if any sub classes implement the interface or if the class
     *         in question is the interface.
     */
    @SuppressWarnings( "rawtypes" )
    public static boolean isInterfacePresent( Class inter, Class cl )
    {
        if ( inter.equals( cl ) )
        {
            return true;
        }
        List< Class > interfaces = listInterfaces( cl );
        return interfaces.contains( inter );
    }

    @SuppressWarnings( "rawtypes" )
    public static List< Class > listInterfacesHelper( Class clazz, List< Class > interfaces )
    {
        if ( clazz == null )
        {
            return interfaces;
        }
        Class superClass = clazz.getSuperclass();
        // Process the super class interfaces.
        if ( superClass != null )
        {
            listInterfacesHelper( superClass, interfaces );
        }
        // Process the interfaces on this class.
        Class[] interfaceClasses = clazz.getInterfaces();
        for ( int c = 0; c < interfaceClasses.length; c++ )
        {
            Class cl = interfaceClasses[ c ];
            LOGGER.info( "Class = " + clazz.getCanonicalName() + ":Interface = " + cl.getName() );
            if ( !interfaces.contains( cl ) )
            {
                interfaces.add( cl );
            }
            listInterfacesHelper( cl, interfaces );
        }
        return interfaces;
    }

    @SuppressWarnings( "rawtypes" )
    public static List< Class > listInterfaces( Class c )
    {
        List< Class > interfaces = new ArrayList< Class >();
        LOGGER.info( "Listing Interfaces On '" + c.getName() + "'" );
        return listInterfacesHelper( c, interfaces );
    }
}
