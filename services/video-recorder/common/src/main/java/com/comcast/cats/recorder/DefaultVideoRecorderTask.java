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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.info.VideoRecordingOptions;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * Default implementation of {@link VideoRecorderTask}.
 * 
 * @author ssugun00c
 * 
 */
public class DefaultVideoRecorderTask extends AbstractVideoRecorderTask
{
    private Runtime runtime = Runtime.getRuntime();

    private int     telnetPort;
    private String  command;

    public DefaultVideoRecorderTask()
    {
        // TODO Auto-generated constructor stub
    }

    public DefaultVideoRecorderTask( Recording recording, String filePath, VideoRecordingOptions videoRecordingOptions,
            VideoRecordingErrorHandler errorHandler )
    {
        this.recording = recording;
        this.filePath = filePath;
        //this.errorHandler = errorHandler;
        this.videoRecordingOptions = videoRecordingOptions;
        this.recordingStatus = recording.getRecordingStatus();
        this.telnetHandler = new TelnetHandler();
    }

    @Override
    public void run()
    {
        setRecording( true );

        try
        {
            runtime.exec( command );

            logger.info( "Record started for videoRecordeId [" + recording.getId() + "]" );

            while ( isRecording() )
            {
                logger.trace( "Video recorder monitor thread is checking status for [" + recording.getId() + "] at "
                        + new SimpleDateFormat( "MM-dd-yyyy hh:mm:ss" ).format( new Date() ) );

                try
                {
                    Thread.sleep( DEFAULT_SLEEP_DURATION );
                }
                catch ( InterruptedException e )
                {
                    logger.error( "Video recorder monitor thread interrupted " + e.getMessage() );
                }
            }
        }
        catch ( IOException iOException )
        {
            logger.error( "iOException happend while recodring - " + iOException.toString() );
        }

        logger.info( "Compled video record  for videoRecordeId [" + recording.getId() + "]" );
    }

    @Override
    public void stopRecording() throws VideoRecorderException, VideoRecorderConnectionException
    {
        synchronized ( this )
        {
            boolean isStopped = telnetHandler.shutDownVlc( getTelnetPort() );

            if ( isStopped )
            {
                setRecorderState( VideoRecorderState.STOPPED );
            }
            else
            {
                setRecorderState( VideoRecorderState.ERROR );
                throw new VideoRecorderException( "Stop recording failed for recordingId [" + recording.getId()
                        + "]. Check telnet logs." );
            }

            setRecording( false );  
        }
    }

    @Override
    public int getTelnetPort()
    {
        return telnetPort;
    }

    @Override
    public void setTelnetPort( int telnetPort )
    {
        this.telnetPort = telnetPort;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand( String command )
    {
        this.command = command;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [filePath=" + getFilePath() + ", isRecording=" + isRecording() + ", status="
                + getRecordingStatus() + ", recordingId=" + recording.getId() + ", mrl=" + recording.getMrl()
                + ", requestedDuration=" + recording.getRequestedDuration() + ", telnetPort=" + getTelnetPort() + "]";
    }
}
