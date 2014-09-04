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

import static com.comcast.cats.RemoteCommand.A;
import static com.comcast.cats.RemoteCommand.B;
import static com.comcast.cats.RemoteCommand.C;
import static com.comcast.cats.RemoteCommand.CHDN;
import static com.comcast.cats.RemoteCommand.CHUP;
import static com.comcast.cats.RemoteCommand.DELETE;
import static com.comcast.cats.RemoteCommand.DOWN;
import static com.comcast.cats.RemoteCommand.EIGHT;
import static com.comcast.cats.RemoteCommand.EXIT;
import static com.comcast.cats.RemoteCommand.FAV;
import static com.comcast.cats.RemoteCommand.FF;
import static com.comcast.cats.RemoteCommand.FIVE;
import static com.comcast.cats.RemoteCommand.FOUR;
import static com.comcast.cats.RemoteCommand.GUIDE;
import static com.comcast.cats.RemoteCommand.HDZOOM;
import static com.comcast.cats.RemoteCommand.HELP;
import static com.comcast.cats.RemoteCommand.INFO;
import static com.comcast.cats.RemoteCommand.LANGUAGE;
import static com.comcast.cats.RemoteCommand.LAST;
import static com.comcast.cats.RemoteCommand.LEFT;
import static com.comcast.cats.RemoteCommand.LIVE;
import static com.comcast.cats.RemoteCommand.MENU;
import static com.comcast.cats.RemoteCommand.MUTE;
import static com.comcast.cats.RemoteCommand.MYDVR;
import static com.comcast.cats.RemoteCommand.NINE;
import static com.comcast.cats.RemoteCommand.ONDEMAND;
import static com.comcast.cats.RemoteCommand.ONE;
import static com.comcast.cats.RemoteCommand.PAUSE;
import static com.comcast.cats.RemoteCommand.PGDN;
import static com.comcast.cats.RemoteCommand.PGUP;
import static com.comcast.cats.RemoteCommand.PIPCHDN;
import static com.comcast.cats.RemoteCommand.PIPCHUP;
import static com.comcast.cats.RemoteCommand.PIPMOVE;
import static com.comcast.cats.RemoteCommand.PIPONOFF;
import static com.comcast.cats.RemoteCommand.PIPSWAP;
import static com.comcast.cats.RemoteCommand.PLAY;
import static com.comcast.cats.RemoteCommand.POUND;
import static com.comcast.cats.RemoteCommand.POWER;
import static com.comcast.cats.RemoteCommand.REC;
import static com.comcast.cats.RemoteCommand.REPLAY;
import static com.comcast.cats.RemoteCommand.REW;
import static com.comcast.cats.RemoteCommand.RIGHT;
import static com.comcast.cats.RemoteCommand.SELECT;
import static com.comcast.cats.RemoteCommand.SEVEN;
import static com.comcast.cats.RemoteCommand.SIX;
import static com.comcast.cats.RemoteCommand.SKIPFWD;
import static com.comcast.cats.RemoteCommand.STAR;
import static com.comcast.cats.RemoteCommand.STOP;
import static com.comcast.cats.RemoteCommand.THREE;
import static com.comcast.cats.RemoteCommand.TV_VCR;
import static com.comcast.cats.RemoteCommand.TWO;
import static com.comcast.cats.RemoteCommand.UP;
import static com.comcast.cats.RemoteCommand.VOLDN;
import static com.comcast.cats.RemoteCommand.VOLUP;
import static com.comcast.cats.RemoteCommand.ZERO;
import static com.comcast.cats.vision.util.CatsVisionConstants.PLEASE_ALLOCATE_MSG;
import static com.comcast.cats.vision.util.CatsVisionConstants.DIRECT_TUNE_TEXT_FIELD_NAME;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.vision.event.ActionType;
import com.comcast.cats.vision.event.RemoteEvent;
import com.comcast.cats.vision.panel.videogrid.model.GridDataModel;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * This class is the controller for the remote panel in cats vision.
 * 
 * @author bemman01c
 * 
 */
@Named
public class RemoteController extends AbstractAction implements MouseListener, FocusListener
{
    /**
     * 
     */
    private static final long   serialVersionUID         = -6999100610171185232L;

    private static final Logger logger                   = Logger.getLogger( RemoteController.class );

    public static long          DELAY                    = 500;
    public static long          DIVISION_FACTOR          = 100;
    Settop                      settop;
    /**
     * Place holder for future development This variable will not be use. We
     * will only be using the CatsEventDispatcher.
     */
    private RemoteProvider      remoteProvider;

    /**
     * Model in MVC for multi grid view.
     */
    private GridDataModel       gridDataModel;
    /**
     * The reference to the CATSEvent dispatcher. This is responsible for
     * dispatching an event to all the boxes in a grid.
     */
    private CatsEventDispatcher catsEventDispatcher;
    /**
     * Direct tune controller handles the direct tune actions
     */
    DirectTuneController        directTuneController;
    /**
     * Reference to the View
     */
    RemoteControlView           remoteControlView;
    private boolean             keyboardShortcutsEnabled = true;
    private long                mousePressed             = 0;
    private long                mouseReleased            = 0;

    /**
     * Construct a Remote taking the CatsEventDispatcher as its parameter
     * 
     * @param catsEventDispatcher
     * @param model
     */
    @Inject
    public RemoteController( CatsEventDispatcher catsEventDispatcher, GridDataModel model )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Creating RemoteController with catsEventDispatcher and gridDataModel" );
        }
        this.catsEventDispatcher = catsEventDispatcher;
        this.remoteProvider = null;
        this.gridDataModel = model;
        List< RemoteLayout > keys = getKeysLayout();
        remoteControlView = new RemoteControlView( this, "RemoteControlPanel", keys );
        initDirectTunePanel();
        remoteControlView.performRemoteButtonLayout();
    }

    private void initDirectTunePanel()
    {
        directTuneController = new DirectTuneController( catsEventDispatcher, ( FocusListener ) this, gridDataModel);
        remoteControlView.addDirectTunePanel( directTuneController.getDirectTunePanel() );

    }

    public RemoteControlView getRemote()
    {

        return remoteControlView;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if ( keyboardShortcutsEnabled )
        {
            Object obj = e.getSource();
            if ( obj instanceof RemoteButton )
            {
                keyPress( ( RemoteButton ) obj );
            }
        }

    }

    protected void keyPress( RemoteButton remoteButton )
    {
        logger.debug( "Clicked GridRemotePanel Remote button - " + remoteButton.getRemoteCommand().toString() );

        Set< Settop > allocatedAndSelectedSettops = gridDataModel.getAllocatedAndSelectedSettops();

        if ( ( allocatedAndSelectedSettops != null ) && !( allocatedAndSelectedSettops.isEmpty() ) )
        {
            long mouseDuration = mouseReleased - mousePressed;
            int count = 0;

            if ( mouseDuration > DELAY )
            {
                /*
                 * There are about 10 pressKeys per second and 1000 ms/second,
                 * so divide by 100 for a good pressKey number.
                 */
                count = new Long( mouseDuration / DIVISION_FACTOR ).intValue();
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Calling pressKeyAndHold. Count for pressAndHold = " + count );
                }

                catsEventDispatcher.sendCatsEvent( new RemoteEvent( ActionType.PRESS_AND_HOLD, RemoteCommand
                        .parse( remoteButton.getText() ), "RemoteController", this, count ) );
            }
            else
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Calling pressKey" );
                }

                catsEventDispatcher.sendCatsEvent( new RemoteEvent( ActionType.PRESS, RemoteCommand.parse( remoteButton
                        .getText() ), "RemoteController", this, count ) );
            }
            mousePressed = 0;
            mouseReleased = 0;
        }
        else
        {
            CatsVisionUtils.showWarning( "Unable to perform the operation", PLEASE_ALLOCATE_MSG + " Remote" );
        }
    }

    @Override
    public void mouseClicked( MouseEvent mouseEvent )
    {
        Object obj = mouseEvent.getSource();
        if ( obj instanceof RemoteButton )
        {
            keyPress( ( RemoteButton ) obj );
        }

    }

    @Override
    public void mousePressed( MouseEvent e )
    {
        mousePressed = System.currentTimeMillis();
        Point mousePosition = getMouseClickedLocation( e );
        JButton button = ( JButton ) e.getSource();
        remoteControlView.showPressAndHoldPanel( mousePosition, button );

    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
        mouseReleased = System.currentTimeMillis();
        remoteControlView.hidePressAndHoldPanel();

    }

    @Override
    public void mouseEntered( MouseEvent e )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited( MouseEvent e )
    {
        // TODO Auto-generated method stub

    }

    private List< RemoteLayout > getKeysLayout()
    {
        List< RemoteLayout > keys = null;
        try
        {
            keys = remoteProvider.getValidKeys();
        }
        catch ( Exception e )
        {
            logger.info( "getValidKeys did not return a value, using defaults" + e );
        }
        if ( keys == null || keys.size() == 0 )
        {
            logger.debug( "Using default key layout." );
            keys = getDefaultKeyLayout();
        }
        return keys;
    }

    /**
     * Provide a default key layout if one is unable to be retrieved from the
     * server.
     */
    private List< RemoteLayout > getDefaultKeyLayout()
    {
        List< RemoteLayout > keys = new ArrayList< RemoteLayout >();

        Integer panel = 0;
        Integer row = 0;
        keys.add( createRemoteLayout( row, 1, panel, ONDEMAND ) );
        keys.add( createRemoteLayout( row, 2, panel, POWER ) );

        panel++;
        row = 0;
        keys.add( createRemoteLayout( row, 0, panel, REW ) );
        keys.add( createRemoteLayout( row, 1, panel, PLAY ) );
        keys.add( createRemoteLayout( row, 2, panel, FF ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, STOP ) );
        keys.add( createRemoteLayout( row, 1, panel, PAUSE ) );
        keys.add( createRemoteLayout( row, 2, panel, REC ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, LAST ) );
        keys.add( createRemoteLayout( row, 1, panel, MYDVR ) );
        keys.add( createRemoteLayout( row, 2, panel, LIVE ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, SKIPFWD ) );
        keys.add( createRemoteLayout( row, 1, panel, REPLAY ) );

        row = 0;
        panel++;
        keys.add( createRemoteLayout( row, 0, panel, A ) );
        keys.add( createRemoteLayout( row, 1, panel, B ) );
        keys.add( createRemoteLayout( row, 2, panel, C ) );

        row = 0;
        panel++;
        keys.add( createRemoteLayout( row, 0, panel, PGUP ) );
        keys.add( createRemoteLayout( row, 1, panel, UP ) );
        keys.add( createRemoteLayout( row, 2, panel, PGDN ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, LEFT ) );
        keys.add( createRemoteLayout( row, 1, panel, SELECT ) );
        keys.add( createRemoteLayout( row, 2, panel, RIGHT ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, LANGUAGE ) );
        keys.add( createRemoteLayout( row, 1, panel, DOWN ) );

        row = 0;
        panel++;
        keys.add( createRemoteLayout( row, 0, panel, GUIDE ) );
        keys.add( createRemoteLayout( row, 1, panel, INFO ) );
        keys.add( createRemoteLayout( row, 2, panel, MENU ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, EXIT ) );
        keys.add( createRemoteLayout( row, 1, panel, HELP ) );
        keys.add( createRemoteLayout( row, 2, panel, LAST ) );

        row = 0;
        panel++;
        keys.add( createRemoteLayout( row, 0, panel, VOLUP ) );
        keys.add( createRemoteLayout( row, 1, panel, MUTE ) );
        keys.add( createRemoteLayout( row, 2, panel, CHUP ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, VOLDN ) );
        keys.add( createRemoteLayout( row, 1, panel, FAV ) );
        keys.add( createRemoteLayout( row, 2, panel, CHDN ) );

        row = 0;
        panel++;
        keys.add( createRemoteLayout( row, 0, panel, ONE ) );
        keys.add( createRemoteLayout( row, 1, panel, TWO ) );
        keys.add( createRemoteLayout( row, 2, panel, THREE ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, FOUR ) );
        keys.add( createRemoteLayout( row, 1, panel, FIVE ) );
        keys.add( createRemoteLayout( row, 2, panel, SIX ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, SEVEN ) );
        keys.add( createRemoteLayout( row, 1, panel, EIGHT ) );
        keys.add( createRemoteLayout( row, 2, panel, NINE ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, STAR ) );
        keys.add( createRemoteLayout( row, 1, panel, ZERO ) );
        keys.add( createRemoteLayout( row, 2, panel, POUND ) );

        row = 0;
        panel++;
        keys.add( createRemoteLayout( row, 0, panel, TV_VCR ) );
        keys.add( createRemoteLayout( row, 1, panel, DELETE ) );
        keys.add( createRemoteLayout( row, 2, panel, HDZOOM ) );
        row++;
        keys.add( createRemoteLayout( row, 0, panel, PIPONOFF ) );
        keys.add( createRemoteLayout( row, 1, panel, PIPSWAP ) );
        keys.add( createRemoteLayout( row, 2, panel, PIPMOVE ) );
        row++;
        keys.add( createRemoteLayout( row, 1, panel, PIPCHUP ) );
        keys.add( createRemoteLayout( row, 2, panel, PIPCHDN ) );

        return keys;
    }

    private RemoteLayout createRemoteLayout( Integer row, Integer col, Integer panel, RemoteCommand c )
    {
        RemoteLayout layout = new RemoteLayout( row, col, panel, c );
        return layout;
    }

    /**
     * To enable and disable keyboard shortcuts
     * 
     * @param value
     */
    public void enableKeyboardShortcuts( boolean value )
    {
        keyboardShortcutsEnabled = value;

    }

    public boolean isKeyboardShortcutsEnabled()
    {
        return keyboardShortcutsEnabled;
    }

    /**
     * Obtain the pixel point where the mouse clicked occurred w.r.t RemotePanel
     * 
     * @param e
     *            - the Mouse Event.
     * @return - the mouse clicked point
     */
    protected Point getMouseClickedLocation( MouseEvent e )
    {
        // this for remote panel button presses only.
        JButton button = ( JButton ) e.getSource();
        int x = e.getX();
        int y = e.getY();
        Component parent = button;
        // add the x, y of all the parents till RemotePanel to get pixel
        // position
        // w.r.t to RemotePanel
        while ( !( parent instanceof RemoteControlView ) )
        {
            x += parent.getX();
            y += parent.getY();
            parent = parent.getParent();
        }
        return new Point( x, y );
    }

    /**
     * Updates the remote layout based on the remote provider
     * 
     * @param remoteProvider
     */
    public void updateRemoteLayout( RemoteProvider remoteProvider )
    {
        this.remoteProvider = remoteProvider;
        List< RemoteLayout > keys = getKeysLayout();
        remoteControlView = new RemoteControlView( this, "RemoteControlPanel", keys );
        remoteControlView.setRemoteLayout( keys );
        remoteControlView.performRemoteButtonLayout();
    }

    @Override
    public void focusGained( FocusEvent fevent )
    {
        validateFocusState( fevent, true );

    }

    @Override
    public void focusLost( FocusEvent fevent )
    {
        validateFocusState( fevent, false );
    }

    /**
     * Listens to focus change events on the text area inside the Direct tune
     * panel and then toggles the keyboard short cut state.If focus state is
     * true disable the key board shortcut and vice versa
     * 
     * @param fevent
     * @param focusState
     */
    private void validateFocusState( FocusEvent fevent, boolean focusState )
    {
        Object source = fevent.getSource();
        if ( !( source instanceof JTextField ) )
        {
            return;
        }
        JTextField textField = ( JTextField ) source;
        if ( textField.getName().equals( DIRECT_TUNE_TEXT_FIELD_NAME ) )
        {
            enableKeyboardShortcuts( !focusState );
        }
    }
}
