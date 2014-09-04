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
package com.comcast.cats.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.service.RecordingEntityService;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;
import com.comcast.cats.recorder.exception.VideoRecorderException;

/**
 * Controller bean for recording.
 * 
 * @author SSugun00c
 * 
 */
@ManagedBean
@RequestScoped
public class RecordingController implements Converter
{
    private final Logger           LOGGER          = LoggerFactory.getLogger( getClass() );

    @EJB
    private RecordingEntityService recordingEntityService;

    @ManagedProperty( value = "#{mediaMetaData}" )
    private MediaMetaData          mediaMetaData;

    private List< Recording >      activeRecording = null;

    private List< Recording >      selectedRecording;

    public RecordingController()
    {
    }

    @PostConstruct
    public void init()
    {
        activeRecording = recordingEntityService.getActiveRecordingList().getRecordingList();
    }

    @Override
    public Object getAsObject( FacesContext arg0, UIComponent arg1, String arg2 )
    {
        return null;
    }

    @Override
    public String getAsString( FacesContext arg0, UIComponent arg1, Object arg2 )
    {
        return null;
    }

    public void start( Recording recording )
    {
        LOGGER.info( "[WEB][START][" + recording + "]" );

        try
        {
            VideoRecorderResponse videoRecorderResponse = null;

            videoRecorderResponse = recordingEntityService.submitRecording( recording.getStbMacAddress(),
                    recording.getVideoServerIp(), recording.getVideoServerPort(), recording.getRequestedDuration(),
                    recording.getName() );

            switch ( videoRecorderResponse.getResult() )
            {
            case FAILURE:
                FacesContext.getCurrentInstance().addMessage( null,
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, videoRecorderResponse.getMessage(), null ) );
                break;

            case SUCCESS:
            default:
                FacesContext.getCurrentInstance().addMessage( null,
                        new FacesMessage( FacesMessage.SEVERITY_INFO, videoRecorderResponse.getMessage(), null ) );
                break;
            }
        }
        catch ( Exception e )
        {
            FacesContext.getCurrentInstance().addMessage( null,
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(), null ) );
        }
    }

    public List< Recording > getActiveRecordingsList()
    {
        LOGGER.trace( "[WEB][LIST ACTIVE]" );
        return activeRecording;
    }

    public void stop( Integer recordingId )
    {
        LOGGER.info( "[WEB][STOP][" + recordingId + "]" );
        try
        {
            VideoRecorderResponse videoRecorderResponse = recordingEntityService.stopRecordingById( recordingId );

            switch ( videoRecorderResponse.getResult() )
            {
            case FAILURE:
                FacesContext.getCurrentInstance().addMessage( null,
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, videoRecorderResponse.getMessage(), null ) );
                break;

            case SUCCESS:
            default:
                FacesContext.getCurrentInstance().addMessage( null,
                        new FacesMessage( FacesMessage.SEVERITY_INFO, videoRecorderResponse.getMessage(), null ) );
                break;
            }
        }
        catch ( VideoRecorderException e )
        {
            FacesContext.getCurrentInstance().addMessage( null,
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(), null ) );
        }
    }

    public List< Recording > getRecordingHistory()
    {
        LOGGER.trace( "[WEB][RECORDING HISTORY]" );
        return recordingEntityService.getRecordingHistory().getRecordingList();
    }

    public void playMedia( Recording recording )
    {
        LOGGER.info( "[WEB][PLAY MEDIA]" );
        LOGGER.info( recording.toString() );

        String httpPath = recording.getMediaInfoEntityList().get( 0 ).getHttpPath();
        String filePath = recording.getMediaInfoEntityList().get( 0 ).getFilePath();
        double size = recording.getMediaInfoEntityList().get( 0 ).getSize();

        LOGGER.info( "[File Path][" + filePath + "], [Actual Http Path][" + httpPath + "], [size][" + size + "]" );

        String host = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getServerName();
        LOGGER.info( "[Host][" + host + "]" );

        httpPath = httpPath.replace( VideoRecorderServiceConstants.LOCALHOST_IP, host );
        httpPath = httpPath.replace( VideoRecorderServiceConstants.LOCALHOST_NAME, host );

        LOGGER.info( "[Enhanced Http Path][" + httpPath );

        mediaMetaData.setHttpPath( httpPath );
        mediaMetaData.setFilePath( filePath );
        mediaMetaData.setSize( size );
    }

    public void delete( Recording recording )
    {
        LOGGER.info( "[WEB][DELETE MEDIA][" + recording.getId() + "]" );

        try
        {
            recordingEntityService.deleteById( recording.getId() );
            FacesContext.getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage( FacesMessage.SEVERITY_INFO, "Recording [" + recording.getId()
                                    + "] deleted", null ) );
        }
        catch ( RecorderNotFoundException recorderNotFoundException )
        {
            FacesContext.getCurrentInstance().addMessage( null,
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, recorderNotFoundException.getMessage(), null ) );
        }
    }

    public void deleteSelected()
    {
        LOGGER.info( "[WEB][DELETE SELECTED][" + selectedRecording + "]" );
    }

    public void setMediaMetaData( MediaMetaData mediaMetaData )
    {
        this.mediaMetaData = mediaMetaData;
    }

    public List< Recording > getSelectedRecording()
    {
        return selectedRecording;
    }

    public void setSelectedRecording( List< Recording > selectedRecording )
    {
        this.selectedRecording = selectedRecording;
    }

}
