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

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class is used by the PropertiesAdapter to convert a Map&lt;String,
 * String&gt; into something that can be serialized into XML.
 * 
 * @author cfrede001
 */
public class NameValueType implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 6570133500821316706L;
    /**
     * The key portion of the map.
     */
    private String            name;
    /**
     * The value portion of the map.
     */
    private String            value;

    /**
     * @return the key portion of the map
     */
    @XmlAttribute
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the key portion of the map
     */
    public void setName( final String name )
    {
        this.name = name;
    }

    /**
     * @return the value portion of the map
     */
    @XmlAttribute
    public String getValue()
    {
        return value;
    }

    /**
     * @param value
     *            the value portion of the map
     */
    public void setValue( final String value )
    {
        this.value = value;
    }
}
