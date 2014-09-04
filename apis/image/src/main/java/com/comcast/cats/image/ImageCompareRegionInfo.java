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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Stores ImageCompare specific region information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageCompareRegionInfo")
public class ImageCompareRegionInfo extends RegionInfo {
    private static final long serialVersionUID = 4428600329230886319L;

    /**
     * The default match percent.
     */
    public static final Float DEFAULT_MATCH_PERCENT = 85.0f;

    /**
     * The default red tolerance.
     */
    public static final Integer  DEFAULT_RED_TOLERANCE = 20;

    /**
     * The default green tolerance.
     */
    public static final Integer  DEFAULT_GREEN_TOLERANCE = 20;

    /**
     * The default blue tolerance.
     */
    public static final Integer  DEFAULT_BLUE_TOLERANCE = 20;

    /**
     * The maximum RGB colour tolerance.
     */
    public static final Integer  MAX_RGB_TOLERANCE = 255;

    /**
     * The minimum RGB colour tolerance.
     */
    public static final Integer  MIN_RGB_TOLERANCE = 0;

    /**
     * The maximum match percent.
     */
    public static final Float MAX_MATCH_PERCENT = 100.0f;

    /**
     * The minimum match percent.
     */
    public static final Float MIN_MATCH_PERCENT = 0.0f;
    @XmlElement(name = "red_tolerance")
    protected Integer  redTolerance = 0;
    @XmlElement(name = "green_tolerance")
    protected Integer  greenTolerance = 0;
    @XmlElement(name = "blue_tolerance")
    protected Integer  blueTolerance = 0;
    @XmlElement(name = "match_percentage")
    protected Float matchPct= 0.0f;


    /**
     * Creates an instance of region info with default values.
     */
    public ImageCompareRegionInfo() {
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
    public ImageCompareRegionInfo(String name, Integer  x, Integer  y, Integer  w, Integer  h) {
        super(name, x, y, w, h);
        blueTolerance = DEFAULT_BLUE_TOLERANCE;
        redTolerance = DEFAULT_RED_TOLERANCE;
        greenTolerance = DEFAULT_GREEN_TOLERANCE;
        matchPct = DEFAULT_MATCH_PERCENT;
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
     * @param filePath
     *           The filepath to the image and the region metadata.
     */
    public ImageCompareRegionInfo(String name, Integer  x, Integer  y, Integer  w, Integer  h, String filepath) {
        super(name, x, y, w, h);
        blueTolerance = DEFAULT_BLUE_TOLERANCE;
        redTolerance = DEFAULT_RED_TOLERANCE;
        greenTolerance = DEFAULT_GREEN_TOLERANCE;
        matchPct = DEFAULT_MATCH_PERCENT;
        this.filepath = filepath;
    }

    /**
     * Copy Constructor.
     * @param org The original RegionInfo.
     * @deprecated Use clone().
     */
    @Deprecated
    private ImageCompareRegionInfo(ImageCompareRegionInfo org) {
        super(org);
        this.matchPct = org.getMatchPct();
        this.blueTolerance = org.getBlueTolerance();
        this.greenTolerance = org.getGreenTolerance();
        this.redTolerance = org.getRedTolerance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final ImageCompareRegionInfo getCopy() {
        return new ImageCompareRegionInfo(this);
    }

    /**
     * Sets the red tolerance.
     *
     * @param redTolerance
     *           the redTolerance to set
     */
    public final void setRedTolerance(Integer  redTolerance) {
        this.redTolerance = redTolerance;
    }

    /**
     * Gets the red tolerance.
     *
     * @return the redTolerance
     */
    public final Integer  getRedTolerance() {
        return redTolerance;
    }

    /**
     * Sets the blue tolerance.
     *
     * @param blueTolerance
     *           the blueTolerance to set
     */
    public final void setBlueTolerance(Integer  blueTolerance) {
        this.blueTolerance = blueTolerance;
    }

    /**
     * Gets the blue tolerance.
     *
     * @return the blueTolerance
     */
    public final Integer  getBlueTolerance() {
        return blueTolerance;
    }

    /**
     * Sets the green tolerance.
     *
     * @param greenTolerance
     *           the greenTolerance to set
     */
    public final void setGreenTolerance(Integer  greenTolerance) {
        this.greenTolerance = greenTolerance;
    }

    /**
     * Gets the green tolerance.
     *
     * @return the greenTolerance
     */
    public final Integer  getGreenTolerance() {
        return greenTolerance;
    }

    /**
     * Sets the match percent.
     *
     * @param matchPct
     *           the matchPct to set
     */
    public final void setMatchPct(Float matchPct) {
        this.matchPct = matchPct;
    }

    /**
     * Gets the match percent.
     *
     * @return the matchPct
     */
    public final Float getMatchPct() {
        return matchPct;
    }

	public static Float getDefaultMatchPercent() {
		return DEFAULT_MATCH_PERCENT;
	}

	public static Integer  getDefaultRedTolerance() {
		return DEFAULT_RED_TOLERANCE;
	}

	public static Integer  getDefaultGreenTolerance() {
		return DEFAULT_GREEN_TOLERANCE;
	}

	public static Integer  getDefaultBlueTolerance() {
		return DEFAULT_BLUE_TOLERANCE;
	}

	public static Integer  getMaxRgbTolerance() {
		return MAX_RGB_TOLERANCE;
	}

	public static Integer  getMinRgbTolerance() {
		return MIN_RGB_TOLERANCE;
	}

	public static Float getMaxMatchPercent() {
		return MAX_MATCH_PERCENT;
	}

	public static Float getMinMatchPercent() {
		return MIN_MATCH_PERCENT;
	}
    /**
     * Returns a string containing all the values in this object.
     *
     * @return a string containing all the values in this object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(name).append(", X: ").append(x).append(", Y: ").append(y).append(", Width: ").append(
            width).append(", Height: ").append(height).append(", Blue Tolerance: ").append(blueTolerance).append(
            ", Red Tolerance: ").append(redTolerance).append(", Green Tolerance: ").append(greenTolerance).append(
            ", X Tolerance: ").append(xTolerance).append(", Y Tolerance: ").append(yTolerance).append(", Match Pct: ").append(
            matchPct);
        return builder.toString();
    }

    /**
     * Compares this object with the specified object for equality.
     * @param o the object to compare against.
     * @return true if they are equal.
     */
    @Override
    public boolean equals(final Object o) {
        boolean ret = false;
        if (!(o instanceof ImageCompareRegionInfo)) {
            ret = false;
        } else if (this == o) {
            ret = true;
        } else {
            ImageCompareRegionInfo info = (ImageCompareRegionInfo) o;
            ret = new EqualsBuilder().append(this.name, info.getName()).append(this.x, info.getX()).append(this.y,
                info.getY()).append(this.width, info.getWidth()).append(this.height, info.getHeight()).append(
                this.matchPct, info.getMatchPct()).append(this.greenTolerance, info.getGreenTolerance()).append(
                this.redTolerance, info.getRedTolerance()).append(this.blueTolerance, info.getBlueTolerance()).append(
                this.xTolerance, info.getXTolerance()).append(this.yTolerance, info.getYTolerance()).isEquals();
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
