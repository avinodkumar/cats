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
package com.comcast.cats.vision.panel.video;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.VideoEvent;

/**
 * This class contains the handler as well as the video panel and when an event
 * comes in it will trigger the paint.
 * 
 * The paint will get the image from the producer/source (video provider thread)
 * and paint.
 * 
 * @author modified by bemman01c
 */
public class VideoDisplayPanel extends JPanel implements CatsEventHandler
{

    /**
	 * 
	 */
    private static final long   serialVersionUID = -8924252252117363375L;

    /**
     * Current BufferedImage Image.
     */
    private BufferedImage       image;

    /**
     * Current VideoService object.
     */
    
    boolean                     clearRect        = false;

    public VideoDisplayPanel()
    {
        setBackground( new Color( 0, 0, 0, 0 ) );
    }

    /**
     * This event listener/handler will be triggered from the producer. It will
     * get an image and paint it.
     * 
     * @param evt
     *            CatsEvent
     */
    public void catsEventPerformed( final CatsEvent evt )
    {
        if ( evt instanceof VideoEvent )
        {
            VideoEvent vEvent = ( VideoEvent ) evt;
            image = vEvent.getImage();
            repaint();
        }
    }

    public void clearPanel()
    {
        clearRect = true;
        repaint();
    }


    public BufferedImage getImage()
    {
        return image;
    }


    @Override
    public void paint( Graphics g )
    {
        g.clearRect( 0, 0, getWidth(), getHeight() );
        if ( getImage() != null )
        {
            Graphics2D g2 = ( Graphics2D ) g;
            g2.setColor( Color.WHITE );
            g2.drawImage( getImage(), 0, 0, getWidth(), getHeight(), null );
        }
        super.paint( g );
       
    }
}
