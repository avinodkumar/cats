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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.comcast.cats.domain.util.CommonUtil;

/**
 * This class will read the test configuration from
 * src/test/resources/test.properties
 * 
 * @author subinsugunan
 * 
 */
public class PropertyUtil extends Properties
{
    /**
     * 
     */
    private static final long   serialVersionUID     = 1L;
    private static final String TEST_PROPERTIES_FILE = "src/test/resources/test.properties";

    public static final String  MAC_ID               = "test.mac.id";
    public static final String  RESERVATION_ID       = "test.reservation.id";
    public static final String  SETTOP_GROUP_ID      = "test.settop.group.id";
    public static final String  SETTOP_RACK_ID       = "test.rack.id";
    public static final String  RESERVED_SETTOP__ID  = "test.my.settop.id";
    public static final String  SERVER__ID           = "test.server.id";
    public static final String  RESERVATION_NAME     = "test.reservation.name";
    public static final String  SETTOP_GROUP_NAME    = "test.settop.group.name";
    public static final String  RACK_NAME            = "test.rack.name";
    public static final String  PROPERTY_ONE         = "test.property.one";
    public static final String  VALUE_ONE            = "test.value.one";
    public static final String  PROPERTY_TWO         = "test.property.two";
    public static final String  VALUE_TWO            = "test.value.two";
    public static final String  OFFSET               = "test.offset";
    public static final String  COUNT                = "test.count";
    public static final int     DEFAULT_OFFSET       = 0;
    public static final int     DEFAULT_COUNT        = 0;

    public PropertyUtil() throws IOException
    {
        loadPropertiesFromFile();
    }

    protected void loadPropertiesFromFile() throws IOException
    {
        File catsPropsFile = new File( TEST_PROPERTIES_FILE );

        FileUtils.touch( catsPropsFile );
        this.load( new FileInputStream( catsPropsFile ) );
    };

    public String getMacId()
    {
        return this.getProperty( MAC_ID );
    }

    public String getReservationId()
    {
        return this.getProperty( RESERVATION_ID );
    }

    public String getReservationName()
    {
        String reservationName = this.getProperty( RESERVATION_NAME );

        if ( null != reservationName )
        {
            reservationName = reservationName.trim();
        }
        return reservationName;
    }

    public String getPropertyOne()
    {
        return this.getProperty( PROPERTY_ONE );
    }

    public String getPropertyValueOne()
    {
        return this.getProperty( VALUE_ONE );
    }

    public String getPropertyTwo()
    {
        return this.getProperty( PROPERTY_TWO );
    }

    public String getPropertyValueTwo()
    {
        return this.getProperty( VALUE_TWO );
    }

    public int getOffset()
    {
        int offset = DEFAULT_OFFSET;
        String offsetStr = this.getProperty( OFFSET );

        if ( CommonUtil.isValidNumber( offsetStr ) )
        {
            offset = Integer.parseInt( offsetStr );
        }
        return offset;
    }

    public int getCount()
    {
        int offset = DEFAULT_COUNT;
        String countStr = this.getProperty( COUNT );

        if ( CommonUtil.isValidNumber( countStr ) )
        {
            offset = Integer.parseInt( countStr );
        }
        return offset;
    }

    public String getSettopGroupId()
    {
        return this.getProperty( SETTOP_GROUP_ID );
    }

    public String getSettopGroupName()
    {
        String settopGroupName = this.getProperty( SETTOP_GROUP_NAME );

        if ( null != settopGroupName )
        {
            settopGroupName = settopGroupName.trim();
        }
        return settopGroupName;
    }

    public String getRackName()
    {
        String rackName = this.getProperty( RACK_NAME );

        if ( null != rackName )
        {
            rackName = rackName.trim();
        }
        return rackName;
    }

    public String getRackId()
    {
        return this.getProperty( SETTOP_RACK_ID );
    }

    public String getReservedSettopId()
    {
        return this.getProperty( RESERVED_SETTOP__ID );
    }

    public String getServerId()
    {
        return this.getProperty( SERVER__ID );
    }
}
