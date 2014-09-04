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
package com.comcast.cats.info;

/**
 * This class holds all of the constants that the configuration management
 * service will be using.
 * 
 * @author cfrede001
 */
public interface ConfigServiceConstants
{
    /**
     * Namespace of all configuration management services.
     */
    String NAMESPACE                                = "urn:com:comcast:cats:config";

    /**
     * Allocation service related constants
     */
    String ALLOCATION_SERVICE_NAME                  = "AllocationService";
    String ALLOCATION_SERVICE_LOCAL_PART_NAME       = "SettopAllocationServiceImplService";
    String ALLOCATION_SERVICE_PORT_NAME             = "AllocationServicePort";
    String ALLOCATION_SERVICE_MAPPED_NAME           = "cats/services/AllocationService";
    String ALLOCATION_SERVICE_ENDPOINT_INTERFACE    = "com.comcast.cats.service.SettopAllocationService";
    String ALLOCATION_SERVICE_WSDL_LOCATION         = "CATS/AllocationService?wsdl";

    /**
     * Device search service related constants
     */
    String DEVICE_SEARCH_SERVICE_NAME               = "DeviceSearchService";
    String DEVICE_SEARCH_SERVICE_LOCAL_PART_NAME    = "DeviceSearchServiceImplService";
    String DEVICE_SEARCH_SERVICE_PORT_NAME          = "DeviceSearchServicePort";
    String DEVICE_SEARCH_SERVICE_MAPPED_NAME        = "cats/services/DeviceSearchService";
    String DEVICE_SEARCH_SERVICE_ENDPOINT_INTERFACE = "com.comcast.cats.service.DeviceSearchService";
    String DEVICE_SEARCH_SERVICE_WSDL_LOCATION      = "CATS/DeviceSearchService?wsdl";

    String DEFAULT_ALLOCATION_TIME_IN_MINS          = "cats.setop.allocation.time.in.mins";
    String CACHE_INTERVAL_IN_SEC                    = "cats.config.cache.interval.in.sec";

    /**
     * System Properties Config Path
     */
    String CONFIG_ENV                              = "CATS_HOME";
    String CONFIG_PATH                              = "cats.config.path";
    String CATS_SERVER_BASE_URL                     = "cats.server.url";
    String CATS_VIRTUAL_ENVIRONMENT            = "VIRTUAL";
}
