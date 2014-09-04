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
package com.comcast.cats.config.ui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.client.ProxyFactory;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.recording.SettopRecordingService;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.keymanager.domain.Remote;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.SettopType;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.local.domain.TraceType;
import com.comcast.cats.service.KeyManagerConstants;
import com.comcast.cats.service.KeyManagerProxy;

@ManagedBean
@SessionScoped
public class SettopController implements Converter
{
    public static final String     TRACE_TYPE_KEY  = "Trace_Type";
    private static final String    MAC_ID_REGEX    = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";

    private List< Slot >           listableSlots   = new ArrayList< Slot >();
    private SlotConnectionBean[]   selectedSlotConnections;
    private SlotConnectionBean     selectedSlotConnection;
    Rack                           selectedRack;
    HtmlSelectOneMenu              slotSelectOneMenu;
    HtmlSelectOneMenu              rackSelectOneMenu;
    List<SlotConnectionBean>       allConnectedSlots;
    
    boolean                        deleteRecordings = false;

    @Inject
    SettopSlotConfigService        settopSlotConfigService;
    @Inject
    RackService                    rackService;

    private static KeyManagerProxy keyManager      = null;

    static Logger                         logger          = LoggerFactory.getLogger( SettopController.class );

    @Inject
    SettopRecordingService               settopRecordingService;
    
    @Inject 
    Event< SettopCreateEvent > settopCreatedNotifier;
    @Inject 
    Event< SettopEditEvent > settopEditedNotifier;
    @Inject 
    Event< SettopDeleteEvent > settopDeletedNotifier;

    
    static{
        if(keyManager == null){
            String hostname = System.getProperty( KeyManagerConstants.KEY_MANAGER_PROXY_IP_NAME );
                try{
                    if(hostname == null || hostname.isEmpty()){
                        hostname = AuthController.getHostAddress();
                    }
                    logger.info( "KeyManagerProxyProviderRest get() "+hostname );
                    if(hostname != null){
                        keyManager = ProxyFactory.create(KeyManagerProxy.class, "http://"+hostname+"/keymanager-service"+KeyManagerConstants.APPLICATION_PATH+KeyManagerConstants.KEYMANAGER_PATH+"/");
                    }else{
                        logger.warn( "System property "+KeyManagerConstants.KEY_MANAGER_PROXY_IP_NAME+" may not be set properly" );
                    }
                    logger.info( "KeyManagerProxyProviderRest keyManagerProxy "+keyManager );
                }catch(Exception e){
                    logger.warn( e.getMessage() );
                }
            }
        }
    
    public SettopController()
    {
    }
    
    @PostConstruct
    public void init(){
        allConnectedSlots = settopSlotConfigService.getAllConnectedSlots();
    }

    public List< SlotConnectionBean > getAllConnectedSlots()
    {
        return allConnectedSlots;
    }

    public List< Slot > getListableSlots()
    {
        return listableSlots;
    }

    public void setListableSlots( List< Slot > listableSlots )
    {
        this.listableSlots = listableSlots;
    }

    public void rowEdited( RowEditEvent evt )
    {
        if ( evt.getObject() instanceof SlotConnectionBean )
        {
            SlotConnectionBean editedSlotConnection = ( SlotConnectionBean ) evt.getObject();
            if ( editedSlotConnection.getSlot() == null )
            {
                editedSlotConnection.setRack( null );
            }
            saveSlotConnection( editedSlotConnection );

            settopEditedNotifier.fire( new SettopEditEvent(editedSlotConnection.getSettop() ) );
            
            //clear the form after successfully saving.
            editedSlotConnection.setSettop( null );
            editedSlotConnection.setRack( null );
            editedSlotConnection.setSlot( null );
        }
    }
    
    public void onCancel( RowEditEvent evt ){
        if ( evt.getObject() instanceof SlotConnectionBean )
        {
            SlotConnectionBean editedSlotConnection = ( SlotConnectionBean ) evt.getObject();
            settopSlotConfigService.refresh();
            editedSlotConnection = settopSlotConfigService.getSlotConnection( editedSlotConnection.getSettop().getId() );
        }
    }

    public String saveSlotConnection( SlotConnectionBean slotConnection, SettopDesc settop )
    {
        if ( settop.getId() == null )
        {
            settop.setId( UUID.randomUUID().toString() );
        }
        slotConnection.setSettop( settop );
        saveSlotConnection( slotConnection );

        clearFormValues( settop );
        settop = null;

        settopCreatedNotifier.fire( new SettopCreateEvent( settop ) );

        // POST-Redirect-GET pattern
        // http://balusc.blogspot.com/2007/03/post-redirect-get-pattern.html
        return "/settop/List.xhtml?faces-redirect=true";
    }

    private void clearFormValues( SettopDesc settop )
    {
        settop.setName( null );
        settop.setHostMacAddress( null );
        settop.setModel( null );
        settop.setManufacturer( null );
        settop.setUnitAddress( null );
        settop.setMcardMacAddress( null );
        settop.setMCardSerialNumber( null );
        settop.setContent( null );
        settop.setRemoteType( null );
        settop.setHostIp4Address( null );
        settop.setId( null );
        settop.setExtraProperties( null );
        settop.setSerialNumber( null );

    }

    public void saveSlotConnection( SlotConnectionBean slotConnection )
    {
        if ( slotConnection.getSettop().getId() == null )
        {
            slotConnection.getSettop().setId( UUID.randomUUID().toString() );
        }

        settopSlotConfigService.saveSlotConnection( slotConnection );
        
        refresh();
        
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage();
        if ( context != null )
        {
            msg.setSummary( "Settop " + slotConnection.getSettop().getName() + " saved successfully" );
            msg.setSeverity( FacesMessage.SEVERITY_INFO );
            context.addMessage( "response", msg );
            logger.trace( "slotConnection saved " + slotConnection );
        }

    }

    public synchronized void deleteSettopAndSlotConnection()
    {
        if(selectedSlotConnection != null){
            settopRecordingService.stopRecording( selectedSlotConnection.getSlot(), selectedSlotConnection.getSettop().getHostMacAddress() );
            settopSlotConfigService.deleteSettopAndConnection( selectedSlotConnection);
            settopDeletedNotifier.fire( new SettopDeleteEvent( selectedSlotConnection.getSettop() ) );
            if(deleteRecordings){
                Thread delThread = new Thread(new DeleteRecordingsJob(selectedSlotConnection.getSettop().getHostMacAddress()));
                delThread.start();
            }
        }
        
        refresh();
        deleteRecordings = false;
    }

    public SlotConnectionBean[] getSelectedSlotConnections()
    {
        return selectedSlotConnections;
    }

    public void setSelectedSlotConnections( SlotConnectionBean[] selectedSlotConnections )
    {
        this.selectedSlotConnections = selectedSlotConnections;
    }
    
    public void setSelectedSlotConnection ( SlotConnectionBean selectedSlotConnection )
    {
        this.selectedSlotConnection = selectedSlotConnection;
    }
    
    public void clearSlotConnectionSelection ( )
    {
        this.selectedSlotConnection = null;
    }

    /**
     * Converter for slot for selectOneMenu
     */
    @Override
    public Object getAsObject( FacesContext context, UIComponent comp, String value )
    {
        Slot slot = null;
        if ( !value.isEmpty() )
        {
            String rackName = value.substring( 0, value.indexOf( "--" ) ).trim();
            String slotNumber = value.substring( value.indexOf( "--" ) + 2 ).trim();
            slot = rackService.findSlotByRack( rackName, Integer.parseInt( slotNumber ) );
        }
        logger.trace( "value " + value + " Slot " + slot );
        return slot;
    }

    /**
     * Converter for slot for selectOneMenu
     */
    @Override
    public String getAsString( FacesContext context, UIComponent comp, Object object )
    {
        String retVal = "";
        if ( object instanceof Slot )
        {
            Slot slot = ( Slot ) object;
            retVal = slot.getRack().getName() + " -- " + slot.getNumber().toString();
            logger.trace( "Slot " + slot + " value " + retVal );
        }
        return retVal;
    }

    /**
     * Validator for mac address name.
     * 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void validateMacAddress( FacesContext context, UIComponent component, Object value )
            throws ValidatorException
    {

        String macAddress = ( String ) value;
        if ( selectedSlotConnection == null || !macAddress.equals( selectedSlotConnection.getSettop().getHostMacAddress() ) )
        {
            if(!macAddress.isEmpty()){
                if ( !macAddress.matches( MAC_ID_REGEX ) || settopSlotConfigService.isMacAlreadyUsed( macAddress ) )
                {
                    logger.debug( "macAddress not valid " + macAddress + " : is already available?? :"
                            + settopSlotConfigService.isMacAlreadyUsed( macAddress ) );
                    throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, null, "Not valid" ) );
                }
            }
        }
    }

    /**
     * Validator for settop name.
     * 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void validateName( FacesContext context, UIComponent component, Object value ) throws ValidatorException
    {

        String name = ( String ) value;
        if ( selectedSlotConnection == null || !name.equals( selectedSlotConnection.getSettop().getName() ) )
        {
            if ( name.isEmpty() || settopSlotConfigService.isSettopNameAlreadyUsed( name ) )
            {
                logger.debug( "Name not valid " + name + " : is already available?? :"
                        + settopSlotConfigService.isSettopNameAlreadyUsed( name ) );
                throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, null, "Not valid" ) );
            }
        }
    }

    public List< String > getAvailableRemoteTypes() throws ValidatorException
    {
        List< String > remotesTypes = new ArrayList< String >();
        if ( keyManager != null )
        {
            try{
                List< Remote > remotes = keyManager.remotes();
                for ( Remote remote : remotes )
                {
                    remotesTypes.add( remote.getName() );
                }
            }catch(Exception e){
                logger.warn( "Could not retrieve remote types "+e.getMessage() );
            }
        }
        return remotesTypes;
    }

    public void launchCATSVision( SettopDesc settop )
    {
        FacesContext ctx = null;
        try
        {
            ctx = FacesContext.getCurrentInstance();
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
                String macAddress = "";
                if ( settop != null && settop.getHostMacAddress() != null )
                {
                    macAddress = settop.getHostMacAddress();
                }
                response.setHeader( "Content-Disposition", "attachment; filename=CATSVision_" + macAddress
                        + ".jnlp" );
                response.setContentType( "application/x-java-jnlp-file; charset=UTF-8" );
                try
                {
                    logger.debug( "Download CATSVision jnlp template from " + request.getServerName() );
                    String jnlpText = readURL( "http://" + request.getServerName() + "/webstart/catsvision.template" );
                    jnlpText = jnlpText.replaceAll( "<host>", request.getServerName() );
                    jnlpText = jnlpText.replaceAll( "<mac>", macAddress );
                    out = new DataOutputStream( response.getOutputStream()  );
                    out.writeBytes( jnlpText );
                }
                catch ( Exception e )
                {
                    logger.debug( e.getMessage() );
                    e.printStackTrace();
                }
                finally
                {
                    if ( out != null )
                    {
                        out.flush();
                        out.close();
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

    private String readURL( String urlString )
    {
        URL url;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader r = null;
        StringBuilder str = new StringBuilder();
        try
        {
            url = new URL( urlString );
            is = url.openStream();
            isr = new InputStreamReader( is );
            r = new BufferedReader( isr );
            String tempString = "";
            do
            {
                tempString = r.readLine();
                if ( tempString != null )
                {
                    str.append( tempString + "\n" );
                }
            } while ( tempString != null );
        }
        catch ( MalformedURLException e )
        {
            logger.debug( e.getMessage() );
        }
        catch ( IOException e )
        {
            logger.debug( e.getMessage() );
        }
        finally{
            if(r != null){
                try
                {
                    r.close();
                }
                catch ( IOException e )
                {
                    logger.error( "Could not close BufferedReader "+e.getMessage() );
                }
            }
        }
        return str.toString();
    }

    public void handleRackSelect( ValueChangeEvent event )
    {
        setListableSlots( settopSlotConfigService.getAllEmptySlots( ( Rack ) event.getNewValue() ) );
    }

    public void handleSlotSelect( ValueChangeEvent event )
    {

        Slot selectedSlot = ( Slot ) event.getNewValue();
        if ( selectedSlot != null )
        {
            List< Slot > emptySlots = settopSlotConfigService.getAllEmptySlots( selectedSlot.getRack() );
            if ( !emptySlots.contains( selectedSlot ) )
            {

                // http://balusc.blogspot.com/2012/03/reset-non-processed-input-components-on.html
                // (Not the solution, but the problem.)
                rackSelectOneMenu.resetValue();
                slotSelectOneMenu.resetValue();
                FacesContext context = FacesContext.getCurrentInstance();
                if ( context != null )
                {
                    logger.debug( "Slot already in use" + selectedSlot );
                    context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "Slot already in use",
                            null ) );
                    context.renderResponse();
                }
            }
        }
    }

    public List< String > getTraceTypes()
    {
        List< String > traceTypes = new ArrayList< String >();
        for ( TraceType traceType : TraceType.values() )
        {
            traceTypes.add( traceType.getRepresentation() );
        }
        return traceTypes;
    }

    public List< String > getSettopTypes()
    {
        List< String > settopTypes = new ArrayList< String >();
        List< SettopType > types = settopSlotConfigService.getAllSettopTypes();
        for ( SettopType settopType : types )
        {
            settopTypes.add( settopType.getName() );
        }
        return settopTypes;
    }

    public boolean isDeleteRecordings()
    {
        return deleteRecordings;
    }

    public void setDeleteRecordings( boolean deleteRecordings )
    {
        this.deleteRecordings = deleteRecordings;
    }

    public void autoPopulate( SettopDesc settop )
    {
        SettopType settopType = settopSlotConfigService.getSettopTypeByName( settop.getComponentType() );
        if ( settopType != null )
        {
            settop.setManufacturer( settopType.getManufacturer() );
            settop.setModel( settopType.getModel() );
            settop.setRemoteType( settopType.getRemoteType() );
            Map< String, String > properties = settop.getExtraProperties();
            properties.put( TRACE_TYPE_KEY, settopType.getTraceType() );
        }
        else
        {
            settop.setManufacturer( null );
            settop.setModel( null );
            settop.setRemoteType( null );
            Map< String, String > properties = settop.getExtraProperties();
            properties.put( TRACE_TYPE_KEY, null );
        }
    }

    public String getTraceTypeKey()
    {
        return TRACE_TYPE_KEY;
    }

    public void refresh()
    {
        settopSlotConfigService.refresh();
        allConnectedSlots = settopSlotConfigService.getAllConnectedSlots();
    }

    public HtmlSelectOneMenu getSlotSelectOneMenu()
    {
        return slotSelectOneMenu;
    }

    public void setSlotSelectOneMenu( HtmlSelectOneMenu slotSelectOneMenu )
    {
        this.slotSelectOneMenu = slotSelectOneMenu;
    }

    public HtmlSelectOneMenu getRackSelectOneMenu()
    {
        return rackSelectOneMenu;
    }

    public void setRackSelectOneMenu( HtmlSelectOneMenu rackSelectOneMenu )
    {
        this.rackSelectOneMenu = rackSelectOneMenu;
    }
    
    private class DeleteRecordingsJob implements Runnable{
        String macID = "";
        
        public DeleteRecordingsJob(String macID){
            this.macID = macID;
        }
        
        @Override
        public void run()
        {
            settopRecordingService.deleteAllRecordingsForSettop( macID );
        }
        
    }
    
    public List< SettopReservationDesc > getAllSettopsByRack(String rackName)
    {
        return settopSlotConfigService.getAllSettopsByRack( rackName );
    }
    
    
    public void copySelectedSettop(SlotConnectionBean toCopySlotConnection){
        if(toCopySlotConnection != null){
            SettopReservationDesc toCopySettop = (SettopReservationDesc)toCopySlotConnection.getSettop();
            if(toCopySettop != null){
                SettopReservationDesc copiedSettop = new SettopReservationDesc(toCopySettop);
                SlotConnectionBean slotConnection = new SlotConnectionBean();
                copiedSettop.setId( null );
                copiedSettop.setHostMacAddress( null );
                copiedSettop.setMcardMacAddress( null );
                copiedSettop.setHostIp4Address( null );
                copiedSettop.setName( "Copy of "+copiedSettop.getName() );
                slotConnection.setSettop( copiedSettop );
                saveSlotConnection( slotConnection );
                settopCreatedNotifier.fire( new SettopCreateEvent( copiedSettop ) );
                refresh();
            }
        }
    }
}
