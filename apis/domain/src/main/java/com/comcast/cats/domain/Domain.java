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
package com.comcast.cats.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 * Base class for all domain classes. Use of {@link Externalizable} is on hold
 * until we crack the implementation of readExternal() method.
 * 
 * @author subinsugunan
 * 
 */
@XmlRootElement( name = "domain" )
public class Domain implements Serializable
{
    private static final long serialVersionUID = -3038292437723947879L;

    private String            id;
    private String            name;
    private Long              revision;
    private Date              createdDate;
    private Date              lastModifiedDate;
    private User              createdBy;
    private User              lastModifiedBy;

    public Domain()
    {
        super();
    }

    public Domain( String id )
    {
        this.id = id;
    }

    public Domain( String id, String name )
    {
        this.id = id;
        this.name = name;
    }

    public Domain( Domain domain )
    {
        this( domain.id, domain.name );
    }

    @XmlAttribute
    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @XmlAttribute
    public Long getRevision()
    {
        return revision;
    }

    public void setRevision( Long revision )
    {
        this.revision = revision;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate( Date createdDate )
    {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate( Date lastModifiedDate )
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void writeExternal( ObjectOutput out ) throws IOException
    {
        StringWriter writer = new StringWriter();

        try
        {
            JAXBContext context = JAXBContext.newInstance( this.getClass() );
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal( this, writer );
            out.writeUTF( writer.toString() );
        }
        catch ( JAXBException e )
        {
            e.printStackTrace();
        }
        finally
        {
            out.close();
        }
    }

    // @Override
    public void readExternal( ObjectInput in )
    {
        throw new UnsupportedOperationException( "Not implemented. Use readExternalObject( ObjectInput in ) method " );
    }

    public Object readExternalObject( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        String str = in.readUTF();
        Object object = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance( this.getClass() );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader( str );

            object = Class.forName( this.getClass().getCanonicalName() ).cast( unmarshaller.unmarshal( reader ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return object;
    }

	@XmlElement()
    public User getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( User createdBy )
    {
        this.createdBy = createdBy;
    }

    public User getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy( User lastModifiedBy )
    {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [id=" + getId() + ", name=" + getName() + ", createdDate="
                + getCreatedDate() + ", lastModifiedDate=" + getLastModifiedDate() + ", revision=" + getRevision()
                + "]";
    }
}
