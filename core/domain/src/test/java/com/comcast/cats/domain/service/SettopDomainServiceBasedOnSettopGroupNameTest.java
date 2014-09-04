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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for {@link SettopGroup} name based search APIs in
 * {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnSettopGroupNameTest extends BaseSettopDomainServiceTest
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupNull() throws SettopNotFoundException
    {
        settopDomainService.findAllBySettopGroup( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupNameNull() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( null );
        settopDomainService.findAllBySettopGroup( settopGroup );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllBySettopGroupNameEmpty() throws SettopNotFoundException
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAllBySettopGroup( settopGroup );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAllByInvalidSettopGroupName() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllBySettopGroupName() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllBySettopGroupNameShallowTrue() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList(true) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAllBySettopGroupNameShallowFlase() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleBySettopGroupNameOffset() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup, offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleBySettopGroupNameOffsetShallowTrue() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup, offset, count,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAlleBySettopGroupNameOffsetShallowFalse() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup, offset, count,false );
        logResult( settops.size() );

        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupNull() throws SettopNotFoundException
    {
        settopDomainService.findAvailableBySettopGroup( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupNameNull()
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( null );
        settopDomainService.findAvailableBySettopGroup( settopGroup );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableBySettopGroupNameEmpty()
    {
        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.EMPTY_STRING );
        settopDomainService.findAvailableBySettopGroup( settopGroup );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test( expected = RuntimeException.class )
    public void findAvailableByInvalidSettopGroupName() throws SettopNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new IllegalSelectorException() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( DataProvider.INVALID_NAME );

        List< SettopDesc > settops = settopDomainService.findAllBySettopGroup( settopGroup );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableBySettopGroupName()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableBySettopGroupNameShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup,true );
        logResult( settops.size() );

        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableBySettopGroupNameShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup,false );
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableBySettopGroupNameOffset()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList() );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup, offset, count );
        logResult( settops.size() );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableBySettopGroupNameOffsetShallowTrue()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( true ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup, offset, count,true );
        logResult( settops.size() );
        Assert.assertNull( "shallow object contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void findAvailableBySettopGroupNameOffsetShallowFalse()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getSettopDescList( false ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup, offset, count,false );
        logResult( settops.size() );
        Assert.assertNotNull( " object does not  contains hardware info",
                settops.get( 0 ).getHardwareInterfaceByType( HardwarePurpose.POWER ) );

    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    @Test
    public void testgetResponseAsDomainListException()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();

        SettopGroup settopGroup = new SettopGroup();
        settopGroup.setName( testProperties.getSettopGroupName() );

        List< SettopDesc > settops = settopDomainService.findAvailableBySettopGroup( settopGroup, offset, count );
        Assert.assertEquals( 0, settops.size() );
    }
}
