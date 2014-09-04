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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents individual video file in a {@link Recording}.
 * 
 * @author SSugun00c
 * @see Recording
 */
@XmlRootElement
@Entity
@Table( name = "pvr_media_metadata" )
@NamedQueries(
    { @NamedQuery( name = "MediaMetaData.deleteById", query = "DELETE FROM MediaMetaData mediaMetaData WHERE mediaMetaData.id = :mediaMetaDataId" ) } )
public class MediaMetaData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "media_metadata_id" )
    private int               id;

    @Column( name = "file_path" )
    private String            filePath;

    @Column( name = "http_path" )
    private String            httpPath;

    @Column( name = "created_time" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date              createdTime;

    @Column( name = "last_updated_time", updatable = false, insertable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date              lastUpdatedTime;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "parent_id" )
    private Recording         recordingEntity;

    @Transient
    private double            size;

    @Transient
    private boolean           playable         = false;

    public MediaMetaData()
    {
        // TODO Auto-generated constructor stub
    }

    public MediaMetaData( String filePath, String httpPath )
    {
        super();
        this.filePath = filePath;
        this.httpPath = httpPath;
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
    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath( String filePath )
    {
        this.filePath = filePath;
    }

    @XmlElement
    public String getHttpPath()
    {
        return httpPath;
    }

    public void setHttpPath( String httpPath )
    {
        this.httpPath = httpPath;
    }

    @XmlElement
    public Date getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( Date createdTime )
    {
        this.createdTime = createdTime;
    }

    @XmlElement
    public Date getLastUpdatedTime()
    {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime( Date lastUpdatedTime )
    {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @XmlTransient
    public Recording getRecordingEntity()
    {
        return recordingEntity;
    }

    public void setRecordingEntity( Recording recordingEntity )
    {
        this.recordingEntity = recordingEntity;
    }

    @XmlElement
    public double getSize()
    {
        return size;
    }

    public void setSize( double size )
    {
        this.size = size;
    }

    @XmlElement
    public boolean isPlayable()
    {
        return playable;
    }

    public void setPlayable( boolean playable )
    {
        this.playable = playable;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [id=" + getId() + ", filePath=" + getFilePath() + ", httpPath=" + getHttpPath()
                + ", size (in bytes) =" + getSize() + ", playable =" + isPlayable() + ", createdTime="
                + getCreatedTime() + ", lastUpdatedTime=" + getLastUpdatedTime() + "]";
    }
}
