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

import java.nio.channels.IllegalSelectorException;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for {@link Rack} id based search APIs in
 * {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnRackIdTest extends BaseSettopDomainServiceTest

{

    /******************** findAllByRackId ******************************/

    public void findAllByRackIdNull()
    {
        settopDomainService.findAllByRackId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackIdEmpty()
    {
        settopDomainService.findAllByRackId( "" );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAllByInvalidRackId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( DataProvider.INVALID_ID );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByRackId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( testProperties.getRackId() );
        logResult( settops.size() );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByRackIdShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( testProperties.getRackId(),true);
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByRackIdShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( testProperties.getRackId(),false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /**************************************** findAllByRackIdOffset ****************************/

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByRackIdOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( testProperties.getRackId(), offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByRackIdOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( testProperties.getRackId(), offset, count ,true);
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByRackIdOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( testProperties.getRackId(), offset, count,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /************************ findAvailableByRackId *****************************************/

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackIdNull()
    {
        settopDomainService.findAvailableByRackId( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackIdEmpty()
    {
        settopDomainService.findAvailableByRackId( "" );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAvailableByInvalidRackId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAllByRackId( DataProvider.INVALID_ID );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackId()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByRackId( testProperties.getRackId() );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackIdShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByRackId( testProperties.getRackId(),true );
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackIdShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByRackId( testProperties.getRackId(),false);
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    /************************ findAvailableByRackIdOffset *****************************************/

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackIdOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByRackId( testProperties.getRackId(), offset,
                count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackIdOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByRackId( testProperties.getRackId(), offset,
                count,true );
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackIdOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        List< SettopDesc > settops = settopDomainService.findAvailableByRackId( testProperties.getRackId(), offset,
                count,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

}
