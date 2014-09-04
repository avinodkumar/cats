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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import com.comcast.cats.keymanager.domain.Key;
import com.comcast.cats.keymanager.domain.Remote;
import com.comcast.cats.service.KeyManager;
import com.comcast.cats.service.impl.KeyManagerImpl;
import com.comcast.cats.service.KeyManagerProxy;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * The Class KeyManagerImplTest.
 * 
 * @Author : cfrede001
 * @since :
 * @Description : The Class KeyManagerImplTest is the unit test of
 *              {@link KeyManagerImpl}.
 */
public class KeyManagerImplTest
{
    public static String        DUMMY_HOST_IP = "10.20.30.40";
    public static String        TEST_IR_CODE  = "0000 0012 0000 ....";
    RemoteLookup                remoteLookup;
    RemoteLayoutLookup          remoteLaoyoutLookup;
    Provider< KeyManagerProxy > keyProxyProvider;
    KeyManagerProxy             keyProxy;
    List< Remote >              remotes;

    /**
     * Use our default keyProxy class instance.
     */
    protected void setup()
    {
        setup( keyProxy );
    }

    /**
     * Using the provided proxy, setup some basic mocks for use by tests.
     * 
     * @param proxy
     */
    protected void setup( KeyManagerProxy proxy )
    {
        expect( keyProxyProvider.get() ).andReturn( proxy ).anyTimes();
        remotes = new ArrayList< Remote >();
        Set< Key > keys = new HashSet< Key >();
        keys.add( new Key( "GUIDE", TEST_IR_CODE, "FORMAT" ) );
        remotes.add( new Remote( "COMCAST", keys ) );
        remotes.add( new Remote( "MOTOROLA" ) );
        expect( keyProxy.remotes() ).andReturn( remotes ).anyTimes();
    }

    protected KeyManager getKeyManager()
    {
        return getKeyManager( false );
    }

    /**
     * Helper to make setting up mock KeyManager for the test easier.
     * 
     * @return
     */
    protected KeyManager getKeyManager( boolean nullProxy )
    {
        if ( nullProxy )
        {
            setup( null );
        }
        else
        {
            setup();
        }
        replayAll();
        KeyManager keyManager = new KeyManagerImpl( remoteLookup, keyProxyProvider,remoteLaoyoutLookup );
        return keyManager;
    }

    @Before
    public void before()
    {
        remoteLookup = new RemoteLookup();
        remoteLaoyoutLookup=new RemoteLayoutLookup();
        keyProxyProvider = createMock( KeyManagerProxyProvider.class );
        keyProxy = createMock( KeyManagerProxy.class );
    }

    @Test
    public void checkNullKeyManagerProxy()
    {
        KeyManager keyManager = getKeyManager( true );
        assertEquals( keyManager.getLastRefreshed(), null );
        /*
         * If we call refresh with null provider, no exception should be thrown,
         * but the date should be null afterwards as well.
         */
        keyManager.refresh();
        assertEquals( keyManager.getLastRefreshed(), null );
        assertEquals( keyManager.getRemotes(), null );
    }

    @Test
    public void checkRemotesRefresh()
    {
        KeyManager keyManager = getKeyManager();
        keyManager.refresh();
        assertEquals( remotes, keyManager.getRemotes() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findNullRemote()
    {
        KeyManager keyManager = getKeyManager();
        keyManager.getIrCode( null, null, "key" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findNullKey()
    {
        KeyManager keyManager = getKeyManager();
        keyManager.getIrCode( "remote", null, null );
    }

    @Test
    public void findKey()
    {
        KeyManager keyManager = getKeyManager();
        String code = keyManager.getIrCode( "COMCAST", null, "GUIDE" );
        assertEquals( code, TEST_IR_CODE );
    }

    @Test
    public void findKeyInvalid()
    {
        KeyManager keyManager = getKeyManager();
        /* for the negative test case. */
        String code = keyManager.getIrCode( "SA", null, "LANGUAGE" );
        assertNull( code );
    }
}
