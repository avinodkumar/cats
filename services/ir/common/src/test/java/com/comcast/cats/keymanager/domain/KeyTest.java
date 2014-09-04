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

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import com.comcast.cats.service.IRHardwareEnum;

/**
 * The Class KeyTest.
 * 
 * @Author : Deepa
 * @since : 31st Oct 2012 
 * @Description : The Class KeyTest is the
 *        unit test of {@link Key}.
 */
public class KeyTest
{
    /* Test object */
    Key key;
    
    /* Initialise the test object. */
    @BeforeMethod
    public void setUp() throws Exception
    {
       key = new Key();
    }
    
    /*  Assign null value to object initialised. */  
    @AfterMethod
    public void tearDown() throws Exception
    {
        key = null;
    }

    /* Test the constructor. */
    @Test
    public void testKey()
    {
        String name = "testKey";
        String value = "11";
        String format = "PRONTO";
        key = new Key(name, value, format);
        Assert.assertEquals( key.getName(), name );
        Assert.assertEquals( key.getValue(), value );
        Assert.assertEquals( key.getFormat(), format );
    }
    
    /**
     * Test Set and Get methods for all fields in the Bean
     */
    @Test
    public void testGetAndSetBeanValues(){
        String name = "test";
        String value = "100";
        String format = "PRONTOCODE";
        key.setName( name );
        Assert.assertEquals( key.getName(), name );
        key.setValue( value );
        Assert.assertEquals( key.getValue(), value );
        key.setFormat( format );
        Assert.assertEquals( key.getFormat(), format );
    }

}
