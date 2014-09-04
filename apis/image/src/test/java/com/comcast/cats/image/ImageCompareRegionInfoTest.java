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

import com.comcast.cats.image.ImageCompareRegionInfo;

/**
 * This is not a functional class, this class focuses on testing the equals method, setters and getters. 
 */
public class ImageCompareRegionInfoTest extends CommonRegionInfoTest {    
    private static final float MATCH = 80.0f;
    private static final int REDTOL = 55;
    private static final int GREENTOL = 65;
    private static final int BLUETOL = 75;
    
    /**
     * Tests no argument constructor.
     * Validates default values.
     */
    @Test(timeOut = TIME_OUT)
    public final void testRegionInfo() {
        ImageCompareRegionInfo ri = new ImageCompareRegionInfo();
        Assert.assertEquals(ri.getName(), DEFAULT_NAME);
        Assert.assertTrue(ri.getX() == DEFAULT_INT, "X has wrong value");
        Assert.assertTrue(ri.getY() == DEFAULT_INT, "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == DEFAULT_INT, "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == DEFAULT_INT, "Height has wrong value");
        
        // Now check the default values.
        Assert.assertTrue(ri.getBlueTolerance() == ImageCompareRegionInfo.DEFAULT_BLUE_TOLERANCE, "Blue Tolerance has wrong value");
        Assert.assertTrue(ri.getRedTolerance() == ImageCompareRegionInfo.DEFAULT_RED_TOLERANCE, "Red Tolerance has wrong value");
        Assert.assertTrue(ri.getGreenTolerance() == ImageCompareRegionInfo.DEFAULT_GREEN_TOLERANCE, "Green Tolerance has wrong value");
        Assert.assertTrue(ri.getXTolerance() == ImageCompareRegionInfo.DEFAULT_X_TOLERANCE, "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == ImageCompareRegionInfo.DEFAULT_Y_TOLERANCE, "Y Tolerance has wrong value");
        Assert.assertTrue(ri.getMatchPct() == ImageCompareRegionInfo.DEFAULT_MATCH_PERCENT, "Match Percent has wrong value");
    }
    
    /**
     * Tests constructor taking required parameters.
     * Validates defaults.
     */
    @Test(timeOut = TIME_OUT)
    public final void testRegionInfoRequired() {
        ImageCompareRegionInfo ri = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        
        Assert.assertEquals(ri.getName(), NAME);
        Assert.assertTrue(ri.getX() == X, "X has wrong value");
        Assert.assertTrue(ri.getY() == Y, "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == WIDTH, "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == HEIGHT, "Height has wrong value");
        
        // Now check the default values.
        Assert.assertTrue(ri.getBlueTolerance() == ImageCompareRegionInfo.DEFAULT_BLUE_TOLERANCE, "Blue Tolerance has wrong value");
        Assert.assertTrue(ri.getRedTolerance() == ImageCompareRegionInfo.DEFAULT_RED_TOLERANCE, "Red Tolerance has wrong value");
        Assert.assertTrue(ri.getGreenTolerance() == ImageCompareRegionInfo.DEFAULT_GREEN_TOLERANCE, "Green Tolerance has wrong value");
        Assert.assertTrue(ri.getXTolerance() == ImageCompareRegionInfo.DEFAULT_X_TOLERANCE, "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == ImageCompareRegionInfo.DEFAULT_Y_TOLERANCE, "Y Tolerance has wrong value");
        Assert.assertTrue(ri.getMatchPct() == ImageCompareRegionInfo.DEFAULT_MATCH_PERCENT, "Match Percent has wrong value");
    }
    
    /**
     * Tests getCopy.
     * @deprecated Use clone().
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    @Deprecated
    public final void testGetCopy() {
        ImageCompareRegionInfo ri = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        
        ri.setBlueTolerance(BLUETOL);
        ri.setRedTolerance(REDTOL);
        ri.setGreenTolerance(GREENTOL);
        ri.setMatchPct(MATCH);
        ri.setXTolerance(XTOL);
        ri.setYTolerance(YTOL);
        
        ImageCompareRegionInfo riCopy = ri.getCopy();
        
        Assert.assertFalse(riCopy == ri, "Object instances should be different.");
        Assert.assertEquals(ri.getName(), riCopy.getName());
        Assert.assertTrue(ri.getX() == riCopy.getX(), "X has wrong value");
        Assert.assertTrue(ri.getY() == riCopy.getY(), "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == riCopy.getWidth(), "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == riCopy.getHeight(), "Height has wrong value");        
        Assert.assertTrue(ri.getBlueTolerance() == riCopy.getBlueTolerance(), "Blue Tolerance has wrong value");
        Assert.assertTrue(ri.getRedTolerance() == riCopy.getRedTolerance(), "Red Tolerance has wrong value");
        Assert.assertTrue(ri.getGreenTolerance() == riCopy.getGreenTolerance(), "Green Tolerance has wrong value");
        Assert.assertTrue(ri.getXTolerance() == riCopy.getXTolerance(), "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == riCopy.getYTolerance(), "Y Tolerance has wrong value");
        Assert.assertTrue(ri.getMatchPct() == riCopy.getMatchPct(), "Match Percent has wrong value");
    }
    
    /**
     * Tests clone().
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public final void testClone() {
        ImageCompareRegionInfo ri = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        
        ri.setBlueTolerance(BLUETOL);
        ri.setRedTolerance(REDTOL);
        ri.setGreenTolerance(GREENTOL);
        ri.setMatchPct(MATCH);
        ri.setXTolerance(XTOL);
        ri.setYTolerance(YTOL);
        
        ImageCompareRegionInfo riCopy = (ImageCompareRegionInfo) ri.clone();
        
        Assert.assertFalse(riCopy == ri, "Object instances should be different.");
        Assert.assertEquals(ri.getName(), riCopy.getName());
        Assert.assertTrue(ri.getX() == riCopy.getX(), "X has wrong value");
        Assert.assertTrue(ri.getY() == riCopy.getY(), "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == riCopy.getWidth(), "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == riCopy.getHeight(), "Height has wrong value");        
        Assert.assertTrue(ri.getBlueTolerance() == riCopy.getBlueTolerance(), "Blue Tolerance has wrong value");
        Assert.assertTrue(ri.getRedTolerance() == riCopy.getRedTolerance(), "Red Tolerance has wrong value");
        Assert.assertTrue(ri.getGreenTolerance() == riCopy.getGreenTolerance(), "Green Tolerance has wrong value");
        Assert.assertTrue(ri.getXTolerance() == riCopy.getXTolerance(), "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == riCopy.getYTolerance(), "Y Tolerance has wrong value");
        Assert.assertTrue(ri.getMatchPct() == riCopy.getMatchPct(), "Match Percent has wrong value");
    }
    
    /**
     * Tests equals.
     * Reflective test - for any reference value x, x.equals(x) should return true.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsReflective() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(x), "Equals does not meet reflective part of contract");
    }

    /**
     * Tests equals.
     * Symmetric test - for any reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsSymmetric() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        ImageCompareRegionInfo y = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(y) && y.equals(x), "Equals does not meet symmetric part of contract");
    }

    /**
     * Tests equals.
     * Transitive test - for any reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true,
     * then x.equals(z) should return true.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsTransitive() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        ImageCompareRegionInfo y = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        ImageCompareRegionInfo z = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z), "Equals does not meet transitive part of contract");
    }

    /**
     * Tests equals.
     * Consistent: for any reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently
     * return false, provided no information used in equals comparisons on the object is modified.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsConsistent() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        ImageCompareRegionInfo y = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(y) && x.equals(y) && x.equals(y) && x.equals(y),
            "Equals does not meet consitent part of contract");
        x.setMatchPct(MATCH);
        Assert.assertTrue(!x.equals(y) && !x.equals(y) && !x.equals(y) && !x.equals(y),
            "Equals does not meet consitent part of contract");
    }
    
    /**
     * Tests equals.
     * Test against null.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEquals1() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertFalse(x.equals(null), "Should not be equal to null");
    }

    /**
     * Tests equals.
     * Test against non ImageCompareRegionInfo instance.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEquals2() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertFalse(x.equals(new File("")), "Should not be equal to File object");
    }
    
    /**
     * Tests toString().
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testToString() {
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);        
        Assert.assertNotNull(x.toString(), "toString() should return a string representation of this class.");
    }    
    
    /**
     * Tests hashCode.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testHashCode() {        
        ImageCompareRegionInfo x = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        ImageCompareRegionInfo y = (ImageCompareRegionInfo) x.clone();
        Assert.assertTrue(x.equals(y), "Objects are not equal");
        Assert.assertTrue(x.hashCode() == y.hashCode(), "HashCodes are not equal");
    }
}
