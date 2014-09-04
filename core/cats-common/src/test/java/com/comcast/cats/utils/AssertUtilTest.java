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
package com.comcast.cats.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases for {@link AssertUtil}
 * 
 * @author SSugun00c
 * 
 */
public class AssertUtilTest
{
    private static final String MAC                   = "00:22:10:21:A4:B0";
    private static final String INVALID_MAC_ID_FORMAT = "1234";
    private static final String EMPTY_STRING          = " ";

    @Test
    public void isValidMacId()
    {
        AssertUtil.isValidMacId( MAC );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isValidMacIdInvalid()
    {
        AssertUtil.isValidMacId( INVALID_MAC_ID_FORMAT );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isValidMacIdNull()
    {
        AssertUtil.isValidMacId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isValidMacIdEmpty()
    {
        AssertUtil.isValidMacId( EMPTY_STRING );
    }

    @Test
    public void isEmpty()
    {
        AssertUtil.isEmpty( "Foo" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isEmptyInvalid()
    {
        AssertUtil.isEmpty( EMPTY_STRING );
    }

    @Test
    public void isNullOrEmptyString()
    {
        AssertUtil.isNullOrEmpty( "Foo" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullOrEmptyStringNull()
    {
        String tempStr = null;
        AssertUtil.isNullOrEmpty( tempStr );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullOrEmptyStringEmpty()
    {
        AssertUtil.isNullOrEmpty( EMPTY_STRING );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullOrEmptyMapNull()
    {
        Map< String, String > map = null;
        AssertUtil.isNullOrEmptyMap( map );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullOrEmptyMapEmpty()
    {
        Map< String, String > map = new HashMap< String, String >();
        AssertUtil.isNullOrEmptyMap( map );
    }

    @Test
    public void isNullOrEmptyMap()
    {
        Map< String, String > map = new HashMap< String, String >();
        map.put( "Foo", "Foo" );
        try
        {
            AssertUtil.isNullOrEmptyMap( map );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullNull()
    {
        AssertUtil.isNull( null );
    }

    @Test
    public void isNull()
    {
        AssertUtil.isNull( new String( "Hello" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullObjectNullOneObject()
    {
        String str = null;
        AssertUtil.isNullObject( str );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullObjectNullTwoObject()
    {
        AssertUtil.isNullObject( new String( "Hello" ), null );
    }

    @Test
    public void isNullObject()
    {
        AssertUtil.isNullObject( new String( "Hello" ), new String( "Hi" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullOrEmptyListNull()
    {
        List< String > list = null;
        AssertUtil.isNullOrEmptyList( list );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isNullOrEmptyListEmpty()
    {
        List< String > list = new ArrayList< String >();
        AssertUtil.isNullOrEmptyList( list );
    }

    @Test
    public void isNullOrEmptyList()
    {
        List< String > list = new ArrayList< String >();
        list.add( "Foo" );
        try
        {
            AssertUtil.isNullOrEmptyList( list );
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void isOneOrMore()
    {
        Assert.assertTrue( AssertUtil.isOneOrMore( 10 ) );
        Assert.assertFalse( AssertUtil.isOneOrMore( 0 ) );
    }
}
