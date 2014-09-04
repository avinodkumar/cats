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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import junit.framework.Assert;

import org.testng.annotations.Test;
import javax.imageio.ImageIO;

@SuppressWarnings("restriction")
public class ImageUtilTest {
	
	// Make sure the new way and old way of getting byte[]
	// will produce the same data.
	@Test
	public void comparOldandNew() {
		final String imagePath = "./testImages/longSentence1.jpg";
		final File f = new File(imagePath);
		try {
			BufferedImage bimage = ImageIO.read(f);
			byte[] image = ImageUtil.bufferedImageToByteArray(bimage);
			byte[] image2 = old_bufferedImageToByteArray(bimage);

			int n = image.length;
			if (n != image2.length) {
				Assert.fail();
			}

			Boolean passed = true;
			// only compare data part of the byte[]
			// first few bytes are used for control structure.
			while (n-- != 16) {				
				if (image[n] != image2[n]) {
					System.out.println("nth byte: " + n + " " + image[n] + " "
							+ image2[n]);
					passed = false;
				}
			}
			Assert.assertTrue(passed);

		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private byte[] old_bufferedImageToByteArray(BufferedImage img)
			throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(img,"jpeg",os);
		return os.toByteArray();
	} 

}
