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

import static com.comcast.cats.vision.util.CatsVisionConstants.APPEND_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.BROWSE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.CLEAR_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.CUSTOM_LOG_START_BUTTON_TXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.HEX_STRING_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.SEND_COMMAND_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.START_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.STOP_LOGGING_BUTTON_TXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.STOP_TRACE_BUTTON_TEXT;
import static com.comcast.cats.vision.util.CatsVisionConstants.TIME_STAMP_CHECK_BOX_TEXT;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;

/**
 * Trace Panel
 * 
 * @author THusai000
 */
public class TracePanel extends JPanel
{

    private String                 currentCustomTraceLocation = "";
    private static final long      serialVersionUID           = 960156041121295033L;
    private String                 mac;
    private static final int       COLS                       = 100;
    private static final int       ROWS                       = 30;
    private static final int       PANEL_WIDTH                = 1000;
    private static final int       PANEL_HEIGHT               = 1000;
    private static final Dimension PREFERRED_PANEL_SIZE       = new Dimension( PANEL_WIDTH, PANEL_HEIGHT );
    private static final Dimension PREFERRED_PANE_SIZE        = new Dimension( 810, 550 );

    /**
     * Logger instance for TracePanel.
     */
    private static final Logger    logger                     = Logger.getLogger( TracePanel.class );

    private JRadioButton           appendTraceRadioButton;
    private JButton                browseButton;
    private JButton                clearTraceButton;
    private JLabel                 commandLabel;
    private JPanel                 commandSendingPanel;
    private JTextField             commandTextFld;
    private JPanel                 controlPanel;
    private JButton                customLogStartButton;

    private JTabbedPane            generalTabbedPane;
    private JRadioButton           hexStringRButton;
    private JLabel                 jLabel1;
    private JPanel                 logOptionPanel;
    private JRadioButton           overwriteTraceRadioButton;
    private JButton                sendCommandButton;
    private JButton                startTraceButton;
    private JButton                stopLoggingButton;
    private JButton                stopTraceButton;
    private JCheckBox              timeStampCheckBox;
    private JTextField             traceFileLocation;
    private JLabel                 traceLocationLabel;
    private JScrollPane            tracePane;
    private JTextArea              traceTextArea;
    private JFileChooser           traceFileSaver;
    private ButtonGroup            buttonGroup;

    /** Creates new form TracePanel */
    public TracePanel( String mac )
    {
        logger.debug( "TracePanel constructor" );
        this.mac = mac;
        initComponents();
        doCustomInit();
        setName( mac );
        setVisible( true );
    }

    public void doCustomInit()
    {
        traceFileSaver = new JFileChooser( "Trace Logs Location" );
        traceFileSaver.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        traceFileSaver.setAcceptAllFileFilterUsed( false );
        buttonGroup = new ButtonGroup();
        buttonGroup.add( appendTraceRadioButton );
        buttonGroup.add( overwriteTraceRadioButton );
        timeStampCheckBox.setEnabled( false );
        traceFileLocation.setEnabled( false );
        setPreferredSize( PREFERRED_PANEL_SIZE );
        setSize( PREFERRED_PANEL_SIZE );
        setMinimumSize( PREFERRED_PANEL_SIZE );
        validateAndHighlightStartButton( "" );

    }

    /**
     * This function enables/disables the start/stop button based on the
     * file/folder selected.
     * 
     * @param newLocation
     */
    protected void validateAndHighlightStartButton( String newLocation )
    {
        if ( currentCustomTraceLocation.isEmpty() || !currentCustomTraceLocation.equals( newLocation ) )
        {
            logger.debug( "ValidateHighlight   currentTraceLoc: " + currentCustomTraceLocation + "new location:"
                    + newLocation );

            customLogStartButton.setEnabled( true );
            stopLoggingButton.setEnabled( false );

        }
        else
        {
            customLogStartButton.setEnabled( false );
            stopLoggingButton.setEnabled( true );
        }
        // This is junk logic..cannot think in a better way.
        String filePath = null;
        if ( currentCustomTraceLocation.isEmpty() )
        {
            filePath = newLocation;
        }
        else
        {
            filePath = currentCustomTraceLocation;
        }
        traceFileLocation.setText( filePath );
    }

    private void initComponents()
    {
        tracePane = new JScrollPane();
        traceTextArea = new JTextArea();
        traceLocationLabel = new JLabel();
        generalTabbedPane = new JTabbedPane();
        controlPanel = new JPanel();
        startTraceButton = new JButton();

        stopTraceButton = new JButton();
        clearTraceButton = new JButton();
        commandSendingPanel = new JPanel();
        commandTextFld = new JTextField();
        hexStringRButton = new JRadioButton();
        sendCommandButton = new JButton();
        commandLabel = new JLabel();
        logOptionPanel = new JPanel();
        timeStampCheckBox = new JCheckBox();
        customLogStartButton = new JButton();
        traceFileLocation = new JTextField();
        jLabel1 = new JLabel();
        appendTraceRadioButton = new JRadioButton();
        overwriteTraceRadioButton = new JRadioButton();
        stopLoggingButton = new JButton();
        browseButton = new JButton();

        setEnabled( false );
        setRequestFocusEnabled( false );
        setVerifyInputWhenFocusTarget( false );

        tracePane.setName( "tracePane" ); // NOI18N

        traceTextArea.setBackground( new Color( 204, 204, 204 ) );
        traceTextArea.setColumns( COLS );
        traceTextArea.setRows( ROWS );
        traceTextArea.setDoubleBuffered( true );
        traceTextArea.setDragEnabled( true );
        traceTextArea.setName( "traceTextArea" ); // NOI18N


        tracePane.setViewportView( traceTextArea );
        tracePane.setMinimumSize( PREFERRED_PANE_SIZE );
        tracePane.setAutoscrolls( true );
        tracePane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        tracePane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        if ( mac != null )
        {
            traceLocationLabel.setText( "Trace Logs for " + mac );
        }
        else
        {
            traceLocationLabel.setText( "Trace Logs : " );
        }
        traceLocationLabel.setName( "traceLocationLabel" ); // NOI18N

        generalTabbedPane.setName( "generalTabbedPane" ); // NOI18N

        controlPanel.setName( "controlPanel" ); // NOI18N

        startTraceButton.setText( START_TRACE_BUTTON_TEXT );
        startTraceButton.setName( mac ); // NOI18N

        stopTraceButton.setText( STOP_TRACE_BUTTON_TEXT );
        stopTraceButton.setName( mac ); // NOI18N

        clearTraceButton.setText( CLEAR_TRACE_BUTTON_TEXT );
        clearTraceButton.setName( mac ); // NOI18N

        GroupLayout controlPanelLayout = new GroupLayout( controlPanel );
        controlPanel.setLayout( controlPanelLayout );
        controlPanelLayout.setHorizontalGroup( controlPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        controlPanelLayout.createSequentialGroup().addContainerGap().addComponent( startTraceButton,
                                GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE ).addPreferredGap(
                                LayoutStyle.ComponentPlacement.RELATED ).addComponent( stopTraceButton )
                                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED ).addComponent(
                                        clearTraceButton ).addContainerGap( 293, Short.MAX_VALUE ) ) );
        controlPanelLayout.setVerticalGroup( controlPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        controlPanelLayout.createSequentialGroup().addContainerGap().addGroup(
                                controlPanelLayout.createParallelGroup( GroupLayout.Alignment.BASELINE ).addComponent(
                                        startTraceButton ).addComponent( stopTraceButton ).addComponent(
                                        clearTraceButton ) ).addContainerGap( 38, Short.MAX_VALUE ) ) );

        generalTabbedPane.addTab( "General", controlPanel );

        commandSendingPanel.setName( mac ); // NOI18N

        commandTextFld.setName( "commandTextFld" ); // NOI18N

        hexStringRButton.setText( HEX_STRING_BUTTON_TEXT );
        hexStringRButton.setName( mac ); // NOI18N

        sendCommandButton.setText( SEND_COMMAND_BUTTON_TEXT );
        sendCommandButton.setName( mac ); // NOI18N

        commandLabel.setText( "Enter Command" );
        commandLabel.setName( "commandLabel" ); // NOI18N

        GroupLayout commandSendingPanelLayout = new GroupLayout( commandSendingPanel );
        commandSendingPanel.setLayout( commandSendingPanelLayout );
        commandSendingPanelLayout.setHorizontalGroup( commandSendingPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addGroup(
                commandSendingPanelLayout.createSequentialGroup().addGroup(
                        commandSendingPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING ).addGroup(
                                commandSendingPanelLayout.createSequentialGroup().addGap( 10, 10, 10 ).addComponent(
                                        commandTextFld, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE )
                                        .addGap( 18, 18, 18 ).addComponent( hexStringRButton ).addGap( 18, 18, 18 )
                                        .addComponent( sendCommandButton ) ).addGroup(
                                commandSendingPanelLayout.createSequentialGroup().addGap( 29, 29, 29 ).addComponent(
                                        commandLabel ) ) ).addContainerGap( 201, Short.MAX_VALUE ) ) );
        commandSendingPanelLayout.setVerticalGroup( commandSendingPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addGroup(
                commandSendingPanelLayout.createSequentialGroup().addComponent( commandLabel ).addPreferredGap(
                        LayoutStyle.ComponentPlacement.RELATED ).addGroup(
                        commandSendingPanelLayout.createParallelGroup( GroupLayout.Alignment.BASELINE ).addComponent(
                                commandTextFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE ).addComponent( hexStringRButton ).addComponent(
                                sendCommandButton ) ).addContainerGap( 29, Short.MAX_VALUE ) ) );

        generalTabbedPane.addTab( "Command Sending", commandSendingPanel );

        logOptionPanel.setName( "logOptionPanel" ); // NOI18N

        timeStampCheckBox.setText( TIME_STAMP_CHECK_BOX_TEXT );
        timeStampCheckBox.setName( mac ); // NOI18N

        customLogStartButton.setText( CUSTOM_LOG_START_BUTTON_TXT );
        customLogStartButton.setName( mac ); // NOI18N

        traceFileLocation.setEditable( false );
        traceFileLocation.setToolTipText( "Hit browse to specify a folder path" );
        traceFileLocation.setName( "traceFileLocation" ); // NOI18N

        jLabel1.setText( "Custom Log Location" );
        jLabel1.setName( "jLabel1" ); // NOI18N

        appendTraceRadioButton.setSelected( true );
        appendTraceRadioButton.setText( APPEND_TRACE_BUTTON_TEXT );
        appendTraceRadioButton.setName( "appendTraceRadioButton" ); // NOI18N

        overwriteTraceRadioButton.setText( "Overwrite Log" );
        overwriteTraceRadioButton.setName( "overwriteTraceRadioButton" ); // NOI18N

        stopLoggingButton.setText( STOP_LOGGING_BUTTON_TXT );
        stopLoggingButton.setName( mac ); // NOI18N

        browseButton.setText( BROWSE_BUTTON_TEXT );
        browseButton.setName( mac ); // NOI18N

        GroupLayout logOptionPanelLayout = new GroupLayout( logOptionPanel );
        logOptionPanel.setLayout( logOptionPanelLayout );
        logOptionPanelLayout.setHorizontalGroup( logOptionPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addGroup(
                logOptionPanelLayout.createSequentialGroup().addContainerGap().addGroup(
                        logOptionPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING, false ).addGroup(
                                logOptionPanelLayout.createSequentialGroup().addComponent( jLabel1 ).addGap( 9, 9, 9 )
                                        .addComponent( traceFileLocation, GroupLayout.PREFERRED_SIZE, 221,
                                                GroupLayout.PREFERRED_SIZE ) )
                                .addGroup(
                                        logOptionPanelLayout.createSequentialGroup().addComponent( timeStampCheckBox )
                                                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ).addComponent(
                                                        browseButton ) ) ).addGap( 6, 6, 6 ).addGroup(
                        logOptionPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING ).addGroup(
                                logOptionPanelLayout.createSequentialGroup().addComponent( appendTraceRadioButton )
                                        .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED ).addComponent(
                                                overwriteTraceRadioButton ) ).addGroup(
                                logOptionPanelLayout.createSequentialGroup().addComponent( customLogStartButton,
                                        GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE ).addPreferredGap(
                                        LayoutStyle.ComponentPlacement.RELATED ).addComponent( stopLoggingButton,
                                        GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE ) ) ).addGap( 25,
                        25, 25 ) ) );
        logOptionPanelLayout.setVerticalGroup( logOptionPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        logOptionPanelLayout.createSequentialGroup().addContainerGap( 11, Short.MAX_VALUE ).addGroup(
                                logOptionPanelLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                                        .addComponent( traceFileLocation, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE ).addComponent(
                                                jLabel1 ).addComponent( appendTraceRadioButton ).addComponent(
                                                overwriteTraceRadioButton ) ).addPreferredGap(
                                LayoutStyle.ComponentPlacement.RELATED ).addGroup(
                                logOptionPanelLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                                        .addComponent( timeStampCheckBox ).addComponent( customLogStartButton )
                                        .addComponent( stopLoggingButton ).addComponent( browseButton ) )
                                .addContainerGap() ) );

        generalTabbedPane.addTab( "Custom Logging Options", logOptionPanel );

        GridBagLayout tracePanelLayout = new GridBagLayout();
        setLayout( tracePanelLayout );

        GridBagConstraints traceLocationLabelConstraints = new GridBagConstraints();
        traceLocationLabelConstraints.gridx = 0;
        traceLocationLabelConstraints.gridy = 0;
        traceLocationLabelConstraints.gridheight = 1;
        traceLocationLabelConstraints.anchor = GridBagConstraints.PAGE_START;
        add( traceLocationLabel, traceLocationLabelConstraints );

        GridBagConstraints tracePaneConstraints = new GridBagConstraints();
        tracePaneConstraints.gridx = 0;
        tracePaneConstraints.gridy = 1;
        tracePaneConstraints.fill = GridBagConstraints.BOTH;
        tracePaneConstraints.anchor = GridBagConstraints.CENTER;
        add( tracePane, tracePaneConstraints );

        GridBagConstraints generalTabbedPaneConstraints = new GridBagConstraints();
        generalTabbedPaneConstraints.gridx = 0;
        generalTabbedPaneConstraints.gridy = 2;
        generalTabbedPaneConstraints.fill = GridBagConstraints.HORIZONTAL;
        generalTabbedPaneConstraints.anchor = GridBagConstraints.PAGE_START;
        add( generalTabbedPane, generalTabbedPaneConstraints );

        traceLocationLabel.getAccessibleContext().setAccessibleName( mac );

    }

    public void appendPayload( String payload )
    {
        traceTextArea.append( payload );
        traceTextArea.setCaretPosition( traceTextArea.getDocument().getLength() );
    }

    /**
     * Shows the trace file location file chooser. Saves as only .log files
     */
    protected String showTraceFileLocationChooser()
    {
        // String fileName = "";
        String fileName = currentCustomTraceLocation;

        int retVal = traceFileSaver.showSaveDialog( this );

        if ( retVal == JFileChooser.APPROVE_OPTION )
        {
            fileName = traceFileSaver.getSelectedFile().getAbsolutePath();
            logger.debug( "Trace log file location chosen : " + fileName );
        }
        return fileName;
    }

    public String getCurrentCustomTraceLocation()
    {
        return currentCustomTraceLocation;
    }

    public void setCurrentCustomTraceLocation( String currentCustomTraceLocation )
    {
        this.currentCustomTraceLocation = currentCustomTraceLocation;
    }

    public JRadioButton getAppendTraceRadioButton()
    {
        return appendTraceRadioButton;
    }

    public void setAppendTraceRadioButton( JRadioButton appendTraceRadioButton )
    {
        this.appendTraceRadioButton = appendTraceRadioButton;
    }

    public JButton getBrowseButton()
    {
        return browseButton;
    }

    public void setBrowseButton( JButton browseButton )
    {
        this.browseButton = browseButton;
    }

    public JButton getClearTraceButton()
    {
        return clearTraceButton;
    }

    public void setClearTraceButton( JButton clearTraceButton )
    {
        this.clearTraceButton = clearTraceButton;
    }

    public JLabel getCommandLabel()
    {
        return commandLabel;
    }

    public void setCommandLabel( JLabel commandLabel )
    {
        this.commandLabel = commandLabel;
    }

    public JPanel getCommandSendingPanel()
    {
        return commandSendingPanel;
    }

    public void setCommandSendingPanel( JPanel commandSendingPanel )
    {
        this.commandSendingPanel = commandSendingPanel;
    }

    public JTextField getCommandTextFld()
    {
        return commandTextFld;
    }

    public void setCommandTextFld( JTextField commandTextFld )
    {
        this.commandTextFld = commandTextFld;
    }

    public JPanel getControlPanel()
    {
        return controlPanel;
    }

    public void setControlPanel( JPanel controlPanel )
    {
        this.controlPanel = controlPanel;
    }

    public JButton getCustomLogStartButton()
    {
        return customLogStartButton;
    }

    public void setCustomLogStartButton( JButton customLogStartButton )
    {
        this.customLogStartButton = customLogStartButton;
    }

    public JTabbedPane getGeneralTabbedPane()
    {
        return generalTabbedPane;
    }

    public void setGeneralTabbedPane( JTabbedPane generalTabbedPane )
    {
        this.generalTabbedPane = generalTabbedPane;
    }

    public JRadioButton getHexStringRButton()
    {
        return hexStringRButton;
    }

    public void setHexStringRButton( JRadioButton hexStringRButton )
    {
        this.hexStringRButton = hexStringRButton;
    }

    public JPanel getLogOptionPanel()
    {
        return logOptionPanel;
    }

    public void setLogOptionPanel( JPanel logOptionPanel )
    {
        this.logOptionPanel = logOptionPanel;
    }

    public JRadioButton getOverwriteTraceRadioButton()
    {
        return overwriteTraceRadioButton;
    }

    public void setOverwriteTraceRadioButton( JRadioButton overwriteTraceRadioButton )
    {
        this.overwriteTraceRadioButton = overwriteTraceRadioButton;
    }

    public JButton getSendCommandButton()
    {
        return sendCommandButton;
    }

    public void setSendCommandButton( JButton sendCommandButton )
    {
        this.sendCommandButton = sendCommandButton;
    }

    public JButton getStartTraceButton()
    {
        return startTraceButton;
    }

    public void setStartTraceButton( JButton startTraceButton )
    {
        this.startTraceButton = startTraceButton;
    }

    public JButton getStopLoggingButton()
    {
        return stopLoggingButton;
    }

    public void setStopLoggingButton( JButton stopLoggingButton )
    {
        this.stopLoggingButton = stopLoggingButton;
    }

    public JButton getStopTraceButton()
    {
        return stopTraceButton;
    }

    public void setStopTraceButton( JButton stopTraceButton )
    {
        this.stopTraceButton = stopTraceButton;
    }

    public JCheckBox getTimeStampCheckBox()
    {
        return timeStampCheckBox;
    }

    public void setTimeStampCheckBox( JCheckBox timeStampCheckBox )
    {
        this.timeStampCheckBox = timeStampCheckBox;
    }

    public JTextField getTraceFileLocation()
    {
        return traceFileLocation;
    }

    public void setTraceFileLocation( JTextField traceFileLocation )
    {
        this.traceFileLocation = traceFileLocation;
    }

    public JLabel getTraceLocationLabel()
    {
        return traceLocationLabel;
    }

    public void setTraceLocationLabel( JLabel traceLocationLabel )
    {
        this.traceLocationLabel = traceLocationLabel;
    }

    public JScrollPane getTracePane()
    {
        return tracePane;
    }

    public void setTracePane( JScrollPane tracePane )
    {
        this.tracePane = tracePane;
    }

    public TracePanel getTracePanel()
    {
        return this;
    }

    public JTextArea getTraceTextArea()
    {
        return traceTextArea;
    }

    public void setTraceTextArea( JTextArea traceTextArea )
    {
        this.traceTextArea = traceTextArea;
    }

    public JFileChooser getTraceFileSaver()
    {
        return traceFileSaver;
    }

    public void setTraceFileSaver( JFileChooser traceFileSaver )
    {
        this.traceFileSaver = traceFileSaver;
    }

    public ButtonGroup getButtonGroup()
    {
        return buttonGroup;
    }

    public void setButtonGroup( ButtonGroup buttonGroup )
    {
        this.buttonGroup = buttonGroup;
    }

    public boolean isAppendRadioButtonSelected()
    {
        return appendTraceRadioButton.isSelected();
    }

    public void addActionListener( ActionListener listener )
    {
        startTraceButton.addActionListener( listener );
        stopTraceButton.addActionListener( listener );
        clearTraceButton.addActionListener( listener );
        hexStringRButton.addActionListener( listener );
        sendCommandButton.addActionListener( listener );
        timeStampCheckBox.addActionListener( listener );
        customLogStartButton.addActionListener( listener );
        stopLoggingButton.addActionListener( listener );
        browseButton.addActionListener( listener );

    }

    public void removeActionListener( ActionListener listener )
    {
        startTraceButton.removeActionListener( listener );
        stopTraceButton.removeActionListener( listener );
        clearTraceButton.removeActionListener( listener );
        hexStringRButton.removeActionListener( listener );
        sendCommandButton.removeActionListener( listener );
        timeStampCheckBox.removeActionListener( listener );
        customLogStartButton.removeActionListener( listener );
        stopLoggingButton.removeActionListener( listener );
        browseButton.removeActionListener( listener );
    }
}
