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
package com.comcast.cats.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Represents a directory or media file.
 * 
 * @author ssugun00c
 * @see DocumentType
 */
@ManagedBean
@RequestScoped
public class DocumentBean implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private UUID                 uuid;
    private String               name;
    private DocumentType         type             = DocumentType.DIRECTORY;
    private double               size;
    private String               absolutePath;
    private String               httpPath;
    private List< DocumentBean > childs           = new ArrayList< DocumentBean >();
    private boolean              isPlayable       = false;

    public DocumentBean( String name, double size )
    {
        super();
        setUuid( UUID.randomUUID() );
        setName( name );
        setSize( size );
    }

    public DocumentBean( String name )
    {
        setUuid( UUID.randomUUID() );
        setName( name );
    }

    public DocumentBean()
    {
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

    public DocumentType getType()
    {
        return type;
    }

    public void setType( DocumentType type )
    {
        this.type = type;
    }

    public double getSize()
    {
        return size;
    }

    public void setSize( double size )
    {
        this.size = size;
    }

    public String getAbsolutePath()
    {
        return absolutePath;
    }

    public void setAbsolutePath( String absolutePath )
    {
        this.absolutePath = absolutePath;
    }

    public String getHttpPath()
    {
        return httpPath;
    }

    public void setHttpPath( String httpPath )
    {
        this.httpPath = httpPath;
    }

    public List< DocumentBean > getChilds()
    {
        return childs;
    }

    public void setChilds( List< DocumentBean > childs )
    {
        this.childs = childs;
    }

    public boolean isPlayable()
    {
        return isPlayable;
    }

    public void setPlayable( boolean isPlayable )
    {
        this.isPlayable = isPlayable;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [name=" + getName() + ", childs=" + getChilds() + "]";
    }
}
