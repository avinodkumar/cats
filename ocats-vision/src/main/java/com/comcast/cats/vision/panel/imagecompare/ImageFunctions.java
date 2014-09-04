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
package com.comcast.cats.vision.panel.imagecompare;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

/**
 * Contains image helper functions.
 */
public abstract class ImageFunctions
{

    /**
     * Makes an image translucent/transparent.
     * 
     * @param srcImage
     *            The source image.
     * @param alpha
     *            The constant alpha to be multiplied with the alpha of the
     *            source. alpha must be a floating point number in the inclusive
     *            range [0.0, 1.0].
     * @return The translucent image.
     */
    public static BufferedImage maskImageTranslucent( BufferedImage srcImage, float alpha )
    {
        BufferedImage dimg = null;
        dimg = new BufferedImage( srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TRANSLUCENT );
        Graphics2D g = dimg.createGraphics();
        g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) );
        g.drawImage( srcImage, 0, 0, null );
        g.dispose();
        return dimg;
    }

    /**
     * Scales an image.
     * 
     * @param source
     *            The source image.
     * @param factor
     *            The factor by which to scale the image.
     * @return The scaled image.
     */
    public static Image getScaledInstanceAWT( BufferedImage source, double factor )
    {
        int w = ( int ) ( source.getWidth() * factor );
        int h = ( int ) ( source.getHeight() * factor );
        return source.getScaledInstance( w, h, Image.SCALE_SMOOTH );
    }

    /**
     * Converts a Image to a BufferedImage.
     * 
     * @param image
     *            The image.
     * @return a buffered image.
     */
    public static BufferedImage toBufferedImage( Image image )
    {
        new ImageIcon( image ); // load image
        int w = image.getWidth( null );
        int h = image.getHeight( null );
        BufferedImage bimage = new BufferedImage( w, h, BufferedImage.TYPE_BYTE_INDEXED );
        Graphics2D g = bimage.createGraphics();
        g.drawImage( image, 0, 0, null );
        g.dispose();
        ImageIO.setUseCache( false );
        return bimage;
    }

    /**
     * Capture the current screen to a BufferedImage at the given Axis video
     * server jpegURL.
     * 
     * @param jpegUrl
     *            The axis video server jpeg URL.
     * @return a BufferedImage if the screen capture succeeds, null otherwise.
     */
    public static BufferedImage captureScreen( String jpegUrl ) throws IOException
    {
        HttpURLConnection huc = null;
        DataInputStream dis = null;
        BufferedImage image = null;
        ImageInputStream iis = null;

        ImageIO.setUseCache( false );
        ImageReader imageDecoder = ( ImageReader ) ImageIO.getImageReadersByFormatName( "JPEG" ).next();

        try
        {
            URL u = new URL( jpegUrl );
            huc = ( HttpURLConnection ) u.openConnection();
            BufferedInputStream bis = new BufferedInputStream( huc.getInputStream() );
            dis = new DataInputStream( bis );
            iis = ImageIO.createImageInputStream( dis );
            imageDecoder.setInput( iis, true );
            image = imageDecoder.read( 0 );
        }
        finally
        {
            if ( huc != null )
            {
                huc.disconnect();
            }
            if ( null != dis )
            {
                try
                {
                    dis.close();
                }
                catch ( Exception e )
                {
                    // There isn't much to do.
                    e.printStackTrace();
                }
                dis = null;
            }
        }
        return image;
    }

    /*
     * Write a jpeg image to a file, and set compression explicitly to highest
     * quality.
     * 
     * @param img - Buffered Image to write to disk
     * 
     * @param f - file to write to
     */
    public static void writeHighQualityJpeg( BufferedImage img, File f ) throws IOException
    {
        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersByFormatName( "jpg" );
        if ( iter.hasNext() )
        {
            writer = ( ImageWriter ) iter.next();
        }
        // Prepare output file
        ImageOutputStream ios = ImageIO.createImageOutputStream( f );
        writer.setOutput( ios );

        ImageWriteParam iwparam = new JPEGImageWriteParam( Locale.getDefault() );
        iwparam.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
        // according to compression quality is between 0 and 1 (lowest to
        // highest quality)
        iwparam.setCompressionQuality( 1 );

        writer.write( null, new IIOImage( img, null, null ), iwparam );

        // Cleanup
        ios.flush();
        writer.dispose();
        ios.close();
    }
}
