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

/**
 * Constants related to Settop Service.
 * 
 * @author cfrede001
 */
public class SettopConstants
{
    /**
     * SettopService related constants.
     */
    public static final String NAMESPACE                         = "urn:com:comcast:cats:settop";

    public static final String SETTOP_SERVICE_NAME               = "SettopService";
    public static final String SETTOP_SERVICE_LOCAL_PART_NAME    = "SettopServiceImplService";
    public static final String SETTOP_SERVICE_PORT_NAME          = "SettopServicePort";
    public static final String SETTOP_SERVICE_ENDPOINT_INTERFACE = "com.comcast.cats.service.SettopService";
    public static final String SETTOP_SERVICE_MAPPED_NAME        = "cats/services/SettopService";
    public static final String SETTOP_SERVICE_WSDL_LOCATION      = "settop-service/SettopService?wsdl";

    /**
     * Default allocation time.
     */
    public static final int    DEFAULT_ALLOCATION_TIME_IN_MINS   = 30;
    public static final int    MIN_ALLOCATION_TIME_IN_MINS       = 4;
    /**
     * Settop properties
     */
    public static final String UNIT_ADDRESS_PROPERTY             = "unitAddress";
    public static final String HOST_MAC_ADDRESS_PROPERTY         = "hostMacAddress";
    public static final String HOST_IP_PROPERTY                  = "hostIp4Address";
    public static final String REMOTE_TYPE_PROPERTY              = "remoteType";
    public static final String MODEL_PROPERTY                    = "model";

    /**
     * Settop family
     */
    public static final String SETTOP_FAMILY_SAMSUNG             = "settop.family.samsung";
    public static final String SETTOP_FAMILY_RNG                 = "settop.family.rng";
    public static final String SETTOP_FAMILY_DTA                 = "settop.family.dta";
    public static final String SETTOP_FAMILY_HD_DTA              = "settop.family.hd.dta";
    public static final String SETTOP_FAMILY_MOTOROLA_LEGACY     = "settop.family.motorola.legacy";
    public static final String SETTOP_FAMILY_CISCO_LEGACY        = "settop.family.cisco.legacy";
    public static final String SETTOP_FAMILY_PARKER_X1           = "settop.family.parker.x1";

    /**
     * Settop decorator qualifier names.
     */
    public static final String SETTOP_DECORATOR_SAMSUNG          = "settop.decorator.samsung";
    public static final String SETTOP_DECORATOR_RNG              = "settop.decorator.rng";
    public static final String SETTOP_DECORATOR_DTA              = "settop.decorator.dta";
    public static final String SETTOP_DECORATOR_HD_DTA           = "settop.decorator.hd.dta";
    public static final String SETTOP_DECORATOR_MOTOROLA_LEGACY  = "settop.decorator.motorola.legacy";
    public static final String SETTOP_DECORATOR_CISCO_LEGACY     = "settop.decorator.cisco.legacy";
    public static final String SETTOP_DECORATOR_PARKER_X1        = "settop.decorator.parker.x1";
}
