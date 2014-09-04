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
package com.comcast.cats;

import java.util.List;
import java.util.Map;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * Factory interface for {@link Settop} creation
 * 
 * @author cfrede001
 * 
 */
public interface SettopFactory
{
    /**
     * Finds a settop based on the mac address.
     * 
     * 
     * @param mac
     * @return Settop
     * @throws SettopNotFoundException
     */
    Settop findSettopByHostMac( String mac ) throws SettopNotFoundException;

    /**
     * Finds a settop based on the mac address and based on the flag given
     * allocates the settop.
     * 
     * @param mac
     * @param isAllocationRequired
     *            - If set to 'true', the settop will be locked before
     *            returning.
     * @return Settop
     * @throws SettopNotFoundException
     * @throws AllocationException
     */
    Settop findSettopByHostMac( String mac, boolean isAllocationRequired ) throws SettopNotFoundException,
            AllocationException;

    /**
     * Finds a reserved settop based on the unit address.
     * 
     * @param unitAddress
     * @return Settop
     * @throws SettopNotFoundException
     */
    Settop findSettopByUnitAddress( String unitAddress ) throws SettopNotFoundException;

    /**
     * Finds a reserved settop based on the ip address.
     * 
     * @param ip
     * @return Settop
     * @throws SettopNotFoundException
     */
    Settop findSettopByHostIpAddress( String ip ) throws SettopNotFoundException;

    /**
     * Finds a list of reserved settops based on the remote type.
     * 
     * @param remoteType
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllSettopByRemoteType( String remoteType ) throws SettopNotFoundException;

    /**
     * Finds a list of reserved settops based on the model.
     * 
     * @param model
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllSettopByModel( String model ) throws SettopNotFoundException;

    /**
     * Return all allocated {@link SettopDesc} for a given user.
     * 
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllAllocatedSettop() throws SettopNotFoundException;

    /**
     * Return all available {@link SettopDesc} for a given user.
     * 
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllAvailableSettop() throws SettopNotFoundException;

    /**
     * Finds a list of reserved settops based on the propery and value.
     * 
     * @param property
     * @param value
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllSettopByPropertyValue( String property, String value ) throws SettopNotFoundException;

    /**
     * Finds a list of reserved settops based on the propery and list of values.
     * 
     * @param property
     * @param values
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllSettopByPropertyValues( String property, String[] values ) throws SettopNotFoundException;

    /**
     * Finds a list of reserved settops based on the criteria specified.
     * 
     * @param criteria
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllSettopByCriteria( Map< String, String > criteria ) throws SettopNotFoundException;

    /**
     * Finds Settops for the given list of mac ids
     * 
     * @param macIdList
     *            of mac ids
     * @param failOnCreateError
     *            true to throw exceptions while creating settops, false to
     *            ignore exceptions
     * @return list of settops
     * @throws SettopNotFoundException
     */
    List< Settop > findSettopByHostMac( List< String > macIdList, boolean failOnCreateError )
            throws SettopNotFoundException;

    /**
     * Finds Settops for the given list of mac ids.
     * 
     * @param macIdList
     *            of mac ids
     * @return list of settops
     * @throws SettopNotFoundException
     */
    List< Settop > findSettopByHostMac( List< String > macIdList ) throws SettopNotFoundException;

    /**
     * Return all {@link Settop} for a given {@link SettopGroup} name
     * 
     * @param settopGroupName
     *            - Settop Group name.
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAllSettopByGroupName( String settopGroupName ) throws SettopNotFoundException;

    /**
     * Return available {@link Settop} for a logged in user for a given
     * {@link SettopGroup} name
     * 
     * @param settopGroupName
     *            - Settop Group name.
     * @return List of Settops
     * @throws SettopNotFoundException
     */
    List< Settop > findAvailableSettopByGroupName( String settopGroupName ) throws SettopNotFoundException;

    /**
     * Get the exception messages while creating settops from the list of mac
     * ids
     * 
     * @return exception message
     */
    String getSettopCreationError();

}
