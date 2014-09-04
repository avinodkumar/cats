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
package com.comcast.cats.provider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.comcast.cats.image.RegionInfo;

/**
 * Interface that allows users to define/store and locate regions for
 * ImageCompare, OCR or any other purposes.
 * 
 * @author sajayjk
 */
public interface RegionLocatorProvider
{

    /**
     * Saves the image, the region metadata to the filepath location mentioned.
     * 
     * @param regionInfoList
     *            - The list of regions.
     * @param refImage
     *            - the image to be saved.
     * @param filePath
     *            - the file location to save the data.
     * @throws IOException.
     * @throws JAXBException
     * 
     */
    void saveImageAndRegion( List< RegionInfo > regionInfoList, BufferedImage refImage, String filePath )
            throws IOException, JAXBException;

    /**
     * Gets the RegionInfo corresponding to the filepath and regionName
     * provided.
     * 
     * @param filepath
     *            - the filepath to the location of the region metadata.
     * @param regionName
     *            - the name of the particular region
     * @return - RegionInfo for the particular region.
     * @throws IOException
     */
    RegionInfo getRegionInfo( String filepath, String regionName ) throws IOException;

    /**
     * Gets the all the RegionInfo corresponding to the filepath provided.
     * 
     * @param filepath
     *            - to the region metadata
     * @return List of RegionInfo.
     * @throws IOException
     */
    List< RegionInfo > getRegionInfo( String filepath ) throws IOException;

    /**
     * Saves the RegionInfo to the xml file corresponding to the image file
     * path. Xml is created it it doesn't exist.
     * 
     * @param regionInfo
     *            - RegionInfo instance.
     * @param imagefilePath
     *            - The image file path
     * @return xml file path corresponding to the image path.
     * @throws IOException
     * @throws JAXBException
     */
    String saveImageRegion( RegionInfo regionInfo, String imagefilePath ) throws IOException, JAXBException;
}
