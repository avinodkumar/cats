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
package com.comcast.cats.utils;

import java.util.regex.Pattern;

/**
 * Collection of common utility methods.
 * 
 * @author SSugun00c
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
     * @return boolean status
     */
    public static boolean isValidMacId( final String macId )
    {
        AssertUtil.isNullOrEmpty( macId );
        boolean retVal = Pattern.compile( MAC_ADDR_REGEX ).matcher( macId ).matches();
        return retVal;
    }

    /**
     * check whether valid TraceServer IP.
     * 
     * @param ip
     *            IP of the TraceServer.
     * @return boolean status.
     */
    public static boolean isValidIp( final String ip )
    {
        AssertUtil.isNullOrEmpty( ip );
        boolean retVal = Pattern.compile( IP_ADDR_REGEX ).matcher( ip ).matches();
        return retVal;
    }

    /**
     * check whether input is a valid number.
     * 
     * @param input
     *            input for validation.
     * @return boolean status.
     */
    public static boolean isValidNumber( String holdTime, String message )
    {
        AssertUtil.isNullOrEmpty( holdTime, message );

        if ( !( Pattern.compile( NUMBER_REGEX ).matcher( holdTime ).matches() ) )
        {
            throw new IllegalArgumentException( message );
        }
        return true;
    }

    public static boolean isValidNumber( String input )
    {
        return isValidNumber( input, "Invalid number" );
    }
}
