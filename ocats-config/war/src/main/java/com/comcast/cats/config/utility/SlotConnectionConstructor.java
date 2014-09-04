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

import javax.inject.Inject;
import javax.inject.Named;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import com.comcast.cats.config.ui.RackService;

@Named
public class SlotConnectionConstructor extends Constructor
{
    @Inject
    RackService rackService;

    public SlotConnectionConstructor()
    {
        this.yamlConstructors.put( new Tag( SlotConnectionRepresenter.RACK_TAG ), new ConstructRack() );
        this.yamlConstructors.put( new Tag( SlotConnectionRepresenter.SLOT_TAG ), new ConstructSlot() );
    }

    private class ConstructRack extends AbstractConstruct
    {
        public Object construct( Node node )
        {
            String val = ( ( String ) constructScalar( ( ScalarNode ) node ) ).trim();
            return rackService.findRack( val );
        }
    }

    private class ConstructSlot extends AbstractConstruct
    {
        public Object construct( Node node )
        {
            String val = ( String ) constructScalar( ( ScalarNode ) node );
            int position = val.indexOf( SlotConnectionRepresenter.SLOT_DELIMITER );
            String rackName = val.substring( 0, position );
            Integer slot = Integer.parseInt( val.substring(
                    position + SlotConnectionRepresenter.SLOT_DELIMITER.length() - 1 ).trim() );
            return rackService.findSlotByRack( rackName, slot );
        }
    }

}
