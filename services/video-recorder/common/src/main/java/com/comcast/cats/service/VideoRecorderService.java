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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.comcast.cats.info.DiskSpaceUsage;
import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;

/**
 * CATS video recorder service. All the operations are exposed as both REST and
 * SOAP.
 * 
 * @author SSugun00c
 * 
 */
@Path( VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_BASE_PATH )
@WebService( name = VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_NAME, targetNamespace = VideoRecorderServiceConstants.NAMESPACE )
public interface VideoRecorderService
{
    /**
     * Start a new {@link Recording}.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : POST
     *   Request : http://{host}:{port}/video-recorder-service/rest/public/recorder/submit?macId={macId}&videoServerIp={videoServerIp}&port={port}&duration={duration}&alias={alias}
     * </pre>
     * 
     * @param macId
     *            Mac address of Settop
     * @param videoServerIp
     *            IP address of video server. E.g Axis
     * @param port
     *            Port or camera of video server
     * @param duration
     *            Recording duration in minutes
     * @param alias
     *            Alias name for recording
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @POST
    @Path( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT )
    @WebMethod
    VideoRecorderResponse start( @QueryParam( "macId" )
    @WebParam( name = "macId" )
    String macId, @QueryParam( "videoServerIp" )
    @WebParam( name = "videoServerIp" )
    String videoServerIp, @QueryParam( "port" )
    @WebParam( name = "port" )
    Integer port, @QueryParam( "duration" )
    @WebParam( name = "duration" )
    Integer duration, @QueryParam( "alias" )
    @WebParam( name = "alias" )
    String alias );

    /**
     * Stop a {@link Recording} using macId.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : POST
     *   Request : http://{host}:{port}/video-recorder-service/rest/public/recorder/stop?macId={macId}
     * </pre>
     * 
     * @param macId
     *            Mac address of Settop
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @POST
    @Path( VideoRecorderServiceConstants.REST_REQUEST_STOP )
    @WebMethod
    VideoRecorderResponse stop( @QueryParam( "macId" )
    @WebParam( name = "macId" )
    String macId );

    /**
     * Stop a {@link Recording} using {@link Recording} Id.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : POST
     *   Request : http://{host}:{port}/video-recorder-service/rest/stop/id?recordingId={recordingId}
     * </pre>
     * 
     * @param recordingId
     *            Id of {@link Recording}
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @POST
    @Path( VideoRecorderServiceConstants.REST_REQUEST_STOP_BY_ID )
    @WebMethod
    VideoRecorderResponse stopById( @QueryParam( "recordingId" )
    @WebParam( name = "recordingId" )
    Integer recordingId );

    /**
     * Get status using macId.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : GET
     *   Request : http://{host}:{port}/video-recorder-service/rest/status?macId={macId}
     * </pre>
     * 
     * @param macId
     *            Mac address of Settop
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_STATUS )
    @WebMethod
    VideoRecorderResponse getStatus( @QueryParam( "macId" )
    @WebParam( name = "macId" )
    String macId );

    /**
     * Get status using {@link Recording} Id.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : GET
     *   Request : http://{host}:{port}/video-recorder-service/rest/status/id?recordingId={recordingId}
     * </pre>
     * 
     * @param recordingId
     *            Id of {@link Recording}
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_STATUS_BY_ID )
    @WebMethod
    VideoRecorderResponse getStatusById( @QueryParam( "recordingId" )
    @WebParam( name = "recordingId" )
    Integer recordingId );

    /**
     * Get a list of all active {@link Recording}.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : GET
     *   Request : http://{host}:{port}/video-recorder-service/rest/active
     * </pre>
     * 
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_ACTIVE )
    @WebMethod
    VideoRecorderResponse getActiveRecordingList();

    /**
     * Get all {@link Recording} against a macId.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : GET
     *   Request : http://{host}:{port}/video-recorder-service/rest/history/mac?macId={macId}
     * </pre>
     * 
     * @param macId
     *            Mac address of Settop
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_HISTORY_BY_MAC )
    @WebMethod
    VideoRecorderResponse getRecordingHistoryByMac( @QueryParam( "macId" )
    @WebParam( name = "macId" )
    String macId );

    /**
     * Get disk usage information.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : GET
     *   Request : http://{host}:{port}/video-recorder-service/rest/diskspace
     * </pre>
     * 
     * @return {@link DiskSpaceUsage}
     */
    @GET
    @Path( VideoRecorderServiceConstants.REST_REQUEST_GET_DISKSPACE_USAGE )
    @WebMethod
    DiskSpaceUsage getDiskSpaceUsage();

    /**
     * Delete a {@link Recording} and associated {@link MediaMetaData} and
     * {@link RecordingStatus} by {@link Recording} Id.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : DELETE
     *   Request : http://{host}:{port}/video-recorder-service/rest/public/recorder/delete/recording/id?recordingId={recordingId}
     * </pre>
     * 
     * @param recordingId
     *            Id of {@link Recording}
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @DELETE
    @Path( VideoRecorderServiceConstants.REST_REQUEST_DELETE_RECORDING_BY_ID )
    @WebMethod
    VideoRecorderResponse deleteRecordingById( @QueryParam( "recordingId" )
    @WebParam( name = "recordingId" )
    Integer recordingId );

    /**
     * Delete {@link MediaMetaData} by {@link MediaMetaData} id. This will not
     * remove parent {@link Recording}.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : DELETE
     *   Request : http://{host}:{port}/video-recorder-service/rest/public/recorder/delete/media/id?mediaMetaDataId={mediaMetaDataId}
     * </pre>
     * 
     * @param mediaMetaDataId
     *            Id of {@link MediaMetaData}
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @DELETE
    @Path( VideoRecorderServiceConstants.REST_REQUEST_DELETE_MEDIA_METADATA_BY_ID )
    @WebMethod
    VideoRecorderResponse deleteMediaMetaDataById( @QueryParam( "mediaMetaDataId" )
    @WebParam( name = "mediaMetaDataId" )
    Integer mediaMetaDataId );

    /**
     * Delete all {@link Recording} and associated {@link MediaMetaData} and
     * {@link RecordingStatus} of a Settop using macId.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : DELETE
     *   Request : http://{host}:{port}/video-recorder-service/rest/public/recorder/delete/recording/mac?macId={macId}
     * </pre>
     * 
     * @param macId
     *            Mac address of Settop
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @DELETE
    @Path( VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_MAC_ID )
    @WebMethod
    VideoRecorderResponse deleteAllRecordingByMacId( @QueryParam( "macId" )
    @WebParam( name = "macId" )
    String macId );

    /**
     * Delete all {@link Recording} and associated {@link MediaMetaData} and
     * {@link RecordingStatus} of a Settop using macId and alias name.
     * 
     * <pre>
     *   <h2>REST API</h2>
     *   Method  : DELETE
     *   Request : http://{host}:{port}/video-recorder-service/rest/public/recorder/delete/recording/alias?alias={alias}&macId={macId}
     * </pre>
     * 
     * @param alias
     *            Alias name for recording
     * @param macId
     *            Mac address of Settop
     * @return {@link VideoRecorderResponse} - A general web service response
     *         object
     */
    @DELETE
    @Path( VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_ALIAS )
    @WebMethod
    VideoRecorderResponse deleteRecordingByAlias( @QueryParam( "alias" )
    @WebParam( name = "alias" )
    String alias, @QueryParam( "macId" )
    @WebParam( name = "macId" )
    String macId );
}
