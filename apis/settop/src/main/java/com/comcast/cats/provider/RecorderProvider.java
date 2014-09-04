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
package com.comcast.cats.provider;

import com.comcast.cats.provider.exceptions.VideoRecorderException;

/**
 * Provider interface for Recorder WebService. Do not confuse this to the client
 * side recorder {@link com.comcast.cats.recorder.Recorder} 
 * 
 * @author skurup00c
 */
public interface RecorderProvider extends BaseProvider
{

    /**
     * Start recording at the server. Mark the session with the name provided.
     * 
     * @return true if recording has been started successfully
     * @throws VideoRecorderException
     *             in case of any error.
     */
    public boolean startVideoRecording( String recordingAliasName ) throws VideoRecorderException;

    /**
     * Start recording at the server. The files be named using the start time
     * timestamp.
     * 
     * @return true if recording has been started successfully
     * @throws VideoRecorderException
     *             in case of any error.
     */
    public boolean startVideoRecording() throws VideoRecorderException;

    /**
     * Stop recording
     * 
     * @return true if recording has been stopped successfully
     * @throws VideoRecorderException
     *             in case of any error.
     */
    public boolean stopVideoRecording() throws VideoRecorderException;

    /**
     * Get the status of the latest recording.
     * 
     * @return status of the recording session
     * @throws VideoRecorderException
     *             in case of any error.
     */
    public String getRecordingInfo() throws VideoRecorderException;
}