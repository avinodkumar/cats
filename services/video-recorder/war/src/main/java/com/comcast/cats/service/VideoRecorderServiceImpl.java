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
package com.comcast.cats.service;

import java.io.File;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.DiskSpaceUsage;
import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.RestVideoRecordingHandler;
import com.comcast.cats.recorder.domain.service.MediaMetaDataEntityService;
import com.comcast.cats.recorder.domain.service.RecordingEntityService;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.service.util.AssertUtil;
import com.comcast.cats.service.util.VideoRecorderUtil;

/**
 * 
 * {@link VideoRecorderService} Service EJB 3.x Implementation. This class is
 * just a wrapper to expose the {@link VideoRecorderService} as web service.
 * This will internally call another Servlet {@link RestVideoRecordingHandler}
 * to do the job. Since EJB specification restricts the usage of custom threads
 * and UDP protocol, Its necessary to use a Servlet as a helper.
 * 
 * @author SSugun00c
 * 
 */
@Remote( VideoRecorderService.class )
@WebService( name = VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_NAME, portName = VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_PORT_NAME, targetNamespace = VideoRecorderServiceConstants.NAMESPACE, endpointInterface = VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_ENDPOINT_INTERFACE )
@Stateless( mappedName = VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_MAPPED_NAME )
// To make EJB injection within JAX-RS resource work.
@LocalBean
public class VideoRecorderServiceImpl implements VideoRecorderService
{
    private final Logger               LOGGER = LoggerFactory.getLogger( getClass() );

    @EJB
    private RecordingEntityService     recordingEntityService;

    @EJB
    private MediaMetaDataEntityService mediaMetaDataEntityService;

    @Override
    public VideoRecorderResponse start( String macId, String videoServerIp, Integer port, Integer duration, String alias )
    {
        LOGGER.info( "[EJB3][START][ALIAS][" + macId + "][" + videoServerIp + "][" + port + "][" + alias + "] for ["
                + duration + "] minutes" );

        AssertUtil.isNullOrEmpty( "macId cannot be null or empty", macId );
        AssertUtil.isValidMacId( "Invalid macId [" + macId + "]", macId );
        AssertUtil.isNullOrEmpty( "videoServerIp cannot be null or empty", videoServerIp );
        AssertUtil.isValidIp( "Video Server [" + videoServerIp + "] is not reachable", videoServerIp );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        if ( null != alias )
        {
            alias = VideoRecorderUtil.validateAndFixAlias( alias );
        }

        try
        {
            videoRecorderResponse = recordingEntityService
                    .submitRecording( macId, videoServerIp, port, duration, alias );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }
        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse stop( String macId )
    {
        LOGGER.info( "[EJB3][STOP][" + macId + "]" );

        AssertUtil.isNullOrEmpty( "macId cannot be null or empty", macId );
        AssertUtil.isValidMacId( "Invalid macId [" + macId + "]", macId );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();
        try
        {
            videoRecorderResponse = recordingEntityService.stopRecordingByMacId( macId );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse stopById( Integer recordingId )
    {
        LOGGER.info( "[EJB3][STOP][" + recordingId + "]" );

        AssertUtil.isNull( "recordingId cannot be null or empty", recordingId );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();
        try
        {
            videoRecorderResponse = recordingEntityService.stopRecordingById( recordingId );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse getStatus( String macId )
    {
        LOGGER.trace( "[EJB3][STATUS-BY-MAC][" + macId + "]" );

        AssertUtil.isNullOrEmpty( "macId cannot be null or empty", macId );
        AssertUtil.isValidMacId( "Invalid macId [" + macId + "]", macId );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        try
        {
            videoRecorderResponse = recordingEntityService.getRecordingStatus( macId );
            LOGGER.trace( "[EJB3][STATUS-BY-MAC][" + macId + "][" + videoRecorderResponse + "]" );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
            LOGGER.info( "[EJB3][STATUS][" + macId + "][" + videoRecorderResponse.getResult() + "]" );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse getStatusById( Integer recordingId )
    {
        LOGGER.trace( "[EJB3][STATUS-BY-ID][" + recordingId + "]" );

        AssertUtil.isNull( "recordingId cannot be null", recordingId );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        try
        {
            videoRecorderResponse = recordingEntityService.getRecordingStatusById( recordingId );
            LOGGER.trace( "[EJB3][STATUS-BY-ID][" + recordingId + "][" + videoRecorderResponse + "]" );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
            LOGGER.info( "[EJB3][STATUS-BY-ID][" + recordingId + "][" + videoRecorderResponse.getResult() + "]" );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse getActiveRecordingList()
    {
        LOGGER.info( "[EJB3][GET-ACTIVE]" );
        return recordingEntityService.getActiveRecordingList();
    }

    @Override
    public VideoRecorderResponse getRecordingHistoryByMac( String macId )
    {
        LOGGER.info( "[EJB3][GET-RECORDING-HISTORY-BY-MAC]" );
        return recordingEntityService.getRecordingHistoryByMac( macId );
    }

    @Override
    public DiskSpaceUsage getDiskSpaceUsage()
    {
        return VideoRecorderUtil.getDiskSpaceUsage( new File( System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH ) ) );
    }

    @Override
    public VideoRecorderResponse deleteRecordingById( Integer recordingId )
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        try
        {
            recordingEntityService.deleteById( recordingId );
            videoRecorderResponse.setMessage( "Recording [" + recordingId + "] deleted." );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse deleteAllRecordingByMacId( String macId )
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        int count = recordingEntityService.deleteAllByMacId( macId );
        videoRecorderResponse.setMessage( count + " item(s) deleted." );

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse deleteRecordingByAlias( String alias, String macId )
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        try
        {
            int count = recordingEntityService.deleteByAlias( alias, macId );
            videoRecorderResponse.setMessage( count + " item(s) deleted." );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }
        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse deleteMediaMetaDataById( Integer mediaMetaDataId )
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        try
        {
            int count = mediaMetaDataEntityService.deleteById( mediaMetaDataId );
            videoRecorderResponse.setMessage( count + " item(s) deleted." );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }

        return videoRecorderResponse;
    }
}
