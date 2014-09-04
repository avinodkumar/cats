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
package com.comcast.cats.vision.panel.videogrid;

import static com.comcast.cats.vision.util.CatsVisionConstants.ALLOCATED_SETTOP;
import static com.comcast.cats.vision.util.CatsVisionConstants.AVAILABLE_SETTOP;
import static com.comcast.cats.vision.util.CatsVisionConstants.MULTIVISION_MAX_SETTOPS;
import static com.comcast.cats.vision.util.CatsVisionConstants.SELECT_ALL_SETTOPS;
import static com.comcast.cats.vision.util.CatsVisionConstants.SETTOP_INFO;
import static com.comcast.cats.vision.util.CatsVisionConstants.SNAP_IMAGE;
import static com.comcast.cats.vision.util.CatsVisionConstants.TRACE;
import static com.comcast.cats.vision.util.CatsVisionUtils.getSettopInfoToolTipText;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.event.AllocationEvent;
import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.CatsResponseEvent;
import com.comcast.cats.event.impl.ManagedThreadPool;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.vision.CATSVisionView;
import com.comcast.cats.vision.components.ErrorConsole;
import com.comcast.cats.vision.event.ActionType;
import com.comcast.cats.vision.event.ConfigButtonEvent;
import com.comcast.cats.vision.event.PowerEvent;
import com.comcast.cats.vision.event.RemoteEvent;
import com.comcast.cats.vision.event.ScriptPlayBackEvent;
import com.comcast.cats.vision.panel.ConfigurableButtonPanel;
import com.comcast.cats.vision.panel.SettopInfoPanel;
import com.comcast.cats.vision.panel.TabbedFrame;
import com.comcast.cats.vision.panel.configuration.ConfigPanel;
import com.comcast.cats.vision.panel.imagecompare.ImageController;
import com.comcast.cats.vision.panel.power.PowerPanel;
import com.comcast.cats.vision.panel.remote.RemoteControlView;
import com.comcast.cats.vision.panel.remote.RemoteController;
import com.comcast.cats.vision.panel.trace.TraceController;
import com.comcast.cats.vision.panel.video.VideoDisplayPanel;
import com.comcast.cats.vision.panel.videogrid.model.GridDataModel;
import com.comcast.cats.vision.task.ConfigButtonTask;
import com.comcast.cats.vision.task.PowerPressKeyTask;
import com.comcast.cats.vision.task.RemotePressKeyTask;
import com.comcast.cats.vision.task.ScriptPlayBackTask;
import com.comcast.cats.vision.task.SetVideoFrameRateTask;
import com.comcast.cats.vision.util.CatsVisionConstants;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * The controller class for the MultivisionPanel
 * 
 * @author aswathyann
 * 
 */
@Named
public class VideoGridController extends WindowAdapter implements CatsEventHandler, MouseListener, ItemListener,
        ActionListener
{
    private static final Logger                 logger                   = Logger.getLogger( VideoGridController.class );

    private final SettopFactory                 settopFactory;

    private final SettopExclusiveAccessEnforcer accessEnforcer;

    private CatsEventDispatcher                 catsEventDispatcherThreaded;

    private ZoomedVideo                         zoomedVideo;

    private CATSVisionView                      catsVisionView;

    private ManagedThreadPool                   managedThreadPool;

    private GridDataModel                       model;

    private ImageController                     imageController;

    private RemoteController                    remoteController;

    private TraceController                     traceController;

    private RightClickMenu                      rightClickMenu           = new RightClickMenu();

    private Settop                              currentSettop;

    private Map< String, Exception >            exceptionsMap            = new LinkedHashMap< String, Exception >();

    private static final String                 SETTOP_INFO_DIALOG_TITLE = "Settop Info";

    private static final String                 SETTOP_INFO_DIALOG_NAME  = "settopInfo";

    private static final Dimension              DIMENSION                = new Dimension( 690, 300 );

    private TabbedFrame                         settopInfoTabbedFrame    = new TabbedFrame( SETTOP_INFO_DIALOG_TITLE,
                                                                                 SETTOP_INFO_DIALOG_NAME, DIMENSION );
    private Map< String, SettopInfoPanel >      settopInfoMap            = new LinkedHashMap< String, SettopInfoPanel >();
    private VideoGridPanel                      videoGridPanel;

    private ConfigPanel                         configPanel;

    private boolean                             isSelectAll;

    private boolean                             isDeselectAll;

    @Inject
    public VideoGridController( SettopFactory settopFactory, SettopExclusiveAccessEnforcer accessEnforcer,
            CatsEventDispatcher dispatcher, GridDataModel model, ManagedThreadPool managedThreadPool,
            RemoteController remoteController, TraceController traceController, ImageController imageController,
            ConfigPanel configPanel )
    {
        this.settopFactory = settopFactory;

        this.accessEnforcer = accessEnforcer;

        this.model = model;

        this.catsEventDispatcherThreaded = dispatcher;

        this.managedThreadPool = managedThreadPool;

        this.remoteController = remoteController;

        this.traceController = traceController;

        this.imageController = imageController;

        this.configPanel = configPanel;

        addListeners();
    }

    private void addListeners()
    {
        List< CatsEventType > catsEventTypes = new ArrayList< CatsEventType >();

        catsEventTypes.add( CatsEventType.REMOTE );
        catsEventTypes.add( CatsEventType.REMOTE_FAIL_RESPONSE );
        catsEventTypes.add( CatsEventType.POWER );
        catsEventTypes.add( CatsEventType.POWER_FAIL_RESPONSE );
        catsEventTypes.add( CatsEventType.ALLOCATION );
        catsEventTypes.add( CatsEventType.CATS_CONFIG_BUTTON_EVENT );
        catsEventTypes.add( CatsEventType.CATS_CONFIG_BUTTON_EVENT_FAIL_RESPONSE );
        catsEventTypes.add( CatsEventType.SCRIPT_PLAY_BACK );
        catsEventTypes.add( CatsEventType.SCRIPT_PLAY_BACK_RESPONSE );
        catsEventDispatcherThreaded.addListener( this, catsEventTypes );
        settopInfoTabbedFrame.addWindowListener( this );
        rightClickMenu.addActionListener( this );
    }

    public void removeListeners()
    {
        catsEventDispatcherThreaded.removeListener( this );
        settopInfoTabbedFrame.removeWindowListener( this );
        rightClickMenu.removeActionListener( this );
    }

    /**
     * Launch CATS Vision for given mac ids
     * 
     * @param macIdList
     *            macId list
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public void launchCatsVision( Set< String > macIds ) throws MalformedURLException, URISyntaxException
    {
        logger.info( "Launching CATS Vision.. " );
        catsVisionView.getFrame().setTitle( "Loading "+CatsVisionConstants.APPLICATION_TITLE+".. Please wait..." );

        /*
         * If launchVideoCheckBox is selected then add to existing CATS Vision
         * videos, else launch new set of videos
         */
        if ( configPanel.getLaunchVideoCheckBox().isSelected() )
        {
            Set< String > macIdSet = filterMacIDsToBeLaunched( macIds );

            if ( checkSettopLimit( macIdSet ) )
            {

                if ( !macIdSet.isEmpty() )
                {
                    Set< Settop > settops = createSettops( macIdSet );

                    if ( !settops.isEmpty() )
                    {
                        if ( model.getSettops().size() > 0 )
                        {
                            model.addSettops( settops );
                        }
                        else
                        {
                            model.setSettops( settops );
                        }
                        updateVideoGridPanel( settops );

                        log( "Launched Video for the settops -" + model.getSettopNames( settops ) );
                    }
                }
            }
        }
        else
        {
            List< VideoPanel > videoPanels = model.getVideoPanels();

            if ( videoPanels.size() > 0 )
            {
                releaseAll();

                destroyVideoPanels();
                for ( VideoPanel videoPanel : videoPanels )
                {
                    removeSettopInfoTab( videoPanel.getSettop().getHostMacAddress() );
                }

                // removeVideoPanelListener();

                model.clearModel();
            }

            if ( checkSettopLimit( macIds ) )
            {
                Set< Settop > settops = createSettops( macIds );
                if ( !settops.isEmpty() )
                {
                    model.setSettops( settops );
                    updateVideoGridPanel( settops );
                    log( "Video launched for settops -" + model.getLaunchedSettopNames() );
                }
            }
        }
        catsVisionView.selectCatsVisionTab();
    }

    /*
     * private void removeVideoPanelListener() { List< VideoPanel > videoPanels
     * = model.getVideoPanels(); for(VideoPanel videoPanel: videoPanels) {
     * videoPanel.removeActionListener( listener );
     * videoPanel.removeItemListener( listener ); }
     * 
     * }
     */

    private void removeSettopInfoTab( String macID )
    {
        if ( ( !settopInfoMap.isEmpty() ) && ( settopInfoMap.containsKey( macID ) )
                && ( settopInfoTabbedFrame.isVisible() ) )
        {
            settopInfoTabbedFrame.removeTab( macID );
            settopInfoMap.remove( macID );

            if ( settopInfoMap.isEmpty() )
            {
                settopInfoTabbedFrame.setVisible( false );

            }
        }
    }

    private Set< String > filterMacIDsToBeLaunched( Set< String > macIds )
    {
        Set< String > macIDSet = new LinkedHashSet< String >();

        Set< String > existingMacIDSet = new LinkedHashSet< String >();

        for ( String macID : macIds )
        {
            if ( !model.getLaunchedSettopNames().contains( macID ) )
            {
                macIDSet.add( macID );
            }
            else
            {
                existingMacIDSet.add( macID );
            }
        }
        if ( !existingMacIDSet.isEmpty() )
        {
            log( "The video for the following settops are already launched -" + existingMacIDSet );
        }
        return macIDSet;
    }

    private void updateVideoGridPanel( Set< Settop > settops )
    {
        createVideoPanels( settops );

        videoGridPanel.removeAll();

        refreshVideoGridPanel();

        startAllVideo();
    }

    private VideoGridPanel createVideoGridPanel()
    {

        List< VideoPanel > videoPanels = createVideoPanels( model.getSettops() );

        /*
         * Resolution will be the default video provider resolution
         */
        startAllVideo();

        int cols = 0;
        int rows = 0;
        if ( ( videoPanels != null ) && ( !videoPanels.isEmpty() ) )
        {
            cols = calculateColumns( videoPanels.size() );
            rows = calculateRows( videoPanels.size(), cols );

        }
        return new VideoGridPanel( model, rows, cols );
    }

    public MultivisionPanel createMultiVisionPanel( Set< Settop > settops ) throws MalformedURLException,
            URISyntaxException
    {
        model.setSettops( settops );

        RemoteControlView remoteControl = remoteController.getRemote();

        AllocationAndSelectionPanel allocationPanel = new AllocationAndSelectionPanel();

        PowerPanel powerPanel = new PowerPanel( model, catsEventDispatcherThreaded );

        ConfigurableButtonPanel configurableButtonPanel = new ConfigurableButtonPanel( model,
                catsEventDispatcherThreaded );

        VideoControlPanel videoControlPanel = new VideoControlPanel( remoteControl, allocationPanel, powerPanel,
                configurableButtonPanel );

        LogPanel logPanel = new LogPanel();

        videoGridPanel = createVideoGridPanel();

        MultivisionPanel multivisionPanel = new MultivisionPanel( videoControlPanel, logPanel, videoGridPanel );

        catsVisionView.setMultivisionPanel( multivisionPanel );

        addMultivisionPanelListeners();

        return multivisionPanel;

    }

    /**
     * Launch CATS Vision for given mac ids
     * 
     * @param macIdList
     *            macId list
     * @throws URISyntaxException
     * @throws MalformedURLException
     */

    public void launchCatsVision( String macId ) throws MalformedURLException, URISyntaxException
    {
        Set< String > macIdSet = new LinkedHashSet< String >();
        macIdSet.add( macId );

        launchCatsVision( macIdSet );
    }

    /**
     * Add listeners for all panels inside MultivisionPanel
     */
    public void addMultivisionPanelListeners()
    {
        MultivisionPanel multivisionPanel = catsVisionView.getMultivisionPanel();

        VideoControlPanel videoControlPanel = multivisionPanel.getVideoControlPanel();

        multivisionPanel.addMouseListener( this );

        multivisionPanel.getVideoGridPanel().addMouseListener( this );

        AllocationAndSelectionPanel allocationAndSelectionPanel = videoControlPanel.getAllocationAndSelectionPanel();

        allocationAndSelectionPanel.addActionListener( this );
    }

    /**
     * Remove listeners for all panels inside MultivisionPanel
     */
    public void removeMultivisionPanelListeners()
    {
        MultivisionPanel multivisionPanel = catsVisionView.getMultivisionPanel();

        VideoControlPanel videoControlPanel = multivisionPanel.getVideoControlPanel();

        multivisionPanel.removeMouseListener( this );

        multivisionPanel.getVideoGridPanel().removeMouseListener( this );

        AllocationAndSelectionPanel allocationAndSelectionPanel = videoControlPanel.getAllocationAndSelectionPanel();

        allocationAndSelectionPanel.removeActionListener( this );
    }

    /**
     * Create Settop from mac ids
     * 
     * @param macIdSet
     *            macId Set
     */
    public Set< Settop > createSettops( Set< String > macIdSet )
    {
        boolean isException = false;
        Map< String, Exception > settopExceptionsMap = new LinkedHashMap< String, Exception >();

        Set< Settop > settops = new LinkedHashSet< Settop >();
        String settopName = "";

        try
        {
            if ( macIdSet != null && !macIdSet.isEmpty() )
            {
                for ( String macId : macIdSet )
                {

                    try
                    {
                        settopName = macId;

                        logger.debug( "Creating settop object for -" + macId );

                        Settop settop = settopFactory.findSettopByHostMac( macId );
                        if ( settop.getVideo() != null )
                        {
                            settops.add( settop );
                            settop.setLocked( true );

                            if ( catsVisionView != null )
                            {
                                int frameRate = catsVisionView.getFrameRate();
                                if ( frameRate != settop.getVideo().getFrameRate() )
                                {
                                    settop.getVideo().setFrameRate( frameRate );
                                }
                            }
                        }
                        else
                        {
                            isException = true;
                            settopExceptionsMap.put( settopName, new SettopNotFoundException(
                                    "Settop does not have Video Provider" ) );
                        }
                    }
                    catch ( SettopNotFoundException settopNotFoundException )
                    {
                        isException = true;

                        settopExceptionsMap.put( settopName, settopNotFoundException );
                    }
                    catch ( Exception exception )
                    {
                        isException = true;

                        settopExceptionsMap.put( settopName, exception );
                    }

                }

            }
        }
        finally
        {
            if ( isException )
            {
                new ErrorConsole( "Settop Creation Error", settopExceptionsMap );
            }
        }
        return settops;
    }

    private boolean checkSettopLimit( Set< String > macIdSet )
    {
        boolean isWithinLimit = true;

        if ( macIdSet != null && !macIdSet.isEmpty() )
        {
            int numberOfAlreadyLaunchedSettops = model.getSettops().size();
            int totalNumberOfSettops = numberOfAlreadyLaunchedSettops + macIdSet.size();

            if ( ( numberOfAlreadyLaunchedSettops > 0 ) && ( totalNumberOfSettops > MULTIVISION_MAX_SETTOPS ) )
            {
                log( "Unable to more launch video. Exceeded the maximum allowed  limit(" + MULTIVISION_MAX_SETTOPS
                        + ")" );
                isWithinLimit = false;
                CatsVisionUtils
                        .showWarning(
                                "Unable to more launch video",
                                "Please note that you have already launched "
                                        + numberOfAlreadyLaunchedSettops
                                        + " settops \nand also selected another "
                                        + macIdSet.size()
                                        + " new settop(s) to be part of \nlaunched video, which exceeds the maximum allowed limit ("
                                        + MULTIVISION_MAX_SETTOPS
                                        + "). \nPlease close existing video or select lesser number of settops \nif you wish to launch more." );

            }
            else if ( macIdSet.size() > MULTIVISION_MAX_SETTOPS )
            {
                log( "Unable to launch video. Exceeded the maximum allowed  limit(" + MULTIVISION_MAX_SETTOPS + ")" );
                isWithinLimit = false;
                CatsVisionUtils.showWarning( "Unable to launch Video",
                        "Please note that the maximum number of settops that is allowed to be launched is "
                                + MULTIVISION_MAX_SETTOPS + ". You have selected " + macIdSet.size() + " settops." );

            }
        }
        return isWithinLimit;
    }

    private void deSelectAll()
    {
        isDeselectAll = true;
        logger.debug( "Inside deSelectAll()" );

        List< VideoPanel > videoPanels = model.getVideoPanels();
        if ( null != videoPanels && !videoPanels.isEmpty() )
        {
            log( "Deselecting All Settops" );

            for ( VideoPanel videoPanel : model.getVideoPanels() )
            {
                JCheckBox selectionCheckBox = videoPanel.getSelectionCheckBox();

                if ( selectionCheckBox.isSelected() )
                {
                    // Deselect settop
                    selectionCheckBox.doClick();
                }
            }
        }
        isDeselectAll = false;
    }

    private void selectAll()
    {
        isSelectAll = true;
        logger.debug( "Inside selectAll()" );

        List< VideoPanel > videoPanels = model.getVideoPanels();
        if ( null != videoPanels && !videoPanels.isEmpty() )
        {
            log( "Selecting All Settops" );

            for ( VideoPanel videoPanel : model.getVideoPanels() )
            {
                JCheckBox selectionCheckBox = videoPanel.getSelectionCheckBox();

                if ( !selectionCheckBox.isSelected() )
                {
                    // Select settop
                    selectionCheckBox.doClick();
                }
            }
            log( "Selected settops - " + model.getAllocatedAndSelectedSettopNames() );
        }
        isSelectAll = false;
    }

    /**
     * Allocate all Settops
     */
    public void allocateAll()
    {
        logger.debug( "Inside allocateAll()" );

        List< VideoPanel > videoPanels = model.getVideoPanels();
        if ( null != videoPanels && !videoPanels.isEmpty() )
        {
            String settopName = "";
            exceptionsMap.clear();
            // model.clearAllSettopAllocations();
            log( "Allocating All Settops" );
            try
            {
                for ( VideoPanel videoPanel : model.getVideoPanels() )
                {
                    try
                    {
                        JButton lockUnlockButton = videoPanel.getLockUnlockButton();

                        if ( lockUnlockButton.getToolTipText().equals( AVAILABLE_SETTOP ) )
                        {
                            allocate( videoPanel.getSettop(), false );
                        }
                    }
                    catch ( Exception exception )
                    {
                        exceptionsMap.put( settopName, exception );
                    }
                }
            }
            finally
            {
                List< String > allocatedSettopNames = model.getAllocatedSettopNames();

                if ( !allocatedSettopNames.isEmpty() )
                {
                    log( "Allocated settops - " + allocatedSettopNames );
                }

                if ( !exceptionsMap.isEmpty() )
                {
                    logException( "Allocation Error occurred for the settops", exceptionsMap );
                    new ErrorConsole( "Allocation Error", exceptionsMap );
                }
            }
        }

    }

    /**
     * Release all Settops
     */
    public void releaseAll()
    {
        logger.debug( "Inside releaseAll()" );
        isDeselectAll = true;
        List< VideoPanel > videoPanels = model.getVideoPanels();
        if ( null != videoPanels && !videoPanels.isEmpty() )
        {
            String settopName = "";
            exceptionsMap.clear();
            // model.clearAllSettopAllocations();
            log( "Releasing All Settops" );
            try
            {
                for ( VideoPanel videoPanel : model.getVideoPanels() )
                {
                    try
                    {
                        JButton lockUnlockButton = videoPanel.getLockUnlockButton();

                        if ( lockUnlockButton.getToolTipText().equals( ALLOCATED_SETTOP ) )
                        {
                            release( videoPanel.getSettop(), false );
                        }
                    }
                    catch ( Exception exception )
                    {
                        exceptionsMap.put( settopName, exception );
                    }
                }
            }
            finally
            {
                if ( !exceptionsMap.isEmpty() )
                {
                    logException( "Release Error occurred for the settops", exceptionsMap );
                    new ErrorConsole( "Release Error", exceptionsMap );
                }
            }
        }
        isDeselectAll = false;
    }

    /**
     * Allocate settop
     * 
     * @param settop
     *            settop to be allocated
     * @param showErrorMessage
     *            if error message should be displayed then pass true, else
     *            false.
     * @return true, if allocation is successful, else false
     */
    public boolean allocate( Settop settop, boolean showErrorMessage )
    {
        boolean isSettopAllocated = false;
        if ( settop != null )
        {
            if ( showErrorMessage )
            {
                exceptionsMap.clear();
            }
            if ( settop != null )
            {
                try
                {
                    accessEnforcer.lock( settop );
                    logger.debug( "Allocated -" + settop.getHostMacAddress() );

                    isSettopAllocated = true;
                    VideoPanel videoPanel = model.getVideoPanel( settop );
                    videoPanel.setEnabledSelectionCheckBox( true );
                    videoPanel.showLockButton();
                    model.setAllocated( settop, true );
                }
                catch ( AllocationException allocationException )
                {
                    logger.debug( "AllocationException : " + allocationException.getMessage() );
                    exceptionsMap.put( settop.getHostMacAddress(), allocationException );
                }
                catch ( Exception exception )
                {
                    logger.debug( "Exception : " + exception.getMessage() );
                    exceptionsMap.put( settop.getHostMacAddress(), exception );
                }
                finally
                {
                    if ( showErrorMessage )
                    {
                        log( "Allocated settop - " + settop.getHostMacAddress() );
                        logException( "Allocation Error occurred for the settop", exceptionsMap );
                        new ErrorConsole( "Allocation Error", exceptionsMap );
                    }
                }
            }
        }
        return isSettopAllocated;
    }

    /**
     * Release settop
     * 
     * @param settop
     *            settop to be released
     * @param showErrorMessage
     *            if error message should be displayed then pass true, else
     *            false.
     * @return true, if allocation is successful, else false
     */
    public boolean release( Settop settop, boolean showErrorMessage )
    {
        boolean isSettopReleased = false;
        if ( settop != null )
        {
            if ( showErrorMessage )
            {
                exceptionsMap.clear();
            }
            if ( settop != null )
            {
                try
                {
                    accessEnforcer.release( settop );

                    logger.debug( "Released -" + settop.getHostMacAddress() );

                    isSettopReleased = true;
                    VideoPanel videoPanel = model.getVideoPanel( settop );
                    // Deselect a settop, if it is already selected
                    if ( model.isSelected( settop ) )
                    {
                        videoPanel.getSelectionCheckBox().doClick();
                    }

                    videoPanel.setEnabledSelectionCheckBox( false );
                    videoPanel.showUnLockButton();
                    model.setAllocated( settop, false );

                    String macID = settop.getHostMacAddress();
                    // Remove Trace tab for the settop if it is present
                    traceController.removeTraceTab( macID );
                }
                catch ( AllocationException allocationException )
                {
                    logger.debug( "AllocationException : " + allocationException.getMessage() );
                    exceptionsMap.put( settop.getHostMacAddress(), allocationException );
                }
                catch ( Exception exception )
                {
                    logger.debug( "Exception : " + exception.getMessage() );
                    exceptionsMap.put( settop.getHostMacAddress(), exception );
                }
                finally
                {
                    if ( showErrorMessage )
                    {
                        log( "Released settop - " + settop.getHostMacAddress() );
                        logException( "Release Error occurred for the settop", exceptionsMap );
                        new ErrorConsole( "Release Error", exceptionsMap );
                    }
                }
            }
        }
        return isSettopReleased;
    }

    /**
     * Log key presses performed in remote
     * 
     * @param text
     *            text to be logged.
     */
    public void log( String text )
    {
        LogPanel logPanel = catsVisionView.getMultivisionPanel().getLogPanel();

        if ( ( text != null ) && ( logPanel != null ) )
        {
            logPanel.logText( CatsVisionUtils.getDateTime() + text + "\n" );
        }
    }

    /**
     * Log Exception
     * 
     * @param errorMessage
     *            error message to be logged
     * @param exceptionsMap
     *            contains <settop name,exception for the settop> as <key,
     *            value> pair
     */
    public void logException( String errorMessage, Map< String, Exception > exceptionsMap )
    {
        if ( ( errorMessage != null ) && ( exceptionsMap != null ) && ( !exceptionsMap.isEmpty() ) )
        {
            int i = 0;
            StringBuilder str = new StringBuilder( errorMessage + " : " + exceptionsMap.keySet() + "\n" );
            for ( String macID : exceptionsMap.keySet() )
            {
                str.append( "  " + ++i + " ) " + exceptionsMap.get( macID ) + "\n" );
            }
            log( str.toString() );
        }
    }

    /**
     * Refreshing the main panel
     */
    public void refreshVideoGridPanel()
    {
        Dimension dim = calculateVideoDimension( videoGridPanel.getSize() );

        int settopSize = model.getSettops().size();
        int cols = calculateColumns( settopSize );
        int rows = calculateRows( settopSize, cols );

        videoGridPanel.refreshRowsAndColumns( rows, cols );
        List< VideoPanel > videoPanels = model.getVideoPanels();
        videoGridPanel.resizeVideoPanels( videoPanels, dim );

        videoGridPanel.arrangePanels( videoPanels );
        refreshMultivisionPanel();
    }

    /*
     * Set FrameRate for all video panels rate.
     */
    public void setFrameRate( int frameRate )
    {
        Set< Settop > settops = model.getSettops();
        if ( ( settops != null ) && ( !settops.isEmpty() ) )
        {
            for ( Settop settop : settops )
            {
                SetVideoFrameRateTask setFrameRateTask = new SetVideoFrameRateTask( settop, frameRate );
                managedThreadPool.addTask( setFrameRateTask );
            }
        }
        logger.debug( "Frame rate = " + frameRate );

    }

    /**
     * Stop video for all video panels
     */
    public void destroyVideoPanels()
    {
        logger.debug( "Stop All Video" );

        final List< VideoProvider > videoProviders = model.getVideoProviders();
     
        if ( ( videoProviders != null ) && ( !videoProviders.isEmpty() ) )
        {
            new Thread( new Runnable()
            {
                public void run()
                {
                    for ( VideoProvider videoProvider : videoProviders )
                    {
                        videoProvider.disconnectVideoServer();
                    }
                }
            } ).start();
        }
    }

    /**
     * Start video for all video panels rate.
     */
    public void startAllVideo()
    {
        logger.debug( "Start All Video" );
        List< VideoProvider > videoProviders = model.getVideoProviders();
        if ( ( videoProviders != null ) && ( !videoProviders.isEmpty() ) )
        {
            for ( final VideoProvider videoProvider : videoProviders )
            {

                new Thread( new Runnable()
                {
                    public void run()
                    {
                        if ( !videoProvider.isStreaming() )
                        {
                            videoProvider.connectVideoServer();
                        }
                    }
                } ).start();
            }
        }
    }

    /**
     * Launches Zoomed VideoPanel
     * 
     * @param videoPanel
     *            instance of VideoPanel
     */
    public void launchZoomedVideoPanel( VideoPanel videoPanel )
    {
        if ( videoPanel != null )
        {
            if ( zoomedVideo != null )
            {
                disposeZoomedVideo();
            }
            zoomedVideo = new ZoomedVideo( videoPanel );
            zoomedVideo.addWindowListener( this );
        }
    }

    /**
     * Get ZoomFrame
     * 
     * @return instance of ZoomFrame
     */
    public ZoomedVideo getZoomedVideo()
    {
        return zoomedVideo;
    }

    public boolean isSettopAlreadyZoomed( String settopName )
    {
        if ( ( settopName != null ) && ( getZoomedVideo() != null ) )
        {
            return zoomedVideo.getVideoPanel().getSelectionCheckBox().getText().equalsIgnoreCase( settopName );
        }
        return false;
    }

    @Override
    public void catsEventPerformed( CatsEvent catsEvent )
    {
        logger.debug( "catsEventPerformed" );
        if ( catsEvent instanceof CatsResponseEvent )
        {
            handleCatsResponseEvent( ( CatsResponseEvent ) catsEvent );
        }
        else if ( catsEvent instanceof AllocationEvent )
        {
            handleAllocationEvent( ( AllocationEvent ) catsEvent );
        }
        else if ( catsEvent instanceof ScriptPlayBackEvent )
        {
            handleScriptPlayBackEvent( ( ScriptPlayBackEvent ) catsEvent );
        }
        else
        {
            handleCatsEvent( catsEvent );
        }
    }

    private void handleAllocationEvent( AllocationEvent ae )
    {
        logger.debug( "Allocation Broken for " + ae.getSettopId() );
        for ( Settop settop : model.getAllocatedSettops() )
        {
            if ( ( ae.isBroken() ) && ( settop != null ) && ( settop.getId().equals( ae.getSettopId() ) ) )
            {
                release( settop, true );
                CatsVisionUtils.showError( "Allocation was broken for setoop " + settop.getHostMacAddress() );
                break;
            }
        }
    }

    private void handleCatsEvent( CatsEvent catsEvent )
    {
        for ( Settop settop : model.getAllocatedAndSelectedSettops() )
        {
            if ( catsEvent instanceof PowerEvent )
            {
                PowerEvent powerEvent = ( PowerEvent ) catsEvent;

                log( "Power Key pressed for Settop -  " + settop.getHostMacAddress() + "[ Key = "
                        + powerEvent.getActionCommand().toString() + "]" );

                PowerPressKeyTask powerPressKeyTask = new PowerPressKeyTask( settop, powerEvent );
                managedThreadPool.addTask( powerPressKeyTask );

            }
            else if ( catsEvent instanceof RemoteEvent )
            {
                RemoteEvent remoteEvent = ( RemoteEvent ) catsEvent;

                if ( remoteEvent.getActionType() == ActionType.TUNE )
                {
                    log( "Remote Key pressed for Settop -  " + settop.getHostMacAddress() + "[ Action Type = "
                            + remoteEvent.getActionType() + ", Channel = " + remoteEvent.getChannelNumber() + "]" );
                }
                else
                {
                    log( "Remote Key pressed for Settop -  " + settop.getHostMacAddress() + "[ Action Type = "
                            + remoteEvent.getActionType() + ", Key = " + remoteEvent.getRemoteCommand() + "]" );
                }
                RemotePressKeyTask remotePressKeyTask = new RemotePressKeyTask( settop, remoteEvent );
                managedThreadPool.addTask( remotePressKeyTask );

            }
            else if ( catsEvent instanceof ConfigButtonEvent )
            {

                log( "DIAG SCREEN  button pressed for Settop - " + settop.getHostMacAddress() + " [ Make="
                        + settop.getSettopInfo().getMake() + ", Manufacturer= "
                        + settop.getSettopInfo().getManufacturer() + "]" );

                ConfigButtonTask configButtonTask = new ConfigButtonTask( settop, ( ConfigButtonEvent ) catsEvent );
                managedThreadPool.addTask( configButtonTask );
            }
        }
    }

    private void handleScriptPlayBackEvent( ScriptPlayBackEvent playBackEvent )
    {
        logger.debug( "ScriptPlayBackEvent - CatsEventType.SCRIPT_PLAY_BACK" );
        Set< Settop > allocatedAndSelectedSettops = model.getAllocatedAndSelectedSettops();
        if ( allocatedAndSelectedSettops.isEmpty() )
        {
            CatsVisionUtils.showError( "Please allocate and select settop(s) before doing playback." );
        }
        else
        {
            log( "Doing script playback." );

            // Changing the focus from script tab to CatsVision main tab.
            catsVisionView.getVisionTabs().setSelectedIndex( 0 );

            for ( Settop settop : allocatedAndSelectedSettops )
            {
                if ( settop.getVideo().isStreaming() )
                {
                    ScriptPlayBackTask playBackTask = new ScriptPlayBackTask( settop, playBackEvent );
                    managedThreadPool.addTask( playBackTask );
                }
                else
                {
                    logger.error( "Playback cannot be done. Video is not streaming in Settop: "
                            + settop.getHostMacAddress() );
                    log( "Playback cannot be done. Video is not streaming in Settop: " + settop.getHostMacAddress() );
                }
            }
        }
    }

    private void handleCatsResponseEvent( CatsResponseEvent catsResponseEvent )
    {
        if ( catsResponseEvent.getType().equals( CatsEventType.REMOTE_FAIL_RESPONSE ) )
        {
            logger.debug( "CatsResponseEvent - CatsEventType.REMOTE_FAIL_RESPONSE" );
        }
        else if ( catsResponseEvent.getType().equals( CatsEventType.POWER_FAIL_RESPONSE ) )
        {
            logger.debug( "CatsResponseEvent - CatsEventType.POWER_FAIL_RESPONSE" );
        }
        else if ( catsResponseEvent.getType().equals( CatsEventType.CATS_CONFIG_BUTTON_EVENT_FAIL_RESPONSE ) )
        {
            logger.debug( "CatsResponseEvent - CatsEventType.CATS_CONFIG_BUTTON_EVENT_FAIL_RESPONSE" );
        }

        logger.debug( catsResponseEvent.getMessage() );
        log( catsResponseEvent.getMessage() );
    }

    @Override
    public void windowClosing( WindowEvent windowEvent )
    {
        Object source = windowEvent.getSource();
        if ( source instanceof ZoomedVideo )
        {
            disposeZoomedVideo();

        }
        else if ( source instanceof TabbedFrame )
        {
            TabbedFrame tabbedDialog = ( TabbedFrame ) source;
            if ( tabbedDialog.getName().equals( SETTOP_INFO_DIALOG_NAME ) )
            {
                cleanSettopInfoDialog();
            }
        }
    }

    private void disposeZoomedVideo()
    {
        if ( zoomedVideo != null )
        {
            logger.debug( "Closing ZoomFrame" );
            refreshVideoGridPanel();
            zoomedVideo.dispose();
            zoomedVideo = null;
        }
    }

    public void resizePanels( Dimension videoGridDimension )
    {
        Set< Settop > settops = model.getSettops();
        if ( ( settops != null ) && ( !settops.isEmpty() ) )
        {
            Dimension videoDimension = calculateVideoDimension( videoGridDimension );
            for ( VideoPanel videoPanel : videoGridPanel.getVideoPanels() )
            {
                videoPanel.setMinimumSize( videoDimension );
                videoPanel.setPreferredSize( videoDimension );
            }
            videoGridPanel.revalidate();

            catsVisionView.getScrollPane( "multiVisionPane" ).revalidate();
        }
    }

    /**
     * Calculate dimension of video panel
     * 
     * @param dimension
     *            Dimension of the VideoGridPanel
     * @return Dimension of video display panel
     */
    public Dimension calculateVideoDimension( Dimension dimension )
    {
        int numberOfPanels = model.getSettops().size();
        int cols = calculateColumns( numberOfPanels );
        int rows = calculateRows( numberOfPanels, cols );

        Dimension videoDimension = null;
        if ( ( cols > 0 ) && ( rows > 0 ) )
            videoDimension = new Dimension( ( int ) ( ( dimension.width / cols ) ), ( int ) ( dimension.height / rows ) );

        return videoDimension;
    }

    /**
     * Fix the number of columns based on number of panels
     * 
     * @param numberOfPanels
     *            number of panels
     */
    public int calculateColumns( int numberOfPanels )
    {
        logger.debug( "Fixing the number of columns in video grid." );

        int columns = 1;

        if ( ( numberOfPanels == 1 ) || ( numberOfPanels == 2 ) )
        {
            columns = numberOfPanels;
        }
        else if ( numberOfPanels > 2 && numberOfPanels <= 4 )
        {
            columns = 2;
        }
        else if ( numberOfPanels > 4 && numberOfPanels <= 9 )
        {
            columns = 3;
        }
        else if ( numberOfPanels > 9 )
        {
            columns = 4;
        }
        return columns;
    }

    /**
     * Calculates number of rows based on number of panels and columns
     * 
     * @param numberOfPanels
     *            number of panels
     * 
     * @param cols
     *            number of columns
     */
    public int calculateRows( int numberOfPanels, int cols )
    {
        logger.debug( "Calculating the number of columns in video grid." );
        int rows = 0;

        if ( numberOfPanels > 0 )
        {
            int rem = numberOfPanels % cols;

            if ( rem == 0 )
            {
                rows = ( numberOfPanels ) / cols;
            }
            else
            {
                rows = ( numberOfPanels + ( cols - 1 ) ) / cols;
            }
        }
        return rows;
    }

    /**
     * Get CATSVisionView
     * 
     * @return catsVisionView
     */
    public CATSVisionView getCatsVisionView()
    {
        return catsVisionView;
    }

    /**
     * Set CATSVisionView
     * 
     * @param catsVisionView
     */
    public void setCatsVisionView( CATSVisionView catsVisionView )
    {
        this.catsVisionView = catsVisionView;
    }

    @Override
    public void mouseClicked( MouseEvent mouseEvent )
    {
        Object source = mouseEvent.getSource();

        if ( source instanceof VideoDisplayPanel )
        {
            source = ( ( VideoDisplayPanel ) source ).getParent();
        }
        if ( source instanceof VideoPanel )
        {
            VideoPanel videoPanel = ( VideoPanel ) source;
            // DE3410
            videoPanel.requestFocus();

            if ( ( mouseEvent.getClickCount() == 2 )
                    && ( !isSettopAlreadyZoomed( videoPanel.getSelectionCheckBox().getText() ) ) )
            {
                videoPanel.setVisible( false );
                launchZoomedVideoPanel( videoPanel );
            }
        }
        else if ( source instanceof MultivisionPanel )
        {
            MultivisionPanel multivisionPanel = ( MultivisionPanel ) source;
            // DE3410
            multivisionPanel.requestFocus();
        }
        else if ( source instanceof VideoGridPanel )
        {
            VideoGridPanel videoGridPanel = ( VideoGridPanel ) source;
            // DE3410
            videoGridPanel.requestFocus();
        }
    }

    @Override
    public void mouseEntered( MouseEvent mouseEvent )
    {

    }

    @Override
    public void mouseExited( MouseEvent mouseEvent )
    {

    }

    @Override
    public void mousePressed( MouseEvent mouseEvent )
    {

    }

    @Override
    public void mouseReleased( MouseEvent mouseEvent )
    {
        popUpMenu( mouseEvent );
    }

    private void popUpMenu( MouseEvent mouseEvent )
    {
        Object source = mouseEvent.getSource();

        if ( source instanceof VideoDisplayPanel )
        {
            source = ( ( VideoDisplayPanel ) source ).getParent();
        }

        if ( source instanceof VideoPanel )
        {
            if ( mouseEvent.isPopupTrigger() )
            {
                String macID = ( ( VideoPanel ) source ).getSelectionCheckBox().getText();

                currentSettop = model.getSettop( macID );

                enableOrDisableRightClickOptions();
                rightClickMenu.show( mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY() );
            }
        }
    }

    /*
     * Enable or disable right click menu items based on the presence of it's
     * provider
     */
    private void enableOrDisableRightClickOptions()
    {
        rightClickMenu.refreshMenu();

        if ( model.getAllocatedSettops().contains( currentSettop ) )
        {
            if ( !( currentSettop.getTracePath() == null ) )
            {
                rightClickMenu.getTraceMenuItem().setEnabled( true );
            }
        }
    }

    @Override
    public void itemStateChanged( ItemEvent evt )
    {
        if ( evt.getSource() instanceof JCheckBox )
        {
            JCheckBox checkBox = ( JCheckBox ) evt.getSource();

            String checkboxText = checkBox.getText();

            if ( checkboxText.contains( SELECT_ALL_SETTOPS ) )
            {
                // performAllocateAllItemStateChange( checkBox );
            }
        }
    }

    /*
     * Method to handle selection and deselection
     */
    private void handleSelectionAndDeselection( Settop settop, boolean value )
    {
        boolean isSelected = model.isSelected( settop );

        boolean isLocked = model.isAllocated( settop );

        if ( isLocked && !isSelected )
        {
            select( settop );
        }
        else if ( isSelected )
        {
            deSelect( settop );
        }
    }

    public void refreshMultivisionPanel()
    {
        catsVisionView.getMultivisionPanel().revalidate();
        catsVisionView.getMultivisionPanel().repaint();
    }

    /*
     * Create all VideoPanels
     */
    public List< VideoPanel > createVideoPanels( Set< Settop > settops )
    {
        logger.debug( "Creating Video panels" );
        List< VideoPanel > videoPanels = new LinkedList< VideoPanel >();
        for ( Settop settop : settops )
        {
            videoPanels.add( createVideoPanel( settop ) );
        }
        return videoPanels;
    }

    /*
     * Create VideoPanel
     */
    private VideoPanel createVideoPanel( Settop settop )
    {
        logger.debug( "Creating Video panel for " + settop.getHostMacAddress() );
        
        VideoProvider videoProvider = settop.getVideo();

        VideoDisplayPanel vdPanel = new VideoDisplayPanel( );
        vdPanel.addMouseListener( this );
        vdPanel.setToolTipText( getSettopInfoToolTipText( ( SettopInfo ) settop ) );
        VideoPanel panel = new VideoPanel( settop, vdPanel );
        catsEventDispatcherThreaded.addListener( vdPanel, CatsEventType.VIDEO, videoProvider );
        panel.addMouseListener( this );

        VideoPanelListener listener = new VideoPanelListener( settop, panel );

        panel.addActionListener( listener );
        panel.addItemListener( listener );

        model.setVideoPanel( settop, panel );
        return panel;
    }

    /**
     * To enable and disable keyboard shortcuts
     * 
     * @param value
     */
    public void enableKeyboardShortcuts( boolean value )
    {
        remoteController.enableKeyboardShortcuts( value );
    }

    public void doAction( final String text, final Settop settop )
    {
        Thread thread = new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                if ( text.equals( TRACE ) )
                {
                    // Add trace tab for the settop
                    traceController.addTraceTab( settop );
                }
                else if ( text.equals( SNAP_IMAGE ) )
                {
                    imageController.addImageCompareTab( settop );
                }
                else if ( text.equals( SETTOP_INFO ) )
                {
                    String mac = settop.getHostMacAddress();
                    if ( !settopInfoTabbedFrame.isTabExists( mac ) )
                    {
                        addSettopInfoTab( settop );
                    }
                }
            }
        } );

        thread.start();
    }

    protected void addSettopInfoTab( Settop settop )
    {
        String macID = settop.getHostMacAddress();
        if ( !settopInfoMap.containsKey( macID ) )
        {
            SettopInfoPanel settopInfoPanel = new SettopInfoPanel( ( SettopInfo ) settop );
            settopInfoMap.put( macID, settopInfoPanel );
            settopInfoTabbedFrame.addTab( macID, settopInfoPanel );
        }
        if ( !settopInfoTabbedFrame.isVisible() )
        {
            settopInfoTabbedFrame.setVisible( true );
        }
    }

    private void cleanSettopInfoDialog()
    {
        settopInfoTabbedFrame.removeAllTabs();
        settopInfoTabbedFrame.setVisible( false );
        settopInfoMap.clear();
    }

    private void deSelect( Settop settop )
    {
        model.setSelected( settop, false );
        if ( !isDeselectAll )
        {
            log( "Deselected settop - " + settop.getHostMacAddress() );
        }
    }

    private void select( Settop settop )
    {
        model.setSelected( settop, true );
        if ( !isSelectAll )
        {
            log( "Selected settop - " + settop.getHostMacAddress() );
        }
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        AllocationAndSelectionPanel allocationAndSelectionPanel = catsVisionView.getMultivisionPanel()
                .getVideoControlPanel().getAllocationAndSelectionPanel();
        Object source = e.getSource();

        /* 
        if ( source == allocationAndSelectionPanel.getLockButton() )
        {
            allocateAll();
        }
        else if ( source == allocationAndSelectionPanel.getUnlockButton() )
        {
            if ( model.getAllocatedSettops().size() > 0 )
            {
                releaseAll();
            }
        }
        else */ if ( source == allocationAndSelectionPanel.getSelectButton() )
        {
            if ( model.getAllocatedSettops().size() > 0 )
            {
                selectAll();
            }
            else
            {
                CatsVisionUtils.showWarning( "Unable to select settops",
                        "Please allocate settop(s) before performing this action." );
            }
        }
        else if ( source == allocationAndSelectionPanel.getDeselectButton() )
        {
            if ( model.getAllocatedAndSelectedSettops().size() > 0 )
            {
                deSelectAll();
            }
        }
        else if ( source instanceof JMenuItem )
        {
            JMenuItem menuItem = ( JMenuItem ) source;

            String menuItemText = menuItem.getText();

            if ( ( menuItemText.equals( SNAP_IMAGE ) ) || ( menuItemText.equals( TRACE ) )
                    || ( menuItemText.equals( SETTOP_INFO ) ) )
            {
                if ( currentSettop != null )
                {
                    doAction( menuItemText, currentSettop );
                }
            }
        }
    }

    /**
     * Inner class handling the key events for close video and start/stop
     * streaming.
     * 
     * @author bobyemmanuvel
     * 
     */
    class VideoPanelListener implements ActionListener, ItemListener
    {
        Settop     settop = null;
        VideoPanel panel  = null;

        VideoPanelListener( Settop settop, VideoPanel panel )
        {
            this.settop = settop;
            this.panel = panel;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            if ( source == panel.getCloseButton() )
            {
                if ( zoomedVideo != null && zoomedVideo.getVideoPanel() == panel )
                {
                    zoomedVideo.dispose();
                }
                imageController.removeImageTab( panel.getName() );
                if ( model.isAllocated( settop ) )
                {
                    release( settop, true );
                }
                settop.getVideo().disconnectVideoServer();
                model.removeSettop( settop );
                panel.removeActionListener( this );
                panel.removeItemListener( this );
                // Remove SettopInfo tab for the settop if it is present
                removeSettopInfoTab( settop.getHostMacAddress() );
                videoGridPanel.removeVideoPanelFromGrid( panel );
                refreshVideoGridPanel();
                videoGridPanel.doLayout();
                videoGridPanel.repaint();
                log( "[" + settop.getHostMacAddress() + "] Settop closed" );

            }
            else if ( source == panel.getStreamStopButton() )
            {
                VideoProvider provider = settop.getVideo();
                if ( provider.isStreaming() )
                {

                    provider.disconnectVideoServer();
                    panel.showStartStreamingButton();
                    log( "[" + settop.getHostMacAddress() + "] Streaming stopped" );
                }
                else
                {
                    provider.connectVideoServer();
                    panel.showStopStreamingButton();
                    log( "[" + settop.getHostMacAddress() + "] Streaming started" );
                }
            }
            else if ( source == panel.getLockUnlockButton() )
            {
                boolean isLocked = model.isAllocated( settop );

                if ( isLocked )
                {
                    release( settop, true );
                }
                else
                {
                    allocate( settop, true );
                }
            }
        }

        @Override
        public void itemStateChanged( ItemEvent evt )
        {
            if ( evt.getSource() == panel.getSelectionCheckBox() )
            {
                handleSelectionAndDeselection( settop, true );
            }
        }
    }
}
