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
package com.comcast.cats.recorder.domain.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.recorder.VideoRecorderTask;
import com.comcast.cats.recorder.VideoRecordingErrorHandler;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.recorder.exception.VideoRecorderException;
import com.comcast.cats.recorder.exception.VideoRecorderInstantiationException;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.util.HttpClientUtil;
import com.comcast.cats.service.util.VideoRecorderUtil;

/**
 * Default implementation of {@link RecordingEntityService}.
 * 
 * @author SSugun00c
 * 
 */
@Stateless
@Remote( RecordingEntityService.class )
public class DefaultRecordingEntityService extends BaseEntityService implements RecordingEntityService,
        VideoRecordingErrorHandler
{
    @EJB
    private MediaMetaDataEntityService   mediaMetaDataEntityService;

    @EJB
    private RecordingStatusEntityService recordingStatusEntityService;

    @PersistenceContext( unitName = PERSISTENCE_UNIT )
    private EntityManager                entityManager;

    @Override
    public VideoRecorderResponse submitRecording( String macId, String videoServerIp, Integer port, Integer duration,
            String alias ) throws VideoRecorderException
    {
        LOGGER.info( "[EJB3-EM][START][" + macId + "][" + videoServerIp + "][" + port + "][" + alias + "]" );

        VideoRecorderResponse videoRecorderResponse = null;

        videoRecorderResponse = getRecordingStatus( macId );

        Recording recording = null;

        if ( WebServiceReturnEnum.SUCCESS.equals( videoRecorderResponse.getResult() ) )
        {
            recording = videoRecorderResponse.getRecording();

            if ( null != recording )
            {
                if ( ( !isActive( recording ) ) )
                {
                    videoRecorderResponse = startNewRecording( macId, videoServerIp, port, duration, alias );
                }
                else
                {
                    throw new VideoRecorderInstantiationException( "Cannot Submit video record for ["
                            + recording.getStbMacAddress() + "][" + recording.getStbMacAddress() + "]["
                            + recording.getVideoServerPort() + "]. An existing recorder found with state : "
                            + recording.getRecordingStatus().getState() + " videoRecorderId is [" + recording.getId()
                            + "]" );
                }
            }
            else
            {
                throw new VideoRecorderInstantiationException( "Cannot Submit video record for [" + macId
                        + "]. An unknown error happend. An existing recorder found but is NULL" );
            }
        }
        else
        {
            videoRecorderResponse = startNewRecording( macId, videoServerIp, port, duration, alias );
        }

        return videoRecorderResponse;
    }

    private VideoRecorderResponse startNewRecording( String macId, String videoServerIp, Integer port,
            Integer duration, String alias )
    {
        Recording recording = createNewRecording( macId, videoServerIp, port, duration, alias );

        LOGGER.info( "Recording ready for [" + macId + "][" + videoServerIp + "][" + port + "]" );
        LOGGER.trace( recording.toString() );

        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil
                .startRecording( recording );

        if ( WebServiceReturnEnum.SUCCESS.equals( videoRecorderResponse.getResult() ) )
        {
            refreshMediaMetaData( videoRecorderResponse.getRecording() );
        }
        else
        {
            RecordingStatus recordingStatus = new RecordingStatus( VideoRecorderState.ERROR.toString(),
                    "Start record failed. " + videoRecorderResponse.getMessage() );
            recordingStatus.setId( recording.getId() );
            recording.setRecordingStatus( recordingStatus );
            entityManager.merge( recording );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse stopRecordingByMacId( String macId ) throws VideoRecorderException
    {
        LOGGER.info( "[EJB3-EM][STOP-BY-MAC][" + macId + "]" );

        VideoRecorderResponse videoRecorderResponse = null;

        Recording recording = getActiveRecording( macId );

        if ( null == recording )
        {
            throw new RecorderNotFoundException( "No active recording found  for [" + macId + "]" );
        }
        else
        {
            videoRecorderResponse = stopRecordingById( recording.getId() );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse stopRecordingById( Integer recordingId ) throws VideoRecorderException
    {
        LOGGER.info( "[EJB3-EM][STOP-BY-ID][" + recordingId + "]" );

        VideoRecorderResponse videoRecorderResponse = null;

        Recording recording = entityManager.find( Recording.class, recordingId );

        if ( null == recording )
        {
            throw new RecorderNotFoundException( "No active recording found with id [" + recordingId + "]" );
        }
        else
        {
            LOGGER.info( "An active recording found for recordingId [" + recordingId + "]. Trying to stop. " );

            videoRecorderResponse = stopNormalRecording( recording );
        }

        return videoRecorderResponse;
    }

    /**
     * Regular stop recording logic.
     */
    private VideoRecorderResponse stopNormalRecording( Recording recording )
    {
        LOGGER.info( recording.toString() );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        if ( isActive( recording ) )
        {
            videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.stopRecordingByRecordingId( recording
                    .getId() );

            switch ( videoRecorderResponse.getResult() )
            {
            case FAILURE:
                recording.getRecordingStatus().setState( VideoRecorderState.ERROR.name() );
                break;

            case SUCCESS:
            default:
                recording.getRecordingStatus().setState( VideoRecorderState.STOPPED.name() );
                refreshMediaMetaData( recording );
                break;
            }

            recording.getRecordingStatus().setMessage( videoRecorderResponse.getMessage() );
            entityManager.merge( recording );
            videoRecorderResponse.setRecording( recording );
        }
        else
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( "No active recording found  for [" + recording.getStbMacAddress() + "]["
                    + recording.getVideoServerIp() + "][" + recording.getVideoServerPort() + "]" );
        }

        return videoRecorderResponse;
    }

    /**
     * Specific to scheduler logic.
     */
    private void stopScheduledRecording( Recording recording ) throws VideoRecorderException,
            VideoRecorderConnectionException
    {
        if ( ( null != recording ) && ( isActive( recording ) ) )
        {
            LOGGER.info( "Stopping recording recordingId [" + recording.getId() + "] from Scheduler" );

            VideoRecorderResponse videoRecorderResponse = HttpClientUtil.stopRecordingByRecordingId( recording.getId() );

            if ( WebServiceReturnEnum.SUCCESS.equals( videoRecorderResponse.getResult() ) )
            {
                recording.getRecordingStatus().setState( VideoRecorderState.STOPPED.toString() );
                recording.getRecordingStatus().setMessage(
                        "Stop request for recordingId [" + recording.getId()
                                + "] from Scheduler has been submitted successfully at " + new Date() );
                LOGGER.info( "Successfully stopped. Updating recording recordingId [" + recording.getId()
                        + "] from Scheduler" );
                entityManager.merge( recording );
            }
            else
            {
                String errorMessage = "An error happend while submitting stop request for recordingId ["
                        + recording.getId() + "] from Scheduler at " + new Date() + ". "
                        + videoRecorderResponse.getMessage() + ". Scheduler will try to stop [" + recording.getId()
                        + "] in next timeout";

                // DONOT change status .It's still recording
                recording.getRecordingStatus().setMessage( errorMessage );

                LOGGER.error( errorMessage + ". Updating database" );

                entityManager.merge( recording );

                if ( VideoRecorderConnectionException.class.getName().equalsIgnoreCase(
                        videoRecorderResponse.getException() ) )
                {
                    throw new VideoRecorderConnectionException( errorMessage );
                }
                else
                {
                    throw new VideoRecorderException( errorMessage );
                }
            }
        }
        else
        {
            throw new VideoRecorderException( "Cannot stop recording from Scheduler. Invalid recoding state."
                    + recording );
        }
    }

    @Override
    public VideoRecorderResponse getActiveRecordingList()
    {
        LOGGER.trace( "[EJB3-EM][ACTIVE-RECORDING]" );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        // Find all active entries.
        final Query query = entityManager.createNamedQuery( "Recording.findAllActiveRecording" );

        @SuppressWarnings( "unchecked" )
        final List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        LOGGER.trace( recordingList.size() + " active recording(s) found" );

        // Refreshing status.
        for ( Recording recording : recordingList )
        {
            if ( isActive( recording ) )
            {
                refreshRecordingStatus( recording );
            }

            refreshMediaMetaData( recording );
        }

        // Removing completed recordings.
        for ( int index = 0; index < recordingList.size(); index++ )
        {
            Recording temp = recordingList.get( index );

            if ( !isActive( temp ) )
            {
                recordingList.remove( index );
            }
        }

        videoRecorderResponse.setRecordingList( recordingList );

        return videoRecorderResponse;
    }

    private Recording refreshRecordingStatus( Recording recording )
    {
        VideoRecorderResponse videoRecorderResponse = null;

        videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.getStatusByRecordingId( recording.getId() );
        LOGGER.trace( "videoRecorderResponse retrieved from live recordingId for [" + recording.getId() + "] - "
                + videoRecorderResponse );

        if ( null != videoRecorderResponse.getRecording() )
        {
            recording.getRecordingStatus().setId( recording.getId() );
            recording.getRecordingStatus().setMessage(
                    videoRecorderResponse.getRecording().getRecordingStatus().getMessage() );
            recording.getRecordingStatus().setState(
                    videoRecorderResponse.getRecording().getRecordingStatus().getState() );

        }

        entityManager.merge( recording );

        return recording;
    }

    @Override
    public VideoRecorderResponse getRecordingHistory()
    {
        LOGGER.trace( "[EJB3-EM][RECORDING-HISTORY]" );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        final Query query = entityManager.createQuery( "from Recording" );

        @SuppressWarnings( "unchecked" )
        final List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        LOGGER.trace( recordingList.size() + " active recording(s) found" );

        if ( recordingList.size() > 0 )
        {
            LOGGER.trace( "Refrshing status" );
            for ( Recording recording : recordingList )
            {
                if ( isActive( recording ) )
                {
                    refreshRecordingStatus( recording );
                }

                refreshMediaMetaData( recording );
            }
            videoRecorderResponse.setRecordingList( recordingList );
        }
        else
        {
            videoRecorderResponse.setMessage( "No records found" );
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
        }

        videoRecorderResponse.setRecordingList( recordingList );

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse getRecordingHistoryByMac( String macId )
    {
        LOGGER.trace( "[EJB3-EM][RECORDING-HISTORY-BY-MAC]" );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        if ( VideoRecorderUtil.isValidMacId( macId ) )
        {
            final Query query = entityManager.createNamedQuery( "Recording.findRecordingByMacId" );
            query.setParameter( "stbMacAddress", macId );

            @SuppressWarnings( "unchecked" )
            final List< Recording > recordingList = ( List< Recording > ) query.getResultList();

            LOGGER.trace( recordingList.size() + " record(s) found for macId[" + macId + "]" );

            if ( recordingList.size() > 0 )
            {
                LOGGER.trace( "Refrshing status" );
                for ( Recording recording : recordingList )
                {
                    if ( isActive( recording ) )
                    {
                        refreshRecordingStatus( recording );
                    }

                    refreshMediaMetaData( recording );
                }
                videoRecorderResponse.setRecordingList( recordingList );
            }
            else
            {
                videoRecorderResponse.setMessage( "No records found for macId[" + macId + "]" );
                videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            }
        }
        else
        {
            videoRecorderResponse.setMessage( "Invalid macId [" + macId + "]" );
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
        }

        return videoRecorderResponse;
    }

    @Override
    public void handleError( Recording recording, String errorMessage )
    {
        // FIXME
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public VideoRecorderResponse getRecordingStatus( String macId ) throws VideoRecorderException
    {
        LOGGER.trace( "[EJB3-EM][STATUS][" + macId + "]" );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        final Query query = entityManager.createNamedQuery( "Recording.findAllRecordingByMacIdWithLatestFirst" );
        query.setParameter( "stbMacAddress", macId );

        try
        {
            final Recording recording = ( Recording ) query.setMaxResults( 1 ).getSingleResult();

            LOGGER.trace( "Latest recording found for [" + macId + "] - " + recording );
            videoRecorderResponse = checkCurrentStatus( recording );
        }
        catch ( NoResultException e )
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( "No recording information found for [" + macId + "]" );
        }

        return videoRecorderResponse;
    }

    @Override
    public VideoRecorderResponse getRecordingStatusById( Integer recordingId ) throws VideoRecorderException
    {
        LOGGER.trace( "[EJB3-EM][STATUS-BY-ID][" + recordingId + "]" );

        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        final Recording recording = entityManager.find( Recording.class, recordingId );

        if ( null != recording )
        {
            LOGGER.trace( "Recording found with recordingId [" + recordingId + "] - " + recording );
            videoRecorderResponse = checkCurrentStatus( recording );
        }
        else
        {
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( "No recording information found with recordingId [" + recordingId + "]" );
        }

        return videoRecorderResponse;

    }

    private VideoRecorderResponse checkCurrentStatus( Recording recording )
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        if ( isActive( recording ) )
        {
            videoRecorderResponse = ( VideoRecorderResponse ) HttpClientUtil.getStatusByRecordingId( recording.getId() );

            LOGGER.trace( "videoRecorderResponse retrieved from live recording - " + videoRecorderResponse );

            recording.getRecordingStatus().setId( recording.getId() );
            recording.getRecordingStatus().setMessage(
                    videoRecorderResponse.getRecording().getRecordingStatus().getMessage() );
            recording.getRecordingStatus().setState(
                    videoRecorderResponse.getRecording().getRecordingStatus().getState() );

            entityManager.merge( recording );
        }

        refreshMediaMetaData( recording );
        videoRecorderResponse.setRecording( recording );

        return videoRecorderResponse;
    }

    @Override
    public void submitScheduledRecording( Integer recordingId ) throws VideoRecorderException,
            VideoRecorderConnectionException
    {
        LOGGER.info( "[EJB3-SCHEDULED-RECORDING] - [" + recordingId + "]" );

        Recording recording = null;

        try
        {
            // Lookup recording
            recording = getActiveRecordingByRecordingId( recordingId );

            LOGGER.info( "[EJB3-SCHEDULED-RECORDING] Active Recording found with recording [" + recordingId + "]" );

            if ( null != recording )
            {
                LOGGER.info( "[EJB3-SCHEDULED-RECORDING] Trying to stop recording [" + recordingId + "]" );

                // Stop current recording. And update database.
                stopScheduledRecording( recording );

                // Wait for some time to actually stop current recording.
                Thread.sleep( VideoRecorderTask.DEFAULT_SLEEP_DURATION + 1000 );

                LOGGER.info( "[EJB3-SCHEDULED-RECORDING] Reinitializing recording [" + recordingId + "]" );
                // Reinitialize and start recording.
                reinitializeRecording( recording );
            }
        }
        catch ( InterruptedException e )
        {
            LOGGER.warn( "SubmitScheduledRecording thread received an InterruptedException. " + e.getMessage()
                    + ". Reinitializing recording [" + recordingId + "]" );

            // Ignore InterruptedException. Reinitialize and start
            // recording.
            reinitializeRecording( recording );
        }
    }

    private void reinitializeRecording( Recording recording )
    {
        LOGGER.info( "Attaching new MetaData file for recordingId [" + recording.getId() + "]" );

        // Create new MetaData file

        MediaMetaData newMediaMetaData = null;

        if ( ( null == recording.getName() ) || ( recording.getName().isEmpty() ) )
        {
            String absolteFilepath = VideoRecorderUtil.getFilePath( recording.getStbMacAddress(),
                    recording.getVideoServerIp(), recording.getVideoServerPort(), null, 0 );

            newMediaMetaData = new MediaMetaData( absolteFilepath, VideoRecorderUtil.getHttpPath( absolteFilepath ) );
        }
        else
        {
            // get initial metadata
            MediaMetaData initialMediaMetaData = recording.getMediaInfoEntityList().get( 0 );
            int fileCount = recording.getMediaInfoEntityList().size() + 1;
            String absolteFilepath = ( VideoRecorderUtil.getSubsequentFile( initialMediaMetaData.getFilePath(),
                    fileCount ) ).getAbsolutePath().trim();

            newMediaMetaData = new MediaMetaData( absolteFilepath, VideoRecorderUtil.getHttpPath( absolteFilepath ) );
        }
        // Add new MetaData file to the recording
        newMediaMetaData.setRecordingEntity( recording );
        recording.getMediaInfoEntityList().add( newMediaMetaData );

        // Set duration for scheduled recording
        recording
                .setRequestedDuration( VideoRecorderServiceConstants.DEFAULT_ROLLING_FILE_RECORDING_DURATION_IN_SECONDS );

        LOGGER.info( "Updating recording recordingId [" + recording.getId() + "] with new MetaData file" );

        // Start New recording
        LOGGER.info( "Reinitializing recording recordingId [" + recording.getId() + "]" );
        // Reinitialize
        recording.getRecordingStatus().setState( VideoRecorderState.INITIALIZING.toString() );
        recording.getRecordingStatus().setMessage(
                "Start record request submitted for [" + recording.getId() + "] from Scheduler at " + new Date() );

        VideoRecorderResponse recorderResponse = HttpClientUtil.startRecording( recording );

        if ( WebServiceReturnEnum.FAILURE.equals( recorderResponse.getResult() ) )
        {
            LOGGER.error( "Reinitialize recording failed. " + recorderResponse.getMessage() );
            recording.getRecordingStatus().setState( VideoRecorderState.ERROR.toString() );
            recording.getRecordingStatus().setMessage(
                    "Reinitialize recording failed for [" + recording.getId() + "] from Scheduler at " + new Date() );
        }

        LOGGER.info( "Updating recording recordingId [" + recording.getId() + "]" );
        // Update Database
        entityManager.merge( recording );

        LOGGER.info( "[Updated Recording] - [" + recording + "]" );
    }

    private Recording getActiveRecordingByRecordingId( Integer recordingId ) throws VideoRecorderException
    {
        try
        {
            LOGGER.info( "Looking up recording with recordingId [" + recordingId + "]" );
            final Query query = entityManager.createNamedQuery( "Recording.findActiveRecordingByRecordingId" );
            query.setParameter( "recordingId", recordingId );

            return ( Recording ) query.getSingleResult();
        }
        catch ( NoResultException e )
        {
            LOGGER.info( "No active recording found with recordingId [" + recordingId + "]" );
            throw new VideoRecorderException( "No active recording found with recordingId [" + recordingId + "]" );
        }
    }

    private Recording createNewRecording( String macId, String videoServerIp, Integer port, Integer duration,
            String alias )
    {
        LOGGER.info( "Creating new recorder for [" + macId + "][" + videoServerIp + "][" + port + "]" );

        if ( ( null == port ) || ( port <= 0 ) )
        {
            port = VideoRecorderServiceConstants.DEFAULT_PORT;
            LOGGER.info( "Using default port [" + VideoRecorderServiceConstants.DEFAULT_PORT + "] for [" + macId + "]["
                    + videoServerIp + "][" + port + "]" );
        }

        if ( ( null == duration ) || ( duration <= 0 ) )
        {
            duration = VideoRecorderServiceConstants.DEFAULT_RECORDING_DURATION;
            LOGGER.info( "Using default duration [" + VideoRecorderServiceConstants.DEFAULT_RECORDING_DURATION
                    + "] for [" + macId + "][" + videoServerIp + "][" + port + "]" );
        }

        // Add to recordings
        Recording newRecording = new Recording( macId, videoServerIp, port, VideoRecorderUtil.getMrl( macId,
                videoServerIp, port ) );
        newRecording.setRequestedDuration( duration );
        newRecording.setName( alias );

        RecordingStatus recordingStatus = new RecordingStatus( VideoRecorderState.INITIALIZING.toString(),
                "Recorder is initializing. This process may take few seconds to complete." );
        // Add to recording status
        newRecording.setRecordingStatus( recordingStatus );

        List< MediaMetaData > mediaMetaDataList = new LinkedList< MediaMetaData >();
        int fileCount = 1;
        String absolteFilepath = VideoRecorderUtil.getFilePath( macId, videoServerIp, port, alias, fileCount );
        MediaMetaData media = new MediaMetaData( absolteFilepath, VideoRecorderUtil.getHttpPath( absolteFilepath ) );
        media.setRecordingEntity( newRecording );
        mediaMetaDataList.add( media );

        // Add to Media Info
        newRecording.setMediaInfoEntityList( mediaMetaDataList );

        entityManager.persist( newRecording );

        return newRecording;
    }

    private Recording getActiveRecording( String macId )
    {
        Recording recording = null;

        final Query query = entityManager.createNamedQuery( "Recording.findActiveRecordingByMacId" );
        query.setParameter( "stbMacAddress", macId );

        try
        {
            recording = ( Recording ) query.getSingleResult();
            LOGGER.info( "An active recording found for macId [" + macId + "]. Trying to stop. " );
        }
        catch ( NoResultException e )
        {
            LOGGER.warn( "No active recording found for [" + macId + "]" );
        }

        return recording;
    }

    @Override
    public void deleteById( Integer recordingId ) throws RecorderNotFoundException
    {
        Recording recording = entityManager.find( Recording.class, recordingId );

        if ( null == recording )
        {
            throw new RecorderNotFoundException( "No recording found with id [" + recordingId + "]" );
        }
        else
        {
            if ( isActive( recording ) )
            {
                stopNormalRecording( recording );
            }

            mediaMetaDataEntityService.deleteByRecordingId( recordingId );
            recordingStatusEntityService.deleteByRecordingId( recordingId );
            entityManager.remove( recording );
        }
    }

    @Override
    public Integer deleteAllByMacId( String macId )
    {
        int count = 0;

        Query query = entityManager.createNamedQuery( "Recording.findAllByMacId" );

        query.setParameter( "stbMacAddress", macId );

        @SuppressWarnings( "unchecked" )
        List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        for ( Recording recording : recordingList )
        {
            try
            {
                deleteById( recording.getId() );
                count++;
            }
            catch ( RecorderNotFoundException e )
            {
                LOGGER.error( "Failed to delete recording [" + recording + "]" );
            }

        }

        return count;
    }

    @Override
    public Integer deleteByAlias( String alias, String macId ) throws RecorderNotFoundException
    {
        if ( ( null == alias ) || ( alias.isEmpty() ) )
        {
            throw new RecorderNotFoundException( "alias is empty" );
        }

        int count = 0;

        Query query = entityManager.createNamedQuery( "Recording.findByMacIdAndAlias" );

        query.setParameter( "stbMacAddress", macId );
        query.setParameter( "name", alias );

        @SuppressWarnings( "unchecked" )
        List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        for ( Recording recording : recordingList )
        {
            try
            {
                deleteById( recording.getId() );
                count++;
            }
            catch ( RecorderNotFoundException e )
            {
                LOGGER.error( "Failed to delete recording [" + recording + "]" );
            }

        }

        return count;

    }

    @Override
    public Integer deleteAllBefore( Date createdDate )
    {
        int count = 0;

        Query query = entityManager.createNamedQuery( "Recording.findBeforeCreatedTime" );

        query.setParameter( "createdTime", createdDate );

        @SuppressWarnings( "unchecked" )
        List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        for ( Recording recording : recordingList )
        {
            try
            {
                deleteById( recording.getId() );
                count++;
            }
            catch ( RecorderNotFoundException e )
            {
                LOGGER.error( "Failed to delete recording [" + recording + "]" );
            }

        }
        return count;
    }

    // For Production
    @Schedule( persistent = false, minute = "*/60", hour = "*" )
    // For Development
    // @Schedule( persistent = false, second = "*/60", minute = "*/5", hour =
    // "*" )
    public void onRecordingTimeout()
    {
        LOGGER.info( "--------- Recording scheduler receievd a timeout at " + new Date() + " ---------" );
        VideoRecorderResponse videoRecorderResponse = getActiveRecordingList();
        List< Recording > recordings = videoRecorderResponse.getRecordingList();

        if ( null != recordings )
        {
            LOGGER.info( recordings.size() + " recording(s) found" );

            for ( Recording recording : recordings )
            {
                try
                {
                    LOGGER.info( "Initiating scheduled recording for recordingId " + recording.getId() );
                    submitScheduledRecording( recording.getId() );
                }
                catch ( VideoRecorderException videoRecorderException )
                {
                    LOGGER.error( "Failed to submit scheduled recording for recordingId " + recording.getId() + " at "
                            + new Date() + " due to videoRecorderException. " + videoRecorderException.getMessage() );

                }
                catch ( VideoRecorderConnectionException videoRecorderConnectionException )
                {
                    LOGGER.error( "Failed to submit scheduled recording for recordingId " + recording.getId() + " at "
                            + new Date() + " due to videoRecorderConnectionException. "
                            + videoRecorderConnectionException.getMessage()
                            + ". The scheduler will try submit scheduled recording in the next timeout" );
                }
            }
        }
    }
}
