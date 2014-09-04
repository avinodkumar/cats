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
import com.comcast.cats.service.util.HttpClientUtil;

/**
 * Integration tests to verify delete REST API of video recorder.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderRestClientDeleteIT
{
    private static final String REST_BASE_URL = "http://localhost:8080/video-recorder-service/rest"
                                                      + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_BASE_PATH;
    private Logger              logger        = LoggerFactory.getLogger( getClass() );

    public VideoRecorderRestClientDeleteIT()
    {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testRestInterface() throws InterruptedException
    {
        String macId = "E4:48:C7:A8:2A:F2";
        String alias = "test";
        int recordingId = 35;
        int mediaMetaDataId = 22;

        String deleteByRecordingId = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_DELETE_RECORDING_BY_ID;
        VideoRecorderResponse result = ( VideoRecorderResponse ) HttpClientUtil.deleteForObject( deleteByRecordingId,
                getParamMap( recordingId ) );
        logger.info( "RESULT: " + result );

        String deleteAllByMacId = REST_BASE_URL
                + VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_MAC_ID;
        result = ( VideoRecorderResponse ) HttpClientUtil.deleteForObject( deleteAllByMacId, getParamMap( macId ) );
        logger.info( "RESULT: " + result );

        String deleteAllByAlias = REST_BASE_URL
                + VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_ALIAS;
        result = ( VideoRecorderResponse ) HttpClientUtil
                .deleteForObject( deleteAllByAlias, getParamMap( macId, alias ) );
        logger.info( "RESULT: " + result );

        String deleteByMediaMetadataId = REST_BASE_URL
                + VideoRecorderServiceConstants.REST_REQUEST_DELETE_MEDIA_METADATA_BY_ID;
        result = ( VideoRecorderResponse ) HttpClientUtil.deleteForObject( deleteByMediaMetadataId,
                getParamMapByMediaMetadataId( mediaMetaDataId ) );
        logger.info( "RESULT: " + result );
    }

    private Map< String, String > getParamMap( int recordingId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "recordingId", String.valueOf( recordingId ) );
        return paramMap;
    }

    private Map< String, String > getParamMapByMediaMetadataId( int mediaMetaDataId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "mediaMetaDataId", String.valueOf( mediaMetaDataId ) );
        return paramMap;
    }

    private Map< String, String > getParamMap( String macId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "macId", macId );
        return paramMap;
    }

    private Map< String, String > getParamMap( String macId, String alias )
    {
        Map< String, String > paramMap = getParamMap( macId );
        paramMap.put( "alias", alias );
        return paramMap;
    }
}
