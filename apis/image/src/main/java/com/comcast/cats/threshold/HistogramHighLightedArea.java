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
package com.comcast.cats.threshold;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.imageutility.ImageUtility;
import com.comcast.cats.imageutility.ImageUtilityException;

/**
 * This class demonstrates how to load an Image from an external file.It
 * implements otsu thresholding method to identify text from an image.
 * 
 * @author :aswathyjs
 * @author :rahulchandra
 * @author Manesh Thomas
 */

public class HistogramHighLightedArea extends Component {
	private static final long serialVersionUID = 1L;
	private static final int ARRAY_SIZE = 256;
	/**
	 * Logger instance.
	 */
	private static Logger logger = LoggerFactory
			.getLogger(HistogramHighLightedArea.class);
	private BufferedImage rgbImage; // rgb image.
	private BufferedImage image;// input source image
	private Raster imageRaster; // image raster
	private int[] histogram = new int[ARRAY_SIZE];
	private boolean flag = false; // flag to check whether the invert function has been called.

	/**
	 * Construct the HistogramHighLightedArea object.
	 * 
	 * @param image
	 *            Path of the image to be read
	 * @param coordinate1
	 *            starting x coordinate
	 * @param coordinate2
	 *            starting y coordinate
	 * @param coordinate3
	 *            ending x coordinate
	 * @param coordinate4
	 *            ending y coordinate
	 */

	public HistogramHighLightedArea(BufferedImage image, int coordinate1,
			int coordinate2, int coordinate3, int coordinate4)
			throws ImageUtilityException {

		rgbImage = image;

		int width = (coordinate3 - coordinate1);
		int height = (coordinate4 - coordinate2);

		try {
			rgbImage = rgbImage.getSubimage(coordinate1, coordinate2, width,
					height);
		} catch (java.awt.image.RasterFormatException rasterException) {
			logger.error(rasterException.getMessage());
			throw new ImageUtilityException(rasterException.getMessage());
		}
	}

	/**
	 * This function converts RGB image to gray scale image
	 * 
	 * @param image
	 *            buffered image to store the output
	 * @param rgbImage
	 *            input Buffered RGB image to be converted
	 * @return : gray scale BufferedImage
	 */

	public BufferedImage convertToGrayScale(BufferedImage image,
			BufferedImage rgbImage) {
		int imageWidth = rgbImage.getWidth();
		int imageHeight = rgbImage.getHeight();
		image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics g = image.getGraphics();
		g.drawImage(rgbImage, 0, 0, null);
		imageRaster = image.getData();
		return image;
	}

	/**
	 * This function implements the OTSU thresholding algorithm.
	 * 
	 * @param data
	 *            : Data buffer on which thresholding has to be done
	 * @return threshold : Threshold value is returned
	 */
	public int otsuThresholding(DataBuffer data) {

		int[] histogramData = new int[ARRAY_SIZE];
		int sourceData[] = new int[data.getSize()];
		for (int i = 0; i < data.getSize(); i++) {
			sourceData[i] = data.getElem(i);
		}
		int ptr = 0;
		while (ptr < sourceData.length) {
			int h = 0xFF & sourceData[ptr];
			histogramData[h]++;
			ptr++;
		}

		// Total number of pixels
		int total = sourceData.length;
		float sum = 0;

		for (int i = 0; i < 255; i++)
			sum += i * histogramData[i];

		float sumBackground = 0;
		int weightBackground = 0;
		int weightForeground = 0;

		float varMax = 0;
		int threshold = 0;

		for (int i = 0; i < ARRAY_SIZE; i++) {
			weightBackground += histogramData[i]; // Weight Background
			if (weightBackground == 0)
				continue;

			weightForeground = (total - weightBackground); // Weight Foreground
			if (weightForeground == 0)
				break;

			sumBackground += (float) (i * histogramData[i]);
			float meanBackground = sumBackground / weightBackground; // Mean
																		// Background
			float meanForeground = (sum - sumBackground) / weightForeground; // Mean
																				// Foreground

			// Calculate Between Class Variance
			float varBetween = (float) weightBackground
					* (float) weightForeground
					* (meanBackground - meanForeground)
					* (meanBackground - meanForeground);
			// Check if new maximum found
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = i;
			}
		}
		return threshold;
	}

	/**
	 * This function sets the histogram array according to input image buffer
	 * 
	 * @param data
	 *            : Data buffer whose histogram is to be generated
	 * @return Generated histogram is returned
	 */
	public int[] setHistogram(DataBuffer data) {
		for (int i = 0; i < data.getSize(); i++) {
			int intensity;
			intensity = data.getElem(0, i);
			histogram[intensity] = histogram[intensity] + 1;
		}
		return histogram;
	}

	/**
	 * This function finds the intensity corresponding to maximum value in
	 * histogram
	 * 
	 * @return max : Intensity value with maximum count
	 */
	public int getHistogramMaximum() {
		int maximum = 0;
		int count = 0;
		for (int i = 0; i < ARRAY_SIZE; i++) {
			if (histogram[i] > count) {
				count = histogram[i];
				maximum = i;
			}
		}
		return maximum;
	}

	/**
	 * This function inverts the original image
	 * 
	 * @param rgbImage
	 *            : Buffered image of RGB image
	 * @return DataBuffer of RGB image
	 */

	public DataBuffer invertImage(BufferedImage rgbImage) {

		for (int i = 0; i < rgbImage.getHeight(); i++) {
			for (int j = 0; j < rgbImage.getWidth(); j++) {
				int newRgb = (~(rgbImage.getRGB(j, i)));
				rgbImage.setRGB(j, i, newRgb);
			}

		}
		flag = true;
		return rgbImage.getData().getDataBuffer();
	}

	/**
	 * This function eliminates the unwanted information from background
	 * 
	 * @param data
	 *            : input image data buffer
	 * @param rightCount
	 *            : measure of flatness of the histogram between zero intensity
	 *            and intensity corresponds maximum value in the histogram
	 * @param leftCount
	 *            : measure of flatness of the histogram between intensity
	 *            corresponds maximum value in the histogram and maximum
	 *            intensity
	 * @param value
	 *            : intensity value to which pixels has to be set
	 * @return intermediateData : Data buffer corresponding to intermediate
	 *         image.
	 */

	public DataBuffer createIntermediateImage(DataBuffer data, int rightCount,
			int leftCount, int value) {
		Raster intermediateImageRaster = image.getData();
		DataBuffer intermediateData = intermediateImageRaster.getDataBuffer();

		for (int l = 0; l < data.getSize(); l++) {
			if (rightCount < leftCount) {
				if (data.getElem(l) < value) {
					intermediateData.setElem(l, value);
				} else {
					intermediateData.setElem(l, data.getElem(l));
				}
			} else {
				if (data.getElem(l) < value) {
					intermediateData.setElem(l, data.getElem(l));
				} else {
					intermediateData.setElem(l, value);
				}
			}
		}
		updateImage(intermediateData);
		return intermediateData;
	}

	/**
	 * This function enhances the text area.
	 * 
	 * @param intermediateData
	 *            : Data buffer corresponding to intermediate data
	 * @param rightCount
	 *            :measure of flatness of the histogram between zero intensity
	 *            and intensity corresponds maximum value in the histogram
	 * @param leftCount
	 *            :measure of flatness of the histogram between intensity
	 *            corresponds maximum value in the histogram and maximum
	 *            intensity
	 * @param threshold
	 *            : threshold value
	 * @return enhanced data buffer.
	 */

	public DataBuffer enhanceImage(DataBuffer intermediateData, int rightCount,
			int leftCount, int threshold) {
		Raster enhancedImageRaster = image.getData();
		DataBuffer enhancedData = enhancedImageRaster.getDataBuffer();
		for (int i = 0; i < intermediateData.getSize(); i++) {
			int intensity;
			intensity = intermediateData.getElem(0, i);

			if (rightCount < leftCount) {
				if (intensity > threshold) {
					enhancedData.setElem(i, 0);
				} else {
					enhancedData.setElem(i, 255);
				}
			} else {
				if (intensity < threshold) {
					enhancedData.setElem(i, 0);
				} else {
					enhancedData.setElem(i, 255);
				}
			}
		}
		updateImage(enhancedData);
		return enhancedData;
	}

	/**
	 * This function highlights the text area in an image
	 * 
	 * @param VC
	 *            : parameter to indicate visual cue images
	 * @return enhancedData : DataBuffere corresponding to enhanced data
	 */
	public DataBuffer highlightText(String VC) {
		Raster imgraster = image.getData();
		DataBuffer data = imgraster.getDataBuffer();
		int rightCount = 0; /*
							 * measure of flatness of the histogram between zero
							 * intensity and intensity corresponds maximum value
							 * in the histogram
							 */
		int leftCount = 0; /*
							 * measure of flatness of the histogram between
							 * intensity corresponds maximum value in the
							 * histogram and maximum intensity
							 */
		// histogram is more flatter in the text area.
		int threshold = 0;
		histogram = setHistogram(data);
		int max = 0;
		max = getHistogramMaximum();
		int present = 0;
		int value = 0;
		int next = 1;
		int rightTotal = 1; /*
							 * total number of none zero intensity points
							 * between zero intensity and intensity corresponds
							 * maximum value in the histogram
							 */
		int leftTotal = 1; /*
							 * total number of none zero intensity points
							 * between intensity corresponds maximum value in
							 * the histogram and maximum intensity
							 */

		for (int i = 0; i < ARRAY_SIZE; i++) {
			if (histogram[i] > 0) {
				if (i > max) {
					rightTotal++;
				} else if (i < max) {
					leftTotal++;
				}
			}
		}

		// calculates the pixel difference between adjacent intensities
		for (present = 0; present < max; present++) {
			value = Math.abs((histogram[next] - histogram[present]));
			leftCount = leftCount + value;
			next++;
		}
		leftCount = leftCount / leftTotal;
		for (present = max; present < 255; present++) {
			value = Math.abs((histogram[next] - histogram[present]));
			rightCount = rightCount + value;
			next++;
		}
		rightCount = rightCount / rightTotal;

		threshold = otsuThresholding(data);
		value = (max + threshold) / 2;
		if (max == 0) {
			int temp = rightCount;
			rightCount = leftCount;
			leftCount = temp;
		}
		if (VC != null) {
			rightCount = leftTotal;
			leftCount = rightTotal;
			value = max;
			if (flag == true) {
				rightCount = rightTotal;
				leftCount = leftTotal;

			}
		}
		DataBuffer intermediateData = createIntermediateImage(data, rightCount,
				leftCount, value);
		threshold = otsuThresholding(intermediateData);
		DataBuffer enhancedData = enhanceImage(intermediateData, rightCount,
				leftCount, threshold);
		return enhancedData;
	}

	/**
	 * Highlight the Background Text.
	 */
	public DataBuffer highlightBackgroundText(String vc) {
		Raster imgraster = image.getData();
		DataBuffer data = imgraster.getDataBuffer();
		int rightCount = 0; /*
							 * measure of flatness of the histogram between zero
							 * intensity and intensity corresponds maximum value
							 * in the histogram
							 */
		int leftCount = 0; /*
							 * measure of flatness of the histogram between
							 * intensity corresponds maximum value in the
							 * histogram and maximum intensity
							 */
		// histogram is more flatter in the text area.
		int threshold = 0;
		histogram = setHistogram(data);
		int max = 0;
		max = getHistogramMaximum();
		int present = 0;
		int value = 0;
		int next = 1;
		int rightTotal = 1; /*
							 * total number of none zero intensity points
							 * between zero intensity and intensity corresponds
							 * maximum value in the histogram
							 */
		int leftTotal = 1; /*
							 * total number of none zero intensity points
							 * between intensity corresponds maximum value in
							 * the histogram and maximum intensity
							 */
		for (int i = 0; i < ARRAY_SIZE; i++) {
			if (histogram[i] > 0) {
				if (i > max) {
					rightTotal++;
				} else if (i < max) {
					leftTotal++;
				}
			}
		}

		// calculates the pixel difference between adjacent intensities
		for (present = 0; present < max; present++) {
			value = Math.abs((histogram[next] - histogram[present]));
			leftCount = leftCount + value;
			next++;
		}
		leftCount = leftCount / leftTotal;

		for (present = max; present < 255; present++) {
			value = Math.abs((histogram[next] - histogram[present]));
			rightCount = rightCount + value;
			next++;
		}
		rightCount = rightCount / rightTotal;

		threshold = otsuThresholding(data);
		if (rightCount < leftCount) {
			threshold = threshold + 10;
		} else {
			threshold = threshold - 10;
		}

		value = (max + threshold) / 2;
		if (max == 0) {
			int temp = rightCount;
			rightCount = leftCount;
			leftCount = temp;
		}
		if (vc != null) {
			rightCount = leftTotal;
			leftCount = rightTotal;
			value = max;
			if (flag == true) {
				rightCount = rightTotal;
				leftCount = leftTotal;
			}
		}
		DataBuffer intermediateData = createIntermediateImage(data, rightCount,
				leftCount, value);
		threshold = otsuThresholding(intermediateData);
		if (rightCount < leftCount) {
			threshold = threshold + 10;
		} else {
			threshold = threshold - 10;
		}
		DataBuffer enhancedData = enhanceImage(intermediateData, rightCount,
				leftCount, threshold);
		return enhancedData;
	}

	/**
	 * This function updates the image with new Data buffer.
	 * 
	 * @param updatedData
	 *            : Updated Data buffer with which Image has to be redrawn
	 * 
	 */
	public void updateImage(DataBuffer updatedData) {
		Raster updatedRaster = Raster.createRaster(
				imageRaster.getSampleModel(), updatedData, null);
		image.setData(updatedRaster);

	}

	/**
	 * This function processes the visual cue images
	 * 
	 * @param vc
	 *            : parameter to indicate visual cue images
	 * @param textBuffer
	 *            : Data buffer corresponding to normal image
	 */

	public void processVisualCueImage(String vc, DataBuffer textBuffer) {
		DataBuffer VisualCue_Text = null;

		image = convertToGrayScale(image, rgbImage);
		invertImage(image);

		VisualCue_Text = highlightText(vc);

		for (int i = 0; i < textBuffer.getSize(); i++) {

			textBuffer.setElem(i,
					(textBuffer.getElem(i) & VisualCue_Text.getElem(i)));
		}

		updateImage(textBuffer);

	}

	/**
	 * This function merges the enhanced sub images
	 * 
	 * @param subImageData
	 *            : Data Buffer which stores the sub image
	 * @param originalImageData
	 *            : Data Buffer which stores the original image
	 * @param subImageCoordinate1
	 *            : starting x coordinate of sub image
	 * @param subImageCoordinate2
	 *            : starting y coordinate of sub image
	 * @param subImageCoordinate3
	 *            : ending x coordinate of sub image
	 * @param subImageCoordinate4
	 *            : ending y coordinate of sub image
	 * @param originalImageWidth
	 *            : Width of original image
	 * @return original image data buffer after merging
	 */
	public DataBuffer mergeHighLightedImage(DataBuffer subImageData,
			DataBuffer originalImageData, int subImageCoordinate1,
			int subImageCoordinate2, int subImageCoordinate3,
			int subImageCoordinate4, int originalImageWidth) {

		int position = ((subImageCoordinate2 * originalImageWidth) + subImageCoordinate1);
		/*
		 * starting position in the original image data buffer where the sub
		 * image is to be placed
		 */
		int subImageWidth = subImageCoordinate3 - subImageCoordinate1;
		int subImageHeight = subImageCoordinate4 - subImageCoordinate2;

		for (int j = 0; j < subImageHeight; j++) {

			position = position + originalImageWidth;
			int offset = 0;
			for (int k = (j * subImageWidth); k < (subImageWidth + (j * subImageWidth)); k++) {
				originalImageData.setElem(position + offset,
						subImageData.getElem(k));
				offset++;
			}
		}

		return originalImageData;
	}

	/**
	 * This function updates the original image by replacing the sub image
	 * portion with the background.
	 * 
	 * @param copyImageData
	 *            : Data buffer which stores the copy of original image
	 * @param subImageCoordinate1
	 *            : starting x coordinate of sub image
	 * @param subImageCoordinate2
	 *            : starting y coordinate of sub image
	 * @param subImageCoordinate3
	 *            : ending x coordinate of sub image
	 * @param subImageCoordinate4
	 *            : ending y coordinate of sub image
	 * @param originalImageWidth
	 *            : Width of original image
	 * @param backgroundIntensity
	 *            : background intensity of the image
	 * @return updated data buffer
	 */

	public DataBuffer updateOriginalImage(DataBuffer copyImageData,
			int subImageCoordinate1, int subImageCoordinate2,
			int subImageCoordinate3, int subImageCoordinate4,
			int originalImageWidth, int backgroundIntensity) {

		int position = ((subImageCoordinate2 * originalImageWidth) + subImageCoordinate1);
		/*
		 * starting position in the original image data buffer where the sub
		 * image is to be placed
		 */
		int subImageWidth = subImageCoordinate3 - subImageCoordinate1;
		int subImageHeight = subImageCoordinate4 - subImageCoordinate2;

		for (int j = 0; j < subImageHeight; j++) {
			position = position + originalImageWidth;
			int offset = 0;
			for (int k = (j * subImageWidth); k < (subImageWidth + (j * subImageWidth)); k++) {
				copyImageData.setElem(position + offset, backgroundIntensity);
				offset++;
			}
		}
		return copyImageData;
	}

	/**
	 * This function calculates the background intensity.
	 * 
	 * @param copyData
	 *            : Data buffer which stores the copy of original data.
	 * @return background intensity
	 */

	public int getBackgroundIntensity(DataBuffer copyData) {
		int[] intensityArray = new int[ARRAY_SIZE];
		// array which stores the number of pixels corresponding to each
		// intensity

		for (int i = 0; i < copyData.getSize(); i++) {
			int intnsty;
			intnsty = copyData.getElem(0, i);
			intensityArray[intnsty] = intensityArray[intnsty] + 1;
		}

		int backgroundIntensity = 0;
		int count = 0;
		// calculates the intensity at which the number of pixels is maximum
		for (int i = 0; i < ARRAY_SIZE; i++) {
			if (intensityArray[i] > count) {
				count = intensityArray[i];
				backgroundIntensity = i;
			}
		}
		return backgroundIntensity;
	}

	/**
	 * Returns the Background intensity of the image.
	 * 
	 */
	public int getBackgroundIntensity(DataBuffer copyData,
			BufferedImage bufImage, int[] rectangleCoordinates) {
		int[] intensityArray = new int[ARRAY_SIZE];
		int width = bufImage.getWidth();
		// array which stores the number of pixels corresponding to each
		// intensity

		for (int i = 100 * width; i < (copyData.getSize() - (50 * width)); i++) {
			int y = i / bufImage.getWidth();
			int x = i % bufImage.getWidth();
			int count1 = 0;
			for (int j = 0; j < rectangleCoordinates.length; j = j + 4) {
				if (y >= rectangleCoordinates[j + 1]
						&& y <= rectangleCoordinates[j + 3]
						&& x >= rectangleCoordinates[j]
						&& x <= rectangleCoordinates[j + 2]) {
					count1++;
				}
			}
			if (count1 == 0) {
				int intnsty;
				intnsty = copyData.getElem(i);
				intensityArray[intnsty] = intensityArray[intnsty] + 1;
			}
		}

		int backgroundIntensity = 0;
		int count = 0;
		// calculates the intensity at which the number of pixels is maximum
		for (int i = 0; i < ARRAY_SIZE; i++) {
			if (intensityArray[i] > count) {
				count = intensityArray[i];
				backgroundIntensity = i;
			}
		}
		return backgroundIntensity;
	}

	/**
	 * This function enhances the text area.
	 * 
	 * @param object
	 *            : object of class TextIdentification
	 * @return enhanced image
	 * @throws ImageUtilityException
	 */

	public BufferedImage enhanceImage(HistogramHighLightedArea object)
			throws ImageUtilityException {

		String vc = null;
		// zoom the input image.
		object.rgbImage = ImageUtility.zoom(object.rgbImage, 2, 2);
		object.image = object.convertToGrayScale(object.image, object.rgbImage);
		object.highlightText(vc);
		// rescale the processed image to actual size
		object.image = ImageUtility.zoom(object.image, 0.5f, 0.5f);
		object.image = object.convertToGrayScale(object.image, object.image);
		return object.image;
	}

	/**
	 * Enhances the background image.
	 */
	public BufferedImage enhanceBackgroundImage(HistogramHighLightedArea object) {

		String vc = null;
		object.image = object.convertToGrayScale(object.image, object.rgbImage);
		object.highlightBackgroundText(vc);
		// rescale the processed image to actual size
		return object.image;
	}

	/**
	 * This function processes images with highlighted regions.
	 * 
	 * @param image
	 *            : Path of the image to be read
	 * @param coordinate1
	 *            starting x coordinate
	 * @param coordinate2
	 *            starting y coordinate
	 * @param coordinate3
	 *            ending x coordinate
	 * @param coordinate4
	 *            ending y coordinate
	 * @param rectangleCoordinates
	 *            : array which holds the coordinates of highlighted regions in
	 *            the image
	 * @throws ImageUtilityException
	 * @throws IOException
	 */

	public BufferedImage processHighlightedImage(BufferedImage inputimage,
			String target, int coordinate1, int coordinate2, int coordinate3,
			int coordinate4, int[] rectangleCoordinates)
			throws ImageUtilityException, IOException {

		logger.info("processHighlightedImage " + inputimage);
		// read the input image and store it in a data buffer
		HistogramHighLightedArea screenshot1 = new HistogramHighLightedArea(
				inputimage, coordinate1, coordinate2, coordinate3, coordinate4);
		screenshot1.rgbImage = screenshot1.convertToGrayScale(
				screenshot1.rgbImage, screenshot1.rgbImage);
		Raster originalImageRaster = screenshot1.rgbImage.getData();
		DataBuffer originalData = originalImageRaster.getDataBuffer();

		// create a copy of input image and store it to a data buffer
		HistogramHighLightedArea screenshot2 = new HistogramHighLightedArea(
				inputimage, coordinate1, coordinate2, coordinate3, coordinate4);
		screenshot2.rgbImage = screenshot2.convertToGrayScale(
				screenshot2.rgbImage, screenshot2.rgbImage);
		Raster copyImageRaster = screenshot2.rgbImage.getData();
		DataBuffer copyData = copyImageRaster.getDataBuffer();

		// update the original image data buffer
		for (int i = 0; i < originalData.getSize(); i++) {
			originalData.setElem(i, 255);
		}
		Raster updatedRaster = Raster.createRaster(
				originalImageRaster.getSampleModel(), originalData, null);
		screenshot1.rgbImage.setData(updatedRaster);

		int originalImageWidth = screenshot1.rgbImage.getWidth();
		// calculates the background intensity
		int subImageCoordinate1 = 0; // starting x coordinate of sub image
		int subImageCoordinate2 = 0; // starting y coordinate of sub image
		int subImageCoordinate3 = 0; // ending x coordinate of sub image
		int subImageCoordinate4 = 0; // ending y coordinate of sub image
		int backgroundIntensity = getBackgroundIntensity(copyData,
				screenshot1.rgbImage, rectangleCoordinates);

		for (int i = 0; i < rectangleCoordinates.length; i = i + 4) {
			subImageCoordinate1 = rectangleCoordinates[i];
			subImageCoordinate2 = rectangleCoordinates[i + 1];
			subImageCoordinate3 = rectangleCoordinates[i + 2];
			subImageCoordinate4 = rectangleCoordinates[i + 3];

			// enhance the sub image store it in a data buffer
			HistogramHighLightedArea screenshot = new HistogramHighLightedArea(
					inputimage, subImageCoordinate1 + coordinate1,
					subImageCoordinate2 + coordinate2, subImageCoordinate3
							+ coordinate1, subImageCoordinate4 + coordinate2);

			screenshot.image = enhanceImage(screenshot);
			Raster subImageRaster = screenshot.image.getData();
			DataBuffer subImageData = subImageRaster.getDataBuffer();

			originalData = screenshot.mergeHighLightedImage(subImageData,
					originalData, subImageCoordinate1, subImageCoordinate2,
					subImageCoordinate3, subImageCoordinate4,
					originalImageWidth);

			updatedRaster = Raster.createRaster(
					originalImageRaster.getSampleModel(), originalData, null);
			screenshot1.rgbImage.setData(updatedRaster);

			copyData = screenshot.updateOriginalImage(copyData,
					subImageCoordinate1, subImageCoordinate2,
					subImageCoordinate3, subImageCoordinate4,
					originalImageWidth, backgroundIntensity);
			Raster updatedRaster1 = Raster.createRaster(
					copyImageRaster.getSampleModel(), copyData, null);
			screenshot2.rgbImage.setData(updatedRaster1);
		}

		// enhance background image
		HistogramHighLightedArea screenshot3 = new HistogramHighLightedArea(
				screenshot2.rgbImage, 0, 0, (coordinate3 - coordinate1),
				(coordinate4 - coordinate2));
		screenshot3.image = enhanceBackgroundImage(screenshot3);
		Raster nonHighlightedImageRaster = screenshot3.image.getData();
		DataBuffer nonHighlightedImageData = nonHighlightedImageRaster
				.getDataBuffer();
		for (int j = 0; j < nonHighlightedImageData.getSize(); j++) {
			originalData.setElem(j, nonHighlightedImageData.getElem(j)
					& originalData.getElem(j));
		}
		updatedRaster = Raster.createRaster(
				originalImageRaster.getSampleModel(), originalData, null);
		screenshot1.rgbImage.setData(updatedRaster);

		//Remove the noise.
		originalData = removeNoise(screenshot1.rgbImage);
		updatedRaster = Raster.createRaster(
				originalImageRaster.getSampleModel(), originalData, null);
		screenshot1.rgbImage.setData(updatedRaster);
		if (target != null) {
			ImageUtility.saveImageAsPNG(screenshot1.rgbImage, target);
		} 

		return screenshot1.rgbImage;
	}

	/**
	 * Removes the noise.
	 * 
	 * @param img
	 *            - Buffered image.
	 */
	public DataBuffer removeNoise(BufferedImage img) {
		Raster noiseRaster = img.getData();
		DataBuffer noiseData = noiseRaster.getDataBuffer();
		int imageWidth = img.getWidth();
		for (int i = 0; i < (noiseData.getSize() - (30 * imageWidth)); i++) {
			int count = 0;
			if (noiseData.getElem(i) < 150) {

				for (int j = 0; j < 20; j++) {
					if (noiseData.getElem(i + j * imageWidth) < 150) {
						count++;
					}
				}
			}
			if (count >= 20) {
				for (int j = i; j < noiseData.getSize(); j = j + imageWidth) {
					if (noiseData.getElem(j) < 150) {
						noiseData.setElem(j, 255);
					} else {
						break;
					}
				}
			}
		}
		return noiseData;
	}

	/**
	 * This function calls methods to enhance the text area of visual cue
	 * images.
	 * 
	 * @param object
	 *            : Object of the class TextIdentification
	 * @return enhanced image
	 * @throws ImageUtilityException
	 */
	public BufferedImage enhanceVisualCueImage(HistogramHighLightedArea object)
			throws ImageUtilityException {

		object.rgbImage = ImageUtility.zoom(object.rgbImage, 2, 2);
		object.image = object.convertToGrayScale(object.image, object.rgbImage);
		DataBuffer textBuffer = object.highlightText("vc");
		object.processVisualCueImage("vc", textBuffer);
		object.image = ImageUtility.zoom(object.image, 0.5f, 0.5f);
		object.image = object.convertToGrayScale(object.image, object.image);

		return object.image;
	}

	/**
	 * This function processes images without highlighted regions
	 * 
	 * @param image
	 *            : Input image
	 * @param coordinate1
	 *            starting x coordinate
	 * @param coordinate2
	 *            starting y coordinate
	 * @param coordinate3
	 *            ending x coordinate
	 * @param coordinate4
	 *            ending y coordinate
	 * @param length
	 *            : number of arguments
	 * @param vc
	 *            : parameter which indicates whether the image is a normal one
	 *            or visual cue type
	 * @throws ImageUtilityException
	 * @throws IOException 
	 */

	public BufferedImage processImage(BufferedImage bImage, String target,
			int coordinate1, int coordinate2, int coordinate3, int coordinate4,
			boolean isVCType) throws ImageUtilityException, IOException {

		logger.info("processImage " + bImage);

		HistogramHighLightedArea screenshot = new HistogramHighLightedArea(
				bImage, coordinate1, coordinate2, coordinate3, coordinate4);
		if (!isVCType) {
			screenshot.image = enhanceImage(screenshot);
		} else {
			screenshot.image = enhanceVisualCueImage(screenshot);
		}
		if (target != null) {
			ImageUtility.saveImageAsPNG(screenshot.image, target);
		} 

		return screenshot.image;
	}

	/**
	 * Performs thresholding of highlighted area.
	 * 
	 * @param bufImage
	 *            source image
	 * @param target
	 *            target location for enhanced image.
	 * @param coordinate1
	 *            starting x coordinate
	 * @param coordinate2
	 *            starting y coordinate
	 * @param coordinate3
	 *            ending x coordinate
	 * @param coordinate4
	 *            ending y coordinate
	 * @param isVCType
	 *            parameter which indicates whether the image is a normal one or
	 *            visual cue type.
	 * @throws ImageUtilityException
	 * @throws IOException 
	 */
	public static void thresholdHighlightedArea(BufferedImage bufImage,
			String target, int coordinate1, int coordinate2, int coordinate3,
			int coordinate4, boolean isVCType) throws ImageUtilityException, IOException {

		int rectangleCoordinates[] = new int[100];
		HistogramHighLightedArea histogramHighLightedArea = new HistogramHighLightedArea(
				bufImage, coordinate1, coordinate2, coordinate3, coordinate4);

		// calculates the coordinates of highlighted regions in the image.
		logger.info("Starting text Identification Operation for" + bufImage.toString());

		RectangleIdentification rectangle = new RectangleIdentification(
				histogramHighLightedArea.rgbImage);
		rectangleCoordinates = rectangle.getRectangles();

		if (rectangleCoordinates.length == 0) {
			histogramHighLightedArea.rgbImage = histogramHighLightedArea
					.processImage(bufImage, target, coordinate1, coordinate2,
							coordinate3, coordinate4, isVCType);
		} else {
			histogramHighLightedArea.rgbImage = histogramHighLightedArea
					.processHighlightedImage(bufImage, target, coordinate1,
							coordinate2, coordinate3, coordinate4,
							rectangleCoordinates);
		}

	}

}
