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
package com.comcast.cats.service.util;

/**
 * A simple utility class to get application version at runtime.
 * 
 * @author ssugun00c
 * 
 */
public class VersionUtil
{
    private static final String DEFAULT_VENDOR_NAME = "Comcast Tooling and Automation";
    private static final String DEFAULT_VERSION     = "UNKNOWN";

    private String              version;
    private String              vendor;

    public String getVersion()
    {
        version = DEFAULT_VERSION;

        if ( null != getClass().getPackage().getImplementationVersion() )
        {
            version = getClass().getPackage().getImplementationVersion().trim();
        }
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getVendor()
    {
        vendor = DEFAULT_VENDOR_NAME;

        if ( null != getClass().getPackage().getImplementationVendor() )
        {
            vendor = getClass().getPackage().getImplementationVendor().trim();
        }
        return vendor;
    }

    public void setVendor( String vendor )
    {
        this.vendor = vendor;
    }

}
