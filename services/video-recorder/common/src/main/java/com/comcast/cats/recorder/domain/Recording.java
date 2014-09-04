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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a particular video recording
 * 
 * @author SSugun00c
 * 
 */
@XmlRootElement
@Entity
@Table( name = "pvr_recording" )
@NamedQueries(
    {
            @NamedQuery( name = "Recording.findAllByMacId", query = "SELECT recording " 
                    + " FROM Recording recording "
                    + " WHERE recording.stbMacAddress=:stbMacAddress" ),
            @NamedQuery( name = "Recording.findByMacIdAndAlias", query = "SELECT recording "
                    + " FROM Recording recording " 
                    + " WHERE recording.stbMacAddress=:stbMacAddress "
                    + " AND recording.name=:name" ),
            @NamedQuery( name = "Recording.findBeforeCreatedTime", query = "SELECT recording "
                    + " FROM Recording recording " 
                    + " WHERE recording.createdTime <= :createdTime" ),
            @NamedQuery( name = "Recording.findAllActiveRecording", query = "SELECT recording "
                    + " FROM Recording recording, RecordingStatus recordingStatus "
                    + " WHERE recording.id=recordingStatus.id " 
                    + " AND recordingStatus.state!='STOPPED' "
                    + " AND recordingStatus.state!='ERROR' " 
                    + " AND recordingStatus.state!='FORCE_CLOSE'" ),
            @NamedQuery( name = "Recording.findActiveRecordingByMacId", query = "SELECT recording "
                    + " FROM Recording recording, RecordingStatus recordingStatus "
                    + " WHERE recording.id=recordingStatus.id " 
                    + " AND recording.stbMacAddress=:stbMacAddress "
                    + " AND recordingStatus.state!='STOPPED' " 
                    + " AND recordingStatus.state!='ERROR' "
                    + " AND recordingStatus.state!='FORCE_CLOSE'" ),
            @NamedQuery( name = "Recording.findRecordingByMacId", query = "FROM Recording recording "
                    + " WHERE recording.stbMacAddress =:stbMacAddress" ),
            @NamedQuery( name = "Recording.findActiveRecordingByRecordingId", query = "SELECT recording "
                    + " FROM Recording recording, RecordingStatus recordingStatus, MediaMetaData mediaMetaData "
                    + " WHERE recording.id= mediaMetaData.recordingEntity.id " 
                    + " AND recording.id=recordingStatus.id"
                    + " AND recording.id=:recordingId" 
                    + " AND recordingStatus.state!='STOPPED'"
                    + " AND recordingStatus.state!='ERROR'" 
                    + " AND recordingStatus.state!='FORCE_CLOSE'" ),
            @NamedQuery( name = "Recording.findAllRecordingByMacIdWithLatestFirst", query = "SELECT recording "
                    + " FROM Recording recording, RecordingStatus recordingStatus "
                    + " WHERE recording.id=recordingStatus.id " 
                    + " AND recording.stbMacAddress=:stbMacAddress "
                    + " ORDER BY recording.id DESC" ) } )
public class Recording extends BaseEntity
{
    private static final long     serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "recording_id" )
    private int                   id;

    @Column( name = "recording_name" )
    private String                name;

    @Column( name = "stb_mac_address" )
    private String                stbMacAddress;

    @Column( name = "video_server_ip" )
    private String                videoServerIp;

    @Column( name = "video_server_port" )
    private int                   videoServerPort;

    @Column( name = "mrl" )
    private String                mrl;

    @Column( name = "requested_duration" )
    private int                   requestedDuration;

    @Column( name = "created_time" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date                  createdTime;

    @Column( name = "last_updated_time", updatable = false, insertable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date                  lastUpdatedTime;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "recording_id", insertable = true, updatable = true, nullable = true, unique = true )
    private RecordingStatus       recordingStatus;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "recordingEntity" )
    private List< MediaMetaData > mediaInfoEntityList;

    public Recording()
    {
        // TODO Auto-generated constructor stub
    }

    public Recording( String stbMacAddress, String videoServerIp, int videoServerPort, String mrl )
    {
        super();
        this.stbMacAddress = stbMacAddress;
        this.videoServerIp = videoServerIp;
        this.videoServerPort = videoServerPort;
        this.mrl = mrl;
    }

    public Recording( Integer recordingId )
    {
        super();
        this.id = recordingId;
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
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @XmlElement
    public String getStbMacAddress()
    {
        return stbMacAddress;
    }

    public void setStbMacAddress( String stbMacAddress )
    {
        this.stbMacAddress = stbMacAddress;
    }

    @XmlElement
    public String getVideoServerIp()
    {
        return videoServerIp;
    }

    public void setVideoServerIp( String videoServerIp )
    {
        this.videoServerIp = videoServerIp;
    }

    @XmlElement
    public int getVideoServerPort()
    {
        return videoServerPort;
    }

    public void setVideoServerPort( int videoServerPort )
    {
        this.videoServerPort = videoServerPort;
    }

    @XmlElement
    public String getMrl()
    {
        return mrl;
    }

    public void setMrl( String mrl )
    {
        this.mrl = mrl;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( Date createdTime )
    {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime()
    {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime( Date lastUpdatedTime )
    {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @XmlElement
    public RecordingStatus getRecordingStatus()
    {
        return recordingStatus;
    }

    public void setRecordingStatus( RecordingStatus recordingStatus )
    {
        this.recordingStatus = recordingStatus;
    }

    @XmlElementWrapper( name = "mediaList" )
    @XmlElement( name = "media" )
    public List< MediaMetaData > getMediaInfoEntityList()
    {
        return mediaInfoEntityList;
    }

    public void setMediaInfoEntityList( List< MediaMetaData > mediaInfoEntityList )
    {
        this.mediaInfoEntityList = mediaInfoEntityList;
    }

    public int getRequestedDuration()
    {
        return requestedDuration;
    }

    public void setRequestedDuration( int requestedDuration )
    {
        this.requestedDuration = requestedDuration;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [id=" + getId() + ", name=" + getName() + ", stbMacAddress="
                + getStbMacAddress() + ", videoServerIp=" + getVideoServerIp() + ", videoServerPort="
                + getVideoServerPort() + ", mrl=" + getMrl() + ", requestedDuration=" + getRequestedDuration()
                + ", createdTime=" + getCreatedTime() + ", lastUpdatedTime=" + getLastUpdatedTime()
                + ", recordingStatusEntity=" + getRecordingStatus() + ", mediaInfoEntityList="
                + getMediaInfoEntityList() + "]";
    }

}
