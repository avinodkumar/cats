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

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import com.comcast.cats.service.impl.ProntoCode;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : cfrede001
 * @since :
 * @Description : The Class PowerControllerDeviceFactoryImplTest is the unit
 *              test of {@link PowerControllerDeviceFactoryImplTest}.
 */
public class ProntoCodeTest
{

    @Test
    public void PositiveValidProntoTest()
    {
        String code = "0000 006C 0012 0002 "
                + "0158 00AD 0013 0057 0013 0057 0013 00AD 0013 0057 0013 00AD 0013 00AD 0013 "
                + "0057 0013 0057 0013 0057 0013 0057 0013 0057 0013 0057 0013 00AD 0013 0057 "
                + "0013 0057 0013 00AD 0013 0491 " + "0158 0057 0013 0D26";

        String firstPair = "0158 00AD 0013 0057 0013 0057 0013 00AD 0013 0057 0013 00AD 0013 00AD 0013 "
                + "0057 0013 0057 0013 0057 0013 0057 0013 0057 0013 0057 0013 00AD 0013 0057 "
                + "0013 0057 0013 00AD 0013 0491 ";
        firstPair = firstPair.replaceAll( "\\s", "" );

        String secondPair = "0158 0057 0013 0D26";
        secondPair = secondPair.replaceAll( "\\s", "" );

        ProntoCode pronto = new ProntoCode( code );
        int frequency = pronto.getFrequency();
        int burst1 = pronto.getFirstBurstPairCnt();
        int burst2 = pronto.getSecondBurstPairCnt();
        System.out.println( "       Frequency = " + Integer.toString( frequency ) );
        System.out.println( " First Burst Cnt = " + Integer.toString( burst1 ) );
        System.out.println( "Second Burst Cnt = " + Integer.toString( burst2 ) );
        AssertJUnit.assertEquals( frequency, 38000 );
        AssertJUnit.assertEquals( burst1, 18 );
        AssertJUnit.assertEquals( burst2, 2 );
        String firstPairStr = pronto.getFirstBurstPair();
        String secondPairStr = pronto.getSecondBurstPair();
        System.out.println( "      First Pair = " + firstPairStr );
        System.out.println( "     Second Pair = " + secondPairStr );
        AssertJUnit.assertEquals( firstPairStr, firstPair );
        AssertJUnit.assertEquals( secondPairStr, secondPair );
    }
}
