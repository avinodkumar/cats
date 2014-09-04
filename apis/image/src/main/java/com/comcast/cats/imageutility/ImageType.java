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
package com.comcast.cats.imageutility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Constants used for Image enhancement operations.
 * 
 * @author maneshthomas
 * 
 */
@XmlType
@XmlEnum( String.class )
public enum ImageType
{
    // auto mode - otsu thresholding method to identify text from an image.
    AUTO( "auto" ),
    // dark background with light foreground
    DL( "dl" ),
    // light background with dark foreground
    LD( "ld" ),
    // dark background with dark foreground
    DD( "dd" ),
    // light background with light foreground
    LL( "ll" ),
    // complex image (special case)
    SC( "sc" ),
    // light red image types(special case)
    MY( "my" ),
    // Unsharpmask Image Operation
    UM( "um" ),
    // unknown image type
    UNKNOWN( "" );

    private final String imageType;

    private ImageType( String type )
    {
        imageType = type;
    }

    public String getImageTypeString()
    {
        return imageType;
    }

    public static ImageType getValue( String imageType )
    {
        try
        {
            return valueOf( imageType.toUpperCase() );
        }
        catch ( Exception e )
        {
            return UNKNOWN;
        }
    }

}
