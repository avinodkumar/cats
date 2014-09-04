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
package com.comcast.cats.config.ui.recording;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.RecordingStatus;

/**
 * Manged Bean that represents recordings for a settop.
 * 
 * @author skurup00c
 * 
 */
public class SettopRecordingBean
{
    Slot                   slot;
    SettopDesc             settop;
    List<MediaInfoBean>    mediaInfo            = new ArrayList< MediaInfoBean >();
    String                 state                = VideoRecorderState.INITIALIZING.name();
    List< SettopRecordingBean >  recordingHistoryList = new ArrayList< SettopRecordingBean >();
    String                 statusMessage;
    StreamedContent        downloadableFile;
    MediaInfoBean          latestMedia;
    Date createdTime;
    String name;
    int id;


    @Inject
    SettopRecordingService settopRecordingService;

    private static Logger  logger               = LoggerFactory.getLogger( SettopRecordingBean.class );


    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
    
    public List<MediaInfoBean> getMediaInfo()
    {
    	System.out.println("get media "+mediaInfo.size());
        return mediaInfo;
    }

    public void setMediaInfo( List<MediaInfoBean> mediaInfo )
    {
        this.mediaInfo = mediaInfo;
    }

    public Slot getSlot()
    {
        return slot;
    }

    public void setSlot( Slot slot )
    {
        this.slot = slot;
    }

    public SettopDesc getSettop()
    {
        return settop;
    }

    public void setSettop( SettopDesc settop )
    {
        this.settop = settop;
    }

    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        try
        {
            this.state = Enum.valueOf( VideoRecorderState.class, state ).name();
        }
        catch ( IllegalArgumentException e )
        {
            logger.debug( "Invalid State " + e.getMessage() );
            // state invalid
            this.state = VideoRecorderState.INITIALIZING.name();
        }
    }

    public void setStatus( RecordingStatus status )
    {
        logger.trace( "status " + status );
        if ( status != null )
        {
            setState( status.getState() );
            setStatusMessage( status.getMessage() );
        }

    }
    
    public void setMediaMetaData(List<MediaMetaData> mediaMetaDataList){
        if ( mediaMetaDataList != null && !mediaMetaDataList.isEmpty())
        {
            mediaInfo.clear();
            setLatestMedia( convertToMediaInfoBean( mediaMetaDataList.get( mediaMetaDataList.size()-1 ) ));
            for(MediaMetaData mediaMetaData : mediaMetaDataList){
                MediaInfoBean mediaInfoBean = convertToMediaInfoBean( mediaMetaData );
                mediaInfo.add( mediaInfoBean );
            }
        }
    }
    
    private MediaInfoBean convertToMediaInfoBean(MediaMetaData mediaMetaData){
        MediaInfoBean mediaInfoBean = new MediaInfoBean();
        mediaInfoBean.setFilePath( mediaMetaData.getHttpPath() );
        mediaInfoBean.setCreatedDate( mediaMetaData.getCreatedTime() );
        mediaInfoBean.setLastModifiedDate( mediaMetaData.getLastUpdatedTime() );
        mediaInfoBean.setRecording( this );
        mediaInfoBean.setId( mediaMetaData.getId() );
  //      mediaInfoBean.setPlayable(mediaMetaData.isPlayable()); not supported as of 1.0.1
        
        double bytes = mediaMetaData.getSize();
        int sizeInMB = ( int ) ( bytes/(1024*1024) );
        mediaInfoBean.setFileSize( sizeInMB );
        
       return mediaInfoBean;
    }

    public void setRecordingHistoryList( List< SettopRecordingBean > recordingHistoryList )
    {
        this.recordingHistoryList = recordingHistoryList;
    }

    public List< SettopRecordingBean > getRecordingHistoryList()
    {
        return recordingHistoryList;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage( String statusMessage )
    {
        this.statusMessage = statusMessage;
    }
    
    public StreamedContent getDownloadableFile(){
        return downloadableFile;
    }
    
    public void setDownloadableFile(StreamedContent downloadableFile){
        this.downloadableFile = downloadableFile;
    }
    
    public MediaInfoBean getLatestMedia()
    {
        return latestMedia;
    }

    public void setLatestMedia( MediaInfoBean latestMedia )
    {
        this.latestMedia = latestMedia;
    }

    public void setCreatedTime( Date createdTime )
    {
       this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
}
