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
package com.comcast.cats.domain.service.it;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integrations test cases for property based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.property.one           :   First property of {@link SettopDesc}
 *    test.value.one              :   Value for first property
 *    test.property.two           :   Second property of {@link SettopDesc}
 *    test.value.two              :   Value for second property
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.property.one           =   Manufacturer
 *    test.value.one              =   Pace
 *    test.property.two           =   Model
 *    test.value.two              =   DC50Xu-Micro
 *    test.offset                 =   0
 *    test.count                  =   1
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   852
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnPropertyIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllByPropertyNull()
    {
        settopDomainService.findAllByProperty( DataProvider.STB_PROP_MANUFACTURER, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByPropertyEmpty()
    {
        String[] values = new String[] { };
        settopDomainService.findAllByProperty( DataProvider.STB_PROP_MANUFACTURER, values );
    }

    @Test
    public void findAllByPropertyOneValue()
    {
        String[] values = new String[]
            { testProperties.getPropertyValueOne() };
        List< SettopDesc > settops = settopDomainService.findAllByProperty( DataProvider.STB_PROP_MANUFACTURER, values );

        List< String > settopList = new ArrayList< String >();
        Collections.addAll( settopList, values );

        logResult( settops.size() );

        for ( SettopDesc settop : settops )
        {
            Assert.assertTrue( settopList.contains( settop.getManufacturer() ) );
        }
    }

    @Test
    public void findAllByPropertyTwoValue()
    {
        List< SettopDesc > settops = settopDomainService.findAllByProperty( DataProvider.STB_PROP_MANUFACTURER,
                dataProvider.getManufacturerList() );

        List< String > settopList = new ArrayList< String >();
        Collections.addAll( settopList, dataProvider.getManufacturerList() );

        logResult( settops.size() );

        for ( SettopDesc settop : settops )
        {
            Assert.assertTrue( settopList.contains( settop.getManufacturer() ) );
        }
    }

    @Test
    public void findAllByPropertyOffset()
    {
        String[] values = new String[]
            { testProperties.getPropertyValueOne() };

        List< SettopDesc > settops = settopDomainService.findAllByProperty( DataProvider.STB_PROP_MANUFACTURER, values,
                offset, count );

        List< String > settopList = new ArrayList< String >();
        Collections.addAll( settopList, values );

        logResult( settops.size() );

        for ( SettopDesc settop : settops )
        {
            Assert.assertTrue( settopList.contains( settop.getManufacturer() ) );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByPropertyNull()
    {
        settopDomainService.findAvailableByProperty( DataProvider.STB_PROP_MANUFACTURER, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByPropertyEmpty()
    {
        String[] values = new String[] { };
        settopDomainService.findAvailableByProperty( DataProvider.STB_PROP_MANUFACTURER, values );
    }

    @Test
    public void findAvailableByPropertyOneValue()
    {
        String[] values = new String[]
            { testProperties.getPropertyValueOne() };
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( DataProvider.STB_PROP_MANUFACTURER,
                values );

        List< String > settopList = new ArrayList< String >();
        Collections.addAll( settopList, values );

        logResult( settops.size() );

        for ( SettopDesc settop : settops )
        {
            Assert.assertTrue( settopList.contains( settop.getManufacturer() ) );
        }
    }

    @Test
    public void findAvailableByPropertyTwoValue()
    {
        String[] values = dataProvider.getManufacturerList();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( DataProvider.STB_PROP_MANUFACTURER,
                values );

        List< String > settopList = new ArrayList< String >();
        Collections.addAll( settopList, values );

        logResult( settops.size() );

        for ( SettopDesc settop : settops )
        {
            Assert.assertTrue( settopList.contains( settop.getManufacturer() ) );
        }
    }

    @Test
    public void findAvailableByPropertyOffset()
    {
        String[] values = new String[]
            { testProperties.getPropertyValueOne() };

        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( DataProvider.STB_PROP_MANUFACTURER,
                values, offset, count );

        List< String > settopList = new ArrayList< String >();
        Collections.addAll( settopList, values );

        logResult( settops.size() );

        for ( SettopDesc settop : settops )
        {
            Assert.assertTrue( settopList.contains( settop.getManufacturer() ) );
        }
    }
}
