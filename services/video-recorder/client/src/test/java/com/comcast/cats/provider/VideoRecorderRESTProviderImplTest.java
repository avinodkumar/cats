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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.provider.exceptions.VideoRecorderException;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.HttpClientUtil;

/**
 * Unit tests for {@link VideoRecorderRESTProviderImpl}.
 * 
 * @author sajayjk
 * 
 */
@PrepareForTest( HttpClientUtil.class )
@RunWith( PowerMockRunner.class )
public class VideoRecorderRESTProviderImplTest
{
    VideoRecorderRESTProviderImpl recorderProvider;

    String                        macID                = "XX:XX:XX:XX:XX:XX";

    String                        videoHostIp          = "xxx";
    Integer                       videoPort            = 1;
    String                        serverHost           = "xxx";

    String                        aliasName            = "unitTest";
    Integer                       duration             = 1;
    VideoRecorderResponse         failureResponse;
    VideoRecorderResponse         successResponseFailedRecordingResponse;
    VideoRecorderResponse         successResponse;
    Recording                     recording;
    Recording                     failedRecording;
    String                        recordingURL         = "http://localhost/video-storage/xxx";
    String                        expectedRecordingURL = "http://" + serverHost + "/video-storage/xxx";

    @Before
    public void setUp()
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, serverHost, macID );

        recording = new Recording();
        RecordingStatus successStatus = new RecordingStatus();
        successStatus.setMessage( "Sucess" );
        successStatus.setState( VideoRecorderState.RECORDING.name() );
        recording.setRecordingStatus( successStatus );
        MediaMetaData media = new MediaMetaData();
        media.setHttpPath( recordingURL );
        List< MediaMetaData > medias = new ArrayList< MediaMetaData >();
        medias.add( media );
        recording.setMediaInfoEntityList( medias );

        failedRecording = new Recording();
        RecordingStatus failedStatus = new RecordingStatus();
        failedStatus.setState( VideoRecorderState.ERROR.name() );
        failedRecording.setRecordingStatus( failedStatus );

        failureResponse = new VideoRecorderResponse();
        failureResponse.setResult( WebServiceReturnEnum.FAILURE );
        failureResponse.setMessage( "Failed from server" );
        failureResponse.setRecording( null );
        failureResponse.setRecordingList( null );

        successResponseFailedRecordingResponse = new VideoRecorderResponse();
        successResponseFailedRecordingResponse.setResult( WebServiceReturnEnum.SUCCESS );
        successResponseFailedRecordingResponse.setMessage( "Recording Started" );
        successResponseFailedRecordingResponse.setRecording( failedRecording );
        successResponseFailedRecordingResponse.setRecordingList( null );

        successResponse = new VideoRecorderResponse();
        successResponse.setResult( WebServiceReturnEnum.SUCCESS );
        successResponse.setMessage( "Recording Started" );
        successResponse.setRecording( recording );
    }

    @After
    public void tearDown()
    {
        recorderProvider = null;
    }

    @Test
    public void startVideoRecordingNullTest() throws VideoRecorderException
    {
        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( successResponse );

            replay( HttpClientUtil.class );

            assertTrue( recorderProvider.startVideoRecording( null ) );
        }
        catch ( Exception e )
        {
            fail();
        }
    }

    @Test
    public void startVideoRecordingEmptyStringTest() throws VideoRecorderException
    {
        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort, "" ) ) ).andReturn( successResponse );

            replay( HttpClientUtil.class );

            assertTrue( recorderProvider.startVideoRecording( "" ) );
        }
        catch ( Exception e )
        {
            fail();
        }
    }

    @Test( expected = VideoRecorderException.class )
    public void startVideoRecordingUnconfiguredTest() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( null, 0, null, null );
        recorderProvider.startVideoRecording( aliasName );
    }

    @Test( expected = VideoRecorderException.class )
    public void startVideoRecordingUnconfigured1Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( "", videoPort, serverHost, macID );
        recorderProvider.startVideoRecording( aliasName );
    }

    @Test( expected = VideoRecorderException.class )
    public void startVideoRecordingUnconfigured2Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, "", macID );
        recorderProvider.startVideoRecording( aliasName );
    }

    @Test( expected = VideoRecorderException.class )
    public void startVideoRecordingUnconfigured3Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, -1, serverHost, macID );
        recorderProvider.startVideoRecording( aliasName );
    }

    @Test( expected = VideoRecorderException.class )
    public void startVideoRecordingUnconfigured4Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, serverHost, "" );
        recorderProvider.startVideoRecording( aliasName );
    }

    @Test
    public void startVideoRecordingServerReturnNullTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort, aliasName ) ) ).andReturn( null );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.startVideoRecording( aliasName ) );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void startVideoRecordingServerReturnFailureTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort, aliasName ) ) ).andReturn( failureResponse );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.startVideoRecording( aliasName ) );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void startVideoRecordingNoRecordingTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort, aliasName ) ) ).andReturn(
                    successResponseFailedRecordingResponse );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.startVideoRecording( aliasName ) );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void startVideoRecordingSuccessTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort, aliasName ) ) ).andReturn( successResponse );

            replay( HttpClientUtil.class );

            assertTrue( recorderProvider.startVideoRecording( aliasName ) );
        }
        catch ( Exception e )
        {
            fail();
        }
    }

    @Test
    public void startVideoRecordingNoInputServerReturnNullTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( null );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.startVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void startVideoRecordingNoInputServerReturnFailureTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( failureResponse );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.startVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void startVideoRecordingNoInputNoRecordingTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn(
                    successResponseFailedRecordingResponse );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.startVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void startVideoRecordingNoInputSuccessTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( successResponse );

            replay( HttpClientUtil.class );

            assertTrue( recorderProvider.startVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }
    }

    @Test( expected = VideoRecorderException.class )
    public void stopVideoRecordingUnconfiguredTest() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( null, 0, null, null );
        recorderProvider.stopVideoRecording();
    }

    @Test( expected = VideoRecorderException.class )
    public void stopVideoRecordingUnconfigured1Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( "", videoPort, serverHost, macID );
        recorderProvider.stopVideoRecording();
    }

    @Test( expected = VideoRecorderException.class )
    public void stopVideoRecordingUnconfigured2Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, "", macID );
        recorderProvider.stopVideoRecording();
    }

    @Test( expected = VideoRecorderException.class )
    public void stopVideoRecordingUnconfigured3Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, -1, serverHost, macID );
        recorderProvider.stopVideoRecording();
    }

    @Test( expected = VideoRecorderException.class )
    public void stopVideoRecordingUnconfigured4Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, serverHost, null );
        recorderProvider.stopVideoRecording();
    }

    @Test( expected = VideoRecorderException.class )
    public void stopVideoRecordingUnconfigured5Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, serverHost, "" );
        recorderProvider.stopVideoRecording();
    }

    @Test
    public void stopVideoRecordingServerReturnNullTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( null );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.stopVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void stopVideoRecordingServerReturnFailureTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( failureResponse );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.stopVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void stopVideoRecordingNoRecordingTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn(
                    successResponseFailedRecordingResponse );

            replay( HttpClientUtil.class );

            assertFalse( recorderProvider.stopVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void stopVideoRecordingSuccessTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.postForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( successResponse );

            replay( HttpClientUtil.class );

            assertTrue( recorderProvider.stopVideoRecording() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingStatusUnconfiguredTest() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( null, 0, null, null );
        recorderProvider.getRecordingInfo();
    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoUnconfigured1Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( "", videoPort, serverHost, macID );
        recorderProvider.getRecordingInfo();
    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoUnconfigured2Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, "", macID );
        recorderProvider.getRecordingInfo();
    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoUnconfigured3Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, -1, serverHost, macID );
        recorderProvider.getRecordingInfo();
    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoUnconfigured4Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, serverHost, null );
        recorderProvider.getRecordingInfo();
    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoUnconfigured5Test() throws VideoRecorderException
    {
        recorderProvider = new VideoRecorderRESTProviderImpl( videoHostIp, videoPort, serverHost, "" );
        recorderProvider.getRecordingInfo();
    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoServerReturnNullTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.getForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( null );

            replay( HttpClientUtil.class );

            recorderProvider.getRecordingInfo();
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test( expected = VideoRecorderException.class )
    public void getRecordingInfoServerReturnFailureTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.getForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( failureResponse );

            replay( HttpClientUtil.class );

            recorderProvider.getRecordingInfo();
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void getRecordingInfoNoRecordingTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.getForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn(
                    successResponseFailedRecordingResponse );

            replay( HttpClientUtil.class );

            assertNotNull( recorderProvider.getRecordingInfo() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    @Test
    public void getRecordingInfoSuccessTest() throws VideoRecorderException
    {

        mockStatic( HttpClientUtil.class );
        try
        {
            EasyMock.expect(
                    HttpClientUtil.getForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                            getParamMap( macID, videoHostIp, videoPort ) ) ).andReturn( successResponse );

            replay( HttpClientUtil.class );

            assertNotNull( recorderProvider.getRecordingInfo() );
        }
        catch ( Exception e )
        {
            fail();
        }

    }

    private Map< String, String > getParamMap( String macID, String videoServerIp, Integer port, String aliasName )
    {
        Map< String, String > paramMap = getParamMap( macID, videoServerIp, port );
        paramMap.put( "alias", String.valueOf( aliasName ) );
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
        String requestUri = "http://" + serverHost + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_BASE_PATH
                + restRequest;

        return requestUri;
    }

}
