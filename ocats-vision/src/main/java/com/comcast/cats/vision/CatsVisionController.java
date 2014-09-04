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
package com.comcast.cats.vision;

import static com.comcast.cats.vision.util.CatsVisionConstants.ABOUT;
import static com.comcast.cats.vision.util.CatsVisionConstants.EMPTY_STRING;
import static com.comcast.cats.vision.util.CatsVisionConstants.FPS;
import static com.comcast.cats.vision.util.CatsVisionConstants.KEYBOARD_SHORTCUTS;
import static com.comcast.cats.vision.util.CatsVisionConstants.MY_SETTOPS;
import static com.comcast.cats.vision.util.CatsVisionConstants.PREFERENCES;
import static com.comcast.cats.vision.util.CatsVisionConstants.SCRIPT_PANEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.SNAP_IMAGE;
import static com.comcast.cats.vision.util.CatsVisionConstants.TRACE;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.vision.panel.configuration.ConfigPanel;
import com.comcast.cats.vision.panel.configuration.ConfigPanelController;
import com.comcast.cats.vision.panel.video.FrameRateType;
import com.comcast.cats.vision.panel.videogrid.MultivisionPanel;
import com.comcast.cats.vision.panel.videogrid.VideoGridController;
import com.comcast.cats.vision.panel.videogrid.model.GridDataModel;
import com.comcast.cats.vision.script.ScriptController;
import com.comcast.cats.vision.util.CATSVisionLoadingDialog;
import com.comcast.cats.vision.util.CatsVisionConstants;

/**
 * Controller for CatsVisionView
 * 
 * @author aswathyann
 * 
 */
@Named
public class CatsVisionController implements ComponentListener, MouseListener, WindowListener, ActionListener,
        ItemListener
{

    private CATSVisionView              catsVisionView;

    private JMenu                       snapImageMenu;

    private JMenu                       traceMenu;

    private GridDataModel               gridDataModel;

    private final ScriptController      scriptController;

    private final VideoGridController   videoGridController;

    private final ConfigPanelController configPanelController;

    private final ConfigPanel           configPanel;

    private CatsProperties              catsProperties;

    private Dimension                   catsVisionViewDimension;

    private String                      authToken;

    private String                      menuText;

    private Set< String >               macIdSet;

    private static final String         NO_SETTOPS_ALLOCATED = "No settops allocated.";

    private static final int            FRAME_BORDER_WIDTH   = 54;

    private static final int            FRAME_BORDER_HEIGHT  = 2;

    private static Logger               logger               = Logger.getLogger( CatsVisionController.class );

    @Inject
    public CatsVisionController( ApplicationContext appContext )
    {
        logger.debug( "Starting CatsVisionController" );

        /**
         * Retrieve all our beans from the application context.
         */
        catsProperties = appContext.getBean( CatsProperties.class );

        videoGridController = appContext.getBean( VideoGridController.class );

        gridDataModel = appContext.getBean( GridDataModel.class );

        scriptController = appContext.getBean( ScriptController.class );

        configPanel = appContext.getBean( ConfigPanel.class );

        configPanelController = appContext.getBean( ConfigPanelController.class );

        authToken = catsProperties.getAuthToken();

        macIdSet = parseMacIDString( catsProperties.getDefaultSettopMac() );
    }

    private Set< String > parseMacIDString( String macIDsString )
    {

        Set< String > macIdSet = new LinkedHashSet< String >();

        if ( ( null != macIDsString ) && !( macIDsString.isEmpty() ) )
        {
            String[] macIDs = macIDsString.split( "," );

            for ( String macID : macIDs )
            {
                macIdSet.add( macID );
            }
        }

        return macIdSet;
    }

    @Override
    public void componentHidden( ComponentEvent event )
    {

    }

    @Override
    public void componentMoved( ComponentEvent event )
    {

    }

    @Override
    public void componentResized( ComponentEvent event )
    {
        if ( event.getSource() instanceof JFrame )
        {
            JFrame catsVisionFrame = ( JFrame ) event.getSource();

            Dimension currentCatsVisionDimension = catsVisionFrame.getSize();

            if ( ( catsVisionViewDimension == null )
                    || ( ( catsVisionViewDimension.getWidth() != currentCatsVisionDimension.width ) && ( ( catsVisionViewDimension
                            .getHeight() != currentCatsVisionDimension.height ) ) ) )
            {
                catsVisionViewDimension = currentCatsVisionDimension;
                // videoGridController.setVideoGridSize();

                Dimension videoControlPanelDimension = catsVisionView.getMultivisionPanel().getVideoControlPanel()
                        .getSize();

                MultivisionPanel multivisionPanel = catsVisionView.getMultivisionPanel();

                Dimension logPanelDimension = calculateLogPanelDimension( catsVisionViewDimension,
                        videoControlPanelDimension );
                multivisionPanel.getLogPanel().setPanelSize( logPanelDimension );

                Dimension videoGridDimension = calculateVideoGridDimension( catsVisionViewDimension,
                        videoControlPanelDimension );
                multivisionPanel.getVideoGridPanel().setPanelSize( videoGridDimension );

                videoGridController.resizePanels( videoGridDimension );
            }
        }
    }

    private Dimension calculateLogPanelDimension( Dimension catsVisionDimension, Dimension videoControlPanelDimension )
    {
        Dimension logDimension = new Dimension(
                ( catsVisionDimension.width - ( videoControlPanelDimension.width + FRAME_BORDER_WIDTH ) ),
                ( int ) ( videoControlPanelDimension.height * 0.3 - FRAME_BORDER_HEIGHT ) );
        return logDimension;
    }

    private Dimension calculateVideoGridDimension( Dimension catsVisionDimension, Dimension videoControlPanelDimension )
    {
        Dimension videoGridDimension = new Dimension(
                ( catsVisionDimension.width - ( videoControlPanelDimension.width + FRAME_BORDER_WIDTH ) ),
                ( int ) ( videoControlPanelDimension.height * 0.7 - FRAME_BORDER_HEIGHT ) );
        return videoGridDimension;
    }

    @Override
    public void componentShown( ComponentEvent event )
    {

    }

    @Override
    public void mouseClicked( MouseEvent event )
    {

    }

    @Override
    public void mouseEntered( MouseEvent event )
    {
        processMouseEvent( event );
    }

    @Override
    public void mouseExited( MouseEvent event )
    {
    }

    @Override
    public void mousePressed( MouseEvent event )
    {
        processMouseEvent( event );
    }

    private void processMouseEvent( MouseEvent event )
    {

        Object source = event.getSource();

        if ( source instanceof JMenu )
        {
            JMenu menu = ( JMenu ) source;
            String mText = menu.getText();
            /*
             * Fix to DE925
             */
            if ( !mText.equals( menuText ) )
            {
                /*
                 * Remove existing menu items and add new menu items only if the
                 * menu is different
                 */
                menuText = mText;
                menu.removeAll();
                addMenuItems( event );
            }
        }
    }

    private void addMenuItems( MouseEvent event )
    {
        if ( menuText.equals( TRACE ) )
        {
            addTraceMenuItems();
        }
        else if ( menuText.equals( SNAP_IMAGE ) )
        {
            addSnapImageMenuItems();
        }

    }

    @Override
    public void mouseReleased( MouseEvent event )
    {

    }

    private void addSnapImageMenuItems()
    {

        snapImageMenu = catsVisionView.getSnapImageMenu();

        addSettopMenuItems( snapImageMenu, gridDataModel.getSettops() );

    }

    private void addTraceMenuItems()
    {
        traceMenu = catsVisionView.getTraceMenu();

        Set< Settop > allocatedSettops = gridDataModel.getAllocatedSettops();

        if ( allocatedSettops.isEmpty() )
        {
            traceMenu.setToolTipText( NO_SETTOPS_ALLOCATED );
        }
        else
        {
            traceMenu.setToolTipText( EMPTY_STRING );

            Set< Settop > settopsWithTrace = gridDataModel.getAllocatedSettopsWithTrace();

            if ( settopsWithTrace.isEmpty() )
            {
                traceMenu.setToolTipText( "No trace associated with the settops." );
            }
            else
            {
                addSettopMenuItems( traceMenu, settopsWithTrace );
            }
        }
    }

    private void addSettopMenuItems( final JMenuItem parentMenu, final Set< Settop > settops )
    {
        ButtonGroup buttonGroup = new ButtonGroup();

        for ( Settop settop : settops )
        {
            JMenuItem settopMenuItem = new JMenuItem( settop.getHostMacAddress() );

            parentMenu.add( settopMenuItem );

            settopMenuItem.setVisible( true );

            buttonGroup.add( settopMenuItem );
            /*
             * Adding ActionListener to each settop menu item
             */
            settopMenuItem.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent actionEvent )
                {
                    for ( Settop settop : settops )
                    {
                        if ( settop.getHostMacAddress().equals( ( ( JMenuItem ) actionEvent.getSource() ).getText() ) )
                        {
                            videoGridController.doAction( parentMenu.getText(), settop );
                        }
                    }
                }
            } );
        }
    }

    public CATSVisionView getCatsVisionView()
    {
        return catsVisionView;
    }

    public void setCatsVisionView( CATSVisionView catsVisionView )
    {
        this.catsVisionView = catsVisionView;
    }

    @Override
    public void windowOpened( WindowEvent e )
    {
    }

    /**
     * We must stop the Settop on close so things get cleaned up properly.
     */
    @Override
    public void windowClosing( WindowEvent event )
    {
        Object source = event.getSource();

        if ( source instanceof JFrame )
        {
            JFrame frame = ( JFrame ) source;

            if ( frame.getTitle().equals( CatsVisionConstants.APPLICATION_TITLE ) )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Closing " + CatsVisionConstants.APPLICATION_TITLE );
                }
                try
                {
                    releaseSettops();
                    videoGridController.removeListeners();
                    videoGridController.removeMultivisionPanelListeners();
                    removeCatsVisionViewListeners();
                }
                catch ( Exception ex )
                {
                    logger.error( "Caught nasty stopSettop exception on exit", ex );

                }
            }
        }
    }

    @Override
    public void windowClosed( WindowEvent e )
    {
    }

    @Override
    public void windowIconified( WindowEvent e )
    {
    }

    @Override
    public void windowDeiconified( WindowEvent e )
    {
    }

    @Override
    public void windowActivated( WindowEvent e )
    {
        CATSVisionLoadingDialog.hideDialog();
    }

    @Override
    public void windowDeactivated( WindowEvent e )
    {
    }

    public void releaseSettops()
    {
        try
        {
            new Thread( new Runnable()
            {
                public void run()
                {
                    MultivisionPanel multivisionPanel = videoGridController.getCatsVisionView().getMultivisionPanel();

                    if ( multivisionPanel != null )
                    {
                        videoGridController.destroyVideoPanels();

                        multivisionPanel.getLogPanel().saveLogFile();

                        videoGridController.releaseAll();
                    }
                }
            } ).start();
        }
        catch ( Exception ex )
        {
            logger.error( "Caught nasty stopSettop exception on exit", ex );

        }
    }

    @Override
    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();
        if ( source instanceof JMenuItem )
        {
            JMenuItem menuItem = ( JMenuItem ) source;

            String menuItemText = menuItem.getText();

            if ( menuItemText.equals( PREFERENCES ) )
            {
                catsVisionView.jPreferncesMenuItemActionPerformed();
            }
            else if ( menuItemText.equals( KEYBOARD_SHORTCUTS ) )
            {
                catsVisionView.remoteShortcutMenuItemActionPerformed();
            }
            else if ( menuItemText.equals( ABOUT ) )
            {
                catsVisionView.aboutMenuItemActionPerformed();
            }
            else if ( menuItem.getText().contains( FPS ) )
            {
                frameRateMenuItemActionPerformed( event );
            }
        }
    }

    private void frameRateMenuItemActionPerformed( ActionEvent evt )
    {
        int frameRate = FrameRateType.getFrameRate( evt.getActionCommand() );
        videoGridController.setFrameRate( frameRate );
        catsVisionView.setFrame( frameRate );
    }

    @Override
    public void itemStateChanged( ItemEvent event )
    {
        Object source = event.getSource();

        if ( source == catsVisionView.getEnableKeyboardShortcutMenuItem() )
        {
            JCheckBoxMenuItem checkBoxMenuItem = ( JCheckBoxMenuItem ) source;
            boolean value = checkBoxMenuItem.getState();
            videoGridController.enableKeyboardShortcuts( value );
        }
    }

    /**
     * Method to fill the available and allocated settop info. This method will
     * be called during the start up of CATS Vision.
     */
    public void fillConfigDataTables()
    {
        configPanelController.fillConfigDataTables();
    }

    public void addCatsVisionViewListeners()
    {
        catsVisionView.addMouseListener( this );

        catsVisionView.addComponentListener( this );

        catsVisionView.addWindowListener( this );

        catsVisionView.addItemListener( this );

        catsVisionView.addActionListener( this );
    }

    public void removeCatsVisionViewListeners()
    {
        catsVisionView.removeMouseListener( this );

        catsVisionView.removeComponentListener( this );

        catsVisionView.removeWindowListener( this );

        catsVisionView.removeItemListener( this );

        catsVisionView.removeActionListener( this );
    }

    /**
     * Create MultivisionPanel
     * 
     * @return MultivisionPanel
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public MultivisionPanel createMultiVisionPanel() throws MalformedURLException, URISyntaxException
    {
        videoGridController.setCatsVisionView( catsVisionView );

        Set< Settop > settops = videoGridController.createSettops( macIdSet );

        MultivisionPanel multivisionPanel = videoGridController.createMultiVisionPanel( settops );

        return multivisionPanel;
    }

    /**
     * Add the following tabs to main view.<br>
     * 1) MultiVision tab 2) MySettops tab 3) Scripting tab
     */
    public void addTabs()
    {
        try
        {
            catsVisionView.addTab( createMultiVisionPanel(), CatsVisionConstants.APPLICATION_TITLE, "multiVisionPane" );
        }
        catch ( MalformedURLException e )
        {
            logger.error( "MalformedURLException : " + e );
        }
        catch ( URISyntaxException e )
        {
            logger.error( "URISyntaxException : " + e );
        }
        catsVisionView.addTab( configPanel, MY_SETTOPS, "mySettopsPane" );

        catsVisionView.addTab( scriptController.getScriptPanel(), SCRIPT_PANEL, "scriptPane" );
    }

    public String getAuthToken()
    {
        return authToken;
    }
}
