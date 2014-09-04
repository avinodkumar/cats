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

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.SettopAllocationService;

/**
 * Client for {@link SettopAllocationService}.
 * 
 * @author subinsugunan
 * 
 */
public class AllocationServiceEndpoint extends Service
{
    /**
     * Creates an instance of Service class.
     * 
     * @throws MalformedURLException
     *             Thrown to indicate that a malformed URL has occurred.
     */
    public AllocationServiceEndpoint() throws MalformedURLException
    {
        this( new URL( ConfigServiceConstants.ALLOCATION_SERVICE_WSDL_LOCATION ), new QName(
                ConfigServiceConstants.NAMESPACE, ConfigServiceConstants.ALLOCATION_SERVICE_LOCAL_PART_NAME ) );
    }

    /**
     * Creates an instance of Service class using the URL
     * 
     * @param wsdlDocumentLocation
     *            The location of the WSDL document for the Service
     */
    public AllocationServiceEndpoint( final URL wsdlDocumentLocation )
    {
        super( wsdlDocumentLocation, new QName( ConfigServiceConstants.NAMESPACE,
                ConfigServiceConstants.ALLOCATION_SERVICE_LOCAL_PART_NAME ) );
    }

    /**
     * Creates an instance of Service class using the URL and QName provided.
     * 
     * @param wsdlDocumentLocation
     *            The location of the WSDL document for the Service.
     * @param serviceName
     *            Qualified name of the service.
     */
    public AllocationServiceEndpoint( final URL wsdlDocumentLocation, final QName serviceName )
    {
        super( wsdlDocumentLocation, serviceName );
    }

    /**
     * The getPort method returns a proxy.
     * 
     * @return Object instance that supports the specified service end point
     *         interface.
     */
    public SettopAllocationService getPort()
    {
        return super.getPort( SettopAllocationService.class );
    }
}
