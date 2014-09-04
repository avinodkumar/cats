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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.config.ui.recording.SettopRecordingServiceImpl;
import com.comcast.cats.domain.HardwareConnection;
import com.comcast.cats.domain.HardwareDevice;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.HardwareType;
import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.HttpClientUtil;

@PrepareForTest(HttpClientUtil.class)
@RunWith(PowerMockRunner.class)
public class SettopRecordingServiceImplTest
{
    SettopRecordingServiceImpl settopRecordingService;
    Slot slot;
    
    private final static String VIDEO_SERVER_IP = "192.168.21.12";
    private final static Integer VIDEO_SERVER_PORT = 10;
    private String restBaseUrl;    
    private VideoRecorderResponse failedStatus;
    private VideoRecorderResponse successStatus;
    
    private String mac = "XX:XX:XX:XX:XX:XX";

    @Before
    public void setUp(){
        settopRecordingService = new SettopRecordingServiceImpl();
        slot = new Slot();
        HardwareInterface videoDevice = new HardwareInterface();
        videoDevice.setHardwarePurpose( HardwarePurpose.VIDEOSERVER );
        videoDevice.setDeviceHost( VIDEO_SERVER_IP );
        videoDevice.setConnectionPort( VIDEO_SERVER_PORT );
//        HardwareConnection connection = new HardwareConnection();
//        connection.setHardwareDevice( videoDevice );
//        connection.setPort( VIDEO_SERVER_PORT );

        Map<HardwarePurpose, HardwareInterface> connectionmap = new HashMap< HardwarePurpose, HardwareInterface >();
        connectionmap.put( HardwarePurpose.VIDEOSERVER, videoDevice );
        
        slot.setConnections( connectionmap );
        
        failedStatus = new VideoRecorderResponse();
        failedStatus.setResult( WebServiceReturnEnum.FAILURE );
        failedStatus.setRecording( null );
        failedStatus.setRecordingList( null );
        
        successStatus = new VideoRecorderResponse();
        successStatus.setResult( WebServiceReturnEnum.SUCCESS );
        
        AuthController.setHostAddress( "host" );
      //  successStatus.setState( VideoRecorderState.RECORDING.name() );
    }
    
    @After
    public void tearDown(){
        settopRecordingService = null;
    }
    
    private String getRequestUri( String restRequest )
    {
        String requestUri = "http://"+AuthController.getHostAddress() + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_PATH + restRequest;
        return requestUri;
    }
    
    private Map< String, String > getParamMap( String videoServerIp, Integer port , String macId)
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "videoServerIp", videoServerIp );
        paramMap.put( "port", String.valueOf( port ) );
        paramMap.put( "macId", macId );
        return paramMap;
    }
    
    private Map< String, String > getParamMap( String macId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( "macId", macId );
        return paramMap;
    }
    
    @Test
    public void getRecordingDetailsNullTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.getForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(),mac))).andReturn(null);
            
            replay( HttpClientUtil.class );
            assertNull(settopRecordingService.getRecordingDetails( slot, mac ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void getRecordingDetailsTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.getForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(failedStatus);
            
            replay( HttpClientUtil.class );
            assertEquals(failedStatus, settopRecordingService.getRecordingDetails( slot, mac ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    
    @Test
    public void getRecordingDetailsNullInputTest(){
            assertNull(settopRecordingService.getRecordingDetails( null, mac ));
    }
    
    @Test
    public void getRecordingDetailsNullInput1Test(){
            assertNull(settopRecordingService.getRecordingDetails( slot, null ));
    }
    
    
    
    @Test
    public void getRecordingHistoryNullTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.getForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_HISTORY_BY_MAC ),
                    getParamMap(mac))).andReturn(null);
            
            replay( HttpClientUtil.class );
            assertNull(settopRecordingService.getRecordingHistory( mac ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void getRecordingHistoryTest(){
        try
        {
            List<Recording> history = new ArrayList< Recording >();
            Recording recording2 = new Recording();
            recording2.setVideoServerPort( VIDEO_SERVER_PORT );
            recording2.setVideoServerIp( VIDEO_SERVER_IP );
            recording2.setStbMacAddress( mac );
            history.add(recording2);
            
            successStatus.setRecordingList( history );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.getForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_HISTORY_BY_MAC ),
                    getParamMap(mac))).andReturn(successStatus);

            replay( HttpClientUtil.class );
            List<Recording> recordingHistory = settopRecordingService.getRecordingHistory( mac );
            assertTrue(recordingHistory.contains( recording2 ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    
    @Test
    public void getRecordingHistoryNullInputTest(){
            assertNull(settopRecordingService.getRecordingHistory( null ));
    }
    
    
    @Test
    public void startRecordingNullTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.postForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(null);

            replay( HttpClientUtil.class );
            assertNull(settopRecordingService.startRecording( slot,mac,null ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void startRecordingTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.postForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(successStatus);
            replay( HttpClientUtil.class );
            assertEquals(successStatus, settopRecordingService.startRecording( slot, mac,null));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void startRecordingFailedReturnTest(){
        try
        {
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.postForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(failedStatus);

            replay( HttpClientUtil.class );
            assertEquals(failedStatus, settopRecordingService.startRecording( slot, mac,null));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    
    @Test
    public void startRecordingNullInputTest(){
        assertNull( settopRecordingService.startRecording( null, null,null ) );
        assertNull( settopRecordingService.startRecording( null, mac,null) );
        assertNull( settopRecordingService.startRecording( slot, null,null ) );
        assertNull( settopRecordingService.startRecording( null, null,"alias" ) );
    }
    
    
    
    @Test
    public void stopRecordingNullTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.postForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(null);

            replay( HttpClientUtil.class );
            assertNull(settopRecordingService.stopRecording( slot,mac ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void stopRecordingTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.postForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(successStatus);

            replay( HttpClientUtil.class );
            assertEquals(successStatus, settopRecordingService.stopRecording( slot,mac));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void stopRecordingFailedReturnTest(){
        try
        {
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.postForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                    getParamMap(slot.getVideoHost(), slot.getVideoPort(), mac))).andReturn(failedStatus);
            replay( HttpClientUtil.class );
            assertEquals(failedStatus, 
                    settopRecordingService.stopRecording( slot,mac));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    
    @Test
    public void stopRecordingNullInputTest(){
        assertNull( settopRecordingService.stopRecording( null, null ) );
        assertNull( settopRecordingService.stopRecording( null,mac ) );
        assertNull( settopRecordingService.stopRecording( slot, null ) );
    }
    
    @Test
    public void deleteFileFailedResponseTest(){
        try
        {
            Map< String, String > paramMap = new HashMap< String, String >();
            paramMap.put( "mediaMetaDataId", "21" );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_MEDIA_METADATA_BY_ID ),
                    paramMap)).andReturn(failedStatus);
            replay( HttpClientUtil.class );
            assertEquals(failedStatus, 
                    settopRecordingService.deleteFile( 21 ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    
    @Test
    public void deleteFileSuccessTest(){
        try
        {
            Map< String, String > paramMap = new HashMap< String, String >();
            paramMap.put( "mediaMetaDataId", "21" );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_MEDIA_METADATA_BY_ID ),
                    paramMap)).andReturn(successStatus);
            replay( HttpClientUtil.class );
            assertEquals(successStatus, 
                    settopRecordingService.deleteFile( 21 ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void deleteRecordingFailedResponseTest(){
        try
        {
            Map< String, String > paramMap = new HashMap< String, String >();
            paramMap.put( "recordingId", "21" );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_RECORDING_BY_ID ),
                    paramMap)).andReturn(failedStatus);
            replay( HttpClientUtil.class );
            assertEquals(failedStatus, 
                    settopRecordingService.deleteRecording( 21 ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void deleteRecordingSuccessTest(){
        try
        {
            Map< String, String > paramMap = new HashMap< String, String >();
            paramMap.put( "recordingId", "21" );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_RECORDING_BY_ID ),
                    paramMap)).andReturn(successStatus);
            replay( HttpClientUtil.class );
            assertEquals(successStatus, 
                    settopRecordingService.deleteRecording( 21 ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void deleteAllRecordingsForSettopFailedResponseTest(){
        try
        {
            Map< String, String > paramMap = new HashMap< String, String >();
            paramMap.put( "macId", "21" );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_MAC_ID ),
                    paramMap)).andReturn(failedStatus);
            replay( HttpClientUtil.class );
            assertEquals(failedStatus, 
                    settopRecordingService.deleteAllRecordingsForSettop( "21" ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void deleteAllRecordingsForSettopNullInputTest(){
        try
        {
            assertNull(settopRecordingService.deleteAllRecordingsForSettop( null ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void deleteAllRecordingsForSettopEmptyInputTest(){
        try
        {
            assertNull(settopRecordingService.deleteAllRecordingsForSettop( "" ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
    @Test
    public void deleteAllRecordingsForSettopSuccessTest(){
        try
        {
            Map< String, String > paramMap = new HashMap< String, String >();
            paramMap.put( "macId", "21" );
            
            mockStatic( HttpClientUtil.class );
            EasyMock.expect(HttpClientUtil.deleteForObject(
                    getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_DELETE_ALL_RECORDING_BY_MAC_ID ),
                    paramMap)).andReturn(successStatus);
            replay( HttpClientUtil.class );
            assertEquals(successStatus, 
                    settopRecordingService.deleteAllRecordingsForSettop( "21" ));
        }
        catch ( Exception e )
        {
            fail();
        }
    }
    
}
