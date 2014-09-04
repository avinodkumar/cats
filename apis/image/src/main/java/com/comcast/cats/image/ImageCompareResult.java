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

import java.awt.image.BufferedImage;

/**
 * Result of image comparison operation to find out a particular image on a
 * search area.
 * 
 * @author minu
 * 
 */
public class ImageCompareResult
{
    private boolean       result;
    private RegionInfo    resultRegionInfo;
    private BufferedImage successImage;

    /**
     * Creates instance of {@link ImageCompareResult} with default values.
     */
    public ImageCompareResult()
    {
    }

    /**
     * Sets all the given parameter values.
     * 
     * @param result
     *            {@link Boolean} comparison result
     * @param successRegionInfo
     *            {@link RegionInfo} matching region info.
     * @param successImage
     *            {@link BufferedImage} result image marked with the search area
     *            and matching region
     */
    public ImageCompareResult( boolean result, RegionInfo successRegionInfo, BufferedImage successImage )
    {
        this.result = result;
        this.resultRegionInfo = successRegionInfo;
        this.successImage = successImage;
    }

    /**
     * To get the result image of comparison. The reference and search areas
     * will be marked in this image.
     * 
     * @return BufferedImage
     */
    public BufferedImage getSuccessImage()
    {
        return successImage;
    }

    /**
     * To set the result image of comparison.
     * 
     * @param successImage
     *            BufferedImage
     */
    public void setSuccessImage( BufferedImage successImage )
    {
        this.successImage = successImage;
    }

    /**
     * To get the result of comparison.
     * 
     * @return boolean result
     */
    public boolean getResult()
    {
        return result;
    }

    /**
     * To set the result of comparison.
     * 
     * @param result
     *            {@link Boolean}
     */
    public void setResult( boolean result )
    {
        this.result = result;
    }

    /**
     * To get the region info (x, y, width and height), where we get the match
     * in search area.
     * 
     * @return
     */
    public RegionInfo getSuccessRegion()
    {
        return resultRegionInfo;
    }

    /**
     * To set the success region info
     * 
     * @param regionInfo
     *            RegionInfo
     */
    public void setgetSuccessRegion( RegionInfo regionInfo )
    {
        this.resultRegionInfo = regionInfo;
    }
}
