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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.comcast.cats.config.ui.recording.SettopRecordingServiceImpl;
import com.comcast.cats.domain.HardwareConnection;
import com.comcast.cats.domain.HardwareDevice;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.HardwareType;
import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.HttpClientUtil;

@PrepareForTest(HttpClientUtil.class)
@RunWith(PowerMockRunner.class)
public class RecordingServiceITest
{
    SettopRecordingServiceImpl settopRecordingService;
    Slot slot;
    
    private final static String VIDEO_SERVER_IP = "192.168.160.201";
    private final static Integer VIDEO_SERVER_PORT = 2;
    private String restBaseUrl;    
    private RecordingStatus failedStatus;
    private RecordingStatus successStatus;
    
    private String mac = "E4:48:C7:A8:1B:12";

    @Before
    public void setUp(){
        settopRecordingService = new SettopRecordingServiceImpl();
        slot = new Slot();
        HardwareInterface videoDevice = new HardwareInterface();
        videoDevice.setHardwarePurpose( HardwarePurpose.VIDEOSERVER );
        videoDevice.setDeviceHost( VIDEO_SERVER_IP );
        videoDevice.setConnectionPort( 1 );
//        HardwareConnection connection = new HardwareConnection();
//        connection.setHardwareDevice( videoDevice );
//        connection.setPort( VIDEO_SERVER_PORT );

        Map<HardwarePurpose, HardwareInterface> connectionmap = new HashMap< HardwarePurpose, HardwareInterface >();
        connectionmap.put( HardwarePurpose.VIDEOSERVER, videoDevice );

        slot.setConnections( connectionmap );
        
        failedStatus = new RecordingStatus();
        failedStatus.setState( VideoRecorderState.ERROR.name() );
        
        successStatus = new RecordingStatus();
        successStatus.setState( VideoRecorderState.RECORDING.name() );

    }
    
    @After
    public void tearDown(){
        settopRecordingService = null;
    }
     
    @Test
    public void getRecordingDetailsTest(){
        System.out.println("ccc "+settopRecordingService.getRecordingDetails( slot, mac ));
        VideoRecorderResponse response = settopRecordingService.getRecordingDetails( slot, mac );
         assertNotNull(response);
         assertNotNull( response );
         assertNotNull( response.getRecording() );
         assertNotNull( response.getRecordingList() );
         assertNotNull( response.getResult() );
    }

    
    @Test
    public void getRecordingHistoryTest(){

         List< Recording > history = settopRecordingService.getRecordingHistory( mac ); //Make sure no exception is thrown.
         for(Recording recording : history ){
             System.out.println("recording "+recording);
         }
    }

    
    @Test
    public void startRecordingTest(){
        VideoRecorderResponse response = settopRecordingService.startRecording( slot,mac,null);
        assertNotNull( response );
        assertNotNull( response.getRecording() );
        assertNotNull( response.getRecordingList() );
        assertNotNull( response.getResult() );
        
     //   System.out.println("Started recording "+settopRecordingService.startRecording( slot,alias));
        try
        {
            Thread.sleep( 60000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
     //   System.out.println("Stopping recording "+settopRecordingService.stopRecording( slot,alias));

        VideoRecorderResponse stopResponse = settopRecordingService.stopRecording( slot,mac);
        assertNotNull( stopResponse );
        assertNotNull( stopResponse.getRecording() );
        assertNotNull( stopResponse.getRecordingList() );
        assertNotNull( stopResponse.getResult() );
    }
    
    @Test
    public void deleteFileTest(){
        VideoRecorderResponse response = settopRecordingService.startRecording( slot,mac,null);
        assertNotNull( response );
     //    assertNotNull( response.getRecording() );
     //   assertNotNull( response.getRecordingList() );
     //   assertNotNull( response.getResult() );
        
     //   System.out.println("Started recording "+settopRecordingService.startRecording( slot,alias));
        try
        {
            Thread.sleep( 60000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
     //   System.out.println("Stopping recording "+settopRecordingService.stopRecording( slot,alias));

        VideoRecorderResponse stopResponse = settopRecordingService.stopRecording( slot,mac);
//        assertNotNull( stopResponse );
//        assertNotNull( stopResponse.getRecording() );
//        assertNotNull( stopResponse.getRecordingList() );
//        assertNotNull( stopResponse.getResult() );
        
        MediaMetaData media = stopResponse.getRecording().getMediaInfoEntityList().get( 0 );
        VideoRecorderResponse deleteResponse = settopRecordingService.deleteFile( media.getId() );
        assertEquals(WebServiceReturnEnum.SUCCESS, deleteResponse.getResult());
        
        Recording recording = stopResponse.getRecording();
        deleteResponse = settopRecordingService.deleteFile( recording.getId() );
        assertEquals(WebServiceReturnEnum.SUCCESS, deleteResponse.getResult());
    }

}
