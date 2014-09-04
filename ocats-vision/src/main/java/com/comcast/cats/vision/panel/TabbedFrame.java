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
package com.comcast.cats.vision.panel;

import static com.comcast.cats.vision.util.CatsVisionConstants.SETTOP;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import com.comcast.cats.vision.util.CatsVisionUtils;

public class TabbedFrame extends JFrame
{
    private static final long   serialVersionUID = 3842229819480379333L;

    private JTabbedPane         tabbedPane       = new JTabbedPane();

    private static final Logger logger           = Logger.getLogger( TabbedFrame.class );

    public TabbedFrame( String title, String name, Dimension dimension )
    {
        logger.debug( "Creating TabbedFrame" );

        setTitle( title );
        setIconImage( CatsVisionUtils.getApplicationIcon() );

        setSize( dimension );

        setName( name );

        add( tabbedPane );

        tabbedPane.setTabLayoutPolicy( JTabbedPane.WRAP_TAB_LAYOUT );

        setLocationRelativeTo( null );

        setVisible( false );
    }

    public void addTab( String macID, JPanel panel )
    {
        logger.debug( "New tab added for settop-" + macID );
        int tabCount = tabbedPane.getTabCount();

        tabbedPane.add( SETTOP + macID, panel );

        setVisible( true );
        tabbedPane.setSelectedIndex( tabCount );

        toFront();
    }

    public boolean isTabExists( String mac )
    {
        boolean isTabPresent = false;
        int tabCount = tabbedPane.getTabCount();
        for ( int i = 0; i < tabCount; i++ )
        {
            if ( tabbedPane.getTitleAt( i ).contains( mac ) )
            {

                isTabPresent = true;
                selectTab( i );
                break;
            }
        }
        return isTabPresent;

    }

    private void selectTab( int i )
    {
        tabbedPane.setSelectedIndex( i );

        if ( getState() != JFrame.NORMAL )
        {
            setState( JFrame.NORMAL );
        }
        toFront();
    }

    protected void selectTab( JPanel panel )
    {
        tabbedPane.setSelectedComponent( panel );

        if ( getState() != JFrame.NORMAL )
        {
            setState( JFrame.NORMAL );
        }
        toFront();
    }

    public void removeAllTabs()
    {
        tabbedPane.removeAll();
    }

    public JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    }

    public void removeTab( String mac )
    {
        int tabCount = tabbedPane.getTabCount();

        for ( int i = 0; i < tabCount; i++ )
        {
            if ( tabbedPane.getTitleAt( i ).contains( mac ) )
            {
                tabbedPane.remove( i );

                break;
            }
        }
    }

    public void showTab( String macID )
    {
        int tabCount = tabbedPane.getTabCount();
        for ( int i = 0; i < tabCount; i++ )
        {
            if ( tabbedPane.getTitleAt( i ).contains( macID ) )
            {
                selectTab( i );

                break;
            }
        }
    }
}
