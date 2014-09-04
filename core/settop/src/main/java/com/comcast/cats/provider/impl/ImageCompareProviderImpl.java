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
/*
 * Image Compare ProviderImpl v0.2
 */
package com.comcast.cats.provider.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.Settop;
import com.comcast.cats.image.BufferedImageLoader;
import com.comcast.cats.image.CoreImageCompare;
import com.comcast.cats.image.CoreRegionCompare;
import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.ImageCompareResult;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.exceptions.ImageCompareException;

/**
 * Provides Implementation of ImageCompareProvider.
 * 
 * @author sajayjk
 * 
 */
public class ImageCompareProviderImpl extends RegionLocatorProviderImpl implements ImageCompareProvider
{

    /**
     * 
     */
    private static final long         serialVersionUID = 1L;
    private VideoProvider             videoProvider    = null;
    private int                       driftX           = 0;
    private int                       driftY           = 0;
    private BufferedImageLoader       imageLoader;

    private int                       redTolerance     = ImageCompareRegionInfo.DEFAULT_RED_TOLERANCE;
    private int                       greenTolerance   = ImageCompareRegionInfo.DEFAULT_GREEN_TOLERANCE;
    private int                       blueTolerance    = ImageCompareRegionInfo.DEFAULT_BLUE_TOLERANCE;
    private float                     matchPercent     = ImageCompareRegionInfo.DEFAULT_MATCH_PERCENT;

    private int                       xTolerance       = RegionInfo.DEFAULT_X_TOLERANCE;
    private int                       yTolerance       = RegionInfo.DEFAULT_Y_TOLERANCE;

    // Global counter used to avoid name collisions if
    // multiple comparisons fail within a very short time.
    // Unsure properly synchronized access!
    private static int                counter          = 0;

    private static final String       imageSaveFormat  = "%1$tY%1$tm%1$td-%1$tH%1$tM%1$tS-%2$04d";
    private static final String       actualFileExtn   = "-actual.jpg";
    private static final String       expectedFileExtn = "-expected.jpg";

    private String                    imageSaveLocation;
    private static final Logger       logger           = LoggerFactory.getLogger( ImageCompareProviderImpl.class );
    private RegionLocatorProviderImpl regionLocatorProvider;

    public ImageCompareProviderImpl( Class< ? > resourceClass, VideoProvider videoProvider, int driftX, int driftY,
            Properties theProps )
    {

        logger.debug( "Instatiating ImageCompareProviderImpl" );
        this.videoProvider = videoProvider;
        this.regionLocatorProvider = new RegionLocatorProviderImpl( resourceClass );
        this.driftX = driftX;
        this.driftY = driftY;
        imageLoader = new BufferedImageLoader( resourceClass, "setup" );
        if ( theProps != null )
        {
            String matchPct = theProps.getProperty( "matchPercent",
                    String.valueOf( ImageCompareRegionInfo.DEFAULT_MATCH_PERCENT ) );
            String redt = theProps.getProperty( "redtolerance",
                    String.valueOf( ImageCompareRegionInfo.DEFAULT_RED_TOLERANCE ) );
            String greent = theProps.getProperty( "greentolerance",
                    String.valueOf( ImageCompareRegionInfo.DEFAULT_GREEN_TOLERANCE ) );
            String bluet = theProps.getProperty( "bluetolerance",
                    String.valueOf( ImageCompareRegionInfo.DEFAULT_BLUE_TOLERANCE ) );
            imageSaveLocation = theProps.getProperty( "imageSaveLocation", null ).trim();
            setMatchPercent( Float.parseFloat( matchPct ) );
            setGreenTolerance( Integer.parseInt( greent ) );
            setRedTolerance( Integer.parseInt( redt ) );
            setBlueTolerance( Integer.parseInt( bluet ) );
        }
        ImageIO.setUseCache( false );
    }

    /**
     * to get DriftX
     */
    public int getDriftX()
    {
        return driftX;
    }

    /**
     * to set DriftX
     * @param driftX
     */
    public void setDriftX( int driftX )
    {
        this.driftX = driftX;
    }

    /**
     * to get DriftY
     */
    public int getDriftY()
    {
        return driftY;
    }

    /**
     * to set DriftY
     * @param driftY
     */
    public void setDriftY( int driftY )
    {
        this.driftY = driftY;
    }
    
    /**
     * To get the image save location of failed comparisons.
     * @return directoryPath
     */
    public String getImageSaveLocation()
    {
        return imageSaveLocation;
    }
    
    /**
     * To set the the directory location for saving the images of failed comparison.
     * @param directoryPath String
     */
    public void setImageSaveLocation( String directoryPath)
    {
        this.imageSaveLocation = directoryPath;
    }

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file.
     * 
     * @param imgXMLPath
     *            The image xml file.
     * @param regionName
     *            The region to wait for.
     * @return true if the region is on screen. False otherwise.
     */
    @Deprecated
    public boolean isRegionOnScreenNow( String imgXMLPath, String regionName )
    {
        boolean retVal = false;
        RegionInfo regionInfo = null;
        if ( imgXMLPath != null && !imgXMLPath.isEmpty() && regionName != null && !regionName.isEmpty() )
        {
            try
            {
                regionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, regionName );
                logger.debug( "RegionInfo " + regionInfo );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
                return false;
            }
            if ( null != regionInfo )
            {
                ImageCompareRegionInfo icRegionInfo = convertToImageCompareRegionInfo( regionInfo );
                try
                {
                    retVal = isRegionOnScreenNow( icRegionInfo );
                }
                catch ( ImageCompareException e )
                {
                    logger.debug( e.getMessage() );
                }
                logger.info( "Result of comparision " + retVal );
            }
        }
        return retVal;
    }

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param icRegionInfo
     *            The region metadata as ImageCompareRegionInfo.
     *            
     * @return true if the region is on screen. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean isRegionOnScreenNow( ImageCompareRegionInfo icRegionInfo ) throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != icRegionInfo )
        {
            logger.debug( "Image Filepath " + icRegionInfo.getFilepath() );
            BufferedImage region = imageLoader.loadImage( icRegionInfo.getFilepath() );
            if ( null == region )
            {
                logger.error( "Could not load image: " + imageLoader.getPath() );
                throw new IllegalStateException( "Could not load image: " + imageLoader.getPath() );
            }
            retVal = isRegionOnScreenNow( icRegionInfo, region );
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Checks if all the images regions are on the current screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file. Maintained for Backward Compatibility
     * 
     * @param imgXMLPath
     *            The image xml file.
     * @return true if all the images regions are on screen. False otherwise.
     */
    @Deprecated
    public boolean areAllRegionsOnScreenNow( String imgXMLPath )
    {
        boolean retVal = false;
        List< RegionInfo > regionInfoList = null;
        if ( imgXMLPath != null && !imgXMLPath.isEmpty() )
        {
            try
            {
                regionInfoList = regionLocatorProvider.getRegionInfo( imgXMLPath );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
            }
            if ( null != regionInfoList )
            {
                List< ImageCompareRegionInfo > icRegionInfo = null;
                icRegionInfo = convertToImageCompareRegionInfo( regionInfoList );
                try
                {
                    retVal = areAllRegionsOnScreenNow( icRegionInfo );
                }
                catch ( ImageCompareException e )
                {
                    logger.debug( e.getMessage() );
                }
                logger.info( "Result of comparision " + retVal );
            }
        }
        return retVal;
    }

    /**
     * Checks if all the images regions are on the current screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * regionList.
     * 
     * @param regionList
     *            List of region metadata's
     * @return true if all the images regions are on screen. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean areAllRegionsOnScreenNow( List< ImageCompareRegionInfo > regionList ) throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != regionList )
        {
            try
            {
                retVal = doRegionCompare( regionList, null );
                logger.info( "Result of comparision " + retVal );
            }
            catch ( IllegalStateException e )
            {
                logger.error( e.getMessage(), e );
                retVal = false;
            }
            catch ( ImageCompareException e )
            {
                throw new ImageCompareException( e.getMessage() );
            }
        }
        return retVal;
    }

    /**
     * Checks if the full image matches the current screen. This function uses
     * this class's match percent and RGB tolerances. Maintained for Backward
     * Compatibility
     * 
     * @param imgFile
     *            The image file.
     * @return true if the image is on the current screen.  False otherwise.
     */
    @Deprecated
    public boolean isSameImageOnScreenNow( String imgFile )
    {
        boolean retVal = false;
        if ( null != imgFile && !imgFile.isEmpty() )
        {
            try
            {
                retVal = isImageOnScreenNow( imageLoader.loadImage( imgFile ) );
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Checks if the full image matches the current screen. This function uses
     * this class's match percent and RGB tolerances.
     * 
     * @param refImage
     *            The image as BufferedImage.
     * @return true if the image is on the current screen. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean isImageOnScreenNow( BufferedImage refImage ) throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != refImage )
        {
            BufferedImage currentScreen = getCurrentImage( refImage );
            if ( null != currentScreen )
            {
                retVal = doFullScreenCompare( currentScreen, refImage );
                logger.info( "Result of comparision " + retVal );
            }
        }
        return retVal;
    }

    /**
     * Waits for the full image to match the current screen within the given
     * timeout. This function uses this class's match percent and RGB
     * tolerances. Maintained for Backward Compatibility
     * 
     * @param imgFile
     *            The image file.
     * @param timeout
     *            The timeout in milliseconds.
     * @return true if the image is on the screen within the timeout. False otherwise.
     */
    @Deprecated
    public boolean waitForSameImage( String imgFile, long timeout )
    {
        boolean retVal = false;
        if ( null != imgFile && !imgFile.isEmpty() )
        {
            try
            {
                retVal = waitForImageOnScreen( imageLoader.loadImage( imgFile ), timeout );
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the full image to match the current screen within the given
     * timeout. This function uses this class's match percent and RGB
     * tolerances.
     * 
     * @param refImage
     *            The image as BufferedImage.
     * @param timeout
     *            The timeout in milliseconds.
     * @return true if the image is on the screen within the timeout. False otherwise.         
     * @throws ImageCompareException        
     */
    @Override
    public boolean waitForImageOnScreen( BufferedImage refImage, long timeout ) throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != refImage )
        {
            retVal = runCallableTask( new CallableFullImageCompare( refImage ), timeout );
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the image to have all its regions on screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file. Maintained for Backward Compatibility
     * 
     * @param imgXMLPath
     *            The image xml file.
     * @param timeout
     *            the timeout.
     * @return true if all regions are on the screen within the timeout.
     *         False otherwise.
     */
    @Deprecated
    public boolean waitForAllRegions( String imgXMLPath, long timeout )
    {
        boolean retVal = false;
        List< RegionInfo > regionInfoList = null;
        if ( null != imgXMLPath && !imgXMLPath.isEmpty() )
        {
            try
            {
                regionInfoList = regionLocatorProvider.getRegionInfo( imgXMLPath );
                logger.debug( "RegionInfoList " + regionInfoList );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
                return false;
            }
            List< ImageCompareRegionInfo > icRegionInfo = null;
            if ( null != regionInfoList )
            {
                icRegionInfo = convertToImageCompareRegionInfo( regionInfoList );
            }
            try
            {
                retVal = waitForAllRegions( icRegionInfo, timeout );
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the image to have all its regions on screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionList.
     * 
     * @param regionList
     *            List of region metadata's
     * @param timeout
     *            the timeout.
     * @return true if all regions are on the screen within the timeout.False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean waitForAllRegions( List< ImageCompareRegionInfo > regionList, long timeout )
            throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != regionList )
        {
            CallableMultipleRegionCompare compare = null;
            try
            {
                compare = new CallableMultipleRegionCompare( regionList );
            }
            catch ( Exception e )
            {
                logger.error( e.getMessage(), e );
                return false;
            }
            retVal = runCallableTask( compare, timeout );
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the image to have the specified region on screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file. Maintained for Backward Compatibility
     * 
     * @param imgXMLPath
     *            The image xml file.
     * @param regionName
     *            The name of the region to wait for.
     * @param timeout
     *            the timeout.
     * @return true if the region is on the screen within the timeout. False otherwise.
     */
    @Deprecated
    public boolean waitForRegion( String imgXMLPath, String regionName, long timeout )
    {
        boolean retVal = false;
        if ( null != imgXMLPath && !imgXMLPath.isEmpty() && null != regionName && !regionName.isEmpty() )
        {
            RegionInfo regionInfo = null;
            try
            {
                regionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, regionName );
                logger.debug( "RegionInfo " + regionInfo );
                if ( null != regionInfo )
                {
                    ImageCompareRegionInfo icRegionInfo = convertToImageCompareRegionInfo( regionInfo );
                    retVal = waitForRegion( icRegionInfo, timeout );
                    logger.info( "Result of comparision " + retVal );
                }
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
                retVal = false;
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
        }
        return retVal;
    }

    /**
     * Waits for the image to have the specified region on screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param regionInfo
     *            The region metadata as ImageCompareRegionInfo
     * @param timeout
     *            the timeout.
     * @return true if the region is on the screen within the timeout. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean waitForRegion( ImageCompareRegionInfo regionInfo, long timeout ) throws ImageCompareException
    {
        boolean retVal = false;

        if ( null != regionInfo )
        {
            regionInfo = ( ImageCompareRegionInfo ) addDrift( regionInfo );
            logger.debug( "RegionInfo " + regionInfo );
            retVal = runCallableTask( new CallableRegionCompare( regionInfo ), timeout );
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * To get the parent object.
     * @return Object
     */
    @Override
    public Object getParent()
    {
        return null;
    }

    private ImageCompareRegionInfo convertToImageCompareRegionInfo( RegionInfo regionInfo )
    {
        ImageCompareRegionInfo info = null;
        /**
         * If region Info instance of ImageCompareRegionInfo, then there is not
         * need to set default values of RGB tolerance and matchPercent
         */
        if ( !( regionInfo instanceof ImageCompareRegionInfo ) )
        {
            logger.debug( "Converting to ImageCompareRegionInfo" );
            info = new ImageCompareRegionInfo();
            info.setBlueTolerance( ( int ) blueTolerance );
            info.setRedTolerance( ( int ) redTolerance );
            info.setGreenTolerance( ( int ) greenTolerance );
            info.setMatchPct( matchPercent );
            info.setFilepath( regionInfo.getFilepath() );
            info.setHeight( regionInfo.getHeight() );
            info.setWidth( regionInfo.getWidth() );
            info.setName( regionInfo.getName() );
            info.setX( regionInfo.getX() );
            info.setY( regionInfo.getY() );
            info.setXTolerance( regionInfo.getXTolerance() );
            info.setYTolerance( regionInfo.getYTolerance() );
        }
        else
        {
            info = ( ImageCompareRegionInfo ) regionInfo;
        }
        return info;
    }

    private List< ImageCompareRegionInfo > convertToImageCompareRegionInfo( List< RegionInfo > regionInfoList )
    {
        logger.debug( "Converting to ImageCompareRegionInfo" );
        List< ImageCompareRegionInfo > retVal = new ArrayList< ImageCompareRegionInfo >();

        for ( RegionInfo regionInfo : regionInfoList )
        {
            retVal.add( convertToImageCompareRegionInfo( regionInfo ) );
        }
        return retVal;
    }

    /**
     * Adds the drift values to RegionInfo object.
     * 
     * @param info
     *            RegionInfo object for which the drift values needs to be
     *            updated.
     * @return RegionInfo Updated RegionInfo object with image drift values.
     * 
     */
    private RegionInfo addDrift( RegionInfo info )
    {
        logger.debug( "Adding Drift" );
        int xValue = info.getX() + driftX;
        int yValue = info.getY() + driftY;
        info.setX( xValue );
        info.setY( yValue );
        return info;
    }

    private boolean doRegionCompare( List< ImageCompareRegionInfo > regionList, BufferedImage refImage )
            throws ImageCompareException
    {
        logger.debug( "doRegionCompare()" );
        if ( null == regionList )
        {
            logger.debug( "regionList == null" );
            return false;
        }
        boolean returnCode = false;

        // taking any region (in this case the first region) to get the filepath
        // of the image to be loaded.
        if ( refImage == null )
        { // try loading reference image from the filepath
          // mentioned in xml.
            ImageCompareRegionInfo firstRegion = regionList.get( 0 );
            if ( null != firstRegion )
            {
                logger.info( "Image filepath : " + firstRegion.getFilepath() );
                refImage = imageLoader.loadImage( firstRegion.getFilepath() );
                if ( null == refImage )
                {
                    logger.error( "Could not load image: " + imageLoader.getPath() );
                    throw new IllegalStateException( "Could not load image: " + imageLoader.getPath() );
                }
            }
        }
        try
        {
            BufferedImage currentScreen = getCurrentImage( refImage );
            if ( null == currentScreen )
            {
                logger.debug( "currentScreen == null" );
                return false;
            }
            if ( CoreRegionCompare.doRegionCompare( regionList, refImage, currentScreen ) )
            {
                returnCode = true;
            }
            else
            {
                saveImages( currentScreen, refImage );
            }
        }
        catch ( Exception e )
        {
            throw new ImageCompareException( e.getMessage() );
        }
        return returnCode;
    }

    private void saveImages( BufferedImage actual, BufferedImage expected )
    {
        saveImages( actual, expected, null, null );
    }

    private void saveImages( BufferedImage actual, BufferedImage expected, RegionInfo targetInfo, ImageCompareRegionInfo refInfo )
    {
        logger.debug( "saveImages()" );
        if ( null == imageSaveLocation || imageSaveLocation.isEmpty() )
        {
            logger.warn( "[IMAGECOMPARE] imagecompare directory not set" );
            return;
        }

        File outputDir = new File( imageSaveLocation );

        // Create a file name from the current date/time and the failure
        // counter.
        // Output format is YYYYmmdd-HHMMSS-0000.
        int failureCount = getFailureCount();
        String filename = String.format( imageSaveFormat, Calendar.getInstance(), Integer.valueOf( failureCount ) );
        if ( null != refInfo )
        {
            filename += "-" + refInfo.getName();
        }
        logger.debug( "filename " + filename );
        File actualFile = new File( outputDir, filename + actualFileExtn );
        File expectedFile = new File( outputDir, filename + expectedFileExtn );
        if ( outputDir.isDirectory() || outputDir.mkdirs() )
        {
            if ( saveImage( actualFile, actual, ( null != targetInfo )? targetInfo : refInfo ) )
            {
                logger.info( "[IMAGECOMPARE] Saved actual image: " + actualFile.getName() );
            }
            else
            {
                logger.error( "[IMAGECOMPARE] Failed to save actual image." );
            }

            if ( saveImage( expectedFile, expected, refInfo ) )
            {
                logger.info( "[IMAGECOMPARE] Saved expected image: " + expectedFile.getName() );
            }
            else
            {
                logger.error( "[IMAGECOMPARE] Failed to save expected image." );
            }
        }
        else
        {
            logger.error( "[IMAGECOMPARE] Failed to create imagecompare directory." );
        }
    }

    private boolean saveImage( File outputFile, BufferedImage image, RegionInfo info )
    {
        boolean savedFile = false;
        try
        {
            if ( null != info )
            {
                // draw compare region on image
                Graphics g = image.getGraphics();
                g.setXORMode( Color.RED );
                if ( g instanceof Graphics2D )
                {
                    Stroke stroke = new BasicStroke( 1.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL );
                    ( ( Graphics2D ) g ).setStroke( stroke );
                }
                g.drawRect( info.getX(), info.getY(), info.getWidth(), info.getHeight() );
            }
            // XXX: Saving the image as a (lossy) JPEG may make debugging more
            // difficult
            // as the lossiness may affect future comparison.
            savedFile = ImageIO.write( image, "JPEG", outputFile );
        }
        catch ( IOException ioe )
        {
            logger.error( ioe.getMessage(), ioe );
        }
        return savedFile;
    }

    /**
     * Increments the global counter and returns the new value. This method must
     * be synchronized.
     * 
     * @return The next number in the sequence.
     */
    private synchronized int getFailureCount()
    {
        return ++counter;
    }

    /**
     * Runs a full image compare between two specified image files.
     * 
     * @param actual
     *            The actual image.
     * @param expected
     *            The expected image.
     * @return true if the images are equal.
     * @throw IllegalStateException if the image cannot be loaded.
     */
    private boolean doFullScreenCompare( BufferedImage actual, BufferedImage expected ) throws ImageCompareException
    {
        boolean returnCode = false;
        if ( null != actual && null != expected )
        {
            try
            {
                returnCode = CoreImageCompare.compareImages( actual, expected, matchPercent, redTolerance,
                        greenTolerance, blueTolerance );
                logger.info( "Result of comparision " + returnCode );
            }
            catch ( Exception e )
            {
                logger.error( e.getMessage() );
                throw new ImageCompareException( e.getMessage() );
            }
        }
        if ( false == returnCode )
        {
            saveImages( actual, expected );
        }

        return returnCode;
    }

    /**
     * Sets the match percent required for a full image compare to pass. This
     * value must be between 0 and 100.
     * 
     * @param matchPercent
     *            The match percent.
     */
    public final void setMatchPercent( float matchPercent )
    {
        if ( matchPercent < ImageCompareRegionInfo.MIN_MATCH_PERCENT
                || matchPercent > ImageCompareRegionInfo.MAX_MATCH_PERCENT )
        {
            throw new IllegalArgumentException( "matchPercent must be >= " + ImageCompareRegionInfo.MIN_MATCH_PERCENT
                    + " and <= " + ImageCompareRegionInfo.MAX_MATCH_PERCENT );
        }
        this.matchPercent = matchPercent;
    }

    /**
     * Gets the match percent required for a full image compare to pass.
     * 
     * @return The match percent.
     */
    public final float getMatchPercent()
    {
        return matchPercent;
    }

    /**
     * Set the red tolerance used for full image comparison. Value must >=
     * MIN_TOLERANCE and <= MAX_TOLERANCE.
     * 
     * @param redTolerance
     *            the redTolerance to set.
     */
    public final void setRedTolerance( int redTolerance )
    {
        checkTolerance( redTolerance );
        this.redTolerance = redTolerance;
    }

    /**
     * Get the red tolerance used for full image comparison.
     * 
     * @return the redTolerance
     */
    public final long getRedTolerance()
    {
        return redTolerance;
    }

    /**
     * Set the green tolerance used for full image comparison. Value must >=
     * MIN_TOLERANCE and <= MAX_TOLERANCE.
     * 
     * @param greenTolerance
     *            the greenTolerance to set
     */
    public final void setGreenTolerance( int greenTolerance )
    {
        checkTolerance( greenTolerance );
        this.greenTolerance = greenTolerance;
    }

    /**
     * Get the green tolerance used for image comparison.
     * 
     * @return the greenTolerance
     */
    public final long getGreenTolerance()
    {
        return greenTolerance;
    }

    /**
     * Set the blue tolerance used for full image comparison. Value must >=
     * MIN_TOLERANCE and <= MAX_TOLERANCE.
     * 
     * @param blueTolerance
     *            the blueTolerance to set
     */
    public final void setBlueTolerance( int blueTolerance )
    {
        checkTolerance( blueTolerance );
        this.blueTolerance = blueTolerance;
    }

    /**
     * Get the blue tolerance used for full image comparison.
     * 
     * @return the blueTolerance
     */
    public final long getBlueTolerance()
    {
        return blueTolerance;
    }

    private void checkTolerance( long value )
    {
        if ( value < ImageCompareRegionInfo.MIN_RGB_TOLERANCE || value > ImageCompareRegionInfo.MAX_RGB_TOLERANCE )
        {
            throw new IllegalArgumentException( "tolerance must be >= " + ImageCompareRegionInfo.MIN_RGB_TOLERANCE
                    + " and <= " + ImageCompareRegionInfo.MAX_RGB_TOLERANCE );
        }
    }

    private ImageCompareResult runCallableSearchRegionTask( CallableSearchForRegionOnTargetRegion compare, long timeout )
            throws ImageCompareException
    {
        ImageCompareResult retVal = new ImageCompareResult();
        if ( timeout > 0 )
        {
            ExecutorService service = Executors.newSingleThreadExecutor();

            Future< ImageCompareResult > taskFuture = service.submit( compare );
            try
            {
                retVal = taskFuture.get( timeout, TimeUnit.MILLISECONDS );
            }
            catch ( CancellationException e )
            {
                logger.error( "Task canceled for finding out region: " + compare.refRegionInfo.getName() +
                        " on search region: " + compare.targetRegionInfo.getName() );
                throw new ImageCompareException( e.getMessage() );
            }
            catch ( ExecutionException ee )
            {
                logger.error( "ExecutionException while finding out region: " + compare.refRegionInfo.getName() +
                        " on search region: " + compare.targetRegionInfo.getName(), ee );
                throw new ImageCompareException( ee.getMessage() );
            }
            catch ( InterruptedException ie )
            {
                logger.error( "InterruptedException while finding out region: " + compare.refRegionInfo.getName() +
                        " on search region: " + compare.targetRegionInfo.getName(), ie );
                throw new ImageCompareException( ie.getMessage() );
            }
            catch ( TimeoutException te )
            {
                logger.error( "Task timed out for finding out region: " + compare.refRegionInfo.getName() +
                        " on search region: " + compare.targetRegionInfo.getName(), te );
                taskFuture.cancel( true );
            }
            finally
            {
                // Just to be safe, make sure nothing else is running.
                service.shutdownNow();
                try
                {
                    // wait for it to complete.
                    service.awaitTermination( timeout, TimeUnit.MILLISECONDS );
                }
                catch ( InterruptedException ie )
                {
                    logger.error( "InterruptedException while finding out region: " + compare.refRegionInfo.getName() +
                            " on search region: " + compare.targetRegionInfo.getName(), ie );
                }
            }
        }
        return retVal;
    }

    private boolean runCallableTask( Callable< Boolean > compare, long timeout ) throws ImageCompareException
    {
        boolean retVal = false;
        if ( timeout > 0 )
        {
            ExecutorService service = Executors.newSingleThreadExecutor();

            Future< Boolean > taskFuture = service.submit( compare );
            try
            {
                retVal = taskFuture.get( timeout, TimeUnit.MILLISECONDS );
            }
            catch ( CancellationException e )
            {
                logger.error( "Task canceled" );
                throw new ImageCompareException( e.getMessage() );
            }
            catch ( ExecutionException ee )
            {
                logger.error( "ExecutionException: " + ee.getMessage(), ee );
                throw new ImageCompareException( ee.getMessage() );
            }
            catch ( InterruptedException ie )
            {
                logger.error( "InterruptedException: " + ie.getMessage(), ie );
                throw new ImageCompareException( ie.getMessage() );
            }
            catch ( TimeoutException te )
            {
                logger.error( "Task timed out" );
                taskFuture.cancel( true );
            }
            finally
            {
                // Just to be safe, make sure nothing else is running.
                service.shutdownNow();
                try
                {
                    // wait for it to complete.
                    service.awaitTermination( timeout, TimeUnit.MILLISECONDS );
                }
                catch ( InterruptedException ie )
                {
                    logger.error( ie.getMessage(), ie );
                }
            }
        }
        return retVal;
    }

    /**
     * A Callable class that performs the actual full screen image compare on a
     * separate thread. This class helps to compare images against a timeout
     * with live video streaming (simulated of course!!)
     * 
     * @author sajayjk
     */
    protected class CallableFullImageCompare implements Callable< Boolean >
    {
        private BufferedImage refImage;

        CallableFullImageCompare( BufferedImage refImage )
        {
            logger.debug( "CallableFullImageCompare Constructor" );
            this.refImage = refImage;
            if ( null != refImage )
            {
                this.refImage = refImage;
            }
            else
            {
                throw new IllegalStateException( "Image load failed.." );
            }
        }

        /**
         * compare the images, or throws an exception if unable to do so.
         *
         * @return true if able to compare the images.
         * @throws Exception if unable to compare the images.
         */
        @Override
        public Boolean call() throws Exception
        {
            Thread.currentThread().setName( "CallableFullImageCompare" );
            boolean retVal = false;
            BufferedImage currentScreen = null;

            while ( !Thread.currentThread().isInterrupted() )
            {
                currentScreen = getCurrentImage( refImage );
                logger.debug( "CallableFullImageCompare.call() currentScreen " + currentScreen );
                if ( null != currentScreen
                        && CoreImageCompare.compareImages( currentScreen, refImage, matchPercent, redTolerance,
                                greenTolerance, blueTolerance ) )
                {
                    retVal = true;
                    break;
                }
                try
                {
                    Thread.sleep( 100 );
                }
                catch ( InterruptedException ie )
                {
                    logger.error( ie.getMessage() );
                    break;
                }
            }

            if ( !retVal && currentScreen != null )
            {
                saveImages( currentScreen, getClone( refImage ) );
            }

            return retVal;
        }
    }

    /**
     * A Callable class that performs the actual region compares on a separate
     * thread. This class helps to compare multiple regions against a timeout
     * with live video streaming (simulated of course!!)
     * 
     * @author sajayjk
     */
    protected class CallableMultipleRegionCompare implements Callable< Boolean >
    {
        List< ImageCompareRegionInfo > regionList;
        BufferedImage                  refImage = null;

        CallableMultipleRegionCompare( List< ImageCompareRegionInfo > regionList )
        {
            logger.debug( "CallableFullImageCompare Constructor" );
            if ( null == regionList || regionList.isEmpty() )
            {
                throw new IllegalArgumentException( "regionList is either null or empty" );
            }

            ImageCompareRegionInfo sampleRegion = ( ImageCompareRegionInfo ) regionList.get( 0 );
            logger.debug( "Image filepath :" + sampleRegion.getFilepath() );
            refImage = imageLoader.loadImage( sampleRegion.getFilepath() );

            if ( null == refImage )
            {
                throw new IllegalStateException( "Could not load image: " + imageLoader.getPath() );
            }

            this.regionList = regionList;
        }

        CallableMultipleRegionCompare( List< ImageCompareRegionInfo > regionList, BufferedImage refImage )
        {
            logger.debug( "CallableFullImageCompare Constructor" );
            if ( null == regionList || regionList.isEmpty() || null == refImage )
            {
                throw new IllegalArgumentException( "regionList is either null or empty" );
            }
            this.regionList = regionList;
            this.refImage = refImage;
        }

        /**
         * compare the region, or throws an exception if unable to do so.
         *
         * @return true if able to compare the region.
         * @throws Exception if unable to compare the region.
         */
        @Override
        public Boolean call() throws Exception
        {
            boolean retVal = false;
            BufferedImage currentScreen = null;
            while ( !Thread.currentThread().isInterrupted() )
            {
                currentScreen = getCurrentImage( refImage );
                logger.debug( "CallableMultipleRegionCompare.call() currentScreen " + currentScreen );
                if ( null != currentScreen && CoreRegionCompare.doRegionCompare( regionList, refImage, currentScreen ) )
                {
                    retVal = true;
                    break;
                }
                try
                {
                    Thread.sleep( 100 );
                }
                catch ( InterruptedException ie )
                {
                    logger.error( ie.getMessage() );
                    break;
                }
            }

            if ( !retVal && currentScreen != null )
            {
                for ( ImageCompareRegionInfo regionInfo : regionList )
                {
                    saveImages( currentScreen, refImage, null, regionInfo );
                }
            }
            return retVal;
        }
    }

    protected class CallableSearchForRegionOnTargetRegion implements Callable< ImageCompareResult >
    {
        private ImageCompareRegionInfo refRegionInfo;
        private RegionInfo             targetRegionInfo;
        BufferedImage                  refImage;

        public CallableSearchForRegionOnTargetRegion( ImageCompareRegionInfo refRegionInfo, RegionInfo targetRegionInfo )
        {
            this.refRegionInfo = refRegionInfo;
            refImage = imageLoader.loadImage( refRegionInfo.getFilepath() );
            if ( null == refImage )
            {
                throw new IllegalStateException( "Could not load image: " + imageLoader.getPath() );
            }
            this.targetRegionInfo = targetRegionInfo;
        }

        public CallableSearchForRegionOnTargetRegion( ImageCompareRegionInfo refRegionInfo,
                RegionInfo targetRegionInfo, BufferedImage refImage )
        {
            if ( null == refImage )
            {
                throw new IllegalStateException( "No refImage Available" );
            }

            this.refRegionInfo = refRegionInfo;
            this.targetRegionInfo = targetRegionInfo;
            this.refImage = refImage;
        }

        /**
         * compare the region on target, or throws an exception if unable to do so.
         *
         * @return true if able to compare the region on target.
         * @throws Exception if unable to compare the region on target.
         */
        @Override
        public ImageCompareResult call() throws Exception
        {
            ImageCompareResult icResult = null;
            BufferedImage currentScreen = null;
            while ( !Thread.currentThread().isInterrupted() )
            {
                currentScreen = getCurrentImage( refImage );
                logger.debug( "CallableSearchForRegionOnTargetRegion.call() currentScreen " + currentScreen );
                if ( null != currentScreen )
                {
                    icResult = CoreRegionCompare.findRegionOnTargetRegion( refRegionInfo, targetRegionInfo, refImage,
                            currentScreen );
                    if( icResult.getResult() )
                    {
                        break;
                    }
                }
                try
                {
                    Thread.sleep( 100 );
                }
                catch ( InterruptedException ie )
                {
                    logger.error( ie.getMessage() );
                    break;
                }
            }

            if ( !icResult.getResult() && null != currentScreen )
            {
                saveImages( currentScreen, getClone( refImage), targetRegionInfo, refRegionInfo );
            }

            return icResult;
        }
    }

    private BufferedImage getClone( BufferedImage image )
    {        
        ColorModel colorModel = image.getColorModel();
        WritableRaster raster = image.copyData( null );
        return new BufferedImage( colorModel, raster, colorModel.isAlphaPremultiplied(), null );
    }

    /**
     * A Callable class that performs the actual region compare on a separate
     * thread. This class helps to compare a region against a timeout with live
     * video streaming (simulated of course!!)
     * 
     * @author sajayjk
     */
    protected class CallableRegionCompare implements Callable< Boolean >
    {
        private ImageCompareRegionInfo info;
        BufferedImage                  refImage;

        CallableRegionCompare( ImageCompareRegionInfo info )
        {
            this.info = info;
            refImage = imageLoader.loadImage( info.getFilepath() );
            if ( null == refImage )
            {
                throw new IllegalStateException( "Could not load image: " + imageLoader.getPath() );
            }
        }

        CallableRegionCompare( ImageCompareRegionInfo info, BufferedImage refImage )
        {
            if ( null == refImage )
            {
                throw new IllegalStateException( "No refImage Available" );
            }
            this.info = info;
            this.refImage = refImage;
        }

        /**
         * compare the region, or throws an exception if unable to do so.
         *
         * @return true if able to compare the region.
         * @throws Exception if unable to compare the region.
         */
        @Override
        public Boolean call() throws Exception
        {
            boolean retVal = false;
            BufferedImage currentScreen = null;
            while ( !Thread.currentThread().isInterrupted() )
            {
                currentScreen = getCurrentImage( refImage );
                logger.debug( "CallableMultipleRegionCompare.call() currentScreen " + currentScreen );
                if ( null != currentScreen && CoreRegionCompare.doRegionCompare( info, refImage, currentScreen ) )
                {
                    retVal = true;
                    break;
                }
                try
                {
                    Thread.sleep( 100 );
                }
                catch ( InterruptedException ie )
                {
                    logger.error( ie.getMessage() );
                    break;
                }
            }

            if ( !retVal && null != currentScreen )
            {
                saveImages( currentScreen, getClone( refImage ), null, info );
            }
            return retVal;
        }
    }

    /**
     * Checks if all the images regions are on the current screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * regionList.
     * 
     * @param regionList
     *            List of region metadata's
     * @param refImage
     *            The refImage to which the regions map to.
     * @return true if all the images regions are on screen. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean areAllRegionsOnScreenNow( List< ImageCompareRegionInfo > regionList, BufferedImage refImage )
            throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != regionList && null != refImage )
        {
            try
            {
                retVal = doRegionCompare( regionList, refImage );
                logger.info( "Result of comparision " + retVal );
            }
            catch ( IllegalStateException e )
            {
                logger.error( e.getMessage(), e );
                retVal = false;
            }
        }
        return retVal;
    }

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param icRegionInfo
     *            The region metadata.
     * @param refImage
     *            The refImage to which the regions map to.
     * @return true if the region is on screen. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean isRegionOnScreenNow( ImageCompareRegionInfo icRegionInfo, BufferedImage refImage )
            throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != icRegionInfo && null != refImage )
        {
            BufferedImage currentScreen = getCurrentImage( refImage );
            logger.debug( "Current Screen " + currentScreen );
            if ( null != currentScreen )
            {
                icRegionInfo = ( ImageCompareRegionInfo ) addDrift( icRegionInfo );
                try
                {
                    retVal = CoreRegionCompare.doRegionCompare( icRegionInfo, refImage, currentScreen );
                    logger.info( "Result of comparision " + retVal );
                }
                catch ( Exception e )
                {
                    logger.debug( e.getMessage() );
                    throw new ImageCompareException( e.getMessage() );
                }
            }
        }
        return retVal;
    }

    /**
     * Waits for the image to have all its regions on screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionList.
     * 
     * @param regionList
     *            List of region metadata's
     * @param timeout
     *            the timeout.
     * @param refImage as {@link BufferedImage BufferedImage}
     *            The refImage to which the regions map to.
     * @return true if all regions are on the screen within the timeout.False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean waitForAllRegions( List< ImageCompareRegionInfo > regionList, BufferedImage refImage, long timeout )
            throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != regionList )
        {
            CallableMultipleRegionCompare compare = null;
            try
            {
                compare = new CallableMultipleRegionCompare( regionList, refImage );
            }
            catch ( Exception e )
            {
                logger.error( e.getMessage(), e );
                return false;
            }
            retVal = runCallableTask( compare, timeout );
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the image to have the specified region on screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param regionInfo
     *            The region metadata.
     * @param timeout
     *            the timeout.
     * @param refImage
     *            The refImage to which the regions map to.
     * @return true if the region is on the screen within the timeout. False otherwise.
     * @throws ImageCompareException
     */
    @Override
    public boolean waitForRegion( ImageCompareRegionInfo regionInfo, BufferedImage refImage, long timeout )
            throws ImageCompareException
    {
        boolean retVal = false;
        if ( null != regionInfo )
        {
            regionInfo = ( ImageCompareRegionInfo ) addDrift( regionInfo );
            retVal = runCallableTask( new CallableRegionCompare( regionInfo, refImage ), timeout );
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Checks if the full image matches the current screen. This function uses
     * this class's match percent and RGB tolerances.
     * 
     * @param imgFile
     *            The image file path.
     * @return true if the image is on the current screen, else false.
     */
    @Override
    public boolean waitForFullImage( String imgFile )
    {
        boolean retVal = false;
        if ( null != imgFile && !imgFile.isEmpty() )
        {
            try
            {
                retVal = isImageOnScreenNow( imageLoader.loadImage( imgFile ) );
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the full image to match the current screen within the given
     * timeout. This function uses this class's match percent and RGB
     * tolerances.
     * 
     * @param imgFile
     *            The image file path.
     * @param timeout
     *            The timeout in milliseconds.
     * @return True if the image is on the screen within the timeout else false.
     */
    @Override
    public boolean waitForFullImage( String imgFile, long timeout )
    {
        boolean retVal = false;
        if ( null != imgFile && !imgFile.isEmpty() )
        {
            try
            {
                retVal = waitForImageOnScreen( imageLoader.loadImage( imgFile ), timeout );
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Checks if all the images regions are on the current screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file.
     * 
     * @param imgXMLPath
     *            The image xml file path.
     * @return True if all the images regions are on current screen, else false.
     */
    @Override
    public boolean waitForImageRegion( String imgXMLPath )
    {
        boolean retVal = false;
        List< RegionInfo > regionInfoList = null;
        if ( imgXMLPath != null && !imgXMLPath.isEmpty() )
        {
            try
            {
                regionInfoList = regionLocatorProvider.getRegionInfo( imgXMLPath );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
            }
            if ( null != regionInfoList )
            {
                List< ImageCompareRegionInfo > icRegionInfo = null;
                icRegionInfo = convertToImageCompareRegionInfo( regionInfoList );
                try
                {
                    retVal = areAllRegionsOnScreenNow( icRegionInfo );
                }
                catch ( ImageCompareException e )
                {
                    logger.debug( e.getMessage() );
                }
                logger.info( "Result of comparision " + retVal );
            }
        }
        return retVal;
    }

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file.
     * 
     * @param imgXMLPath
     *            The Image xml file path.
     * @param regionName
     *            The name of the region to be compared.
     * @return True if the region is on the current screen, else false.
     */
    @Override
    public boolean waitForImageRegion( String imgXMLPath, String regionName )
    {
        boolean retVal = false;
        RegionInfo regionInfo = null;
        if ( imgXMLPath != null && !imgXMLPath.isEmpty() && regionName != null && !regionName.isEmpty() )
        {
            try
            {
                regionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, regionName );
                logger.debug( "RegionInfo " + regionInfo );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
                return false;
            }
            if ( null != regionInfo )
            {
                ImageCompareRegionInfo icRegionInfo = convertToImageCompareRegionInfo( regionInfo );
                try
                {
                    retVal = isRegionOnScreenNow( icRegionInfo );
                }
                catch ( ImageCompareException e )
                {
                    logger.debug( e.getMessage() );
                }
                logger.info( "Result of comparision " + retVal );
            }
        }
        return retVal;
    }

    /**
     * Waits for all the regions to be on screen. This function uses the match
     * percent, RGB tolerances, and x & y tolerances from the imgXMLPath file.
     * 
     * @param imgXMLPath
     *            The image xml file path.
     * @param timeout
     *            The timeout in milliseconds.
     * @return True if all regions are on the screen within the timeout, else
     *         false.
     */
    @Override
    public boolean waitForImageRegion( String imgXMLPath, long timeout )
    {
        boolean retVal = false;
        List< RegionInfo > regionInfoList = null;
        if ( null != imgXMLPath && !imgXMLPath.isEmpty() )
        {
            try
            {
                regionInfoList = regionLocatorProvider.getRegionInfo( imgXMLPath );
                logger.debug( "RegionInfoList " + regionInfoList );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
                return false;
            }
            List< ImageCompareRegionInfo > icRegionInfo = null;
            if ( null != regionInfoList )
            {
                icRegionInfo = convertToImageCompareRegionInfo( regionInfoList );
            }
            try
            {
                retVal = waitForAllRegions( icRegionInfo, timeout );
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
            logger.info( "Result of comparision " + retVal );
        }
        return retVal;
    }

    /**
     * Waits for the specified region to be on screen for the specified timeout.
     * This function uses the match percent, RGB tolerances, and x & y
     * tolerances from the imgXMLPath file.
     * 
     * @param imgXMLPath
     *            The Image xml file path.
     * @param regionName
     *            The name of the region to be compared.
     * @param timeout
     *            The timeout in milliseconds.
     * @return True if the region is on the screen within the timeout, else
     *         false.
     */
    @Override
    public boolean waitForImageRegion( String imgXMLPath, String regionName, long timeout )
    {
        boolean retVal = false;
        if ( null != imgXMLPath && !imgXMLPath.isEmpty() && null != regionName && !regionName.isEmpty() )
        {
            RegionInfo regionInfo = null;
            try
            {
                regionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, regionName );
                logger.debug( "RegionInfo " + regionInfo );
                if ( null != regionInfo )
                {
                    ImageCompareRegionInfo icRegionInfo = convertToImageCompareRegionInfo( regionInfo );
                    retVal = waitForRegion( icRegionInfo, timeout );
                    logger.info( "Result of comparision " + retVal );
                }
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
                retVal = false;
            }
            catch ( ImageCompareException e )
            {
                logger.debug( e.getMessage() );
            }
        }
        return retVal;
    }

    /**
     * to get current image
     * 
     * @param referenceImage {@linkplain BufferedImage}
     * @return BufferedImage
     */
    protected BufferedImage getCurrentImage( BufferedImage referenceImage )
    {
        return videoProvider.getVideoImage( new Dimension( referenceImage.getWidth(), referenceImage.getHeight() ) );
    }

    @Override
    public String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y,
            int xtolerance, int yTolerance, int redTolerance, int greenTolerance, int blueTolerance,
            float matchPercentage ) throws IOException, JAXBException
    {
        if ( null == imageFilePath || imageFilePath.isEmpty() || null == regionName || regionName.isEmpty() )
        {
            throw new IllegalArgumentException( "ImageFilePath and ReginName cannot be null or empty" );
        }

        String imgFilePath = validateImageFile( imageFilePath );
        
        ImageCompareRegionInfo icRegionInfo = new ImageCompareRegionInfo();
        icRegionInfo.setFilepath( new File( imgFilePath ).getName());
        icRegionInfo.setBlueTolerance( blueTolerance );
        icRegionInfo.setGreenTolerance( greenTolerance );
        icRegionInfo.setHeight( height );
        icRegionInfo.setMatchPct( matchPercentage );
        icRegionInfo.setName( regionName );
        icRegionInfo.setRedTolerance( redTolerance );
        icRegionInfo.setWidth( width );
        icRegionInfo.setX( x );
        icRegionInfo.setXTolerance( xtolerance );
        icRegionInfo.setY( y );
        icRegionInfo.setYTolerance( yTolerance );

        return saveImageRegion( icRegionInfo, imgFilePath );
    }

    /**
     * Saves the specified region to the corresponding xml file of the image.
     * Xml file will be created if it does not exist.
     * 
     * @param imageFilePath
     *            Image file path
     * @param regionName
     *            Region name
     * @param width
     *            Width of the region
     * @param height
     *            Height of the region
     * @param x
     *            x position
     * @param y
     *            y position
     * @param matchPercentage
     *            match percentage
     * @return xml file path
     * @throws IOException
     * @throws JAXBException
     */
    @Override
    public String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y,
            float matchPercentage ) throws IOException, JAXBException
    {
        return saveImageRegion( imageFilePath, regionName, width, height, x, y, xTolerance, yTolerance, redTolerance,
                greenTolerance, blueTolerance, matchPercentage );
    }

    /**
     * Saves the specified region to the corresponding xml file of the image.
     * Xml file will be created if it does not exist.
     * 
     * @param imageFilePath
     *            Image file path
     * @param regionName
     *            Region name
     * @param width
     *            Width of the region
     * @param height
     *            Height of the region
     * @param x
     *            x position
     * @param y
     *            y position
     * @return xml file path
     * @throws IOException
     * @throws JAXBException
     */
    @Override
    public String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
            throws IOException, JAXBException
    {
        return saveImageRegion( imageFilePath, regionName, width, height, x, y, xTolerance, yTolerance, redTolerance,
                greenTolerance, blueTolerance, matchPercent );
    }

    private String getCleanMac()
    {
        String cleanMac = "";
        if ( null != videoProvider.getParent() )
        {
            try
            {
                Settop settop = ( Settop ) videoProvider.getParent();
                String hostMacAddress = settop.getHostMacAddress();
                cleanMac = hostMacAddress.trim().replace( ":", "" ).toUpperCase();
            }
            catch ( ClassCastException e )
            {
                logger.error( "VideoProvider doesn't have a parent settop." );
            }
        }
        return cleanMac;
    }

    private boolean isValidImageFile( File file )
    {
        boolean result = false;

        if ( file.exists() && file.isFile() )
        {
            try
            {
                BufferedImage img = ImageIO.read( file );
                if ( img != null )
                {
                    result = true;
                }
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage() );
            }
        }
        return result;
    }

    /**
     * Finds out whether the reference region is anywhere in the target region
     * of current screen.
     * 
     * @param refRegionInfo
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegionInfo
     *            The search area {@link RegionInfo}
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    @Override
    public ImageCompareResult isRegionOnTargetRegion( ImageCompareRegionInfo refRegionInfo, RegionInfo targetRegionInfo )
            throws ImageCompareException
    {
        ImageCompareResult icResult = null;
        if ( null != refRegionInfo && null != targetRegionInfo )
        {
            logger.debug( "Image Filepath " + refRegionInfo.getFilepath() );
            BufferedImage refImage = imageLoader.loadImage( refRegionInfo.getFilepath() );
            if ( null == refImage )
            {
                 throw new IllegalStateException( "Could not load image: " + imageLoader.getPath() );
            }

            icResult = isRegionOnTargetRegion( refRegionInfo, targetRegionInfo, refImage );
            logger.info( "Result of comparision " + icResult.getResult() );
        }
        return icResult;
    }

    /**
     * Finds out whether the reference region is anywhere in the target region
     * of current screen.
     * 
     * @param refRegionInfo
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegionInfo
     *            The search area {@link RegionInfo}
     * @param refImage
     *            The reference image {@link BufferedImage}
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    @Override
    public ImageCompareResult isRegionOnTargetRegion( ImageCompareRegionInfo refRegionInfo,
            RegionInfo targetRegionInfo, BufferedImage refImage ) throws ImageCompareException
    {
        ImageCompareResult icResult = null;

        if ( null != refRegionInfo && null != targetRegionInfo && null != refImage )
        {
            BufferedImage currentScreen = getCurrentImage( refImage );

            if ( null != currentScreen )
            {                
                try
                {
                    icResult = CoreRegionCompare.findRegionOnTargetRegion( refRegionInfo, targetRegionInfo, refImage,
                            currentScreen );
                    logger.info( "Result of comparision " + icResult.getResult() );
                }
                catch ( Exception e )
                {
                    logger.error( "Error while finding out region: " + refRegionInfo.getName() + 
                            " on search region: " + targetRegionInfo.getName(), e );
                    throw new ImageCompareException( e.getMessage() );
                }
            }
        }
        return icResult;
    }

    /**
     * Waits for the reference region to appear anywhere in the target region
     * until the time out happens.
     * 
     * @param refRegionInfo
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegionInfo
     *            The search area {@link RegionInfo}
     * @param timeout
     *            The timeout in milliseconds.
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    @Override
    public ImageCompareResult waitForRegionOnTargetRegion( ImageCompareRegionInfo refRegionInfo,
            RegionInfo targetRegionInfo, long timeout ) throws ImageCompareException
    {
        ImageCompareResult icResult = null;

        if ( null != refRegionInfo && null != targetRegionInfo )
        {
            icResult = runCallableSearchRegionTask( new CallableSearchForRegionOnTargetRegion( refRegionInfo,
                    targetRegionInfo ), timeout );
            logger.info( "Result of comparision " + icResult.getResult() );
        }
        return icResult;
    }

    /**
     * Waits for the reference region to appear anywhere in the target region
     * until the time out happens.
     * 
     * @param refRegionInfo
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegionInfo
     *            The search area {@link RegionInfo}
     * @param refImage
     *            The reference image {@link BufferedImage}
     * @param timeout
     *            The timeout in milliseconds.
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    @Override
    public ImageCompareResult waitForRegionOnTargetRegion( ImageCompareRegionInfo refRegionInfo,
            RegionInfo targetRegionInfo, BufferedImage refImage, long timeout ) throws ImageCompareException
    {
        ImageCompareResult icResult = null;
        
        if ( null != refRegionInfo && null != targetRegionInfo && null != refImage )
        {
            icResult = runCallableSearchRegionTask( new CallableSearchForRegionOnTargetRegion( refRegionInfo,
                    targetRegionInfo, refImage ), timeout );
            logger.info( "Result of comparision " + icResult );
        }
        return icResult;
    }

    @Override
    public ImageCompareResult waitForRegionOnTargetRegion( String imgXMLPath, String refRegionName,
            String targetRegionName )
    {        
        RegionInfo refRegionInfo = null;
        RegionInfo targetRegionInfo = null;
        
        ImageCompareResult icResult = null;
        
        if ( imgXMLPath != null && !imgXMLPath.isEmpty() && refRegionName != null && !refRegionName.isEmpty() 
                && targetRegionName != null && !targetRegionName.isEmpty() )
        {
            try
            {
                refRegionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, refRegionName );
                logger.debug( "Reference RegionInfo " + refRegionInfo );
                
                targetRegionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, targetRegionName );
                logger.debug( "Target RegionInfo " + targetRegionInfo );
                
                if ( null != refRegionInfo && null != targetRegionInfo )
                {
                    ImageCompareRegionInfo icRegionInfo = convertToImageCompareRegionInfo( refRegionInfo );
                    icResult = isRegionOnTargetRegion( icRegionInfo, targetRegionInfo );
                    logger.info( "Result of comparision " + icResult.getResult() );
                }
            }
            catch ( IOException e )
            {
                logger.error( "IOException while finding out region: " + refRegionName + 
                        " on search region: " + targetRegionName, e );
            }
            catch ( ImageCompareException e )
            {
                logger.error( "ImageCompareException while finding out region: " + refRegionName + 
                        " on search region: " + targetRegionName, e );
            }
        }
        return icResult;
    }

    @Override
    public ImageCompareResult waitForRegionOnTargetRegion( String imgXMLPath, String refRegionName,
            String targetRegionName, long timeout )
    {        
        RegionInfo refRegionInfo = null;
        RegionInfo targetRegionInfo = null;
        
        ImageCompareResult icResult = null;
        
        if ( imgXMLPath != null && !imgXMLPath.isEmpty() && refRegionName != null && !refRegionName.isEmpty() 
                && targetRegionName != null && !targetRegionName.isEmpty() )
        {
            try
            {
                refRegionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, refRegionName );
                logger.debug( "Reference RegionInfo " + refRegionInfo );
                
                targetRegionInfo = regionLocatorProvider.getRegionInfo( imgXMLPath, targetRegionName );
                logger.debug( "Target RegionInfo " + targetRegionInfo );
                
                if ( null != refRegionInfo && null != targetRegionInfo )
                {
                    ImageCompareRegionInfo icRegionInfo = convertToImageCompareRegionInfo( refRegionInfo );
                    icResult = waitForRegionOnTargetRegion( icRegionInfo, targetRegionInfo, timeout );
                    logger.info( "Result of comparision " + icResult.getResult() );
                }
            }
            catch ( IOException e )
            {
                logger.error( "IOException while finding out region: " + refRegionName + 
                        " on search region: " + targetRegionName + " with timeout: " + timeout, e );
            }
            catch ( ImageCompareException e )
            {
                logger.error( "ImageCompareException while finding out region: " + refRegionName + 
                        " on search region: " + targetRegionName + " with timeout: " + timeout, e );
            }
        }
        return icResult;
    }

    @Override
    public String saveSearchRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
            throws IOException, JAXBException
    {
        if ( null == imageFilePath || imageFilePath.isEmpty() || null == regionName || regionName.isEmpty() )
        {
            throw new IllegalArgumentException( "ImageFilePath and ReginName cannot be null or empty" );
        }

        String imgPath = validateImageFile( imageFilePath );
       
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.setName( regionName );
        regionInfo.setX( x );
        regionInfo.setY( y );
        regionInfo.setWidth( width );
        regionInfo.setHeight( height );
        
        return saveImageRegion( regionInfo, imgPath );
    }
    
    private String validateImageFile(String imageFilePath)
    {
        String imgPath = imageFilePath;
        File imgFile = new File( imgPath );
        if ( !imgFile.isAbsolute() )
        {
            String catsHome = System.getProperty( "cats.home" );
            if ( null != catsHome )
            {
                imgPath = catsHome + System.getProperty( "file.separator" ) + getCleanMac()
                        + System.getProperty( "file.separator" ) + imageFilePath;
            }
            imgFile = new File( imgPath );
        }

        if ( !isValidImageFile( imgFile ) )
        {
            throw new IllegalArgumentException( "Invalid image file! " + imageFilePath );
        }
        
        return imgPath;
    }
}