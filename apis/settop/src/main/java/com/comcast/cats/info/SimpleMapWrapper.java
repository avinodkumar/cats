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
import java.util.Map;

/**
 * A simple wrapper class for {@link Map} interface. HashMap is not directly
 * supported in JAX-WS. A work around is to use a serialized wrapper of the map
 * instead.
 * 
 * TODO: Use of @XmlJavaTypeAdapter
 * 
 * @author subinsugunan
 * 
 */
public class SimpleMapWrapper implements Serializable
{
    /**
     * Serialization support.
     */
    private static final long     serialVersionUID = 2600704900564672531L;

    /**
     * Map the hold the actual data.
     */
    private Map< String, String > map;

    /**
     * To get Map
     * 
     * @return Map
     */
    public Map< String, String > getMap()
    {
        return map;
    }

    /**
     * To set Map
     * 
     * @param map
     */
    public void setMap( Map< String, String > map )
    {
        this.map = map;
    }
}
