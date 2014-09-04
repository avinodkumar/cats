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
package com.comcast.cats.domain.service.it.cdi;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.service.AllocationService;

/**
 * Integrations test for {@link AllocationService} using Weld.
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
 *   cats.user.authToken        =   8520
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class AllocationServiceIT extends BaseDomainIT
{
    private AllocationService allocationService;
    private static String     allocationId;
    private String            componentId;
	

    @Override
    public void setup()
    {
        super.setup();
        allocationService = container().select( AllocationService.class ).get();
        componentId = dataProvider.getReservedSettopId();
    }

    @Override
    @Test
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( allocationService );
    }

    @Test
    public void testAllocation() throws Exception
    {
        /*
         * 1. Create allocation by componentId 2. Verify allocation by
         * allocationId - true 3. Get user information 4. Find active
         * allocations for the current User 5. Count active allocations for the
         * current User 6. Find an allocation by componentId 7. Update
         * allocation by allocationId 8. Verify allocation by allocationId -
         * true 9. Release allocation by allocationId 10. Verify allocation by
         * allocationId - false
         */

        // 1. CREATE BY COMPONENT ID
        LOGGER.info( "Creating new allocation for componentId [" + componentId + "]" );
        Allocation createdAllocation = allocationService.createByComponentId( componentId, dataProvider.getDuration() );
        Assert.assertNotNull( createdAllocation );
        Assert.assertEquals( componentId, createdAllocation.getComponent().getId() );
        Assert.assertNotNull( createdAllocation.getId() );
        // Set allocationId globally
        allocationId = createdAllocation.getId();
        LOGGER.info( "Allocation created " + createdAllocation );

        // 2. VERIFY BY ALLOCATION ID
        LOGGER.info( "Verifying allocation by allocationId [" + allocationId + "]" );
        Assert.assertTrue( allocationService.verify( allocationId ) );

        // 3. GET USER
        // User user = userService.findUser();
        // Assert.assertNotNull( user );
        // Assert.assertNotNull( user.getFirstName() );

        // 4. FIND ACTIVE ALLOCATIONS
        LOGGER.info( "Searching active allocations for current user" );
        List< Allocation > allocations = allocationService.findActive();
        Assert.assertNotNull( allocations );
        LOGGER.info( allocations.size() + " allocation found with status 'Active'" );
        for ( Allocation tempAllocation : allocations )
        {
            LOGGER.info( tempAllocation.toString() );
        }

        // 5. COUNT ACTIVE ALLOCATIONS
        LOGGER.info( "Counting active allocations for current user" );
        int count = allocationService.countActive();
        LOGGER.info( "Active allocation count=" + count );
        Assert.assertEquals( allocations.size(), count );

        // 6. FIND BY COMPONENT ID
        LOGGER.info( "Searching allocation for componentId [" + componentId + "]" );
        Allocation retrievedAllocation = allocationService.findByComponentId( componentId );
        Assert.assertNotNull( retrievedAllocation );
        Assert.assertEquals( componentId, retrievedAllocation.getComponent().getId() );
        Assert.assertEquals( createdAllocation.getId(), retrievedAllocation.getId() );
        LOGGER.info( "Allocation retrieved: " + retrievedAllocation );
        Assert.assertEquals( createdAllocation.getId(), retrievedAllocation.getId() );

        // 7. UPDATE BY ALLOCATION ID
        LOGGER.info( "Updating allocation with allocationId [" + allocationId + "] for " + dataProvider.getDuration()
                + " mins" );
        Allocation updatedAllocation = allocationService.update( allocationId, dataProvider.getDuration() );
        Assert.assertNotNull( updatedAllocation );
        Assert.assertEquals( createdAllocation.getId(), updatedAllocation.getId() );
        LOGGER.info( "Allocation updated: " + updatedAllocation );

        // 8. VERIFY BY ALLOCATION ID
        LOGGER.info( "Verifying allocation by allocationId [" + allocationId + "]" );
        Assert.assertTrue( allocationService.verify( allocationId ) );

        // 9. RELEASE BY ALLOCATION ID
        LOGGER.info( "Releasing allocation by allocationId [" + allocationId + "]" );
        allocationService.release( allocationId );

        // 10. VERIFY BY ALLOCATION ID
        LOGGER.info( "Verifying allocation by allocationId [" + allocationId + "]" );
        Assert.assertFalse( allocationService.verify( allocationId ) );
    }

    @Test
    public void testReleaseComponentId() throws AllocationNotFoundException, AllocationInstantiationException,
            InterruptedException
    {
        /*
         * 1. Create allocation by componentId 2. Verify allocation by
         * allocationId - true 3. Release allocation by componentId 4. Verify
         * allocation by allocationId - false
         */

        // 1. CREATE BY COMPONENT ID
        LOGGER.info( "Creating new allocation for componentId [" + componentId + "]" );
        Allocation createdAllocation = allocationService.createByComponentId( componentId, dataProvider.getDuration() );
        Assert.assertNotNull( createdAllocation );
        Assert.assertEquals( componentId, createdAllocation.getComponent().getId() );
        Assert.assertNotNull( createdAllocation.getId() );
        // Set allocationId globally
        allocationId = createdAllocation.getId();
        LOGGER.info( "Allocation created " + createdAllocation );

        // 2. VERIFY BY ALLOCATION ID
        LOGGER.info( "Verifying allocation by allocationId [" + allocationId + "]" );
        Assert.assertTrue( allocationService.verify( allocationId ) );

        // 3. RELEASE BY ALLOCATION ID
        LOGGER.info( "Releasing allocation by componentId [" + componentId + "]" );
        // Give some time for DB
        Thread.sleep( 1000 );
        allocationService.releaseByComponentId( componentId );

        // 4. VERIFY BY ALLOCATION ID
        LOGGER.info( "Verifying allocation by allocationId [" + allocationId + "]" );
        Assert.assertFalse( allocationService.verify( allocationId ) );
    }

}
