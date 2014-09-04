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
package com.comcast.cats.ir.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DelayCommandTest
{
    DelayCommand delayCommand;
    int          delay = 100;

    @Before
    public void setup()
    {
        delayCommand = new DelayCommand(delay);
    }

    @After
    public void tearDown()
    {
        delayCommand = null;
    }

    @Test
    public void delayCommandTest(){
        assertEquals( delay, delayCommand.getDelay() );
        delayCommand.setDelay( 200 );
        assertEquals( 200, delayCommand.getDelay() );
        delayCommand.setDelay( -200 );
        assertEquals( 0, delayCommand.getDelay() );
    }
}
