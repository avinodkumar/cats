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
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * Used to return OCR Result from JNI layer with some extra values.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OCRCompareResult")
public class OCRCompareResult extends OCRResult {
	
	/**
	 * First Image to return for an OCR Failure.Due to performance issue it's decided to
	 * Return as byte array, may have to change in order to deal with image object as such
	 */
	@XmlInlineBinaryData
	protected byte[] firstImageCompared;
	
	/**
	 * Last Image to return for an OCR Failure
	 */
	@XmlInlineBinaryData
	protected byte[] lastImageCompared;
	
	
	/**
     * The accuracy percentage.
     */
	@XmlElement(name = "percentAccurate")
    protected Integer  accuracy;

    /**
     * The number of deletions, insertions, or substitutions required to transform expectedText Integer o textResult.
     */
    protected Integer  distance;

    /**
     * The expected text string used in the compare.
     */
    protected String expectedText;

    /**
     *  Error log message to be sent back to client 
     */
    @XmlElement(name = "errorMessage")
    protected String errorMsg;
	
	/**
	 * Returns first image compared
	 * @return the firstImageCompared
	 */
	
	public byte[] getFirstImageCompared() {
		return firstImageCompared;
	}
	
	/**
	 * Sets the firstImageCompared.
	 * @param firstImageCompared to send back to the client if it is a failure.
	 */
	
	public void setFirstImageCompared(final byte[] firstImageCompared) {
		this.firstImageCompared = firstImageCompared;
	}
	
	/**
	 * Returns lastImageCompared compared
	 * @return the lastImageCompared
	 */

	public byte[] getLastImageCompared() {
		return lastImageCompared;
	}
	
	/**
	 * Sets the lastImageCompared.
	 * @param lastImageCompared will be returned in case of an OCR failure
	 */
	
	public void setLastImageCompared(final byte[] lastImageCompared) {
		this.lastImageCompared = lastImageCompared;
	}
	
    /**
     * Returns expectedText.
     * @return the expectedText
     */
    public String getExpectedText() {
        return expectedText;
    }

    /**
     * Sets the expectedText.
     * @param expectedText The text you are looking for in the image.
     */
    public void setExpectedText(final String expectedText) {
        this.expectedText = expectedText;
    }

    /**
     * Returns accuracy.
     * @return the accuracy
     */
    public Integer  getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the accuracy.
     * @param accuracy The percent success accuracy you are expecting for a successful comparison.
     */
    public void setAccuracy(final Integer  accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Returns distance.
     * @return the distance
     */
    public Integer  getDistance() {
        return distance;
    }

    /**
     * Sets the distance.
     * @param distance The distance.
     */
    public void setDistance(final Integer  distance) {
        this.distance = distance;
    }

    /**
     *  Sets the error message.
     *
     *  @param msg  The error message
     */
    public void setErrorMsg(String msg) {
        errorMsg = msg;
    }

    /**
     *  Gets the error message.
     *
     *  @return he error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }
}
