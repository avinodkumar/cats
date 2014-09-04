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
package com.comcast.cats.service.settop;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.service.SettopToken;

/**
 * Keeps the information about allocated settops in memory.
 * 
 * @author cfrede001
 * 
 */
public interface SettopCatalog
{
    /**
     * Retrieve the {@link SettopToken} based on the user token and mac. This
     * will also add the {@link Settop} to a memory map for quick lookup in
     * future.
     * 
     * @param mac
     * @param userToken
     * @return
     * @throws SettopNotFoundException
     */
    SettopToken obtainSettopByMac( String mac, String userToken ) throws SettopNotFoundException;

    /**
     * Retrieve the {@link Settop} object from memory based on the user token.
     * 
     * @param token
     * @return
     * @throws SettopNotFoundException
     */
    Settop lookupSettop( SettopToken token ) throws SettopNotFoundException;

    /**
     * Removes the {@link Settop} from memory map.
     * 
     * @param token
     * @throws SettopNotFoundException
     */
    void removeSettop( SettopToken token ) throws SettopNotFoundException;

    /**
     * Retrieve the last error message occurred against a {@link Settop}.
     * 
     * @param token
     * @return
     * @throws SettopNotFoundException
     */
    String getLastError( SettopToken token ) throws SettopNotFoundException;

    /**
     * Adds the error message occurred against a {@link Settop} toa a memory
     * map.
     * 
     * @param token
     * @param errorMsg
     */
    void putSettopError( String token, String errorMsg );
}