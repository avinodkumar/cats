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
package com.comcast.cat.domain.service.it;

import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Integrations test cases for all basic APIs in {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceIT extends BaseDomainIT
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
            settopDomainService.findByMacId( EMPTY_STRING );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = SOAPFaultException.class )
    public void findByInvalidMacId() throws SettopNotFoundException
    {
        settopDomainService.findByMacId( INVALID_MAC_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findByInvalidMacIdSyntax()
    {
        try
        {
            settopDomainService.findByMacId( INVALID_MAC_ID_FORMAT );
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
            SettopDesc settopDesc = settopDomainService.findByMacId( MAC_ID );
            Assert.assertNotNull( settopDesc );
            Assert.assertEquals( MAC_ID, settopDesc.getHostMacAddress() );
            Assert.assertNotNull( settopDesc.getEnvironmentId() );
            LOGGER.debug( settopDesc );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = UnsupportedOperationException.class )
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

        for ( SettopReservationDesc temp : settopDescList )
        {
            Assert.assertTrue( "The object received is not an instance of SettopReservationDesc",
                    ( temp instanceof SettopReservationDesc ) );
        }

        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );
        Assert.assertTrue( "Make sure you have atleast 1 settop available",
                AssertUtil.isOneOrMore( settopDescList.size() ) );
    }
}
