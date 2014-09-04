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
package com.comcast.cats.domain.configuration.test;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.service.DomainService;
import com.comcast.cats.domain.service.SettopDomainService;

/**
 * Class to make sure the {@link ApplicationContext} is initialized properly.
 * 
 * @author subinsugunan
 * 
 */
@ContextConfiguration( locations =
    { "classpath:application-config-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
public class ApplicationContextTest
{
    @Inject
    private SettopDomainService settopDomainService;

    @Inject
    CatsProperties              catsProperties;

    @Inject
    @Qualifier( "domainServiceImpl" )
    private DomainService< ? >  domainService;

    @Test
    public void fileContextTest()
    {
        Assert.assertNotNull( domainService );
        Assert.assertNotNull( settopDomainService );
        Assert.assertNotNull( catsProperties );
    }
}
