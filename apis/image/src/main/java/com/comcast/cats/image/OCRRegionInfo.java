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

import java.awt.Rectangle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * Stores OCR specific region information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OCRRegionInfo")
public class OCRRegionInfo extends RegionInfo {
    private static final long serialVersionUID = 1225874371471320405L;
    @XmlElement(name = "AxisURL")
    protected String url;
    @XmlElement(name = "ExpectedResultText")
    protected String expectedResult;
    @XmlElement(name = "OCRtimeout")
    protected Integer  timeout;
    @XmlElement(name = "Tolerance")
    protected Integer  successTolerance;

    /**
     * The default expected result.
     */
    public static final String DEFAULT_EXPECTED_RESULT = "";

    /**
     * The default expected result.
     */
    public static final String DEFAULT_URL = "";

    /**
     * Default timeout.
     */
    public static final Integer  DEFAULT_TIMEOUT = 10;

    /**
     * The default success tolerance.
     */
    public static final Integer  DEFAULT_SUCCESS_TOLERANCE = 80;

    /**
     * Creates an instance of region info with default values.
     */
    public OCRRegionInfo() {
        this("", 0, 0, 0, 0);
    }

    /**
     * Sets the given parameters.
     * 
     * @param name
     *           The name of the region.
     * @param x
     *           The x coordinate.
     * @param y
     *           The y coordinate.
     * @param w
     *           The region width.
     * @param h
     *           The region height.
     */
    public OCRRegionInfo(String name, Integer  x, Integer  y, Integer  w, Integer  h) {
        super(name, x, y, w, h);
        expectedResult = DEFAULT_EXPECTED_RESULT;
        timeout = DEFAULT_TIMEOUT;
        successTolerance = DEFAULT_SUCCESS_TOLERANCE;
        url = DEFAULT_URL;
    }

    /**
     * Copy Constructor.
     * @param org The original RegionInfo.
     * @deprecated Use clone().
     */
    @Deprecated
    private OCRRegionInfo(OCRRegionInfo org) {
        super(org);
        this.setUrl(org.getUrl());
        this.setExpectedResult(org.getExpectedResult());
        this.setSuccessTolerance(org.getSuccessTolerance());
        this.setTimeout(org.getTimeout());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final OCRRegionInfo getCopy() {
        return new OCRRegionInfo(this);
    }

    /**
     * Gets the URL.
     * @return the url.
     */
    
    public final String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     * @param url the url to set.
     */
    public final void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the expected text.
     * @return The expected text.
     */    
    public final String getExpectedResult() {
        return expectedResult;
    }

    /**
     * Sets the expected text.
     * @param expectedResult the expected text.
     */
    public final void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    /**
     * Gets the timeout.
     * @return the timeout.
     */    
    public final Integer  getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout.
     * @param timeout the timeout.
     */
    public final void setTimeout(Integer  timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets the expected success accuracy.
     * @return the expected success accuracy.
     */    
    public final Integer  getSuccessTolerance() {
        return successTolerance;
    }

    /**
     * Sets the success tolerance.
     * @param successTolerance The success tolerance.
     */
    public final void setSuccessTolerance(Integer  successTolerance) {
        this.successTolerance = successTolerance;
    }

    /**
     * Returns a string containing all the values in this object.
     * 
     * @return a string containing all the values in this object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(name).append(", X: ").append(x).append(", Y: ").append(y).append(", Width: ").append(width).append(", Height: ").append(height).append(", URL: ").append(url).append(", Expected Result: ").append(expectedResult).append(", Timeout: ").append(timeout).append(", Success Tolerance: ").append(successTolerance);
        return builder.toString();
    }

    /**
     * Returns a Rectangle object comprising of the x,y,width and height values from this region info.
     * If there invalid x, y, widht, or height values, null is returned.
     * 
     * @return The Rectangle object representing this region info or null if there are invalid values.
     */
    public Rectangle getJavaRect() {
        Rectangle rectangle = new Rectangle();
        if (x >= 0 && y >= 0 && width > 0 && height > 0) {
            rectangle.setBounds(x, y, width, height);
        } else {
            rectangle = null;
        }
        return rectangle;       
    }

    /**
     * Compares this object with the specified object for equality.
     * @param o the object to compare against.
     * @return true if they are equal.
     */
    @Override
    public boolean equals(final Object o) {
        boolean ret;
        if (!(o instanceof OCRRegionInfo)) {
            ret = false;
        } else if (this == o) {
            ret = true;
        } else {
            OCRRegionInfo info = (OCRRegionInfo) o;
            ret = new EqualsBuilder().append(this.name, info.getName()).append(this.x, info.getX()).append(this.y, info.getY()).append(this.width, info.getWidth()).append(this.height, info.getHeight()).append(this.url, info.getUrl()).append(this.expectedResult, info.getExpectedResult()).append(this.timeout, info.getTimeout()).append(this.successTolerance, info.getSuccessTolerance()).append(this.url, info.getUrl()).isEquals();
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int  hashCode() {
        final int  SEVENTEEN = 17;
        final int  THIRTYSEVEN = 37;
        return new HashCodeBuilder(SEVENTEEN, THIRTYSEVEN).append(this.name).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return super.clone();
    }
}
