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

import javax.xml.bind.annotation.XmlElement;

/**
 * Used to return OCR results from JNI layer.
 */
public class OCRResult
{

    /**
     * OCR success returnCode.
     */
    public static final Integer SUCCESS = 1;

    /**
     * OCR failure returnCode.
     */
    public static final Integer FAILURE = 0;

    /**
     * This actual text result used in the compare.
     */
    protected String            textResult;

    /**
     * The return code of the functions. This will be 1 for success, 0 for
     * failure or some negative result on error.
     */
    protected Integer           returnCode;

    /**
     * Returns textResult.
     * 
     * @return the textResult
     */
    @XmlElement( name = "decodedText" )
    public String getTextResult()
    {
        return textResult;
    }

    /**
     * Sets the textResult.
     * 
     * @param textResult
     *            The text that ocr has found.
     */
    public void setTextResult( final String textResult )
    {
        this.textResult = textResult;
    }

    /**
     * Returns returnCode.
     * 
     * @return the returnCode
     */
    @XmlElement( name = "ocrTestResult" )
    public Integer getReturnCode()
    {
        return returnCode;
    }

    /**
     * Sets the returnCode.
     * 
     * @param returnCode
     *            The return code for the result, where -1 indicates error, 0
     *            for failure and 1 for success.
     */
    public void setReturnCode( final Integer returnCode )
    {
        this.returnCode = returnCode;
    }
}
