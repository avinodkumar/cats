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
package com.comcast.cats.info;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author TATA
 * 
 */
@XmlType
@XmlEnum( String.class )
public enum SnmpServiceReturnEnum
{
    /**
     * General Snmp service success.
     */
    SNMP_SERVICE_SUCCESS( 0, "General Snmp service success" ),
    /**
     * General Snmp service failure.
     */
    SNMP_SERVICE_FAILURE( -10, "General Snmp service failure" ),
    /**
     * Snmp requst time out.
     */
    SNMP_SERVICE_TIME_OUT( -11, "Snmp requst time out" ),
    /**
     * Snmp input parameter error.
     */
    SNMP_SERVICE_INVALID_INPUT( -12, "Snmp input parameter error" );

    /**
     * Holding the code of an enum.
     */
    private final int    value;
    /**
     * Holding the description of an enum.
     */
    private final String genericDescription;

    /**
     * Two argument constructor. Creates an enum with supplied Code and
     * Description.
     * 
     * @param value
     *            Code corresponding to an enum constant.
     * @param genericDescription
     *            Description corresponding to an enum constant.
     */
    SnmpServiceReturnEnum( final int value, final String genericDescription )
    {
        this.value = value;
        this.genericDescription = genericDescription;
    }

    /**
     * Getter method for the Code of an enum constant.
     * 
     * @return Code corresponding to an enum constant.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Getter method for the Description of an enum constant.
     * 
     * @return Description corresponding to an enum constant.
     */
    public String getDescription()
    {
        return genericDescription;
    }
}
