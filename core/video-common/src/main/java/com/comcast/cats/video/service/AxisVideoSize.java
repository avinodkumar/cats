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
package com.comcast.cats.video.service;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public enum AxisVideoSize {
	AXIS_D1				(720,480,"D1", false),
	AXIS_D1_SQUARE		(640,480,"D1", true),
	AXIS_4CIF			(704,480,"4CIF", false),
	AXIS_2CIF			(704,240,"2CIF", false),
	AXIS_CIF_SQUARE     (320,240,"CIF", true),
	AXIS_CIF			(352,240,"CIF", false),
	AXIS_QCIF_SQUARE    (160,120,"QCIF", true),
	AXIS_QCIF			(176,120,"QCIF", false);
	

	private Dimension dim;
	private String axisStr;
	private String dimStr;
	private boolean squareResolution;
	
	AxisVideoSize(int width,int height, String axisString, boolean squareRes) {
		this.axisStr = axisString;
		this.squareResolution = squareRes;
		this.dim = new Dimension(width,height);
		this.dimStr = createDimensionString(dim);
	}
	
	private static String createDimensionString(Dimension dim) {
		String dimStr = Integer.toString(dim.width) + "x" + Integer.toString(dim.height);
		return dimStr;
	}
	
	public static List<String> getVideoSizesAsString() {
		AxisVideoSize[] sizes = AxisVideoSize.values();
		List<String> videoSizes = new ArrayList<String>();
		for(int i=0;i<sizes.length;i++) {
			videoSizes.add(sizes[i].dimStr);
		}
		return videoSizes;
	}
	
	public Dimension getDimension() {
		return this.dim;
	}
	
	public String getDimensionString() {
		return this.dimStr;
	}
	
	public String getAxisResolution() {
		return this.axisStr;
	}
	
	public boolean isSquareResoultion() {
		return squareResolution;
	}
	
	public static Dimension getDimension(AxisVideoSize axisVideoSize) {
		return axisVideoSize.dim;
	}
	
	public static String getDimensionString(AxisVideoSize axisVideoSize) {
		return axisVideoSize.dimStr;
	}
	
	public static AxisVideoSize getAxisFormatFromDimension(Dimension dim) {
		return getAxisFormatFromDimension(createDimensionString(dim));
	}
	
	public static AxisVideoSize getAxisFormatFromDimension(String dim) {
		AxisVideoSize[] sizes = AxisVideoSize.values();
		for(int i=0;i<sizes.length;i++) {
			if(sizes[i].dimStr.equals(dim)) {
				return sizes[i];
			}
		}
		return null;
	}
}
