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
package com.comcast.cats.vision.panel.remote;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;

/**
 * | The view logic of the remote control in CATS Vision
 * 
 * @author bemman01c
 * 
 */
public class RemoteControlView extends JPanel
{
    private static final long      serialVersionUID       = -758016305574347046L;
    private static final Integer   REMOTE_PANEL_WIDTH     = 275;
    private static final Integer   REMOTE_PANEL_HEIGHT    = 400;
    public static final Font       DEFAULT_FONT           = new Font( "Serif", Font.ITALIC, 24 );
    private static final Dimension REMOTE_PANEL_DIMENSION = new Dimension( REMOTE_PANEL_WIDTH, REMOTE_PANEL_HEIGHT );
    private CounterPanel           pressAndHoldPanel;
    private static final String    ACTION_KEY             = "actioned";

    /**
     * Logger instance for RemotePanel.
     */
    private static final Logger    logger                 = Logger.getLogger( RemoteControlView.class );

    /**
     * The the handler to take care of key events.
     */
    private Object                 handler;
    /**
     * Name of this view panel
     */
    String                         name;
    /**
     * All the remote button grouping panels.
     */
    private List< ButtonPanel >    panels;

    /**
     * Direct Tune Panel
     */
    protected DirectTunePanel      directTunePanel;
    List< RemoteLayout >           keys                   = null;

    /** Creates a Remote panel with the help of a handler */
    public RemoteControlView( Object handler, String panelName, List< RemoteLayout > keys )
    {
        this.handler = handler;
        this.name = panelName;
        this.keys = keys;
        initComponents();
        clearPanels();
        this.setVisible( true );

    }

    /**
     * Place holder for chaning remote layout
     */
    public void setRemoteLayout( List< RemoteLayout > keys )
    {
        /**
         * TODO: In future this can be used to change the remote based on the
         * remote type.
         * 
         * We have to clear the current remote pannel, add the new buttons and
         * repaint()
         */
        this.keys = keys;
    }

    /**
     * initialize the view
     */
    private void initComponents()
    {
        setName( name );
        setRemoteControlLook();
        pressAndHoldPanel = new CounterPanel();
        add( pressAndHoldPanel );

    }

    /**
     * Method sets the size color and shape of the remote control
     */
    public void setRemoteControlLook()
    {
        setMinimumSize( REMOTE_PANEL_DIMENSION );
        setPreferredSize( REMOTE_PANEL_DIMENSION );
        setSize( REMOTE_PANEL_DIMENSION );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        setBorder( BorderFactory.createLineBorder( Color.BLACK, 1 ) );
        setBackground( Color.LIGHT_GRAY );
    }

    /**
     * Adds a Direct tune panel to this view
     * 
     * @param directTunePanel
     */
    public void addDirectTunePanel( DirectTunePanel directTunePanel )
    {
        this.directTunePanel = directTunePanel;

    }

    private void addDirectTunePanelToRemoteView()
    {
        this.add( directTunePanel );
    }

    /**
     * instatiates the button layout on the view with the key list and the
     * dierct tune panel
     */
    public void performRemoteButtonLayout()
    {

        Integer panelCount = getPanelCount( keys );

        /**
         * Set the array size to be the panel count.
         */
        panels = new ArrayList< ButtonPanel >( panelCount );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        // For Direct Tune
        addDirectTunePanelToRemoteView();
        /**
         * Create new JPanels to hold buttons and add all the panels to the main
         * panel.
         */
        for ( int p = 0; p < panelCount; p++ )
        {
            ButtonPanel panel = new ButtonPanel();
            panel.setVisible( true );
            panels.add( p, panel );
            // gbc.gridy = p;
            //
            // this.add(panel, margin);
            // this.add(panel, gbc);
            this.add( panel );
        }
        logger.debug( "panelCount = " + panelCount );
        logger.debug( "Panels = " + panels.size() );
        // Put this around row to give bottom padding.

        for ( RemoteLayout key : keys )
        {
            ButtonPanel p = panels.get( key.getPanel() );
            addRemoteButtonToPanel( p, key );
        }
    }

    private void clearPanels()
    {
        if ( null != panels )
        {
            this.panels.clear();
        }
    }

    private void addRemoteButtonToPanel( ButtonPanel panel, RemoteLayout key )
    {
        RemoteButton rb = new RemoteButton( key );
        // This panel should be the action listener for button presses.
        // rb.addActionListener(this);
        rb.addMouseListener( ( MouseListener ) handler );
        rb.setFocusable( false );
        setKeyBoardShortCut( rb );
        panel.addButtonToPanel( rb );
    }

    private void setKeyBoardShortCut( RemoteButton remoteButton )
    {
        String buttonText = remoteButton.getText();
        if ( ( buttonText == RemoteCommand.ZERO.toString() ) || ( buttonText == RemoteCommand.ONE.toString() )
                || ( buttonText == RemoteCommand.TWO.toString() ) || ( buttonText == RemoteCommand.THREE.toString() )
                || ( buttonText == RemoteCommand.FOUR.toString() ) || ( buttonText == RemoteCommand.FIVE.toString() )
                || ( buttonText == RemoteCommand.SIX.toString() ) || ( buttonText == RemoteCommand.SEVEN.toString() )
                || ( buttonText == RemoteCommand.EIGHT.toString() ) || ( buttonText == RemoteCommand.NINE.toString() ) )
        {
            setInputAndActionMap( remoteButton, buttonText );
        }
        if ( ( buttonText == RemoteCommand.VOLUP.toString() ) )
        {
            setInputAndActionMap( remoteButton, "PERIOD" ); // corresponds to >
            setInputAndActionMap( remoteButton, "GREATER" );
        }
        else if ( ( buttonText == RemoteCommand.VOLDN.toString() ) )
        {
            setInputAndActionMap( remoteButton, "COMMA" ); // corresponds to <
            setInputAndActionMap( remoteButton, "LESS" );
        }
        else if ( ( buttonText == RemoteCommand.CHUP.toString() ) )
        {
            setInputAndActionMap( remoteButton, "EQUALS" ); // corresponds to +
        }
        else if ( ( buttonText == RemoteCommand.CHDN.toString() ) )
        {
            setInputAndActionMap( remoteButton, "MINUS" );
        }
        else if ( ( buttonText == RemoteCommand.MENU.toString() ) )
        {
            setInputAndActionMap( remoteButton, "M" );
            setInputAndActionMap( remoteButton, "m" );
        }
        else if ( ( buttonText == RemoteCommand.INFO.toString() ) )
        {
            setInputAndActionMap( remoteButton, "I" );
            setInputAndActionMap( remoteButton, "i" );
        }
        else if ( ( buttonText == RemoteCommand.GUIDE.toString() ) )
        {
            setInputAndActionMap( remoteButton, "G" );
            setInputAndActionMap( remoteButton, "g" );
        }
        else if ( ( buttonText == RemoteCommand.SELECT.toString() ) )
        {
            setInputAndActionMap( remoteButton, "ENTER" );
        }
        else if ( ( buttonText == RemoteCommand.EXIT.toString() ) )
        {
            setInputAndActionMap( remoteButton, "ESCAPE" );
        }
        else if ( ( buttonText == RemoteCommand.LAST.toString() ) )
        {
            setInputAndActionMap( remoteButton, "BACK_SPACE" );
        }
        else if ( ( buttonText == RemoteCommand.UP.toString() ) )
        {
            setInputAndActionMap( remoteButton, "W" );
        }
        else if ( ( buttonText == RemoteCommand.DOWN.toString() ) )
        {
            setInputAndActionMap( remoteButton, "S" );
        }
        else if ( ( buttonText == RemoteCommand.LEFT.toString() ) )
        {
            setInputAndActionMap( remoteButton, "A" );
        }
        else if ( ( buttonText == RemoteCommand.RIGHT.toString() ) )
        {
            setInputAndActionMap( remoteButton, "D" );
        }
    }

    private void setInputAndActionMap( RemoteButton remoteButton, String keyStroke )
    {
        InputMap inputMap = remoteButton.getInputMap();
        inputMap = remoteButton.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        inputMap.put( KeyStroke.getKeyStroke( keyStroke ), ACTION_KEY );
        inputMap.put( KeyStroke.getKeyStroke( "shift " + keyStroke ), ACTION_KEY );
        ActionMap actionMap = remoteButton.getActionMap();
        actionMap.put( ACTION_KEY, ( Action ) handler );
    }

    private Integer getPanelCount( List< RemoteLayout > keys )
    {
        Integer panelCount = -1;
        for ( RemoteLayout key : keys )
        {
            if ( key.getPanel() > panelCount )
            {
                panelCount = key.getPanel();
            }
        }
        // Adjust the panel by one to account for the zero start.
        return panelCount + 1;
    }

    /**
     * Get the location to show the press and hold visualization. Adds any
     * offset, corrections to make sure that the panel is visible, provided it
     * has the space to be shown fully.
     * 
     * @param point
     *            - the point where mouse clicked.
     * @param xOffset
     *            - any x offset
     * @param yOffset
     *            - any y offset
     * @return - the point where to position the press and hold panel.
     */
    protected Point getPressAndHoldPanelLocation( Point point, int xOffset, int yOffset )
    {
        int x = point.x;
        int y = point.y;
        // if the panel width will fall out of bounds of the remote panel,
        // negate the width to make the panel fall within bounds.
        if ( ( x + pressAndHoldPanel.getPreferredSize().width ) > getWidth() )
        {
            x = x - pressAndHoldPanel.getPreferredSize().width - xOffset;
        }
        else
        {
            x += xOffset;
        }

        if ( ( y + pressAndHoldPanel.getHeight() ) > getHeight() )
        {
            y = y - pressAndHoldPanel.getHeight() - yOffset;
        }
        else
        {
            y += yOffset;
        }

        // finally adjust x and y to zero if they are negative and hence out of
        // bounds.
        x = ( x < 0 ) ? 0 : x;
        y = ( x < 0 ) ? 0 : y;
        return new Point( x, y );
    }

    /**
     * notify the view to show the pressAndHoldPanel
     */
    public void showPressAndHoldPanel( Point mousePosition, JButton button )
    {
        Point panelPosition = getPressAndHoldPanelLocation( mousePosition, 0, button.getHeight() ); // provide
        pressAndHoldPanel.setLocation( panelPosition.x, panelPosition.y );
        pressAndHoldPanel.showCounterPanel( CounterPanel.COUNT_MODE );
    }

    /**
     * Hide the press and Hold panel.
     */
    protected void hidePressAndHoldPanel()
    {
        pressAndHoldPanel.hideCounterPanel();
        this.repaint();
    }

}
