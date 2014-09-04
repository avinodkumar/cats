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
package com.comcast.cats.domain.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.comcast.cats.domain.util.CommonUtil;

/**
 * This class will read the test configuration from
 * src/test/resources/test.props
 * 
 * @author subinsugunan
 * 
 */
public class PropertyUtil
{
    private Properties         properties          = new Properties();

    public static final String MAC_ID              = "test.mac.id";
    public static final String RESERVATION_ID      = "test.reservation.id";
    public static final String SETTOP_GROUP_ID     = "test.settop.group.id";
    public static final String SETTOP_RACK_ID      = "test.rack.id";
    public static final String RESERVED_SETTOP__ID = "test.my.settop.id";
    public static final String SERVER_ID           = "test.server.id";
    public static final String ENVIRONMENT_ID      = "test.environment.id";
    public static final String RESERVATION_NAME    = "test.reservation.name";
    public static final String SETTOP_GROUP_NAME   = "test.settop.group.name";
    public static final String RACK_NAME           = "test.rack.name";
    public static final String PROPERTY_ONE        = "test.property.one";
    public static final String VALUE_ONE           = "test.value.one";
    public static final String PROPERTY_TWO        = "test.property.two";
    public static final String VALUE_TWO           = "test.value.two";
    public static final String OFFSET              = "test.offset";
    public static final String COUNT               = "test.count";
    public static final int    DEFAULT_OFFSET      = 0;
    public static final int    DEFAULT_COUNT       = 0;

    public PropertyUtil()
    {
        try
        {
            properties.load( new FileInputStream( "src/test/resources/test.props" ) );
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

    }

    public void setProperties( Properties properties )
    {
        this.properties = properties;
    }

    public String getMacId()
    {
        return properties.getProperty( MAC_ID );
    }

    public String getReservationId()
    {
        return properties.getProperty( RESERVATION_ID );
    }

    public String getReservationName()
    {
        String reservationName = properties.getProperty( RESERVATION_NAME );

        if ( null != reservationName )
        {
            reservationName = reservationName.trim();
        }
        return reservationName;
    }

    public String getPropertyOne()
    {
        return properties.getProperty( PROPERTY_ONE );
    }

    public String getPropertyValueOne()
    {
        return properties.getProperty( VALUE_ONE );
    }

    public String getPropertyTwo()
    {
        return properties.getProperty( PROPERTY_TWO );
    }

    public String getPropertyValueTwo()
    {
        return properties.getProperty( VALUE_TWO );
    }

    public Integer getOffset()
    {
        Integer offset = DEFAULT_OFFSET;
        String offsetStr = properties.getProperty( OFFSET );

        if ( CommonUtil.isValidNumber( offsetStr ) )
        {
            offset = Integer.parseInt( offsetStr );
        }
        return offset;
    }

    public Integer getCount()
    {
        Integer offset = DEFAULT_COUNT;
        String countStr = properties.getProperty( COUNT );

        if ( CommonUtil.isValidNumber( countStr ) )
        {
            offset = Integer.parseInt( countStr );
        }
        return offset;
    }

    public String getSettopGroupId()
    {
        return properties.getProperty( SETTOP_GROUP_ID );
    }

    public String getSettopGroupName()
    {
        String settopGroupName = properties.getProperty( SETTOP_GROUP_NAME );

        if ( null != settopGroupName )
        {
            settopGroupName = settopGroupName.trim();
        }
        return settopGroupName;
    }

    public String getRackName()
    {
        String rackName = properties.getProperty( RACK_NAME );

        if ( null != rackName )
        {
            rackName = rackName.trim();
        }
        return rackName;
    }

    public String getRackId()
    {
        return properties.getProperty( SETTOP_RACK_ID );
    }

    public String getReservedSettopId()
    {
        return properties.getProperty( RESERVED_SETTOP__ID );
    }

    public String getServerId()
    {
        return properties.getProperty( SERVER_ID );
    }

    public String getEnvironmentId()
    {
        return properties.getProperty( ENVIRONMENT_ID );
    }
}
