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

import java.awt.Rectangle;

import javax.swing.JDialog;
import org.apache.log4j.Logger;

/**
 * ZoomedVideo holds the zoomed VideoPanel
 * 
 * @author aswathyann
 * 
 */
public class ZoomedVideo extends JDialog
{
    private static final long      serialVersionUID = 5412440530209735824L;
    private static final Rectangle FRAME_BOUNDS     = new Rectangle( 80, 150, 1136, 800 );
    private static Logger          logger           = Logger.getLogger( ZoomedVideo.class );

    private VideoPanel             videoPanel;

    /**
     * Constructor for ZoomFrame
     * 
     * @param videoPanel
     *            instance of VideoPanel
     */
    public ZoomedVideo( VideoPanel videoPanel )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Creating ZoomFrame" );
        }
        this.videoPanel = videoPanel;

        setTitle( "Zooming... Please wait..." );
        setVisible( true );
        setBounds( FRAME_BOUNDS );

        add( videoPanel );

        setTitle( "Zoomed video" );
        videoPanel.setVisible( true );
    }

    /**
     * Get VideoPanel
     * 
     * @return VideoPanel
     */
    public VideoPanel getVideoPanel()
    {
        return videoPanel;
    }
}
