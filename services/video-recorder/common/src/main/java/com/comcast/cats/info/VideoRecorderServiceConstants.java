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
package com.comcast.cats.info;

/**
 * Constants used in Video Recorder Service.
 * 
 * @author SSugun00c
 */
public interface VideoRecorderServiceConstants
{
    String       NAMESPACE                                            = "urn:com:comcast:cats:videorecorder";

    String       VIDEO_RECORDER_SERVICE_NAME                          = "VideoRecorderService";
    String       VIDEO_RECORDER_SERVICE_LOCAL_PART_NAME               = "VideoRecorderServiceImplService";
    String       VIDEO_RECORDER_SERVICE_PORT_NAME                     = "VideoRecorderServicePort";
    String       VIDEO_RECORDER_SERVICE_MAPPED_NAME                   = "cats/services/VideoRecorderService";
    String       VIDEO_RECORDER_SERVICE_ENDPOINT_INTERFACE            = "com.comcast.cats.service.VideoRecorderService";
    String       VIDEO_RECORDER_SERVICE_WSDL_LOCATION                 = "video-recorder-service/VideoRecorderService?wsdl";

    String       REST_REQUEST_INTERNAL_BASE_PATH                      = "/internal/recorder/";
    String       REST_REQUEST_EXTERNAL_BASE_PATH                      = "/public/recorder/";

    String       REST_REQUEST_EXTERNAL_PATH                           = "/video-recorder-service/rest"
                                                                              + REST_REQUEST_EXTERNAL_BASE_PATH;

    String       REST_REQUEST_SUBMIT                                  = "submit";
    String       REST_REQUEST_STOP                                    = "stop";
    String       REST_REQUEST_STOP_BY_ID                              = "stop/id";
    String       REST_REQUEST_STATUS                                  = "status";
    String       REST_REQUEST_STATUS_BY_ID                            = "status/id";
    String       REST_REQUEST_HANDLE_ERROR                            = "error";
    String       REST_REQUEST_ACTIVE                                  = "active";
    String       REST_REQUEST_HISTORY_BY_MAC                          = "history/mac";
    String       REST_REQUEST_CURRENT_TASKS                           = "task/list";
    String       REST_REQUEST_ACTIVE_TELNET_PORTS                     = "telnet/list";
    String       REST_REQUEST_RECENTLY_USED_TELNET_PORTS              = "telnet/used/list";
    String       REST_REQUEST_GET_DISKSPACE_USAGE                     = "diskspace";

    String       REST_REQUEST_DELETE_RECORDING_BY_ID                  = "delete/recording/id";
    String       REST_REQUEST_DELETE_MEDIA_METADATA_BY_ID             = "delete/media/id";
    String       REST_REQUEST_DELETE_ALL_RECORDING_BY_MAC_ID          = "delete/recording/mac";
    String       REST_REQUEST_DELETE_ALL_RECORDING_BY_ALIAS           = "delete/recording/alias";                           ;

    String       REST_QUERY_PARAM_RECORDING_ID                        = "recordingId";

    String       UTF                                                  = "UTF-8";
    String       CONTENT_TYPE                                         = "Content-Type";
    String       APPLICATION_XML                                      = "application/xml";

    /**
     * Limiting to 1 Rack(16 settop recording).
     */
    int          MAX_CONCURRENT_RECORDING                             = 16;

    /**
     * 0 : Means unlimited. Recording will continue until a client base stop
     * request is received.
     */
    int          DEFAULT_RECORDING_DURATION                           = 0;

    /**
     * 0 : Means unlimited. Recording will continue until a client base stop
     * request is received.
     */
    int          DEFAULT_ROLLING_FILE_RECORDING_DURATION_IN_SECONDS   = 60 * 60;

    /**
     * Default port (camera) 1.
     */
    int          DEFAULT_PORT                                         = 1;

    /**
     * Default video width for image operations
     */
    int          DEFAULT_VIDEO_WIDTH                                  = 0;

    /**
     * Default video height for image operations
     */
    int          DEFAULT_VIDEO_HEIGHT                                 = 0;

    String       DEFAULT_FILE_NAME_SEPERATOR                          = "-";
    String       DEFAULT_EXTENSION                                    = "mp4";

    String       SYSTEM_PROPERTY_VLC_EXECUTABLE_PATH                  = "cats.pvr.vlc.executable.path";
    String       SYSTEM_PROPERTY_VLC_TELNET_HOST                      = "cats.pvr.vlc.telnet.host";
    String       SYSTEM_PROPERTY_VLC_TELNET_PASSWORD                  = "cats.pvr.vlc.telnet.password";
    String       SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_START = "cats.pvr.vlc.telnet.port.range.start";
    String       SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_END   = "cats.pvr.vlc.telnet.port.range.end";
    String       SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH       = "cats.pvr.file.server.base.path";
    String       SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH       = "cats.pvr.http.server.base.path";
    String       SYSTEM_PROPERTY_CATS_PVR_DB_USER_ID                  = "cats.pvr.db.user.id";
    String       SYSTEM_PROPERTY_CATS_PVR_DB_PASSWORD                 = "cats.pvr.db.password";
    String       SYSTEM_PROPERTY_CATS_PVR_DB_CONNECTION_URL           = "cats.pvr.db.connection.url";
    String       SYSTEM_PROPERTY_CATS_PVR_DB_DRIVER_CLASS_NAME        = "cats.pvr.db.driver.class.name";
    String       SYSTEM_PROPERTY_CATS_PVR_REST_API_BASE_URL           = "cats.pvr.rest.api.base.url";

    int          DEFAULT_TELNET_PORT_RANGE_START                      = 9000;
    int          DEFAULT_TELNET_PORT_RANGE_END                        = 9090;

    CharSequence LOCALHOST_IP                                         = "127.0.0.1";
    CharSequence LOCALHOST_NAME                                       = "localhost";
}
