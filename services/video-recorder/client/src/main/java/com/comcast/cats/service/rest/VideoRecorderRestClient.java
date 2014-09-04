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
package com.comcast.cats.service.rest;

import java.util.List;

import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.service.VideoRecorderService;

/**
 * Rest client for {@link VideoRecorderService}.
 * 
 * @author ssugun00c
 * 
 */
public class VideoRecorderRestClient
{

    public Recording start( String macId, String videoServerIp, Integer port, Integer duration )
            throws VideoRecorderException
    {
        return null;
    }

    public void stop( String macId, String videoServerIp, Integer port ) throws VideoRecorderException
    {
    }

    public void stopById( Integer recordingId ) throws VideoRecorderException
    {
    }

    public Recording getStatus( String macId, String videoServerIp, Integer port ) throws VideoRecorderException
    {
        return null;
    }

    public Recording getStatusById( Integer recordingId ) throws VideoRecorderException
    {
        return null;
    }

    public List< Recording > getActiveRecordingList()
    {
        return null;
    }

    public List< Recording > getRecordingHistoryByMac( String macId )
    {
        return null;
    }

}
