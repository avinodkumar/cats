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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds an entry of the SNMP Walk result.
 * 
 * <pre>
 *  For a snmpwalk result like 1.3.6.1.2.1.2.2.1.6.1 = 54:d4:6f:7e:06:62,
 *  this bean holds the oid (1.3.6.1.2.1.2.2.1.6.1) and its value (54:d4:6f:7e:06:62)
 * </pre>
 * 
 * This is
 * 
 * @author ajith
 * 
 */
@XmlRootElement
public class SnmpWalkResult implements Serializable
{
    /**
     * Generated Serial ID.
     */
    private static final long serialVersionUID = -3883639322632144305L;
    /**
     * Holds the Object Identifier value.
     */
    private String            oid;
    /**
     * Holds the value for this above mentioned OID.
     */
    private String            value;

    // Getters and Setters.
    public String getOid()
    {
        return oid;
    }

    public void setOid( String oid )
    {
        this.oid = oid;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        String toStringVal = "OID: " + getOid() + " Value :" + getValue();
        return toStringVal;
    }

}
