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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.comcast.cats.provider.VideoProvider;

public class VideoCaptureIT extends BaseVideo {

	public VideoCaptureIT() throws URISyntaxException {
		super();
	}

	public void printPixelARGB(Integer x, Integer y, int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		System.out.println(x + "x" + y + " :argb: " + alpha + ", " + red + ", "
				+ green + ", " + blue);
	}

	public int maskValue(int pixel) {
		int grey = (pixel) & 0xFF;
		int rtn = 0xFF000000;
		// int rtn = 0xFFFFFFFF;
		if (grey >= 0xA0 /* || grey <= 0x40 */) {
			rtn = 0xFFFFFFFF;
			// rtn = 0xFF000000;
		}
		return rtn;
	}

	public void captureImage(VideoProvider vp, Integer n) throws IOException {
		BufferedImage image = vp.getVideoImage();
		ImageIO.write(image, "JPEG", new File("Color-" + n + ".jpg"));
		int w = image.getWidth();
		int h = image.getHeight();
		System.out.println("width, height: " + w + ", " + h);

		long start = System.currentTimeMillis();

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				// System.out.println("x,y: " + j + ", " + i);
				int pixel = image.getRGB(j, i);
				image.setRGB(j, i, maskValue(pixel));
				// printPixelARGB(j, i, pixel);
			}
		}

		BufferedImage grey = new BufferedImage(w, h,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grey.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		long end = System.currentTimeMillis();
		Long diff = end - start;
		System.out.println("Processing took= " + diff);
		ImageIO.write(grey, "JPEG", new File("GreyScale-" + n + ".jpg"));
	}

	@Test
	public void turnIntoMonoChromeImage() throws IOException {
		// Load the file.
		VideoProvider vp = new VideoServiceImpl(dispatcher, axisLocator);

		for (int i = 0; i < 1; i++) {
			captureImage(vp, i);
		}
	}
}
