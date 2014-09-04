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
package com.comcast.cats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;

import com.comcast.cats.configuration.StringsDMHandler;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.provider.ExclusiveAccessBridge;
import com.comcast.cats.provider.ExclusiveAccessManager;

/**
 * Integration test to make sure all beans and relationships works. <br>
 * <br>
 * 
 * @author subinsugunan
 * 
 */
public class AnnotationWiringIT extends CatsAbstarctTestCase
{
    @Inject
    private CatsProperties           catsProperties;

    @Inject
    private StringsDMHandler         stringsDMHandler;

    @Inject
    private ExclusiveAccessBridge    exclusiveAccessBridge;

    @Inject
    private ExclusiveAccessManager   exclusiveAccessManager;

    @Inject
    private SettopFactory            settopFactory;

    @Test
    public void testAllComponents()
    {
        assertNotNull( catsProperties );
        assertEquals( SERVER_BASE, catsProperties.getServerUrl() );
        assertEquals( AUTH_TOKEN, catsProperties.getAuthToken() );
        assertNotNull( stringsDMHandler );
        assertNotNull( exclusiveAccessBridge );
        assertNotNull( exclusiveAccessBridge.getExclusiveAccessProvider() );
        assertNotNull( exclusiveAccessManager );
        assertNotNull( settopFactory );
    }
}