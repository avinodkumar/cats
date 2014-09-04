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
package com.comcast.cats.monitor.reboot;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;

public class TraceRebootMonitorTest
{
    private static final int   INITIAL_REBOOT_COUNT = 0;
    private Settop             settop;
    private RebootReporter     rebootReporter;
    private TraceRebootMonitor traceRebootMonitor;
    public final String        TEST_MAC_ID          = "E4:48:C7:A7:B9:90";

    @BeforeMethod
    public void setUp()
    {
        settop = getSettop();
        traceRebootMonitor = new TraceRebootMonitor();
        traceRebootMonitor.setSettop( settop );
        traceRebootMonitor.setRebootReporter( rebootReporter );
        traceRebootMonitor.setState( new TraceRebootMonitorState() );
    }

    @AfterMethod
    public void tearDown()
    {
        settop = null;
    }

    @Test
    public void testConstructor()
    {
        Assert.assertEquals( INITIAL_REBOOT_COUNT,
                ( ( TraceRebootMonitorState ) traceRebootMonitor.getState() ).getHits() );
        Assert.assertEquals( rebootReporter, traceRebootMonitor.getRebootReporter() );
        Assert.assertEquals( settop, traceRebootMonitor.getSettop() );
    }

    private Settop getSettop()
    {
        SettopImpl settop = new SettopImpl();
        SettopDesc settopInfo = new SettopDesc();
        String hostMacAddress = TEST_MAC_ID;
        settopInfo.setHostMacAddress( hostMacAddress );
        settop.setSettopInfo( settopInfo );

        return settop;
    }

}
