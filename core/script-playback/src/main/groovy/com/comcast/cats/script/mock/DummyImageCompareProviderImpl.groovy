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
package com.comcast.cats.script.mock

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.ImageCompareResult;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.exceptions.ImageCompareException;

class DummyImageCompareProviderImpl implements ImageCompareProvider
{
    public Object getParent(){
        return null;
    }
    /**
    * Checks if the full image matches the current screen. This function uses
    * this class's match percent and RGB tolerances.
    *
    * @param refImage
    *            The image.
    * @return true if the image is on the current screen. Otherwise false is
    *         returned.
    */
   boolean isImageOnScreenNow( BufferedImage refImage ) throws ImageCompareException{
       return true;
   }

   /**
    * Waits for the full image to match the current screen within the given
    * timeout. This function uses this class's match percent and RGB
    * tolerances.
    *
    * @param refImage
    *            The image.
    * @param timeout
    *            The timeout in milliseconds.
    * @return true if the image is on the screen within the timeout. Otherwise
    *         false is returned.
    */
   boolean waitForImageOnScreen( BufferedImage refImage, long timeout ) throws ImageCompareException{
       return true;
   }

   /**
    * Checks if the image region is on the current screen. This function uses
    * the match percent, RGB tolerances, and x & y tolerances from the
    * regionInfo.
    *
    * @param regionInfo
    *            The region metadata.
    * @param regionName
    *            The region to wait for.
    * @return true if the region is on screen. Otherwise false is returned.
    */

   boolean isRegionOnScreenNow( ImageCompareRegionInfo regionInfo ) throws ImageCompareException{
       return true;
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
    * @return true if the region is on the screen within the timeout. Otherwise
    *         false is returned.
    */
   boolean waitForRegion( ImageCompareRegionInfo regionInfo, long timeout ) throws ImageCompareException{
       return true;
   }

   /**
    * Checks if all the images regions are on the current screen. This function
    * uses the match percent, RGB tolerances, and x & y tolerances from the
    * regionList.
    *
    * @param regionList
    *            List of region metadata's
    * @return true if all the images regions are on screen. Otherwise false is
    *         returned.
    */

   boolean areAllRegionsOnScreenNow( List< ImageCompareRegionInfo > regionList ) throws ImageCompareException{
       return true;
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
    * @return true if all regions are on the screen within the timeout.
    *         Otherwise false is returned.
    */
   boolean waitForAllRegions( List< ImageCompareRegionInfo > regionList, long timeout ) throws ImageCompareException{
       return true;
   }

   /**
    * Checks if the image region is on the current screen. This function uses
    * the match percent, RGB tolerances, and x & y tolerances from the
    * regionInfo.
    *
    * @param regionInfo
    *            The region metadata.
    * @param refImage
    *            The refImage to which the regions map to.
    * @return true if the region is on screen. Otherwise false is returned.
    */
   boolean isRegionOnScreenNow( ImageCompareRegionInfo regionInfo, BufferedImage refImage )
           throws ImageCompareException{
               return true;
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
    * @return true if all the images regions are on screen. Otherwise false is
    *         returned.
    */

   boolean areAllRegionsOnScreenNow( List< ImageCompareRegionInfo > regionList, BufferedImage refImage )
           throws ImageCompareException{
               return true;
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
    * @return true if the region is on the screen within the timeout. Otherwise
    *         false is returned.
    */
   boolean waitForRegion( ImageCompareRegionInfo regionInfo, BufferedImage refImage, long timeout )
           throws ImageCompareException{
               return true;
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
    * @param refImage
    *            The refImage to which the regions map to.
    * @return true if all regions are on the screen within the timeout.
    *         Otherwise false is returned.
    */
   boolean waitForAllRegions( List< ImageCompareRegionInfo > regionList, BufferedImage refImage, long timeout )
           throws ImageCompareException{
               return true;
           }

   /**
    * Checks if the full image matches the current screen. This function uses
    * this class's match percent and RGB tolerances. Maintained for Backward
    * Compatibility
    *
    * @param imgFile
    *            The image file.
    * @return true if the image is on the current screen. Otherwise false is
    *         returned.
    */
   @Deprecated
   boolean isSameImageOnScreenNow( String imgFile ){
       return true;
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
    * @return true if the image is on the screen within the timeout. Otherwise
    *         false is returned.
    */
   @Deprecated
   boolean waitForSameImage( String imgFile, long timeout ){
       return true;
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
    * @return true if the region is on screen. Otherwise false is returned.
    */
   @Deprecated
   boolean isRegionOnScreenNow( String imgXMLPath, String regionName ){
       return true;
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
    * @return true if the region is on the screen within the timeout. Otherwise
    *         false is returned.
    */
   @Deprecated
   boolean waitForRegion( String imgXMLPath, String regionName, long timeout ){
       return true;
   }

   /**
    * Checks if all the images regions are on the current screen. This function
    * uses the match percent, RGB tolerances, and x & y tolerances from the
    * imgXMLPath file. Maintained for Backward Compatibility
    *
    * @param imgXMLPath
    *            The image xml file.
    * @return true if all the images regions are on screen. Otherwise false is
    *         returned.
    */
   @Deprecated
   boolean areAllRegionsOnScreenNow( String imgXMLPath ){
       return true;
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
    *         Otherwise false is returned.
    */
   @Deprecated
   boolean waitForAllRegions( String imgXMLPath, long timeout ){
       return true;
   }

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
   boolean waitForImageRegion( String xmlFilePath, String regionName ){
       return true;
   }

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
   boolean waitForImageRegion( String xmlFilePath, String regionName, long timeOut ){
       return true;
   }

   /**
    * Checks if all the images regions are on the current screen. This function
    * uses the match percent, RGB tolerances, and x & y tolerances from the
    * imgXMLPath file.
    *
    * @param xmlFilePath
    *            The image xml file path.
    * @return True if all the images regions are on current screen, else false.
    */
   boolean waitForImageRegion( String xmlFilePath ){
       return true;
   }

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
   boolean waitForImageRegion( String xmlFilePath, long timeOut ){
       return true;
   }

   /**
    * Checks if the full image matches the current screen. This function uses
    * this class's match percent and RGB tolerances.
    *
    * @param imgFile
    *            The image file path.
    * @return true if the image is on the current screen, else false.
    */
   boolean waitForFullImage( String imgFilePath ){
       return true;
   }

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
   boolean waitForFullImage( String imgFilePath, long timeOut ){
       return true;
   }
   
   String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y, int xtolerance,
       int yTolerance, int redTolerance, int greenTolerance, int blueTolerance, float matchPercentage ) 
       throws IOException, JAXBException
   {
       return ""; 
   }

   String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y, 
       float matchPercentage ) throws IOException, JAXBException
   {
       return "";
   }

   String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y ) 
       throws IOException, JAXBException
   {
       return "";
   }
   
    ImageCompareResult isRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion )
            throws ImageCompareException
    {
        return null;
    }
    
    ImageCompareResult isRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion,
        BufferedImage refImage ) throws ImageCompareException
    {
        return null;
    }
    
    ImageCompareResult waitForRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion,
        long timeout ) throws ImageCompareException
    {
        return null;
    }
    
    ImageCompareResult waitForRegionOnTargetRegion( ImageCompareRegionInfo refRegion, RegionInfo targetRegion,
        BufferedImage refImage, long timeout ) throws ImageCompareException
    {
        return null;
    }    
    
    ImageCompareResult waitForRegionOnTargetRegion( String xmlFilePath, String refRegionName, String targetRegionName )
    {
        return null;
    }
    
    ImageCompareResult waitForRegionOnTargetRegion( String xmlFilePath, String refRegionName, String targetRegionName, long timeout)
    {
        return null;
    }
    
    String saveSearchRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
        throws IOException, JAXBException
    {
        return null;
    }
}
