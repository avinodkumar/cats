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
 * Image Compare Provider v0.2
 */
package com.comcast.cats.provider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.ImageCompareResult;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.provider.exceptions.ImageCompareException;

/**
 * Interface that defines rules for providers who wish to provide for
 * ImageCompare services.
 * 
 * @author sajayjk
 */
public interface ImageCompareProvider extends BaseProvider
{
    /**
     * Checks if the full image matches the current screen. This function uses
     * this class's match percent and RGB tolerances.
     * 
     * @param refImage
     *            The image as BufferedImage.
     * @return true if the image is on the current screen. False otherwise.
     * @throws ImageCompareException
     */
    boolean isImageOnScreenNow( BufferedImage refImage ) throws ImageCompareException;

    /**
     * Waits for the full image to match the current screen within the given
     * timeout. This function uses this class's match percent and RGB
     * tolerances.
     * 
     * @param refImage
     *            The image as BufferedImage.
     * @param timeout
     *            The timeout in milliseconds.
     * @return true if the image is on the screen within the timeout. False
     *         otherwise.
     * @throws ImageCompareException
     */
    boolean waitForImageOnScreen( BufferedImage refImage, long timeout ) throws ImageCompareException;

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param regionInfo
     *            The region metadata as ImageCompareRegionInfo.
     * 
     * @return true if the region is on screen. False otherwise.
     * @throws ImageCompareException
     */

    boolean isRegionOnScreenNow( ImageCompareRegionInfo regionInfo ) throws ImageCompareException;

    /**
     * Waits for the image to have the specified region on screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param regionInfo
     *            The region metadata as ImageCompareRegionInfo
     * @param timeout
     *            the timeout.
     * @return true if the region is on the screen within the timeout. False
     *         otherwise.
     * @throws ImageCompareException
     */
    boolean waitForRegion( ImageCompareRegionInfo regionInfo, long timeout ) throws ImageCompareException;

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

    boolean areAllRegionsOnScreenNow( List< ImageCompareRegionInfo > regionList ) throws ImageCompareException;

    /**
     * Waits for the image to have all its regions on screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionList.
     * 
     * @param regionList
     *            List of region metadata's
     * @param timeout
     *            the timeout.
     * @return true if all regions are on the screen within the timeout.False
     *         otherwise.
     * @throws ImageCompareException
     */
    boolean waitForAllRegions( List< ImageCompareRegionInfo > regionList, long timeout ) throws ImageCompareException;

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionInfo.
     * 
     * @param regionInfo
     *            The region metadata.
     * @param refImage
     *            The refImage to which the regions map to.
     * @return true if the region is on screen. False otherwise.
     * @throws ImageCompareException
     */
    boolean isRegionOnScreenNow( ImageCompareRegionInfo regionInfo, BufferedImage refImage )
            throws ImageCompareException;

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

    boolean areAllRegionsOnScreenNow( List< ImageCompareRegionInfo > regionList, BufferedImage refImage )
            throws ImageCompareException;

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
     * @return true if the region is on the screen within the timeout. False
     *         otherwise.
     * @throws ImageCompareException
     */
    boolean waitForRegion( ImageCompareRegionInfo regionInfo, BufferedImage refImage, long timeout )
            throws ImageCompareException;

    /**
     * Waits for the image to have all its regions on screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * regionList.
     * 
     * @param regionList
     *            List of region metadata's
     * @param timeout
     *            the timeout.
     * @param refImage
     *            as {@link BufferedImage BufferedImage} The refImage to which
     *            the regions map to.
     * @return true if all regions are on the screen within the timeout.False
     *         otherwise.
     * @throws ImageCompareException
     */
    boolean waitForAllRegions( List< ImageCompareRegionInfo > regionList, BufferedImage refImage, long timeout )
            throws ImageCompareException;

    /**
     * Checks if the full image matches the current screen. This function uses
     * this class's match percent and RGB tolerances. Maintained for Backward
     * Compatibility
     * 
     * @param imgFile
     *            The image file.
     * @return true if the image is on the current screen. False otherwise.
     */
    @Deprecated
    boolean isSameImageOnScreenNow( String imgFile );

    /**
     * Waits for the full image to match the current screen within the given
     * timeout. This function uses this class's match percent and RGB
     * tolerances. Maintained for Backward Compatibility
     * 
     * @param imgFile
     *            The image file.
     * @param timeout
     *            The timeout in milliseconds.
     * @return true if the image is on the screen within the timeout. False
     *         otherwise.
     */
    @Deprecated
    boolean waitForSameImage( String imgFile, long timeout );

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
    boolean isRegionOnScreenNow( String imgXMLPath, String regionName );

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
     * @return true if the region is on the screen within the timeout. False
     *         otherwise.
     */
    @Deprecated
    boolean waitForRegion( String imgXMLPath, String regionName, long timeout );

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
    boolean areAllRegionsOnScreenNow( String imgXMLPath );

    /**
     * Waits for the image to have all its regions on screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file. Maintained for Backward Compatibility
     * 
     * @param imgXMLPath
     *            The image xml file.
     * @param timeout
     *            the timeout.
     * @return true if all regions are on the screen within the timeout. False
     *         otherwise.
     */
    @Deprecated
    boolean waitForAllRegions( String imgXMLPath, long timeout );

    /**
     * Checks if the image region is on the current screen. This function uses
     * the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file.
     * 
     * @param xmlFilePath
     *            The Image xml file path.
     * @param regionName
     *            The name of the region to be compared.
     * @return True if the region is on the current screen, else false.
     */
    boolean waitForImageRegion( String xmlFilePath, String regionName );

    /**
     * Waits for the specified region to be on screen for the specified timeout.
     * This function uses the match percent, RGB tolerances, and x & y
     * tolerances from the imgXMLPath file.
     * 
     * @param xmlFilePath
     *            The Image xml file path.
     * @param regionName
     *            The name of the region to be compared.
     * @param timeOut
     *            The timeout in milliseconds.
     * @return True if the region is on the screen within the timeout, else
     *         false.
     */
    boolean waitForImageRegion( String xmlFilePath, String regionName, long timeOut );

    /**
     * Checks if all the images regions are on the current screen. This function
     * uses the match percent, RGB tolerances, and x & y tolerances from the
     * imgXMLPath file.
     * 
     * @param xmlFilePath
     *            The image xml file path.
     * @return True if all the images regions are on current screen, else false.
     */
    boolean waitForImageRegion( String xmlFilePath );

    /**
     * Waits for all the regions to be on screen. This function uses the match
     * percent, RGB tolerances, and x & y tolerances from the imgXMLPath file.
     * 
     * @param xmlFilePath
     *            The image xml file path.
     * @param timeOut
     *            The timeout in milliseconds.
     * @return True if all regions are on the screen within the timeout, else
     *         false.
     */
    boolean waitForImageRegion( String xmlFilePath, long timeOut );

    /**
     * Checks if the full image matches the current screen. This function uses
     * this class's match percent and RGB tolerances.
     * 
     * @param imgFilePath
     *            The image file path.
     * @return true if the image is on the current screen, else false.
     */
    boolean waitForFullImage( String imgFilePath );

    /**
     * Waits for the full image to match the current screen within the given
     * timeout. This function uses this class's match percent and RGB
     * tolerances.
     * 
     * @param imgFilePath
     *            The image file path.
     * @param timeOut
     *            The timeout in milliseconds.
     * @return True if the image is on the screen within the timeout else false.
     */
    boolean waitForFullImage( String imgFilePath, long timeOut );

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
     * @param xtolerance
     *            x tolerance
     * @param yTolerance
     *            y tolerance
     * @param redTolerance
     *            red tolerance
     * @param greenTolerance
     *            green tolerance
     * @param blueTolerance
     *            blue tolerance
     * @param matchPercentage
     *            match percentage
     * @return xml file path
     * @throws IOException
     * @throws JAXBException
     */
    String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y,
            int xtolerance, int yTolerance, int redTolerance, int greenTolerance, int blueTolerance,
            float matchPercentage ) throws IOException, JAXBException;

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
    String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y,
            float matchPercentage ) throws IOException, JAXBException;

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
    String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
            throws IOException, JAXBException;

    /**
     * Finds out whether the reference region is anywhere in the target region
     * of current screen.
     * 
     * @param refRegion
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegion
     *            The search area {@link RegionInfo}
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    ImageCompareResult isRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion )
            throws ImageCompareException;

    /**
     * Finds out whether the reference region is anywhere in the target region
     * of current screen.
     * 
     * @param refRegion
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegion
     *            The search area {@link RegionInfo}
     * @param refImage
     *            The reference image {@link BufferedImage}
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    ImageCompareResult isRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion,
            BufferedImage refImage ) throws ImageCompareException;

    /**
     * Waits for the reference region to appear anywhere in the target region
     * until the time out happens.
     * 
     * @param refRegion
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegion
     *            The search area {@link RegionInfo}
     * @param timeout
     *            The timeout in milliseconds.
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    ImageCompareResult waitForRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion,
            long timeout ) throws ImageCompareException;

    /**
     * Waits for the reference region to appear anywhere in the target region
     * until the time out happens.
     * 
     * @param refRegion
     *            The region to be searched {@link ImageCompareRegionInfo}
     * @param targetRegion
     *            The search area {@link RegionInfo}
     * @param refImage
     *            The reference image {@link BufferedImage}
     * @param timeout
     *            The timeout in milliseconds.
     * @return {@link ImageCompareResult} The comparison result
     * @throws ImageCompareException
     */
    ImageCompareResult waitForRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion,
            BufferedImage refImage, long timeout ) throws ImageCompareException;

    /**
     * Finds out whether the reference region is anywhere in the target region
     * of current screen.
     * 
     * @param xmlFilePath
     *            The image xml file path.
     * @param refRegionName
     *            The name of Reference region
     * @param targetRegionName
     *            The name of Target region
     * @return {@link ImageCompareResult}
     */
    ImageCompareResult waitForRegionOnTargetRegion( String xmlFilePath, String refRegionName, String targetRegionName );

    /**
     * Waits for the reference region to appear anywhere in the target region
     * until the time out happens.
     * 
     * @param xmlFilePath
     *            The image xml file path.
     * @param refRegionName
     *            The name of Reference region
     * @param targetRegionName
     *            The name of Target region
     * @param timeout
     *            The timeout in milliseconds.
     * @return {@link ImageCompareResult}
     */
    ImageCompareResult waitForRegionOnTargetRegion( String xmlFilePath, String refRegionName, String targetRegionName,
            long timeout );

    /**
     * Saves the specified search region to the corresponding xml file of the
     * image. Xml file will be created if it does not exist.
     * 
     * @param imageFilePath
     *            Image file path.
     * @param regionName
     *            Neme of the region
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
    String saveSearchRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
            throws IOException, JAXBException;
}
