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
package com.comcast.cats.vision.panel.trace;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.TraceEvent;
import com.comcast.cats.event.TraceEventDispatcher;
import com.comcast.cats.event.TraceEventHandler;
import com.comcast.cats.provider.BaseProvider;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.vision.panel.TabbedFrame;
import com.comcast.cats.vision.util.PanelAndProviders;
import com.comcast.cats.vision.util.ProviderType;

import static com.comcast.cats.vision.util.CatsVisionConstants.BROWSE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.CLEAR_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.CUSTOM_LOG_START_BUTTON_TXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.HEX_STRING_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.SEND_COMMAND_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.START_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.STOP_LOGGING_BUTTON_TXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.STOP_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.TIME_STAMP_CHECK_BOX_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.SETTOP;

/**
 * Controller for Trace Panel
 * 
 * @author aswathyann
 * 
 */
@Named
public class TraceController implements TraceEventHandler, WindowListener, ActionListener
{

    private static final Dimension           DIMENSION                 = new Dimension( 950, 750 );
    private static final Point               POINT                     = new Point( 50, 50 );
    private static final Rectangle           BOUNDS                    = new Rectangle( POINT, DIMENSION );

    private static final String              SETTOP_TRACE              = "Settop Trace";

    private static final String              TRACE_DIALOG_NAME         = "SettopTrace";

    private Map< String, PanelAndProviders > macIdPanelAndProvidersMap = new LinkedHashMap< String, PanelAndProviders >();

    private TabbedFrame                      traceFrame;
    /**
     * Logger instance for TraceController.
     */
    private static final Logger              logger                    = Logger.getLogger( TraceController.class );
    private TraceEventDispatcher             traceEventDispatcher;

    @Inject
    public TraceController( TraceEventDispatcher traceEventDispatcher )
    {

        logger.debug( "TraceController constructor" );

        this.traceEventDispatcher = traceEventDispatcher;
    }

    @Override
    public void traceEventPerformed( TraceEvent event )
    {

        logger.debug( "Inside traceEventPerformed" );

        String sourceId = event.getSourceId();

        if ( sourceId != null )
        {
            PanelAndProviders panelAndProviders = macIdPanelAndProvidersMap.get( sourceId );

            TracePanel tracePanel = ( TracePanel ) panelAndProviders.getPanel();

            tracePanel.appendPayload( event.getPayload() );
        }
    }

    public void addTraceTab( Settop settop )
    {

        logger.debug( "Adding TraceTab" );

        if ( null == traceFrame )
        {
            traceFrame = new TabbedFrame( SETTOP_TRACE, TRACE_DIALOG_NAME, DIMENSION );
            traceEventDispatcher.addTraceListener( this );
            traceFrame.addWindowListener( this );
            traceFrame.setBounds( BOUNDS );
        }

        String macID = settop.getHostMacAddress();
        /*
         * Maintaining the map (macIdPanelAndProvidersMap) helps in easily
         * identifying if a panel is already added to the TabbedFrame or not.
         * Otherwise one has to iterate through each tab and get it's title to
         * check if a tab is already added or not. Also helps in getting the
         * associate provider of the panel.
         */
        if ( !macIdPanelAndProvidersMap.containsKey( macID ) )
        {
            TracePanel panel = new TracePanel( settop.getHostMacAddress() );

            Map< ProviderType, BaseProvider > providers = new LinkedHashMap< ProviderType, BaseProvider >();
            providers.put( ProviderType.TRACE, settop.getTrace() );
            PanelAndProviders panelAndProviders = new PanelAndProviders( panel, providers );
            macIdPanelAndProvidersMap.put( macID, panelAndProviders );

            // Add listeners to each trace panel
            panel.addActionListener( this );
            panel.setAutoscrolls(true);
            traceFrame.addTab( macID, panel );
        }
        else
        {
            traceFrame.showTab( macID );
        }
    }

    public void removeTraceTab( String mac )
    {
        if ( macIdPanelAndProvidersMap.containsKey( mac ) )
        {

            logger.debug( "Removing Trace Tab with title -" + SETTOP + mac );

            // Remove Trace panel listeners
            TracePanel tracePanel = ( TracePanel ) macIdPanelAndProvidersMap.get( mac ).getPanel();
            tracePanel.removeActionListener( this );

            // Remove trace tab
            traceFrame.removeTab( mac );

            macIdPanelAndProvidersMap.remove( mac );

            if ( macIdPanelAndProvidersMap.isEmpty() )
            {
                // Remove Trace frame
                removeTraceFrame();
            }
        }
    }

    @Override
    public void windowActivated( WindowEvent e )
    {

    }

    @Override
    public void windowClosed( WindowEvent e )
    {

    }

    @Override
    public void windowClosing( WindowEvent e )
    {
        Set< String > keySet = macIdPanelAndProvidersMap.keySet();
        for ( String mac : keySet )
        {
            TracePanel tracePanel = ( TracePanel ) macIdPanelAndProvidersMap.get( mac ).getPanel();
            tracePanel.removeActionListener( this );
        }
        removeTraceFrame();
    }

    public void removeTraceFrame()
    {
        macIdPanelAndProvidersMap.clear();
        traceFrame.setVisible( false );
        traceEventDispatcher.removeTraceListener( this );
        traceFrame.removeWindowListener( this );
        traceFrame = null;
    }

    @Override
    public void windowDeactivated( WindowEvent e )
    {

    }

    @Override
    public void windowDeiconified( WindowEvent e )
    {

    }

    @Override
    public void windowIconified( WindowEvent e )
    {

    }

    @Override
    public void windowOpened( WindowEvent e )
    {

    }

    @Override
    public void actionPerformed( ActionEvent evt )
    {
        logger.debug( "Inside actionPerformed" );

        Object source = evt.getSource();

        if ( source instanceof JButton )
        {
            JButton button = ( JButton ) source;

            String macID = button.getName();

            PanelAndProviders panelAndProviders = macIdPanelAndProvidersMap.get( macID );

            TracePanel tracePanel = ( TracePanel ) panelAndProviders.getPanel();

            TraceProvider traceProvider = ( TraceProvider ) panelAndProviders.getProvider( ProviderType.TRACE );

            String text = button.getText();

            logger.debug( "Button text =" + text );

            if ( text.equals( START_TRACE_BUTTON_TEXT ) )
            {
                startTrace( tracePanel, traceProvider );
            }
            else if ( text.equals( STOP_TRACE_BUTTON_TEXT ) )
            {
                stopTrace( tracePanel, traceProvider );
            }
            else if ( text.equals( CLEAR_TRACE_BUTTON_TEXT ) )
            {
                clearTraceActionPerformed( tracePanel );
            }
            else if ( text.equals( SEND_COMMAND_BUTTON_TEXT ) )
            {
                sendCommand( tracePanel, traceProvider );
            }
            else if ( text.equals( CUSTOM_LOG_START_BUTTON_TXT ) )
            {
                customLogStartButtonActionPerformed( tracePanel, traceProvider );
            }
            else if ( text.equals( BROWSE_BUTTON_TEXT ) )
            {
                browseButtonActionPerformed( tracePanel, traceProvider );
            }
            else if ( text.equals( STOP_LOGGING_BUTTON_TXT ) )
            {
                stopLoggingButtonHandler( tracePanel, traceProvider );
            }
        }
        else if ( source instanceof JRadioButton )
        {
            String text = ( ( JRadioButton ) source ).getText();

            logger.debug( "RadioButton text =" + text );

            if ( text.equals( HEX_STRING_BUTTON_TEXT ) )
            {
                hexStringRButtonActionPerformed( evt );
            }
        }
        else if ( source instanceof JCheckBox )
        {
            String text = ( ( JCheckBox ) source ).getText();
            if ( text.equals( TIME_STAMP_CHECK_BOX_TEXT ) )
            {
                timeStampCheckBoxActionPerformed( evt );
            }
        }

    }

    // The following methods are cut-pasted from TracePanel
    private void stopLoggingButtonHandler( TracePanel tracePanel, TraceProvider traceProvider )
    {
        logger.debug( "Stop trace " );

        String currentCustomTraceLocation = "";
        tracePanel.setCurrentCustomTraceLocation( currentCustomTraceLocation );
        tracePanel.getTraceFileLocation().setText( currentCustomTraceLocation );
        traceProvider.setTraceLogLocation( null, false );
        tracePanel.validateAndHighlightStartButton( "" );
    }

    private void customLogStartButtonActionPerformed( TracePanel tracePanel, TraceProvider traceProvider )
    {
        logger.debug( "Custom Trace logging" );

        String filename = tracePanel.getTraceFileLocation().getText();
        if ( filename == null || filename.isEmpty() )
        {
            JOptionPane.showMessageDialog( null, "Please provide a vaild parth to start logging", "No path specified",
                    JOptionPane.ERROR_MESSAGE );
        }
        tracePanel.setCurrentCustomTraceLocation( filename );

        logger.debug( "Start logging at: " + filename );

        setTraceLoggerLocation( filename, traceProvider, tracePanel.isAppendRadioButtonSelected() );
        tracePanel.validateAndHighlightStartButton( filename );
    }

    /**
     * Sets the trace location in the TraceProvider to the new file location.
     * 
     * @param fileName
     *            : The file where we want to output trace.
     */
    private void setTraceLoggerLocation( String fileName, TraceProvider traceProvider, boolean shouldAppend )
    {
        logger.debug( "fileName : " + fileName );

        if ( null != fileName && !fileName.isEmpty() )
        {

            logger.debug( "Append Trace to file " + fileName + " append status :" + shouldAppend );

            traceProvider.setTraceLogLocation( fileName, shouldAppend );
        }
    }

    private void timeStampCheckBoxActionPerformed( ActionEvent evt )
    {
        // TODO add your handling code here:
    }

    private void hexStringRButtonActionPerformed( ActionEvent evt )
    {
        // TODO add your handling code here:
    }

    private void clearTraceActionPerformed( TracePanel tracePanel )
    {
        tracePanel.getTraceTextArea().setText( "" );
    }

    /**
     * Start trace.
     */
    public void startTrace( TracePanel tracePanel, TraceProvider traceProvider )
    {
        logger.debug( "Start Trace" );

        try
        {
            traceProvider.startTrace();
            tracePanel.appendPayload( "Trace Started\n" );
        }
        catch ( Exception e )
        {
            tracePanel.appendPayload( "Failed to connect to Trace Server" );
            logger.error( "Failed to connect to Trace Server" + e.getMessage() );
        }

    }

    /**
     * Stop trace.
     */
    public void stopTrace( TracePanel tracePanel, TraceProvider traceProvider )
    {
        logger.debug( "Stop Trace" );

        try
        {
            traceProvider.stopTrace();

        }
        catch ( Exception e )
        {
            tracePanel.appendPayload( "Failed to stop trace." );
            logger.warn( "Failed to stop trace" + e.getMessage() );
        }
        tracePanel.appendPayload( "Trace stopped\n" );

    }

    public void sendCommand( TracePanel tracePanel, TraceProvider traceProvider )
    {

        try
        {
            // FIXME: this must be removed once tgcommon is upgraded.
            // the If blok should be removed.
            String commandText = tracePanel.getCommandTextFld().getText();
            String command = commandText;
            if ( command.equals( " " ) || command.isEmpty() )
            {
                traceProvider.sendTraceString( commandText, false );
            }
            else
            {
                traceProvider.sendTraceString( commandText, tracePanel.getHexStringRButton().isSelected() );
            }
            tracePanel.getCommandTextFld().setText( "" );
        }
        catch ( Exception e )
        {
            tracePanel.appendPayload( "Failed to stop trace." );
            logger.warn( "Failed to stop trace" + e.getMessage() );
        }

    }

    private void browseButtonActionPerformed( TracePanel tracePanel, TraceProvider traceProvider )
    {

        String fileName = tracePanel.showTraceFileLocationChooser();
        int shouldStopCurrentLogging = 1;
        // The following logic can be relooked. This is not the most effecient
        // No time in 2.3.5

        String currentCustomTraceLocation = tracePanel.getCurrentCustomTraceLocation();

        if ( !currentCustomTraceLocation.isEmpty() && !currentCustomTraceLocation.equals( fileName ) )
        {
            shouldStopCurrentLogging = JOptionPane.showConfirmDialog( null, "This will stop logging to"
                    + currentCustomTraceLocation, "Log location change confirmation", JOptionPane.OK_CANCEL_OPTION );
        }
        if ( shouldStopCurrentLogging == 0 )
        {
            currentCustomTraceLocation = "";
            traceProvider.setTraceLogLocation( null, false );
        }
        // This is also junk logic,just because of lack of time.
        if ( !currentCustomTraceLocation.isEmpty() )
        {
            // since cancel is pressed, set the currentLocation back
            fileName = currentCustomTraceLocation;
        }
        tracePanel.validateAndHighlightStartButton( fileName );
    }
}