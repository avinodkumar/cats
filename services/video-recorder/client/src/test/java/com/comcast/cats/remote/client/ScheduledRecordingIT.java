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

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.service.RecordingEntityService;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.VideoRecorderUtil;

/**
 * Integration tests to verify scheduled recording.
 * 
 * @author SSugun00c
 * 
 */
public class ScheduledRecordingIT extends RemoteClientIT
{
    private static final String      IMPLEMENTATION_CLASS_SIMPLE_NAME = "DefaultRecordingEntityService";
    private static final String      INTERFACE_NAME                   = RecordingEntityService.class.getName();
    private static final long        SLEEP_ONE_SEC                    = 1000;

    protected RecordingEntityService recordingEntityService;

    @Before
    public void setup() throws NamingException
    {
        super.setup();

        String jndiName = EJB_JNDI_PREFIX + MODULE_NAME + "//" + IMPLEMENTATION_CLASS_SIMPLE_NAME + "!"
                + INTERFACE_NAME;

        logger.info( "[JNDINAME][" + jndiName + "]" );

        recordingEntityService = ( RecordingEntityService ) context.lookup( jndiName );

        Assert.assertNotNull( recordingEntityService );
    }

    @Test
    public void testScheduledRecordingOfStoppedRecording()
    {
        Integer recordingId = 1;

        try
        {
            recordingEntityService.submitScheduledRecording( recordingId );
        }
        catch ( VideoRecorderException e )
        {
            logger.info( "[VideoRecorderException][" + e.getMessage() + "]" );
            Assert.fail( e.getMessage() );
        }
        catch ( VideoRecorderConnectionException e )
        {
            logger.info( "[VideoRecorderConnectionException][" + e.getMessage() + "]" );
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    @Ignore
    public void testScheduledRecording() throws InterruptedException
    {
        String macId = "00:19:47:25:AD:7A";
        String videoServerIp = "192.168.160.202";
        int port = 1;
        int duration = 0;

        int scheduleCount = 10;

        try
        {
            logger.info( "***************** START NEW RECORDING *****************" );

            VideoRecorderResponse recorderResponse = recordingEntityService.submitRecording( macId, videoServerIp,
                    port, duration, null );

            Thread.sleep( SLEEP_ONE_SEC );

            Assert.assertEquals( recorderResponse.getResult(), WebServiceReturnEnum.SUCCESS );
            Assert.assertNull( recorderResponse.getRecordingList() );
            Assert.assertNotNull( recorderResponse.getRecording() );

            Assert.assertEquals( recorderResponse.getRecording().getStbMacAddress(), macId );
            Assert.assertEquals( recorderResponse.getRecording().getVideoServerIp(), videoServerIp );
            Assert.assertEquals( recorderResponse.getRecording().getVideoServerPort(), port );
            Assert.assertEquals( recorderResponse.getRecording().getRequestedDuration(), duration );

            Assert.assertNotNull( recorderResponse.getRecording().getRecordingStatus() );

            assert ( ( recorderResponse.getRecording().getRecordingStatus().getState() == VideoRecorderState.INITIALIZING
                    .name() ) || ( recorderResponse.getRecording().getRecordingStatus().getState() == VideoRecorderState.RECORDING
                    .name() ) );

            Assert.assertNotNull( recorderResponse.getRecording().getMediaInfoEntityList() );

            for ( MediaMetaData mediaMetaData : recorderResponse.getRecording().getMediaInfoEntityList() )
            {
                Assert.assertNotNull( mediaMetaData );
                Assert.assertNotNull( mediaMetaData.getFilePath() );
                Assert.assertTrue( VideoRecorderUtil.isExists( mediaMetaData.getFilePath() ) );
            }

            int recordingId = recorderResponse.getRecording().getId();

            logger.info( "***************** GET RECORDING STATUS *****************" );

            Thread.sleep( 2 * SLEEP_ONE_SEC );

            recorderResponse = recordingEntityService.getRecordingStatus( macId );

            Assert.assertEquals( recorderResponse.getResult(), WebServiceReturnEnum.SUCCESS );
            Assert.assertNull( recorderResponse.getRecordingList() );
            Assert.assertNotNull( recorderResponse.getRecording() );
            Assert.assertEquals( recorderResponse.getRecording().getId(), recordingId );
            Assert.assertNotNull( recorderResponse.getRecording().getRecordingStatus() );
            Assert.assertEquals( recorderResponse.getRecording().getRecordingStatus().getState(),
                    VideoRecorderState.RECORDING.name() );

            for ( int i = 1; i <= scheduleCount; i++ )
            {
                logger.info( "***************** SUBMIT SCHEDULED RECORDING [" + i + "]*****************" );
                Thread.sleep( 2 * SLEEP_ONE_SEC );

                try
                {
                    recordingEntityService.submitScheduledRecording( recordingId );
                }
                catch ( VideoRecorderConnectionException e )
                {
                    logger.error( e.getMessage() );
                }

                logger.info( "***************** GET RECORDING STATUS *****************" );

                Thread.sleep( 2 * SLEEP_ONE_SEC );

                recorderResponse = recordingEntityService.getRecordingStatus( macId );

                Assert.assertEquals( recorderResponse.getResult(), WebServiceReturnEnum.SUCCESS );
                Assert.assertNull( recorderResponse.getRecordingList() );
                Assert.assertNotNull( recorderResponse.getRecording() );
                Assert.assertEquals( recorderResponse.getRecording().getId(), recordingId );
                Assert.assertNotNull( recorderResponse.getRecording().getRecordingStatus() );
                Assert.assertEquals( recorderResponse.getRecording().getRecordingStatus().getState(),
                        VideoRecorderState.RECORDING.name() );

                Assert.assertNotNull( recorderResponse.getRecording().getMediaInfoEntityList() );

                for ( MediaMetaData mediaMetaData : recorderResponse.getRecording().getMediaInfoEntityList() )
                {
                    Assert.assertNotNull( mediaMetaData );
                    Assert.assertNotNull( mediaMetaData.getFilePath() );
                    Assert.assertTrue( VideoRecorderUtil.isExists( mediaMetaData.getFilePath() ) );
                }
            }

            logger.info( "***************** GET RECORDING STATUS *****************" );

            recorderResponse = recordingEntityService.stopRecordingById( recordingId );
            Assert.assertEquals( recorderResponse.getResult(), WebServiceReturnEnum.SUCCESS );
            Assert.assertEquals( recorderResponse.getRecording().getId(), recordingId );
        }
        catch ( VideoRecorderException videoRecorderException )
        {
            Assert.fail( videoRecorderException.getMessage() );
        }
    }
}
