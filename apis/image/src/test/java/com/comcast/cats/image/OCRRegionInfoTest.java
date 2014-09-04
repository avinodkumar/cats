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

import com.comcast.cats.image.OCRRegionInfo;

/**
 * Testing.
 * 
 * This is not a functional class, this class focuses on testing the equals method, setters and getters. 
 */
public class OCRRegionInfoTest extends CommonRegionInfoTest {
    
    private static final String EXPECTED_RESULT = "this is expected";
    private static final int THE_TIMEOUT = 22;
    private static final int SUCCESS_TOLERANCE = 33;
    private static final String URL = "www.google.com";
    
    /**
     * Tests no argument constructor.
     * Validates default values.
     */
    @Test(timeOut = TIME_OUT)
    public final void testRegionInfo() {
        OCRRegionInfo ri = new OCRRegionInfo();
        Assert.assertEquals(ri.getName(), DEFAULT_NAME);
        Assert.assertTrue(ri.getX() == DEFAULT_INT, "X has wrong value");
        Assert.assertTrue(ri.getY() == DEFAULT_INT, "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == DEFAULT_INT, "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == DEFAULT_INT, "Height has wrong value");
        
        // Now check the default values.
        Assert.assertTrue(ri.getXTolerance() == OCRRegionInfo.DEFAULT_X_TOLERANCE, "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == OCRRegionInfo.DEFAULT_Y_TOLERANCE, "Y Tolerance has wrong value");        
        Assert.assertTrue(ri.getExpectedResult() == OCRRegionInfo.DEFAULT_EXPECTED_RESULT, "ExpectedResult has wrong value");
        Assert.assertTrue(ri.getSuccessTolerance() == OCRRegionInfo.DEFAULT_SUCCESS_TOLERANCE, "Success Tolerance has wrong value");
        Assert.assertTrue(ri.getTimeout() == OCRRegionInfo.DEFAULT_TIMEOUT, "Timeout has wrong value");
        Assert.assertTrue(ri.getUrl() == OCRRegionInfo.DEFAULT_URL, "URL has wrong value");
    }
    
    /**
     * Tests constructor taking required parameters.
     * Validates defaults.
     */
    @Test(timeOut = TIME_OUT)
    public final void testRegionInfoRequired() {
        OCRRegionInfo ri = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        
        Assert.assertEquals(ri.getName(), NAME);
        Assert.assertTrue(ri.getX() == X, "X has wrong value");
        Assert.assertTrue(ri.getY() == Y, "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == WIDTH, "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == HEIGHT, "Height has wrong value");
        
        // Now check the default values.
        Assert.assertTrue(ri.getXTolerance() == OCRRegionInfo.DEFAULT_X_TOLERANCE, "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == OCRRegionInfo.DEFAULT_Y_TOLERANCE, "Y Tolerance has wrong value");        
        Assert.assertTrue(ri.getExpectedResult() == OCRRegionInfo.DEFAULT_EXPECTED_RESULT, "ExpectedResult has wrong value");
        Assert.assertTrue(ri.getSuccessTolerance() == OCRRegionInfo.DEFAULT_SUCCESS_TOLERANCE, "Success Tolerance has wrong value");
        Assert.assertTrue(ri.getTimeout() == OCRRegionInfo.DEFAULT_TIMEOUT, "Timeout has wrong value");
        Assert.assertTrue(ri.getUrl() == OCRRegionInfo.DEFAULT_URL, "URL has wrong value");
    }
    
    /**
     * Tests getCopy.
     * @deprecated Use clone().
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    @Deprecated
    public final void testGetCopy() {
        OCRRegionInfo ri = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        
        ri.setExpectedResult(EXPECTED_RESULT);
        ri.setTimeout(THE_TIMEOUT);
        ri.setSuccessTolerance(SUCCESS_TOLERANCE);
        ri.setXTolerance(XTOL);
        ri.setYTolerance(YTOL);
        ri.setUrl(URL);
        
        OCRRegionInfo riCopy = ri.getCopy();
        
        Assert.assertFalse(riCopy == ri, "Object instances should be different.");
        Assert.assertEquals(ri.getName(), riCopy.getName());
        Assert.assertTrue(ri.getX() == riCopy.getX(), "X has wrong value");
        Assert.assertTrue(ri.getY() == riCopy.getY(), "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == riCopy.getWidth(), "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == riCopy.getHeight(), "Height has wrong value");                       
        Assert.assertTrue(ri.getXTolerance() == riCopy.getXTolerance(), "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == riCopy.getYTolerance(), "Y Tolerance has wrong value");
        Assert.assertEquals(ri.getExpectedResult(), riCopy.getExpectedResult(), "Expected Result has wrong value");
        Assert.assertEquals(ri.getUrl(), riCopy.getUrl(), "URL has wrong value");
        Assert.assertTrue(ri.getTimeout() == riCopy.getTimeout(), "Timeout has wrong value");
        Assert.assertTrue(ri.getSuccessTolerance() == riCopy.getSuccessTolerance(), "Success tolerance has wrong value");
    }
    
    /**
     * Tests clone().
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public final void testClone() {
        OCRRegionInfo ri = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        
        ri.setExpectedResult(EXPECTED_RESULT);
        ri.setTimeout(THE_TIMEOUT);
        ri.setSuccessTolerance(SUCCESS_TOLERANCE);
        ri.setXTolerance(XTOL);
        ri.setYTolerance(YTOL);
        
        OCRRegionInfo riCopy = (OCRRegionInfo) ri.clone();
        
        Assert.assertFalse(riCopy == ri, "Object instances should be different.");
        Assert.assertEquals(ri.getName(), riCopy.getName());
        Assert.assertTrue(ri.getX() == riCopy.getX(), "X has wrong value");
        Assert.assertTrue(ri.getY() == riCopy.getY(), "Y has wrong value");
        Assert.assertTrue(ri.getWidth() == riCopy.getWidth(), "Width has wrong value");
        Assert.assertTrue(ri.getHeight() == riCopy.getHeight(), "Height has wrong value");                       
        Assert.assertTrue(ri.getXTolerance() == riCopy.getXTolerance(), "X Tolerance has wrong value");
        Assert.assertTrue(ri.getYTolerance() == riCopy.getYTolerance(), "Y Tolerance has wrong value");
        Assert.assertEquals(ri.getExpectedResult(), riCopy.getExpectedResult(), "Expected Result has wrong value");
        Assert.assertEquals(ri.getUrl(), riCopy.getUrl(), "URL has wrong value");
        Assert.assertTrue(ri.getTimeout() == riCopy.getTimeout(), "Timeout has wrong value");
        Assert.assertTrue(ri.getSuccessTolerance() == riCopy.getSuccessTolerance(), "Success tolerance has wrong value");
    }
    
    /**
     * Tests equals.
     * Reflective test - for any reference value x, x.equals(x) should return true.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsReflective() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(x), "Equals does not meet reflective part of contract");
    }

    /**
     * Tests equals.
     * Symmetric test - for any reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsSymmetric() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        OCRRegionInfo y = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(y) && y.equals(x), "Equals does not meet symmetric part of contract");
    }

    /**
     * Tests equals.
     * Transitive test - for any reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true,
     * then x.equals(z) should return true.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsTransitive() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        OCRRegionInfo y = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        OCRRegionInfo z = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z), "Equals does not meet transitive part of contract");
    }

    /**
     * Tests equals.
     * Consistent: for any reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently
     * return false, provided no information used in equals comparisons on the object is modified.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEqualsConsistent() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        OCRRegionInfo y = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertTrue(x.equals(y) && x.equals(y) && x.equals(y) && x.equals(y),
            "Equals does not meet consitent part of contract");
        x.setExpectedResult(EXPECTED_RESULT);
        Assert.assertTrue(!x.equals(y) && !x.equals(y) && !x.equals(y) && !x.equals(y),
            "Equals does not meet consitent part of contract");
    }
    
    /**
     * Tests equals.
     * Test against null.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEquals1() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertFalse(x.equals(null), "Should not be equal to null");
    }

    /**
     * Tests equals.
     * Test against non OCRRegionInfo instance.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testEquals2() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        Assert.assertFalse(x.equals(new File("")), "Should not be equal to File object");
    }
    
    /**
     * Tests toString().
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testToString() {
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);        
        Assert.assertNotNull(x.toString(), "toString() should return a string representation of this class.");
    }    
    
    /**
     * Tests hashCode.
     */
    @Test(dependsOnMethods = "testRegionInfoRequired", timeOut = TIME_OUT)
    public void testHashCode() {        
        OCRRegionInfo x = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        OCRRegionInfo y = (OCRRegionInfo) x.clone();
        Assert.assertTrue(x.equals(y), "Objects are not equal");
        Assert.assertTrue(x.hashCode() == y.hashCode(), "HashCodes are not equal");
    }
}