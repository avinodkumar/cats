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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class dealing with stream recording.
 */
public class AxisServerStream {
    private static final String IMAGE_FORMAT = "JPG";
    private static final String IMAGE_URL = "image.cgi";

    private final Logger log = LoggerFactory.getLogger(AxisServerStream.class);

    /**
     * Axis Server URL.
     */
    private URL url;

    /** 
     * Object that transform an InputStream into a BufferedImage
     */
    private ImageReader imageReader;

    /** 
     * URL connection.
     */
    private HttpURLConnection httpConnection;

    /** 
     * Image stream handle.
     */
    private ImageInputStream iis;

    /**
     * Connection check.
     */
    private boolean isConnected;

    /**
     * Connection timeout for url connection
     */
    private int connectionTimeOut = 0;
    
    /**
     * Whether the URL provided is image URL or video url
     */
    private boolean isImageUrl = false;
    /**
     * Constructs a stream recorder object.
     * @param url The string url representation.
     * @throws MalformedURLException on invalid url syntax.
     */
    public AxisServerStream(final String url) throws MalformedURLException {
        this(new URL(url));
    }

    /**
     * Constructs a stream recorder object.
     * @param url The url object representation.
     */
    public AxisServerStream(final URL url) {
        if (null == url) {
            throw new IllegalArgumentException("url cannot be null");
        }
        this.url = url;
        ImageIO.setUseCache(false);
        if(url.toExternalForm().toLowerCase().contains(IMAGE_URL)){
        	isImageUrl = true;
        }
    }

    /**
     * Constructs a stream recorder object with Connection timeout set.
     * @param url The url object representation. 
     * @param connectionTimeOut The connection timeout in milliseconds.
     */
    public AxisServerStream(final URL url, int connectionTimeOut) {
        if (null == url) {
            throw new IllegalArgumentException("url cannot be null");
        }
        this.url = url;
        this.connectionTimeOut = connectionTimeOut;
        ImageIO.setUseCache(false);
    }
    
    public AxisServerStream(final String url, int connectionTimeOut) throws MalformedURLException {
        this(new URL(url), connectionTimeOut);
    }
    
    /**
     * Establishes the http connection.
     * @return True if the connection was successful, false otherwise.
     */
    public synchronized boolean connect() {
        if (!isConnected) {
            imageReader = (ImageReader) ImageIO.getImageReadersByFormatName(IMAGE_FORMAT).next();
            try {
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(connectionTimeOut);
                iis = ImageIO.createImageInputStream(httpConnection.getInputStream());
                isConnected = HttpURLConnection.HTTP_OK == httpConnection.getResponseCode();
            } catch (IOException ioe) {
                log.error("IOException caught trying to make connection: " + ioe.getMessage());
                disconnect();
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if connection already exists.
     * @return True if connection was established, false otherwise.
     */
    public synchronized boolean isConnected() {
        return isConnected;
    }

    /**
     * Closes the connection and any related streams.
     */
    public synchronized void disconnect() {
        isConnected = false;
        if (null != imageReader) {
            imageReader.abort();
            imageReader.dispose();
        }
        if (null != httpConnection) {
            httpConnection.disconnect();
        }
        try {
            if (null != iis) {
                iis.close();
            }
        } catch (IOException ioe) {
            log.error("IOException caught trying to close imageInputStream: " + ioe.getMessage());
        }
        imageReader = null;
        httpConnection = null;
        iis = null;
    }

    /** 
     * Get the url related to this stream.
     * @return The streams URL.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Captures a JPG from the connection.
     * @return BufferedImage from the stream. null if not connected.
     */
    public synchronized BufferedImage getNextImage() {
        BufferedImage image = null;
        if (isConnected()) {
            imageReader.setInput(iis, true);
            try {
                //Do the http header stripping only if the URL is video URL
            	if (!isImageUrl){
                boolean jpegFound = false;
                boolean contentTypeFound = false;
                String line;
                do {
                    line = iis.readLine();
                    if (line.indexOf("Content-") > -1) {
                        contentTypeFound = true;
                    } else if (contentTypeFound && (line.length() == 0)) {
                        jpegFound = true;
                    }
                } while (!jpegFound);
                }
                image = imageReader.read(0);
            } catch (Exception e) {
                log.error("Error occured while trying to read image: " + e.getMessage() + ". Disconnecting.");
                disconnect();
            }
        } else {
            log.debug("axis server stream connection: " + isConnected());
        }
        return image;
    }
}

