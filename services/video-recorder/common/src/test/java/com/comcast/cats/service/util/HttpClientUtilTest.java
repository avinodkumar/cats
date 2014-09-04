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
package com.comcast.cats.service.util;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.VideoRecorderTask;

/**
 * Unit tests for {@link HttpClientUtil},
 * 
 * @author ssugun00c
 * 
 */
public class HttpClientUtilTest
{
    private static final String BASE_URL = "http://localhost:8080/video-recorder-service/rest";
    private Logger              logger   = LoggerFactory.getLogger( getClass() );

    @SuppressWarnings( "unchecked" )
    @Test
    public void testVlcTasks() throws Exception
    {
        String uri = BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_INTERNAL_BASE_PATH
                + VideoRecorderServiceConstants.REST_REQUEST_CURRENT_TASKS;

        logger.info( "uri = " + uri );

        Map< Integer, VideoRecorderTask > vlcTasks = ( Map< Integer, VideoRecorderTask > ) HttpClientUtil.getForObject(
                uri, null );

        for ( int key : vlcTasks.keySet() )
        {
            logger.info( key + " = " + vlcTasks.get( key ) );
        }

        for ( Map.Entry< Integer, VideoRecorderTask > entry : vlcTasks.entrySet() )
        {
            logger.info( entry.getKey() + "/" + entry.getValue() );
        }

    }
}
