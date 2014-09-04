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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.service.VideoRecorderService;
import com.comcast.cats.service.util.HttpClientUtil;

/**
 * Integration tests to verify REST API of {@link VideoRecorderService}.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderRestClientIT
{
    private static final long   SLEPP_DURATION = 30 * 1000;
    private static final String REST_BASE_URL  = "http://localhost:8080/video-recorder-service/rest"
                                                       + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_BASE_PATH;
    private Logger              logger         = LoggerFactory.getLogger( getClass() );

    public VideoRecorderRestClientIT()
    {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testRestInterface() throws InterruptedException
    {
        String macId = "E4:48:C7:A8:2A:F2";
        Integer port = 1;
        String videoServerIp = "192.168.160.202";

        printHistoryByMac( macId );

        start( macId, videoServerIp, port );
        Thread.sleep( SLEPP_DURATION );

        printStatus( macId, videoServerIp, port );

        stop( macId, videoServerIp, port );
        Thread.sleep( SLEPP_DURATION );

        printStatus( macId, videoServerIp, port );

        printHistoryByMac( macId );

    }

    private void start( String macId, String videoServerIp, Integer port )
    {
        logger.info( "START" );

        String startUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_SUBMIT;

        VideoRecorderResponse result = ( VideoRecorderResponse ) HttpClientUtil.postForObject( startUri,
                getParamMap( macId, videoServerIp, port ) );
        logger.info( "RESULT: " + result );
    }

    private void stop( String macId, String videoServerIp, Integer port )
    {
        logger.info( "STOP" );
        String stopUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_STOP;

        VideoRecorderResponse result = ( VideoRecorderResponse ) HttpClientUtil.postForObject( stopUri,
                getParamMap( macId, videoServerIp, port ) );
        logger.info( "RESULT: " + result );
    }

    private void printStatus( String macId, String videoServerIp, Integer port )
    {
        logger.info( "STATUS" );
        String statusUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_STATUS;

        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.getForObject( statusUri,
                getParamMap( macId, videoServerIp, port ) );

        logger.info( "RESULT: " + videoRecorderResponse );

        logger.info( "Status: " + videoRecorderResponse.getRecording().getRecordingStatus() );
    }

    private void printHistoryByMac( String macId )
    {
        logger.info( "HISTORY" );

        String historyUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_HISTORY_BY_MAC;

        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.getForObject(
                historyUri, getParamMap( macId ) );

        logger.info( "RESULT: " + videoRecorderResponse );

        for ( Recording recording : videoRecorderResponse.getRecordingList() )
        {
            logger.info( recording.toString() );
        }
    }

    private Map< String, String > getParamMap( String macId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "macId", macId );
        return paramMap;
    }

    private Map< String, String > getParamMap( String macId, String videoServerIp, Integer port )
    {
        Map< String, String > paramMap = getParamMap( macId );
        paramMap.put( "videoServerIp", videoServerIp );
        paramMap.put( "port", port.toString() );
        return paramMap;
    }
}
