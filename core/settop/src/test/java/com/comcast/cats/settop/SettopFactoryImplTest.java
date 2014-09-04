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

import javax.inject.Inject;

import org.junit.Test;

import com.comcast.cats.CatsAbstarctTestCase;
import com.comcast.cats.DataProvider;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * 
 * @author subinsugunan
 * 
 */
public class SettopFactoryImplTest extends CatsAbstarctTestCase
{
    @Inject
    private SettopFactory settopFactory;

    @Test
    public void testContext()
    {
        assertNotNull( settopFactory );
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
        String macId = dataProvider.getMacId();
        Settop settop = settopFactory.findSettopByHostMac( macId );
        assertNotNull( settop );
        assertNotNull( settop.getRemote() );
        assertNotNull( settop.getPower() );
        // assertNotNull( settop.getAudio() );
        assertNotNull( settop.getVideo() );
        // assertNotNull( settop.getTrace() );
        // assertNotNull( settop.getAudio() );
        LOGGER.info( settop.toString() );
    }

    @Test
    public void findSettopByHostMacAndAllocate() throws SettopNotFoundException
    {
        try
        {
            String macId = dataProvider.getMacId();
            Settop settop = settopFactory.findSettopByHostMac( macId, true );
            assertNotNull( settop );
            assertNotNull( settop.getRemote() );
            assertNotNull( settop.getPower() );
            assertNotNull( settop.getVideo() );
            LOGGER.info( settop.toString() );
        }
        catch ( AllocationException e )
        {
            fail( e.getMessage() );
        }
    }

    @Test
    public void findSettopByHostMac_AllocateFalse() throws SettopNotFoundException, AllocationException
    {
        String macId = dataProvider.getMacId();
        Settop settop = settopFactory.findSettopByHostMac( macId, false );
        assertNotNull( settop );
        assertNotNull( settop.getRemote() );
        assertNotNull( settop.getPower() );
        assertNotNull( settop.getVideo() );
        LOGGER.info( settop.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByHostMacAndAllocate_MacNull() throws SettopNotFoundException, AllocationException
    {
        String macId = null;
        settopFactory.findSettopByHostMac( macId, false );
    }

    @Test( expected = SettopNotFoundException.class )
    public void findSettopByHostMacAndAllocate_InvalidMac() throws SettopNotFoundException, AllocationException
    {
        String macId = DataProvider.INVALID_MAC_ID_FORMAT;
        settopFactory.findSettopByHostMac( macId, false );
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
        macIdList.add( dataProvider.getMacId() );
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
        criteria.put( SettopConstants.HOST_MAC_ADDRESS_PROPERTY, dataProvider.getMacId() );
        List< Settop > settops = settopFactory.findAllSettopByCriteria( criteria );
        assertNotNull( settops );
    }
}
