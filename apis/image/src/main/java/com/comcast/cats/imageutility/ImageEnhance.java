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

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.imageutility.ImageType;

/**
 * 
 * This class provides utility methods for performing image enhancement
 * functions to increase the readability of OCR.
 * 
 * @author Sijil CV
 * @author Manesh Thomas
 * 
 */
public class ImageEnhance
{
    private static Logger logger = LoggerFactory.getLogger( ImageEnhance.class );

    /**
     * Image Enhancement function to increase the readability of OCR.
     * 
     * @param image
     *            - the image to be enhanced.
     * @param target
     *            - target location at which the image needs to be saved.
     * @param coordinate1
     *            Top X coordinate
     * @param coordinate2
     *            Top Y coordinate
     * @param coordinate3
     *            Bottom X coordinate
     * @param coordinate4
     *            Bottom Y coordinate
     * @param imagetype
     *            - 1)dark background with light foreground ( dl ) 2)light
     *            background with dark foreground ( ld ) 3)dark background with
     *            dark foreground ( dd ) 4)light background with light
     *            foreground ( ll ) 5)complex image (special case) ( sc )
     *            6)light red image types ( my )
     * 
     * @param zoomscale
     *            - how much zoomed o/p is needed
     * @param volume
     *            - enhancing for volume check or not
     * @param urlpath
     * @param mode
     * @throws ImageUtilityException
     * @throws IOException
     */

    public static void enhance( BufferedImage image, String target, int coordinate1, int coordinate2, int coordinate3,
            int coordinate4, String imagetype, int zoomscale, boolean volume, boolean urlpath, int mode )
            throws ImageUtilityException, IOException
    {

        if ( zoomscale <= 0 )
        {
            logger.error( " Zoom Value should be greater than Zero" );
            throw new ImageUtilityException( "Zoom Value can't be Zero/Negative" );
        }

        logger.info( "Starting Image enhance operation " + "with " + imagetype + " Type." );

        int zoom = 1;
        boolean fullMode = false;
        float scale = 1f;
        float offset = 1f;

        // Set the scale and offset values based on image type.
        switch ( ImageType.getValue( imagetype ) )
        {
        case LD:
            scale = 1f;
            offset = 20f;
            break;
        case DL:
            scale = 1f;
            offset = 1f;
            break;
        case LL:
            scale = 9f;
            offset = 1.5f;
            break;
        case DD:
            scale = 1.5f;
            offset = 20f;
            break;
        case SC:
            scale = 1f;
            offset = -20f;
            break;
        case MY:
            scale = .813799f;
            offset = 15f;
            break;
        default:
            logger.error( " Invalid Image type " );
            throw new ImageUtilityException( " Invalid Image type " );
        }

        // Full image processing.
        if ( ( coordinate1 == 0 ) & ( coordinate2 == 0 ) & ( coordinate3 == 0 ) & ( coordinate4 == 0 ) )
        {
            fullMode = true;
            image = processFullImage( image, imagetype, zoomscale, scale, offset );
        }
        else
        {
            image = processSubImage( image, coordinate1, coordinate2, coordinate3, coordinate4, imagetype, zoom, scale,
                    offset );
        }

        // Sharp image generation
        image = ImageUtility.generateSharpImage( image );
        image = ImageUtility.generateBlurImage( image );
        // image to gray scale
        ColorConvertOp colorConvert = new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null );
        image = colorConvert.filter( image, null );
        // Black and White Image generation
        image = ImageUtility.genBlackAndWhiteImage( image );
        float zscale = ( 1f / zoom );
        if ( fullMode == true )
        {
            image = ImageUtility.zoom( image, 0.5f * zoomscale, 0.5f * zoomscale );
        }
        else
        {
            image = ImageUtility.zoom( image, zscale * zoomscale, zscale * zoomscale );
        }

        if ( target != null )
        {
            ImageUtility.saveImageAsPNG( image, target );
        }
    }

    /**
     * Process sub image.
     * 
     * @throws ImageUtilityException
     */
    private static BufferedImage processSubImage( BufferedImage image, int coordinate1, int coordinate2,
            int coordinate3, int coordinate4, String imagetype, int zoom, float scale, float offset )
            throws ImageUtilityException
    {
        int width = Math.abs( coordinate3 - coordinate1 );
        int height = Math.abs( coordinate4 - coordinate2 );
        try
        {
            image = image.getSubimage( coordinate1, coordinate2, width, height );
        }
        catch ( java.awt.image.RasterFormatException io )
        {
            logger.error( "Invalid x y cordinate. Coordinate1: " + coordinate1 + "coordinate2: " + coordinate2
                    + "width: " + width + "height : " + height );
            throw new ImageUtilityException( "Invalid x y cordinate" );
        }

        // invert Image
        if ( !( imagetype.equals( ImageType.DD.getImageTypeString() ) ) )
            // for dark background and foreground image.
            ImageUtility.invertImage( image ); // Inversion not needed

        ZoomedImage zoomedImage = zoomInMultipleTimes( zoom, image );
        image = ImageUtility.performRescaleOperation( zoomedImage.getImage(), scale, offset );

        zoomedImage = zoomInMultipleTimes( zoom, image );
        image = zoomedImage.getImage();

        return image;
    }

    /**
     * Process Full Image.
     * 
     * @throws ImageUtilityException
     */
    private static BufferedImage processFullImage( BufferedImage image, String imagetype, int zoomscale, float scale,
            float offset ) throws ImageUtilityException
    {
        if ( image.getWidth() >= 1600 && image.getHeight() >= 1200 )
        {
            logger.error( "Image Size is Too large To Enhance. Reduce the Image Width * Height" );
            throw new ImageUtilityException( "Image Size is Too large To Enhance. Reduce the Image Width * Height" );
        }

        if ( zoomscale * image.getWidth() >= 2400 && zoomscale * image.getHeight() >= 1600 )
        {
            logger.error( "Image Size is Too large To Enhance. Reduce the Image Width * Height" );
            throw new ImageUtilityException( "Image Size is Too large To Enhance. Reduce the Image Width * Height" );
        }

        if ( !( imagetype.equals( ImageType.DD.getImageTypeString() ) ) )
        {
            // invert Image
            ImageUtility.invertImage( image );
        }

        image = ImageUtility.zoom( image, 2f, 2f );
        image = ImageUtility.performRescaleOperation( image, scale, offset );
        return image;
    }

    /**
     * Perform zoom in multiple times.
     * 
     * @param zoom
     *            zoom scale.
     * @param image
     *            input image
     * @return Zoomed image
     * @throws ImageUtilityException
     */
    private static ZoomedImage zoomInMultipleTimes( int zoom, BufferedImage image ) throws ImageUtilityException
    {
        ZoomedImage zoomedImage = null;
        int initialWidth = image.getWidth();
        image = ImageUtility.zoom( image, 4f, 4f );

        if ( image.getWidth() != initialWidth )
        {
            zoom = zoom * 4;
        }
        else
        {
            image = ImageUtility.zoom( image, 2f, 2f );
            if ( image.getWidth() != initialWidth )
            {
                zoom = zoom * 2;
            }
        }
        zoomedImage = new ZoomedImage( zoom, image );
        return zoomedImage;
    }
}