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
package com.comcast.cats.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.service.util.HttpClientUtil;

/**
 * Integration tests to verify delay to get response from video recorder.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderGetStatusRestIT
{
    private static final String REST_BASE_URL = "http://localhost/video-recorder-service/rest"
                                                      + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_BASE_PATH;
    private Logger              logger        = LoggerFactory.getLogger( getClass() );

    public VideoRecorderGetStatusRestIT()
    {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testRestInterface() throws InterruptedException
    {
        List< String > macList = new ArrayList< String >();
        macList.add( "E4:48:C7:A8:1A:56" );
        macList.add( "E4:48:C7:A8:4B:C8" );
        macList.add( "E4:48:C7:A8:2A:12" );
        macList.add( "E4:48:C7:A8:1B:12" );
        macList.add( "54:D4:6F:96:D9:BC" );
        macList.add( "E4:48:C7:A8:2E:C4" );
        macList.add( "E4:48:C7:A8:2A:F2" );

        logger.info( "******************** GET STATUS ******************************" );

        long startTimeInMilliSec = System.currentTimeMillis();

        for ( String mac : macList )
        {
            printStatus( mac );
        }

        long durationInMilliSecs = System.currentTimeMillis()-startTimeInMilliSec;

        logger.info( "******************** elapsed time in seconds ******************************" );
        logger.info( "[ELAPSED TIME][" + durationInMilliSecs + " Milli seconds] OR ["
                + ( TimeUnit.MILLISECONDS.toSeconds( durationInMilliSecs ) ) + " Seconds]" );

    }

    private void printStatus( String macId )
    {
        logger.info( "[STATUS][" + macId + "]" );
        String statusUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_STATUS;

        try
        {
            VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.getForObject(
                    statusUri, getParamMap( macId ) );

            logger.info( "RESULT: " + videoRecorderResponse );

            logger.info( "Status: " + videoRecorderResponse.getRecording().getRecordingStatus() );
        }
        catch ( Exception e )
        {
            logger.error( e.getMessage() );
        }
    }

    private Map< String, String > getParamMap( String macId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "macId", macId );
        return paramMap;
    }
}
