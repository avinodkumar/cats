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
package com.comcast.cats.service.impl;

import java.net.URI;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.IRHardwareEnum;
import com.comcast.cats.service.IRService;
import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.IRServiceProvider;
import com.comcast.cats.service.ir.redrat.RedRatIRServiceFacade;

@Stateless
@TransactionAttribute( TransactionAttributeType.NOT_SUPPORTED )
public class IRServiceFacadeRetriever implements IRServiceProvider
{

    @EJB
    LegacyIRServiceFacade       legacyFacade;

    @EJB
    RedRatIRServiceFacade       redRatFacade;

    private static final Logger logger = LoggerFactory.getLogger( IRServiceFacadeRetriever.class );

    @Override
    public IRService getIRService( URI path )
    {
        IRService irService = null;
        logger.info( "IRServiceFacadeRetriever : path "+path );
        if ( path == null )
        {
            logger.warn( "IRServiceFacadeRetriever : path is null " );
        }
        else if ( !IRHardwareEnum.validate( path.getScheme() ) )
        {
            logger.warn( "IRServiceFacadeRetriever : IRCommunicator type is unknown " );
        }
        else
        {
            IRHardwareEnum type = IRHardwareEnum.getByValue( path.getScheme() );
            logger.trace( "IRServiceFacadeRetriever : IRHardwareEnum type "+type );
            switch ( type )
            {
            case GC100:
            case GC100_12:
            case GC100_6:
            case ITACH:
            case TEST:
                irService = legacyFacade;
                break;
            case IRNETBOXPRO3:
                irService = redRatFacade;
                break;
            default:
                logger.warn( "IRServiceFacadeRetriever : No IRService implementation for '" + path.toString()
                        + "' could be found or created!" );

            }
        }
        return irService;
    }
}
