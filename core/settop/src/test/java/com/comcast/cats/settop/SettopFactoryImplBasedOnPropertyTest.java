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

import javax.inject.Inject;

import org.junit.Test;

import com.comcast.cats.CatsAbstarctTestCase;
import com.comcast.cats.DataProvider;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;

/**
 * 
 * @author subinsugunan
 * 
 */
public class SettopFactoryImplBasedOnPropertyTest extends CatsAbstarctTestCase
{
    @Inject
    private SettopFactory       settopFactory;

    @Inject
    private SettopDomainService settopDomainService;

    @Test
    public void testContext()
    {
        assertNotNull( settopFactory );
        assertNotNull( settopDomainService );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByPropertyNullValues() throws SettopNotFoundException
    {
        String property = null;
        String[] values = new String[]
            { dataProvider.getMacId() };
        settopFactory.findAllSettopByPropertyValues( property, values );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByPropertyValuesNullValue() throws SettopNotFoundException
    {
        String property = SettopConstants.HOST_MAC_ADDRESS_PROPERTY;
        String[] values = null;
        settopFactory.findAllSettopByPropertyValues( property, values );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByPropertyEmptyValues() throws SettopNotFoundException
    {
        String property = DataProvider.EMPTY_STRING;
        String[] values = new String[]
            { dataProvider.getMacId() };
        settopFactory.findAllSettopByPropertyValues( property, values );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByPropertyValuesEmpty() throws SettopNotFoundException
    {
        String property = SettopConstants.HOST_MAC_ADDRESS_PROPERTY;
        String[] values = new String[] { };
        settopFactory.findAllSettopByPropertyValues( property, values );
    }

    @Test
    public void findAllSettopByPropertyValues() throws SettopNotFoundException
    {
        String property = SettopConstants.HOST_MAC_ADDRESS_PROPERTY;
        String[] values = new String[]
            { dataProvider.getMacId() };
        List< Settop > settops = settopFactory.findAllSettopByPropertyValues( property, values );
        assertNotNull( settops );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByUnitAddressNull() throws SettopNotFoundException
    {
        String unitAdderess = null;
        settopFactory.findSettopByUnitAddress( unitAdderess );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByUnitAddressEmpty() throws SettopNotFoundException
    {
        String unitAdderess = DataProvider.EMPTY_STRING;
        settopFactory.findSettopByUnitAddress( unitAdderess );
    }

    @Test
    public void findSettopByUnitAddress() throws SettopNotFoundException
    {
        String unitAdderess = dataProvider.getUnitAdderess();
        Settop settop = settopFactory.findSettopByUnitAddress( unitAdderess );
        assertNotNull( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByHostIpAddressNull() throws SettopNotFoundException
    {
        String ipAdderess = null;
        settopFactory.findSettopByHostIpAddress( ipAdderess );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findSettopByHostIpAddressEmpty() throws SettopNotFoundException
    {
        String ipAdderess = DataProvider.EMPTY_STRING;
        settopFactory.findSettopByHostIpAddress( ipAdderess );
    }

    @Test( expected = SettopNotFoundException.class )
    public void findSettopByHostIpAddressInvalid() throws SettopNotFoundException
    {
        String ipAdderess = DataProvider.INVALID_IP_ADDRESS;
        settopFactory.findSettopByHostIpAddress( ipAdderess );
    }

    @Test
    public void findSettopByHostIpAddress() throws SettopNotFoundException
    {
        String ipAdderess = dataProvider.getIpAdderess();
        Settop settop = settopFactory.findSettopByHostIpAddress( ipAdderess );
        assertNotNull( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByRemoteTypeNull() throws SettopNotFoundException
    {
        String remoteType = null;
        settopFactory.findAllSettopByRemoteType( remoteType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByRemoteTypeEmpty() throws SettopNotFoundException
    {
        String remoteType = DataProvider.EMPTY_STRING;
        settopFactory.findAllSettopByRemoteType( remoteType );
    }

    @Test
    public void findAllSettopByRemoteType() throws SettopNotFoundException
    {
        String remoteType = dataProvider.getRemoteType();
        List< Settop > settops = settopFactory.findAllSettopByRemoteType( remoteType );
        assertNotNull( settops );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByModelNull() throws SettopNotFoundException
    {
        String model = null;
        settopFactory.findAllSettopByModel( model );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllSettopByModelEmpty() throws SettopNotFoundException
    {
        String model = DataProvider.EMPTY_STRING;
        settopFactory.findAllSettopByModel( model );
    }

    @Test
    public void findAllSettopByModel() throws SettopNotFoundException
    {
        String model = dataProvider.getModel();
        List< Settop > settops = settopFactory.findAllSettopByModel( model );
        assertNotNull( settops );
    }
}
