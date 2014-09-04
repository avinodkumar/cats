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
package com.comcast.cats.mock.service;

import javax.inject.Singleton;

/**
 * Alternative implementation of Device Search Service without any dependency
 * with configuration management system. This class should used for testing
 * purpose only.
 * 
 * @author ssugun00c
 * 
 */
@Singleton
public class DeviceSearchServiceLocal
{
    public static final String DEFAULT_SETTOP_FILE_PATH           = "src/main/resources/mock/settop.xml";
    public static final String DEFAULT_ALLOCATED_SETTOP_FILE_PATH = "src/main/resources/mock/settop-allocated.xml";
    public static final String DEFAULT_AVAILABLE_SETTOP_FILE_PATH = "src/main/resources/mock/settop_available.xml";

    private String      settopFilePath                     = DEFAULT_SETTOP_FILE_PATH;
    private String      allocatedSettopFilePath            = DEFAULT_ALLOCATED_SETTOP_FILE_PATH;
    private String      availableSettopFilepath            = DEFAULT_AVAILABLE_SETTOP_FILE_PATH;

    /**
     * to get settop file path.
     * 
     * @return string
     */
    public String getSettopFilePath()
    {
        return settopFilePath;
    }
    
    /**
     * to set settop file path.
     * 
     * @param settopFilePath
     */
    public void setSettopFilePath( String settopFilePath )
    {
        this.settopFilePath = settopFilePath;
    }

    /**
     * to get allocated settop file path.
     * 
     * @return string
     */
    public String getAllocatedSettopFilePath()
    {
        return allocatedSettopFilePath;
    }

    /**
     * to set allocated settop file path.
     * 
     * @param allocatedSettopFilePath
     */
    public void setAllocatedSettopFilePath( String allocatedSettopFilePath )
    {
        this.allocatedSettopFilePath = allocatedSettopFilePath;
    }

    /**
     * to get available settop file path.
     * 
     * @return string
     */
    public String getAvailableSettopFilepath()
    {
        return availableSettopFilepath;
    }

    /**
     * to set available settop file path.
     * 
     * @param availableSettopFilepath
     */
    public void setAvailableSettopFilepath( String availableSettopFilepath )
    {
        this.availableSettopFilepath = availableSettopFilepath;
    }

}
