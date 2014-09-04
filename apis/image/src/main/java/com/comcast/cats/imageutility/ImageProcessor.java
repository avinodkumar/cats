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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.comcast.cats.threshold.HistogramHighLightedArea;

/**
 * Image Processor class acts as the interface for accessing the various Image
 * enhancement functions.
 * 
 * @author maneshthomas
 * 
 */
public class ImageProcessor
{

    private static Logger logger = LoggerFactory.getLogger( ImageProcessor.class );

    /**
     * Enhance the specified Image. The enhanced image will be available at the
     * target location.
     * 
     * @param source
     *            - source image
     * @param target
     *            - target location for enhanced image.
     * @param imageType
     *            1)dark background with light foreground ( dl ) 2)light
     *            background with dark foreground ( ld ) 3)dark background with
     *            dark foreground ( dd ) 4)light background with light
     *            foreground ( ll ) 5)complex image (special case) ( sc )
     *            6)light red image types ( my ) 7) auto mode which used otsu
     *            thresholding method to identify text from an image.(auto)
     * @throws ImageUtilityException
     * @throws IOException
     */
    public static boolean enhanceImage( BufferedImage sourceBuffered, String target, String imageType )
            throws ImageUtilityException, IOException
    {
        boolean enhanceImageStatus = false;

        logger.debug( "Source Image = " + sourceBuffered );
        logger.info( "target = " + target );
        logger.info( "imageType  = " + imageType );

        try
        {
            switch ( ImageType.getValue( imageType ) )
            {
            case AUTO:
                HistogramHighLightedArea.thresholdHighlightedArea( sourceBuffered, target, 0, 0,
                        sourceBuffered.getWidth(), sourceBuffered.getHeight(), true );
                enhanceImageStatus = true;
                break;
            case LD:
            case DL:
            case LL:
            case DD:
            case SC:
            case MY:
                ImageEnhance.enhance( sourceBuffered, target, 0, 0, 0, 0, imageType, 1, false, false, 1 );
                enhanceImageStatus = true;
                break;
            default:
                logger.error( " Invalid Image type " + imageType );
            }

        }
        catch ( ImageUtilityException imageUtilityException )
        {
            logger.error( " Failed to enhance image " + imageUtilityException.getMessage() );
            throw new ImageUtilityException( imageUtilityException );
        }
        logger.info( "Image enhancement operation completed with status : " + enhanceImageStatus );
        return enhanceImageStatus;

    }

}
