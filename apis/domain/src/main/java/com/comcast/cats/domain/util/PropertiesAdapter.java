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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This XmlAdapter is used to convert a {@code Map< String, String >>} into
 * something that can be serialized into XML.
 * 
 * @author cfrede001
 */
public class PropertiesAdapter extends XmlAdapter< NameValueType[], Map< String, String >>
{

    /**
     * The method to convert the {@code Map< String, String >>} into a temporary
     * form that is readily converted into XML.
     * 
     * @param map
     *            The map to convert
     * @return The NameValueType that represents this map
     */
    @Override
    public NameValueType[] marshal( final Map< String, String > map )
    {
        List< NameValueType > rc = new LinkedList< NameValueType >();

        for ( Map.Entry< String, String > e : map.entrySet() )
        {
            NameValueType x = new NameValueType();
            x.setName( e.getKey() );
            x.setValue( e.getValue() );
            rc.add( x );
        }

        return ( com.comcast.cats.domain.util.NameValueType[] ) rc.toArray( new NameValueType[ rc.size() ] );
    }

    /**
     * The method to convert the temp form of the map back into a Map&lt;String,
     * String&gt;.
     * 
     * @param v
     *            The map to convert back
     * @return The {@code Map< String, String >>} that represents this map
     * @throws java.lang.Exception
     *             never actually thrown, just making the signature match
     *             cleanly
     */
    @Override
    public Map< String, String > unmarshal( final NameValueType[] v ) throws Exception
    {
        Map< String, String > rc = new HashMap< String, String >();
        for ( NameValueType entry : v )
        {
            rc.put( entry.getName(), entry.getValue() );
        }
        return rc;
    }
}
