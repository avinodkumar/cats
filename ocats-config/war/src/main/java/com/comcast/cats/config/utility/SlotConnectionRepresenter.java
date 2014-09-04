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
package com.comcast.cats.config.utility;

import java.beans.IntrospectionException;
import java.net.URI;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Named;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

@Named
public class SlotConnectionRepresenter extends Representer
{
    public static final String SLOT_DELIMITER = " :: ";
    public static final String RACK_TAG       = "!rack";
    public static final String SLOT_TAG       = "!slot";

    public SlotConnectionRepresenter()
    {
        this.representers.put( Rack.class, new RepresentRack() );
        this.representers.put( Slot.class, new RepresentSlot() );
    }

    @Override
    /**
     * This method is not called. It was earlier and we had to filter the representation of URI in the yaml
     * Leaving this method here as an example of how to use filters for YAML for any future use.
     * 
     * @author skurup00c
     */
    protected Set< Property > getProperties( Class< ? extends Object > type ) throws IntrospectionException
    {
        Set< Property > set = super.getProperties( type );
        Set< Property > filtered = new TreeSet< Property >();
        for ( Property prop : set )
        {
            Class typeClass = prop.getType();
            if ( !typeClass.equals( URI.class ) )
            {
                filtered.add( prop );
            }
        }
        return filtered;
    }

    private class RepresentRack implements Represent
    {
        public Node representData( Object data )
        {
            Rack rack = ( Rack ) data;
            String value = rack.getName();
            return representScalar( new Tag( RACK_TAG ), value );
        }
    }

    private class RepresentSlot implements Represent
    {
        public Node representData( Object data )
        {
            Slot slot = ( Slot ) data;
            String value = slot.getRack().getName() + SLOT_DELIMITER + slot.getNumber();
            return representScalar( new Tag( SLOT_TAG ), value );
        }
    }
}
