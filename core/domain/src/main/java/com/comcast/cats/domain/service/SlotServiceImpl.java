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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.comcast.cats.domain.Slot;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.SlotNotFoundException;

/**
 * Implementation of {@link SlotService}.
 * 
 * @author ssugun00c
 * 
 */
@Named
public class SlotServiceImpl extends DomainServiceImpl< Slot > implements SlotService
{

    @Override
    public List< Slot > findAllByRackId( String rackId )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + SLOTS + BACK_SLASH + RACK ) + rackId;

        List< Slot > slots = new ArrayList< Slot >();
        try
        {
            slots = getResponseAsDomainList( requestUrl, Slot.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve slot list. " + e.getMessage() );
        }

        return slots;
    }

    @Override
    public List< Slot > findAllByRackName( String rackName )
    {
        String requestUrl = getBaseUrl( MDS + BACK_SLASH + SLOTS + BACK_SLASH + RACK ) + rackName;

        List< Slot > slots = new ArrayList< Slot >();
        try
        {
            slots = getResponseAsDomainList( requestUrl, Slot.class );
        }
        catch ( DomainServiceException e )
        {
            logger.error( "Failed to retrieve slot list. " + e.getMessage() );
        }

        return slots;
    }

    @Override
    public Slot findByMacId( String macId ) throws SlotNotFoundException
    {
        throw new UnsupportedOperationException( "Not supported yet" );
    }
}
