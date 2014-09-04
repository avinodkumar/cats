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
package com.comcast.cats.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.util.CommonUtil;

/**
 * Test cases for property based search APIs in {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnPropertyTest extends BaseSettopDomainServiceTest
{
    private String                property;
    private String[]              values;
    private Map< String, Object > paramsMap;

    @Before
    public void beforeMethod()
    {
        super.beforeMethod();
        property = DataProvider.STB_PROP_MANUFACTURER;
        values = dataProvider.getManufacturerList();
        paramsMap = new HashMap< String, Object >();
        paramsMap.put( DomainServiceImpl.PROPERTY_NAME, property );
        paramsMap.put( DomainServiceImpl.PROPERTY_VALUE, CommonUtil.arrayToString( values ) );
    }

    /************************* findAllByProperty *****************************************/

    @Test( expected = IllegalArgumentException.class )
    public void findAllByPropertyNull()
    {
        settopDomainService.findAllByProperty( property, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByPropertyEmpty()
    {
        values = new String[] { };
        settopDomainService.findAllByProperty( property, values );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByProperty()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByProperty( property, values );
        Assert.assertNotNull( settops );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByPropertyShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByProperty( property, values,true );
        Assert.assertNotNull( settops );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByPropertyShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByProperty( property, values ,false);
        Assert.assertNotNull( settops );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /************************* findAllByPropertyOffset **********************/

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByPropertyOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByProperty( property, values, offset, count );
        Assert.assertNotNull( settops );
        verifyAll();
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByPropertyOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByProperty( property, values, offset, count,true );
        Assert.assertNotNull( settops );
        verifyAll();

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByPropertyOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByProperty( property, values, offset, count,false );
        Assert.assertNotNull( settops );
        verifyAll();

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /************************************ findAvailableByProperty *****************************************************/

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByPropertyNull()
    {
        settopDomainService.findAvailableByProperty( property, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByPropertyEmpty()
    {
        String[] values = new String[] { };
        settopDomainService.findAvailableByProperty( property, values );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByProperty()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( property, values );
        Assert.assertNotNull( settops );
        verifyAll();
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByPropertyShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( property, values,true );
        Assert.assertNotNull( settops );
        verifyAll();
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByPropertyShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( property, values,false );
        Assert.assertNotNull( settops );
        verifyAll();

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /************************************ findAvailableByPropertyOffset ***********************************************/

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByPropertyOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( property, values, offset, count );
        Assert.assertNotNull( settops );
        verifyAll();
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByPropertyOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( property, values, offset, count ,true);
        Assert.assertNotNull( settops );
        verifyAll();
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByPropertyOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByProperty( property, values, offset, count,false );
        Assert.assertNotNull( settops );
        verifyAll();

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test( expected = RuntimeException.class )
    public void testIllegalStateException()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalStateException() );
        replayAll();
        settopDomainService.findAvailableByProperty( property, values );
        verifyAll();
    }
}
