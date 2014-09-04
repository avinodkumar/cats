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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.provider.exceptions.ExclusiveAccessException;

public class RemoteProviderExclusiveAccessTest
{
    protected final Logger LOGGER = LoggerFactory.getLogger( getClass() );
    SettopDummy settop;
    RemoteProviderDummy provider;
    RemoteProvider settopRemoteProvider;
    @Before
    public void initDummySettop() throws URISyntaxException{
    	 settop = new SettopDummy();
         provider = new RemoteProviderDummy();
         provider.setParent( settop );
         settop.setRemote( provider );
         settopRemoteProvider = ( RemoteProvider ) settop;
    }
    @Test
    public void runExclusiveAccess() throws URISyntaxException, InterruptedException
    {
    	 settop.setLocked( true );

        LOGGER.info( "Testing provider" );
        provider.pressKey( null, 0 );
        provider.tune( "invalid" );

        LOGGER.info( "Testing provider interface" );
        RemoteProvider providerInterface = ( RemoteProvider ) provider;
        providerInterface.pressKey( null, 0 );
        providerInterface.tune( "invalid" );

        LOGGER.info( "Testing casted provider from settop" );
        settopRemoteProvider.pressKey( null, 0 );
        settopRemoteProvider.tune( "invalid" );

        LOGGER.info( "Testing settop" );
        settop.pressKey( null, 0 );
        settop.tune( "invalid" );        

    }
    
    @Test( expected = ExclusiveAccessException.class )
    public void runExclusiveAccessFailure() throws URISyntaxException, InterruptedException
    {

       
        settop.setLocked( false );
        LOGGER.info( "Testing provider" );
        settopRemoteProvider.pressKey(RemoteCommand.CHUP, 0 );
    
     }

    @Test( expected = ExclusiveAccessException.class )
    public void runExclusiveAccessFailureOnSettop() throws URISyntaxException, InterruptedException
    {
        settop.setLocked( false );
        LOGGER.info( "Testing provider" );
        settop.pressKey( RemoteCommand.CHUP );

     }
}
