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
package com.comcast.cats.domain.util;

import java.util.List;
import java.util.Map;

/**
 * Assertion utility class to simplify validating input arguments.
 * 
 * @author subinsugunan
 * 
 */
public final class AssertUtil
{
    private AssertUtil()
    {
    }

    /**
     * to validate mac id.
     * 
     * @param string
     */
    public static void isValidMacId( String string )
    {
        isValidMacId( string, "[Assertion failed] - this object is not valid" );
    }

    /**
     * to validate mac id.
     * 
     * @param string
     * @param message
     */
    public static void isValidMacId( String string, String message )
    {
        if ( !CommonUtil.isValidMacId( string ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * to check emptiness.
     * 
     * @param string
     */
    public static void isEmpty( String string )
    {
        isEmpty( string, "[Assertion failed] - this object is empty" );
    }

    /**
     * to check emptiness.
     * 
     * @param string
     * @param message
     */
    public static void isEmpty( String string, String message )
    {
        isNullOrEmpty( string, message );
        
        if ( string.isEmpty() )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * for null check.
     * 
     * @param objects
     */
    public static void isNullObject( Object... objects )
    {
        if ( null == objects )
        {
            throw new IllegalArgumentException( "[Assertion failed] - this object is null" );
        }
        for ( Object object : objects )
        {
            isNull( object );
        }
    }

    /**
     * for null check.
     * 
     * @param object
     */
    public static void isNull( Object object )
    {
        isNull( object, "[Assertion failed] - this object is null" );
    }

    /**
     * for null check.
     * 
     * @param object
     * @param message
     */
    public static void isNull( Object object, String message )
    {
        if ( null == object )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * for null/empty check.
     * 
     * @param string
     */
    public static void isNullOrEmpty( String string )
    {
        isNullOrEmpty( string, "[Assertion failed] - this object is empty" );
    }

    /**
     * for null/empty check.
     * 
     * @param string
     * @param message
     */
    public static void isNullOrEmpty( String string, String message )
    {
        if ( ( null == string ) || ( string.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * for null/empty check on map.
     * 
     * @param map
     */
    public static void isNullOrEmptyMap( Map< String, String > map )
    {
        isNullOrEmptyMap( map, "[Assertion failed] - this map is empty" );
    }

    /**
     * for null/empty check on map.
     * 
     * @param map
     * @param message
     */
    public static void isNullOrEmptyMap( Map< String, String > map, String message )
    {
        if ( ( null == map ) || ( map.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * for null/empty check on List.
     * 
     * @param list
     */
    public static void isNullOrEmptyList( List< ? extends Object > list )
    {
        isNullOrEmptyList( list, "[Assertion failed] - this list is empty or null" );
    }

    /**
     * for null/empty check on list.
     * 
     * @param list
     * @param message
     */
    public static void isNullOrEmptyList( List< ? extends Object > list, String message )
    {
        if ( ( null == list ) || ( list.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * for null/empty check.
     * 
     * @param stringArray
     * @param message
     */
    public static void isNullOrEmpty( String[] stringArray, String message )
    {
        if ( ( null == stringArray ) || ( stringArray.length <= 0 ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    /**
     * for multiple check.
     * 
     * @param count
     */
    public static Boolean isOneOrMore( Integer count )
    {
        Boolean result = false;
        if ( count > 0 )
        {
            result = true;
        }
        return result;
    }
}
