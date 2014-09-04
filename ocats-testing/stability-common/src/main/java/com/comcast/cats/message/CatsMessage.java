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
package com.comcast.cats.message;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * @author SSugun00c
 * 
 */
public class CatsMessage implements Serializable
{
    private static final long serialVersionUID = 1L;

    private UUID              uuid;
    private String            name;
    private BuildAction       buildAction;
    private Object            message;

    public CatsMessage()
    {
        super();
        setUuid( UUID.randomUUID() );
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid( UUID uuid )
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Object getMessage()
    {
        return message;
    }

    public void setMessage( Object message )
    {
        this.message = message;
    }

    public BuildAction getBuildAction()
    {
        return buildAction;
    }

    public void setBuildAction( BuildAction buildAction )
    {
        this.buildAction = buildAction;
    }

    @Override
    public String toString()
    {
        return "BaseCatsMessage [uuid=" + getUuid() + ", name=" + getName() + ", buildAction=" + getBuildAction()
                + ", message=" + getMessage() + "]";
    }
}
