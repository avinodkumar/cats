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

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.AllocationInstantiationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.service.AllocationService;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Integrations test cases for allocation related APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.my.settop.id         :  Component UUID of the settop in the configuration management system. 
 *        
 *    E.g.
 *    test.my.settop.id         =   4-7aea-4966-87ca-fd98bcacbfe1
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   456
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceAllocationIT extends BaseSettopDomainServiceIT
{
    @Inject
    protected AllocationService allocationService;

    static String               allocationId;
    String                      componentId;

    @Override
    public void setup()
    {
        super.setup();
        componentId = dataProvider.getReservedSettopId();
    }

    @Test
    public void findAllAllocated() throws AllocationInstantiationException, AllocationNotFoundException,
            InterruptedException
    {
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

        Thread.sleep( 1000 );

        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAllocated();
        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );
        Assert.assertTrue( AssertUtil.isOneOrMore( settopDescList.size() ) );

        allocationService.releaseByComponentId( componentId );
    }

    @Test
    public void findAllAvailable()
    {
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAvailable();

        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );

        for ( SettopReservationDesc settopReservationDesc : settopDescList )
        {
            Assert.assertNotNull( settopReservationDesc );
            LOGGER.info( settopReservationDesc.toString() );
        }

        Assert.assertTrue( AssertUtil.isOneOrMore( settopDescList.size() ) );
    }

}
