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

import static com.comcast.cats.domain.test.DataProvider.FALSE;
import static com.comcast.cats.domain.test.DataProvider.TRUE;

import java.util.List;

import javax.inject.Inject;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Test cases for all basic APIs in {@link AllocationService}.
 * 
 * @author subinsugunan
 * 
 */
public class AllocationServiceImplTest extends BaseTestCase
{
    @Inject
    DataProvider                    dataProvider;

    @Inject
    protected AllocationServiceImpl allocationService;

    static String                   allocationId;
    String                          componentId;

    @Before
    public void beforeMethod()
    {
        super.beforeMethod();
        componentId = dataProvider.getReservedSettopId();
        allocationService.setRestTemplate( restTemplateMock );
    }

    @Override
    @Test
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( allocationService );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCreateByComponentIdNull() throws AllocationInstantiationException
    {
        allocationService.createByComponentId( null, 0 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCreateByComponentIdEmpty() throws AllocationInstantiationException
    {
        allocationService.createByComponentId( DataProvider.EMPTY_STRING, 0 );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testCreateByComponentId() throws AllocationInstantiationException
    {
        EasyMock.expect(
                restTemplateMock.postForObject( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull(),
                        ( Class ) EasyMock.notNull() ) ).andReturn( dataProvider.getAllocation( componentId ) );
        replayAll();
        Allocation allocation = allocationService.createByComponentId( componentId, dataProvider.getDuration() );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testReacquireByComponentId() throws AllocationInstantiationException
    {
        EasyMock.expect(
                restTemplateMock.postForObject( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull(),
                        ( Class ) EasyMock.notNull() ) ).andReturn( dataProvider.getAllocation( componentId ) );
        replayAll();
        Allocation allocation = allocationService.reacquireByComponentId( componentId );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test( expected = AllocationInstantiationException.class )
    public void testCreateByComponentIdException() throws AllocationInstantiationException
    {
        EasyMock.expect(
                restTemplateMock.postForObject( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull(),
                        ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        allocationService.createByComponentId( componentId, dataProvider.getDuration() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test( expected = AllocationInstantiationException.class )
    public void testReacquireByComponentIdException() throws AllocationInstantiationException
    {
        EasyMock.expect(
                restTemplateMock.postForObject( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull(),
                        ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        allocationService.reacquireByComponentId( componentId );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testFindActive()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getAllocationList() );
        replayAll();
        List< Allocation > allocations = allocationService.findActive();
        Assert.assertNotNull( allocations );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testFindActiveOffsetCount()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getAllocationList() );
        replayAll();
        List< Allocation > allocations = allocationService.findActive( offset, count );
        Assert.assertNotNull( allocations );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testFindActiveException()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        List< Allocation > allocations = allocationService.findActive();
        Assert.assertEquals( 0, allocations.size() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testFindByComponentIdNull() throws AllocationNotFoundException
    {
        componentId = null;
        allocationService.findByComponentId( componentId );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testFindByComponentId() throws AllocationNotFoundException, InterruptedException
    {
        Thread.sleep( 1000 );
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getAllocation( componentId ) );
        replayAll();
        Allocation allocation = allocationService.findByComponentId( componentId );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        LOGGER.debug( allocation.toString() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test( expected = AllocationNotFoundException.class )
    public void testFindByComponentIdExeption() throws AllocationNotFoundException, InterruptedException
    {
        Thread.sleep( 1000 );
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        allocationService.findByComponentId( componentId );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testVerifyByAllocationId() throws AllocationNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( TRUE );
        replayAll();
        Assert.assertTrue( allocationService.verify( allocationId ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testVerifyByAllocationIdNull() throws AllocationNotFoundException
    {
        allocationService.verify( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testVerifyByAllocationIdEmpty() throws AllocationNotFoundException
    {
        allocationService.verify( DataProvider.EMPTY_STRING );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testVerifyByAllocationIdInvalid() throws AllocationNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( FALSE );
        replayAll();
        Assert.assertFalse( allocationService.verify( DataProvider.INVALID_ID ) );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testVerifyException() throws AllocationNotFoundException
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        Assert.assertFalse( allocationService.verify( DataProvider.INVALID_ID ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testUpdateByAllocationIdNull() throws AllocationNotFoundException
    {
        allocationService.update( null, 0 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testUpdateByAllocationIdEmpty() throws AllocationNotFoundException
    {
        allocationService.update( DataProvider.EMPTY_STRING, 0 );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testUpdateByAllocationId() throws AllocationNotFoundException
    {
        EasyMock.expect(
                restTemplateMock.postForObject( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull(),
                        ( Class ) EasyMock.notNull() ) ).andReturn( dataProvider.getAllocation( componentId ) );
        replayAll();
        Allocation allocation = allocationService.update( allocationId, dataProvider.getDuration() );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test( expected = AllocationNotFoundException.class )
    public void testUpdateByAllocationIdException() throws AllocationNotFoundException
    {
        EasyMock.expect(
                restTemplateMock.postForObject( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull(),
                        ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        allocationService.update( allocationId, dataProvider.getDuration() );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testCountActive()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andReturn( dataProvider.getCountAsString() );
        replayAll();
        int count = allocationService.countActive();
        logResult( count );
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    @Test
    public void testCountActiveException()
    {
        EasyMock.expect( restTemplateMock.getForObject( ( String ) EasyMock.notNull(), ( Class ) EasyMock.notNull() ) )
                .andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        int count = allocationService.countActive();
        Assert.assertEquals( 0, count );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseByAllocationIdNull() throws AllocationNotFoundException
    {
        String allocationId = null;
        allocationService.release( allocationId );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseByAllocationIdEmpty() throws AllocationNotFoundException
    {
        String allocationId = DataProvider.EMPTY_STRING;
        allocationService.release( allocationId );
    }

    @Test
    public void testReleaseByAllocationId() throws AllocationNotFoundException
    {
        restTemplateMock.put( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull() );
        EasyMock.expectLastCall();
        replayAll();
        allocationService.release( allocationId );
    }

    @Ignore
    @Test( expected = AllocationNotFoundException.class )
    public void testReleaseByAllocationIdFail() throws AllocationNotFoundException
    {
        restTemplateMock.put( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull() );
        EasyMock.expectLastCall().andThrow( new DomainServiceException() );
        replayAll();
        allocationService.release( allocationId );
    }

    @Ignore
    @Test( expected = AllocationNotFoundException.class )
    public void testReleaseByAllocationIdException() throws AllocationNotFoundException
    {
        restTemplateMock.put( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull() );
        EasyMock.expectLastCall().andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        allocationService.release( allocationId );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseAllocationNull() throws AllocationNotFoundException
    {
        Allocation allocation = null;
        allocationService.release( allocation );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseAllocationEmpty() throws AllocationNotFoundException
    {
        Allocation allocation = new Allocation();
        allocation.setId( DataProvider.EMPTY_STRING );
        allocationService.release( allocation );
    }

    @Ignore
    @Test( expected = AllocationNotFoundException.class )
    public void testReleaseAllocationException() throws AllocationNotFoundException
    {
        restTemplateMock.put( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull() );
        EasyMock.expectLastCall().andThrow( new HttpClientErrorException( HttpStatus.NOT_FOUND ) );
        replayAll();
        Allocation allocation = dataProvider.getAllocation( DataProvider.TEST_ID );
        allocationService.release( allocation );
    }

    @Test
    public void testReleaseAllocation() throws AllocationNotFoundException
    {
        restTemplateMock.put( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull() );
        EasyMock.expectLastCall();
        replayAll();
        Allocation allocation = dataProvider.getAllocation( DataProvider.TEST_ID );
        allocationService.release( allocation );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseComponentIdNull() throws AllocationNotFoundException
    {
        String componentId = null;
        allocationService.releaseByComponentId( componentId );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseComponentIdEmpty() throws AllocationNotFoundException
    {
        String componentId = DataProvider.EMPTY_STRING;
        allocationService.releaseByComponentId( componentId );
    }

    @Test
    public void testReleaseComponentId() throws AllocationNotFoundException, AllocationInstantiationException,
            InterruptedException
    {
        restTemplateMock.put( ( String ) EasyMock.notNull(), ( Object ) EasyMock.isNull() );
        EasyMock.expectLastCall();
        replayAll();
        allocationService.releaseByComponentId( componentId );
    }
}
