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
package com.comcast.cats.recorder.domain.service;

import java.util.Date;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * Entity service for {@link Recording}.
 * 
 * @author SSugun00c
 * 
 */
public interface RecordingEntityService
{
    VideoRecorderResponse submitRecording( String macId, String videoServerIp, Integer port, Integer duration,
            String alias ) throws VideoRecorderException;

    VideoRecorderResponse stopRecordingByMacId( String macId ) throws VideoRecorderException;

    VideoRecorderResponse stopRecordingById( Integer recordingId ) throws VideoRecorderException;

    VideoRecorderResponse getRecordingStatus( String macId ) throws VideoRecorderException;

    VideoRecorderResponse getRecordingStatusById( Integer recordingId ) throws VideoRecorderException;

    VideoRecorderResponse getActiveRecordingList();

    VideoRecorderResponse getRecordingHistory();

    VideoRecorderResponse getRecordingHistoryByMac( String macId );

    void submitScheduledRecording( Integer recordingId ) throws VideoRecorderException,
            VideoRecorderConnectionException;

    void deleteById( Integer recordingId ) throws RecorderNotFoundException;

    Integer deleteAllByMacId( String macId );

    Integer deleteByAlias( String alias, String macId ) throws RecorderNotFoundException;

    Integer deleteAllBefore( Date createdDate );

}
