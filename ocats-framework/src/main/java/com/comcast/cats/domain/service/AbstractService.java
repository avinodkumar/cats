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

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.DeviceSearchServiceEndpoint;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.DeviceSearchService;

/**
 * <p>
 * Common features of Domain services. {@link RestTemplate} ,
 * {@link DomainProperties} and logger instances are available to subclass.
 * </p>
 * <p>
 * 
 * @author subinsugunan
 * 
 * @param <Type>
 */
@Named
public abstract class AbstractService< Type >
{
    @Inject
    protected CatsProperties           properties;

    /**
     * All subclass should use this.
     */
    protected final Log                logger = LogFactory.getLog( getClass() );

    private static DeviceSearchService deviceSearchService;

    public DeviceSearchService getDeviceSearchService() throws SettopNotFoundException
    {
        if ( null == deviceSearchService )
        {
            try
            {
                deviceSearchService = new DeviceSearchServiceEndpoint( new URL( getServerUrl()
                        + ConfigServiceConstants.DEVICE_SEARCH_SERVICE_WSDL_LOCATION ) ).getPort();
            }
            catch ( MalformedURLException e )
            {
                throw new SettopNotFoundException( e.getMessage() );
            }
        }
        return deviceSearchService;
    }

    private String getServerUrl()
    {
        String serverurl = properties.getServerUrl();

        if ( null == serverurl )
        {
            serverurl = System.getProperty( ConfigServiceConstants.CATS_SERVER_BASE_URL );
        }

        return serverurl;
    }
}
