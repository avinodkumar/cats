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
package com.comcast.cats.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.domain.Recording;

/**
 * Integration test to verify concurrency isues.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderServiceConcurrencyEndPointIT
{
    private static final String            BASE_URL        = "http://localhost:8080/";

    private Logger                         logger          = LoggerFactory.getLogger( getClass() );

    private VideoRecorderEndPoint          videoRecorderEndPoint;
    private VideoRecorderService           videoRecorderService;

    private static final List< Recording > recordingList   = new LinkedList< Recording >();
    private static final List< Integer >   recordingIdList = new LinkedList< Integer >();

    public VideoRecorderServiceConcurrencyEndPointIT() throws MalformedURLException
    {
        String spec = BASE_URL + VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_WSDL_LOCATION;
        URL wsdlDocumentLocation = new URL( spec );

        videoRecorderEndPoint = new VideoRecorderEndPoint( wsdlDocumentLocation );
        Assert.assertNotNull( videoRecorderEndPoint );

        videoRecorderService = videoRecorderEndPoint.getVideoRecorderServiceImplPort();
        Assert.assertNotNull( videoRecorderService );
    }

    @Before
    public void setup() throws NamingException
    {
        recordingList.add( new Recording( "E4:48:C7:A8:2A:F2", "192.168.160.202", 2, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:1B:12", "192.168.160.202", 2, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:2A:12", "192.168.160.202", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:1A:56", "192.168.160.202", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:4B:C8", "192.168.160.202", 2, null ) );
        recordingList.add( new Recording( "54:D4:6F:96:D9:BC", "192.168.160.202", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:2E:C4", "192.168.160.202", 1, null ) );
    }

    @Test
    public final void testConcurrentStartStop() throws InterruptedException
    {
        logger.info( "************************[START ALL]*************************" );
        startAll();

        // Wait for sometime to actually start telnet interface.
        Thread.sleep( 20 * 1000 );

        logger.info( "*************************[STOP ALL BY ID]*************************" );
        stopAllByRecordingId();

        // Wait for sometime to retrieve the response. Hit terminate once all
        // responses are available
        Thread.sleep( 10 * 1000 );
    }

    private void stopAllByRecordingId() throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool( 16 );

        for ( final int recordingId : recordingIdList )
        {
            exec.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    runConcurrentStopByrRecordingId( recordingId );
                }

            } );
        }

        exec.shutdown();
    }

    private void startAll() throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool( 16 );

        for ( final Recording recording : recordingList )
        {
            exec.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    runConcurrentStart( recording.getStbMacAddress(), recording.getVideoServerIp(),
                            recording.getVideoServerPort() );
                }

            } );
        }

        exec.shutdown();
    }

    private void runConcurrentStart( String macId, String videoServerIp, int port )
    {
        logger.info( "[START][" + macId + "]" );
        VideoRecorderResponse recorderResponse = videoRecorderService.start( macId, videoServerIp, port, 0, null );
        logger.info( "[START][" + macId + "][" + recorderResponse.getResult() + "][" + recorderResponse.getMessage()
                + "]" );

        if ( null != recorderResponse.getRecording() )
        {
            recordingIdList.add( recorderResponse.getRecording().getId() );
        }

        logger.info( "--------------------------------------------" );
    }

    private void runConcurrentStopByrRecordingId( int recordingId )
    {
        logger.info( "[STOP][" + recordingId + "]" );
        VideoRecorderResponse recorderResponse = videoRecorderService.stopById( recordingId );
        logger.info( "[STOP][" + recordingId + "][" + recorderResponse.getResult() + "][" + recorderResponse + "]" );
        logger.info( "--------------------------------------------" );

    }
}
