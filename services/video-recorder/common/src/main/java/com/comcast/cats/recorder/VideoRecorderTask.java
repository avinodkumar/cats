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

import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * Thread responsible for managing recording using VLC.
 * 
 * @author ssugun00c
 * 
 */
public interface VideoRecorderTask extends Runnable
{
    long DEFAULT_SLEEP_DURATION = 2 * 1000;

    /**
     * Start current recording
     */
    void startRecording() throws VideoRecorderException;

    /**
     * Stop current recording
     */
    void stopRecording() throws VideoRecorderException, VideoRecorderConnectionException;

    /**
     * get current recording status.
     * 
     * @return
     */
    RecordingStatus getRecordingStatus();

    int getTelnetPort();

    void setTelnetPort( int telnetPort );

}
