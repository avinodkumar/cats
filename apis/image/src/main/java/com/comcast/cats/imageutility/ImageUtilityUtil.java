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

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Image Utility Util Class
 * 
 * @author Shijo Stanly
 * @author maneshthomas
 */

public final class ImageUtilityUtil
{

    /**
     * Index values for Colors.
     */
    static final int        RED_COLOR_INDEX   = 0;
    static final int        GREEN_COLOR_INDEX = 1;
    static final int        BLUE_COLOR_INDEX  = 2;

    public static final int DIRECTIONS[][]    =
                                                  {
                                                      { 0, -1 }, /* up */
                                                      { -1, -1 }, /* up-left */
                                                      { -1, 0 }, /* left */
                                                      { -1, 1 }, /* down-left */
                                                      { 0, 1 }, /* down */
                                                      { 1, 1 }, /* down-right */
                                                      { 1, 0 }, /* right */
                                                      { 1, -1 } }; /* up-right */

    public static final int UP                = 0;
    public static final int UP_LEFT           = 1;
    public static final int LEFT              = 2;
    public static final int DOWN_LEFT         = 3;
    public static final int DOWN              = 4;
    public static final int DOWN_RIGHT        = 5;
    public static final int RIGHT             = 6;
    public static final int UP_RIGHT          = 5;

    /**
     * This method is used to check whether particular Pixels Color is within
     * the given Tolerance Color range.
     * 
     * @param searchedButtonColor
     *            Searched Button Color
     * @param redColor
     *            pixels Red Color
     * @param greenColor
     *            Pixels Green color
     * @param blueColor
     *            Pixels blue Color
     * @param searchedButtonTolerance
     *            Searched Button Tolerance Level
     * @return colorSearchStatus Status
     */

    public static boolean isButtonColorWithinToleranceRange( int searchedButtonColor[], int redColor, int greenColor,
            int blueColor, int searchedButtonTolerance )
    {

        boolean colorSearchStatus = false;
        if ( ( ( redColor >= ( searchedButtonColor[ RED_COLOR_INDEX ] - searchedButtonTolerance ) ) && ( redColor <= ( searchedButtonColor[ RED_COLOR_INDEX ] + searchedButtonTolerance ) ) )
                && ( ( greenColor >= ( searchedButtonColor[ GREEN_COLOR_INDEX ] - searchedButtonTolerance ) ) && ( greenColor <= ( searchedButtonColor[ GREEN_COLOR_INDEX ] + searchedButtonTolerance ) ) )
                && ( ( blueColor >= ( searchedButtonColor[ BLUE_COLOR_INDEX ] - searchedButtonTolerance ) ) && ( blueColor <= ( searchedButtonColor[ BLUE_COLOR_INDEX ] + searchedButtonTolerance ) ) ) )
        {

            colorSearchStatus = true;
        }

        return colorSearchStatus;
    }

    /**
     * This method is used to check whether particular Pixels Color is within
     * the given tolerance Color range.
     * 
     * @param searchedRedColor
     *            Reference Red Color
     * @param searchedGreenColor
     *            Reference Green Color
     * @param searchedBlueColor
     *            Reference Blue Color
     * @param redColor
     *            Current Pixel Red Color
     * @param greenColor
     *            Current Pixel Green Color
     * @param blueColor
     *            Current Pixel Blue Color
     * @param searchedColorTolerance
     *            Tolerance Level
     * @return colorSearchStatus
     */

    public static boolean isButtonColorSearchSuccessful( int searchedRedColor, int searchedGreenColor,
            int searchedBlueColor, int redColor, int greenColor, int blueColor, int searchedColorTolerance )
    {

        boolean colorSearchStatus = false;

        if ( ( ( redColor >= ( searchedRedColor - searchedColorTolerance ) ) && ( redColor <= ( searchedRedColor + searchedColorTolerance ) ) )
                && ( ( greenColor >= ( searchedGreenColor - searchedColorTolerance ) ) && ( greenColor <= ( searchedGreenColor + searchedColorTolerance ) ) )
                && ( ( blueColor >= ( searchedBlueColor - searchedColorTolerance ) ) && ( blueColor <= ( searchedBlueColor + searchedColorTolerance ) ) ) )
        {

            colorSearchStatus = true;
        }

        return colorSearchStatus;
    }

    /**
     * Creates sub image of the image specified.
     * 
     * @param image
     *            Original Image Name
     * @param coordinate1
     *            Top X Coordinate
     * @param coordinate2
     *            Top y Coordinate
     * @param coordinate3
     *            Bootom X Coordinate
     * @param coordinate4
     *            Bootom Y Coordinate
     * @return subImage of Selected portion
     */
    public static BufferedImage createSubImage( BufferedImage image, int coordinate1, int coordinate2, int coordinate3,
            int coordinate4 )
    {

        BufferedImage subImage = null;
        int width = Math.abs( coordinate3 - coordinate1 );
        int height = Math.abs( coordinate4 - coordinate2 );
        subImage = image.getSubimage( coordinate1, coordinate2, width, height );
        return subImage;
    }

    /**
     * Creates sub image of the image specified.
     * 
     * @param imagePath
     *            path of the image
     * @param coordinate1
     *            Top X Coordinate
     * @param coordinate2
     *            Top y Coordinate
     * @param coordinate3
     *            Bootom X Coordinate
     * @param coordinate4
     *            Bootom Y Coordinate
     * @return subImage
     * @throws IOException
     */
    public static BufferedImage createSubImage( String imagePath, int coordinate1, int coordinate2, int coordinate3,
            int coordinate4 ) throws IOException
    {

        BufferedImage subImage = null;
        BufferedImage image = null;

        image = ImageUtility.readImage( imagePath );

        if ( !( ( coordinate1 == 0 ) && ( coordinate2 == 0 ) && ( coordinate3 == 0 ) && ( coordinate4 == 0 ) ) )
        {
            int width = Math.abs( coordinate3 - coordinate1 );
            int height = Math.abs( coordinate4 - coordinate2 );
            subImage = image.getSubimage( coordinate1, coordinate2, width, height );
        }
        else
        {
            subImage = image;
        }

        return subImage;
    }
}
