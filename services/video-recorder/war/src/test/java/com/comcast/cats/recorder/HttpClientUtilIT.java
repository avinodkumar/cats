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
package com.comcast.cats.recorder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.service.util.HttpClientUtil;

/**
 * Integration tests for {@link HttpClientUtil}.
 * 
 * @author ssugun00c
 * 
 */
public class HttpClientUtilIT extends VLCBaseTestCase
{
    private Logger logger = LoggerFactory.getLogger( getClass() );

    @Test
    public void testCustomObjectPost() throws Exception
    {
        Recording recording = getRecording();

        byte[] payload = HttpClientUtil.getPayload( recording, true );

        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.postForObject(
                "http://localhost:8080/video-recorder-service/rest/internal/recorder/submit", payload );

        logger.info( "[ RESPONSE  ] " + videoRecorderResponse );
    }

    private Recording getRecording()
    {

        Recording recording = new Recording();
        recording.setId( 110 );
        recording.setMrl( "rtsp://127.0.0.1/axis-media/media.amp?videocodec=h264" );
        List< MediaMetaData > mediaInfoEntityList = new ArrayList< MediaMetaData >();

        String filePath = "E:/temp/vlc-file-server/mac/2012/10/15/00-19-47-25-AD-7E/16-00-19-EST.mp4";
        String httpPath = "http://localhost:8080/mac/2012/10/15/00-19-47-25-AD-7E/16-00-19-EST.mp4";
        MediaMetaData mediaMetaData = new MediaMetaData( filePath, httpPath );
        mediaInfoEntityList.add( mediaMetaData );
        recording.setMediaInfoEntityList( mediaInfoEntityList );
        recording.setVideoServerIp( "192.168.160.202" );

        recording.setRecordingStatus( new RecordingStatus( VideoRecorderState.INITIALIZING.name(), "init" ) );

        return recording;
    }
}
