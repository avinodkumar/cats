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
package com.comcast.cats.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.provider.exceptions.VideoRecorderException;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.HttpClientUtil;

/**
 * REST implementation of {@link RecorderProvider}.
 * 
 * @author sajayjk
 * 
 */
public class VideoRecorderRESTProviderImpl implements RecorderProvider
{
    private static final long serialVersionUID = -8068245325619415851L;
    String                    videoHostIp;
    int                       videoPort;
    String                    serverHost;
    String                    macID;
    private Object            parent;

    private static Logger     logger           = Logger.getLogger( VideoRecorderRESTProviderImpl.class );

    public VideoRecorderRESTProviderImpl( String videoHostIp, int videoPort, String serverHost, String macID )
    {
        this.videoHostIp = videoHostIp;
        this.videoPort = videoPort;
        this.serverHost = serverHost;
        this.macID = macID;
    }

    @Override
    public Object getParent()
    {
        return parent;
    }

    public void setParent( Object parent )
    {
        this.parent = parent;
    }

    @Override
    public boolean startVideoRecording( String recordingAliasName ) throws VideoRecorderException
    {
        return execute( recordingAliasName, VideoRecorderServiceConstants.REST_REQUEST_SUBMIT );
    }

    @Override
    public boolean startVideoRecording() throws VideoRecorderException
    {
        return execute( null, VideoRecorderServiceConstants.REST_REQUEST_SUBMIT );
    }

    @Override
    public boolean stopVideoRecording() throws VideoRecorderException
    {
        return execute( null, VideoRecorderServiceConstants.REST_REQUEST_STOP );
    }

    private boolean execute( String recordingGroupNameAlias, String requestURI ) throws VideoRecorderException
    {
        boolean retVal = false;
        VideoRecorderResponse response = null;
        if ( videoHostIp != null && !videoHostIp.isEmpty() && videoPort >= 0 && requestURI != null
                && !requestURI.isEmpty() && macID != null && !macID.isEmpty() && serverHost != null
                && !serverHost.isEmpty() )
        {
            try
            {
                response = ( VideoRecorderResponse ) HttpClientUtil.postForObject( getRequestUri( requestURI ),
                        getParamMap( macID, videoHostIp, videoPort, recordingGroupNameAlias ) );
                logger.debug( "webServiceReturn " + response );
            }
            catch ( ClassCastException e )
            {
                logger.debug( "RecorderServer not a valid one. " + " hostServer " + serverHost + " error : "
                        + e.getMessage() );
            }

            if ( response == null
                    || response.getResult() == WebServiceReturnEnum.FAILURE
                    || ( response.getRecording() != null && response.getRecording().getRecordingStatus() != null && ( response
                            .getRecording().getRecordingStatus().getState()
                            .equalsIgnoreCase( VideoRecorderState.ERROR.name() ) || response.getRecording()
                            .getRecordingStatus().getState().equalsIgnoreCase( VideoRecorderState.FORCE_CLOSE.name() ) ) ) )
            {
                retVal = false;
            }
            else
            {
                retVal = true;
            }
        }
        else
        {
            throw new VideoRecorderException( "Provider not instantiated properly" );
        }
        logger.trace( "execute:  videoHostIp " + videoHostIp + " videoPort " + videoPort + " requestURI " + requestURI
                + " response " + response );

        return retVal;
    }

    @Override
    public String getRecordingInfo() throws VideoRecorderException
    {
        String retVal = "Status could not be retrieved";
        VideoRecorderResponse response = null;
        if ( videoHostIp != null && !videoHostIp.isEmpty() && videoPort >= 0 && macID != null && !macID.isEmpty()
                && serverHost != null && !serverHost.isEmpty() )
        {
            try
            {
                response = ( VideoRecorderResponse ) HttpClientUtil.getForObject(
                        getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                        getParamMap( macID, videoHostIp, videoPort ) );
            }
            catch ( ClassCastException e )
            {
                logger.debug( "RecorderServer not a valid one. " + " hostServer " + serverHost + " error : "
                        + e.getMessage() );
            }

            if ( response == null || response.getResult() == WebServiceReturnEnum.FAILURE )
            {
                String message = ( response == null ) ? "Error Recording: Cause Unknown" : response.getMessage();
                throw new VideoRecorderException( message );
            }
            else
            {
                StringBuilder stringBuilder = new StringBuilder();
                if ( response.getRecording() != null )
                {
                    stringBuilder.append( "\n" );
                    stringBuilder.append( "MAC Address           : " + response.getRecording().getStbMacAddress() );
                    stringBuilder.append( "\n" );
                    stringBuilder.append( "Created Time          : " + response.getRecording().getCreatedTime() );
                    stringBuilder.append( "\n" );
                    stringBuilder.append( "VideoServerIP         : " + response.getRecording().getVideoServerIp() );
                    stringBuilder.append( "\n" );
                    stringBuilder.append( "VideoServer Camera    : " + response.getRecording().getVideoServerPort() );
                    stringBuilder.append( "\n" );
                    if ( response.getRecording().getRecordingStatus() != null )
                    {
                        stringBuilder.append( "Recording State       : "
                                + response.getRecording().getRecordingStatus().getState() );
                        stringBuilder.append( "\n" );
                        stringBuilder.append( "Status Message        : "
                                + response.getRecording().getRecordingStatus().getMessage() );
                        stringBuilder.append( "\n" );
                    }
                    if ( response.getRecording().getMediaInfoEntityList() != null
                            && response.getRecording().getMediaInfoEntityList().size() > 0 )
                    {
                        String filePath = substituteFilePath( response.getRecording().getMediaInfoEntityList().get( 0 )
                                .getHttpPath() );
                        stringBuilder.append( "Http Path             : " + filePath );
                        stringBuilder.append( "\n" );
                    }
                }

                retVal = stringBuilder.toString();
            }
        }
        else
        {
            throw new VideoRecorderException( "Provider not instantiated properly" );
        }

        return retVal;
    }

    private Map< String, String > getParamMap( String macID, String videoServerIp, Integer port, String aliasName )
    {
        Map< String, String > paramMap = getParamMap( macID, videoServerIp, port );
        if ( aliasName != null )
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
        String requestUri = "http://" + serverHost + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_PATH
                + restRequest;

        return requestUri;
    }

    private String substituteFilePath( String filePath )
    {
        String retVal = filePath;
        try
        {
            URL filePathURL = new URL( filePath );
            String host = filePathURL.getHost();
            retVal = StringUtils.replaceOnce( filePath, host, serverHost );
        }
        catch ( MalformedURLException e )
        {
            logger.debug( "Provider doesnt know how to parse this syntax" );
        }
        return retVal;
    }
}
