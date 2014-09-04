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
package com.comcast.cats.recorder.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.junit.Test;

import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;

/**
 * General entity service tests.
 * 
 * @author SSugun00c
 * 
 */
public class EntityServiceIT extends BaseEntityServiceIT
{
    @Test
    public void testAddNewRecording() throws Exception
    {
        int videoServerPort = 0;
        String videoServerIp = "192.168.160.202";
        String mrl = "rtsp://192.168.160.202/axis-media/media.amp?videocodec=h264";

        // Add to recordings
        Recording recording = new Recording( null, videoServerIp, videoServerPort, mrl );

        RecordingStatus recordingStatus = new RecordingStatus( VideoRecorderState.RECORDING.toString(), null );
        // Add to recording status
        recording.setRecordingStatus( recordingStatus );

        List< MediaMetaData > mediaMetaDataList = new ArrayList< MediaMetaData >();

        String filePath_01 = "C:/Program Files (x86)/Apache Software Foundation/Apache2.2/htdocs/ip/2012/November/13/192.168.160.202/0/cats-capture-01-32-09-PM-EST.mp4";
        String filePath_02 = "C:/Program Files (x86)/Apache Software Foundation/Apache2.2/htdocs/ip/2012/November/13/192.168.160.202/0/cats-capture-02-00-00-PM-EST.mp4";

        String httpPath_01 = "http://localhost/ip/2012/November/13/192.168.160.202/0/cats-capture-01-32-09-PM-EST.mp4";
        String httpPath_02 = "http://localhost/ip/2012/November/13/192.168.160.202/0/cats-capture-02-00-00-PM-EST.mp4";

        MediaMetaData media_01 = new MediaMetaData( filePath_01, httpPath_01 );
        media_01.setRecordingEntity( recording );
        mediaMetaDataList.add( media_01 );

        MediaMetaData media_02 = new MediaMetaData( filePath_02, httpPath_02 );
        media_02.setRecordingEntity( recording );
        mediaMetaDataList.add( media_02 );

        // Add to Media Info
        recording.setMediaInfoEntityList( mediaMetaDataList );

        entityManager.getTransaction().begin();
        entityManager.persist( recording );
        entityManager.getTransaction().commit();

        LOGGER.info( "Created New recording with id= " + recording.getId() );
        LOGGER.info( "Created New recording status with id= " + recording.getRecordingStatus().getId() );
    }

    /*
     * @Test public void testGetAllRecording() throws Exception {
     * 
     * @SuppressWarnings( "unchecked" ) List< Recording > recordingEntityList =
     * ( List< Recording > ) entityManager .createQuery( "from RecordingEntity"
     * ).getResultList();
     * 
     * LOGGER.info( recordingEntityList.size() + " items found" );
     * 
     * for ( Recording recordingEntity : recordingEntityList ) { LOGGER.info(
     * recordingEntity.toString() ); } }
     */
    @Test
    public void testGetAllRecordingById() throws Exception
    {
        @SuppressWarnings( "unchecked" )
        List< Recording > recordingEntityList = ( List< Recording > ) entityManager.createQuery( "from Recording" )
                .getResultList();

        LOGGER.info( recordingEntityList.size() + " items found" );

        for ( Recording recordingEntity : recordingEntityList )
        {
            LOGGER.info( recordingEntity.toString() );
        }
    }

    @Test
    public void testGetActiveRecording() throws Exception
    {
        final Query query = entityManager.createNamedQuery( "Recording.findActiveRecordingByMacId" );
        String macId = "00:19:47:25:AD:7E";
        query.setParameter( "stbMacAddress", macId );

        try
        {
            final Recording recording = ( Recording ) query.getSingleResult();

            LOGGER.info( recording.toString() );
        }
        catch ( NoResultException e )
        {
            LOGGER.info( "No record available" );
        }

    }

    @Test
    public void testGetLatestRecording() throws Exception
    {
        final Query query = entityManager.createNamedQuery( "Recording.findAllRecordingByMacIdWithLatestFirst" );
        String macId = "00:19:47:25:AD:7E";
        query.setParameter( "stbMacAddress", macId );

        try
        {
            final Recording recording = ( Recording ) query.setMaxResults( 1 ).getSingleResult();

            LOGGER.info( recording.toString() );
        }
        catch ( NoResultException e )
        {
            LOGGER.info( "No record available" );
        }

    }

    @Test
    public void testUpdateRecordingStatus() throws Exception
    {
        String videoServerIp = "192.168.160.202";
        Integer port = 80;

        final Query query = entityManager.createNamedQuery( "Recording.findActiveRecording" );
        query.setParameter( "videoServerIp", videoServerIp ).setParameter( "videoServerPort", port );

        Recording recording = null;
        try
        {
            recording = ( Recording ) query.getSingleResult();
            LOGGER.info( "An active recording found" );

            RecordingStatus recordingStatus = recording.getRecordingStatus();
            LOGGER.info( "before: " + recordingStatus );

            recordingStatus.setState( VideoRecorderState.STOPPED.toString() );

            entityManager.getTransaction().begin();
            entityManager.merge( recordingStatus );
            entityManager.getTransaction().commit();
            LOGGER.info( "after: " + recordingStatus );
        }
        catch ( NoResultException e )
        {
            LOGGER.info( "No active recording found " );
        }
    }

    @Test
    public void testGetActiveRecordingById() throws Exception
    {
        /*
         * SELECT * FROM pvr_recording,pvr_media_metadata,pvr_recording_status
         * where pvr_recording.recording_id= pvr_media_metadata.parent_id AND
         * pvr_recording.recording_id=pvr_recording_status.recording_status_id
         * AND pvr_recording.recording_id=1 AND
         * pvr_recording_status.state!='STOPPED' AND
         * pvr_recording_status.state!='ERROR' AND
         * pvr_recording_status.state!='FORCE_CLOSE';
         */

        /*
         * @NamedQuery( name = "Recording.findActiveRecordingByMacId", query =
         * "SELECT recording FROM Recording recording, RecordingStatus recordingStatus "
         * + "WHERE recording.id=recordingStatus.id " +
         * "AND recording.stbMacAddress=:stbMacAddress " +
         * "AND recordingStatus.state!='STOPPED' " +
         * "AND recordingStatus.state!='ERROR' " +
         * "AND recordingStatus.state!='FORCE_CLOSE'" ),
         */

        /*
         * String queryStr="SELECT recording " +
         * "FROM Recording recording, RecordingStatus recordingStatus, MediaMetaData mediaMetaData "
         * + " WHERE recording.id= mediaMetaData.recordingEntity.id" +
         * " AND recording.id=recordingStatus.id" +
         * " AND recording.id=:recordingId" +
         * " AND recordingStatus.state!='STOPPED'" +
         * " AND recordingStatus.state!='ERROR'" +
         * " AND recordingStatus.state!='FORCE_CLOSE'";
         */

        // LOGGER.info( "queryStr= "+queryStr );

        final Query query = entityManager.createNamedQuery( "Recording.findActiveRecordingByRecordingId" );
        query.setParameter( "recordingId", 1 );

        Recording recording = ( Recording ) query.getSingleResult();

        LOGGER.info( "recording= " + recording );
    }

    @Test
    public void testGetRecordingHistoryByMac() throws Exception
    {
        String macId = "00:19:47:25:AD:7E";

        final Query query = entityManager.createNamedQuery( "Recording.findRecordingByMacId" );
        // .createQuery(
        // "from Recording recording where recording.stbMacAddress =:stbMacAddress"
        // );
        query.setParameter( "stbMacAddress", macId );
        @SuppressWarnings( "unchecked" )
        final List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        LOGGER.info( recordingList.size() + " record(s) found for macId[" + macId + "]" );
    }
}
