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
package com.comcast.cats.service.power;

import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * The Class PowerStatusExceptionTest.
 * @Author : Aneesh
 * @since : 28th Sept 2012
 * Description : The class PowerStatusExceptionTest is the unit test of {@link PowerStatusException}
 */
public class PowerStatusExceptionTest
{
    
    /** The pwer exp. */
    PowerStatusException pwerExp;

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        pwerExp = new PowerStatusException();
        Assert.assertTrue( pwerExp instanceof RuntimeException);
    }
    
    /**
     * Test text msg constructor.
     */
    @Test
    public void testTextMsgConstructor() {
        String msg = "Exception message";
        pwerExp = new PowerStatusException(msg);
        Assert.assertTrue( pwerExp instanceof RuntimeException);
        Assert.assertTrue( pwerExp.getMessage().equals( msg ));
    }
    
    /**
     * Test thowable constructor.
     */
    @Test
    public void testThowableConstructor() {
        Throwable cause = new Throwable();
        pwerExp = new PowerStatusException(cause);
        Assert.assertTrue( pwerExp instanceof RuntimeException);
        Assert.assertTrue( pwerExp.getCause().equals( cause ) );
    }
    
    /**
     * Test thowable msg constructor.
     */
    @Test
    public void testThowableMsgConstructor() {
        Throwable cause = new Throwable();
        String msg = "Exception message";
        pwerExp = new PowerStatusException(msg, cause);
        Assert.assertTrue( pwerExp instanceof RuntimeException);
        Assert.assertTrue( pwerExp.getCause().equals( cause ) );
        Assert.assertTrue( pwerExp.getMessage().equals( msg ));
    }
}
