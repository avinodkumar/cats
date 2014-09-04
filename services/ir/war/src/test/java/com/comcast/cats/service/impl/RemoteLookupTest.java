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
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cats.keymanager.domain.Key;
import com.comcast.cats.keymanager.domain.Remote;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : Deepa
 * @since : 10th Sept 2012
 * @Description : The Class RemoteLookupTest is the unit test of
 *              {@link RemoteLookup}.
 */
public class RemoteLookupTest
{
    private RemoteLookup lookup;

    @BeforeMethod
    public void before()
    {
        lookup = new RemoteLookup();
    }

    private List< Remote > getRemoteSetInitial()
    {
        List< Remote > remotes = new ArrayList< Remote >();
        Remote r1 = new Remote( "COMCAST" );
        r1.getKeys().add( new Key( "GUIDE", "GUIDE_VALUE_1", "PRONTO" ) );
        r1.getKeys().add( new Key( "MENU", "MENU_VALUE", "PRONTO" ) );
        remotes.add( r1 );
        return remotes;
    }

    private List< Remote > getRemoteSetSecondary()
    {
        List< Remote > remotes = new ArrayList< Remote >();
        Remote r1 = new Remote( "COMCAST" );
        r1.getKeys().add( new Key( "GUIDE", "GUIDE_VALUE_2", "PRONTO" ) );
        remotes.add( r1 );
        return remotes;
    }

    @Test
    public void negativeFindIrCode()
    {
        String value = lookup.findIrCode( "COMCAST", "GUIDE" );
        AssertJUnit.assertNull( value );
        // assertNull( value );
    }

    @Test
    public void testProcessRemotes()
    {
        lookup.processRemotes( getRemoteSetInitial() );
        AssertJUnit.assertEquals( lookup.findIrCode( "COMCAST", "GUIDE" ), "GUIDE_VALUE_1" );
        AssertJUnit.assertEquals( lookup.findIrCode( "COMCAST", "MENU" ), "MENU_VALUE" );
        /*
         * Now let's process a new set of IR codes and verify that update
         * occurs.
         */
        lookup.processRemotes( getRemoteSetSecondary() );
        AssertJUnit.assertEquals( lookup.findIrCode( "COMCAST", "GUIDE" ), "GUIDE_VALUE_2" );
        /* Menu value shouldn't be overwritten. */
        AssertJUnit.assertEquals( lookup.findIrCode( "COMCAST", "MENU" ), "MENU_VALUE" );
    }

    @Test
    public void testClearRemotes()
    {
        lookup.processRemotes( getRemoteSetInitial() );
        AssertJUnit.assertEquals( "GUIDE_VALUE_1", lookup.findIrCode( "COMCAST", "GUIDE" ) );
        AssertJUnit.assertEquals( "MENU_VALUE", lookup.findIrCode( "COMCAST", "MENU" ) );
        lookup.clear();
        /*
         * Now let's process a new set of IR codes and verify that update
         * occurs.
         */
        lookup.processRemotes( getRemoteSetSecondary() );
        AssertJUnit.assertEquals( lookup.findIrCode( "COMCAST", "GUIDE" ), "GUIDE_VALUE_2" );
        /* Menu value shouldn't be overwritten. */
        AssertJUnit.assertEquals( null, lookup.findIrCode( "COMCAST", "MENU" ) );
    }

    @Test( expectedExceptions = IllegalArgumentException.class )
    public void negtiveProcessRemotesNull()
    {
        lookup.processRemotes( null );
    }

    @Test
    public void testGetRemotes()
    {
        lookup.processRemotes( getRemoteSetInitial() );
        AssertJUnit.assertNotNull( lookup.getRemotes() );
    }
}
