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

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class GlobalCacheCodeTest.
 * 
 * @Author : Deepa
 * @since : 1st Nov 2012
 * @Description : The Class GlobalCacheCodeTest is the unit test of
 *              {@link GlobalCacheCode}.
 */
public class GlobalCacheCodeTest
{
    /* Test Object*/
    GlobalCacheCode globalCacheCode;
    String code ;
            

    @BeforeMethod
    public void setUp() throws Exception
    {
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
    }

    @Test(expectedExceptions= IllegalArgumentException.class)
    public void testGlobalCacheCode()
    {
        code = "0000 006C";
        globalCacheCode = new GlobalCacheCode( code );
    }

    @Test(enabled = true)
    public void testGetSendIRCommand()
    {
        code = "0158 00AD 0013 0057 0013 0057 0013";
        globalCacheCode = new GlobalCacheCode( code );
        String result = globalCacheCode.getSendIRCommand( 1, 1, 1, "2:1" );
        Assert.assertNotNull( result );
    }

    @Test
    public void testVerifyCompleteIRCommand()
    {
        code = "0158 00AD 0013 0057 0013 0057 0013";
        globalCacheCode = new GlobalCacheCode( code );
        Assert.assertTrue(globalCacheCode.verifyCompleteIRCommand( "completeir,2:1,23000", 23000, "2:1" ));
    }

    @Test
    public void testIsUnkownCommand()
    {
        String unknownCommand =  "unkowncommand";
        code = "0158 00AD 0013 0057 0013 0057 0013";
        globalCacheCode = new GlobalCacheCode( code );
        Assert.assertTrue(globalCacheCode.isUnkownCommand( unknownCommand ));
    }

}
