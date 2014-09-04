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
package com.comcast.cats.remote.client;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.service.VideoRecorderService;

/**
 * Integration tests to verify concurrency using remote beans.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderServiceConcurrencyRemoteIT extends RemoteClientIT
{
    private static final String            IMPLEMENTATION_CLASS_SIMPLE_NAME = "VideoRecorderServiceImpl";
    private static final String            INTERFACE_NAME                   = VideoRecorderService.class.getName();

    protected VideoRecorderService         videoRecorderService;

    private static final List< Recording > recordingList                    = new LinkedList< Recording >();

    @Before
    public void setup() throws NamingException
    {
        super.setup();

        String jndiName = EJB_JNDI_PREFIX + MODULE_NAME + "//" + IMPLEMENTATION_CLASS_SIMPLE_NAME + "!"
                + INTERFACE_NAME;

        logger.info( "[JNDINAME][" + jndiName + "]" );

        videoRecorderService = ( VideoRecorderService ) context.lookup( jndiName );

        Assert.assertNotNull( videoRecorderService );

        recordingList.add( new Recording( "E4:48:C7:A8:2A:F2", "192.168.160.202", 2, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:1B:12", "192.168.160.203", 2, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:2A:12", "192.168.160.204", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:1A:56", "192.168.160.205", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:4B:C8", "192.168.160.206", 2, null ) );
        recordingList.add( new Recording( "54:D4:6F:96:D9:BC", "192.168.160.207", 1, null ) );
        recordingList.add( new Recording( "E4:48:C7:A8:2E:C4", "192.168.160.208", 1, null ) );
    }

    @Test
    public final void testConcurrentStartStop() throws InterruptedException
    {
        logger.info( "*************************[START ALL]*************************" );
        startAll();

        // Wait for sometime to actually start telnet interface.
        Thread.sleep( 20 * 1000 );

        logger.info( "*************************[STOP ALL]*************************" );
        stopAll();

        // Wait for sometime to retrieve the response. Hit terminate once all
        // responses are available
        Thread.sleep( 10 * 1000 );
    }

    private void stopAll() throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool( 16 );

        for ( final Recording recording : recordingList )
        {
            exec.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    runConcurrentStop( recording.getStbMacAddress() );
                }

            } );
        }

        exec.shutdown();
    }

    private void startAll() throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool( 16 );

        for ( final Recording recording : recordingList )
        {
            exec.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    runConcurrentStart( recording.getStbMacAddress(), recording.getVideoServerIp(),
                            recording.getVideoServerPort() );
                }

            } );
        }

        exec.shutdown();
    }

    private void runConcurrentStart( String macId, String videoServerIp, int port )
    {
        VideoRecorderResponse recorderResponse = videoRecorderService.start( macId, videoServerIp, port, 0, null );
        logger.info( "[START][" + macId + "][" + recorderResponse.getResult() + "][" + recorderResponse.getMessage()
                + "]" );
    }

    private void runConcurrentStop( String macId )
    {
        VideoRecorderResponse recorderResponse = videoRecorderService.stop( macId );
        logger.info( "[STOP-BY-MAc][" + macId + "][" + recorderResponse.getResult() + "]["
                + recorderResponse.getMessage() + "]" );
    }
}
