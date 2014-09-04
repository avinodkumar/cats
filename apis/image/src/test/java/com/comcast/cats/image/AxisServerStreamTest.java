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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.comcast.cats.image.testutil.ImageStreamData;
import com.comcast.cats.image.testutil.JettyAxisCameraServer;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;


/**
 * Tests AxisServerStream class.
 * @author mzmuda
 */
public class AxisServerStreamTest {
    
    private static JettyAxisCameraServer server;
    
    /**
     * Camera1 ID.
     */
    public static final int CAM1 = 1;
    
    /**
     * Camera2 ID.
     */
    public static final int CAM2 = 2;
    
    /**
     * Camera3 ID.
     */
    public static final int CAM3 = 3;
    
    private static final int GENERAL_TIMEOUT = 20000; 
    
    /* These URLS are set when jetty web server is setup */
    private static String s_mpeg_url = "";    
    private static String s_bad_url = "";
    
    private static final String JETTY_TEST = "jetty";
    
    private static final String CONSTRUCTOR_TEST = "constructor";

    private static final String PORT_PROPS = "portConfig" + File.separator + "ports.properties";
        
    private static String getCamraURL(int camera) {
        Assert.assertTrue(camera > 0, "Invalid camera, must be > 0: " + camera);
        return s_mpeg_url + "?camera=" + camera;
    }
    
    /**
     * Using basic logging.
     */
    @BeforeClass
    public void setupBeforeClass() {
        BasicConfigurator.configure();
    }
    
    /**
     * Starts the jetty web server.
     * Note that every tests that requires the jetty server to be running should be
     * in the group "jetty".
     */
    @BeforeGroups(groups = JETTY_TEST)
    public final void setupJettyServer() {
        Properties portProp = new Properties();
        try {
            portProp.load(ResourceUtil.loadResource(this.getClass(), PORT_PROPS));
        } catch (IOException e) {
            Assert.fail("Could not load port propeties file: " + PORT_PROPS, e);
        }
        String avaialblePort = portProp.getProperty("availablePort");
        if (avaialblePort != null) {
            try {
                int port = Integer.parseInt(avaialblePort);
                
                s_mpeg_url = "http://localhost:" + port + "/mjpg/video.cgi";        
                s_bad_url = "http://localhost:" + (port + 1) + "/mjpg/video.cgi";
                        
                server = new JettyAxisCameraServer(port, getCameraConfig());        
                Assert.assertTrue(server.startServer(), "Could not start jetty server");
                Reporter.log("Jetty Server running on port " + port);
            } catch (NumberFormatException ex) {
                Assert.fail("Error converting port: " + avaialblePort + " to int.", ex);
            }
        } else {
            Assert.fail("Could not get availablePort from properties.");
        }
    }
    
    /**
     * Stops the jetty web server.
     */
    @AfterGroups(groups = JETTY_TEST)
    public final void tearDownJettyServer() {
        Assert.assertTrue(server.stopServer(), "Could not stop jetty server.");
        Reporter.log("Jetty Server Stopped");
    }
    
    private Hashtable<Integer, List<ImageStreamData>> getCameraConfig() {
        Hashtable<Integer, List<ImageStreamData>> cameras = new Hashtable<Integer, List<ImageStreamData>>();
        
        BufferedImageLoader imageLoader = new BufferedImageLoader(this.getClass(), "Green.jpg");
        BufferedImage green = imageLoader.loadImage();
        Assert.assertNotNull(green, "Green image was null");       
        
        imageLoader.setPath("Red.jpg");
        BufferedImage red = imageLoader.loadImage();
        Assert.assertNotNull(red, "Red image was null");
        
        imageLoader.setPath("Blue.jpg");
        BufferedImage blue = imageLoader.loadImage();
        Assert.assertNotNull(blue, "Blue image was null");
        
        ImageStreamData redImageData = new ImageStreamData(red, 2000);
        ImageStreamData greenImageData = new ImageStreamData(green, 2000);
        ImageStreamData blueImageData = new ImageStreamData(blue, 2000);
        
        // Camera 1
        ArrayList<ImageStreamData> camera1List = new ArrayList<ImageStreamData>();                
        Assert.assertTrue(camera1List.add(redImageData), "Could not add data to list");
        
        cameras.put(CAM1, camera1List);
        
        // Camera 2
        ArrayList<ImageStreamData> camera2List = new ArrayList<ImageStreamData>();        
        Assert.assertTrue(camera2List.add(redImageData), "Could not add data to list");        
        Assert.assertTrue(camera2List.add(greenImageData), "Could not add data to list");
        
        cameras.put(CAM2, camera2List);
        
        // Camera 3
        ArrayList<ImageStreamData> camera3List = new ArrayList<ImageStreamData>();                
        Assert.assertTrue(camera3List.add(redImageData), "Could not add data to list");        
        Assert.assertTrue(camera3List.add(greenImageData), "Could not add data to list");
        Assert.assertTrue(camera3List.add(blueImageData), "Could not add data to list");
        
        cameras.put(CAM3, camera3List);
        
        return cameras;
    }
    
    /**
     * Tests constructor passing in valid string url.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = CONSTRUCTOR_TEST)
    public void testAxisServerStreamStringValid1() {
        String url = "http://google.com";
        try {
            new AxisServerStream(url);
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
    
    /**
     * Tests constructor passing in valid URL.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = CONSTRUCTOR_TEST)
    public void testAxisServerStreamURLValid1() {
        String url = "http://google.com";
        try {                       
            new AxisServerStream(new URL(url));
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
    
    /**
     * Tests constructor passing in invalid string url.
     * @throws MalformedURLException this is expected.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = CONSTRUCTOR_TEST, expectedExceptions = java.net.MalformedURLException.class)
    public void testAxisServerStreamStringInvalid() throws MalformedURLException {
        new AxisServerStream("This is not a URL");
    }
    
    /**
     * Tests constructor passing in null url string.
     * @throws MalformedURLException this is expected.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = CONSTRUCTOR_TEST, expectedExceptions = java.net.MalformedURLException.class)
    public void testAxisServerStreamStringNull() throws MalformedURLException {
        String nullStr = null;
        new AxisServerStream(nullStr);
    }
    
    /**
     * Tests constructor passing in null URL.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = CONSTRUCTOR_TEST, expectedExceptions = java.lang.IllegalArgumentException.class)
    public void testAxisServerStreamURLNull() {
        URL nullURL = null;
        new AxisServerStream(nullURL);
    }
    
    /**
     * Testing connect/isConnected/disconnect to a valid url.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = JETTY_TEST, dependsOnGroups = {CONSTRUCTOR_TEST })
    public void testConnectAndDisconnectValidURL() {
        String url = getCamraURL(CAM1);
        try {
            AxisServerStream stream = new AxisServerStream(url);
            boolean connectResult = stream.connect();
            boolean isConnected = stream.isConnected();
            
            if (connectResult) {
                stream.disconnect();
                Assert.assertFalse(stream.isConnected(), "isConnected should be false after disconnect()");
            }
            
            Assert.assertTrue(connectResult, "AxisServerStream could not make a connection to " + url);
            Assert.assertTrue(isConnected, "AxisServerStream made a succesful connection but isConnected returned false.");            
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
    
    /**
     * Testing connect/isConnected/disconnect to a invalid url.
     */
//    @Test(timeOut = GENERAL_TIMEOUT, groups = JETTY_TEST, dependsOnGroups = {CONSTRUCTOR_TEST })
//    public void testConnectAndDisconnectInValidURL() {
//        try {
//            AxisServerStream stream = new AxisServerStream(s_bad_url);
//            boolean connectResult = stream.connect();
//            boolean isConnected = stream.isConnected();
//            
//            if (connectResult) {
//                stream.disconnect();
//            }
//            
//            Assert.assertFalse(connectResult, "AxisServerStream made a connection to bad url: " + s_bad_url);
//            Assert.assertFalse(isConnected, "AxisServerStream did not make a succesful connection but isConnected returned true.");            
//        } catch (MalformedURLException e) {
//            Assert.fail("Bad url not expected for url " + s_bad_url, e);
//        }
//    }
    
    /**
     * Testing connect/isConnected/disconnect to a valid url.
     * Attempts to make connection 2 times.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = JETTY_TEST, dependsOnMethods = "testConnectAndDisconnectValidURL", dependsOnGroups = {CONSTRUCTOR_TEST })
    public void testConnectTwice() {
        String url = getCamraURL(CAM1);
        try {
            AxisServerStream stream = new AxisServerStream(url);
            boolean connectResult1 = stream.connect();
            boolean isConnected1 = stream.isConnected();
            
            boolean connectResult2 = false;            
            boolean isConnected2 = false;
            
            if (connectResult1) {
                connectResult2 = stream.connect();
                isConnected2 = stream.isConnected();
                
                stream.disconnect();
                Assert.assertFalse(stream.isConnected(), "isConnected should be false after disconnect()");
            }
            
            Assert.assertTrue(connectResult1, "AxisServerStream could not make a first connection to " + url);
            Assert.assertTrue(isConnected1, "AxisServerStream made a succesful first connection but isConnected returned false.");
            
            Assert.assertTrue(connectResult2, "AxisServerStream could not make a second connection to " + url);
            Assert.assertTrue(isConnected2, "AxisServerStream made a succesful first connection but isConnected returned false.");
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
    
    /**
     * Testing connect/isConnected/disconnect to a valid url.
     * Attempts to make connection 2 times.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = JETTY_TEST, dependsOnMethods = "testConnectAndDisconnectValidURL", dependsOnGroups = {CONSTRUCTOR_TEST })
    public void testDisconnectTwice() {
        String url = getCamraURL(CAM1);
        try {
            AxisServerStream stream = new AxisServerStream(url);
            boolean connectResult1 = stream.connect();
            boolean isConnected1 = stream.isConnected();            
            
            if (connectResult1) {
                stream.disconnect();
                Assert.assertFalse(stream.isConnected(), "isConnected should be false after first disconnect()");
                
                stream.disconnect();
                Assert.assertFalse(stream.isConnected(), "isConnected should be false after disconnect()");
            }
            
            Assert.assertTrue(connectResult1, "AxisServerStream could not make a connection to " + url);
            Assert.assertTrue(isConnected1, "AxisServerStream made a succesful connection but isConnected returned false.");            
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
    
    /**
     * Testing getNextImage().
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = JETTY_TEST, dependsOnMethods = "testConnectAndDisconnectValidURL", dependsOnGroups = {CONSTRUCTOR_TEST })
    public void testGetNextImageConnected() {
        // Using the longer stream.
        String url = getCamraURL(CAM3);
        try {
            AxisServerStream stream = new AxisServerStream(url);
            boolean connectResult1 = stream.connect();
            boolean isConnected1 = stream.isConnected();            
            
            if (connectResult1) {
                
                Assert.assertNotNull(stream.getNextImage(), "Buffered Image should not be null");
                Assert.assertNotNull(stream.getNextImage(), "Buffered Image should not be null");
                Assert.assertNotNull(stream.getNextImage(), "Buffered Image should not be null");
                
                stream.disconnect();
                Assert.assertFalse(stream.isConnected(), "isConnected should be false after disconnect()");
            }
            
            Assert.assertTrue(connectResult1, "AxisServerStream could not make a connection to " + url);
            Assert.assertTrue(isConnected1, "AxisServerStream made a succesful connection but isConnected returned false.");            
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
    
    /**
     * Testing getNextImage() while not connected.
     */
    @Test(timeOut = GENERAL_TIMEOUT, groups = JETTY_TEST, dependsOnGroups = {CONSTRUCTOR_TEST })
    public void testGetNextImageNotConnected() {
        // Using the longer stream.
        String url = getCamraURL(CAM1);
        try {
            AxisServerStream stream = new AxisServerStream(url);                        
            Assert.assertFalse(stream.isConnected(), "isConnected should not be connected");
            Assert.assertNull(stream.getNextImage(), "Buffered Image should be null because there is no connection.");                       
        } catch (MalformedURLException e) {
            Assert.fail("Bad url not expected for url " + url, e);
        }
    }
}

