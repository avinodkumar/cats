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
package com.comcast.cats.service.impl;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.createPartialMock;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import javax.naming.Context;
import javax.naming.NamingException;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.KeyManagerProxy;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : Deepa
 * @since : 20th Sept 2012
 * @Description : The Class KeyManagerProxyProviderTest is the unit test of
 *              {@link KeyManagerProxyProvider}.
 */
//@RunWith( PowerMockRunner.class )
@PrepareForTest(
    { KeyManagerProxyProvider.class } )
public class KeyManagerProxyProviderTest
{

    @Test
    public void testContextNull() throws NamingException
    {
        /**
         * Create a partial mock to test the getContext returning null.
         */
        
        KeyManagerProxyProvider mck  = new KeyManagerProxyProvider();
        KeyManagerProxy proxy = mck.get();
        AssertJUnit.assertNull( proxy);
        
    }


    @Test( enabled = false ) //TODO
    public void validContext() throws NamingException
    {
        KeyManagerProxy mockProxy = createMock( KeyManagerProxy.class );
        Context mockContext = createMock( Context.class );
        /**
         * Create a partial mock to test the getContext returning null.
         */
        KeyManagerProxyProvider mockProvider = createPartialMock( KeyManagerProxyProvider.class, "getContext" );
        expect( mockProvider.getContext() ).andReturn( mockContext );
        expect( mockContext.lookup( IRServiceConstants.KEY_MANAGER_PROXY_NAME ) ).andReturn( mockProxy );
        /* Close the context. */
        mockContext.close();
        expectLastCall();
        replayAll();
        KeyManagerProxy proxy = mockProvider.get();
        Assert.assertEquals( proxy, mockProxy );
        verifyAll();
    }
}
