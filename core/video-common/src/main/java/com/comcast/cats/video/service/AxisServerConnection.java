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
package com.comcast.cats.video.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.stream.ImageInputStream;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import java.io.DataInputStream;

/**
 * This class provides low level control and connectivity to the video device.
 * 
 * @author jtyrre001
 */
public class AxisServerConnection
{

    /**
     * url of axis video device.
     */
    private URL                 url;

    /**
     * Data input stream.
     */
    private DataInputStream     dis;

    /**
     * Buffered image captured from video device.
     */
    private BufferedImage       image;

    /**
     * HTTP connection handle.
     */
    private HttpURLConnection   huc;

    /**
     * Image reader handle.
     */
    private ImageReader         imageDecoder;

    /**
     * Image input stream handle.
     */
    private ImageInputStream    iis;

    /**
     * video device connectivity flag.
     */
    private boolean             connected;

    AxisVideoSize               DEFAULT_VIDEO_SIZE = AxisVideoSize.AXIS_4CIF;

    private static final Logger logger             = Logger.getLogger( AxisServerConnection.class );
    private String              connectedURL;
    private final Object syncObject = new Object();
    /**
     * Initial Axis Server reader setup.
     */
    private void init()
    {
        ImageIO.setUseCache( false );
        imageDecoder = ( ImageReader ) ImageIO.getImageReadersByFormatName( "JPEG" ).next();
    }

    /**
     * AxisServerConnection Constructor.
     * 
     * @param url
     *            URL of Axis Server device
     * @throws MalformedURLException
     */
    public AxisServerConnection( final String url ) throws MalformedURLException
    {
        this.url = new URL( url );
        init();
    }

    /**
     * Returns the Axis Server device URL.
     * 
     * @return url URL of Axis Server device
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * The act of setting the url could initiate a disconnect. I'm leaving this
     * alone for now in case a situation arises where you want to change the URL
     * but video should still be coming out from the old location. This should
     * allow for more flexibility.
     * 
     * @param url
     *            URL of Axis Server device
     */
    public void setUrl( final URL url )
    {
        this.url = url;
    }

    /**
     * Sets the Axis Server device URL.
     * 
     * @param url
     *            URL of Axis Server device
     * @throws MalformedURLException
     */
    public void setUrl( final String url ) throws MalformedURLException
    {
        this.url = new URL( url );
    }

    /**
     * HTTP Connect to the Axis Server device.
     * 
     * @param fps
     *            Frames per second
     * @param vidDim
     *            Selected video dimension to size screen to
     * @throws MalformedURLException
     */
    public synchronized void connect() throws MalformedURLException
    {
        synchronized ( syncObject )
        {
            
        
        logger.debug( "Connected " + connected + " url " + url + " connectedURL " + connectedURL );
        // DE3423 : Reconnect if not already connected or if URL has changed and
        // hence new connection should be established
        if ( url != null )
        {
            if ( !connected || !url.toString().equals( connectedURL ) )
            {
                try
                {
                    logger.trace( "Connecting to AxisServer at URL[" + url + "]" );
                    huc = ( HttpURLConnection ) url.openConnection();
                    if( huc.getUseCaches() ) 
                    {
                        huc.setUseCaches( false );
                    }                    
                    logger.debug( "useCache is :" +  huc.getUseCaches() + " for url: " + url );

                    dis = new DataInputStream( new BufferedInputStream( huc.getInputStream() ) );
                    iis = ImageIO.createImageInputStream( dis );
                    connected = true;
                    connectedURL = url.toString();
                    
                }
                catch ( Exception e )
                {
                    logger.warn( "Exception caught during establishing connection to " + url, e );
                    disconnect();
                }
            }
        }
        }
    }

    /**
     * HTTP Disconnect from the Axis Server Device.
     */
    public synchronized void disconnect()
    {
        synchronized ( syncObject )
        {
        try
        {
            if ( connected )
            {
 
                logger.debug( "Disconnecting: Currently connected? " + connected );
                connected = false;
                if ( dis != null )
                {
                    dis.close();
                }
                if ( iis != null )
                {
                    iis.close();
                }
                if ( huc != null )
                {
                    huc.disconnect();
                }
                dis = null;
                iis = null;
            }
        }
        catch ( Exception e )
        {
            logger.debug( "Axis Server not disconnected properly " + e.getMessage() );
            e = null;
        }
        }
    }

    /**
     * Reads a JPEG image from the Axis Server device.
     * 
     * @throws IOException
     */
    private synchronized void readJPG() throws IOException
    {
        synchronized ( syncObject )
        {
        if ( !connected )
        {
            return;
        }
        imageDecoder.setInput( iis, true );
        boolean jpegFound = false;
        boolean contentTypeFound = false;
        String line;
        int retryCount=0;
        do
        {
        	//logger.info("readJPEG on :"+url+"retry count:"+retryCount);
        	retryCount++;
            line = iis.readLine();
           
            if(line != null){
                if ( line.indexOf( "Content-" ) > -1 )
                {
                	//logger.debug(" Read the line:"+line  +"retry count"+retryCount);
                    contentTypeFound = true;
                }
                else if ( ( contentTypeFound ) && ( line.length() == 0 ) )
                {
                    jpegFound = true;
                }
            }
            /*
             * bemman01c: I still dont understand why this do while is put here.
             * But if i remove this i get an exception from the ImageDecoder.
             * ===================================================================
             * else if ( ( contentTypeFound ) && ( line.length() == 0 ) )
             *   {
             *       jpegFound = true;
             *   }
             *   why is the length check doing an equal to 0 check and deciding it is jpeg.
             *   
             *   Adding a break condition so that this does not go into an infinite loop.
             *   What i found was the Content-length or Content-imagetype strings comes in the 3rd
             *   or 4th retry. Putting a bigger value 20 just to be on the safer side.
             */
            if(retryCount>20){
            	logger.error("Reading jpeg from Axis url failed. URL:"+url);
            	throw new IOException("Reading jpeg from Axis url failed.URL:"+url);
            }
        } while ( !jpegFound && connected );
        image = imageDecoder.read( 0 );
        }
    }

    /**
     * Returns Axis Server device connection status true or false.
     * 
     * @return connected connection status to Axis Server device
     */
    public boolean isConnected()
    {
        return connected;
    }

    /**
     * Returns a snapshot JPEG image captured from the Axis server Device.
     * 
     * @return image JPEG image
     * @throws IOException
     */

    public BufferedImage getImage() throws IOException
    {
        readJPG();
        return image;
    }

    /**
     * Returns an ArrayList (table) of available Axis Video Server screen
     * resolutions. This is a list of a list which creates a multidemensioned
     * List (table). [col1: Dimension id] [col2: Dimension w x h]
     * 
     * @return ArrayList Arraylist of available screen dimensions.
     */
    public static Collection< String > getAvailableVideoResolutions()
    {
        return AxisVideoSize.getVideoSizesAsString();
    }
    

}
