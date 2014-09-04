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
package com.comcast.cats.provider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.provider.impl.RegionLocatorProviderImpl;

public class RegionLocatorProviderImplTest {

	RegionLocatorProviderImpl implObject = null;
	BufferedImage refImage = null;
	RegionInfo regionInfo1 = null;
	RegionInfo regionInfo2 = null;
 	RegionInfo xfinityRegionInfo = null;
	
	private final static String filepathJPG = "src/test/resources/RefImageRLProviderTest.JPG";
	private final static String filepathXML = "src/test/resources/RefImageRLProviderTest.xml";
	private final static String region1 = "Monica-Face";
	private final static String region2 = "Chandler-Face";
	
    private final static String filepathGuideJPG = "src/test/resources/guide.jpg";
    private final static String filepathGuideXML = "src/test/resources/guide.xml";
    private final static String region_xfinity = "xfinity";
	
	ArrayList<RegionInfo> regionInfoList = null;
	
	@Before
	public void setUp() throws Exception {

		
		File file = new File(filepathJPG);
		try {
			refImage = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		regionInfo1 = new ImageCompareRegionInfo(region1,30,30,70,60);
		

		regionInfo2 = new ImageCompareRegionInfo(region2,180,85,50,50);
		
		regionInfoList = new ArrayList<RegionInfo>();
		regionInfoList.add(regionInfo1);
		regionInfoList.add(regionInfo2);
		
		xfinityRegionInfo = new ImageCompareRegionInfo(region_xfinity, 30,33,143, 67);
		
		implObject = new RegionLocatorProviderImpl();
	}

	@After
	public void tearDown() throws Exception {
		implObject = null;
		refImage = null;
		regionInfoList = null;
		regionInfo1 = null;
		regionInfo2 = null;
		
	}
	
	@Test
	public void saveImageAndRegionTest(){
		File file = new File(filepathJPG);
		try {
			implObject.saveImageAndRegion(regionInfoList,refImage,filepathJPG);
		} catch (IOException e) {
			Assert.fail();
		}
        catch ( JAXBException e )
        {
            Assert.fail();
        }
	}
	
	@Test
	public void saveImageAndRegionTestFail1(){
		boolean testPassed = false;
		File file = new File(filepathJPG);
		try {
			implObject.saveImageAndRegion(null,refImage,filepathJPG);
		} catch (IOException e) {
			Assert.fail();
		}catch(IllegalArgumentException e){
			testPassed = true;
		}catch ( JAXBException e )
        {
            Assert.fail();
        }
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void saveImageAndRegionTestFail2(){
		boolean testPassed = false;
		File file = new File(filepathJPG);
		try {
			implObject.saveImageAndRegion(regionInfoList,null,filepathJPG);
		} catch (IOException e) {
			Assert.fail();
		}catch(IllegalArgumentException e){
			testPassed = true;
		} catch ( JAXBException e )
        {
            Assert.fail();
        }
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void saveImageAndRegionTestFail3(){
		boolean testPassed = false;
		try {
			implObject.saveImageAndRegion(regionInfoList,refImage,null);
		} catch (IOException e) {
			Assert.fail();
		}catch(IllegalArgumentException e){
			testPassed = true;
		} catch ( JAXBException e )
        {
            Assert.fail();
        }
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void getRegionInfoTest(){
		saveImageAndRegionTest();
		File file = new File(filepathXML);
		try {
			ArrayList<RegionInfo> listRegionInfo =
				(ArrayList<RegionInfo>)implObject.getRegionInfo(filepathXML);
			Assert.assertEquals(2, listRegionInfo.size());
			if(!regionInfo1.equals(listRegionInfo.get(0))){
				if(!regionInfo1.equals(listRegionInfo.get(1))){
					Assert.fail();
				}
			}
			
			if(!regionInfo2.equals(listRegionInfo.get(0))){
				if(!regionInfo2.equals(listRegionInfo.get(1))){
					Assert.fail();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getRegionInfoTestFail(){
		boolean testPassed = false;
		saveImageAndRegionTest();
		try {
			ArrayList<RegionInfo> listRegionInfo =
				(ArrayList<RegionInfo>)implObject.getRegionInfo(null);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			testPassed = true;
		}
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void getRegionInfoTestFail2(){
		boolean testPassed = false;
		saveImageAndRegionTest();
		try {
			ArrayList<RegionInfo> listRegionInfo =
				(ArrayList<RegionInfo>)implObject.getRegionInfo("");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			testPassed = true;
		}
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void getSingleRegionInfoTest(){
		//saveImageAndRegionTest();
		File file = new File(filepathXML);
		try {
			RegionInfo regionInfo =
				implObject.getRegionInfo(filepathXML,region1);
			Assert.assertEquals(regionInfo1, regionInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getSingleRegionInfoTestFail(){
		boolean testPassed = false;
		saveImageAndRegionTest();
		try {
			RegionInfo regionInfo =
				implObject.getRegionInfo(null,region2);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			testPassed = true;
		}
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void getSingleRegionInfoTestFail1(){
		boolean testPassed = false;
		saveImageAndRegionTest();
		File file = new File(filepathXML);
		try {
			RegionInfo regionInfo =
				implObject.getRegionInfo(filepathXML,null);
		
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			testPassed = true;
		}
		Assert.assertTrue(testPassed);
	}
	
	@Test
	public void saveImageRegionTest()
	{
	    try
        {
            String xmlFilePath = implObject.saveImageRegion( xfinityRegionInfo, filepathGuideJPG );
            Assert.assertEquals( filepathGuideXML, xmlFilePath );
            RegionInfo region = implObject.getRegionInfo( xmlFilePath, region_xfinity );
            Assert.assertNotNull( region );
        }
        catch ( IOException e )
        {           
           Assert.fail( e.getMessage() );
        }
        catch ( JAXBException e )
        {
            Assert.fail( e.getMessage() );
        }
	}
	
	@Test
    public void saveImageRegionTestFail()
    {
	    boolean testResult = false;
        try
        {
            implObject.saveImageRegion( null, filepathGuideJPG );
        }
        catch ( IOException e )
        {           
           Assert.fail( e.getMessage() );
        }
        catch (IllegalArgumentException e) 
        {
            testResult = true;
        }
        catch ( JAXBException e )
        {
            Assert.fail( e.getMessage() );
        }
        Assert.assertTrue( testResult );
    }
}
