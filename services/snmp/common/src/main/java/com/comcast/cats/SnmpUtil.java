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
package com.comcast.cats;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.snmp4j.smi.TimeTicks;

/**
 * Utility class for the snmp input parameter validations.
 * @author TATA
 *
 */
public final class SnmpUtil
{
    /**
     * The minimum length of the USM password in the case of Snmp V3.
     */
    private static final int MIN_PWD_LENGTH = 8;

    /**
     * Regular expression for validating OID.
     */
    private static final String OID_REGEX  = "(\\.?\\d+)*";
    /**
     * Constant separator for DOT.
     */
    private static final String CONST_DOT        = ".";

    /**
     * Regular expression for validating Target machine IP Address.
     */
    private static final String IP_ADDR_REGEX  = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.)"
        + "{3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    
    public static final String TIMETICK_PATTERN = "{0,number,00}:{1,number,00}:{2,number,00}:{3,number,00}.{4,number,00}";
    

    /**
     * Private constructor added to enforce the check style.
     */
    private SnmpUtil()
    {
    }

    /**
     * Checking whether the specified OID is valid using regular expression.
     * @param oid
     *            Object identifier representing the functionality.
     * @return True if the OID is in correct format else false
     */
    public static boolean isValidOid(final String oid)
    {
        return Pattern.compile(OID_REGEX).matcher(oid).matches();
    }

    /**
     * Checking whether the specified IP Address is valid using regular expression.
     * @param ip
     *            The IP Address of the target machine
     * @return True if the IP is in correct format else false
     */
    public static boolean isValidIp(final String ip)
    {
        return Pattern.compile(IP_ADDR_REGEX).matcher(ip).matches();
    }

    /**
     * Checking whether the password specified has minimum 8 characters.
     * @param password
     *            The Authentication or Privacy Password in the case of Snmp V3.
     * @return True if the Password has minimum length else false
     */
    public static boolean isValidPassword(final String password)
    {
        return (MIN_PWD_LENGTH <= password.length()) ? true : false;
    }

    /**
     * Returns the Formatted time in day:HH:mm:ss format. The input time is in
     * seconds. Returns empty string if input is negative.
     * 
     * @param upTimeInSec
     * @return
     */
    public static String getFormattedTime( final long upTimeInSec )
    {
        String timeTicksString = getFormattedTimeForTimeTicks( upTimeInSec * 100 );
        return StringUtils.substringBefore( timeTicksString, CONST_DOT );
    }

    /**
     * Returns time in days:HH:mm:ss format if input time is greater than 0 else
     * returns empty string. The input time is in hundredth of a seconds. The
     * same output from SNMP.
     * 
     * 
     * @param upTimeInHundredthSec
     * @return
     */
    public static String getFormattedTimeForTimeTicks( final long upTimeInHundredthSec )
    {
        String result = "";
        if ( upTimeInHundredthSec >= 0 )
        {
            TimeTicks timeTicks = new TimeTicks( upTimeInHundredthSec );
            result = timeTicks.toString( TIMETICK_PATTERN );
        }
        return result;
    }
    
}
