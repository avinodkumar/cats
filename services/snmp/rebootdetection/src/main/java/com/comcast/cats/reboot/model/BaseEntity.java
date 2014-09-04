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
package com.comcast.cats.reboot.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * BaseEntity which should be used as base class for all MDS entities.
 * 
 * @author cfrede001
 * 
 */
@MappedSuperclass
public class BaseEntity implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 2575264924028878301L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", updatable = false, nullable = false )
    protected Long            id               = 0L;

    @Version
    @Column( name = "version" )
    protected Long            version          = 0L;

    /*
     * Add UUID support to account for external entity references.
     */
    @Column( nullable = false, unique = true )
    protected String          uuid;

    public BaseEntity()
    {
        super();
    }

    public BaseEntity( String uuid )
    {
        super();
        this.uuid = uuid;
    }

    @XmlAttribute( )
    public Long getId()
    {
        return id;
    }

    @XmlTransient
    public Long getVersion()
    {
        return version;
    }

    @XmlAttribute
    public String getUuid()
    {
        return uuid;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public void setVersion( Long version )
    {
        this.version = version;
    }

    public void setUuid( String uuid )
    {
        this.uuid = uuid;
    }

    @PrePersist
    public void prepersist()
    {
        if ( null == this.uuid || this.uuid.isEmpty() )
        {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        BaseEntity other = ( BaseEntity ) obj;
        if ( id == null )
        {
            if ( other.id != null )
                return false;
        }
        else if ( !id.equals( other.id ) )
            return false;
        if ( version == null )
        {
            if ( other.version != null )
                return false;
        }
        else if ( !version.equals( other.version ) )
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [id=" + getId() + ", uuid=" + getUuid() + ", version="
                + getVersion() + "]";
    }
}
