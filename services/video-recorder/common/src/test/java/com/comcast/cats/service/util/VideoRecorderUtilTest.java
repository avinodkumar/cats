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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.DiskSpaceUsage;

/**
 * Unit tests for {@link VideoRecorderUtil}.
 * 
 * @author ssugun00c
 * 
 */
public class VideoRecorderUtilTest
{
    private Logger logger = LoggerFactory.getLogger( getClass() );

    @Test
    public void testGetDiskSpaceUsage() throws Exception
    {
        for ( DiskSpaceUsage diskSpaceUsage : VideoRecorderUtil.getDiskSpaceUsage() )
        {
            logger.info( diskSpaceUsage.toString() );
            logger.info( "Total Space " + diskSpaceUsage.getTotalSpace() / 1024 / 1024 + " GB" );
        }
    }
}
