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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.provider.VideoProvider;

/**
 *
 * A simple EJB service that can affect the video to a STB based on the path
 * to the video device.
 *
 * @author jtyrre001
 *
 */
public class VideoServiceImpl implements VideoProvider
{
    
    private static final long serialVersionUID      = 1L;
    private static final Logger logger              = Logger.getLogger(VideoServiceImpl.class);
    private static final String IMAGE_FILE_FORMAT   = "jpg";
    private static final String DATE_TIME_FORMAT    = "yyyy.MM.dd-HH.mm.ss";
    
    /**
     * 
     */
    private Object parentObj;
    
    /**
     * VideoController object.
     */
    private final VideoController videoController;

    /**
     * VideoServiceImpl constructor.
     * Instantiates an videoController instance.
     * @throws MalformedURLException 
     */
    public VideoServiceImpl(final CatsEventDispatcher dispatcher, final URI videoLocator) throws MalformedURLException
    {
        if(dispatcher == null || videoLocator == null) {
            throw new IllegalArgumentException("CatsEventDispatcher and Video Locator must not be null");
        }
        this.videoController = new VideoController(dispatcher, videoLocator, this);
    }

    /**
     * Returns an ArrayList of available video sizes retrieved from the Axis Device.
     * First column is resolution id: D1, 4CIF, CIF etc.
     * Second column is resolution: 720 x 480 etc.
     *
     * @return vidSizelist    Table list of all available video sizes
     */
    public List<String> getAvailableVideoSizes()
    {
        return (List<String>) videoController.getAvailableVideoSizes();
    }

    /**
     * Sets the video size requested by the client.
     *
     *  @param vidSize      Video Size / dimension of screen.
     */
    public void setVideoSize(final Dimension vidSize)
    {
        this.videoController.setVideoDimension(vidSize);
        if(this.videoController.isConnected()) {
            try {
                this.videoController.connect();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the frame read rate from the incoming stream.
     *
     * @param fps      frame rate in frames/per second
     */
    public void setFrameRate(final Integer fps)
    {
        videoController.setFrameRate(fps);
    }

    /**
     * Returns the frame read rate from the incoming stream.
     *
     * @return rate     frame rate in frames/per second
     */
    public Integer getFrameRate()
    {
        return videoController.getFrameRate();
    }

    /**
     * Returns the current video streaming dimensions.
     */
    @Override
    public Dimension getVideoSize() {
        return videoController.getVideoDimension();
    }
    
    /**
     * Returns the URI of the Axis Server device.
     *
     * @return URI  axis server URI
     */
    public URI getVideoLocator()
    {
        return videoController.getVideoLocator();
    }

    /**
     * Returns streaming status of server true or false.
     *
     * @return videoController.isStreaming()
     */
    public boolean isStreaming()
    {
        return this.videoController.isStreaming();
    }

    /**
     * Returns connect status to Axis Server true or false.
     *
     * @return videoController.isConnected()
     */
    public boolean isConnected()
    {
        return this.videoController.isConnected();
    }
    
    /**
     * Makes the HTTP connection to the Axis Server device.
     */
    public void connectVideoServer()
    {
        try
        {
            videoController.connect();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects from the Axis Server device.
     * Note: A disconnect automatically stop streaming video.
     */
    public void disconnectVideoServer()
    {
        this.videoController.disconnectFromAxisDevice();
    }

    /**
     * Gets a still video image from the Axis Camera.
     *
     * @return BufferedImage
     */
    @Override
    public BufferedImage getVideoImage() {
        return this.videoController.getImage();
    }

    /**
     * Gets a still video image from the Axis Camera.
     *
     * @return BufferedImage
     */
    @Override
    public BufferedImage getVideoImage(Dimension dim) {
        return this.videoController.getImage(dim);
    }
    
    
    public void setParent(Object parent) {
        this.parentObj = parent; 
    }
    
    @Override
    public Object getParent() { 
        return this.parentObj;
    }

    @Override
    public String getVideoURL()
    {
        return videoController.getURL();
    }

    @Override
    public String saveVideoImage()
    {
        String fileLocation = null;
        String catsHome = System.getProperty( "cats.home" );
        if( null != catsHome ){
            fileLocation = catsHome + System.getProperty( "file.separator" ) + getFolderAndFileName();
            saveVideoImage(fileLocation);
        }
        else
        {
            logger.error("CATS HOME is not set. Saving Image cannot continue");
        }
        return fileLocation;
    }

    @Override
    public void saveVideoImage( String filePath )
    {
        if( null == filePath || filePath.isEmpty() )
        {
           logger.error( "Saving video image failed. The file location cannot be null or empty: " + filePath );
        }
        else
        {
            String imagePath = filePath;
            File imagefile = new File( imagePath );
            // If the file path specified is a relative path, the image must be saved in to CATS_HOME/$macFolder/<filePath>
            if( !imagefile.isAbsolute() )
            {
                String catsHome = System.getProperty( "cats.home" );
                if( null != catsHome )
                {
                    imagePath = catsHome + System.getProperty( "file.separator" ) + getCleanMac() + 
                        System.getProperty( "file.separator" )+ imagePath;
                }
                imagefile = new File( imagePath );
            }
            
            /**
             * If the specified file path is of an existing directory, the image will be saved in to 
             * <filepath>/$macFolder/$defaultFileName.jpg
             */
            if( imagefile.exists() && imagefile.isDirectory() )
            {
                imagePath = imagePath + System.getProperty( "file.separator" ) + getFolderAndFileName();
                imagefile = new File( imagePath );
            }
            
            try
            {
                FileUtils.forceMkdir( imagefile.getAbsoluteFile().getParentFile() );
                ImageIO.write( getVideoImage(), IMAGE_FILE_FORMAT, imagefile );
                logger.info( "Saved video image to file: " + imagefile.getAbsolutePath() );                    
            }
            catch (Exception e)
            {
                logger.error( "Saving video image failed. Error in creating file: '" + imagePath + "'.\n" + e.getMessage() );
            }
        }
    }
    
    private String getFolderAndFileName()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_TIME_FORMAT );
        String dateString =  dateFormat.format( date ); 
        
        return getCleanMac() + System.getProperty( "file.separator" ) + dateString + "." + IMAGE_FILE_FORMAT;
    }
    
    private String getCleanMac()
    {
        String cleanMac = "";
        if( null != getParent() )
        {
            try
            {
                Settop settop = (Settop) getParent();
                String hostMacAddress = settop.getHostMacAddress();            
                cleanMac = hostMacAddress.trim().replace( ":", "" ).toUpperCase();
            }
            catch(ClassCastException e)
            {
                logger.error( "VideoProvider doesn't have a parent settop." );
            }
        }
        return cleanMac;        
    }
}