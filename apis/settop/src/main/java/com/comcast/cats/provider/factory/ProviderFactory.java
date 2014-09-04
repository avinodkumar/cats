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
package com.comcast.cats.provider.factory;

import java.net.URI;

import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.exceptions.ProviderCreationException;

/**
 * Base interface for all service provider factories
 * 
 * @author Ssugun00c
 * 
 * @param <ProviderType>
 */
public interface ProviderFactory< ProviderType >
{
    /**
     * to get the provider.
     * 
     * @param settop
     * @return ProviderType
     */
    ProviderType getProvider( SettopInfo settop ) throws ProviderCreationException;

    ProviderType getProvider( SettopInfo settop, URI string ) throws ProviderCreationException;
}
