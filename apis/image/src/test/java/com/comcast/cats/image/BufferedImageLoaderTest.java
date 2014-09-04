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

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * This class tests loading valid and invalid images, valid and invalid paths, and unexpected arguments.
 * 
 * @author mzmuda
 */
public class BufferedImageLoaderTest {
    /** This timeout should be 2-3 seconds. Changing because tests are timing out in VM **/
    private static final int TIME_OUT = 30000;
    
    private static final String TEXT_NOSLASH = "testFolder/Blue.txt";
    private static final String IMAGE_NOSLASH = "testFolder/Blue.jpg";
    private static final String IMAGE_SLASH = "/" + IMAGE_NOSLASH;
    private static final String INVALID_RESOURCE = "/thisDoesNotExits/none.test.ok";
    private static final String IMAGE_RELETIVE = "src/test/resources/testFolder/Blue.jpg";
    private static final String TEXT_RELETIVE = "src/test/resources/testFolder/Blue.txt";
    
    /**
     * Testing String constructor.
     */
    @Test(timeOut = TIME_OUT)
    public void testBufferedImageLoaderString1() {
        BufferedImageLoader bil = new BufferedImageLoader(TEXT_NOSLASH);
        Assert.assertEquals(bil.getPath(), TEXT_NOSLASH);
        Assert.assertNull(bil.getResourceClass());
    }
    
    /**
     * Testing String constructor.
     * Passing in null.
     */
    @Test(timeOut = TIME_OUT, expectedExceptions = java.lang.IllegalArgumentException.class)
    public void testBufferedImageLoaderString2() {
        new BufferedImageLoader(null);
    }
    
    /**
     * Testing String constructor.
     * Passing in empty string.
     */
    @Test(timeOut = TIME_OUT, expectedExceptions = java.lang.IllegalArgumentException.class)
    public void testBufferedImageLoaderString3() {
        new BufferedImageLoader("");
    }
    
    /**
     * Testing Class, String constructor.
     */
    @Test(timeOut = TIME_OUT)
    public void testBufferedImageLoaderClassString1() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), TEXT_NOSLASH);
        Assert.assertEquals(bil.getPath(), TEXT_NOSLASH);
        Assert.assertEquals(bil.getResourceClass(), this.getClass());
    }
    
    /**
     * Testing Class, String constructor.
     * Pass in null class.
     */
    @Test(timeOut = TIME_OUT)
    public void testBufferedImageLoaderClassString2() {
        BufferedImageLoader bil = new BufferedImageLoader(null, TEXT_NOSLASH);
        Assert.assertEquals(bil.getPath(), TEXT_NOSLASH);
        Assert.assertNull(bil.getResourceClass());
    }
    
    /**
     * Testing BufferedImageLoader.loadImageResource().
     * Passing in resource with no slash.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImageResource1() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), IMAGE_NOSLASH);
        Assert.assertNotNull(bil.loadImageResource(), "BufferedImage should not be null");
    }

    /**
     * Testing BufferedImageLoader.loadImageResource().
     * Passing in resource with slash.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImageResource2() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), IMAGE_SLASH);
        Assert.assertNotNull(bil.loadImageResource(), "BufferedImage should not be null");
    }

    /**
     * Testing BufferedImageLoader.loadImageResource().
     * Passing in null class.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImageResource3() {
        BufferedImageLoader bil = new BufferedImageLoader(null, IMAGE_SLASH);
        Assert.assertNull(bil.loadImageResource(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImageResource().
     * Passing in invalid resource path.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImageResource4() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), INVALID_RESOURCE);
        Assert.assertNull(bil.loadImageResource(), "BufferedImage should not null");
    }

    /**
     * Testing BufferedImageLoader.loadImageResource().
     * Passing in non image resource.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImageResource5() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), TEXT_NOSLASH);
        Assert.assertNull(bil.loadImageResource(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImageFromFile(File theFile).
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderString1")
    public void testLoadImageFromFile1() {
        File f = new File(IMAGE_RELETIVE);
        Assert.assertTrue(f.isFile(), "Required file does not exist: " + f.getAbsolutePath());
        BufferedImageLoader bil = new BufferedImageLoader(f.getAbsolutePath());
        Assert.assertNotNull(bil.loadImageFromFile(), "BufferedImage should not be null");
    }

    /**
     * Testing BufferedImageLoader.loadImageFromFile(File theFile).
     * Passing in invalid file.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderString1")
    public void testLoadImageFromFile2() {
        File f = new File(INVALID_RESOURCE);
        Assert.assertFalse(f.isFile(), "Invalid file actually exist: " + f.getAbsolutePath());
        BufferedImageLoader bil = new BufferedImageLoader(f.getAbsolutePath());
        Assert.assertNull(bil.loadImageFromFile(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImageFromFile(File theFile).
     * Trying to load non image file.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderString1")
    public void testLoadImageFromFile3() {
        File f = new File(TEXT_RELETIVE);
        Assert.assertTrue(f.isFile(), "Required file does not exist: " + f.getAbsolutePath());
        BufferedImageLoader bil = new BufferedImageLoader(f.getAbsolutePath());
        Assert.assertNull(bil.loadImageFromFile(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Load from file on disk.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderString1")
    public void testLoadImage1() {
        File f = new File(IMAGE_RELETIVE);
        Assert.assertTrue(f.isFile(), "Required file does not exist: " + f.getAbsolutePath());
        BufferedImageLoader bil = new BufferedImageLoader(f.getAbsolutePath());
        Assert.assertNotNull(bil.loadImage(), "BufferedImage should not be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Load non image file on disk.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderString1")
    public void testLoadImage2() {
        File f = new File(TEXT_RELETIVE);
        Assert.assertTrue(f.isFile(), "Required file does not exist: " + f.getAbsolutePath());
        BufferedImageLoader bil = new BufferedImageLoader(f.getAbsolutePath());
        Assert.assertNull(bil.loadImage(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Load from resource with slash.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImage3() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), IMAGE_SLASH);
        Assert.assertNotNull(bil.loadImage(), "BufferedImage should not be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Load from resource with no slash.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImage4() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), IMAGE_NOSLASH);
        Assert.assertNotNull(bil.loadImage(), "BufferedImage should not be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Load from resource with invalid image file.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImage5() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), TEXT_NOSLASH);
        Assert.assertNull(bil.loadImage(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Load from resource with invalid file.
     */
    @Test(timeOut = TIME_OUT, dependsOnMethods = "testBufferedImageLoaderClassString1")
    public void testLoadImage6() {
        BufferedImageLoader bil = new BufferedImageLoader(this.getClass(), INVALID_RESOURCE);
        Assert.assertNull(bil.loadImage(), "BufferedImage should be null");
    }

    /**
     * Testing BufferedImageLoader.loadImage()
     * Pass in null path.
     */
    @Test(timeOut = TIME_OUT, expectedExceptions = java.lang.IllegalArgumentException.class)
    public void testLoadImage7() {
        new BufferedImageLoader(this.getClass(), null);
    }
    
    /**
     * Testing BufferedImageLoader.loadImage()
     * Pass in empty path.
     */
    @Test(timeOut = TIME_OUT, expectedExceptions = java.lang.IllegalArgumentException.class)
    public void testLoadImage8() {
        new BufferedImageLoader(this.getClass(), "");
    }
}
