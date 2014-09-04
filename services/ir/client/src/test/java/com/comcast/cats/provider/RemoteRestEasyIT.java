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
package com.comcast.cats.provider;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.info.RemoteCommandSequence;

public class RemoteRestEasyIT
{
    private RemoteRest            remRest;
    private RemoteCommand[]       commandArray =
                                                   { RemoteCommand.parse( "GUIDE" ), RemoteCommand.parse( 1 ) };
    private List< RemoteCommand > commandList  = new ArrayList< RemoteCommand >()
                                               {
                                                   private static final long serialVersionUID = 1L;

                                                   {
                                                       add( RemoteCommand.parse( "GUIDE" ) );
                                                       add( RemoteCommand.parse( 1 ) );
                                                   }
                                               };

    @BeforeTest
    public void setup() throws URISyntaxException
    {
        String irServiceURL = "http://cats-dev/ir-service/rest/";
        remRest = new RemoteRest( irServiceURL, "192.168.160.201", 4, "gc100", "COMCAST" );
        Assert.assertNotNull( remRest );
        Assert.assertEquals( "gc100://192.168.160.201/?port=4", remRest.getRemoteLocator().toString(),
                "URI not matching" );

    }

    @Test( expectedExceptions = UnsupportedOperationException.class )
    public void proxyGetValidKeys()
    {
        remRest.getValidKeys();
    }

    @Test
    public void proxyPressKeyCmd()
    {
        Assert.assertTrue( remRest.pressKey( RemoteCommand.parse( "GUIDE" ) ), "Press Key for GUIDE failed" );
    }

    @Test
    public void proxyPressKeyInt()
    {
        Assert.assertTrue( remRest.pressKey( new Integer( 1 ) ), "Press Key for 1 failed" );
    }

    @Test
    public void proxyPressKeyCmdWithDelay()
    {
        Assert.assertTrue( remRest.pressKey( RemoteCommand.parse( "GUIDE" ), new Integer( 5 ) ),
                "Press Key for GUIDE with delay 5 failed" );
    }

    @Test
    public void proxyPressKeyCmdWithCount()
    {
        Assert.assertTrue( remRest.pressKey( new Integer( 5 ), RemoteCommand.parse( "GUIDE" ) ),
                "Press Key for GUIDE with count 5 failed" );
    }

    @Test
    public void proxyPressKeyCmdWithCountAndDelay()
    {
        Assert.assertTrue( remRest.pressKey( new Integer( 5 ), RemoteCommand.parse( "GUIDE" ), new Integer( 5 ) ),
                "Press Key for GUIDE with count 5 and delay 5 failed" );
    }

    @Test
    public void proxyPressKeyCmdArray()
    {
        Assert.assertTrue( remRest.pressKey( commandArray ), "Press Key for command array {GUIDE, 1} failed" );
    }

    @Test
    public void proxyPressKeyCmdArrayWithDelayAndCount()
    {
        Assert.assertTrue( remRest.pressKey( new Integer( 5 ), new Integer( 5 ), commandArray ),
                "Press Key for command array {GUIDE, 1} with count 5 and delay 5 failed" );
    }

    @Test
    public void proxyPressKeyAndHold()
    {
        Assert.assertTrue( remRest.pressKeyAndHold( RemoteCommand.parse( "GUIDE" ), new Integer( 2 ) ),
                "Press Key GUIDE with hold count 2 failed" );
    }

    @Test
    public void proxyPressKeys()
    {
        Assert.assertTrue( remRest.pressKeys( commandList ), "Press Key for command list {GUIDE,1} failed" );
    }

    @Test
    public void proxyPressKeysWithDelay()
    {
        Assert.assertTrue( remRest.pressKeys( commandList, new Integer( 5 ) ),
                "Press Key for command list {GUIDE,1} with delay 5 failed" );
    }

    @Test
    public void proxySendText()
    {
        Assert.assertTrue( remRest.sendText( "12345678" ), "Sending text '12345678' failed" );
    }

    @Test
    public void proxyTuneStrChannel()
    {
        Assert.assertTrue( remRest.tune( "1000" ), "Tuning String channel 1000 failed" );
    }

    @Test
    public void proxyTuneIntChannel()
    {
        Assert.assertTrue( remRest.tune( 1000 ), "Tuning Integer channel 1000 failed" );
    }

    @Test
    public void proxySendIR()
    {
        Assert.assertTrue( remRest
                .sendIR( "0000 006D 0000 0012 0157 00AB 0013 0056 0013 0056 0013 0056 0013 0056 0013 00AB 0013 00AB 0013 0056 0013 0056 0013 0056 0013 0056 0013 0056 0013 0056 0013 00AB 0013 0056 0013 00AB 0013 00AB 0013 02FB" ) );
    }

    @Test
    public void proxyEnterCustomKeySequence()
    {
        List< Integer > countList = Arrays.asList( 2, 3 );
        List< Integer > delayList = Arrays.asList( 20, 30 );
        Assert.assertTrue( remRest.enterCustomKeySequence( commandList, countList, delayList ) );
    }

    @Test
    public void proxyEnterRemoteCommandSequence()
    {
        List< RemoteCommandSequence > remCmdSeqList = new ArrayList< RemoteCommandSequence >();
        remCmdSeqList.add( new RemoteCommandSequence( RemoteCommand.parse( "GUIDE" ), 2, 20 ) );
        remCmdSeqList.add( new RemoteCommandSequence( RemoteCommand.parse( "MENU" ), 3, 10 ) );
        remCmdSeqList.add( new RemoteCommandSequence( RemoteCommand.parse( "EXIT" ), 1, 30 ) );
        Assert.assertTrue( remRest.enterRemoteCommandSequence( remCmdSeqList ) );
    }
}