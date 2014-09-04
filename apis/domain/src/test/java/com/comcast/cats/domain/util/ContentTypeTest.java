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
package com.comcast.cats.domain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

public class ContentTypeTest
{
    protected final Logger LOGGER = LoggerFactory.getLogger( ContentTypeTest.class );
    
    @Test
    public void testContents()
    {
        LOGGER.info( "{}", ContentType.APPLICATION_JAXB_SERIALIZED_OBJECT );
        LOGGER.info( "{}", ContentType.APPLICATION_JAXB_SERIALIZED_LIST );

        LOGGER.info( ContentType.APPLICATION_JAXB_SERIALIZED_OBJECT.getType() );
        LOGGER.info( ContentType.APPLICATION_JAXB_SERIALIZED_LIST.getType() );

        LOGGER.info( ContentType.APPLICATION_JAXB_SERIALIZED_OBJECT.getSubtype() );
        LOGGER.info( ContentType.APPLICATION_JAXB_SERIALIZED_LIST.getSubtype() );
        
    }
}
