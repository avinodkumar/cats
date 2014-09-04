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
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.RecorderInstantiationException;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.service.VideoRecorderService;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.exception.ApplicationExceptionHandler;
import com.comcast.cats.service.exception.IllegalArgumentExceptionHandler;
import com.comcast.cats.service.util.AssertUtil;

/**
 * A RESTful implementation of {@link VideoRecorderService}. Also act as an
 * interface between EJB layer and the core recording classes. This layer is
 * important as EJB cannot create/manage its own threads.
 * 
 * @author SSugun00c
 * @see IllegalArgumentExceptionHandler
 * @see ApplicationExceptionHandler
 */
@Path( VideoRecorderServiceConstants.REST_REQUEST_INTERNAL_BASE_PATH )
public class RestVideoRecordingHandler
{
    private static final Logger          LOGGER = LoggerFactory.getLogger( RestVideoRecordingHandler.class );

    private static VideoRecordingHandler videoRecordingHandler;

    // This will be called once in life time
    static
    {
        LOGGER.info( "----------------------------------------------" );
        LOGGER.info( "INITIALIZING VIDEO RECORDER REST INTERFACE" );

        if ( ( null == System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST ) )
                || ( null == System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD ) )
                || ( null == System
                        .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH ) || ( null == System
                        .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH ) ) ) )
        {
            throw new RecorderInstantiationException(
                    "Cannot initilize CATS video recorder.One of the required system property is missing !!!" );
        }

        LOGGER.info( "[VLC TELNET HOST]["
                + System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST ) + "]" );
        LOGGER.info( "[VLC TELNET PASSWORD]["
                + System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD ) + "]" );
        LOGGER.info( "[PVR HOME]["
                + System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH )
                + "]" );
        LOGGER.info( "[PVR HTTP BASE URL]["
                + System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH )
                + "]" );
        videoRecordingHandler = new DefaultVideoRecordingHandler();
        LOGGER.info( "VIDEO RECORDER REST INTERFACE INITIALIZED" );
        LOGGER.info( "-----------------------------------------------" );
    }

    public RestVideoRecordingHandler()
    {
        // This will be invoked in every request
    }

    @POST
    @Path( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT )
    @Consumes( VideoRecorderServiceConstants.APPLICATION_XML )
    public VideoRecorderResponse submitRecording( Recording recording ) throws VideoRecorderException
    {
        LOGGER.info( "[REST][START][" + recording + "]" );
        AssertUtil.isNull( "recorderId cannot be null", recording.getId() );
        AssertUtil.isNullOrEmpty( "mrl cannot be null or empty", recording.getMrl() );
        AssertUtil.isNullOrEmpty( "recording.getMediaInfoEntityList() cannot be null or empty",
                recording.getMediaInfoEntityList() );
        AssertUtil.isNullOrEmpty( "filePath cannot be null or empty",
                recording.getMediaInfoEntityList().get( recording.getMediaInfoEntityList().size() - 1 ).getFilePath() );
        AssertUtil.isNullOrEmpty( "videoServerIp cannot be null or empty", recording.getVideoServerIp() );
        AssertUtil.isValidIp( "Video Server [" + recording.getVideoServerIp() + "] is not reachable",
                recording.getVideoServerIp() );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        try
        {
            videoRecordingHandler.submitRecording( recording );
            videoRecorderResponse.setMessage( "Start record request submitted for [" + recording.getStbMacAddress()
                    + "][" + recording.getName() + "][" + recording.getVideoServerIp() + "]["
                    + recording.getVideoServerPort() + "]. videoRecorderId is [" + recording.getId() + "]" );
            videoRecorderResponse.setRecording( recording );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
        }

        return videoRecorderResponse;
    }

    @POST
    @Path( VideoRecorderServiceConstants.REST_REQUEST_STOP )
    public VideoRecorderResponse stopRecording(
            @QueryParam( VideoRecorderServiceConstants.REST_QUERY_PARAM_RECORDING_ID )
            Integer recordingId ) throws VideoRecorderException, VideoRecorderConnectionException
    {
        LOGGER.info( "[REST][STOP][" + recordingId + "]" );
        AssertUtil.isNull( "recorderId cannot be null", recordingId );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        videoRecordingHandler.stopRecording( recordingId );
        videoRecorderResponse.setMessage( "Stop request for recordingId [" + recordingId
                + "] has been submitted successfully at " + new Date() );

        return videoRecorderResponse;
    }

    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_STATUS )
    public VideoRecorderResponse getRecordingStatus(
            @QueryParam( VideoRecorderServiceConstants.REST_QUERY_PARAM_RECORDING_ID )
            Integer recordingId ) throws VideoRecorderException
    {
        LOGGER.trace( "[REST][STATUS][" + recordingId + "]" );
        AssertUtil.isNull( "recorderId cannot be null", recordingId );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        // Just to hold status information.
        Recording recording = new Recording( recordingId );
        RecordingStatus recordingStatus = null;

        try
        {
            recordingStatus = videoRecordingHandler.getRecordingStatus( recordingId );
            recording.setRecordingStatus( recordingStatus );
        }
        catch ( RecorderNotFoundException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
            videoRecorderResponse.setException( e.getClass().getName() );
            recording.setRecordingStatus( new RecordingStatus( VideoRecorderState.STOPPED.name(), e.getMessage() ) );
        }
        catch ( VideoRecorderException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( e.getMessage() );
            videoRecorderResponse.setException( e.getClass().getName() );
            recording.setRecordingStatus( new RecordingStatus( VideoRecorderState.ERROR.name(), e.getMessage() ) );
        }

        videoRecorderResponse.setRecording( recording );

        return videoRecorderResponse;
    }

    @POST
    @Path( VideoRecorderServiceConstants.REST_REQUEST_HANDLE_ERROR )
    @Consumes( VideoRecorderServiceConstants.APPLICATION_XML )
    public void handleError( Recording recording, String errorMessage )
    {
        // FIXME: handleError( Recording recording, String errorMessage )
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_CURRENT_TASKS )
    public Map< Integer, VideoRecorderTask > getCurrentTasks()
    {
        return ( ( DefaultVideoRecordingHandler ) videoRecordingHandler );
    }

    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_ACTIVE_TELNET_PORTS )
    public static synchronized List< Integer > getActiveTelnetPortList()
    {
        return DefaultVideoRecordingHandler.getActiveTelnetPortList();
    }

    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_RECENTLY_USED_TELNET_PORTS )
    public static synchronized List< Integer > getRecentlyUsedTelnetPortList()
    {
        return DefaultVideoRecordingHandler.getRecentlyUsedTelnetPortList();
    }
}
