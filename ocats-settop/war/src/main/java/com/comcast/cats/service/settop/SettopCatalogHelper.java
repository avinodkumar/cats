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
package com.comcast.cats.service.settop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.service.SettopToken;

/**
 * Helper class for {@linkplain SettopCatalog}
 * 
 * @author subinsugunan
 * 
 */
public class SettopCatalogHelper
{
    private static Map< String, SettopToken > settopTokenMap = new ConcurrentHashMap< String, SettopToken >();
    private static Map< String, String >      settopErrorMap = new ConcurrentHashMap< String, String >();

    public static SettopToken lookForExistingAllocation( String mac, String userToken )
    {
        String settopLookupKey = getSettopLookupKey( mac, userToken );

        return ( settopTokenMap.containsKey( settopLookupKey ) ) ? settopTokenMap.get( settopLookupKey ) : null;
    }

    public static void putSettopToken( String mac, SettopToken settopToken, String userToken )
    {
        String settopLookupKey = getSettopLookupKey( mac, userToken );
        settopTokenMap.put( settopLookupKey, settopToken );
    }

    public static void removeSettopToken( String mac, String userToken )
    {
        String settopLookupKey = getSettopLookupKey( mac, userToken );
        settopTokenMap.remove( settopLookupKey );
    }

    /**
     * One user can have more than one allocation.
     * 
     * @param userToken
     * @param mac
     * @return
     */
    private static String getSettopLookupKey( String mac, String userToken )
    {
        String key = "[" + mac + "][" + userToken + "]";
        return key;
    }

    public static void putSettopError( String token, String errorMsg )
    {
        if ( ( null != token ) && ( null != errorMsg ) )
        {
            settopErrorMap.put( token, errorMsg );
        }
    }

    public static String gettSettopError( String token ) throws SettopNotFoundException
    {
        if ( !settopErrorMap.containsKey( token ) )
        {
            throw new SettopNotFoundException( "No error found for user [" + token + "]" );
        }
        return settopErrorMap.get( token );
    }

    public static Map< String, SettopToken > getSettopTokenMap()
    {
        return settopTokenMap;
    }

    public static Map< String, String > getSettopErrorMap()
    {
        return settopErrorMap;
    }
}
