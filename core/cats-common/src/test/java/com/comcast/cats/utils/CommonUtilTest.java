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

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases for {@link CommonUtil}
 * 
 * @author SSugun00c
 * 
 */
public class CommonUtilTest
{
    private static final String MAC                   = "00:22:10:21:A4:B0";
    private static final String INVALID_MAC_ID_FORMAT = "1234";

    @Test
    public void testIsValidIp()
    {
        Assert.assertTrue( CommonUtil.isValidIp( "76.96.76.2" ) );
        Assert.assertFalse( CommonUtil.isValidIp( "fe80::5efe:192.168.160.84%12" ) );
        Assert.assertFalse( CommonUtil.isValidIp( "comcast.com" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidIpEmpty()
    {
        CommonUtil.isValidIp( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidIpNull()
    {
        CommonUtil.isValidIp( null );
    }

    @Test
    public void testIsValidNumber()
    {
        Assert.assertTrue( CommonUtil.isValidNumber( "100" ) );
        Assert.assertTrue( CommonUtil.isValidNumber( "1.0" ) );
        Assert.assertTrue( CommonUtil.isValidNumber( "000" ) );
        Assert.assertTrue( CommonUtil.isValidNumber( "-10" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidNumberEmpty()
    {
        CommonUtil.isValidNumber( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidNumberLetter()
    {
        CommonUtil.isValidNumber( "a" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidNumberNull()
    {
        CommonUtil.isValidNumber( null );
    }

    @Test
    public void testIsValidMacId()
    {
        Assert.assertTrue( CommonUtil.isValidMacId( MAC ) );
        Assert.assertFalse( CommonUtil.isValidMacId( INVALID_MAC_ID_FORMAT ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidMacIdEmpty()
    {
        CommonUtil.isValidMacId( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidMacIdNull()
    {
        CommonUtil.isValidMacId( null );
    }
}
