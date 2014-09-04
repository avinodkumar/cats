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
import static com.comcast.cats.vision.util.CatsVisionConstants.KEYBOARD_SHORTCUTS;
import static com.comcast.cats.vision.util.CatsVisionConstants.PREFERENCES;
import static com.comcast.cats.vision.util.CatsVisionConstants.SNAP_IMAGE;
import static com.comcast.cats.vision.util.CatsVisionConstants.TRACE;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.layout.GroupLayout;

import com.comcast.cats.vision.panel.preferencepanes.PreferenceDialog;
import com.comcast.cats.vision.panel.video.FrameRateType;
import com.comcast.cats.vision.panel.videogrid.MultivisionPanel;
import com.comcast.cats.vision.util.CatsVisionConstants;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * The application's main frame.
 */
public class CATSVisionView extends FrameView
{
    static Logger                                      logger             = Logger.getLogger( CATSVisionView.class );
    private MultivisionPanel                           multivisionPanel;
    // private JMenu featuresMenu;
    private JMenu                                      traceMenu;

    private String                                     authToken;
    private JMenuItem                                  jOpenScript;

    private JCheckBoxMenuItem                          jEnableKeyboardShortcutMenuItem;
    private JMenu                                      jSnapImageMenu;
    private JMenuItem                                  jPreferencesMenuItem;
    private JMenuItem                                  remoteShortcutMenuItem;
    private JMenuItem                                  aboutMenuItem;
    private JMenu                                      frameRateMenu;
    private JPanel                                     mainPanel;
    private JMenuBar                                   menuBar;
    private JMenu                                      optionMenu;

    private JTabbedPane                                visionTabs;

    private JDialog                                    aboutBox;

    private Map< FrameRateType, JRadioButtonMenuItem > frameRateMenuItems = new HashMap< FrameRateType, JRadioButtonMenuItem >();

    private static final Rectangle                     FRAME_BOUNDS       = new Rectangle( 50, 100, 1136, 800 );
    private static int                                 frameRate          = FrameRateType
                                                                                  .getFrameRate( FrameRateType.DEFAULT_FPS );

    /**
     * Main CATS Vision Constructor.
     * 
     */
    public CATSVisionView( SingleFrameApplication app )
    {
        super( app );
        logger.info( "CATSVisionView is starting..." );

        initComponents();
        visionTabs.setActionMap( null );
        getFrame().setTitle( CatsVisionConstants.APPLICATION_TITLE );
        getFrame().setIconImage( CatsVisionUtils.getApplicationIcon() );
    }

    public void addTab( JPanel panel, String title, String name )
    {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        scrollPane.getViewport().add( panel, null );
        scrollPane.setName( name );
        visionTabs.add( title, scrollPane );
    }

    public void updateTab( JPanel panel, String scrollPaneName )
    {
        for ( int i = 0; i < visionTabs.getComponentCount(); i++ )
        {
            Component component = visionTabs.getComponent( i );

            if ( ( component instanceof JScrollPane ) && ( component.getName().equals( scrollPaneName ) ) )
            {
                JScrollPane scrollPane = ( JScrollPane ) component;
                scrollPane.getViewport().add( panel, null );
                break;
            }
        }
    }

    public JScrollPane getScrollPane( String name )
    {
        JScrollPane scrollPane = new JScrollPane();
        for ( int i = 0; i < visionTabs.getComponentCount(); i++ )
        {
            Component component = visionTabs.getTabComponentAt( i );

            if ( ( component instanceof JScrollPane ) && ( component.getName().equals( name ) ) )
            {
                scrollPane = ( JScrollPane ) component;
            }
        }
        return scrollPane;
    }

    public MultivisionPanel getMultivisionPanel()
    {
        return multivisionPanel;
    }

    public void setMultivisionPanel( MultivisionPanel multivisionPanel )
    {
        this.multivisionPanel = multivisionPanel;
    }

    /**
     * About CATS Vision dialog.
     */
    @Action
    private void showAboutBox()
    {
        if ( aboutBox == null )
        {
            JFrame mainFrame = CATSVisionApplication.getApplication().getMainFrame();
            aboutBox = new CATSVisionAboutBox( mainFrame );
            aboutBox.setLocationRelativeTo( mainFrame );
        }
        CATSVisionApplication.getApplication().show( aboutBox );
    }

    private void initComponents()
    {
        mainPanel = new JPanel();
        visionTabs = new JTabbedPane();
        menuBar = new JMenuBar();
        jOpenScript = new JMenuItem();
        optionMenu = new JMenu();
        // featuresMenu = new JMenu();

        traceMenu = new JMenu();

        jEnableKeyboardShortcutMenuItem = new JCheckBoxMenuItem( "Enable Keyboard shortcut", true );
        jSnapImageMenu = new JMenu();
        jPreferencesMenuItem = new JMenuItem();

        JMenu helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        remoteShortcutMenuItem = new JMenuItem( KEYBOARD_SHORTCUTS );

        mainPanel.setName( "mainPanel" ); // NOI18N
        mainPanel.setRequestFocusEnabled( false );

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap( CATSVisionView.class );
        visionTabs.setBackground( resourceMap.getColor( "visionTabs.background" ) ); // NOI18N
        visionTabs.setDoubleBuffered( true );
        visionTabs.setName( "visionTabs" ); // NOI18N
        visionTabs.setPreferredSize( new java.awt.Dimension( 1024, 700 ) );

        GroupLayout mainPanelLayout = new GroupLayout( mainPanel );
        mainPanel.setLayout( mainPanelLayout );
        mainPanelLayout
                .setHorizontalGroup( mainPanelLayout.createParallelGroup( GroupLayout.LEADING ).add( visionTabs ) );
        mainPanelLayout.setVerticalGroup( mainPanelLayout.createParallelGroup( GroupLayout.LEADING ).add( visionTabs ) );

        visionTabs.getAccessibleContext().setAccessibleName(
                resourceMap.getString( "jTabbedPane1.AccessibleContext.accessibleName" ) ); // NOI18N

        menuBar.setName( "menuBar" ); // NOI18N

        jOpenScript.setAccelerator( KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_O,
                java.awt.event.InputEvent.CTRL_MASK ) );
        jOpenScript.setText( resourceMap.getString( "jOpenScript.text" ) ); // NOI18N
        jOpenScript.setName( "jOpenScript" ); // NOI18N

        optionMenu.setText( resourceMap.getString( "optionMenu.text" ) ); // NOI18N
        optionMenu.setName( "optionMenu" ); // NOI18N

        // featuresMenu.setText( resourceMap.getString( "featuresMenu.text" ) );
        // featuresMenu.setName( "featuresMenu" ); // NOI18N

        traceMenu.setText( TRACE ); // NOI18N
        traceMenu.setName( "traceMenu" ); // NOI18N
        // featuresMenu.add( traceMenu );

        jSnapImageMenu.setText( SNAP_IMAGE ); // NOI18N
        jSnapImageMenu.setName( "jSnapImageCheckBoxMenuItem" ); // NOI18N
        optionMenu.add( jSnapImageMenu );

        jEnableKeyboardShortcutMenuItem.setName( "jEnableKeyboardShortcutMenuItem" );

        optionMenu.add( jEnableKeyboardShortcutMenuItem );

        jPreferencesMenuItem.setText( PREFERENCES ); // NOI18N
        jPreferencesMenuItem.setName( "jPreferencesMenuItem" ); // NOI18N

        optionMenu.add( jPreferencesMenuItem );

        frameRateMenu = new JMenu();
        frameRateMenu.setText( resourceMap.getString( "frameRateMenu.text" ) );

        /*
         * Creating frame rate menu items
         */
        FrameRateType[] frameRateTypes = FrameRateType.values();
        for ( FrameRateType frameRateType : frameRateTypes )
        {
            frameRateMenuItems.put( frameRateType, createFrameRateMenuItem( frameRateType ) );
        }

        /*
         * Adding framerate menu items to button group
         */
        ButtonGroup frameRateGroup = new ButtonGroup();

        for ( FrameRateType frameRateType : frameRateTypes )
        {
            frameRateGroup.add( frameRateMenuItems.get( frameRateType ) );
        }

        for ( FrameRateType frameRateType : frameRateTypes )
        {
            frameRateMenu.add( frameRateMenuItems.get( frameRateType ) );
        }

        /*
         * Adding frameRate menu to 'Options' menu
         */
        optionMenu.add( frameRateMenu );
        menuBar.add( optionMenu );
        // menuBar.add( featuresMenu );

        helpMenu.setText( resourceMap.getString( "helpMenu.text" ) ); // NOI18N
        helpMenu.setName( "helpMenu" ); // NOI18N

        aboutMenuItem.setText( ABOUT ); // NOI18N
        aboutMenuItem.setName( "aboutMenuItem" ); // NOI18N
        helpMenu.add( aboutMenuItem );
        helpMenu.add( remoteShortcutMenuItem );
        menuBar.add( helpMenu );

        setComponent( mainPanel );
        setMenuBar( menuBar );
        getFrame().setBounds( FRAME_BOUNDS );
    }

    protected void remoteShortcutMenuItemActionPerformed()
    {
        JDialog dialog = new JDialog();

        dialog.setTitle( "Keyboard Shortcuts" );
        dialog.setIconImage( CatsVisionUtils.getApplicationIcon() );
        dialog.setLocationRelativeTo( null );

        URL url = this.getClass().getResource( "/images/keyboard_shortcuts.png" );
        ImageIcon imageIcon = new ImageIcon( url, "Info" );

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon( imageIcon );

        JPanel infoPanel = new JPanel();
        infoPanel.add( iconLabel );
        infoPanel.setVisible( true );

        dialog.getContentPane().add( infoPanel );
        dialog.setBounds( 100, 100, imageIcon.getIconWidth() + 50, imageIcon.getIconHeight() + 70 );
        dialog.setResizable( false );
        dialog.setVisible( true );
    }

    /*
     * Create MenuItem for FrameRate
     */
    private JRadioButtonMenuItem createFrameRateMenuItem( FrameRateType frameRateType )
    {

        JRadioButtonMenuItem frameRateMenuItem;
        String name = FrameRateType.getName( frameRateType );
        if ( frameRateType == FrameRateType.DEFAULT_FPS )
        {
            frameRateMenuItem = new JRadioButtonMenuItem( name, true );
        }
        else
        {
            frameRateMenuItem = new JRadioButtonMenuItem( name, false );
        }
        return frameRateMenuItem;
    }

    /**
     * Display about dialog.
     * 
     * @param evt
     */
    protected void aboutMenuItemActionPerformed()
    {
        showAboutBox();
    }

    /**
     * Preferences Menu-bar
     * 
     * @param evt
     */
    protected void jPreferncesMenuItemActionPerformed()
    {
        try
        {
            PreferenceDialog prefDialog = new PreferenceDialog();

            Rectangle rect = getFrame().getBounds();
            int x = rect.x + ( rect.width - prefDialog.getSize().width ) / 2;
            int y = rect.y + ( rect.height - prefDialog.getSize().height ) / 2;
            prefDialog.setLocation( x, y );
            prefDialog.setVisible( true );
        }
        catch ( Exception e )
        {
            logger.error( e.getMessage() );
        }
    }

    public JTabbedPane getVisionTabs()
    {
        return visionTabs;
    }

    /*
     * public JMenu getFeaturesMenu() { return featuresMenu; }
     */

    /**
     * Update the mainScrollPane in CatsVisionView with multivisionPanel
     */
    public void updateMainScrollPane( MultivisionPanel multivisionPanel ) throws MalformedURLException,
            URISyntaxException
    {
        updateTab( multivisionPanel, "multiVisionPane" );

        logger.info( "CATS Vision launched. " );

        selectCatsVisionTab();
    }

    public void selectCatsVisionTab()
    {

        getFrame().setTitle( CatsVisionConstants.APPLICATION_TITLE );
        /*
         * To bring the focus to 'CATS Vision' tab
         */
        getVisionTabs().setSelectedIndex( 0 );

    }

    public int getFrameRate()
    {
        return frameRate;
    }

    public JMenuItem getPreferencesMenuItem()
    {
        return jPreferencesMenuItem;
    }

    public JMenuItem getRemoteShortcutMenuItem()
    {
        return remoteShortcutMenuItem;
    }

    public JMenu getTraceMenu()
    {
        return traceMenu;
    }

    public JMenuItem getAboutMenuItem()
    {
        return aboutMenuItem;
    }

    public JMenu getSnapImageMenu()
    {
        return jSnapImageMenu;
    }

    public JCheckBoxMenuItem getEnableKeyboardShortcutMenuItem()
    {
        return jEnableKeyboardShortcutMenuItem;
    }

    public Map< FrameRateType, JRadioButtonMenuItem > getFrameRateMenuItems()
    {
        return frameRateMenuItems;
    }

    public void setFrameRateMenuItems( Map< FrameRateType, JRadioButtonMenuItem > frameRateMenuItems )
    {
        this.frameRateMenuItems = frameRateMenuItems;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken( String authToken )
    {
        this.authToken = authToken;
    }

    public void setFrame( int frameRate )
    {
        CATSVisionView.frameRate = frameRate;
    }

    public void addMouseListener( MouseListener listener )
    {
        /*
         * Registering listener to 'Snap Image'
         */
        jSnapImageMenu.addMouseListener( listener );

        /*
         * Adding listener for Trace Menu
         */
        traceMenu.addMouseListener( listener );
    }

    public void addComponentListener( ComponentListener listener )
    {
        /*
         * Registering listener for frame resizing events
         */
        this.getFrame().addComponentListener( listener );
    }

    public void addWindowListener( WindowListener listener )
    {
        /*
         * Registering listener for frame resizing events
         */
        this.getFrame().addWindowListener( listener );
    }

    public void addItemListener( ItemListener listener )
    {
        /*
         * Adding listener for EnableKeyboardShortcut MenuItem
         */
        jEnableKeyboardShortcutMenuItem.addItemListener( listener );
    }

    public void addActionListener( ActionListener listener )
    {
        /*
         * Adding listener for Preferences MenuItem
         */
        jPreferencesMenuItem.addActionListener( listener );
        /*
         * Adding listener for Frame Rate MenuItems
         */
        for ( FrameRateType frameRateType : FrameRateType.values() )
        {
            frameRateMenuItems.get( frameRateType ).addActionListener( listener );
        }
        /*
         * Adding listener for About MenuItem
         */
        aboutMenuItem.addActionListener( listener );
        /*
         * Adding listener for Keyboard shortcuts MenuItem
         */
        remoteShortcutMenuItem.addActionListener( listener );
    }

    public void removeMouseListener( MouseListener listener )
    {
        /*
         * Removing listener to 'Snap Image'
         */
        jSnapImageMenu.removeMouseListener( listener );

        /*
         * Removing listener for Trace Menu
         */
        traceMenu.removeMouseListener( listener );
    }

    public void removeComponentListener( ComponentListener listener )
    {
        /*
         * Removing listener for frame resizing events
         */
        this.getFrame().removeComponentListener( listener );
    }

    public void removeWindowListener( WindowListener listener )
    {
        /*
         * Removing listener for window events
         */
        this.getFrame().removeWindowListener( listener );
    }

    public void removeItemListener( ItemListener listener )
    {
        /*
         * Removing listener for EnableKeyboardShortcut MenuItem
         */
        jEnableKeyboardShortcutMenuItem.removeItemListener( listener );
    }

    public void removeActionListener( ActionListener listener )
    {
        /*
         * Removing listener for Preferences MenuItem
         */
        jPreferencesMenuItem.removeActionListener( listener );
        /*
         * Removing listener for Frame Rate MenuItems
         */
        for ( FrameRateType frameRateType : FrameRateType.values() )
        {
            frameRateMenuItems.get( frameRateType ).removeActionListener( listener );
        }
        /*
         * Removing listener for About MenuItem
         */
        aboutMenuItem.removeActionListener( listener );
        /*
         * Removing listener for Keyboard shortcuts MenuItem
         */
        remoteShortcutMenuItem.removeActionListener( listener );
    }
}
