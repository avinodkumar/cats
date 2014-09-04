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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.HttpClientUtil;

@Named
@Singleton
public class SettopRecordingServiceImpl implements SettopRecordingService
{

    private static Logger logger = LoggerFactory.getLogger( SettopRecordingServiceImpl.class );

    @Override
    public VideoRecorderResponse getRecordingDetails( Slot slot, String macID )
    {
        VideoRecorderResponse retVal = null;

        if ( slot != null && macID != null )
        {
            retVal = getStatus( slot.getVideoHost(), slot.getVideoPort(), macID );
        }
        logger.info( "getRecordingDetails: " + slot + " : is retVal null ?" + ( retVal == null ) );
        return retVal;
    }

    @Override
    public List< Recording > getRecordingHistory( String macID )
    {
        List< Recording > history = null;
        if ( isValidHostAddress() )
        {
            if ( macID != null )
            {
                VideoRecorderResponse response = ( VideoRecorderResponse ) HttpClientUtil
                        .getForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_HISTORY_BY_MAC ),
                                getParamMap( macID ) );
                if ( response != null && response.getResult() != WebServiceReturnEnum.FAILURE )
                {
                    history = response.getRecordingList();
                }
            }
        }
        logger.info( "getRecordingHistory: " + macID + " : retVal null? " + ( history == null ) );
        return history;
    }

    @Override
    public VideoRecorderResponse startRecording( Slot slot, String macID, String recordingAlias )
    {
        return execute( slot, macID, recordingAlias, VideoRecorderServiceConstants.REST_REQUEST_SUBMIT );
    }

    @Override
    public VideoRecorderResponse stopRecording( Slot slot, String macID )
    {
        return execute( slot, macID, null, VideoRecorderServiceConstants.REST_REQUEST_STOP );
    }

    private VideoRecorderResponse execute( Slot slot, String macID, String recordingAlias, String requestURI )
    {
        VideoRecorderResponse response = null;
        if ( isValidHostAddress() )
        {
            if ( slot != null && slot.getVideoHost() != null && slot.getVideoPort() != null && macID != null
                    && requestURI != null && !requestURI.isEmpty() )
            {
                response = ( VideoRecorderResponse ) HttpClientUtil.postForObject( getRequestUri( requestURI ),
                        getParamMap( macID, slot.getVideoHost(), slot.getVideoPort(), recordingAlias ) );
                logger.debug( "webServiceReturn " + response );
            }
        }
        logger.trace( "execute: Slot " + slot + " requestURI " + requestURI + " response " + response );
        return response;
    }

    private VideoRecorderResponse getStatus( String videoHost, Integer videoPort, String macID )
    {
        VideoRecorderResponse response = null;
        if ( isValidHostAddress() )
        {
            response = ( VideoRecorderResponse ) HttpClientUtil.getForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                    getParamMap( macID, videoHost, videoPort, null ) );
        }
        return response;
    }

    private Map< String, String > getParamMap( String macID, String videoServerIp, Integer port, String aliasName )
    {
        Map< String, String > paramMap = getParamMap( macID, videoServerIp, port );
        if ( aliasName != null && !aliasName.isEmpty() )
        {
            paramMap.put( "alias", String.valueOf( aliasName ) );
        }
        return paramMap;
    }

    private Map< String, String > getParamMap( String macId, String videoServerIp, Integer port )
    {
        Map< String, String > paramMap = getParamMap( macId );
        paramMap.put( "videoServerIp", videoServerIp );
        paramMap.put( "port", String.valueOf( port ) );
        return paramMap;
    }

    private Map< String, String > getParamMap( String macId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "macId", macId );
        return paramMap;
    }

    private String getRequestUri( String restRequest )
    {
        String requestUri = "http://" + AuthController.getHostAddress()
                + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_PATH + restRequest;
        logger.info( "CATS webui " + requestUri );
        return requestUri;
    }

    private boolean isValidHostAddress()
    {
        boolean retVal = false;
        if ( AuthController.getHostAddress() != null )
        {
            retVal = true;
        }else{
            logger.debug( "Host address is null, request cannot be completed" );
        }

        return retVal;
    }

    @Override
    public VideoRecorderResponse deleteFile( int mediaId )
    {
        VideoRecorderResponse response = null;
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "mediaMetaDataId", String.valueOf( mediaId ) );
        if ( isValidHostAddress() )
        {
            response = ( VideoRecorderResponse ) HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_MEDIA_METADATA_BY_ID ), paramMap );
        }
        logger.info( "deleting Media " + mediaId + " response " + response );
        return response;
    }

    @Override
    public VideoRecorderResponse deleteRecording( int recordingId )
    {
        VideoRecorderResponse response = null;
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "recordingId", String.valueOf( recordingId ) );
        if ( isValidHostAddress() )
        {
            response = ( VideoRecorderResponse ) HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_RECORDING_BY_ID ), paramMap );
        }
        logger.info( "deleting Recording " + recordingId + " response " + response );
        return response;
    }

    @Override
    public VideoRecorderResponse deleteAllRecordingsForSettop( String macAddress )
    {
        VideoRecorderResponse response = null;
        if ( isValidHostAddress() )
        {
            if ( macAddress != null && !macAddress.isEmpty() )
            {
                response = ( VideoRecorderResponse ) HttpClientUtil.deleteForObject(
                        getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_MAC_ID ),
                        getParamMap( macAddress ) );
            }
        }
        logger.info( "deleting All Recordings " + macAddress + " response " + response );
        return response;
    }
}
