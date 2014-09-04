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
package com.comcast.cats.imageutility;

import java.awt.image.BufferedImage;

/**
 * Class for storing the zoomed image and the zoom scale values after zoom
 * operation.
 * 
 * @author Manesh Thomas
 */
public class ZoomedImage
{
    /**
     * Zoom scale.
     */
    int           zoom  = 0;
    /**
     * Image after zoom operation.
     */
    BufferedImage image = null;

    /**
     * @return the zoom
     */
    public int getZoom()
    {
        return zoom;
    }

    /**
     * @param zoom
     *            the zoom to set
     */
    public void setZoom( int zoom )
    {
        this.zoom = zoom;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * @param image
     *            the image to set
     */
    public void setImage( BufferedImage image )
    {
        this.image = image;
    }

    /**
     * Default Constructor.
     */
    public ZoomedImage( int zoom, BufferedImage image )
    {
        this.zoom = zoom;
        this.image = image;
    }

}
