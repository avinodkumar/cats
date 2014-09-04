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
package com.comcast.cats.settop;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.CatsAbstarctIT;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;

/**
 * Integration test for to verify Support OCR Service override in cats.props
 * <br>
 * <br>
 * 
 * @author subinsugunan
 * 
 */
public class SettopFactoryPropertyOverrideIT extends CatsAbstarctIT
{
    private SettopFactory       settopFactory;
    private SettopDomainService settopDomainService;

    public SettopFactoryPropertyOverrideIT()
    {
        settopFactory = catsFramework.getSettopFactory();
        settopDomainService = catsFramework.getContext().getBean( SettopDomainService.class );
    }

    @Test
    public void testContext()
    {
        assertNotNull( settopFactory );
        assertNotNull( settopDomainService );
    }

    @Test
    public void findSettopByHostMac() throws SettopNotFoundException
    {
        Settop settop = settopFactory.findSettopByHostMac( propertyUtil.getMacId() );
        assertNotNull( settop );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop.getImageCompareProvider() );
        LOGGER.info( "{}", settop );
    }

    @Test
    @Ignore( "Make sure to allocate a Settop before running this" )
    public void findAllAllocatedSettop() throws SettopNotFoundException
    {
        List< Settop > settops = settopFactory.findAllAllocatedSettop();

        assertNotNull( settops );

        LOGGER.info( "AllocatedSettop" );

        for ( Settop settop : settops )
        {
            LOGGER.info( "{}", settop );
        }
    }

    @Test
    public void findAllAvailableSettop() throws SettopNotFoundException
    {
        List< Settop > settops = settopFactory.findAllAvailableSettop();

        assertNotNull( settops );

        LOGGER.info( "AvailableSettop" );

        for ( Settop settop : settops )
        {
            LOGGER.info( "{}", settop );
        }
    }
}
