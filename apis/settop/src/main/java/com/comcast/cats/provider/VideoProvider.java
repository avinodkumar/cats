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
package com.comcast.cats.provider;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

/**
 * Interface that defines Video usage for a device like Axis Camera. This video
 * interface assumes that Video streaming would be part of the implementation.
 * 
 * @author cfrede001
 */
public interface VideoProvider extends BaseProvider
{

    /**
     * Get Video device hardware information in URI form. ie:
     * axis://<ip>:<port>/?camera=1
     * 
     * @return URI of Video device.
     */
    public URI getVideoLocator();

    /**
     * Return streaming state of Xxis video device.
     * 
     * @return True if video is currently streaming, false otherwise.
     */
    public boolean isStreaming();

    /**
     * Return connected state of Axis video device.
     * 
     * @return True if video device is currently connected.
     */
    public boolean isConnected();

    /**
     * Connects to the Axis video device.
     */
    public void connectVideoServer();

    /**
     * Disconnects from the Axis video device.
     */
    public void disconnectVideoServer();

    /**
     * Update the frame rate of the video source.
     * 
     * @precondition Video is currently not streaming.
     * @postcondition Video frame rate has been changed.
     * @param fps
     *            - Frames per second video feed should be streamed at.
     */
    public void setFrameRate( Integer fps );

    /**
     * Get current frame rate.
     * 
     * @return The current frame rate for the video provider.
     */
    public Integer getFrameRate();

    /**
     * Get the available video sizes for this video provider.
     * 
     * @return - List of dimensions specifying video sizes applicable for this
     *         device.
     */
    public List< ? > getAvailableVideoSizes();

    /**
     * Set the video dimensions, which must be one specified by the
     * getAvailableVideoSizes() call.
     * 
     * @precondition Video is currently not streaming.
     * @postcondition Video size will be updated during next streaming call.
     * @param dim
     *            - Video size that should be set.
     */
    public void setVideoSize( Dimension dim );

    /**
     * Gets the video size requested by the client.
     * 
     * @return vidDimLookup video height and width string
     */
    public Dimension getVideoSize();

    /**
     * Gets a still video image from the Axis Camera.
     * 
     * @return BufferedImage
     */
    public BufferedImage getVideoImage();

    /**
     * Gets a still video image from the Axis Camera with specified dimension.
     * 
     * @return BufferedImage
     */
    public BufferedImage getVideoImage( Dimension dim );

    /**
     * Gets the axis server URL.
     * 
     * @return URL of the axis server device
     */
    public String getVideoURL();

    /**
     * TBD Describes a video message for streaming video.
     * 
     * @return videoMessage video message.
     */
    // public videoMessage videoEvent();

    // Marker
    /**
     * Starts reading the video stream and parsing out frames in frames/per
     * second.
     */
    // public void startStreamingVideo();

    /**
     * Saves a still video image from the Axis Camera to CATS_HOME.
     * 
     * @return The file path of saved image.
     */
    String saveVideoImage();

    /**
     * Saves a still video image from the Axis Camera to the specified location.
     * 
     * @param file
     *            - file location
     * @throws IOException
     */
    void saveVideoImage( String file );
}