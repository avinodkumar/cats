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
package com.comcast.cats.recorder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Current status of a {@link Recording}.
 * 
 * @author SSugun00c
 * 
 */
@XmlRootElement
@Entity
@Table( name = "pvr_recording_status" )
public class RecordingStatus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "recording_status_id" )
    private int               id;

    @Column( name = "state" )
    private String            state;

    @Column( name = "message" )
    private String            message;

    public RecordingStatus()
    {
        // TODO Auto-generated constructor stub
    }

    public RecordingStatus( String state, String message )
    {
        super();
        this.state = state;
        this.message = message;
    }

    public RecordingStatus( int id, String state, String message )
    {
        super();
        this.id = id;
        this.state = state;
        this.message = message;
    }

    @XmlElement
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @XmlElement
    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    @XmlElement
    public String getMessage()
    {
        if ( null == message )
        {
            message = "No message available to display";
        }
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [id=" + getId() + ", state=" + getState() + ", message=" + getMessage() + "]";
    }
}
