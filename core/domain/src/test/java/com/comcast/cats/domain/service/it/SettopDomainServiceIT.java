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

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.test.DataProvider;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Integrations test cases for all basic APIs in {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.mac.id                 :   MacId of an available {@link SettopDesc} in the configuration management system. 
 *        
 *    E.g.
 *    test.mac.id                 =   12:BE:20:AF:7F:D0
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   74
 *   
 *  4. Use CATSVision and allocate the settop before you execute the test
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdNull()
    {
        try
        {
            settopDomainService.findByMacId( null );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByMacIdEmpty()
    {
        try
        {
            settopDomainService.findByMacId( DataProvider.EMPTY_STRING );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = SettopNotFoundException.class )
    public void findByInvalidMacId() throws SettopNotFoundException
    {
        settopDomainService.findByMacId( DataProvider.INVALID_MAC_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByInvalidMacIdSyntax()
    {
        try
        {
            settopDomainService.findByMacId( DataProvider.INVALID_MAC_ID_FORMAT );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void findByMacId()
    {
        try
        {
            SettopDesc settopDesc = settopDomainService.findByMacId( "00:19:47:25:B6:1E" );
            Assert.assertNotNull( settopDesc );
           // Assert.assertEquals( testProperties.getMacId(), settopDesc.getHostMacAddress() );
            Assert.assertNotNull( settopDesc.getEnvironmentId() );
            LOGGER.debug( settopDesc.toString() );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void findAllAllocated()
    {
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAllocated();
        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );
        Assert.assertTrue( "Make sure you have atleast 1 settop allocated",
                AssertUtil.isOneOrMore( settopDescList.size() ) );
    }

    @Test
    public void findAllAvailable()
    {
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAvailable();
        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );
        Assert.assertTrue( "Make sure you have atleast 1 settop available",
                AssertUtil.isOneOrMore( settopDescList.size() ) );
    }
}
