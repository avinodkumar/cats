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

import static com.comcast.cats.vision.util.CatsVisionConstants.SETTOP_INFO;
import static com.comcast.cats.vision.util.CatsVisionConstants.SNAP_IMAGE;
import static com.comcast.cats.vision.util.CatsVisionConstants.TRACE;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * A JPopupMenu which contains the following menu items 1) Audio 2) Digital
 * Controller 3)Trace 4) Snap Image 5) Settop Info
 * 
 * @author aswathyann
 * 
 */
public class RightClickMenu extends JPopupMenu
{
    private static final long serialVersionUID = 3631988109167602250L;

    private JMenuItem         snapImageMenuItem;

    private JMenuItem         settopInfoMenuItem;

    private JMenuItem         traceMenuItem;

    public static void main( String[] args )
    {
        final RightClickMenu rightClickMenu = new RightClickMenu();

        JFrame frame = new JFrame( "Creating a Popup Menu" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.addMouseListener( new MouseAdapter()
        {
            public void mouseReleased( MouseEvent event )
            {
                if ( event.isPopupTrigger() )
                {
                    rightClickMenu.show( event.getComponent(), event.getX(), event.getY() );
                }
            }
        } );
        frame.setSize( 400, 400 );
        frame.setVisible( true );

    }

    public RightClickMenu()
    {
        traceMenuItem = new JMenuItem( TRACE );
        traceMenuItem.setEnabled( false );
        add( traceMenuItem );

        snapImageMenuItem = new JMenuItem( SNAP_IMAGE );
        snapImageMenuItem.setEnabled( true );
        add( snapImageMenuItem );

        settopInfoMenuItem = new JMenuItem( SETTOP_INFO );
        settopInfoMenuItem.setEnabled( true );
        add( settopInfoMenuItem );

    }

    public void refreshMenu()
    {
        traceMenuItem.setEnabled( false );
    }

    public JMenuItem getSnapImageMenuItem()
    {
        return snapImageMenuItem;
    }

    public void setSnapImageMenuItem( JMenuItem snapImageMenuItem )
    {
        this.snapImageMenuItem = snapImageMenuItem;
    }

    public JMenuItem getSettopInfoMenuItem()
    {
        return settopInfoMenuItem;
    }

    public void setSettopInfoMenuItem( JMenuItem settopInfoMenuItem )
    {
        this.settopInfoMenuItem = settopInfoMenuItem;
    }

    public JMenuItem getTraceMenuItem()
    {
        return traceMenuItem;
    }

    public void setTraceMenuItem( JMenuItem traceMenuItem )
    {
        this.traceMenuItem = traceMenuItem;
    }

    public void addActionListener( ActionListener listener )
    {
        traceMenuItem.addActionListener( listener );
        snapImageMenuItem.addActionListener( listener );
        settopInfoMenuItem.addActionListener( listener );
    }

    public void removeActionListener( ActionListener listener )
    {
        traceMenuItem.removeActionListener( listener );
        snapImageMenuItem.removeActionListener( listener );
        settopInfoMenuItem.removeActionListener( listener );
    }
}