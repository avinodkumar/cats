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

import static com.comcast.cats.RemoteCommand.DOWN;
import static com.comcast.cats.RemoteCommand.EXIT;
import static com.comcast.cats.RemoteCommand.TWO;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.info.RemoteCommandSequence;

/**
 * The Class IRServiceEndpointTest.
 * 
 * @Author : Deepa
 * @since : 10th Sept 2012 
 * Description : The Class IRServiceEndpointTest is the unit test of
 *        {@link IRServiceEndpoint}.
 *       
 */
public class IRServiceEndpointTest
{
    /* CATS development server */
    private static final String endPointStr       = "http://192.168.160.201:8080/ir-service/IRService?wsdl";
    private static final long   KEYPRESS_INTERVAL = 1000;
    private static URI          path              = null;
    private static String       keySet            = "XR2";
    private IRService           irService         = null;

    private static String       SETTOP_IR         = "gc100://192.168.160.201/?port=1";

    public IRServiceEndpointTest() throws URISyntaxException
    {
        path = new URI( SETTOP_IR );
    }

    @BeforeMethod
    public void instantiateEndpoint() throws MalformedURLException
    {
        IRServiceEndpoint endpoint = new IRServiceEndpoint( new URL( endPointStr ) );
        irService = endpoint.getIRServiceImplPort();
    }

    @Test
    public void sendSimpleCommands() throws InterruptedException
    {
        Assert.assertNotNull( irService );
        Assert.assertTrue( irService.pressKeyAndHold( path, keySet, EXIT, 30 ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( irService.pressKey( path, keySet, DOWN ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( irService.pressKey( path, keySet, DOWN ) );
        Thread.sleep( KEYPRESS_INTERVAL );
        Assert.assertTrue( irService.pressKey( path, keySet, TWO ) );
        Thread.sleep( KEYPRESS_INTERVAL );
    }

    @Test
    public void sendPressKeys() throws InterruptedException
    {
        Assert.assertNotNull( irService );
        ArrayList< RemoteCommand > commandList = new ArrayList< RemoteCommand >();
        commandList.add( EXIT );
        commandList.add( DOWN );
        commandList.add( DOWN );
        commandList.add( TWO );
        ArrayList< Integer > repeatList = new ArrayList< Integer >();
        repeatList.add( 30 );
        repeatList.add( 0 );
        repeatList.add( 0 );
        repeatList.add( 0 );
        ArrayList< Integer > delayList = new ArrayList< Integer >();
        delayList.add( 1000 );
        delayList.add( 1000 );
        delayList.add( 1000 );
        delayList.add( 1000 );
        boolean retval = irService.enterCustomKeySequence( path, keySet, commandList, repeatList, delayList );
        Assert.assertTrue( retval );
    }

    //TODO
    public void sendPressKeysRemoteCommandSequence() throws InterruptedException
    {
        Assert.assertNotNull( irService );
        ArrayList< RemoteCommandSequence > commandList = new ArrayList< RemoteCommandSequence >();
        commandList.add( new RemoteCommandSequence( EXIT, 30, 500 ) );
        commandList.add( new RemoteCommandSequence( DOWN, 0, 500 ) );
        commandList.add( new RemoteCommandSequence( DOWN, 0, 500 ) );
        commandList.add( new RemoteCommandSequence( DOWN, 0, 0 ) );
        boolean retval = irService.enterRemoteCommandSequence( path, keySet, commandList );
        Assert.assertTrue( retval );
    }
    @Test
    public void getVersion() throws InterruptedException
    {
        Assert.assertNotNull( irService );
        String version=irService.getVersion();
        Assert.assertNotNull(version);
    }
}
