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
package com.comcast.cats.service.util;

import org.junit.Test;

/**
 * Unit tests for {@link AssertUtil}.
 * 
 * @author ssugun00c
 * 
 */
public class AssertUtilTest
{
    @Test
    public void isValidIp() throws Exception
    {
        AssertUtil.isValidIp( "Valid FQDN", "localhost" );
        AssertUtil.isValidIp( "Valid CATS Server IP", "192.168.160.201" );
        AssertUtil.isValidIp( "Valid AXis IP", "192.168.160.201" );
        AssertUtil.isValidIp( "localhost", "localhost" );
        AssertUtil.isValidIp( "127.0.0.1", "127.0.0.1" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isValidIpInvalidFqdn() throws Exception
    {
        AssertUtil.isValidIp( "InValid FQDN", "localhost" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void isValidIpInvalidIp() throws Exception
    {
        AssertUtil.isValidIp( "InValid IP", "00.000.000.00" );
    }

}
