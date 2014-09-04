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
package com.comcast.cats.config.ui.recording;

import java.util.List;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.recorder.domain.Recording;

public interface SettopRecordingService
{
    VideoRecorderResponse stopRecording( Slot slot, String macId );

    VideoRecorderResponse startRecording( Slot slot, String macId, String recordingAlias );

    VideoRecorderResponse getRecordingDetails( Slot slot, String macId );

    List< Recording > getRecordingHistory( String macId );
    
    VideoRecorderResponse deleteFile(int mediaId);
    
    VideoRecorderResponse deleteRecording(int recordingId);
    
    VideoRecorderResponse deleteAllRecordingsForSettop(String macAddress);
    
}
