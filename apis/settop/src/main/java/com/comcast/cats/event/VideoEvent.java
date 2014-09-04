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
package com.comcast.cats.event;

import java.awt.image.BufferedImage;

/**
 * Class for the VideoEvent
 * 
 */
public class VideoEvent extends CatsEvent
{

    /**
     * Serial version id
     */
    private static final long serialVersionUID = -8820459291010302916L;
    BufferedImage             image;

    /**
     * Constructor
     */
    public VideoEvent()
    {
    }

    /**
     * Constructor
     * 
     * @param sequence
     * @param sourceId
     * @param source
     */
    public VideoEvent( Integer sequence, String sourceId, Object source )
    {
        super( sequence, sourceId, source, CatsEventType.VIDEO );
    }

    /**
     * Constructor
     * 
     * @param sequence
     * @param sourceId
     * @param source
     * @param image
     *            {@link BufferedImage}
     */
    public VideoEvent( Integer sequence, String sourceId, Object source, BufferedImage image )
    {
        super( sequence, sourceId, source, CatsEventType.VIDEO );
        this.image = image;
    }

    /**
     * To get the BufferedImage
     * 
     * @return BufferedImage
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * To set the BufferedImage
     * 
     * @param image
     *            BufferedImage
     */
    public void setImage( BufferedImage image )
    {
        this.image = image;
    }
}