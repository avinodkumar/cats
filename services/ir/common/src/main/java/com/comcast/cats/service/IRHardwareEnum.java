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
package com.comcast.cats.service;

public enum IRHardwareEnum
{
    GC100( "gc100" ),
    GC100_12( "gc100-12" ),
    GC100_6( "gc100-6" ),
    ITACH( "itach" ),
    TEST( "test" ),
    IRNETBOXPRO3("irnetboxpro3" );

    private String scheme;

    IRHardwareEnum( String scheme )
    {
        this.setScheme( scheme );
    }

    public static IRHardwareEnum getByValue( String scheme )
    {
        return IRHardwareEnum.valueOf( scheme.toUpperCase().replace( '-', '_' ) );
    }

    /**
     * Validate scheme is a valid enum.
     * 
     * @param scheme
     *            -
     * @return
     */
    public static boolean validate( String scheme )
    {
        boolean isValid = false;
        try
        {
            if ( getByValue( scheme ) instanceof IRHardwareEnum )
            {
                isValid = true;
            }
        }
        catch ( IllegalArgumentException iae )
        {
            // Make sure this is caught for invalid input.
            isValid = false;
        }
        return isValid;

    }

    public String getScheme()
    {
        return scheme;
    }

    public void setScheme( String scheme )
    {
        this.scheme = scheme;
    }
}