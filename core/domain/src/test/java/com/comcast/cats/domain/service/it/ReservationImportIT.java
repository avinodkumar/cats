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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.service.DomainService;
import com.comcast.cats.domain.service.ReservationService;

/**
 * 
 * @author subinsugunan
 * 
 */
public class ReservationImportIT extends BaseDomainIT
{

    @Inject
    protected ReservationService reservationService;

    static List< String >        reservationNameList = new ArrayList< String >();
    static Set< String >         reservationNameSet  = new HashSet< String >();

    @Override
    public void setup()
    {
        super.setup();
    }

    @Override
    @Test
    public void testContext()
    {
        super.testContext();
        Assert.assertNotNull( reservationService );
    }

    @Test
    public void testFindActive() throws Exception
    {
        importDomain( reservationService, Reservation.class, true );
    }

    private void importDomain( DomainService< ? extends Domain > domainService, Class< ? extends Domain > clazz,
            boolean activeOnly )
    {
        LOGGER.info( "Importing [" + clazz.getSimpleName() + "] - START" );

        int totalResults = 0;

        if ( activeOnly )
        {
            totalResults = domainService.countActive();
        }
        else
        {
            totalResults = domainService.count();
        }

        int maxBatchSize = 100;
        int totalPages = ( int ) ( Math.ceil( ( double ) totalResults / ( double ) maxBatchSize ) );

        LOGGER.info( "Total records to be imported [" + totalResults + "]" );
        LOGGER.info( "Records will be imported in  [" + totalPages + "] batches with a maximum of [" + maxBatchSize
                + "] per batch" );

        int page = 1;
        int offset = 0;

        List< ? extends Domain > list = null;
        int importCount = 0;

        while ( page <= totalPages )
        {
            int count = getCount( page, totalResults, maxBatchSize );

            LOGGER.info( "Retrieving data [CurrentPage/TotalPages][" + page + "/" + totalPages + "][Offset][" + offset
                    + "][Count][" + count + "]" );

            if ( activeOnly )
            {
                list = domainService.findActive( offset, count );
            }
            else
            {
                list = domainService.find( offset, count );
            }

            if ( ( null != list ) && ( !list.isEmpty() ) )
            {
                LOGGER.info( "Retrieved " + list.size() + " records" );

                for ( Domain domain : list )
                {
                    try
                    {
                        LOGGER.info( "Creating [" + clazz.getSimpleName() + "] [" + ( ++importCount ) + "/"
                                + totalResults + "] [" + domain + "]" );

                        if ( domain instanceof Reservation )
                        {
                            createReservation( ( Reservation ) domain );
                        }
                    }
                    catch ( Exception e )
                    {
                        LOGGER.error( "Failed to create [" + clazz.getSimpleName() + "] [" + ( importCount ) + "/"
                                + totalResults + "] [" + domain + "] [" + e.getMessage() + "]" );
                    }
                }
            }

            page++;
            offset += count;
        }

        LOGGER.info( "Importing [" + clazz.getSimpleName() + "] - END" );
        LOGGER.info( "List Size " + reservationNameList.size() );
        LOGGER.info( "Set Size  " + reservationNameSet.size() );

    }

    private void createReservation( Reservation reservation )
    {
        LOGGER.info( reservation.getName() );

        reservationNameList.add( reservation.getName() );
        reservationNameSet.add( reservation.getName() );
    }

}
