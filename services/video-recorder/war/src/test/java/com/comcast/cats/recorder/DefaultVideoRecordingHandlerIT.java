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

import org.junit.Test;
import org.testng.Assert;

import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * Integration tests for {@link DefaultVideoRecordingHandler}.
 * 
 * @author SSugun00c
 * 
 */
public class DefaultVideoRecordingHandlerIT extends VLCBaseTestCase
{

    private VideoRecordingHandler videoRecordingHandler;

    public DefaultVideoRecordingHandlerIT()
    {
        videoRecordingHandler = new DefaultVideoRecordingHandler();
    }

    @Test
    public void testSubmitRecordingWithDefaultOptions() throws Exception
    {
        Recording recording = getRecording( 112 );
        videoRecordingHandler.submitRecording( recording );

        try
        {
            Thread.sleep( DEFAULT_DURATION );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }

        videoRecordingHandler.stopRecording( recording.getId() );

        try
        {
            Thread.sleep( DEFAULT_DURATION / 2 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubmitRecordingAndCheckStatus() throws Exception
    {
        Recording recording = getRecording( 113 );
        videoRecordingHandler.submitRecording( recording );

        // Wait for sometime to finish recording, in the mean time , we'll check
        // the status
        int count = 500;
        for ( int interval = 1; interval <= count; interval++ )
        {
            try
            {
                logger.info( "Status: " + videoRecordingHandler.getRecordingStatus( recording.getId() ) );
                Thread.sleep( DEFAULT_DURATION / count );
            }
            catch ( VideoRecorderException e )
            {
                logger.warn( e.getMessage() );
            }
            catch ( InterruptedException e )
            {
                Assert.fail( e.getMessage() );
            }

            if ( interval == 400 )
            {
                logger.info( "STOPPING RECORD" );

                videoRecordingHandler.stopRecording( recording.getId() );
            }
        }
    }

    @Test
    public void testSubmitConcurrentRecordingAndCheckStatus() throws Exception
    {
        Recording recording_1 = getRecording( 113 );
        Recording recording_2 = getRecording( 114 );

        videoRecordingHandler.submitRecording( recording_1 );
        videoRecordingHandler.submitRecording( recording_2 );

        // Wait for sometime to finish recording, in the mean time , we'll check
        // the status
        int count = 500;
        for ( int interval = 1; interval <= count; interval++ )
        {
            try
            {
                logger.info( "[Status][recording_1] " + videoRecordingHandler.getRecordingStatus( recording_1.getId() ) );
                logger.info( "[Status][recording_2] " + videoRecordingHandler.getRecordingStatus( recording_2.getId() ) );

                Thread.sleep( DEFAULT_DURATION / count );
            }
            catch ( VideoRecorderException e )
            {
                logger.warn( e.getMessage() );
            }
            catch ( InterruptedException e )
            {
                Assert.fail( e.getMessage() );
            }

            if ( interval == 400 )
            {
                logger.info( "STOPPING RECORD" );

                videoRecordingHandler.stopRecording( recording_1.getId() );
                videoRecordingHandler.stopRecording( recording_2.getId() );
            }
        }
    }
}
