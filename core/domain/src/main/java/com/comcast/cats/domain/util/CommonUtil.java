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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Collection of common utility methods.
 * 
 * @author subinsugunan
 * 
 */
public final class CommonUtil
{
    private CommonUtil()
    {
    }

    /**
     * Regular expression for IP address.
     */
    private static final String IP_ADDR_REGEX  = "^(([0-9]|[1-9][0-9]"
                                                       + "|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|"
                                                       + "[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    /**
     * Regular expression for STB mac address.
     */
    private static final String MAC_ADDR_REGEX = "^([0-9a-fA-F][0-9a-fA-F]:)" + "{5}([0-9a-fA-F][0-9a-fA-F])$";

    /**
     * Regular expression for positive and valid timeout.
     */
    private static final String NUMBER_REGEX   = "[-+]?([0-9]*\\.[0-9]+|[0-9]+)";

    /**
     * check whether valid macId.
     * 
     * @param macId
     *            macID of the STB.
     * @return Boolean status
     */
    public static Boolean isValidMacId( final String macId )
    {
        AssertUtil.isNullOrEmpty( macId );
        Boolean retVal = Pattern.compile( MAC_ADDR_REGEX ).matcher( macId ).matches();
        return retVal;
    }

    /**
     * check whether valid TraceServer IP.
     * 
     * @param ip
     *            IP of the TraceServer.
     * @return Boolean status.
     */
    public static Boolean isValidIp( final String ip )
    {
        AssertUtil.isNullOrEmpty( ip );
        Boolean retVal = Pattern.compile( IP_ADDR_REGEX ).matcher( ip ).matches();
        return retVal;
    }

    /**
     * Converts String array to a string representation of List. =>
     * ['value','value'].
     * 
     * @param values
     * @return converted string
     */
    public static String arrayToString( String[] values )
    {
        StringBuilder valueStr = new StringBuilder();

        if ( ( null == values ) || ( values.length == 0 ) )
        {
            valueStr.append( "[]" );
        }
        else
        {
            valueStr.append( "[" );
            for ( String value : values )
            {
                valueStr.append( "'" ).append( value ).append( "'," );
            }
            valueStr.deleteCharAt( valueStr.lastIndexOf( "," ) );
            valueStr.append( "]" );
        }
        return valueStr.toString();
    }

    /**
     * Create a string representation of Map. => ['key':'value','key':'value'].
     * 
     * @param criteria
     * @return converted string
     */
    public static String mapToString( Map< String, String > criteria )
    {
        StringBuilder criteriaMapStr = new StringBuilder();

        if ( ( null == criteria ) || ( criteria.isEmpty() ) )
        {
            criteriaMapStr.append( "[]" );
        }
        else
        {
            Iterator< Entry< String, String > > iterator = criteria.entrySet().iterator();
            Entry< String, String > entry;

            criteriaMapStr.append( "[" );
            while ( iterator.hasNext() )
            {
                entry = iterator.next();
                criteriaMapStr.append( "'" ).append( entry.getKey() ).append( "':'" ).append( entry.getValue() )
                        .append( "'," );
            }
            criteriaMapStr.deleteCharAt( criteriaMapStr.lastIndexOf( "," ) );
            criteriaMapStr.append( "]" );
        }
        return criteriaMapStr.toString();
    }

    /**
     * to get the name value pair.
     * 
     * @param params
     * @return name value pair as string.
     */
    public static String getNameValuePair( Map< String, String > params )
    {
        StringBuilder paramString = new StringBuilder();

        if ( ( null != params ) && ( params.size() > 0 ) )
        {
            paramString.append( "?" );

            Iterator< Entry< String, String > > iterator = params.entrySet().iterator();
            Entry< String, String > entry;

            while ( iterator.hasNext() )
            {
                entry = iterator.next();
                paramString.append( entry.getKey() ).append( "=" ).append( entry.getValue() ).append( "&" );
            }
            paramString.deleteCharAt( paramString.lastIndexOf( "&" ) );
        }

        return paramString.toString();
    }

    /**
     * check whether input is a valid number.
     * 
     * @param input
     *            input for validation.
     * @return Boolean status.
     */
    public static Boolean isValidNumber( String input )
    {
        AssertUtil.isNullOrEmpty( input );
        Boolean retVal = Pattern.compile( NUMBER_REGEX ).matcher( input ).matches();
        return retVal;
    }
}
