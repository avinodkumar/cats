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

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.net.URI;
import java.net.URISyntaxException;

import org.testng.annotations.BeforeMethod;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.service.IRService;
import static org.easymock.EasyMock.*;

/**
 * The Class RemoteProviderServiceImplDelayTest.
 * 
 * @Author : Deepa
 * @since : 20th Sept 2012 
 * Description : The Class RemoteProviderServiceImplDelayTest is the unit test of
 *        {@link RemoteProviderServiceImpl}.
 */
public class RemoteProviderServiceImplDelayTest
{

    private IRService irServiceMock;

    URI               path;
    String            irKeySet      = "COMCAST";
    RemoteCommand     command       = RemoteCommand.GUIDE;
    RemoteProvider    remote;

    /**
     * Delay in milliseconds that implies no real delay has been included.
     */
    Integer           NO_DELAY      = 50;
    Integer           DEFAULT_DELAY = 100;

    public RemoteProviderServiceImplDelayTest() throws URISyntaxException
    {
        path = new URI( "gc100://192.168.160.201:4998/?port=1" );
    }

    @BeforeMethod
    public void before()
    {
        irServiceMock = createMock( IRService.class );
        remote = new RemoteProviderServiceImpl( irServiceMock, path, irKeySet );
    }

    public void assertDelay( long start, boolean condition )
    {
        long delay = System.currentTimeMillis() - start;
        boolean expr = delay > NO_DELAY;
        AssertJUnit.assertEquals( condition, expr );
    }

    @Test( expectedExceptions = IllegalArgumentException.class )
    public void testInvalidDelayNegative()
    {
        remote.setDelay( -1 );
    }

    @Test( expectedExceptions = IllegalArgumentException.class )
    public void testInvalidDelayPositive()
    {
        remote.setDelay( RemoteProvider.MAX_DELAY + 1 );
    }

    @Test
    public void testInvalidDelayReset()
    {
        remote.setDelay( DEFAULT_DELAY );
        boolean exception = false;
        try
        {
            remote.setDelay( RemoteProvider.MAX_DELAY + 1 );
        }
        catch ( IllegalArgumentException iae )
        {
            exception = true;
        }
        AssertJUnit.assertTrue( exception );
        assert ( remote.getDelay() == DEFAULT_DELAY );
    }

    /**
     * The default RemoteProvider implementation should use a default delay.
     */
    @Test
    public void verifyNoDelayPressKey()
    {
        // Set expectations on mocks.
        expect( irServiceMock.pressKey( path, irKeySet, command ) ).andReturn( true );
        replay( irServiceMock );
        long start = System.currentTimeMillis();
        remote.pressKey( command );
        assertDelay( start, false );
    }

    /**
     * Test delay occurs when setting default delay.
     */
    @Test
    public void verifyDefaultDelayPressKey()
    {
        // Set expectations on mocks.
        expect( irServiceMock.pressKey( path, irKeySet, command ) ).andReturn( true );
        replay( irServiceMock );
        remote.setDelay( DEFAULT_DELAY );
        long start = System.currentTimeMillis();
        remote.pressKey( command );
        assertDelay( start, true );
    }

    /**
     * Verify specifying delay in overloaded method is observed.
     */
    @Test
    public void verifyDelayPressKeyOverloaded()
    {
        // Set expectations on mocks.
        expect( irServiceMock.pressKey( path, irKeySet, command ) ).andReturn( true );
        replay( irServiceMock );
        remote.setDelay( DEFAULT_DELAY );
        long start = System.currentTimeMillis();
        remote.pressKey( command, 0 );
        assertDelay( start, false );
    }

    /**
     * Mock a failure on IRService and verify no delay is used.
     */
    @Test
    public void verifyNoDelayPressKeyAndHold()
    {
        expect( irServiceMock.pressKeyAndHold( path, irKeySet, command, 10 ) ).andReturn( false );
        replay( irServiceMock );
        remote.setDelay( DEFAULT_DELAY );
        long start = System.currentTimeMillis();
        remote.pressKeyAndHold( command, 10 );
        assertDelay( start, false );
    }
}
