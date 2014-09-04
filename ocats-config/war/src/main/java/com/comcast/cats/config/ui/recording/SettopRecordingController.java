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

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.config.ui.SettopCreateEvent;
import com.comcast.cats.config.ui.SettopDeleteEvent;
import com.comcast.cats.config.ui.SettopEditEvent;
import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.config.ui.SlotConnectionBean;
import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Controller for recordings list view.
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
@SessionScoped
public class SettopRecordingController
{
    @Inject
    SettopSlotConfigService              settopService;
    @Inject
    SettopRecordingService               settopRecordingService;

    private SettopRecordingBean[]        selectedRecordings;
    private SettopRecordingBean          selectedRecording;
    private List< SettopRecordingBean >  recordings                   = new ArrayList< SettopRecordingBean >();
    private MediaInfoBean                mediaToPlay;
    private String                       aliasName;

    private static String                 masterRecordDirectory        = null;
    public static String                 relativeFilePathForRecording = "";
    private static final String          RECORDING_PROPERTIES_FILE    = "recorder.properties";

    private static Logger                logger                       = LoggerFactory.getLogger( SettopRecordingController.class );

    private static final String          PVR_HTTP_SERVER_BASE_PATH    = "pvr.http.server.base.path";

    private static final RecordingStatus UNKNOWN_STATUS;
    
    private SettopRecordingBean selectedRecordingHistory;
    private SettopRecordingBean[] selectedRecordingHistoryList ;

	static
    {
        Properties prop = new Properties();
        try
        {
            prop.load( SettopRecordingController.class.getClassLoader().getResourceAsStream( RECORDING_PROPERTIES_FILE ) );
            relativeFilePathForRecording = ( String ) prop.get( PVR_HTTP_SERVER_BASE_PATH );
            logger.info( "relativeFilePathForRecording " + relativeFilePathForRecording );
            // check if masterDirectory has been explicitly set already
            if ( masterRecordDirectory == null && AuthController.getHostAddress() != null )
            {
                masterRecordDirectory = "http://" + AuthController.getHostAddress() + relativeFilePathForRecording;
            }
        }
        catch ( FileNotFoundException e )
        {
            logger.error( "Properties file not found or could not be loaded " + e.getMessage() );
        }
        catch ( IOException e )
        {
            logger.error( "Properties file not found or could not be loaded " + e.getMessage() );
        }
        catch ( Exception e )
        {
            logger.error( "Properties file not found or could not be loaded " + e.getMessage() );
        }

        UNKNOWN_STATUS = new RecordingStatus();
        UNKNOWN_STATUS.setState( VideoRecorderState.UNKNOWN.name() );
    }
    
    @PostConstruct
    public void getRecordings(){
        refreshRecordingsListing();
    }

    public synchronized List< SettopRecordingBean > getSettopRecordingStatusList()
    {
        return recordings;
    }
    
    public void handleSettopEditEvent( @Observes SettopEditEvent settopEditEvent )
    {
        refreshRecordingsListing(); // refresh on view should see updated changes.
    }
    
    public void handleSettopCreatedEvent( @Observes SettopCreateEvent settopCreatedEvent )
    {
        refreshRecordingsListing(); // refresh on view should see updated changes.
    }
    
    public void handleSettopDeletedEvent( @Observes SettopDeleteEvent settopDeletedEvent )
    {
        refreshRecordingsListing(); // refresh on view should see updated changes.
    }
    

    private synchronized void refreshRecordingsListing()
    {
        logger.trace( "refreshRecordingsListing " );

        List< SlotConnectionBean > slotConnections = settopService.getAllConnectedSlots();
        List<SettopRecordingBean> recordingsList = new ArrayList< SettopRecordingBean >();

        for ( SlotConnectionBean slotConnection : slotConnections )
        {
            VideoRecorderResponse response = null;
            SettopRecordingBean recording = new SettopRecordingBean();
            try
            {
                response = settopRecordingService.getRecordingDetails( slotConnection.getSlot(), slotConnection
                        .getSettop().getHostMacAddress() );
            }
            catch ( Exception e )
            {
                logger.warn( "Refresh recording error happened. Maybe video recorder service is down " + e.getMessage() );
            }
            if ( response != null && response.getResult() != WebServiceReturnEnum.FAILURE )
            {
                recording.setStatus( response.getRecording().getRecordingStatus() );
                recording.setMediaMetaData( response.getRecording().getMediaInfoEntityList() );
                recording.setCreatedTime( response.getRecording().getCreatedTime() );
                recording.setName( response.getRecording().getName() );
                recording.setId( response.getRecording().getId() );
            }
            else
            {
                recording.setStatus( UNKNOWN_STATUS );
            }

            recording.setSettop( slotConnection.getSettop() );
            recording.setSlot( slotConnection.getSlot() );
            recordingsList.add( recording );
        }
        recordings = recordingsList;
    }

    private List< SettopRecordingBean > convertRecordingHistory( List< Recording > recordinghistory )
    {
        List< SettopRecordingBean > previousRecordings = new ArrayList< SettopRecordingBean >();
        if ( recordinghistory != null )
        {
            for ( int i = recordinghistory.size()-1; i >=0 ; i-- ) //quick and simple solution to ensure latest recording is at top.
            {
                Recording historyRecord = recordinghistory.get( i );
                SettopRecordingBean pastRecording = new SettopRecordingBean();
                pastRecording.setStatus( historyRecord.getRecordingStatus() );
                pastRecording.setMediaMetaData( historyRecord.getMediaInfoEntityList() );
                pastRecording.setCreatedTime( historyRecord.getCreatedTime() );
                pastRecording.setName( historyRecord.getName() );
                pastRecording.setId( historyRecord.getId() );
                previousRecordings.add( pastRecording );
            }
        }
        return previousRecordings;
    }

    public SettopRecordingBean[] getSelectedRecordings()
    {
        return selectedRecordings;
    }

    public void setSelectedRecordings( SettopRecordingBean[] selectedRecordings )
    {
        this.selectedRecordings = selectedRecordings;
    }

    public String start( SettopRecordingBean recording )
    {
        logger.trace( "Start Recording : " + recording.getSettop() );

        Slot slot = recording.getSlot();
        VideoRecorderResponse response = null;
        try
        {
            response = settopRecordingService.startRecording( slot, recording.getSettop().getHostMacAddress(),aliasName );
        }
        catch ( Exception e )
        {
            logger.warn( "Start recording error happened. Maybe video recorder service is down " + e.getMessage() );
        }
        handleResponse( response, recording );
        setMessage( recording, recording.getSettop().getName(), "Started Recording" );

        aliasName=null; //reset view
        
        // POST-Redirect-GET pattern
        // http://balusc.blogspot.com/2007/03/post-redirect-get-pattern.html
         return "/recording/List.xhtml?faces-redirect=true";
    }

    private void handleResponse( VideoRecorderResponse response, SettopRecordingBean recording )
    {
        RecordingStatus status;
        status = new RecordingStatus();
        status.setState( VideoRecorderState.ERROR.name() );
        if ( response == null || response.getResult() == WebServiceReturnEnum.FAILURE )
        {

            if ( response == null )
            {
                status.setMessage( "Error. Could not reach server" );
                recording.setStatus( status );
            }
            else
            {
                status.setMessage( response.getMessage() );
                if ( response.getRecording() != null )
                {
                    recording.setStatus( response.getRecording().getRecordingStatus() );
                }
                else
                {
                    recording.setStatus( status );
                }
            }
        }
        else
        {
            recording.setMediaMetaData( response.getRecording().getMediaInfoEntityList() );
            if ( response.getRecording() != null )
            {
                recording.setStatus( response.getRecording().getRecordingStatus() );
            }
            else
            {
                status.setMessage( "Server returned true, but no recording found" );
                recording.setStatus( status );
            }
        }
    }

    private void setMessage( SettopRecordingBean recording, String settopName, String action )
    {
        FacesContext context = FacesContext.getCurrentInstance();

        if ( recording.getState().equals( VideoRecorderState.ERROR.name() ) )
        {
            context.addMessage(
                    null,
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, "ERROR : " + settopName, recording
                            .getStatusMessage() ) );
        }
        else
        {
            context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_INFO, "Success : " + action + " : "
                    + settopName, recording.getStatusMessage() ) );
        }
    }

    public String stop( SettopRecordingBean recording )
    {
        logger.trace( "Stop Recording : " + recording.getSettop() );
        Slot slot = recording.getSlot();
        VideoRecorderResponse response = null;
        try
        {
            response = settopRecordingService.stopRecording( slot, recording.getSettop().getHostMacAddress() );
        }
        catch ( Exception e )
        {
            logger.warn( "Start recording error happened. Maybe video recorder service is down " + e.getMessage() );
        }
        handleResponse( response, recording );

        setMessage( recording, recording.getSettop().getName(), "Stopped Recording" );
        
        // POST-Redirect-GET pattern
        // http://balusc.blogspot.com/2007/03/post-redirect-get-pattern.html
         return "/recording/List.xhtml?faces-redirect=true";
    }

    public MediaInfoBean getMediaToPlay()
    {
        return mediaToPlay;
    }

    public void setMediaToPlay( MediaInfoBean mediaToPlay )
    {
        this.mediaToPlay = mediaToPlay;
    }

    public SettopRecordingBean getSelectedRecording()
    {
        return selectedRecording;
    }

    public void setSelectedRecording( SettopRecordingBean selectedRecording )
    {
        this.selectedRecording = selectedRecording;
        obtainHistoryList(this.selectedRecording);
    }
    
    private void obtainHistoryList(SettopRecordingBean recording )
    {
        try
        {
            List< Recording > recordinghistory = settopRecordingService.getRecordingHistory( recording.getSettop()
                    .getHostMacAddress() );

            recording.setRecordingHistoryList( convertRecordingHistory( recordinghistory ) );
        }
        catch ( Exception e )
        {
            logger.warn( "Retrieving Recording History error happened. Maybe video recorder service is down "
                    + e.getMessage() );
        }
    }

    public String getMasterRecordDirectory()
    {
        if ( masterRecordDirectory == null )
        {
            masterRecordDirectory = "http://" + AuthController.getHostAddress() + relativeFilePathForRecording;
        }
        return masterRecordDirectory;
    }

    public void setMasterRecordDirectory( String masterRecordDirectory )
    {
        logger.trace( "setMasterRecordDirectory : " + masterRecordDirectory );
        SettopRecordingController.masterRecordDirectory = masterRecordDirectory;
    }

    public void recordSelectedSettops()
    {
        logger.trace( "recordSelectedSettops : " + ( selectedRecordings == null ) );
        if ( selectedRecordings != null && selectedRecordings.length > 0 )
        {
            for ( SettopRecordingBean recording : selectedRecordings )
            {
                start( recording );
            }
        }
    }

    public void stopSelectedSettops()
    {
        logger.trace( "stopSelectedSettops : " + ( selectedRecordings == null ) );
        if ( selectedRecordings != null && selectedRecordings.length > 0 )
        {
            for ( SettopRecordingBean recording : selectedRecordings )
            {
                stop( recording );
            }
        }
    }

    public String getAliasName()
    {
        return aliasName;
    }

    public void setAliasName( String aliasName )
    {
        this.aliasName = aliasName;
    }

    public void refreshAll()
    {
        logger.trace( "refreshAll " );
        refreshRecordingsListing();
    }

    public void clearMediaToPlay()
    {
        setMediaToPlay( null );
    }
    
    public void deleteRecording(){
    		deleteRecording(selectedRecordingHistory);
            selectedRecordingHistory = null;
    }
    
    public void deleteRecording(SettopRecordingBean recording){
        if(recording != null){
            VideoRecorderResponse response = null;
            try
            {
                response = settopRecordingService.deleteRecording( recording.getId());
                obtainHistoryList(this.selectedRecording); 
            }
            catch ( Exception e )
            {
                logger.warn( "Delete failed. Maybe video recorder service is down " + e.getMessage() );
            }
            handleDeleteRespone(response);
        }
    }
    
    public void deleteMedia(MediaInfoBean media){
        VideoRecorderResponse response = null;
        try
        {
            response = settopRecordingService.deleteFile( media.getId());
            obtainHistoryList(this.selectedRecording);  
            this.selectedRecording.getMediaInfo().remove(media); //update media list
            System.out.println("Removed media "+selectedRecording.getMediaInfo().size());
        }
        catch ( Exception e )
        {
            logger.warn( "Delete failed. Maybe video recorder service is down " + e.getMessage() );
        }
        handleDeleteRespone(response);
    }
    
    private void handleDeleteRespone(VideoRecorderResponse response){
        FacesContext context = FacesContext.getCurrentInstance();
        if(response != null){
            if(WebServiceReturnEnum.FAILURE.equals( response.getResult())){
                context.addMessage(
                        null,
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, "DELETE ERROR",response.getMessage()) );
            }
        }else{
            context.addMessage(
                    null,
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, "No response from server","Maybe service is down" ));
        }
    }

    public void downloadMedia( MediaInfoBean mediaInfoBean )
    {
        try
        {
            FacesContext ctx = FacesContext.getCurrentInstance();

            ExternalContext ectx = ctx.getExternalContext();
            HttpServletRequest request = ( HttpServletRequest ) ectx.getRequest();
            HttpServletResponse response = ( HttpServletResponse ) ectx.getResponse();

            DataOutputStream out = null;
            if ( request.getParameter( "target" ) != null )
            {
                RequestDispatcher dispatcher = request.getRequestDispatcher( request.getParameter( "target" ) );
                dispatcher.forward( request, response );
            }
            else
            {
                response.setContentType( "APPLICATION/OCTET-STREAM" );
                response.setHeader( "Content-Disposition", "attachment; filename=" + mediaInfoBean.getFileName() );
                BufferedInputStream input = null;
                try
                {
                    out = new DataOutputStream( response.getOutputStream() );
                    input = new BufferedInputStream( new URL( mediaInfoBean.getFilePath() ).openStream() );

                    byte[] buffer = new byte[ 1024 ]; // 1024 buffer size
                    int length;
                    while ( ( length = input.read( buffer ) ) > 0 )
                    {
                        out.write( buffer, 0, length );
                    }
                }
                catch ( Exception e )
                {
                    logger.debug( e.getMessage() );
                }
                finally
                {
                    if ( out != null )
                    {
                        out.flush();
                        out.close();
                    }
                    if(input != null){
                        input.close();
                    }
                }
            }
            ctx.responseComplete();
        }
        catch ( Exception e )
        {
            logger.debug( e.getMessage() );
            e.printStackTrace();
        }
    }

    public SettopRecordingBean getSelectedRecordingHistory()
    {
        return selectedRecordingHistory;
    }

    public void setSelectedRecordingHistory( SettopRecordingBean selectedRecordingHistory )
    {
        this.selectedRecordingHistory = selectedRecordingHistory;
    }
    
    
    public void handleRecordingHistoryDlgClose(CloseEvent event) {
        selectedRecordingHistory = null;
        selectedRecordingHistoryList = null;
    }
    

    public SettopRecordingBean[] getSelectedRecordingHistoryList() {
		return selectedRecordingHistoryList;
	}

	public void setSelectedRecordingHistoryList(
			SettopRecordingBean[] selectedRecordingHistoryList) {
		if(selectedRecordingHistoryList != null && selectedRecordingHistoryList.length > 0){
	    	selectedRecordingHistory = selectedRecordingHistoryList[0];
		}
		this.selectedRecordingHistoryList = selectedRecordingHistoryList;
	}
   
	public void deleteRecordingsList(){
		if(selectedRecordingHistoryList != null && selectedRecordingHistoryList.length > 0){
			for(SettopRecordingBean recording : selectedRecordingHistoryList){
				deleteRecording(recording);
			}
		}
		selectedRecordingHistoryList = null;
		selectedRecordingHistory = null;
	}
	
}
