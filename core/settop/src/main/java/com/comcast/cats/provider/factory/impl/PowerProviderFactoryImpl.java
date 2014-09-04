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

import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.PowerProviderServiceImpl;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.PowerProviderFactory;
import com.comcast.cats.service.PowerService;

/**
 * Provider factory implementation for Power Web Service
 * 
 * @author SSugun00c
 * 
 * @see PowerProvider
 * @see PowerService
 */
public class PowerProviderFactoryImpl extends ProviderFactoryImpl< PowerProvider > implements PowerProviderFactory
{
    private PowerService powerService;

    /**
     * Constructor
     * @param powerService {@linkplain PowerService}
     */
    public PowerProviderFactoryImpl( PowerService powerService )
    {
        this.powerService = powerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerProvider getProvider( SettopInfo settop )
    {
        LOGGER.trace( "Creating PowerProvider for [" + settop.getHostMacAddress() + "]" );

        AssertUtil.isNull( settop );
        AssertUtil.isNull( settop.getPowerPath(), "Cannot create PowerProvider. PowerPath is null" );

        PowerProviderServiceImpl power = new PowerProviderServiceImpl( powerService, settop.getPowerPath() );
        power.setParent( settop );

        return power;
    }

    /**
     * To get power service
     * 
     * @return powerService {@linkplain PowerService}
     */
    public PowerService getPowerService()
    {
        return powerService;
    }

    /**
     * To set power service
     * 
     * @param powerService {@linkplain PowerService}
     */
    public void setPowerService( PowerService powerService )
    {
        this.powerService = powerService;
    }

    @Override
    public PowerProvider getProvider( SettopInfo settop, URI string ) throws ProviderCreationException
    {
        throw new UnsupportedOperationException( "getProvider(SettopInfo settop, URI string) for "
                + getClass().getName()
                + " is not implemented. Please use getProvider(SettopInfo settop) constructor instead." );
    }

}
