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
import java.util.List;

import com.comcast.cats.image.OCRCompareResult;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.provider.exceptions.OCRException;

/**
 * Interface that defines rules for providers who wish to provide for OCR
 * services.
 * 
 * @author minu
 */
public interface OCRProvider extends BaseProvider
{

    /**
     * Check if all the expected text is on the current screen. Timeouts are
     * disregarded.
     * 
     * @param ocrRegionList
     *            The list of ocr regions.
     * @return true if all the expected text is on the current screen. Otherwise
     *         false is returned.
     * @throws OCRException
     */
    boolean isAllOCRTextOnScreenNow( List< OCRRegionInfo > ocrRegionList ) throws OCRException;

    /**
     * Checks if the expected text from the specified region is on the current
     * screen. Timeouts are disregarded.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @return true if the expected text for the region is on the current
     *         screen. Otherwise false is returned.
     * @throws OCRException
     */
    boolean isOCRTextOnScreenNow( OCRRegionInfo ocrRegionInfo ) throws OCRException;

    /**
     * Waits for all the text regions to be on screen using specific region
     * timeouts. As soon as one region passes its timeout and does not find a
     * successful result this will fail.
     * 
     * @param ocrRegionList
     *            The list of ocr regions.
     * @return true if all the text regions were found within the regions
     *         specific timeouts. Otherwise false is returned.
     */
    boolean waitForAllOCRRegions( List< OCRRegionInfo > ocrRegionList ) throws OCRException;

    /**
     * Waits for the specified ocr region to be on screen using the specific
     * region timeout.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @return true if the ocr region was found within the region specific
     *         timeout. Otherwise false is returned.
     * @throws OCRException
     */
    boolean waitForOCRRegion( OCRRegionInfo ocrRegionInfo ) throws OCRException;

    /**
     * Waits for the ocr region to be on screen using the specific region
     * timeout.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param text
     *            The text to search for in the given region. Overrides the
     *            contents in the OCRRegionInfo instance.
     * @return boolean
     * @throws OCRException
     */
    boolean waitForTextInOCRRegion( OCRRegionInfo ocrRegionInfo, String text ) throws OCRException;

    /**
     * Waits for the ocr region to be on screen using the specific region
     * timeout.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param text
     *            The text to search for in the given region. Overrides the
     *            contents in the ocr region meta data.
     * @param timeOut
     *            Time out in seconds for the ocr comparison.
     * @return true if the the specified text is found within the region in
     *         specific timeout. Otherwise false is returned.
     * @throws OCRException
     */
    boolean waitForTextInOCRRegion( OCRRegionInfo ocrRegionInfo, String text, int timeOut ) throws OCRException;

    /**
     * Gets the current text found by OCR for the specific region. Timeouts are
     * disregarded.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @return The found text within the specified region. Null if text could
     *         not be retrieved.
     * @throws OCRException
     */
    String getCurrentText( OCRRegionInfo ocrRegionInfo ) throws OCRException;

    /**
     * Gets the current text found by OCR for the specific region in the image
     * specified.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param imageFile
     *            The external image for the comparison.
     * @return The found text within the specified region. Null if text could
     *         not be retrieved.
     * @throws OCRException
     */
    String getTextFromImage( OCRRegionInfo ocrRegionInfo, BufferedImage imageFile ) throws OCRException;

    /**
     * Checks if the expected text from the specified region is on the image.
     * Timeouts are disregarded.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param imageFile
     *            The external image for the comparison.
     * @return true if the expected text for the region is on the current
     *         screen. Otherwise false is returned.
     */
    boolean isOCRTextOnImage( OCRRegionInfo ocrRegionInfo, BufferedImage imageFile ) throws OCRException;

    /**
     * Performs OCR against the specified region on the current screen and
     * returns the OCRCompareResult. Timeouts are disregarded.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @return The OCRCompareResult instance
     * @throws OCRException
     */
    OCRCompareResult getOCRTextOnScreenNow( OCRRegionInfo ocrRegionInfo ) throws OCRException;

    /**
     * Waits for the specified ocr region to be on screen using the specific
     * region timeout. It returns the OCRCompareResult of last comparison.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @return OCRCompareResult of last comparison.
     * @throws OCRException
     */
    OCRCompareResult waitForOCRRegionCompareResult( OCRRegionInfo ocrRegionInfo ) throws OCRException;

    /**
     * Waits for the ocr region to be on screen using the specific region
     * timeout. It returns the OCRCompareResult of last comparison.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param text
     *            The text to search for in the given region. Overrides the
     *            contents in the OCRRegionInfo instance.
     * @return OCRCompareResult of last comparison.
     * @throws OCRException
     */
    OCRCompareResult waitForText( OCRRegionInfo ocrRegionInfo, String text ) throws OCRException;

    /**
     * Waits for the ocr region to be on screen using the specific region
     * timeout. It returns the OCRCompareResult of last comparison.
     * 
     * @param ocrRegionInfo
     *            ocrRegionInfo
     * @param text
     *            The text to search for in the given region. Overrides the
     *            contents in the ocr region meta data.
     * @param timeOut
     *            Time out in seconds for the ocr comparison.
     * @return OCRCompareResult of last comparison.
     * @throws OCRException
     */
    OCRCompareResult waitForText( OCRRegionInfo ocrRegionInfo, String text, int timeOut ) throws OCRException;

    /**
     * Performs OCR against the specified region on the image and returns the
     * OCRCompareResult. Timeouts are disregarded.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param imageFile
     *            The external image for the comparison.
     * @return OCRCompareResult of the comparison.
     * @throws OCRException
     */
    OCRCompareResult getOCRTextOnImage( OCRRegionInfo ocrRegionInfo, BufferedImage imageFile ) throws OCRException;

    /**
     * Checks whether the result accuracy is greater than the success tolerance
     * specified in the ocrRegionInfo.
     * 
     * @param ocrRegionInfo
     *            The ocr region metadata.
     * @param ocrResult
     *            The OCR comparison result.
     * @return True if the result accuracy is greater than the success tolerance
     *         else, false.
     */
    boolean isOCRResultAccurate( OCRRegionInfo ocrRegionInfo, OCRCompareResult ocrResult );

    /**
     * Method to set the Axis server url used in OCRRegionInfo instance.
     */
    void setVideoURL( String videoURL );

    /**
     * Method to get the Axis server url used in OCRRegionInfo instance.
     * 
     * @return Axis server url
     */
    String getVideoURL();

    /**
     * Check if all the expected text is on the current screen. Timeouts are
     * disregarded.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @return true if all the expected text is on the current screen. Otherwise
     *         false is returned.
     */
    @Deprecated
    boolean isAllOCRTextOnScreenNow( String ocrXMLPath );

    /**
     * Checks if the expected text from the specified region is on the current
     * screen. Timeouts are disregarded.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @param regionName
     *            The region name to use.
     * @return true if the expected text for the region is on the current
     *         screen. Otherwise false is returned.
     */
    @Deprecated
    boolean isOCRTextOnScreenNow( String ocrXMLPath, String regionName );

    /**
     * Waits for all the text regions to be on screen using specific region
     * timeouts. As soon as one region passes its timeout and does not find a
     * successful result this will fail.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @return true if all the text regions were found within the regions
     *         specific timeouts. Otherwise false is returned.
     */
    @Deprecated
    boolean waitForAllOCRRegions( String ocrXMLPath );

    /**
     * Waits for the specified ocr region to be on screen using the specific
     * region timeout.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @param regionName
     *            The name of the ocr region to wait for.
     * @return true if the ocr region was found within the region specific
     *         timeout. Otherwise false is returned.
     */
    @Deprecated
    boolean waitForOCRRegion( String ocrXMLPath, String regionName );

    /**
     * Waits for all the ocr region to be on screen using the specific region
     * timeout.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @param regionName
     *            The name of the ocr region to wait for.
     * @param text
     *            The text to search for in the given region. Overrides the
     *            contents of the file.
     * @return true if all the ocr region was found within the region specific
     *         timeout. Otherwise false is returned.
     */
    @Deprecated
    boolean waitForTextInOCRRegion( String ocrXMLPath, String regionName, String text );

    /**
     * Waits for all the ocr region to be on screen using the specific region
     * timeout.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @param regionName
     *            The name of the ocr region to wait for.
     * @param text
     *            The text to search for in the given region. Overrides the
     *            contents of the file.
     * @param timeOut
     *            - time out value for the ocr comparison.
     * @return true if all the ocr region was found within the region specific
     *         timeout. Otherwise false is returned.
     */
    @Deprecated
    boolean waitForTextInOCRRegion( String ocrXMLPath, String regionName, String text, int timeOut );

    /**
     * Gets the current text found by OCR for the specific region. Timeouts are
     * disregarded.
     * 
     * @param ocrXMLPath
     *            The path to xml file containing region information.
     * @param regionName
     *            The name of the ocr region to use.
     * @return The found text within the specified region. Null if text could
     *         not be retrieved.
     */
    @Deprecated
    String getCurrentText( String ocrXMLPath, String regionName );
}
