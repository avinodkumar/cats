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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.info.ConfigServiceConstants;

/**
 * Implementation of {@link SettopDomainService}.
 * 
 * @author subinsugunan
 * 
 */
@Named
public class SettopDomainServiceImpl extends AbsractSettopDomainServiceImpl
{
    @Override
    public SettopDesc findByMacId( String macId ) throws SettopNotFoundException
    {
        AssertUtil.isValidMacId( macId, "Invalid macId" );
        SettopDesc settopDesc = getDeviceSearchService().findByMacId( macId );
        settopDesc.setEnvironmentId( ConfigServiceConstants.CATS_VIRTUAL_ENVIRONMENT );
        return settopDesc;
    }

    @Override
    public List< SettopReservationDesc > findAllAvailable()
    {
        List< SettopReservationDesc > settops = Collections.emptyList();
        try
        {
            settops = getDeviceSearchService().findAllAvailableSettopReservationDesc();
        }
        catch ( SettopNotFoundException e )
        {
            logger.error( "Error while invoking findAllAvailableSettopReservationDesc()" + e.getMessage() );
        }
        return settops;

    }

    @Override
    public List< SettopReservationDesc > findAllAllocated()
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopReservationDesc > findAllAllocated( Boolean arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopReservationDesc > findAllAvailable( Boolean arg0 )
    {
        return findAllAvailable();
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String arg0, String[] arg1, Boolean arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String arg0, String[] arg1, Integer arg2, Integer arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String arg0, String[] arg1, Integer arg2, Integer arg3, Boolean arg4 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup arg0, Boolean arg1 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup arg0, Integer arg1, Integer arg2 )
            throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup arg0, Integer arg1, Integer arg2, Boolean arg3 )
            throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > arg0, Integer arg1, Integer arg2,
            Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String arg0, String[] arg1, Boolean arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String arg0, String[] arg1, Integer arg2, Integer arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String arg0, String[] arg1, Integer arg2, Integer arg3,
            Boolean arg4 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String arg0, Boolean arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String arg0, Integer arg1, Integer arg2, Boolean arg3 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public SettopDesc findByMacId( String arg0, Boolean arg1 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findByMacIdList( List< String > arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }
}
