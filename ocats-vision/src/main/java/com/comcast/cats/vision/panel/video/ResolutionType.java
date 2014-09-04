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
package com.comcast.cats.vision.panel.video;

import java.awt.Dimension;

/**
 * ResolutionType is an enum for holding the resolutions supported by the CATS
 * Vision
 * 
 * @author aswathyann
 * 
 */
public enum ResolutionType
{
    RESOLUTION_720_480( 720, 480, "720x480" ),
    RESOLUTION_640_480( 640, 480, "640x480" ),
    RESOLUTION_704_480( 704, 480, "704x480" ),
    RESOLUTION_704_240( 704, 240, "704x240" ),
    RESOLUTION_352_240( 352, 240, "352x240" ),
    RESOLUTION_320_240( 320, 240, "320x240" ),
    RESOLUTION_176_120( 176, 120, "176x120" ),
    RESOLUTION_160_120( 160, 120, "160x120" );

    private String resolutionStr;
    private int    width;
    private int    height;

    /**
     * Constructor for ResolutionType
     * 
     * @param width
     *            width of the video
     * @param height
     *            height of the video
     * @param resolutionStr
     *            resolution in string
     */
    ResolutionType( int width, int height, String resolutionStr )
    {
        this.width = width;
        this.height = height;
        this.resolutionStr = resolutionStr;
    }

    /**
     * Get string representation of resolution
     * 
     * @param resolutionType
     *            a ResolutionType
     * @return string representation of resolution
     */
    public static String getName( ResolutionType resolutionType )
    {
        return resolutionType.resolutionStr;
    }

    private static Dimension getDimension( ResolutionType resolutionType )
    {
        return new Dimension( resolutionType.width, resolutionType.height );
    }

    /**
     * Return resolution for the given action command
     * 
     * @param actionCommand
     *            the command string associated with this action event
     * @return resolution
     */
    public static Dimension getResolution( String actionCommand )
    {
        Dimension resolution = getDimension( RESOLUTION_640_480 );

        ResolutionType[] resolutionTypes = ResolutionType.values();

        for ( ResolutionType resolutionType : resolutionTypes )
        {
            if ( actionCommand.equals( ResolutionType.getName( resolutionType ) ) )
            {
                resolution = ResolutionType.getDimension( resolutionType );
                break;
            }
        }
        return resolution;
    }

    /**
     * Returns the ResolutionType for the specified dimension.
     * 
     * @param dimension
     *            The resolution.
     * @return ResolutionType for the Dimension else null.
     */
    public static ResolutionType getResolutionType( Dimension dimension )
    {
        ResolutionType resolutionType = null;
        String dimensionString = dimension.width + "x" + dimension.height;
        ResolutionType[] resolutionTypes = ResolutionType.values();
        for ( ResolutionType type : resolutionTypes )
        {
            if ( type.resolutionStr.equals( dimensionString ) )
            {
                resolutionType = type;
                break;
            }
        }
        return resolutionType;
    }

    /**
     * Method to check whether a ResolutionType exists with the specified
     * Dimension.
     * 
     * @param resolution
     *            The Dimension
     * @return True if ResolutionType exists with the specified Dimension, else
     *         false.
     */
    public static boolean isResolutionValid( Dimension resolution )
    {
        boolean isResolutionValid = false;
        ResolutionType[] resolutionTypes = ResolutionType.values();
        for ( ResolutionType resolutionType : resolutionTypes )
        {
            Dimension referenceResolution = ResolutionType.getDimension( resolutionType );
            if ( ( referenceResolution.width == resolution.width )
                    && ( referenceResolution.height == resolution.height ) )
            {
                isResolutionValid = true;
                break;
            }
        }
        return isResolutionValid;
    }
}
