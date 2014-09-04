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

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import org.junit.Test;

public class CatsUtilsTest
{
    
    @Test
    public void hasTimeElapsedTest(){
        Calendar calendarPast = Calendar.getInstance();
        calendarPast.set( calendarPast.get( Calendar.YEAR ), calendarPast.get( Calendar.MONTH ), calendarPast.get( Calendar.DATE ) -1 );
        assertTrue(CatsUtils.hasTimeElapsed(calendarPast.getTime() , 23*60*60*1000 )); // 11 hour timeinterval
        
        Calendar calendarPresent = Calendar.getInstance();
        calendarPresent.set( calendarPresent.get( Calendar.YEAR ), calendarPresent.get( Calendar.MONTH ), calendarPresent.get( Calendar.DATE ), calendarPresent.get( Calendar.HOUR_OF_DAY ),calendarPresent.get( Calendar.MINUTE ) - 5);
        assertFalse(CatsUtils.hasTimeElapsed(calendarPresent.getTime() , 10*60*1000 )); // 10 minutes
        
        Calendar calendarFuture = Calendar.getInstance();
        calendarFuture.set( calendarFuture.get( Calendar.YEAR ), calendarFuture.get( Calendar.MONTH ), calendarFuture.get( Calendar.DATE ), calendarFuture.get( Calendar.HOUR_OF_DAY ),calendarFuture.get( Calendar.MINUTE ) + 5);
        assertFalse(CatsUtils.hasTimeElapsed(calendarFuture.getTime() , 10*60*1000 )); // 10 minutes
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hasTimeElapsedNullTest(){
       CatsUtils.hasTimeElapsed( null, 100 );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hasTimeElapsedInvalidTest(){
       CatsUtils.hasTimeElapsed( new Date(), -100 );
    }

}
