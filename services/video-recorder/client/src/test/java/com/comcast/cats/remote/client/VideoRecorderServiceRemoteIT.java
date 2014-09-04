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

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.service.VideoRecorderService;

/**
 * Integration tests to verify {@link VideoRecorderService} using remote beans.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderServiceRemoteIT extends RemoteClientIT
{
    private static final String    IMPLEMENTATION_CLASS_SIMPLE_NAME = "VideoRecorderServiceImpl";
    private static final String    INTERFACE_NAME                   = VideoRecorderService.class.getName();

    protected VideoRecorderService videoRecorderService;

    @Before
    public void setup() throws NamingException
    {
        super.setup();

        String jndiName = EJB_JNDI_PREFIX + MODULE_NAME + "//" + IMPLEMENTATION_CLASS_SIMPLE_NAME + "!"
                + INTERFACE_NAME;

        logger.info( "[JNDINAME][" + jndiName + "]" );

        videoRecorderService = ( VideoRecorderService ) context.lookup( jndiName );

        Assert.assertNotNull( videoRecorderService );
    }

    @Test
    public void testStatusByMac()
    {
        String macId = "00:19:47:25:AD:7A";
        VideoRecorderResponse recorderResponse = videoRecorderService.getRecordingHistoryByMac( macId );
        logger.info( recorderResponse + "" );
    }
}
