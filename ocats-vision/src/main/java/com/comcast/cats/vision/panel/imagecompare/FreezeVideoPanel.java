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
package com.comcast.cats.vision.panel.imagecompare;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.VideoEvent;
import com.comcast.cats.image.RegionInfo;

public class FreezeVideoPanel extends JPanel implements MouseListener, MouseMotionListener, CatsEventHandler
{

    private static final long   serialVersionUID = -758016315574347047L;

    private static final Logger logger           = Logger.getLogger( FreezeVideoPanel.class );

    private Point               start            = new Point();
    private Point               end              = start;
    private BufferedImage       image            = null;

    private BufferedImage       backUpSnapshot   = null;
    private final BasicStroke   dashed           = new BasicStroke( 1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                                         10.0f, new float[]
                                                             { 10.0f }, 0.0f );
    private int                 rectX;
    private int                 rectY;
    private int                 rectWidth;
    private int                 rectHeight;
    private Rectangle2D.Double  selectionRect    = new Rectangle2D.Double( 0, 0, 0, 0 );
    private RegionDetailsPanel  regionDetailsPanel;
    private boolean             videoMode        = false;

    private List< RegionInfo >  regionsList;

    public FreezeVideoPanel( BufferedImage image )
    {
        this.image = image;
        initImageScreen();
        addMouseListener( this );
        addMouseMotionListener( this );
        setMinimumSize( getSize() );
    }

    /**
     * Initialize the panel screen dimension/size. This call is needed in order
     * to display the initial image screen at 720x480.
     */
    public void initImageScreen()
    {
        logger.info( "Initializing Image panel" );
        GroupLayout layout = new GroupLayout( this );
        this.setLayout( layout );
        refreshSize();
    }

    /**
     * Initiate painting of a region.
     * 
     * @param region
     *            The region details.
     */
    public void paintRegion( RegionInfo region )
    {
        if ( region != null )
        {
            start = new Point( region.getX(), region.getY() );
            end = new Point( region.getX() + region.getWidth(), region.getY() + region.getHeight() );
            repaint();
        }
    }

    /**
     * Clear all regions from the snapshot.
     */
    public void clearRegion()
    {

        start.setLocation( 0, 0 );
        end = start;
        regionsList = null;
        repaint();
    }

    /**
     * Paints all regions on the snapshot.
     * 
     * @param regionsList
     *            List of regions
     */
    public void paintRegions( List< RegionInfo > regionsList )
    {
        if ( regionsList != null )
        {
            this.regionsList = regionsList;
        }
    }

    /**
     * Paint method
     */
    public void paint( Graphics g )
    {
        Graphics2D g2 = ( Graphics2D ) g;
        g2.clearRect( 0, 0, this.getWidth(), this.getHeight() );
        g2.setStroke( dashed );
        g2.setColor( Color.WHITE );

        if ( !videoMode )
        {
            if ( image != null )
            {

                // Calculate the rectangle.
                rectX = ( int ) start.getX();
                rectY = ( int ) start.getY();
                rectWidth = ( int ) ( end.getX() - start.getX() );
                rectHeight = ( int ) ( end.getY() - start.getY() );
                if ( rectWidth < 0 )
                {
                    rectX += rectWidth;
                    rectWidth *= -1;
                }
                if ( rectHeight < 0 )
                {
                    rectY += rectHeight;
                    rectHeight *= -1;
                }

                if ( start != end )
                {
                    // User is selecting a region, draw image translucent.
                    BufferedImage translucentSnapShot = ImageFunctions.maskImageTranslucent( image, 0.7f );
                    g2.drawImage( translucentSnapShot, 0, 0, ( int ) ( translucentSnapShot.getWidth() ),
                            ( int ) ( translucentSnapShot.getHeight() ), this );
                }
                else
                {
                    // No region is being selected, draw the actual image.
                    rectWidth = 0;
                    rectHeight = 0;
                    g2.drawImage( image, 0, 0, ( int ) ( image.getWidth() ), ( int ) ( image.getHeight() ), this );
                }

                // Draw the non transparent subimage in the selected area.
                if ( rectWidth > 0 && rectHeight > 0 )
                {

                    // Make sure we don't draw outside of the raster.
                    if ( ( rectX + rectWidth ) >= image.getWidth() )
                    {
                        rectWidth = image.getWidth() - rectX;
                    }
                    else if ( rectX < 0 )
                    {
                        rectX = 0;
                    }

                    if ( ( rectY + rectHeight ) >= image.getHeight() )
                    {
                        rectHeight = image.getHeight() - rectY;
                    }
                    else if ( rectY < 0 )
                    {
                        rectY = 0;
                    }
                    g2.drawImage( image.getSubimage( rectX, rectY, rectWidth, rectHeight ), rectX, rectY, this );
                    selectionRect.setFrame( rectX, rectY, rectWidth, rectHeight );
                    g2.draw( selectionRect );
                }

                // Update the panel with coordinates.
                if ( regionDetailsPanel != null )
                {
                    regionDetailsPanel.setRegionInfo( rectX, rectY, rectWidth, rectHeight );
                }
            }
        }
        else
        {
            g2.drawImage( image, 0, 0, ( int ) ( image.getWidth() ), ( int ) ( image.getHeight() ), this );

            if ( regionsList != null )
            {
                for ( RegionInfo region : regionsList )
                {
                    g2.drawRect( region.getX(), region.getY(), region.getWidth(), region.getHeight() );
                }
            }
        }
    }

    @Override
    public void mousePressed( MouseEvent e )
    {
        start = e.getPoint();
    }

    @Override
    public void mouseDragged( MouseEvent e )
    {
        end = e.getPoint();
        // Don't let the end point go off screen.
        if ( end.getX() < 0 )
        {
            end.setLocation( 0, end.getY() );
        }
        if ( end.getY() < 0 )
        {
            end.setLocation( end.getX(), 0 );
        }
        repaint();
    }

    @Override
    public void mouseClicked( MouseEvent e )
    {
        start = end = e.getPoint();
        repaint();
    }

    @Override
    public void mouseEntered( MouseEvent e )
    {
    }

    @Override
    public void mouseExited( MouseEvent e )
    {
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
    }

    @Override
    public void mouseMoved( MouseEvent e )
    {
    }

    /**
     * Registers the RegionDetailsPanel that will maintain the region
     * information of this snapshot.
     * 
     * @param regionDetailsPanel
     *            The region details panel.
     */
    public void registerDetailsPanel( RegionDetailsPanel regionDetailsPanel )
    {
        if ( null != regionDetailsPanel )
        {
            this.regionDetailsPanel = regionDetailsPanel;
        }
    }

    /**
     * Sets a snapshot to this panel as the current frozen image.
     * 
     * @param image
     *            The snapshot to be set.
     */
    public void setSnapshot( final BufferedImage image )
    {
        this.image = image;
        refreshSize();
        repaint();
    }

    private void refreshSize()
    {
        GroupLayout layout = ( GroupLayout ) getLayout();
        layout.setHorizontalGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING ).addGap( 0,
                image.getWidth(), Short.MAX_VALUE ) );
        layout.setVerticalGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING ).addGap( 0,
                image.getHeight(), Short.MAX_VALUE ) );

    }

    /**
     * Get the current frozen image.
     * 
     * @return
     */
    public BufferedImage getSnapshot()
    {
        return image;
    }

    /**
     * Start video mode; The panel will run the current video.
     */
    public void startVideoMode()
    {
        videoMode = true;
        backUpSnapshot = image; // back up the frozen image.

    }

    /**
     * Stop video.
     */
    public void stopVideoMode()
    {
        image = backUpSnapshot; // revert back to the snapshot.
        repaint();
        videoMode = false;
    }

    @Override
    public void catsEventPerformed( CatsEvent evt )
    {
        if ( evt instanceof VideoEvent )
        {
            VideoEvent vEvent = ( VideoEvent ) evt;
            image = vEvent.getImage(); // update the current frame and reapint.
            repaint();
        }

    }

    public boolean isVideoMode()
    {
        return videoMode;
    }

    public void setVideoMode( boolean videoMode )
    {
        this.videoMode = videoMode;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage( BufferedImage image )
    {
        this.image = image;
        repaint();
    }
}
