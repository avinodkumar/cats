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
package com.comcast.cats.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;


/**
 * Contains the core function(s) that are used for image comparison.
 * Because this is a utility class, an instance of it cannot be created.
 */
public class CoreImageCompare {
    
    private CoreImageCompare() { }

    /**
     * Compares the specified images based on the specified tolerances.
     * Parameters are not checked (for nulls or out of range values) to maximize performance. Expected values
     * are specified in parameter java docs.
     * 
     * @param currentImage
     *           The image to check. Cannot be null.
     * @param expectedImage
     *           The expected image. Cannot be null.
     * @param matchPercent
     *           The match percent needed for the image compare to be successful.
     *           Value should be between 0 - 100 for correct results. 
     *           Negative value will always result in true. > 100 will always result in false.
     * @param redTolerance
     *           The red tolerance.
     *           Value should be between 0 - 255 for correct results.
     *           Negative value will always result in false if matchPercent > 0
     * @param greenTolerance
     *           The green tolerance.
     *           Value should be between 0 - 255 for correct results.
     *           Negative value will always result in false if matchPercent > 0
     * @param blueTolerance
     *           The blue tolerance.
     *           Value should be between 0 - 255 for correct results.
     *           Negative value will always result in false if matchPercent > 0
     * @return true if the images are equal.
     *         false is the images are not equal.
     */
    public static boolean compareImages(BufferedImage currentImage, BufferedImage expectedImage, float matchPercent, long redTolerance, long greenTolerance, long blueTolerance) {
        int w1 = currentImage.getWidth(null);
        int h1 = currentImage.getHeight(null);
        int w2 = expectedImage.getWidth(null);
        int h2 = expectedImage.getHeight(null);
        
        if ((w1 != w2) || (h1 != h2)) {
            //Log.err("ERROR! Images are not the same size (src is " + w1 + "x" + h1 + " and dst is " + w2 + "x" + h2 + ")");
            return false;
        }
        int pixelMatch = 0;

        int arraySize = w1 * h1;
        int[] rgbs1 = new int[arraySize];
        int[] rgbs2 = new int[arraySize];
        
        // Fetch all of the ARGB pixel values from currentImage and expectedImage
        currentImage.getRGB(0, 0, w1, h1, rgbs1, 0, w1);
        expectedImage.getRGB(0, 0, w2, h2, rgbs2, 0, w2);
        
        final float COEFF = 100.0f / (float) arraySize;
        for (int col = 0; col < w1; col++) {
            for (int idx = col; idx < rgbs1.length; idx += w1) {
                int rgb1 = rgbs1[idx];
                int red1 = (rgb1 & 0xff0000) >> 16;
                int green1 = (rgb1 & 0x00ff00) >> 8;
                int blue1 = rgb1 & 0x0000ff;

                int rgb2 = rgbs2[idx];
                int red2 = (rgb2 & 0xff0000) >> 16;
                int green2 = (rgb2 & 0x00ff00) >> 8;
                int blue2 = rgb2 & 0x0000ff;

                if ((Math.abs(red1 - red2) <= redTolerance)
                     && (Math.abs(green1 - green2) <= greenTolerance)
                        && (Math.abs(blue1 - blue2) <= blueTolerance)) {
                    pixelMatch++;
                }
            }
            
        }
        
        float actualMatchPercent = pixelMatch * COEFF;
        if (actualMatchPercent < matchPercent) {
            return false;
        }
        return true;
    }

    /**
     * Search for a particular image on the specified search area of another image, based on the specified tolerances.
     * 
     * @param refImage
     *            The image to find out. Cannot be null.
     * @param targetImage
     *            The full target image. Cannot be null.
     *            This is the image on which we are going to perform the search for small image in the defined search area.
     * @param x
     *            The starting X position of the search area in the target
     *            image.
     * @param y
     *            The starting Y position of the search area in the target
     *            image.
     * @param width
     *            The Width of search area in the target image.
     * @param height
     *            The Height of search area in the target image.
     * @param matchPercent
     *            The match percent needed for the image compare to be
     *            successful. Value should be between 0 - 100 for correct
     *            results. Negative value will always result in true. > 100 will
     *            always result in false.
     * @param redTolerance
     *            The red tolerance. Value should be between 0 - 255 for correct
     *            results. Negative value will always result in false if
     *            matchPercent > 0
     * @param greenTolerance
     *            The green tolerance. Value should be between 0 - 255 for
     *            correct results. Negative value will always result in false if
     *            matchPercent > 0
     * @param blueTolerance
     *            The blue tolerance. Value should be between 0 - 255 for
     *            correct results. Negative value will always result in false if
     *            matchPercent > 0
     * @return ImageCompareResult
     */
    public static ImageCompareResult findImageOnTargetRegion( BufferedImage refImage, BufferedImage fullTargetImage, int x,
            int y, int width, int height, float matchPercent, int redTolerance, int greenTolerance, int blueTolerance )
    {
        ImageCompareResult icResult = new ImageCompareResult();

        // Getting the sub image on which the search need to be performed.
        BufferedImage bigImage = fullTargetImage.getSubimage( x, y, width, height );

        int bwidth = bigImage.getWidth();
        int bheight = bigImage.getHeight();
        int swidth = refImage.getWidth();
        int sheight = refImage.getHeight();

        // Getting the pixels of the big image (image on which the search is to be performed) and small image (the image to be found out)
        int[][] bigImgPix = getColorModel( bigImage );
        int[][] smallImgPix = getColorModel( refImage );

        // Getting pixel tolerance to be allowed in the comparison
        float unmatchedTolerance = ( swidth * sheight ) * ( 100.0f - matchPercent ) / 100;

        boolean found = true;

        // Iterating over the big image pixels and placing the small image over each position and comparing whether it matches or not.
        for ( int colIdx = 0; colIdx <= ( bwidth - swidth ); colIdx++ )
        {
            for ( int rowIdx = 0; rowIdx <= ( bheight - sheight ); rowIdx++ )
            {
                found = true;
                int unmatchCount = 0;

                // placing the small image on the big image and doing comparison
                for ( int sImgColIdx = 0, bImgColIdx = colIdx; sImgColIdx < swidth && bImgColIdx < bwidth; sImgColIdx++, bImgColIdx++ )
                {
                    for ( int sImgRowIdx = 0, bImgRowIdx = rowIdx; sImgRowIdx < sheight && bImgRowIdx < bheight; sImgRowIdx++, bImgRowIdx++ )
                    {
                        // Getting the pixels at a particular position
                        int bigImgPixel = bigImgPix[ bImgColIdx ][ bImgRowIdx ];
                        int smallImgPixel = smallImgPix[ sImgColIdx ][ sImgRowIdx ];

                        // Applying tolerances
                        int red1 = ( bigImgPixel & 0xff0000 ) >> 16;
                        int green1 = ( bigImgPixel & 0x00ff00 ) >> 8;
                        int blue1 = bigImgPixel & 0x0000ff;

                        int red2 = ( smallImgPixel & 0xff0000 ) >> 16;
                        int green2 = ( smallImgPixel & 0x00ff00 ) >> 8;
                        int blue2 = smallImgPixel & 0x0000ff;

                        // comparing the pixels
                        if ( !( ( Math.abs( red1 - red2 ) <= redTolerance )
                                && ( Math.abs( green1 - green2 ) <= greenTolerance ) && ( Math.abs( blue1 - blue2 ) <= blueTolerance ) ) )
                        {
                            // If the unmatched count exceeds the unmatched tolerance, breaking from the small image pixel iteration loop.
                            if ( unmatchCount >= unmatchedTolerance )
                            {
                                found = false;
                                break;
                            }
                            unmatchCount++;
                        }
                    }
                    if ( !found )
                    {
                        break;
                    }
                }
                if ( found )
                {
                    icResult = createResult( x + colIdx, y + rowIdx, swidth, sheight, x, y, width, height, fullTargetImage );
                    return icResult;
                }
            }
        }

        return icResult;
    }

    private static ImageCompareResult createResult( int resultX, int resultY, int resultWidth, int resultHeight, 
            int searchX, int searchY, int searchWidth, int searchHeight, BufferedImage image )
    {
        BufferedImage clonedImage = getClone( image );
        Graphics2D gc = clonedImage.createGraphics();
        gc.setColor( Color.RED );
        gc.drawRect( resultX, resultY, resultWidth, resultHeight );
        
        gc.setStroke( new BasicStroke( 1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10.0f, new float[] { 10.0f }, 0.0f ));
        gc.setColor( Color.WHITE );
        gc.drawRect( searchX, searchY, searchWidth, searchHeight );
        
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.setX( resultX );
        regionInfo.setY( resultY );
        regionInfo.setWidth( resultWidth );
        regionInfo.setHeight( resultHeight );

        return new ImageCompareResult(true, regionInfo, clonedImage);
    }

    private static int[][] getColorModel( BufferedImage image )
    {
        int w = image.getWidth();
        int h = image.getHeight();
        int[][] list = new int[ w ][ h ];

        for ( int i = 0; i < h; i++ )
        {
            for ( int j = 0; j < w; j++ )
            {
                int pixel = image.getRGB( j, i );
                list[ j ][ i ] = pixel;
            }
        }
        return list;
    }
    
    private static BufferedImage getClone( BufferedImage image )
    {
        ColorModel colorModel = image.getColorModel();        
        WritableRaster raster = image.copyData( null );
        return new BufferedImage( colorModel, raster, colorModel.isAlphaPremultiplied(), null );
    } 
}
