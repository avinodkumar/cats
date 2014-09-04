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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.service.SettopDomainServiceImpl;

/**
 * Integration test for {@link SettopFactory} and
 * {@link SettopFactoryServiceImpl} <br>
 * <br>
 * <br>
 * 
 * @author subinsugunan
 * 
 */
public class SettopFactoryImplIT extends CatsAbstarctIT
{
    private static final String MAC_ID                = "54:D4:6F:96:CD:78";
    public static final String  INVALID_MAC_ID_FORMAT = "1234567890";

    private SettopFactory       settopFactory;
    private SettopDomainService settopDomainService;

    public SettopFactoryImplIT()
    {
        settopFactory = catsFramework.getSettopFactory();
        settopDomainService = catsFramework.getContext().getBean( SettopDomainServiceImpl.class );
    }

    @Test
    public void testContext()
    {
        assertNotNull( settopFactory );
        assertNotNull( settopDomainService );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByHostMacNull() throws SettopNotFoundException
    {
        String macId = null;
        settopFactory.findSettopByHostMac( macId );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByHostMacEmpty() throws SettopNotFoundException
    {
        String macId = null;
        settopFactory.findSettopByHostMac( macId );
    }

    @Test( expected = SettopNotFoundException.class )
    public void findSettopByHostMacInvalid() throws SettopNotFoundException
    {
        settopFactory.findSettopByHostMac( INVALID_MAC_ID_FORMAT );
    }

    @Test
    public void findSettopByHostMac() throws SettopNotFoundException
    {
        Settop settop = settopFactory.findSettopByHostMac( MAC_ID );
        assertNotNull( settop );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop.getImageCompareProvider() );
        LOGGER.info( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopListByHostMacNull() throws SettopNotFoundException
    {
        List< String > macIdList = null;
        settopFactory.findSettopByHostMac( macIdList );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopListByHostMacEmpty() throws SettopNotFoundException
    {
        List< String > macIdList = new ArrayList< String >();
        settopFactory.findSettopByHostMac( macIdList );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopListByHostMacEmptyList() throws SettopNotFoundException
    {
        @SuppressWarnings( "unchecked" )
        List< String > macIdList = Collections.EMPTY_LIST;
        settopFactory.findSettopByHostMac( macIdList );
    }

    @Test( expected = SettopNotFoundException.class )
    public void findSettopListByHostMacInvalid() throws SettopNotFoundException
    {
        List< String > macIdList = new ArrayList< String >();
        macIdList.add( INVALID_MAC_ID_FORMAT );
        settopFactory.findSettopByHostMac( macIdList );
    }

    @Test
    public void findSettopListByHostMac() throws SettopNotFoundException
    {
        List< String > macIdList = new ArrayList< String >();
        macIdList.add( MAC_ID );
        List< Settop > settops = settopFactory.findSettopByHostMac( macIdList );
        assertNotNull( settops );
    }
}
