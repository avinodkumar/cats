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
package com.comcast.cats.service.util;

import java.util.List;
import java.util.Map;

/**
 * Assertion utility class to simplify validating input arguments.
 * 
 * @author ssugun00c
 * 
 */
public final class AssertUtil
{
    private AssertUtil()
    {
    }

    public static void isEmpty( String message, String string )
    {
        if ( string.isEmpty() )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmpty( String message, String string )
    {
        if ( ( null == string ) || ( string.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }
    
    public static void isNullOrEmpty( String message, List< ? > list )
    {
        if ( ( null == list ) || ( list.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNull( String message, Object object )
    {
        if ( null == object )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullObject( String message, Object... objects )
    {
        if ( null == objects )
        {
            throw new IllegalArgumentException( message );
        }
        for ( Object object : objects )
        {
            isNull( message, object );
        }
    }

    public static void isNullOrEmptyMap( String message, Map< ?, ? > map )
    {
        if ( ( null == map ) || ( map.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmptyList( String message, List< ? > list )
    {
        if ( ( null == list ) || ( list.isEmpty() ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isNullOrEmpty( String message, Object[] stringArray )
    {
        if ( ( null == stringArray ) || ( stringArray.length <= 0 ) )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isTrue( String message, boolean value )
    {
        if ( !value )
        {
            throw new IllegalArgumentException( message );
        }
    }

    public static void isValidIp( String message, String ipOrFqdn )
    {
       //FIXME: Temporarily Disabling as InetAddress.getByNameis not trustworthy.
       /* try
        {
            InetAddress address = InetAddress.getByName( ipOrFqdn );
            if ( !address.isReachable( 10000 ) )
            {
                throw new IllegalArgumentException( message );
            }
        }
        catch ( UnknownHostException e )
        {
            throw new IllegalArgumentException( message + " Additional info: " + e.getMessage() );
        }
        catch ( IOException e )
        {
            throw new IllegalArgumentException( message + " Additional info: " + e.getMessage() );
        }*/
    }

    public static void isValidMacId( String message, String macId )
    {
        if ( !VideoRecorderUtil.isValidMacId( macId ) )
        {
            throw new IllegalArgumentException( message );
        }
    }
}
