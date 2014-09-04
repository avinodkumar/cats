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

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Wrapper class for starting and stopping Jetty Web Server.
 * This class is to be used for CoreImageCompareTest.
 */
public class JettyAxisCameraServer {
    private Server server;
    private int port;
    Hashtable<Integer, List<ImageStreamData>> cameras;
    
    private final Logger log = LoggerFactory.getLogger(JettyAxisCameraServer.class);
    
    private static final String MPEG = "/mjpg";

    /**
     * Constructor. Sets the port and camera list.
     * @param port The port to set.
     * @param cameras the camera list.
     */
    public JettyAxisCameraServer(int port, Hashtable<Integer, List<ImageStreamData>> cameras) {
        if (null == cameras || cameras.isEmpty()) {
            throw new IllegalArgumentException("cameras cannot be null or empty.");
        }
        
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("port range must be between 0 - 65535");            
        }
        
        Iterator<Map.Entry<Integer, List<ImageStreamData>>> iter = cameras.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<Integer, List<ImageStreamData>> entry = iter.next();
            List<ImageStreamData> images = entry.getValue();
            if (null == images || images.isEmpty()) {
                throw new IllegalArgumentException("ImageStreamData List cannot be null or empty.");
            }
            
            for (ImageStreamData data : images) {
                if (null == data) {
                    throw new IllegalArgumentException("ImageStreamData List cannot contain null values.");
                }
            }            
        }
        this.cameras = cameras;
        this.port = port;
    }

    /**
     * Starts the jetty web server.
     * @return true on successful start.
     */
    public boolean startServer() {
        if (server != null && server.isRunning()) {
            return true;
        } else {
            server = new Server(port);
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            server.setHandler(contexts);

            Context mjpg = new Context(contexts, MPEG, Context.SESSIONS);
            mjpg.addServlet(new ServletHolder(new AxisServlet()), "/*");
            
            try {
                server.start();
                return true;
            } catch (Exception e1) {
                log.error("Could not start server", e1);
            }
        }
        return false;
    }

    /**
     * Stops the jetty web server.
     * @return true on successful stop.
     */
    public boolean stopServer() {
        if (server == null || !server.isRunning()) {
            return false;
        }
        try {
            server.stop();
            return true;
        } catch (Exception e) {
            log.error("Could not stop server", e);
        }
        return false;
    }

    /**
     * Class used to parse out common URL parameters between Axis Server Servlets.
     */
    private class AxisServlet extends HttpServlet {
        private static final long serialVersionUID = 6865895657001503625L;

        protected int camera;
        protected int fps;
        
        public AxisServlet() {
            fps = 4;
        }
        
        /**
         * Gets the camera and fps url parameters.
         * Camera parameters must exist otherwise this function returns false.
         * Response status will be set to HttpServletResponse.SC_NOT_FOUND if camera param is not found.
         * Fps is set to 4 if not specified.
         * 
         * @param request The request.
         * @param response The response.
         * @return True on success.
         */
        private boolean configureParams(HttpServletRequest request, HttpServletResponse response) {            
            assert null != request : "request cannot be null";
            assert null != response : "response cannot be null";
            
            // Get the "camera". If its not specified then send back back request code.
            String cameraStr = request.getParameter("camera");
            if (cameraStr == null) {
                log.warn("No camera paramater found in url request.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                ((Request) request).setHandled(true);
                return false;
            } else {
                try {
                    camera = Integer.valueOf(cameraStr);
                } catch (NumberFormatException nfe) {
                    log.error("Invalid camera paramater found in url request. Integer value required.", nfe);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ((Request) request).setHandled(true);
                    return false;
                }
            }

            String fpsStr = request.getParameter("fps");
            if (null != fpsStr) {
                try {
                    fps = Integer.valueOf(fpsStr);
                } catch (NumberFormatException nfe) {
                    log.error("Invalid fps paramater found in url request. Using default value " + fps, nfe);
                }
            }
            
            return true;
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            assert null != request : "request cannot be null";
            assert null != response : "response cannot be null";
            
            if (configureParams(request, response)) {
                // Stream the appropriate image.                
                stream(response, cameras.get(camera));                
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);                
            }
            ((Request) request).setHandled(true);
        }

        private void stream(HttpServletResponse response, List<ImageStreamData> images) throws IOException, ServletException {
            assert null != response : "response cannot be null";
            assert null != images : "images cannot be null";
            
            response.setContentType("multipart/x-mixed-replace; boundary=--myboundary");
            response.setStatus(HttpServletResponse.SC_OK);

            ServletOutputStream stream = null;
            try {
                stream = response.getOutputStream();

                int fpsDelay = 1000 / fps;
                Iterator<ImageStreamData> iter = images.iterator();

                while (iter.hasNext()) {
                    ImageStreamData imageData = iter.next();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    // Need to get the bytes from image so we know what the size of it is.
                    ImageIO.write(imageData.getImage(), "JPG", bos);
                    byte[] imageInBytes = bos.toByteArray();

                    String content_info = "--myboundary\nContent-Type: image/jpeg\nContent-Length: " + imageInBytes.length + "\n";
                    
                    long currentStreamTime = 0L;
                    // Stream the image for the specified amount of time.
                    while (currentStreamTime < imageData.getTimeout()) {
                        long start = System.currentTimeMillis();
                        // Send this as defined by Axis Camera HTTP API.                        
                        stream.write(content_info.getBytes());
                        stream.write("\n".getBytes());

                        // Now send the image.
                        stream.write(imageInBytes);
                        stream.flush();
                        try {
                            // Only sleep if we need to.
                            long sleep = fpsDelay - (System.currentTimeMillis() - start);
                            if (sleep > 0) {
                                Thread.sleep(sleep);
                            }
                        } catch (InterruptedException e) {
                            e = null;
                        }
                        currentStreamTime += System.currentTimeMillis() - start;
                    }
                }
            } catch (EOFException eof) {
                // Don't log this as error, it will happen every time a client disconnects.
                log.trace("EOFException. This may just be because the client has disconnected", eof);
            } catch (IOException ioe) {
                log.warn("IOException. This may just be because the client has disconnected", ioe);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }
    }
}
