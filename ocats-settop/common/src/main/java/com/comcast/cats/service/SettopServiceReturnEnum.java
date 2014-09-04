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
package com.comcast.cats.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author cfrede001
 */
@XmlType
@XmlEnum( String.class )
public enum SettopServiceReturnEnum
{
    SETTOP_SERVICE_SUCCESS( 0, "General SettopService Success" ), SETTOP_SERVICE_FAILURE( -1,
            "General SettopService Failure" ), ALLOCATION_CHECK_FAILURE( -10, "Allocation check failure" ),
    ALLOCATION_CREATE_FAILURE( -11, "Allocation create failure" ), IS_LOCKED_FAILURE( -20, "Is Locked Failure" ),
    IR_FAILURE( -30, "IR Transission Error, please try again" ), TRACE_SERVICE_INVALID_INPUT( -40, "Invalid input" );

    private final int    value;
    private final String genericDescription;

    SettopServiceReturnEnum( int value, String genericDescription )
    {
        this.value = value;
        this.genericDescription = genericDescription;
    }

    SettopServiceReturnEnum( int value )
    {
        this.value = value;
        this.genericDescription = "";
    }

    int getValue()
    {
        return value;
    }

    String getDescription()
    {
        return genericDescription;
    }
}
