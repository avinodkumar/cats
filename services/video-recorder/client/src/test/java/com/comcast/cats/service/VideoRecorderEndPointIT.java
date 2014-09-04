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
package com.comcast.cats.service;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;

/**
 * Integration tests for {@link VideoRecorderEndPoint}.
 * 
 * @author ssugun00c
 * 
 */
public class VideoRecorderEndPointIT
{
    private static final long     SLEPP_DURATION = 30 * 1000;
    private static final String   BASE_URL       = "http://localhost:8080/";
    private static final Integer  PORT           = 1;
    private static final String   VIDEOSERVER_IP = "192.168.160.202";
    private static final Integer  DURATION       = 1000;
    private static final String   MAC_ID         = "00:19:47:25:AD:7E";

    private Logger                logger         = LoggerFactory.getLogger( getClass() );

    private VideoRecorderEndPoint videoRecorderEndPoint;
    private VideoRecorderService  videoRecorderService;

    public VideoRecorderEndPointIT() throws MalformedURLException
    {
        String spec = BASE_URL + VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_WSDL_LOCATION;
        URL wsdlDocumentLocation = new URL( spec );

        videoRecorderEndPoint = new VideoRecorderEndPoint( wsdlDocumentLocation );
        Assert.assertNotNull( videoRecorderEndPoint );

        videoRecorderService = videoRecorderEndPoint.getVideoRecorderServiceImplPort();
        Assert.assertNotNull( videoRecorderService );
    }

    @Test
    public void testEndPoint() throws Exception
    {
        printStatus();

        start();
        Thread.sleep( SLEPP_DURATION );

        printStatus();

        stop();
        Thread.sleep( SLEPP_DURATION );

        printStatus();
    }

    private void start()
    {
        logger.info( "START" );

        WebServiceReturn webServiceReturn = videoRecorderService.start( MAC_ID, VIDEOSERVER_IP, PORT, DURATION, null );
        logger.info( "[RESULT][" + webServiceReturn.getResult() + "][MESSAGE][" + webServiceReturn.getMessage() + "]" );
    }

    private void stop()
    {
        logger.info( "STOP" );

        WebServiceReturn webServiceReturn = videoRecorderService.stop( MAC_ID );
        logger.info( "[RESULT][" + webServiceReturn.getResult() + "][MESSAGE][" + webServiceReturn.getMessage() + "]" );
    }

    private void printStatus()
    {
        logger.info( "STATUS" );

        WebServiceReturn webServiceReturn = videoRecorderService.getStatus( MAC_ID );
        logger.info( "[RESULT][" + webServiceReturn.getResult() + "][MESSAGE][" + webServiceReturn.getMessage() + "]" );
    }

}
