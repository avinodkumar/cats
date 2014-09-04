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
package com.comcast.cats.keymanager.domain;

import java.util.HashSet;
import java.util.Set;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class RemoteTest.
 * 
 * @Author : Deepa
 * @since : 31st Oct 2012 
 * @Description : The Class RemoteTest is the
 *        unit test of {@link Remote}.
 */
public class RemoteTest
{
    /* Test object. */
    Remote remote;
    
    /**
     * Initialise the object.
     * @throws Exception
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        remote = new Remote();
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        remote = null;
    }

    @Test
    public void testRemote()
    {
        String name = "dummy";
        remote =  new Remote( name );
        Assert.assertEquals( name, remote.getName() );
        
        Key key = new Key();
        Set<Key> keys = new HashSet<Key>(0);
        keys.add( key );
       // Whitebox.setInternalState( remote, keys, keys );
        remote =  new Remote( name, keys );
        Assert.assertEquals( name, remote.getName() );
        Assert.assertEquals( keys, remote.getKeys() );
    }
    
    @Test
    public void testGetAndSetBeanValues(){
        
        String name = "dummy name";
        Key key = new Key();
        Set<Key> keys = new HashSet<Key>(0);
        keys.add( key );
        
        remote.setName( name );
        Assert.assertEquals( remote.getName(), name );
        remote.setKeys( keys );
        Assert.assertEquals( remote.getKeys(), keys );
        
    }

}
