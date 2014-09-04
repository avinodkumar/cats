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

import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * A handler which responsible to handle video recording.
 * 
 * @author ssugun00c
 * 
 */
public interface VideoRecordingHandler
{
    /**
     * Submit a new record request. The recorder will use default recording
     * options.
     * 
     * @param videoServerIp
     * @throws VideoRecorderException
     */
    void submitRecording( Recording recording ) throws VideoRecorderException;

    /**
     * 
     * @param recordingId
     * @throws VideoRecorderException
     * @throws VideoRecorderConnectionException
     */
    void stopRecording( Integer recordingId ) throws VideoRecorderException, VideoRecorderConnectionException;

    /**
     * Get the current status of the recording.
     * 
     * @param videoServerIp
     * @return
     * @throws VideoRecorderException
     */
    RecordingStatus getRecordingStatus( Integer recordingId ) throws VideoRecorderException;
}
