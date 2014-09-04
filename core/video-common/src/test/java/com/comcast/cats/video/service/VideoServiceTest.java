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

import java.io.File;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import static org.junit.Assert.*;
import org.junit.Test;
import junit.framework.Assert;

import com.comcast.cats.provider.VideoProvider;

/**
 * Performs Junit testing on the Axis server classes.
 * 
 * Note: These tests can be brittle and brake if the hardware pointed to by the
 * VideoLocator URL/URI goes away or changes.
 * 
 * @author cfrede001
 */
public class VideoServiceTest extends BaseVideo
{
    Dimension DEFAULT_VIDEO_RESOLUTION = new Dimension( 640, 480 );
    Integer   DEFAULT_FRAME_RATE       = 10;
    String    VIDEO_STRING_704_X_480   = "704x480";

    /**
     * Tests setting the Axis server URI.
     * 
     * @throws URISyntaxException
     */
    public VideoServiceTest() throws URISyntaxException
    {
        super();
    }

    /**
     * Test setting the Video Dimension with a specified setting.
     * 
     * @throws MalformedURLException
     */
    @Test
    public void testSetVideoDimension() throws MalformedURLException
    {
        VideoProvider vp = new VideoServiceImpl( dispatcher, axisLocator );
        Dimension dm = new Dimension();
        dm.width = 704;
        dm.height = 480;
        vp.setVideoSize( dm );
        String videoDim = Integer.toString( vp.getVideoSize().width ) + "x"
                + Integer.toString( vp.getVideoSize().height );
        Assert.assertEquals( VIDEO_STRING_704_X_480, videoDim );
    }

    /**
     * Test Connection to the Axis Video Server.
     * 
     * @throws MalformedURLException
     */
    @Test
    public void testConnectAxisVideo() throws MalformedURLException
    {
        VideoServiceImpl vp = new VideoServiceImpl( dispatcher, axisLocator );
        Dimension dm = new Dimension();
        dm.width = 704;
        dm.height = 480;
        vp.setVideoSize( dm );
        vp.connectVideoServer();
        Assert.assertTrue( "Axis Server connection passed", vp.isConnected() );
        vp.disconnectVideoServer();
        Assert.assertFalse( "Axis Server disconnect passed", vp.isConnected() );
    }

    /**
     * Test the default frames per second setting. Default setting is 10fps.
     * 
     * @throws MalformedURLException
     */
    @Test
    public void testAxisDefaultFrameRate() throws MalformedURLException
    {
        VideoProvider vp = new VideoServiceImpl( dispatcher, axisLocator );
        vp.setFrameRate( 0 );
        Assert.assertTrue( "Default fps passed", vp.getFrameRate() == DEFAULT_FRAME_RATE );
    }

    /**
     * Test setting the frames per.
     * 
     * @throws MalformedURLException
     */
    @Test
    public void testAxisFrameRate() throws MalformedURLException
    {
        VideoProvider vp = new VideoServiceImpl( dispatcher, axisLocator );
        vp.setFrameRate( 15 );
        Assert.assertTrue( "Set fps passed", vp.getFrameRate() == 15 );
    }

    @Test
    public void testNoStreamingImageRetrieval() throws IOException
    {
        VideoProvider vp = new VideoServiceImpl( dispatcher, axisLocator );
        assertFalse( vp.isStreaming() );
        BufferedImage image = vp.getVideoImage();
        assertTrue( image != null );
        System.out.println( "Image Dimenstions " + String.valueOf( image.getWidth() ) + "x"
                + String.valueOf( image.getHeight() ) );
        ImageIO.write( image, "JPG", new File( "noStream.jpg" ) );
        assertTrue( image.getHeight() == DEFAULT_VIDEO_RESOLUTION.height );
        assertTrue( image.getWidth() == DEFAULT_VIDEO_RESOLUTION.width );
    }

    @Test
    public void testSaveVideoImage() throws MalformedURLException
    {
        VideoProvider vp = new VideoServiceImpl( dispatcher, axisLocator );
        vp.saveVideoImage( "captureScreen.jpg" );
        assertTrue( new File( "captureScreen.jpg" ).exists() );
    }
}
