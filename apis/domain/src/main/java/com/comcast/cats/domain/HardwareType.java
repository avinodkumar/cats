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
package com.comcast.cats.domain;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * The type of a {@link HardWareDevice}
 * 
 * @deprecated This class is deprecated. Use {@link HardwarePurpose} instead.
 * 
 * @author subinsugunan
 * 
 */
@XmlType
@XmlEnum( String.class )
@Deprecated
public enum HardwareType
{
    SETTOP, IR, POWER, TRACE, AUDIO, VIDEOSERVER, VIDEOSELECTOR, RF_SPLITTER, RFCONTROL;

    public static HardwareType parse( final String name )
    {
        for ( HardwareType type : values() )
        {
            if ( name != null && ( name.equals( type.name() ) ) )
            {
                return type;
            }
        }
        throw new IllegalArgumentException( "[" + name + "] is not a valid HardwareType." );
    }

}
