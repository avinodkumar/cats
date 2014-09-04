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
package com.comcast.cats.vision.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

/**
 * A simple hyper link component.
 * 
 * @author ssugun00c
 * 
 */
public class JHyperlink extends JLabel
{
    private static final long   serialVersionUID = 1L;
    private static final Logger LOGGER           = Logger.getLogger( JHyperlink.class );
    private Color               underlineColor   = null;
    private String              url;

    public JHyperlink( String label )
    {
        super( label );
        setForeground( Color.BLUE.darker() );
        setCursor( new Cursor( Cursor.HAND_CURSOR ) );
        addMouseListener( new HyperlinkLabelMouseAdapter() );
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        g.setColor( underlineColor == null ? getForeground() : underlineColor );

        Insets insets = getInsets();

        int left = insets.left;
        if ( getIcon() != null )
            left += getIcon().getIconWidth() + getIconTextGap();

        g.drawLine( left, getHeight() - 1 - insets.bottom, ( int ) getPreferredSize().getWidth() - insets.right,
                getHeight() - 1 - insets.bottom );
    }

    public class HyperlinkLabelMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if ( null != getUrl() )
            {
                OpenBrowser( getUrl() );
            }
        }
    }

    private void OpenBrowser( String url )
    {
        String os = System.getProperty( "os.name" ).toLowerCase();

        Runtime rt = Runtime.getRuntime();
        try
        {
            if ( os.indexOf( "win" ) >= 0 )
            {
                rt.exec( "rundll32 url.dll,FileProtocolHandler " + url );
            }
            else if ( os.indexOf( "mac" ) >= 0 )
            {
                rt.exec( "open " + url );
            }
            else if ( os.indexOf( "nix" ) >= 0 || os.indexOf( "nux" ) >= 0 )
            {
                String[] browsers =
                    { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx" };

                // Build a command string which looks like
                // "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for ( int i = 0; i < browsers.length; i++ )
                    cmd.append( ( i == 0 ? "" : " || " ) + browsers[ i ] + " \"" + url + "\" " );

                rt.exec( new String[]
                    { "sh", "-c", cmd.toString() } );
            }
            else
            {
                return;
            }
        }
        catch ( Exception e )
        {
            LOGGER.error( "Failed to open browser" );
        }
        return;
    }

    public Color getUnderlineColor()
    {
        return underlineColor;
    }

    public void setUnderlineColor( Color underlineColor )
    {
        this.underlineColor = underlineColor;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

}
