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
 * RfMode identifies the different modes that can be set to an addressable tap.
 * For connect-disconnect, we basically switch between OFF and PAY.
 * 
 * @author skurup00c
 * 
 */
@XmlType
@XmlEnum( String.class )
public enum RfMode
{
    BASIC( "basic" ), PAY( "pay" ), OFF( "off" );

    /**
     * Represents how this enum should be represented like in JSF page etc.
     */
    private String representation;

    private RfMode( String representation )
    {
        this.representation = representation;
    }

    public String getRepresentation()
    {
        return representation;
    }
}
