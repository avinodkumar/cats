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
package com.comcast.cats.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for BufferedImage.
 * Used to load a BufferedImage from file or resource.
 * 
 * @author mzmuda
 */
public class BufferedImageLoader {
    private final Logger log = LoggerFactory.getLogger(BufferedImageLoader.class);
    
    private String path;
    private Class<?> resourceClass;

    /**
     * Sets the path to load from.
     * @param path The path to load the BufferedImage from.
     */
    public BufferedImageLoader(String path) {
        setPath(path);
    }

    /**
     * Sets the path and resourceClass.
     * @param path The path to load the BufferedImage from.
     * @param resourceClass The Class that is in the same location as the resources we will load.
     *                      If this is null if we load from disk.
     */
    public BufferedImageLoader(Class<?> resourceClass, String path) {
        setPath(path);
        setResourceClass(resourceClass);
    }

    /**
     * Sets the path to load from.
     * @param path The path to load the BufferedImage from.
     */
    public final void setPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path cannot be null or empty");
        }

        this.path = path;
    }

    /**
     * Returns the set path.
     * @return The path.
     */
    public final String getPath() {
        return path;
    }

    /**
     * Returns the set resource class.
     * @return The resource class.
     */
    public final Class<?> getResourceClass() {
        return resourceClass;
    }

    /**
     * Sets the resourceClass.
     * @param resourceClass The Class that is in the same location as the resources we will load.
     *                      If this is null if we load from disk.
     */
    public final void setResourceClass(Class<?> resourceClass) {
        this.resourceClass = resourceClass;
    }

    /**
     * Loads a BufferedImage from path if
     * the specified path.
     * @return A BufferedImage or null if no image can be loaded.
     */
    public BufferedImage loadImage(String path) {
    	setPath(path);
        return loadImage();
    }
    
    /**
     * Loads a BufferedImage from the specified path.
     * @return A BufferedImage or null if no image can be loaded.
     */
    public BufferedImage loadImage() {
        BufferedImage img = null;
        if (resourceClass != null) { // check if image is available in a jar or not
            img = loadImageResource();
        } 
        if(img == null) { // check if image exists as a file on disk.
            img = loadImageFromFile();
        }
        return img;
    }

    /**
     * Loads a BufferedImage from file.
     * @return The file as a Buffered Image. If the file is not found, null is returned.
     */
    public BufferedImage loadImageFromFile() {
        BufferedImage img = null;
        File theFile = new File(path);
        if (theFile.isFile()) {
            try {
                img = ImageIO.read(theFile);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Cannot load image from path: " + theFile.getAbsolutePath());
            }
        }
        return img;
    }

    /**
     * Loads a BufferedImage from a resource.
     * @return The resource as a Buffered Image. If the resource is not found, null is returned.
     */
    public BufferedImage loadImageResource() {
        BufferedImage img = null;
        InputStream is = ResourceUtil.loadResource(resourceClass, path);
        if (is != null) {
            try {
                img = ImageIO.read(is);
            } catch (IOException e) {
                log.error("Cannot load image as resource: " + path);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("Could not close input stream: " + e.getMessage());
                }
            }
        }
        return img;
    }   
}

