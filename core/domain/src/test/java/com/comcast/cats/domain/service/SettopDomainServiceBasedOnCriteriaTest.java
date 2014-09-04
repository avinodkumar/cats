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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.SettopDesc;

/**
 * Test cases for criteria based search APIs in {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnCriteriaTest extends BaseSettopDomainServiceTest
{
    private Map< String, String > criteriaMap;

    @Before
    public void beforeMethod()
    {
        super.beforeMethod();
        criteriaMap = new LinkedHashMap< String, String >();
        criteriaMap.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );

    }

    /********************* findAllByCriteria ****************************************/

    @Test( expected = IllegalArgumentException.class )
    public void findAllByCriteriaNull()
    {
        settopDomainService.findAllByCriteria( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByCriteriaEmpty()
    {
        Map< String, String > criteria = Collections.emptyMap();
        settopDomainService.findAllByCriteria( criteria);
    }
    
    @Test( expected = IllegalArgumentException.class )
    public void findAllByCriteriaEmptyShallowTrue()
    {
        Map< String, String > criteria = Collections.emptyMap();
        settopDomainService.findAllByCriteria( criteria,true);
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByCriteria()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteriaMap );
        Assert.assertNotNull( settops );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByCriteriaShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteriaMap,true );
        Assert.assertNotNull( settops );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByCriteriaShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteriaMap,false );
        Assert.assertNotNull( settops );

        Assert.assertNotNull( "object does not contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /********************* findAllByCriteriaOffset **************************************/

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByCriteriaOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteriaMap, offset, count );
        Assert.assertNotNull( settops );
        verifyAll();
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByCriteriaOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteriaMap, offset, count,true);
        Assert.assertNotNull( settops );
        verifyAll();
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAllByCriteriaOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList(false) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteriaMap, offset, count,false );
        Assert.assertNotNull( settops );
        verifyAll();
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /********************** findAvailableByCriteria ***********************************/

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByCriteriaNull()
    {
        settopDomainService.findAvailableByCriteria( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByCriteriaEmpty()
    {
        Map< String, String > criteria = Collections.emptyMap();
        settopDomainService.findAvailableByCriteria( criteria );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByCriteria()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteriaMap );

        Assert.assertNotNull( settops );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByCriteriaShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteriaMap, true );
        Assert.assertNotNull( settops );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByCriteriaShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteriaMap,false);
        Assert.assertNotNull( settops );

        Assert.assertNotNull( "object does not contains Hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /********************** findAvailableByCriteriaoffset *****************************/
    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByCriteriaOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteriaMap, offset, count );

        Assert.assertNotNull( settops );
        verifyAll();

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByCriteriaOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteriaMap, offset, count,true );

        Assert.assertNotNull( settops );
        verifyAll();

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void findAvailableByCriteriaOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();
        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteriaMap, offset, count,false );

        Assert.assertNotNull( settops );
        verifyAll();

        Assert.assertNotNull( "shallow object does not contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

}
