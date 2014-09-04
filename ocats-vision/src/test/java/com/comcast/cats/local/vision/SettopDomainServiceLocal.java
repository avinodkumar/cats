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
package com.comcast.cats.local.vision;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.DomainServiceImpl;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.domain.util.SimpleListWrapper;

/**
 * 
 * @author subinsugunan
 * 
 */
@Named
public class SettopDomainServiceLocal extends DomainServiceImpl< SettopDesc > implements SettopDomainService
{

    private File FILE_PATH_SETTOP           = new File( "src/test/resources/settop.xml" );
    private File FILE_PATH_SETTOP_ALLOCATED = new File( "src/test/resources/settop-allocated.xml" );
    private File FILE_PATH_SETTOP_AVAILABLE = new File( "src/test/resources/settop_available.xml" );

    @Override
    public SettopDesc findByMacId( String macId ) throws SettopNotFoundException
    {
        SettopDesc settop = new SettopDesc();

        try
        {
            JAXBContext jc = JAXBContext.newInstance( SettopDesc.class );
            Unmarshaller um = jc.createUnmarshaller();
            settop = ( SettopDesc ) um.unmarshal( new java.io.FileInputStream( FILE_PATH_SETTOP ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return settop;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public List< SettopReservationDesc > findAllAllocated()
    {
        SimpleListWrapper< SettopReservationDesc > simpleListWrapper = null;

        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, SettopReservationDesc.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            simpleListWrapper = ( SimpleListWrapper< SettopReservationDesc > ) unmarshaller
                    .unmarshal( FILE_PATH_SETTOP_ALLOCATED );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return simpleListWrapper.getList();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public List< SettopReservationDesc > findAllAvailable()
    {
        SimpleListWrapper< SettopReservationDesc > simpleListWrapper = null;

        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, SettopReservationDesc.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            simpleListWrapper = ( SimpleListWrapper< SettopReservationDesc > ) unmarshaller
                    .unmarshal( FILE_PATH_SETTOP_AVAILABLE );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return simpleListWrapper.getList();
    }

    @Override
    public Integer countAllByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public Integer countAllByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public Integer countAllBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public Integer countAvailableByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public Integer countAvailableByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public Integer countAvailableBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public SettopDesc findByMacId( String macId, Boolean isShallow ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findByMacIdList( List< String > macIds )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopReservationDesc > findAllAllocated( Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopReservationDesc > findAllAvailable( Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String rackId )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String rackId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack rack )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack rack, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
            throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count )
            throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }

    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in test environment" );
    }
}
