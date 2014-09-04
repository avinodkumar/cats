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
package com.comcast.cats.jenkins.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * @author SSugun00c
 * 
 */
@Root
public class ParameterDefinition extends BaseJenkinsDomain
{
    private static final long     serialVersionUID = 1L;

    @Element( required = false )
    private DefaultParameterValue defaultParameterValue;

    @Element( required = false )
    private String                description;

    @Element( required = false )
    private String                name;

    @Element( required = false )
    private String                type;

    public DefaultParameterValue getDefaultParameterValue()
    {
        return defaultParameterValue;
    }

    public void setDefaultParameterValue( DefaultParameterValue defaultParameterValue )
    {
        this.defaultParameterValue = defaultParameterValue;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }
}
