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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.info.VideoRecordingOptions;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * Abstract implementation of {@link VideoRecorderTask}.
 * 
 * @author ssugun00c
 * 
 */
public abstract class AbstractVideoRecorderTask implements VideoRecorderTask
{
    protected Logger                     logger                = LoggerFactory.getLogger( getClass() );

    //protected VideoRecordingErrorHandler errorHandler          = null;
    protected VideoRecordingOptions      videoRecordingOptions = null;
    protected RecordingStatus            recordingStatus       = null;

    protected Recording                  recording;
    protected String                     filePath;

    protected TelnetHandler              telnetHandler;
    private boolean                      isRecording           = false;

    // ------ START Utility methods
    protected boolean isRecording()
    {
        return isRecording;
    }

    protected void setRecording( boolean isRecording )
    {
        this.isRecording = isRecording;
        setRecorderState( VideoRecorderState.RECORDING );
    }

    protected String getVlcOptions()
    {
        String recorderOptions = videoRecordingOptions.getVideoRecorderParams() + ":duplicate{dst=file{dst=" + filePath
                + "}}";

        logger.info( "Recorder Options for videoRecordeId [" + recording.getId() + "] is [" + recorderOptions + "]" );

        return recorderOptions;
    }

    protected void setRecorderState( VideoRecorderState videoRecorderState )
    {
        getRecordingStatus().setState( videoRecorderState.toString() );

        switch ( videoRecorderState )
        {
        case RECORDING:
            getRecordingStatus().setMessage( "Recording in progress." );
            break;
        case ERROR:
            getRecordingStatus().setMessage( "An error happend while recording." );
            break;
        case STOPPED:
            getRecordingStatus().setMessage( "Recording stopped." );
            break;
        default:
            break;
        }
    }

    @Override
    public RecordingStatus getRecordingStatus()
    {
        return recordingStatus;
    }

    public void setRecordingStatus( RecordingStatus recordingStatus )
    {
        this.recordingStatus = recordingStatus;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath( String filePath )
    {
        this.filePath = filePath;
    }

    @Override
    public void startRecording() throws VideoRecorderException
    {
        throw new UnsupportedOperationException(
                "DefaultVideoRecorderTask is a thread and should use run method to strat recording" );
    }

    public Recording getRecording()
    {
        return recording;
    }

    public void setRecording( Recording recording )
    {
        this.recording = recording;
    }
}