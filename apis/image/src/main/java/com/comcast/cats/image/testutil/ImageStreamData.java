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
package com.comcast.cats.image.testutil;

import java.awt.image.BufferedImage;

/**
 * Container class used to store some data.
 * @author mzmuda
 */
public class ImageStreamData {
    private BufferedImage image;
    private int timeout;
    
    /**
     * Sets each value.
     * @param image the image.
     * @param timeout The timeout.
     */
    public ImageStreamData(BufferedImage image, int timeout) {
        setImage(image);
        setTimeout(timeout);
    }
    
    /**
     * Sets the image.
     * @param image the image.
     */
    public final void setImage(BufferedImage image) {
        if (null == image) {
            throw new IllegalArgumentException("image cannot be null");
        }
        this.image = image;
    }
    
    /**
     * Returns the image.
     * @return the image.
     */
    public final BufferedImage getImage() {
        return image;
    }

    /**
     * Returns the timeout.
     * @return the timeout
     */
    public final int getTimeout() {
        return timeout;
    }
    
    /**
     * Sets the timeout.
     * Value must be > 0
     * @param timeout the timeout to set.
     */
    public final void setTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be > 0");
        }
        this.timeout = timeout;
    }
}
