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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.SettopConstants;
import com.comcast.cats.domain.SettopDesc;

@XmlRootElement( name = "settopList", namespace = SettopConstants.NAMESPACE )
public class SettopList extends ArrayList< SettopDesc > implements Serializable
{
    /**
     * Serial version id
     */
    private static final long serialVersionUID = 5651828525259915623L;

    /**
     * no argument constructor
     */
    public SettopList()
    {
        // Do Nothing
    }

    /**
     * constructor
     * 
     * @param settops
     *            - List of {@link SettopDesc}
     */
    public SettopList( List< SettopDesc > settops )
    {
        this.addAll( settops );
    }

    /**
     * To get settop list
     * 
     * @return List of {@link SettopDesc}
     */
    @XmlElementWrapper( name = "settops" )
    @XmlElement( name = "settop" )
    public List< SettopDesc > getSettops()
    {
        return ( List< SettopDesc > ) this;
    }

    /**
     * To set settop list
     * 
     * @param settops
     *            - List of {@link SettopDesc}
     */
    public void setSettops( List< SettopDesc > settops )
    {
        // Empty this list before assigning new settops.
        this.clear();
        this.addAll( settops );
    }
}
