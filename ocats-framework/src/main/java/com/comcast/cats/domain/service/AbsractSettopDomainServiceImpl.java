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

import java.util.List;
import java.util.Map;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;

public abstract class AbsractSettopDomainServiceImpl extends DomainServiceImpl< SettopDesc > implements SettopDomainService
{
    @Override
    public Integer countAllByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Integer countAllByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Integer countAllBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Integer countAvailableByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Integer countAvailableByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public Integer countAvailableBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String arg0, String[] arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup arg0 ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String arg0, String[] arg1 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supportedd in OCATS" );
    }
}
