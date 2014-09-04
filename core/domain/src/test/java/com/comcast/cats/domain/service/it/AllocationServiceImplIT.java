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

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.service.AllocationService;
import com.comcast.cats.domain.test.DataProvider;

/**
 * Integrations test cases for all basic APIs in {@link AllocationService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. The componentId of a reserved {@link SettopDesc} should be provided in 
 *    <b>/src/test/resources/test.props</b> for the key [<b>test.my.settop.id</b>].
 * 
 *    E.g.
 *    test.my.settop.id         =   4417d3fb-ac16-41cb-b50b-203ccdd70476
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   2312
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class AllocationServiceImplIT extends BaseDomainIT
{
    @Inject
    private DataProvider        dataProvider;

    @Inject
    protected AllocationService allocationService;

    private static String       allocationId;

    private String              componentId;

    @Inject
    private CatsProperties      catsProperties;

    @Override
    public void setup()
    {
        super.setup();
        componentId = dataProvider.getReservedSettopId();
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

    @Test
    public void testCreateByComponentIdAuthToken() throws AllocationInstantiationException
    {
        releaseAlloctionIfExists();
        
        Allocation allocation = allocationService.createByComponentId( componentId, dataProvider.getDuration(),
                catsProperties.getAuthToken() );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @Test
    public void testCreateByComponentId() throws AllocationInstantiationException
    {
        releaseAlloctionIfExists();
        
        Allocation allocation = allocationService.createByComponentId( componentId, dataProvider.getDuration() );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @Test
    public void testReacquireByComponentId() throws AllocationInstantiationException
    {
        Allocation allocation = null;
        allocation = allocationService.reacquireByComponentId( componentId );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        System.out.println( allocation.toString() );
        LOGGER.debug( allocation.toString() );
    }

    @Test
    public void testReacquireByComponentIdAfterAllocation() throws AllocationInstantiationException,
            AllocationNotFoundException
    {
        Allocation allocation = allocationService.reacquireByComponentId( componentId );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
        LOGGER.debug( allocation.toString() );
    }

    @Test( expected = AllocationInstantiationException.class )
    public void testContinuousAllocation() throws AllocationInstantiationException, AllocationNotFoundException
    {
        allocationService.createByComponentId( componentId, dataProvider.getDuration() );
    }

    @Test
    public void testFindActive()
    {
        List< Allocation > allocations = allocationService.findActive();
        Assert.assertNotNull( allocations );
    }

    @Test
    public void testFindActiveOffsetCount()
    {
        List< Allocation > allocations = allocationService.findActive( offset, count );
        Assert.assertNotNull( allocations );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testFindByComponentIdNull() throws AllocationNotFoundException
    {
        componentId = null;
        allocationService.findByComponentId( componentId );
    }

    @Test( expected = AllocationNotFoundException.class )
    public void testFindByComponentIdInvalid() throws AllocationNotFoundException
    {
        componentId = DataProvider.INVALID_ID;
        allocationService.findByComponentId( componentId );
    }

    @Test
    public void testFindByComponentId() throws AllocationNotFoundException, InterruptedException
    {
        Thread.sleep( 1000 );
        Allocation allocation = allocationService.findByComponentId( componentId );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        LOGGER.debug( allocation.toString() );
    }

    @Test
    public void testVerifyByAllocationId() throws AllocationNotFoundException
    {
        Assert.assertTrue( allocationService.verify( allocationId ) );
    }

    @Test
    public void testVerifyByAllocationIdAuthToken() throws AllocationNotFoundException
    {
        Assert.assertTrue( allocationService.verify( allocationId, catsProperties.getAuthToken() ) );
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

    @Test
    public void testVerifyByAllocationIdInvalid() throws AllocationNotFoundException
    {
        Assert.assertFalse( allocationService.verify( DataProvider.INVALID_ID ) );
    }

    @Test
    public void testVerifyByAllocationIdInvalidAuthToken() throws AllocationNotFoundException
    {
        Assert.assertFalse( allocationService.verify( DataProvider.INVALID_ID, catsProperties.getAuthToken() ) );
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

    @Test
    public void testUpdateByAllocationId() throws AllocationNotFoundException
    {
        Allocation allocation = allocationService.update( allocationId, dataProvider.getDuration() );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @Test
    public void testUpdateByAllocationIdAuthToken() throws AllocationNotFoundException
    {
        Allocation allocation = allocationService.update( allocationId, dataProvider.getDuration(),
                catsProperties.getAuthToken() );
        Assert.assertNotNull( allocation );
        Assert.assertNotNull( allocation.getComponent() );
        Assert.assertNotNull( allocation.getComponent().getId() );
        Assert.assertEquals( componentId, allocation.getComponent().getId() );
        Assert.assertNotNull( allocation.getId() );
        allocationId = allocation.getId();
        LOGGER.debug( allocation.toString() );
    }

    @Test
    public void testCountActive()
    {
        int count = allocationService.countActive();
        logResult( count );
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
        allocationService.release( allocationId );
    }

    @Test
    public void testReleaseByAllocationIdAuthToken() throws AllocationNotFoundException,
            AllocationInstantiationException
    {
        releaseAlloctionIfExists();
        
        Allocation allocation = allocationService.createByComponentId( componentId, dataProvider.getDuration() );
        allocationService.release( allocation.getId(), catsProperties.getAuthToken() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testReleaseAllocationNull() throws AllocationNotFoundException
    {
        Allocation allocation = null;
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
        Allocation allocation = allocationService.createByComponentId( componentId, dataProvider.getDuration() );
        Assert.assertNotNull( allocation );
        LOGGER.debug( allocation.toString() );

        // Give some time for DB
        Thread.sleep( 1000 );
        allocationService.releaseByComponentId( componentId );
    }

    private void releaseAlloctionIfExists()
    {
        try
        {
            allocationService.releaseByComponentId( componentId );
        }
        catch ( Exception e )
        {
            LOGGER.debug( "Box  is already released" + componentId );
        }
    }
}
