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
package com.comcast.cats.domain.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.service.BaseTestCase;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for {@link CommonUtil}
 * 
 * @author subinsugunan
 * 
 */
public class CommonUtilTest extends BaseTestCase
{
    @Test
    public void testArrayToStringWithOneValue()
    {
        String[] values = new String[]
            { "Pace DTA" };

        String expected = "['Pace DTA']";
        String actual = CommonUtil.arrayToString( values );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testArrayToStringWithTwoValues()
    {
        String[] values = new String[]
            { "Pace DTA", "Motorola" };

        String expected = "['Pace DTA','Motorola']";
        String actual = CommonUtil.arrayToString( values );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testArrayToStringWithEmptyArray()
    {
        String[] values = new String[] { };

        String expected = "[]";
        String actual = CommonUtil.arrayToString( values );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testArrayToStringWithNull()
    {
        String expected = "[]";
        String actual = CommonUtil.arrayToString( null );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testmapToStringWithOneValue()
    {
        Map< String, String > criteria = new HashMap< String, String >();
        criteria.put( "Manufacturer", "Pace DTA" );

        String expected = "['Manufacturer':'Pace DTA']";
        String actual = CommonUtil.mapToString( criteria );

        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testmapToStringWithTwoValue()
    {
        Map< String, String > criteria = new HashMap< String, String >();
        criteria.put( "Manufacturer", "Pace DTA" );
        criteria.put( "Model", "DC50X" );

        String expected = "['Manufacturer':'Pace DTA','Model':'DC50X']";
        String actual = CommonUtil.mapToString( criteria );

        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testmapToStringWithEmptyMap()
    {
        Map< String, String > criteria = new HashMap< String, String >();

        String expected = "[]";
        String actual = CommonUtil.mapToString( criteria );

        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testmapToStringWithNull()
    {
        String expected = "[]";
        String actual = CommonUtil.mapToString( null );

        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testGetNameValuePairNullValue()
    {
        String expected = "";
        String actual = CommonUtil.getNameValuePair( null );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testGetNameValuePairEmptyValue()
    {
        Map< String, String> params  = Collections.emptyMap();
        String expected = "";
        String actual = CommonUtil.getNameValuePair( params );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testGetNameValuePairOneValue()
    {
        Map< String, String> params  = new LinkedHashMap< String, String>();
        params.put( "1", "one" );

        String expected = "?1=one";
        String actual = CommonUtil.getNameValuePair( params );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testGetNameValuePairTwoValue()
    {
        Map< String, String> params  = new LinkedHashMap< String, String>();
        params.put( "1", "one" );
        params.put( "2", "two" );

        String expected = "?1=one&2=two";
        String actual = CommonUtil.getNameValuePair( params );
        Assert.assertEquals( expected, actual );
    }

    @Test
    public void testGetNameValuePairThreeValue()
    {
        Map< String, String > params = new LinkedHashMap< String, String>();
        params.put( "1", "one" );
        params.put( "2", "two" );
        params.put( "3", "three" );

        String expected = "?1=one&2=two&3=three";
        String actual = CommonUtil.getNameValuePair( params );
        Assert.assertEquals( expected, actual );
    }

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
        Assert.assertFalse( CommonUtil.isValidNumber( "a" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidNumberEmpty()
    {
        CommonUtil.isValidNumber( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testIsValidNumberNull()
    {
        CommonUtil.isValidNumber( null );
    }

    @Test
    public void testIsValidMacId()
    {
        Assert.assertTrue( CommonUtil.isValidMacId( testProperties.getMacId() ) );
        Assert.assertFalse( CommonUtil.isValidMacId( DataProvider.INVALID_MAC_ID_FORMAT ) );
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

    @Test
    public void getInstance()
    {
       // AssertUtil.isNull( CommonUtil.getInstance() ); // FIXME this throws compilation failure
    }
}
