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
package com.comcast.cats.image;

/**
 * Class contains common Region Info test info.
 */
public abstract class CommonRegionInfoTest {
    /** This timeout should be 5 seconds. Changing because tests are timing out in VM **/
    protected static final int TIME_OUT = 50000;
    
    protected static final String DEFAULT_NAME = "";
    protected static final int DEFAULT_INT = 0;
    protected static final int X = 100;
    protected static final int Y = 150;
    protected static final int WIDTH = 200;
    protected static final int HEIGHT = 250;
    protected static final String NAME = "THENAME";    
    protected static final int XTOL = 7;
    protected static final int YTOL = 8;
}
