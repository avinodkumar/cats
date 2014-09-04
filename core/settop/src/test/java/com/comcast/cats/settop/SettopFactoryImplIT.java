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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.comcast.cats.CatsAbstarctIT;
import com.comcast.cats.DataProvider;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;

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
    private SettopFactory       settopFactory;
    private SettopDomainService settopDomainService;

    public SettopFactoryImplIT()
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
        String macId = DataProvider.INVALID_MAC_ID_FORMAT;
        settopFactory.findSettopByHostMac( macId );
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
		assertNotNull("Logger should not be null", settop.getLogger());
        LOGGER.info( settop.toString() );
    }

    @Test
    public void findSettopAndAllocateByHostMac() throws SettopNotFoundException, AllocationException
    {
        try
        {
            Settop settop = settopFactory.findSettopByHostMac( propertyUtil.getMacId(), true );
            assertNotNull( settop );
            assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
            assertNotNull( "PowerProvider should not be  null", settop.getPower() );
            assertNotNull( "VideoProvider should not be  null", settop.getVideo() );
            assertNotNull( "ImageCompareProvider should not be  null", settop.getImageCompareProvider() );
            LOGGER.info( settop.toString() );
        }
        catch ( AllocationException e )
        {
            fail( e.getMessage() );
        }

    }

    @Test
    public void findSettopByHostMacAndAllocate_False() throws SettopNotFoundException, AllocationException
    {
        Settop settop = settopFactory.findSettopByHostMac( propertyUtil.getMacId(), false );
        assertNotNull( settop );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop.getImageCompareProvider() );
        LOGGER.info( settop.toString() );
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
        macIdList.add( DataProvider.INVALID_MAC_ID_FORMAT );
        settopFactory.findSettopByHostMac( macIdList );
    }

    @Test
    public void findSettopListByHostMac() throws SettopNotFoundException
    {
        List< String > macIdList = new ArrayList< String >();
        macIdList.add( propertyUtil.getMacId() );
        List< Settop > settops = settopFactory.findSettopByHostMac( macIdList );
        assertNotNull( settops );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByCriteriaNull() throws SettopNotFoundException
    {
        Map< String, String > criteria = null;
        settopFactory.findAllSettopByCriteria( criteria );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByCriteriaEmpty() throws SettopNotFoundException
    {
        Map< String, String > criteria = new HashMap< String, String >();
        settopFactory.findAllSettopByCriteria( criteria );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByCriteriaEmptyMap() throws SettopNotFoundException
    {
        @SuppressWarnings( "unchecked" )
        Map< String, String > criteria = Collections.EMPTY_MAP;
        settopFactory.findAllSettopByCriteria( criteria );
    }

    @Test
    public void findAllSettopByCriteria() throws SettopNotFoundException
    {
        Map< String, String > criteria = new HashMap< String, String >();
        criteria.put( SettopConstants.HOST_MAC_ADDRESS_PROPERTY, propertyUtil.getMacId() );
        List< Settop > settops = settopFactory.findAllSettopByCriteria( criteria );
        assertNotNull( settops );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopBySettopGroupNull() throws SettopNotFoundException
    {
        settopFactory.findAllSettopByGroupName( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopBySettopGroupEmpty() throws SettopNotFoundException
    {
        settopFactory.findAllSettopByGroupName( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findAllSettopBySettopGroup() throws SettopNotFoundException
    {
        List< Settop > settops = settopFactory.findAllSettopByGroupName( DataProvider.getSettopGroupName() );
        assertNotNull( settops );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableSettopBySettopGroupNull() throws SettopNotFoundException
    {
        settopFactory.findAvailableSettopByGroupName( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableSettopBySettopGroupEmpty() throws SettopNotFoundException
    {
        settopFactory.findAvailableSettopByGroupName( DataProvider.EMPTY_STRING );
    }

    @Test
    public void findAvailableSettopBySettopGroup() throws SettopNotFoundException
    {
        List< Settop > settops = settopFactory.findAvailableSettopByGroupName( DataProvider.getSettopGroupName() );
        assertNotNull( settops );
    }
}
