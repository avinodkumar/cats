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

import org.junit.Ignore;
import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.util.AssertUtil;

/**
 * Integrations test cases for profiling {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 *   You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   8520
 * 
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceProfilingIT extends BaseSettopDomainServiceIT
{
    private String macId     = "12:BE:20:AF:7F:D0";


    @Test
    public void findByMacId()
    {
        long start = System.currentTimeMillis();

        LOGGER.info( "First getSettop Request" );
        start = System.currentTimeMillis();
        findByMacId( macId );
        logElaspedTime( start );

        LOGGER.info( "------------------------------" );
        LOGGER.info( "Second getSettop Request" );
        start = System.currentTimeMillis();
        findByMacId( macId );
        logElaspedTime( start );

        LOGGER.info( "------------------------------" );
        LOGGER.info( "Third getSettop Request" );
        start = System.currentTimeMillis();
        findByMacId( macId );
        logElaspedTime( start );
        LOGGER.info( "------------------------------" );

    }

    private void logElaspedTime( long start )
    {
        long elapsed = System.currentTimeMillis() - start;
        LOGGER.info( "Took " + elapsed + " milli seconds [" + ( elapsed / 1000f ) + " second(s)]" );
    }

    private void findByMacId( String mac )
    {
        try
        {
            SettopDesc settopDesc = settopDomainService.findByMacId( mac );
            Assert.assertNotNull( settopDesc );
            LOGGER.info( settopDesc.toString() );
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    @Ignore
    public void findAllAllocated()
    {
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAllocated();
        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );
        Assert.assertTrue( "Make sure you have atleast 1 settop allocated",
                AssertUtil.isOneOrMore( settopDescList.size() ) );
    }

    @Test
    @Ignore
    public void findAllAvailable()
    {
        List< SettopReservationDesc > settopDescList = settopDomainService.findAllAvailable();
        Assert.assertNotNull( settopDescList );
        logResult( settopDescList.size() );
        Assert.assertTrue( "Make sure you have atleast 1 settop available",
                AssertUtil.isOneOrMore( settopDescList.size() ) );
    }
}
