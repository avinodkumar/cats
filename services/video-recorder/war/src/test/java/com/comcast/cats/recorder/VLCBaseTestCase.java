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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;

/**
 * Base test case for all tests cases related to VLCJ.
 * 
 * @author ssugun00c
 * 
 */
public class VLCBaseTestCase
{
    /**
     * Installation path to VLC player
     */
    private static final String    VIDEO_RECORDING_BASE_DIR      = "E:/temp/vlc-file-server/";
    private static final String    VIDEO_RECORDING_HTTP_BASE_URL = "http://localhost/";
    private static final String    VLC_EXECUTABLE_PATH           = "E:/dev-tools/vlc/vlc-2.0.0-win32/";
    private static final String    VLC_TELNET_HOST               = "127.0.0.1";
    private static final String    VLC_TELNET_PASSWORD           = "admin123";

    protected static final long    DEFAULT_DURATION              = 30 * 1000;
    protected static final Integer DEFAULT_PORT                  = 1;

    protected Logger               logger                        = LoggerFactory.getLogger( getClass() );

    @Before
    public void setup()
    {
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_EXECUTABLE_PATH, VLC_EXECUTABLE_PATH );
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST, VLC_TELNET_HOST );
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD, VLC_TELNET_PASSWORD );

        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH,
                VIDEO_RECORDING_BASE_DIR );
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH,
                VIDEO_RECORDING_HTTP_BASE_URL );
    }

    protected Settop getSettop() throws JAXBException, FileNotFoundException
    {
        SettopImpl settop = new SettopImpl();
        SettopDesc settopDesc = new SettopDesc();

        InputStream inputStream = null;
        try
        {
            JAXBContext jc = JAXBContext.newInstance( SettopDesc.class );
            Unmarshaller um = jc.createUnmarshaller();

            inputStream = new java.io.FileInputStream( "src/test/resources/settop.xml" );
            settopDesc = ( SettopDesc ) um.unmarshal( inputStream );
        }
        catch ( JAXBException jaxbException )
        {
            jaxbException.printStackTrace();
        }
        catch ( FileNotFoundException fileNotFoundException )
        {
            fileNotFoundException.printStackTrace();
        }
        finally
        {
            if ( null != inputStream )
            {
                try
                {
                    inputStream.close();
                }
                catch ( IOException e )
                {
                    logger.error( e.getMessage() );
                }
            }
        }

        settop.setSettopInfo( settopDesc );
        return settop;
    }

    protected Recording getRecording( int recordingId )
    {

        Recording recording = new Recording( recordingId );
        recording.setStbMacAddress( "00-19-47-25-AD-7E" );
        recording.setMrl( "rtsp://192.168.160.202/axis-media/media.amp?videocodec=h264" );

        String filePath = "E:/temp/vlc-file-server/mac/2012/10/15/00-19-47-25-AD-7E/16-00-19-EST.mp4";
        String httpPath = "http://localhost:8080/mac/2012/10/15/00-19-47-25-AD-7E/16-00-19-EST.mp4";
        MediaMetaData mediaMetaData = new MediaMetaData( filePath, httpPath );

        List< MediaMetaData > mediaInfoEntityList = new ArrayList< MediaMetaData >();
        mediaInfoEntityList.add( mediaMetaData );
        recording.setMediaInfoEntityList( mediaInfoEntityList );

        recording.setRecordingStatus( new RecordingStatus( VideoRecorderState.INITIALIZING.name(), "init" ) );
        return recording;
    }
}
