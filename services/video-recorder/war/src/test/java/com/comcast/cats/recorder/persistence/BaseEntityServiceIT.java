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
package com.comcast.cats.recorder.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.recorder.VLCBaseTestCase;

/**
 * Base test case for all entity service tests.
 * 
 * @author SSugun00c
 * 
 */
public class BaseEntityServiceIT extends VLCBaseTestCase
{
    protected Logger             LOGGER                = LoggerFactory.getLogger( getClass() );

    private EntityManagerFactory entityManagerFactory;
    protected EntityManager      entityManager;

    // Refer /src/main/resources/META-INF/persistence.xml
    private String               PERSISTENCE_UNIT_NAME = "pvr-persistence-unit-test";

    @Before
    public void setup()
    {
        super.setup();

        entityManagerFactory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
        entityManager = entityManagerFactory.createEntityManager();
    }

    @After
    public void cleanUp()
    {
        if ( null != entityManager )
        {
            entityManager.close();
        }

        if ( null != entityManagerFactory )
        {
            entityManagerFactory.close();
        }
    }
}
