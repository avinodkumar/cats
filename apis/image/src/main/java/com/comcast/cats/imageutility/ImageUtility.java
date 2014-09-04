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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides various utility functions for image enhancement
 * operations.
 * 
 * @author sijil cv
 * @author maneshthomas
 * 
 */

public class ImageUtility
{
    /**
     * Logger instance
     */
    public final static Logger   logger = LoggerFactory.getLogger( ImageUtility.class );
    /**
     * Source image.
     */
    private static BufferedImage image  = null;

    /**
     * Constructor of ImageUtility with file name as argument.
     */
    public ImageUtility( String file ) throws IOException
    {
        readImage( file );
    }

    /**
     * Constructor of ImageUtility which takes buffered image as argument.
     */
    public ImageUtility( BufferedImage bufImage )
    {
        image = bufImage;
    }

    /**
     * Returns the buffered image.
     * 
     * @return Buffered Image
     */
    public static BufferedImage getImage()
    {
        return image;
    }

    /**
     * Zoom the image.
     * 
     * @param image
     * @param scale
     * @throws IOException
     * @throws ImageUtilityException
     */
    public static void zoom( String imageName, int scale ) throws IOException, ImageUtilityException
    {

        String absPath = getAbsolutePathFromFileName( imageName );
        logger.info( "Processing \" " + absPath + " \" " + "With  Scale < " + scale + " >" );

        if ( scale <= 0 )
        {
            logger.error( " Zoom Value can't be Zero/negative" );
            throw new ImageUtilityException( "Zoom Value can't be Zero/Negative" );
        }

        BufferedImage bufImage = readImage( imageName );
        // cleanup old files.
        deleteFile( "./zoom.bmp" );

        if ( bufImage.getWidth() >= 1600 && bufImage.getHeight() >= 1200 )
        {
            logger.error( " Image Size is Too large To Zoom " + " Reduce the Image Width * Height" );
            throw new ImageUtilityException( " Image Size is Too large To Zoom " + " Reduce the Image Width * Height" );
        }

        if ( scale * bufImage.getWidth() >= 2400 && scale * bufImage.getHeight() >= 1600 )
        {
            logger.error( " Image Size is Too large To Zoom " + " Reduce the Image Width * Height" );
            throw new ImageUtilityException( " Image Size is Too large To Zoom " + " Reduce the Image Width * Height" );
        }
        logger.debug( "Starting Zoom Operation for image " + imageName );

        bufImage = zoom( bufImage, scale, scale ); // zoom in
        saveImage( bufImage, "./zoom.bmp" );

    }

    /**
     * Saves buffered image into specified location.
     * 
     * @param filename
     *            file name.
     * @throws FileNotFoundException
     * @throws ImageUtilityException
     * @throws IOException
     */
    public static void saveImage( BufferedImage bi, String filename ) throws ImageUtilityException, IOException
    {

        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream( filename );
        }
        catch ( java.io.FileNotFoundException io )
        {
            logger.error( " File Not Found" );
            throw new ImageUtilityException( io );
        }
        int index = filename.lastIndexOf( '.' );
        String extension = filename.substring( index + 1 );

        try
        {
            if ( extension.equals( "bmp" ) )
            {
                ImageIO.write( bi, "bmp", out );
            }
            else if ( extension.equals( "jpg" ) )
            {
                ImageIO.write( bi, "jpg", out );
            }
            else if ( extension.equals( "png" ) )
            {
                ImageIO.write( bi, "png", out );
            }
            logger.info( filename + " has been saved Sucessfully " );
        }
        catch ( java.io.IOException io )
        {
            logger.error( "IOException while trying to save :" + filename );
            throw new ImageUtilityException( io );
        }
        finally
        {
            out.close();
        }
    }

    /**
     * Saves the image as a PNG file.
     * 
     * @param bufImage
     *            Buffered Image to be saved.
     * @param path
     *            Location for saving the image.
     * @param filename
     *            File name.
     * @throws ImageUtilityException
     * @throws IOException
     * 
     */
    public static void saveImageAsPNG( BufferedImage bufImage, String filename ) throws ImageUtilityException,
            IOException
    {

        File file = new File( filename );
        String absolutePath = file.getAbsolutePath();
        checkDirectoryPath( absolutePath.substring( 0, absolutePath.lastIndexOf( File.separator ) ) );

        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream( filename );

            ImageIO.write( bufImage, "png", out );
            logger.info( filename + " has been saved Sucessfully " );
        }
        catch ( FileNotFoundException io )
        {
            logger.error( "File Not Found" );
            throw new ImageUtilityException( io );
        }
        catch ( IOException ioException )
        {
            logger.error( " IOException  while trying to save " + filename );
            throw new ImageUtilityException( ioException );
        }
        finally
        {
            out.close();
        }
    }

    /**
     * Checks the directory path, create the directory if not exists.
     * 
     * @param path
     *            directory path.
     * @throws ImageUtilityException 
     */
    private static void checkDirectoryPath( String path ) throws ImageUtilityException
    {
        File directory = new File( path );
        if ( !( directory.exists() ) )
        {
            logger.info( " Directory not exists....." + path );
            boolean status = directory.mkdirs();
            if ( !status )
            {
                logger.error( "Failed to create the specified directory structure" + path );
                throw new ImageUtilityException("Failed to create the specified directory structure" + path);
            }
        }

    }

    /**
     * Performs image zoom operation.
     * 
     * @param bufImage
     *            Input buffered image
     * @param xscale
     * @param yscale
     * @return BufferedImage
     * @throws ImageUtilityException
     */
    public static BufferedImage zoom( BufferedImage bufImage, float xscale, float yscale ) throws ImageUtilityException
    {

        int width = ( int ) ( bufImage.getWidth() * xscale );
        int height = ( int ) ( bufImage.getHeight() * yscale );
        BufferedImage biScale = null;

        try
        {
            biScale = new BufferedImage( width, height, bufImage.getType() ); // TYPE_4BYTE_ABGR

            AffineTransform tx = new AffineTransform();
            tx.scale( xscale, yscale );

            AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BICUBIC );

            biScale = op.filter( bufImage, null );

            return imageToBufferedImage( biScale );
        }
        catch ( java.lang.OutOfMemoryError outOfMemoryError )
        {
            logger.error( " Zoom  not possible \nReduce the Zoom Scale" + outOfMemoryError.getMessage() );
            throw new ImageUtilityException( "Zoom not possible, OutOfMemoryError" + outOfMemoryError.getMessage() );
        }
        catch ( java.lang.IllegalArgumentException illegalArgumentException )
        {
            logger.error( "IllegalArgumentException, Zoom not possible " + illegalArgumentException.getMessage() );
            throw new ImageUtilityException( "Zoom not possible, IllegalArgumentException"
                    + illegalArgumentException.getMessage() );
        }
    }

    /**
     * Perform image invert operation.
     * 
     * @param image
     *            Input Image
     * @return Inverted image
     */

    public static void invertImage( BufferedImage image )
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int new_rgb;

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                new_rgb = ( ~( image.getRGB( x, y ) ) );
                image.setRGB( x, y, new_rgb );
            }

        }

    }

    /**
     * Write the result in text file
     * 
     * @param name
     *            file name
     * @param result
     *            result string.
     * @throws ImageUtilityException 
     */
    public static void writeResult( String name, String result ) throws ImageUtilityException
    {
        try
        {
            String fileName = name;
            File f = new File( "./" + name );
            if ( f.exists() )
                f.delete();
            PrintWriter out = new PrintWriter( new File( fileName ) );
            out.println( result );
            out.close();
        }
        catch ( FileNotFoundException e )
        {
            logger.error( "FileNotFoundException" + e.getMessage() );
            throw new ImageUtilityException( "FileNotFoundException" + e.getMessage() );
        }
    }

    /**
     * convert image to buffered image
     * 
     * @param img
     *            Input Image
     * @return Converted BufferedImage
     */
    public static BufferedImage imageToBufferedImage( Image img )
    {
        BufferedImage bi = new BufferedImage( img.getWidth( null ), img.getHeight( null ), BufferedImage.TYPE_INT_RGB ); // TYPE_4BYTE_ABGR
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage( img, null, null );

        return bi;
    }

    /**
     * Generate Sharp Image.
     * 
     * @param image
     *            imput image
     * @param ie
     * @return
     */
    public static BufferedImage generateSharpImage( BufferedImage image )
    {
        float[] sharpelements =
            { -1f, -1f, -1f, -1f, 9f, -1f, -1f, -1f, -1f };// sharp
                                                           // Kernel

        Kernel sharpkernel = new Kernel( 3, 3, sharpelements );
        ConvolveOp sop = new ConvolveOp( sharpkernel, ConvolveOp.EDGE_NO_OP, null );
        image = sop.filter( image, null );
        return image;
    }

    /**
     * Generate Black and white image.
     */
    public static BufferedImage genBlackAndWhiteImage( BufferedImage image )
    {
        BufferedImage blackAndWhiteImage = new BufferedImage( image.getWidth( null ), image.getHeight( null ),
                BufferedImage.TYPE_BYTE_BINARY );

        Graphics2D g = ( Graphics2D ) blackAndWhiteImage.getGraphics();
        g.drawImage( image, 0, 0, null );
        g.dispose();
        image = blackAndWhiteImage;
        return image;
    }

    /**
     * Perform Rescale Operation.
     */
    public static BufferedImage performRescaleOperation( BufferedImage image, float scale, float offset )
    {
        RescaleOp brightenOp = new RescaleOp( scale, offset, null );
        image = brightenOp.filter( image, null );
        return image;
    }

    /**
     * Generate Blur Image.
     * 
     * @param image
     *            input image.
     */
    public static BufferedImage generateBlurImage( BufferedImage image )
    {
        float[] blurelement =
            { 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 7f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f // blur
                                                                                              // Kernel
            };

        Kernel blurKernel = new Kernel( 3, 3, blurelement );
        ConvolveOp simpleBlur = new ConvolveOp( blurKernel, ConvolveOp.EDGE_NO_OP, null );// EDGE_NO_OP,
                                                                                          // null);
        image = simpleBlur.filter( image, null ); // blur the image
        return image;
    }

    /**
     * Check if the file name exists or not.
     * 
     * @param fileName
     */
    public static boolean isFileNameExists( String fileName )
    {
        boolean status = false;
        if ( new File( fileName ).exists() )
        {
            status = true;
        }
        else
        {
            status = false;
        }
        return status;
    }

    /**
     * Reads the the Image from the image path.
     * 
     * @param imagePath
     *            location of the image
     * @throws MalformedURLException
     * @throws IOException
     * @return
     */
    public static BufferedImage readImage( String imagePath ) throws MalformedURLException, IOException
    {
        BufferedImage image = null;
        if ( imagePath.substring( 0, 4 ).equals( "http" ) )
        {

            URL url = new URL( imagePath );
            image = ImageIO.read( url );

        }
        else
        {

            File Image = new File( imagePath );
            image = ImageIO.read( Image );
        }
        return image;
    }

    /**
     * Creates TimeStamp For Logging.
     * 
     * @return timeStamp details from Current Time.
     */
    public static String createTimeStamps()
    {
        java.util.Date today = new java.util.Date();
        SimpleDateFormat date1 = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss.SSS" );
        return ( date1.format( today ) );
    }

    /**
     * Writes Messages to file.
     * 
     * @param logWriter
     *            LogWriter Reference
     * @param str
     *            Message
     */

    public static void writeToFile( BufferedWriter logWriter, String str ) throws IOException
    {

        try
        {

            logWriter.write( str );
            logWriter.flush();

        }
        catch ( IOException io )
        {
            logger.error( "Exception while writing to file." + io.getMessage() );
            throw new IOException( "Exception while writing to file." + io.getMessage() );
        }

    }

    /**
     * Appends a line to Log file.
     * 
     * @param logWriter
     *            Log Writer reference
     * @throws IOException
     */
    public static void appendLine( BufferedWriter logWriter ) throws IOException
    {
        try
        {
            logWriter.newLine();
        }
        catch ( IOException io )
        {
            logger.error( "Exception while trying to append  the writer." + io.getMessage() );
            throw new IOException( "Exception while closing the writer." + io.getMessage() );
        }

    }

    /**
     * Closes the log writer.
     * 
     * @param logWriter
     *            logWriter reference
     * @throws IOException
     */
    public static void closingWriter( BufferedWriter logWriter ) throws IOException
    {

        try
        {
            if ( logWriter != null )
            {
                logWriter.close();
            }
        }
        catch ( IOException io )
        {
            logger.error( "Exception while closing the writer." + io.getMessage() );
            throw new IOException( "Exception while closing the writer." + io.getMessage() );
        }

    }

    /**
     * Deletes the specified file.
     * 
     * @param fileName
     */
    public static void deleteFile( String fileName )
    {
        File file = new File( fileName );

        if ( file.exists() )
            file.delete();
    }

    /**
     * Returns absolutepath from File name.
     * 
     * @param image
     *            file name
     * @return absolutepath
     */
    private static String getAbsolutePathFromFileName( String image )
    {
        String absPath;
        if ( !( image.substring( 0, 4 ).equals( "http" ) ) )
        {
            File imageName = new File( image );
            absPath = imageName.getAbsolutePath();
        }
        else
        {
            absPath = image;
        }
        return absPath;
    }

}
