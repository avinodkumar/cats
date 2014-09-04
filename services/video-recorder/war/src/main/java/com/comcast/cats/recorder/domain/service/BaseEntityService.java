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
package com.comcast.cats.recorder.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.service.util.VideoRecorderUtil;

/**
 * Base class for all entity service.
 * 
 * @author ssugun00c
 * 
 */
public abstract class BaseEntityService
{
    protected final Logger        LOGGER           = LoggerFactory.getLogger( getClass() );
    /**
     * Name of the persistence unit specified in the persistence.xml.
     */
    protected static final String PERSISTENCE_UNIT = "pvr-persistence-unit";

    protected boolean isActive( Recording recording )
    {
        boolean active = false;

        if ( ( VideoRecorderState.INITIALIZING.toString().equalsIgnoreCase( recording.getRecordingStatus().getState() ) )
                || ( VideoRecorderState.RECORDING.toString().equalsIgnoreCase( recording.getRecordingStatus()
                        .getState() ) )
                || ( VideoRecorderState.BUFFERING.toString().equalsIgnoreCase( recording.getRecordingStatus()
                        .getState() ) ) )
        {
            active = true;
        }

        return active;
    }

    protected void refreshMediaMetaData( Recording recording )
    {
        List< MediaMetaData > mediaMetaDataList = recording.getMediaInfoEntityList();

        for ( MediaMetaData mediaMetaData : mediaMetaDataList )
        {
            String filePath = mediaMetaData.getFilePath();

            if ( ( null != filePath ) && ( !filePath.isEmpty() ) )
            {
                mediaMetaData.setSize( VideoRecorderUtil.getFileSize( filePath ) );
                // This has a negative impact w.r.t to time.
                // mediaMetaData.setPlayable( VideoRecorderUtil.isPlayable(
                // filePath ) );
            }
        }
    }
}
