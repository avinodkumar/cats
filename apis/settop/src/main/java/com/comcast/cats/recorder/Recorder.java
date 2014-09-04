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

import java.util.Date;

// is this is a provider? Should it be named as a provider.
public interface Recorder
{

    /**
     * Delay before next sample is retrieved. This captures video image at these
     * points. The captured video images are stacked in the timeline according
     * to the frame rate set above.
     * 
     * Default Delay Value : 2000 milliseconds.
     * 
     * @param seconds
     *            - Number of milliseconds to delay.
     */
    void setSampleInterval( int seconds );

    /**
     * File or URL to be passed down to underlying implementation for output
     * location.
     * 
     * @param url
     */
    void setOutputFileLocation( String url );

    /**
     * Get File path set
     * 
     * @return String
     */
    String getOutputFileLocation();

    /**
     * start video record.
     */
    void record();

    /**
     * Record this video for the given time duration. Overrides any end time
     * settings. Must be greater than zero.
     * 
     * @param durationSec
     *            - Recording duration in seconds.
     */
    void record( int durationSec );

    /**
     * Pause current recording.
     */
    void pauseRecording();

    /**
     * Resume recording.
     */
    void resumeRecording();

    /**
     * Stop current recording
     */
    void stopRecording();

    /**
     * Return status.
     * 
     * @return true: if currently recording.
     */
    boolean isRecording();

    /**
     * Return true if recording is started but paused.
     * 
     * @return boolean
     */
    boolean isPaused();

    /**
     * Set record start timer. If start Time has elapsed, the recorder will
     * start immediately.
     * 
     * @param startTime
     */
    void setRecordingStartTime( Date startTime );

    /**
     * Set record finish timer. End Time must be greater than start time
     * 
     * @param endTime
     */
    void setRecordingEndTime( Date endTime );

    /**
     * Get current set start time. Will return null if no timer is set.
     * 
     * @return Date
     */
    Date getRecordingStartTime();

    /**
     * Get current set end time. Will return null if no timer is set.
     * 
     * @return Date
     */
    Date getRecordingEndTime();

    /**
     * Set the compression ratio for this recording. i.e in how much time should
     * the entire video be compressed to. If a recording of 60 minutes needs to
     * be compressed in 30minutes, then the compression ratio is 0.5. This can
     * be calculated as : original video length/required video length.
     * 
     * This will work only if a endTime or duration is specified. If no end
     * time, duration is specified then the compression ratio will be 1.
     * 
     * @param ratio
     */
    void setCompressionRatio( float ratio );

    /**
     * Get the compression ratio set on the system.
     * 
     * @return compression ratio
     */
    float getCompressionRatio();

    /**
     * Get the sample interval set on the system.
     * 
     * @return sample interval
     */
    int getSampleInterval();

}
