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
package com.comcast.cats.service;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static com.comcast.cats.service.IRHardwareEnum.*;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : Deepa
 * @since : 10th Sept 2012 
 * @Description : The Class IRHardwareEnumTest is the
 *        unit test of {@link IRHardwareEnum}.
 */
public class IRHardwareEnumTest
{

    @Test
    public void testEnum()
    {
        AssertJUnit.assertEquals( IRHardwareEnum.getByValue( "GC100" ), GC100 );
        AssertJUnit.assertEquals( IRHardwareEnum.getByValue( "GC100-12" ), GC100_12 );
        AssertJUnit.assertEquals( IRHardwareEnum.getByValue( "gc100-12" ), GC100_12 );
    }

    @Test( expectedExceptions = IllegalArgumentException.class )
    public void negative()
    {
        IRHardwareEnum.getByValue( "DUMMY" );
    }

    @Test
    public void validate()
    {
        AssertJUnit.assertTrue( IRHardwareEnum.validate( "GC100" ) );
        AssertJUnit.assertFalse( IRHardwareEnum.validate( "DUMMY" ) );
    }
}
