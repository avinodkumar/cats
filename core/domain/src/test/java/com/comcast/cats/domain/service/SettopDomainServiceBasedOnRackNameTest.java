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
 * Test cases for {@link Rack} name based search APIs in
 * {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnRackNameTest extends BaseSettopDomainServiceTest
{

   

    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackNull()
    {
        settopDomainService.findAllByRack( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackNameNull()
    {
        Rack rack = new Rack();
        rack.setName( null );
        settopDomainService.findAllByRack( rack );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByRackNameEmpty()
    {
        Rack rack = new Rack();
        rack.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAllByRack( rack );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAllByInvalidRackName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        Rack rack = new Rack();
        rack.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByRackName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByRackNameShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllByRackNameShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack ,false);
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByRackNameOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack, offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByRackNameOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack, offset, count,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleByRackNameOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack, offset, count,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackNull()
    {
        settopDomainService.findAvailableByRack( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackNameNull()
    {
        Rack rack = new Rack();
        rack.setName( null );
        settopDomainService.findAvailableByRack( rack );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByRackNameEmpty()
    {
        Rack rack = new Rack();
        rack.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAvailableByRack( rack );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAvailableByInvalidRackName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        Rack rack = new Rack();
        rack.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllByRack( rack );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackNameShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack,true );
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackNameShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackNameOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack, offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackNameOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack, offset, count,true );
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableByRackNameOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        Rack rack = new Rack();
        rack.setName( testProperties.getRackName() );

        List< SettopDesc > settops = settopDomainService.findAvailableByRack( rack, offset, count,false );
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

}
