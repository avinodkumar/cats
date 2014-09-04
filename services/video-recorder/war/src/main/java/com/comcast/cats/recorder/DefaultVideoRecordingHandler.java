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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.info.VideoRecordingOptions;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.recorder.exception.VideoRecorderInstantiationException;
import com.comcast.cats.service.util.ApplicationConfigUtil;
import com.comcast.cats.service.util.AssertUtil;

/**
 * Default implementation of {@link VideoRecordingHandler}
 * 
 * @author ssugun00c
 * @see VideoRecordingErrorHandler
 * 
 */
public class DefaultVideoRecordingHandler extends ConcurrentHashMap< Integer, VideoRecorderTask > implements
        VideoRecordingHandler, VideoRecordingErrorHandler
{
    private static List< Integer >          activeTelnetPortList       = new LinkedList< Integer >();
    private static List< Integer >          recentlyUsedTelnetPortList = new LinkedList< Integer >();

    private static final long               serialVersionUID           = 1L;
    transient protected Logger              logger                     = LoggerFactory.getLogger( getClass() );

    // We need to support 16 concurrent recording
    private static final int                POOL_SIZE                  = 20;

    transient private final ExecutorService pool                       = Executors.newFixedThreadPool( POOL_SIZE );

    @Override
    public void submitRecording( Recording recording ) throws VideoRecorderInstantiationException
    {
        AssertUtil.isNull( "recorderId cannot be null", recording.getId() );
        AssertUtil.isNullOrEmpty( "mrl cannot be null or empty", recording.getMrl() );

        // Get the latest file in the list
        String filePath = recording.getMediaInfoEntityList().get( recording.getMediaInfoEntityList().size() - 1 )
                .getFilePath();

        logger.info( "Start record request receieved for videoRecorderId [" + recording.getId() + "]["
                + recording.getMrl() + "][" + filePath + "]" );

        VideoRecorderTask videoRecorderTask = getVideoRecorder( recording, filePath, new VideoRecordingOptions() );

        logger.info( "Submiting videoRecorderTask [" + videoRecorderTask + "]" );
        submitRecording( recording, videoRecorderTask );
    }

    private VideoRecorderTask getVideoRecorder( Recording recording, String filePath,
            VideoRecordingOptions videoRecordingOptions ) throws VideoRecorderInstantiationException
    {

        DefaultVideoRecorderTask videoRecorderTask = ( DefaultVideoRecorderTask ) get( recording.getId() );

        if ( null == videoRecorderTask )
        {
            if ( size() > VideoRecorderServiceConstants.MAX_CONCURRENT_RECORDING )
            {
                throw new VideoRecorderInstantiationException( "Can't create new VideoRecorder. Max limit of ["
                        + VideoRecorderServiceConstants.MAX_CONCURRENT_RECORDING + "] reached" );
            }
            else
            {
                if ( null == videoRecordingOptions )
                {
                    videoRecordingOptions = new VideoRecordingOptions();
                    logger.info( "Using default videoRecordingOptions [" + videoRecordingOptions
                            + "] for videoRecorderId [" + recording.getId() + "]" );
                }

                synchronized ( this )
                {
                    videoRecorderTask = new DefaultVideoRecorderTask( recording, filePath, videoRecordingOptions, this );
                    int telnetport = getTelnetPort();
                    videoRecorderTask.setTelnetPort( telnetport );
                    videoRecorderTask.setCommand( getVlcCommand( recording.getMrl(), filePath, telnetport ) );

                    put( recording.getId(), videoRecorderTask );
                }
            }
        }

        return videoRecorderTask;
    }

    private synchronized String getVlcCommand( String mrl, String filePath, int telnetport )
    {
        String command = System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_EXECUTABLE_PATH )
                + "vlc -I dummy " + mrl + " --sout file/" + VideoRecorderServiceConstants.DEFAULT_EXTENSION + ":"
                + filePath + " --extraintf=telnet --telnet-password " + getTelnetPassword() + " --telnet-port "
                + telnetport;

        logger.info( "[VLC command][" + command + "]" );

        return command;
    }

    private String getTelnetPassword()
    {
        return System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD );
    }

    private synchronized int getTelnetPort()
    {
        int port = ApplicationConfigUtil.getTelnetPortRangeStart();

        if ( getMax( activeTelnetPortList ) >= ApplicationConfigUtil.getTelnetPortRangeEnd() )
        {
            cleanupRecentlyUsedTelnetPortList();
        }

        while ( activeTelnetPortList.contains( port ) || recentlyUsedTelnetPortList.contains( port ) )
        {
            port++;
        }

        activeTelnetPortList.add( port );

        logger.info( "Calculated Telnet port [" + port + "]. Current list of active ports " + activeTelnetPortList );

        return port;
    }

    private void submitRecording( Recording recording, VideoRecorderTask videoRecorderTask )
            throws VideoRecorderInstantiationException
    {
        final String videoRecorderState = videoRecorderTask.getRecordingStatus().getState();

        if ( ( null != videoRecorderState )
                && ( VideoRecorderState.INITIALIZING.toString().equalsIgnoreCase( videoRecorderState ) ) )
        {
            logger.info( "Submiting start record task for videoRecorderId [" + recording.getId() + "]" );
            pool.submit( videoRecorderTask );
        }
        else
        {
            throw new VideoRecorderInstantiationException( "Cannot Submit video record for ["
                    + recording.getStbMacAddress() + "][" + recording.getStbMacAddress() + "]["
                    + recording.getVideoServerPort() + "]. An existing recorder found with state : "
                    + videoRecorderState + " videoRecorderId is [" + recording.getId() + "]" );
        }
    }

    public void stopRecording( Integer recordingId ) throws VideoRecorderException, VideoRecorderConnectionException
    {
        AssertUtil.isNull( "recorderId cannot be null", recordingId );

        logger.info( "Stop record request receieved for videoRecorderId [" + recordingId + "]" );

        VideoRecorderTask videoRecorderTask = get( recordingId );

        if ( null == videoRecorderTask )
        {
            throw new VideoRecorderException( "No active recorder found for videoRecorderId [" + recordingId + "]."
                    + "The record operation may already completed." );
        }

        logger.info( "Submiting stop record task for videoRecorderId [" + recordingId + "] with telnet port ["
                + videoRecorderTask.getTelnetPort() + "]" );

        // Stop recording
        videoRecorderTask.stopRecording();

        logger.info( "Removing  telnet port [" + videoRecorderTask.getTelnetPort() + "]" );

        // This block will never execute if
        // videoRecorderTask.stopRecording()
        // fails.

        synchronized ( this )
        {
            activeTelnetPortList.remove( Integer.valueOf( videoRecorderTask.getTelnetPort() ) );
            recentlyUsedTelnetPortList.add( Integer.valueOf( videoRecorderTask.getTelnetPort() ) );
            logger.info( "Complted removing telnet port. Current list of active ports [" + activeTelnetPortList + "]" );
            remove( recordingId );
        }
    }

    private void cleanupRecentlyUsedTelnetPortList()
    {
        logger.info( "Cleaning up recently used Telnet ports" );

        while ( !recentlyUsedTelnetPortList.isEmpty() )
        {
            recentlyUsedTelnetPortList.remove( 0 );
        }
    }

    private int getMax( List< Integer > telnetPortList )
    {
        int max = 0;

        for ( int port : telnetPortList )
        {
            if ( port > max )
            {
                max = port;
            }
        }

        return max;
    }

    @Override
    public RecordingStatus getRecordingStatus( Integer recordingId ) throws VideoRecorderException
    {
        AssertUtil.isNull( "recorderId cannot be null", recordingId );

        logger.trace( "Get status request receieved for videoRecorderId [" + recordingId + "]" );

        VideoRecorderTask videoRecorderTask = get( recordingId );

        if ( null == videoRecorderTask )
        {
            throw new RecorderNotFoundException( "No active recorder found for videoRecorderId [" + recordingId + "]."
                    + "The record operation may already completed." );
        }

        return videoRecorderTask.getRecordingStatus();
    }

    // FIXME
    @Override
    public void handleError( Recording recording, String errorMessage )
    {
        logger.info( "[HANDLE-ERROR][" + recording + "][" + errorMessage + "]" );

        throw new UnsupportedOperationException( "handleError is not supported yet." );
    }

    public static List< Integer > getActiveTelnetPortList()
    {
        return activeTelnetPortList;
    }

    public static List< Integer > getRecentlyUsedTelnetPortList()
    {
        return recentlyUsedTelnetPortList;
    }
}
