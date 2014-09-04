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
package com.comcast.cats.provider.factory.impl;

import java.net.URI;

import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.RemoteProviderServiceImpl;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.RemoteProviderFactory;
import com.comcast.cats.service.IRService;

/**
 * Provider factory implementation for Remote Web Service
 * 
 * @author SSugun00c
 * 
 * @see RemoteProvider
 * @see IRService
 */
public class RemoteProviderFactoryImpl extends ProviderFactoryImpl< RemoteProvider > implements RemoteProviderFactory
{

    private IRService irService;

    public RemoteProviderFactoryImpl( IRService irService )
    {
        this.irService = irService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteProvider getProvider( SettopInfo settop )
    {
        LOGGER.trace( "Creating RemoteProvider[" + settop.getRemotePath() + "] Type [" + settop.getRemoteType()
                + "] for " + settop.getHostMacAddress() );
        if ( settop.getRemotePath() == null )
        {
            LOGGER.error( "RemoteProvider instance will be NULL.  A remote locator is required." );
            throw new IllegalArgumentException( "Remote Control is required for Settops creation." );
        }
        if ( settop.getRemoteType() == null )
        {
            LOGGER.error( "RemoteProvider instance will be NULL.  A remote type is required." );
            throw new IllegalArgumentException( "Remote Control is required for Settops creation." );
        }
        RemoteProviderServiceImpl remote = new RemoteProviderServiceImpl( irService, settop.getRemotePath(),
                settop.getRemoteType() );
        remote.setParent( settop );
        return remote;
    }

    /**
     * to get Ir service.
     * 
     * @return IRService
     */
    public IRService getIrService()
    {
        return irService;
    }

    /**
     * to get Ir service.
     * 
     * @param irService
     *            {@linkplain IRService}
     */
    public void setIrService( IRService irService )
    {
        this.irService = irService;
    }

    @Override
    public RemoteProvider getProvider( SettopInfo settop, URI string ) throws ProviderCreationException
    {
        throw new UnsupportedOperationException( "getProvider(SettopInfo settop, URI string) for "
                + getClass().getName()
                + " is not implemented. Please use getProvider(SettopInfo settop) constructor instead." );
    }
}