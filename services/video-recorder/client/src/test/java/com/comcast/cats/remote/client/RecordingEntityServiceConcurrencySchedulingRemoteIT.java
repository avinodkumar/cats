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
package com.comcast.cats.remote.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.service.RecordingEntityService;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.service.VideoRecorderEndPoint;
import com.comcast.cats.service.VideoRecorderService;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Integration tests to verify concurrency through SOAP client.
 * 
 * @author SSugun00c
 * 
 */
public class RecordingEntityServiceConcurrencySchedulingRemoteIT extends RemoteClientIT
{
    private static final String            BASE_URL                         = "http://localhost:8080/";
    private static final String            IMPLEMENTATION_CLASS_SIMPLE_NAME = "DefaultRecordingEntityService";
    private static final String            INTERFACE_NAME                   = RecordingEntityService.class.getName();
    private static final long              SLEEP_ONE_SEC                    = 1000;

    private static RecordingEntityService  recordingEntityService;
    private static VideoRecorderService    videoRecorderService;

    private static final List< Recording > recordingList                    = new LinkedList< Recording >();
    private static final List< Integer >   recordingIdList                  = new LinkedList< Integer >();
    private static final int               SCHEDULE_COUNT                   = 1;

    protected static Logger                logger                           = LoggerFactory
                                                                                    .getLogger( RecordingEntityServiceConcurrencySchedulingRemoteIT.class );

    @Before
    public void setup() throws NamingException
    {
        super.setup();

        String jndiName = EJB_JNDI_PREFIX + MODULE_NAME + "//" + IMPLEMENTATION_CLASS_SIMPLE_NAME + "!"
                + INTERFACE_NAME;

        logger.info( "[JNDINAME][" + jndiName + "]" );

        recordingEntityService = ( RecordingEntityService ) context.lookup( jndiName );

        Assert.assertNotNull( recordingEntityService );

        String spec = BASE_URL + VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_WSDL_LOCATION;

        try
        {
            URL wsdlDocumentLocation = new URL( spec );
            VideoRecorderEndPoint videoRecorderEndPoint = new VideoRecorderEndPoint( wsdlDocumentLocation );
            Assert.assertNotNull( videoRecorderEndPoint );

            videoRecorderService = videoRecorderEndPoint.getVideoRecorderServiceImplPort();
            Assert.assertNotNull( videoRecorderService );
        }
        catch ( MalformedURLException e )
        {
            Assert.fail( e.getMessage() );
        }

        recordingList.add( new Recording( "E4:48:C7:A8:2A:F2", "192.168.160.202", 2, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:1B:12", "192.168.160.203", 2, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:2A:12", "192.168.160.204", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:1A:56", "192.168.160.205", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:4B:C8", "192.168.160.206", 2, null ) );
        recordingList.add( new Recording( "54:D4:6F:96:D9:BC", "192.168.160.207", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:2E:C4", "192.168.160.208", 1, null ) );
        // --From here . Everything is Dummy
        recordingList.add( new Recording( "E4:48:C7:A8:2A:F3", "192.168.160.209", 1, null ) );
    }

    @Test
    public void testConcurrentScheduling() throws InterruptedException
    {
        logger.info( "************************[START ALL]*************************" );
        startAll();

        // Wait for sometime to actually start telnet interface.
        Thread.sleep( 60 * 1000 );

        logger.info( "*************************[START SCHEDULED RECORDING BY ID]*************************" );
        startScheduledRecording();

        // Wait for sometime to retrieve the response. Hit terminate once all
        // responses are available
        Thread.sleep( SCHEDULE_COUNT * 50 * 1000 );

        logger.info( "*************************[STOP RECORDING BY ID]*************************" );
        stopAllByRecordingId();
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

    private void startScheduledRecording() throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool( 16 );

        for ( final int recordingId : recordingIdList )
        {
            exec.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    runTimeoutRecordingId( recordingId );
                }

            } );
        }

        exec.shutdown();
    }

    private void runTimeoutRecordingId( int recordingId )
    {
        logger.info( "[RUN-TIMEOUT][" + recordingId + "]" );

        VideoRecorderResponse recorderResponse = null;

        for ( int i = 1; i <= SCHEDULE_COUNT; i++ )
        {

            try
            {
                recordingEntityService.submitScheduledRecording( recordingId );

                Thread.sleep( 2 * SLEEP_ONE_SEC );

                recorderResponse = videoRecorderService.getStatusById( recordingId );

                Assert.assertEquals( recorderResponse.getResult(), WebServiceReturnEnum.SUCCESS );
                Assert.assertNull( recorderResponse.getRecordingList() );
                Assert.assertNotNull( recorderResponse.getRecording() );
                Assert.assertEquals( recorderResponse.getRecording().getId(), recordingId );
                Assert.assertNotNull( recorderResponse.getRecording().getRecordingStatus() );
                Assert.assertEquals( recorderResponse.getRecording().getRecordingStatus().getState(),
                        VideoRecorderState.RECORDING.name() );

                Assert.assertNotNull( recorderResponse.getRecording().getMediaInfoEntityList() );

                logger.info( "[RUN-TIMEOUT][" + recorderResponse.getResult() + "]["
                        + recorderResponse.getRecording().getMediaInfoEntityList().size() + "]" );

                for ( MediaMetaData mediaMetaData : recorderResponse.getRecording().getMediaInfoEntityList() )
                {
                    Assert.assertNotNull( mediaMetaData );
                    Assert.assertNotNull( mediaMetaData.getFilePath() );
                    // Assert.assertTrue( VideoRecorderUtil.isExists(
                    // mediaMetaData.getFilePath() ) );
                }
            }
            catch ( VideoRecorderConnectionException e )
            {
                logger.error( e.getMessage() );
            }
            catch ( VideoRecorderException e )
            {
                logger.error( e.getMessage() );
            }
            catch ( InterruptedException e )
            {
                logger.error( e.getMessage() );
            }

            logger.info( "--------------------------------------------" );
        }
    }

    private static void stopAllByRecordingId() throws InterruptedException
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

    private static void runConcurrentStopByrRecordingId( int recordingId )
    {
        logger.info( "[STOP][" + recordingId + "]" );
        VideoRecorderResponse recorderResponse = videoRecorderService.stopById( recordingId );
        logger.info( "[STOP][" + recordingId + "][" + recorderResponse.getResult() + "][" + recorderResponse + "]" );
        logger.info( "--------------------------------------------" );

    }
}