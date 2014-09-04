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
package com.comcast.cats.threshold;

import java.awt.Component;
import java.awt.image.BufferedImage;

/**
 * This class implements otsu thresholding method to identify text from an
 * image.
 * 
 * @author aswathyjs
 * @author rahulchandra
 * @author maneshthomas
 */

public class RectangleIdentification extends Component
{

    private static final String YELLOW           = "yellow";
    private static final String WHITE            = "white";
    private static final String BLACK            = "black";
    private static final long   serialVersionUID = 1L;
    private BufferedImage       testImage;
    private int                 check            = 1;                     // to
                                                                          // check
                                                                          // all
                                                                          // rectangles
                                                                          // have
                                                                          // been
                                                                          // identified
    private int                 index            = 0;
    private int                 coordinates[][]  = new int[ 10000 ][ 4 ];
    private String              prevColour       = WHITE;

    /**
     * Creates RectangleIdentification object.
     * 
     * @param rgbImage
     */
    public RectangleIdentification( BufferedImage rgbImage )
    {
        testImage = rgbImage;
    }

    /**
     * Returns the coordinates.
     * 
     * @param image
     *            : input image
     * @param color
     *            : colour to be identified
     * @return coordinates of the highlighted area
     */

    public int[] getCoordinates( BufferedImage image, String color )
    {
        int pixel;
        int[] xcoordinates = new int[ 10000 ];
        int[] ycoordinates = new int[ 10000 ];
        int[] coordinates1 = new int[ 4 ];
        int red = 0;
        int green = 0;
        int blue = 0;
        int arrayIndexofX = 0;
        int columnNumber = 0;
        int rowNumber = 0;
        int checkRectangle = 0;
        int minimumWidth = 40;
        int minimumHeight = 10;

        // Scan the image through x direction
        for ( int j = 0; j < image.getHeight(); j++ )
        {
            checkRectangle = 0;
            for ( int i = 0; i < image.getWidth(); i++ )
            {
                pixel = image.getRGB( i, j );
                red = ( pixel >> 16 ) & 0xff;
                green = ( pixel >> 8 ) & 0xff;
                blue = ( pixel ) & 0xff;
                if ( ( blue < red && blue < green && ( green - blue ) > 30 && color == YELLOW )
                        | ( ( Math.abs( blue - red ) < 20 && Math.abs( green - red ) < 20
                                && Math.abs( blue - green ) < 20 && red > 200 && blue > 200 && green > 200 ) && color == WHITE )
                        | ( ( Math.abs( blue - red ) < 5 && Math.abs( green - red ) < 5 && red > 80
                                && Math.abs( blue - green ) < 5 && blue > 80 && red < 150 && green > 80 && blue < 150 && green < 150 ) && color == BLACK ) )

                {
                    checkRectangle = checkRectangle + 1;
                    columnNumber = i;
                    rowNumber = j;
                }

            }

            int shift = 0;
            if ( color == BLACK )
            {
                minimumWidth = 30;
                shift = 10;
            }
            if ( color == YELLOW )
            {
                shift = 7;
            }
            if ( color == WHITE )
            {
                shift = 20;

            }
            if ( checkRectangle > minimumWidth )
            {
                if ( arrayIndexofX == 0 )
                {
                    xcoordinates[ arrayIndexofX ] = rowNumber;
                    arrayIndexofX++;
                }
                else if ( arrayIndexofX != 0 )
                {
                    if ( ( rowNumber - ( xcoordinates[ arrayIndexofX - 1 ] ) < shift ) )
                    {
                        xcoordinates[ arrayIndexofX ] = rowNumber;
                        arrayIndexofX++;
                    }
                    else
                    {
                        if ( arrayIndexofX < 15 )
                        {
                            xcoordinates[ arrayIndexofX - 1 ] = rowNumber;
                            arrayIndexofX = 0;
                        }
                    }
                    if ( ( arrayIndexofX - 1 ) == 0 )
                    {
                        xcoordinates[ 0 ] = rowNumber;
                        arrayIndexofX = 0;
                    }
                }
            }
        }

        int arrayIndexofY = 0;
        int minimumshift = 20;
        if ( arrayIndexofX == 0 )
        {
            arrayIndexofX = 1;
        }
        // scan the image through y direction
        for ( int i = 0; i < image.getWidth(); i++ )
        {
            checkRectangle = 0;
            for ( int j = xcoordinates[ 0 ]; j < xcoordinates[ arrayIndexofX - 1 ]; j++ )
            {
                pixel = image.getRGB( i, j );
                red = ( pixel >> 16 ) & 0xff;
                green = ( pixel >> 8 ) & 0xff;
                blue = ( pixel ) & 0xff;
                if ( ( blue < red && blue < green && ( green - blue ) > 30 && color == YELLOW )
                        | ( ( Math.abs( blue - red ) < 20 && Math.abs( green - red ) < 20
                                && Math.abs( blue - green ) < 20 && red > 200 && blue > 200 && green > 200 ) && color == WHITE )
                        | ( ( Math.abs( blue - red ) < 5 && Math.abs( green - red ) < 5 && red > 80
                                && Math.abs( blue - green ) < 5 && red < 160 && blue < 160 && green < 160 && blue > 80 && green > 80 ) && color == BLACK ) )

                {
                    checkRectangle = checkRectangle + 1;
                    columnNumber = i;
                    rowNumber = j;

                }
                if ( color == BLACK )
                {
                    minimumshift = 40;
                }
                if ( arrayIndexofY != 0 )
                {
                    if ( ( columnNumber - ( ycoordinates[ arrayIndexofY - 1 ] ) > minimumshift ) )
                    {
                        break;
                    }
                }
            }

            if ( color == BLACK )
            {
                minimumHeight = 10;
                if ( rowNumber - xcoordinates[ 0 ] < 30 )
                {
                    minimumHeight = 5;
                }
            }

            if ( checkRectangle > minimumHeight )
            {
                ycoordinates[ arrayIndexofY ] = columnNumber;
                arrayIndexofY++;
            }

        }

        if ( arrayIndexofX != 0 && arrayIndexofY != 0 )
        {
            coordinates1[ 0 ] = xcoordinates[ 0 ]; // top left coordinates of
                                                   // the
                                                   // rectangle
            coordinates1[ 1 ] = ycoordinates[ 0 ]; // top left coordinates of
                                                   // the
                                                   // rectangle
            coordinates1[ 2 ] = xcoordinates[ arrayIndexofX - 1 ];// bottom
                                                                  // right
                                                                  // coordinates
                                                                  // of the
                                                                  // rectangle
            coordinates1[ 3 ] = ycoordinates[ arrayIndexofY - 1 ];// bottom
                                                                  // right
                                                                  // coordinates
                                                                  // of the
                                                                  // rectangle
        }
        return coordinates1;
    }

    /**
     * Sets the coordinates.
     * 
     * @param image
     *            : input image
     * @param color
     *            : colour of the highlighted region to be identified
     * @return updated image
     */
    public BufferedImage setCoordinates( BufferedImage image, String color )
    {
        coordinates[ index ] = getCoordinates( image, color );
        if ( index != 0 )
        {
            if ( color == prevColour )
            {
                if ( Math.abs( coordinates[ index ][ 2 ] - coordinates[ index - 1 ][ 2 ] ) < 10
                        && ( Math.abs( coordinates[ index ][ 0 ] - coordinates[ index - 1 ][ 0 ] ) > 10 ) )
                {
                    coordinates[ index - 1 ][ 2 ] = coordinates[ index ][ 0 ];

                }
                else if ( Math.abs( coordinates[ index ][ 0 ] - coordinates[ index - 1 ][ 0 ] ) < 10
                        && ( Math.abs( coordinates[ index ][ 2 ] - coordinates[ index - 1 ][ 2 ] ) > 10 ) )
                {
                    coordinates[ index - 1 ][ 0 ] = coordinates[ index ][ 2 ] + 2;
                }
                else if ( ( Math.abs( coordinates[ index ][ 2 ] - coordinates[ index - 1 ][ 2 ] ) < 15 && ( Math
                        .abs( coordinates[ index ][ 0 ] - coordinates[ index - 1 ][ 0 ] ) < 15 ) ) )
                {
                    coordinates[ index - 1 ][ 0 ] = coordinates[ index ][ 0 ] + 5;
                }
            }
            prevColour = color;
        }
        int blackpixel = 0x00000000;
        for ( int k = coordinates[ index ][ 1 ]; k < coordinates[ index ][ 3 ] + 1; k++ )
        {
            for ( int l = coordinates[ index ][ 0 ]; l < coordinates[ index ][ 2 ] + 1; l++ )
            {
                image.setRGB( k, l, blackpixel );
            }
        }
        check = coordinates[ index ][ 1 ] + coordinates[ index ][ 3 ] + coordinates[ index ][ 0 ]
                + coordinates[ index ][ 2 ];
        if ( check > 0 )
        {
            index++;
        }
        return image;
    }

    /**
     * This function returns the coordinates of the highlighted rectangles.
     * 
     * @return coordinates
     */
    public int[] getRectangles()
    {
        int isrectanglefinished = 3;
        String[] color = new String[ 3 ];
        color[ 1 ] = YELLOW;
        color[ 0 ] = WHITE;
        color[ 2 ] = BLACK;
        int colorIndex = 0;
        while ( isrectanglefinished > 0 )
        {
            while ( check > 0 )
            {
                testImage = setCoordinates( testImage, color[ colorIndex ] );
                if ( check == 0 )
                {
                    colorIndex++;
                    isrectanglefinished--;
                }
            }
            check = 1;
        }
        index--;
        int[] coordinates1 = new int[ ( index + 1 ) * 4 ];
        int i = 0;
        int size = 0;
        while ( index >= 0 )
        {
            int width = coordinates[ index ][ 3 ] - coordinates[ index ][ 1 ];
            int height = coordinates[ index ][ 2 ] - coordinates[ index ][ 0 ];
            if ( width > 40 && height > 15 )
            {
                coordinates1[ i ] = coordinates[ index ][ 1 ];
                coordinates1[ i + 1 ] = coordinates[ index ][ 0 ];
                coordinates1[ i + 2 ] = coordinates[ index ][ 3 ];
                coordinates1[ i + 3 ] = coordinates[ index ][ 2 ];
                i = i + 4;
                size++;
            }
            index--;

        }
        // to remove the zero elements at the end
        int[] rectangleCoordinates = new int[ size * 4 ];
        for ( int j = 0; j < rectangleCoordinates.length; j++ )
        {
            rectangleCoordinates[ j ] = coordinates1[ j ];
        }
        return rectangleCoordinates;

    }

}
