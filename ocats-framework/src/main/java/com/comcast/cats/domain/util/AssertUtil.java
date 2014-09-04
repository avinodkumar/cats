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

import com.comcast.cats.utils.CommonUtil;

/**
 * Assertion utility class to simplify validating input arguments.
 * 
 * @author subinsugunan
 * 
 */
public final class AssertUtil
{
    private static AssertUtil assertUtil = null;

    private AssertUtil()
    {
    }

    public static AssertUtil getInstance()
    {
        if ( null == assertUtil )
        {
            assertUtil = new AssertUtil();
        }

        return assertUtil;
    }

    public static void isValidMacId( String string )
    {
        isValidMacId( string, "[Assertion failed] - this object is not valid" );
    }

    public static void isValidMacId( String string, String message )
    {
        if ( !CommonUtil.isValidMacId( string ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isEmpty( String string )
    {
        isEmpty( string, "[Assertion failed] - this object is empty" );
    }

    public static void isEmpty( String string, String message )
    {
        if ( string.isEmpty() )
        {
            throw new IllegalArgumentException( message );
        }
    }

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

    public static void isNull( Object object )
    {
        isNull( object, "[Assertion failed] - this object is null" );
    }

    public static void isNull( Object object, String message )
    {
        if ( null == object )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmpty( String string )
    {
        isNullOrEmpty( string, "[Assertion failed] - this object is empty" );
    }

    public static void isNullOrEmpty( String string, String message )
    {
        if ( ( null == string ) || ( string.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmptyMap( Map< String, String > map )
    {
        isNullOrEmptyMap( map, "[Assertion failed] - this map is empty" );
    }

    public static void isNullOrEmptyMap( Map< String, String > map, String message )
    {
        if ( ( null == map ) || ( map.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmptyList( List< ? extends Object > list )
    {
        isNullOrEmptyList( list, "[Assertion failed] - this list is empty or null" );
    }

    public static void isNullOrEmptyList( List< ? extends Object > list, String message )
    {
        if ( ( null == list ) || ( list.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmpty( String[] stringArray, String message )
    {
        if ( ( null == stringArray ) || ( stringArray.length <= 0 ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static boolean isOneOrMore( int count )
    {
        boolean result = false;
        if ( count > 0 )
        {
            result = true;
        }
        return result;
    }
}
