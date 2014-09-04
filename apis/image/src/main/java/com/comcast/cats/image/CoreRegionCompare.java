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
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the core function(s) that are used for image region comparison.
 * Because this is a utility class, an instance of it cannot be created.
 */
public class CoreRegionCompare {
    // TODO: At some point this class should be generalised to handle ImageCompareRegionInfo and OCRRegionInfo.
    
    private CoreRegionCompare() { }
    
    /**
     * Direction of the rectangular spiral algorithm.
     */
    static enum DIRECTION {
        RIGHT,
        DOWN,
        LEFT,
        UP
    }

    /**
     * The rectangle shape.
     */
    static enum SHAPE {
        SQUARE,
        VRECT,
        HRECT
    }
    
    public static ImageCompareResult findRegionOnTargetRegion( ImageCompareRegionInfo smallRegion,
            RegionInfo largeRegion, BufferedImage precapturedImage, BufferedImage liveImage ) throws IOException
    {
        BufferedImage smallImage = precapturedImage.getSubimage( smallRegion.getX(), smallRegion.getY(),
                smallRegion.getWidth(), smallRegion.getHeight() );

        return CoreImageCompare.findImageOnTargetRegion( smallImage, liveImage, largeRegion.getX(), largeRegion.getY(),
                largeRegion.getWidth(), largeRegion.getHeight(), smallRegion.getMatchPct(), smallRegion.getRedTolerance(),
                smallRegion.getGreenTolerance(), smallRegion.getBlueTolerance() );        
    }

    /**
     * Helper function. Puts info into a ArrrayList and calls doRegionCompare(List< RegionInfo > infoList, BufferedImage snapshot, BufferedImage liveImage).
     *  
     * @param info The region info.
     * @param precapturedImage The precaptured image.
     * @param liveImage The live image.
     * @return true if the region is found in the live image.
     * @throws IllegalArgumentException if images are not the same size.
     */
    public static boolean doRegionCompare(ImageCompareRegionInfo info, BufferedImage precapturedImage, BufferedImage liveImage) {
        List<ImageCompareRegionInfo> infoList = new ArrayList<ImageCompareRegionInfo>(1);
        infoList.add(info);
        if (doRegionCompare(infoList, precapturedImage, liveImage)) {
            return true;
        }
        return false;
    }

    /**
     * Checks the liveImage for each region found in the infoList based on the precapturedImage.
     * Uses a rectangular spiral starting at the expected origin of the image and spiraling clockwise
     * in a square/rectangle motion until the image is found or each pixel has been checked within the
     * regions x + y tolerance.
     * 
     * @param infoList The list of regions.
     * @param precapturedImage The precaptured image.
     * @param liveImage The live image (or image to compare against).
     * @return <b>true</b> if all the regions are found in the live image.
     * @throws IllegalArgumentException if images are not the same size.
     * @throws NullPointerException if precapturedImage or liveImage is null.
     */
    public static boolean doRegionCompare(List<ImageCompareRegionInfo> infoList, BufferedImage precapturedImage, BufferedImage liveImage) {        
        boolean found = false;
                
        int w1 = precapturedImage.getWidth(null);
        int h1 = precapturedImage.getHeight(null);        
        int w2 = liveImage.getWidth(null);
        int h2 = liveImage.getHeight(null);

        if ((w1 != w2) || (h1 != h2)) {
            throw new IllegalArgumentException("precapturedImage and liveImage must be the same size");
        }
        
        if (infoList != null && !infoList.isEmpty()) {
            BufferedImage expectedImage = null;
            BufferedImage currentImage = null;

            for (ImageCompareRegionInfo info : infoList) {                
                try {
                    expectedImage = precapturedImage.getSubimage(info.getX(), info.getY(), info.getWidth(), info.getHeight());
                } catch (RasterFormatException rfe) {
                    return false;
                }
                found = false;
                int minX = info.getX() - info.getXTolerance();
                int maxX = info.getX() + info.getXTolerance();
                int curX = info.getX();

                int minY = info.getY() - info.getYTolerance();
                int maxY = info.getY() + info.getYTolerance();
                int curY = info.getY();

                // First lets see if its in the original position.
                // This can never throw a raster exception because the images are the precapturedImage
                // and liveImage are same size. We got the expectedImage just fine so we can get current image too.
                currentImage = liveImage.getSubimage(curX, curY, info.getWidth(), info.getHeight());
                if (CoreImageCompare.compareImages(currentImage, expectedImage, info.getMatchPct(), info.getRedTolerance(),
                    info.getGreenTolerance(), info.getBlueTolerance())) {
                    found = true;
                    continue;
                }
                
                DIRECTION direction = DIRECTION.RIGHT;
                int distance = 1;
                
                SHAPE recType;
                if (info.getYTolerance() == info.getXTolerance()) {
                    recType = SHAPE.SQUARE;
                } else if (info.getYTolerance() > info.getXTolerance()) {
                    recType = SHAPE.VRECT;
                } else {
                    recType = SHAPE.HRECT;
                }
                
                boolean keepSearching = true;
                while (keepSearching) {
                    switch (direction) {
                        case RIGHT:
                            for (int i = 0; i != distance; ++i) {
                                ++curX;
                                if (curX <= maxX) {                                    
                                    try {
                                        currentImage = liveImage.getSubimage(curX, curY, info.getWidth(), info.getHeight());
                                        if (CoreImageCompare.compareImages(currentImage, expectedImage, info.getMatchPct(), info.getRedTolerance(),
                                            info.getGreenTolerance(), info.getBlueTolerance())) {
                                            found = true;
                                            keepSearching = false;
                                            break;
                                        }
                                    } catch (RasterFormatException rfe) {
                                        rfe = null;
                                    }
                                } else {
                                    if (recType == SHAPE.SQUARE) {
                                        // Square always ends in this direction
                                        keepSearching = false;
                                        break;
                                    } else if (recType == SHAPE.VRECT) {
                                        // Either a jump down or finish.
                                        if (curY > minY) {
                                            curY += distance++;                                    
                                            direction = DIRECTION.LEFT;
                                            break;
                                        } else {
                                            // No match found.
                                            found = false;
                                            keepSearching = false;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (direction == DIRECTION.RIGHT) {
                                direction = DIRECTION.DOWN;
                                // Is this the last time we will be going
                                // Right then down?
                                if (recType == SHAPE.HRECT && (curX == maxX || curY == minY)) {
                                    ++distance;
                                }
                            }
                            break;
                        case DOWN:
                            for (int i = 0; i != distance; ++i) {
                                ++curY;
                                if (curY <= maxY) {
                                    try {
                                        currentImage = liveImage.getSubimage(curX, curY, info.getWidth(), info.getHeight());
                                        if (CoreImageCompare.compareImages(currentImage, expectedImage, info.getMatchPct(), info.getRedTolerance(),
                                            info.getGreenTolerance(), info.getBlueTolerance())) {
                                            found = true;
                                            keepSearching = false;
                                            break;
                                        }
                                    } catch (RasterFormatException rfe) {
                                        rfe = null;
                                    }
                                } else {
                                    // Can never end going down
                                    // so jump up
                                    if (recType == SHAPE.HRECT) {
                                        curX -= distance++;
                                        direction = DIRECTION.UP;
                                        break;
                                    }
                                }
                            }
                            if (direction == DIRECTION.DOWN) {
                                ++distance;
                                direction = DIRECTION.LEFT;
                            }
                            break;
                        case LEFT:
                            for (int i = 0; i != distance; ++i) {
                                --curX;
                                if (curX >= minX) {
                                    try {
                                        currentImage = liveImage.getSubimage(curX, curY, info.getWidth(), info.getHeight());
                                        if (CoreImageCompare.compareImages(currentImage, expectedImage, info.getMatchPct(), info.getRedTolerance(),
                                            info.getGreenTolerance(), info.getBlueTolerance())) {
                                            found = true;
                                            keepSearching = false;
                                            break;
                                        }
                                    } catch (RasterFormatException rfe) {
                                        rfe = null;
                                    }
                                } else {
                                    // Can never end going left
                                    // so jump up
                                    if (recType == SHAPE.VRECT) {
                                        curY -= distance++;
                                        direction = DIRECTION.RIGHT;                                
                                        break;
                                    }
                                }
                            }
                            if (direction == DIRECTION.LEFT) {
                                direction = DIRECTION.UP;
                            }
                            break;
                        case UP:
                            for (int i = 0; i != distance; ++i) {
                                --curY;
                                if (curY >= minY) {
                                    try {
                                        currentImage = liveImage.getSubimage(curX, curY, info.getWidth(), info.getHeight());
                                        if (CoreImageCompare.compareImages(currentImage, expectedImage, info.getMatchPct(), info.getRedTolerance(),
                                            info.getGreenTolerance(), info.getBlueTolerance())) {
                                            found = true;
                                            keepSearching = false;
                                            break;
                                        }
                                    } catch (RasterFormatException rfe) {
                                        rfe = null;
                                    }
                                } else {
                                    // Either a jump down or finish.
                                    if (recType == SHAPE.HRECT) {
                                        if (curX > minX) {
                                            curX += distance++;                                    
                                            direction = DIRECTION.DOWN;
                                            break;
                                        } else {
                                            keepSearching = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (direction == DIRECTION.UP) {
                                ++distance;
                                direction = DIRECTION.RIGHT;
                            }
                            break;
                        default:
                            break;
                    }
                }
                
                // The image was not found, no point in checking any of the other
                // regions.
                if (!found) {
                    break;
                }
            }
        }
        return found;
    }
}
