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
package com.comcast.cats.provider.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.image.ImageRegionInfo;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.image.RegionInfoXmlUtil;
import com.comcast.cats.provider.RegionLocatorProvider;

/**
 * Interface that allows users to define/store and locate regions for
 * ImageCompare, OCR or any other purposes.
 * 
 * @author sajayjk
 */
public class RegionLocatorProviderImpl implements RegionLocatorProvider {
  
    /**
     * ******************************************************** End of legacy.
     * ********************************************************
     */
    private static final String IMAGE_FILE_FORMAT = "jpg";

    private static final Logger logger = LoggerFactory
            .getLogger(RegionLocatorProviderImpl.class);
    Class<?> resourceClass = null;

    public RegionLocatorProviderImpl() {
        resourceClass = getClass();
    }

    public RegionLocatorProviderImpl(Class<?> resourceClass) {
        this.resourceClass = resourceClass;
    }
    
    /**
	 * Saves the RegionInfo to the xml file corresponding to the image file path. Xml is created it it doesn't exist.
	 * 
	 * @param regionInfo - RegionInfo instance.
	 * @param imageFilePath - The image file path
	 * @return xml file path corresponding to the image path.
	 * @throws IOException
	 * @throws JAXBException
	 */
    @Override
    public String saveImageRegion( RegionInfo regionInfo, String imageFilePath ) throws IOException, JAXBException
    {
        if ( null == regionInfo || null == imageFilePath || imageFilePath.isEmpty() )
        {
            throw new IllegalArgumentException( "regionInfo and imageFilePath can't be null or empty" );
        }

        ImageRegionInfo imageRegionInfo = null;
        String xmlFilePath = changeExtensionToXML( imageFilePath );
        File xmlFile = new File( xmlFilePath );
        if ( xmlFile.exists() && xmlFile.isFile() )
        {
            logger.debug( "xmlfile exists: " + xmlFile.getAbsolutePath() );            
            imageRegionInfo = loadFromXML( getClass(), xmlFilePath );
            if ( null != imageRegionInfo )
            {
                if ( null != imageRegionInfo.getRegion( regionInfo.getName() ) )
                {
                    logger.debug( "Removing already existing region: " + regionInfo.getName() );                    
                    imageRegionInfo.deleteRegion( regionInfo.getName() );
                }
            }
            else
            {
                imageRegionInfo = new ImageRegionInfo();
            }
        }
        else
        {
            logger.debug( "xmlfile does not exist." + xmlFilePath );
            imageRegionInfo = new ImageRegionInfo();
        }
        
        logger.debug( "Adding current region: " + regionInfo.getName() );
        imageRegionInfo.addRegionInfo( regionInfo );
        if( null == regionInfo.getFilepath() )
        {
            regionInfo.setFilepath( new File( imageFilePath ).getName() );   
        }        
        writeToXML( imageRegionInfo, xmlFilePath );
        return xmlFilePath;
    }

    /**
	 * Saves the image, the region metadata to the filepath location mentioned.
	 * 
	 * @param regionInfoList - The list of regions. 
	 * @param refImage - the image to be saved.
	 * @param filePath - the file location to save the data.
	 * @throws IOException.
	 * @throws JAXBException
	 * 
	 */
    @Override
    public void saveImageAndRegion(List<RegionInfo> regionInfoList,
            BufferedImage refImage, String filePath) throws IOException,
            JAXBException {
        if (null == refImage || null == regionInfoList || null == filePath
                || filePath.isEmpty()) {
            throw new IllegalArgumentException("Input parameter cant be null");
        }

        ImageRegionInfo imageRegionInfo = new ImageRegionInfo();
        imageRegionInfo.setRegionInfoList(regionInfoList);
        for (int i = 0; i < regionInfoList.size(); i++) {
            File file = new File(filePath);
            regionInfoList.get(i).setFilepath(file.getName());
        }
        logger.debug("Writing to XML");
        writeToXML(imageRegionInfo, filePath);
        File outputfile = new File(filePath);
        logger.debug("Writing to JPG :" + outputfile);
        ImageIO.write(refImage, IMAGE_FILE_FORMAT, outputfile);
    }

    /**
	 * Gets the all the RegionInfo corresponding to the filepath provided.
	 *
	 * @param filepath - to the region metadata
	 * @return List of RegionInfo.
	 * @throws IOException
	 */
    @Override
    public List<RegionInfo> getRegionInfo(String filepath) throws IOException {
        if (filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException(
                    "xmlPath cannot be null or empty");
        }

        List<RegionInfo> regionInfoList = null;

        ImageRegionInfo imageRegionInfo = loadFromXML(resourceClass, filepath);
        logger.debug("imageRegionInfo " + imageRegionInfo);
        if (imageRegionInfo != null) {
            regionInfoList = imageRegionInfo.getRegionInfoList();
        }
        for (RegionInfo regInfo : regionInfoList) {
            // Jpeg images will be found in the same folder as the xml"
            // Set the relative path to the jpeg"
            String relativeJPGPath = getRelativePathForJPG(regInfo, filepath);
            regInfo.setFilepath(relativeJPGPath);
        }
        return regionInfoList;
    }

    /**
	 * Gets the RegionInfo corresponding to the filepath and regionName provided.
	 *
	 * @param filepath - the filepath to the location of the region metadata.
	 * @param regionName - the name of the particular region
	 * @return - RegionInfo for the particular region.
	 * @throws IOException
	 */
    @Override
    public RegionInfo getRegionInfo(String filepath, String regionName)
            throws IOException {
        if (regionName == null || filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException(
                    "xmlPath cannot be null or empty");
        }

        RegionInfo regionInfo = null;
        // TODO: should this be null
        ImageRegionInfo imageRegionInfo = loadFromXML(getClass(), filepath);
        logger.debug("imageRegionInfo " + imageRegionInfo);
        if (imageRegionInfo != null) {
            regionInfo = imageRegionInfo.getRegion(regionName);
            if(regionInfo != null){
                // Jpeg images will be found in the same folder as the xml"
                // Set the relative path to the jpeg"
                String relativeJPGPath = getRelativePathForJPG(regionInfo, filepath);
                regionInfo.setFilepath(relativeJPGPath);
                logger.debug("regionInfo " + regionInfo + " regionName "
                        + regionName);
            }
        }

        return regionInfo;
    }

    /**
     * Populate this object with the image path and RegionInfo list.
     * 
     * @param resourceClass
     *            The Class that is in the same location as the XML resource we
     *            will load. This is null if we load from disk or a valid class
     *            if loading from a jar.
     * @param xmlPath
     *            The xml file to use to populate this object with. This can be
     *            a absolute path to the file or path to the resource.
     * @throws FileNotFoundException
     *             if the XML document cannot be loaded from the specified path.
     * @throws IllegalArgumentException
     *             if xmlPath is null or empty.
     * @throws RuntimeException
     *             if a region is not defined properly in the XML file.
     * @return Populated ImageRegionInfo object. Null if there is an error
     *         loading the file.
     */
    private final ImageRegionInfo loadFromXML(Class<?> resourceClass,
            String xmlPath) throws FileNotFoundException {
        if (xmlPath == null || xmlPath.isEmpty()) {
            logger.error("xmlPath cannot be null or empty");
            throw new IllegalArgumentException(
                    "xmlPath cannot be null or empty");
        }

        ImageRegionInfo imageRegInfo = null;

        InputStream is = getAppropriateIS(xmlPath);
        if (is != null) {
            imageRegInfo = loadFromSerializedXML(is);
            closInputStream(is);
            logger.debug("imageRegInfo " + imageRegInfo);
        }

        if (imageRegInfo == null) {
            throw new FileNotFoundException(xmlPath
                    + " could not be found on disk or as resource");
        }
        return imageRegInfo;
    }
    
    private InputStream getAppropriateIS(String xmlPath){
        InputStream is = null;
        File xmlFile = new File(xmlPath);
        logger.debug("xmlFile " + xmlFile);
        
        if (xmlFile.isFile()) {
            logger.debug("loading from disk");
            try
            {
                is = new FileInputStream(xmlFile);
            }
            catch ( FileNotFoundException e )
            {
                is = null;
            }
        } else {
            logger.debug("loading from resource");
            is = loadResource(resourceClass, xmlPath);
        }
        
        return is;
    }
    
    private void closInputStream(InputStream is){
        try {
            is.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

   

  

    /**
     * Attempts to load a resource from the specified path.
     * 
     * @param resourceClass
     *            The Class that is in the same location as the resources we
     *            will load.
     * @param path
     *            The path to load.
     * @return The ImputStream for the resource. null if the resource is not
     *         found.
     */
    private InputStream loadResource(Class<?> resourceClass, String path) {
        InputStream is = null;
        if (resourceClass != null && path != null) {
            is = resourceClass.getResourceAsStream(path);
            // this might work if the resource is in the classpath.
            if (is == null && !path.startsWith("/")) {
                is = resourceClass.getResourceAsStream("/" + path);
            }
        }
        return is;
    }

    /**
     * Loads RegionInfo from the specified input stream. This function does not
     * close the input stream.
     * 
     * @param is
     *            The input stream to load object from.
     * @return Populated RegionInfo object. null on error.
     */
    private final ImageRegionInfo loadFromSerializedXML(InputStream inputStream) {
        ImageRegionInfo imageRegionInfo = null;
        try {
            imageRegionInfo = RegionInfoXmlUtil.loadFromJaxbXML(inputStream);
        } catch (Exception e) {
            logger.warn("Error loading serialized object: " + e.getMessage());
            logger.warn("This may be a legacy file format.");
        }
        return imageRegionInfo;
    }

    /**
     * Writes this object instance to an xml file. This function will close the
     * output stream. This needs to happen otherwise the </java> take is not
     * added by the XMLEncoder.
     * 
     * @param os
     *            The output stream write to.
     * @throws JAXBException
     */
    private final void writeToXML(ImageRegionInfo imageRegionInfo,
            String filePath) throws IOException, JAXBException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("filePath cannot be null.");
        }

        filePath = changeExtensionToXML(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            imageRegionInfo.writeToXML(fos);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private String changeExtensionToXML(String filePath) {
        logger.debug("Changing extension to xml " + filePath);
        int dotPos = filePath.lastIndexOf(".");
        String filename = filePath.substring(0, dotPos);
        String xmlfile = filename + "." + "xml";
        logger.debug("XML FIle " + xmlfile);
        return xmlfile;
    }

    private String getRelativePathForJPG(RegionInfo regInfo, String filepath) {
        String relativePath = null;
        if (filepath != null && regInfo != null) {
            File xmlFile = new File(filepath);
            File jpegFile = new File(regInfo.getFilepath());
            if (xmlFile.getParent() == null) {
                relativePath = jpegFile.getName();
            } else {
                relativePath = xmlFile.getParent()
                        + System.getProperty("file.separator")
                        + jpegFile.getName();
            }
        }
        return relativePath;
    }
}