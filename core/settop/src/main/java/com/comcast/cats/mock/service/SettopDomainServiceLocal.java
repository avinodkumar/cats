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
package com.comcast.cats.mock.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
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
 * Alternative implementation of {@link SettopDomainService} without any
 * dependency with configuration management system. This class should used for
 * testing purpose only.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class SettopDomainServiceLocal extends DomainServiceImpl< SettopDesc > implements SettopDomainService
{
    @Inject
    private DeviceSearchServiceLocal deviceSearchServiceLocal;

    /**
     * to find settop description by mac id.
     * 
     * @param macId
     * @return {@linkplain SettopDesc}
     * @throws SettopNotFoundException
     */
    @Override
    public SettopDesc findByMacId( String macId ) throws SettopNotFoundException
    {
        SettopDesc settop = new SettopDesc();

        try
        {
            JAXBContext jc = JAXBContext.newInstance( SettopDesc.class );
            Unmarshaller um = jc.createUnmarshaller();
            settop = ( SettopDesc ) um.unmarshal( new java.io.FileInputStream( new File( deviceSearchServiceLocal
                    .getSettopFilePath() ) ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return settop;
    }

    /**
     * to find all allocated settop reservations.
     * 
     * @return List of {@linkplain SettopReservationDesc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public List< SettopReservationDesc > findAllAllocated()
    {
        SimpleListWrapper< SettopReservationDesc > simpleListWrapper = null;

        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, SettopReservationDesc.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            simpleListWrapper = ( SimpleListWrapper< SettopReservationDesc > ) unmarshaller.unmarshal( new File(
                    deviceSearchServiceLocal.getAllocatedSettopFilePath() ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return ( null != simpleListWrapper ) ? simpleListWrapper.getList() : null;
    }

    /**
     * to find all available settop reservations.
     * 
     * @return List of {@linkplain SettopReservationDesc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public List< SettopReservationDesc > findAllAvailable()
    {
        SimpleListWrapper< SettopReservationDesc > simpleListWrapper = null;

        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, SettopReservationDesc.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            simpleListWrapper = ( SimpleListWrapper< SettopReservationDesc > ) unmarshaller.unmarshal( new File(
                    deviceSearchServiceLocal.getAvailableSettopFilepath() ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return ( null != simpleListWrapper ) ? simpleListWrapper.getList() : null;

    }

    /**
     * count settop by rack id.
     * 
     * @param arg0
     * @return Integer
     */
    @Override
    public Integer countAllByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * count settop by reservation id.
     * 
     * @param arg0
     * @return Integer
     */
    @Override
    public Integer countAllByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * count settop by settop group id.
     * 
     * @param arg0
     * @return Integer
     */
    @Override
    public Integer countAllBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * count available settop by rack id.
     * 
     * @param arg0
     * @return Integer
     */
    @Override
    public Integer countAvailableByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * count available settop by reservation id.
     * 
     * @param arg0
     * @return Integer
     */
    @Override
    public Integer countAvailableByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * count available settop by group id.
     * 
     * @param arg0
     * @return Integer
     */
    @Override
    public Integer countAvailableBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by criteria.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by criteria.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by property.
     * 
     * @param property
     * @param values
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by property.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByProperty( String arg0, String[] arg1, Integer arg2, Integer arg3 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by rack.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByRack( Rack arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by rack.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByRack( Rack arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by rack id.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by rack id.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByRackId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by reservation.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByReservation( Reservation arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by reservation.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByReservation( Reservation arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by reservation id.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by reservation id.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllByReservationId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by group.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by group.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by group id.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by group id.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAllBySettopGroupId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop by criteria.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop by criteria.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop.
     * 
     * @param arg0
     * @param arg1
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByProperty( String arg0, String[] arg1 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByProperty( String arg0, String[] arg1, Integer arg2, Integer arg3 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByRack( Rack arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByRack( Rack arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByRackId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByRackId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByReservation( Reservation arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByReservationId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableByReservationId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find available settop.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop.
     * 
     * @param arg0
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String arg0 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    /**
     * find all settop.
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return List of {@linkplain SettopDesc}
     */
    @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String arg0, Integer arg1, Integer arg2 )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    @Override
    public SettopDesc findByMacId( String macId, Boolean isShallow ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopReservationDesc > findAllAllocated( Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );

    }

    // @Override
    public List< SettopReservationDesc > findAllAvailable( Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByRackId( String rackId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByRackId( String rackId, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByRack( Rack rack, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllByRack( Rack rack, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
            throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAllBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow ) throws SettopNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByCriteria( Map< String, String > criteria, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByProperty( String property, String[] values, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByReservationId( String reservationId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByRackId( String rackId, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableBySettopGroupId( String settopGroupId, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByReservation( Reservation reservation, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableByRack( Rack rack, Integer offset, Integer count, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    // @Override
    public List< SettopDesc > findAvailableBySettopGroup( SettopGroup settopGroup, Integer offset, Integer count,
            Boolean isShallow )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }

    @Override
    public List< SettopDesc > findByMacIdList( List< String > settopList )
    {
        throw new UnsupportedOperationException( "Not supported in local development environment yet" );
    }
}
