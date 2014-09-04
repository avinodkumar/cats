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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Properties;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.provider.exceptions.ImageCompareException;
import com.comcast.cats.provider.factory.impl.ImageCompareProviderFactoryImpl;
import com.comcast.cats.provider.impl.ImageCompareProviderImpl;
import com.comcast.cats.provider.impl.RegionLocatorProviderImpl;


public class ImageCompareProviderImplTest {
	
	ImageCompareProviderImpl icProvider = null;
	VideoProvider videoProvider;
	BufferedImage refImage = null;
	BufferedImage currentImage = null;
	BufferedImage nonMatchingCurrentImage = null;
	RegionInfo regionInfo1 = null;
	RegionInfo regionInfo2 = null;
	ArrayList<RegionInfo> regionInfoList = null;
	Dimension expectedReferenceDimension;
	RegionLocatorProviderImpl regionLocatorImpl;
	
	private final static String filepathCurrentJPG = "src/test/resources/currentImage.JPG";
	private final static String filepathRefJPG = "src/test/resources/RefImage.JPG";
	private final static String filepathNonMatchingJPG = "src/test/resources/NonMatchingCurrentImage.JPG";
	private final static String filepathRefXML = "src/test/resources/RefImage.xml";
	private final static String region1 = "Monica-Face";
	private final static String region2 = "Chandler-Face";
	
	private final static String filepathGuideJPG = "src/test/resources/guide.jpg";
	private final static String filepathGuideXML = "src/test/resources/guide.xml";
	private final static String region_xfinity = "xfinity";
	
	@Before
	public void setUp() throws Exception {
		
		File file1 = new File(filepathRefJPG);
		try {
			refImage = ImageIO.read(file1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		expectedReferenceDimension = new Dimension(refImage.getWidth(), refImage.getHeight());
		
		File file2 = new File(filepathCurrentJPG);
		try {
			currentImage = ImageIO.read(file2);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file3 = new File(filepathNonMatchingJPG);
		try {
			nonMatchingCurrentImage = ImageIO.read(file3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		regionInfo1 = new ImageCompareRegionInfo(region1,30,30,70,60);
		regionInfo2 = new ImageCompareRegionInfo(region2,180,85,50,50);
		
		regionInfoList = new ArrayList<RegionInfo>();
		regionInfoList.add(regionInfo1);
		regionInfoList.add(regionInfo2);
		
		regionLocatorImpl = new RegionLocatorProviderImpl();

		
		videoProvider = EasyMock.createMock( VideoProvider.class );
		Properties props = new Properties();
		FileInputStream in = new FileInputStream("src/test/resources/test.properties");
		props.load(in);
		icProvider = ( ImageCompareProviderImpl ) ImageCompareProviderFactoryImpl.getProvider( videoProvider );
		icProvider = new ImageCompareProviderImpl(getClass(), videoProvider, 10, 10,props );
	}

	@After
	public void tearDown() throws Exception {
		icProvider = null;
		videoProvider = null;
		refImage = null;
		currentImage = null;
		nonMatchingCurrentImage = null;
		regionInfo1 = null;
		regionInfo2 = null;
		regionInfoList = null;
		regionLocatorImpl = null;
	}

	@Test
	public void testIsImageOnScreenNow() {
		  EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		  EasyMock.replay( videoProvider );
		  try
        {
            Assert.assertTrue(icProvider.isImageOnScreenNow(refImage));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}

	@Test
	public void testIsImageOnScreenNowFail() {
		
		  EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		  EasyMock.replay( videoProvider );
		  try
        {
            Assert.assertFalse(icProvider.isImageOnScreenNow(refImage));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}

	@Test
	public void testIsImageOnScreenNowNullTest() {
		  EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		  EasyMock.replay( videoProvider );
		  try
        {
            Assert.assertFalse(icProvider.isImageOnScreenNow(null));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testIsSameImageOnScreenNow() {
		  EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		  EasyMock.replay( videoProvider );
		  Assert.assertTrue(icProvider.isSameImageOnScreenNow(filepathRefJPG));
	}

	@Test
	public void testIsSameImageOnScreenNowFail() {
		
		  EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		  EasyMock.replay( videoProvider );
		  Assert.assertFalse(icProvider.isSameImageOnScreenNow(filepathRefJPG));
	}

	@Test
	public void testIsSameImageOnScreenNowNullTest() {
		
		  EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		  EasyMock.replay( videoProvider );
		  Assert.assertFalse(icProvider.isSameImageOnScreenNow(null));
	}
	

	@Test
	public void testIsRegionOnScreenNowImageCompareRegionInfo() {
  
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		  EasyMock.replay( videoProvider );
				RegionInfo regionInfo =null;
				try {
					regionInfo = regionLocatorImpl.getRegionInfo(filepathRefXML,region2);
				} catch (IOException e) {
					e.printStackTrace();
				}
		  try
        {
            Assert.assertTrue(icProvider.isRegionOnScreenNow((ImageCompareRegionInfo)regionInfo));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}

	@Test
	public void testIsRegionOnScreenNowImageCompareRegionInfoFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		  EasyMock.replay( videoProvider );

				RegionInfo regionInfo =null;
				try {
					regionInfo = regionLocatorImpl.getRegionInfo(filepathRefXML,region2);
				} catch (IOException e) {
					e.printStackTrace();
				}
		  try
        {
            Assert.assertFalse(icProvider.isRegionOnScreenNow((ImageCompareRegionInfo)regionInfo));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testIsRegionOnScreenNowImageCompareRegionInfoNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		  EasyMock.replay( videoProvider );

				RegionInfo regionInfo =null;
				try {
					regionInfo = regionLocatorImpl.getRegionInfo(filepathRefXML,region2);
				} catch (IOException e) {
					e.printStackTrace();
				}
		  try
        {
            Assert.assertFalse(icProvider.isRegionOnScreenNow(null));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}

	@Test
	public void testIsRegionOnScreenNowString() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		EasyMock.replay( videoProvider );

		Assert.assertTrue(icProvider.isRegionOnScreenNow(filepathRefXML,region2));
	//	 Assert.assertTrue(icProvider.isRegionOnScreenNow("kkk.xml",region2)); //resource in jar
	}

	@Test
	public void testIsRegionOnScreenNowStringFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );

		Assert.assertFalse(icProvider.isRegionOnScreenNow(filepathRefXML,region2));
	}
	
	@Test
	public void testIsRegionOnScreenNowStringNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );

		Assert.assertFalse(icProvider.isRegionOnScreenNow(null,region2));
	}
	
	@Test
	public void testIsRegionOnScreenNowStringNullTest1() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );

		Assert.assertFalse(icProvider.isRegionOnScreenNow(filepathRefXML,null));
	}

	@Test
	public void testAreAllRegionsOnScreenNowListOfImageCompareRegionInfo() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		EasyMock.replay( videoProvider );
		
		ArrayList<ImageCompareRegionInfo> listRegionInfo = null;
		try {
			listRegionInfo =(ArrayList)regionLocatorImpl.getRegionInfo(filepathRefXML);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
        {
            Assert.assertTrue(icProvider.areAllRegionsOnScreenNow((ArrayList<ImageCompareRegionInfo>)listRegionInfo));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testAreAllRegionsOnScreenNowListOfImageCompareRegionInfoFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );
		ArrayList<ImageCompareRegionInfo> listRegionInfo = null;
		try {
			listRegionInfo =(ArrayList)regionLocatorImpl.getRegionInfo(filepathRefXML);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
        {
            Assert.assertFalse(icProvider.areAllRegionsOnScreenNow((ArrayList<ImageCompareRegionInfo>)listRegionInfo));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testAreAllRegionsOnScreenNowListOfImageCompareRegionInfoNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );
		ArrayList<ImageCompareRegionInfo> listRegionInfo = null;
		try
        {
            Assert.assertFalse(icProvider.areAllRegionsOnScreenNow(listRegionInfo));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	

	@Test
	public void testAreAllRegionsOnScreenNowString() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage);
		EasyMock.replay( videoProvider );

		Assert.assertTrue(icProvider.areAllRegionsOnScreenNow(filepathRefXML));
	//	 Assert.assertTrue(icProvider.areAllRegionsOnScreenNow("kkk.xml")); //resource in jar
		
	}
	
	@Test
	public void testAreAllRegionsOnScreenNowStringFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );

		Assert.assertFalse(icProvider.areAllRegionsOnScreenNow(filepathRefXML));
	}
	
	@Test
	public void testAreAllRegionsOnScreenNowStringNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		EasyMock.replay( videoProvider );
		String imgXMLPath = null;
		Assert.assertFalse(icProvider.areAllRegionsOnScreenNow(imgXMLPath));
	}
	


	@Test
	public void testWaitForImageOnScreen() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage).anyTimes();
		  EasyMock.replay( videoProvider );
		  try
        {
            Assert.assertTrue(icProvider.waitForImageOnScreen(refImage,1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForImageOnScreenFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		  EasyMock.replay( videoProvider );
		  try
        {
            Assert.assertFalse(icProvider.waitForImageOnScreen(refImage,1000));
        }
        catch ( ImageCompareException e )
        {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForImageOnScreenNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		  EasyMock.replay( videoProvider );
		  try
        {
            Assert.assertFalse(icProvider.waitForImageOnScreen(null,1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForSameImage() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage).anyTimes();
		  EasyMock.replay( videoProvider );
		  Assert.assertTrue(icProvider.waitForSameImage(filepathRefJPG,1000));
	//	  Assert.assertTrue(icProvider.waitForSameImage("kkk.JPG",1000)); //resource in jar
	}
	
	@Test
	public void testWaitForSameImageFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		  EasyMock.replay( videoProvider );
		  Assert.assertFalse(icProvider.waitForSameImage(filepathRefJPG,1000));
	}
	
	@Test
	public void testWaitForSameImageNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage);
		  EasyMock.replay( videoProvider );
		  String filePath = null;
		  Assert.assertFalse(icProvider.waitForSameImage(filePath,1000));
	}
	

	@Test
	public void testWaitForAllRegionsListOfImageCompareRegionInfoLong() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage).anyTimes();
		EasyMock.replay( videoProvider );

		ArrayList<ImageCompareRegionInfo> listRegionInfo = null;
		try {
			listRegionInfo =(ArrayList)regionLocatorImpl.getRegionInfo(filepathRefXML);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
        {
            Assert.assertTrue(icProvider.waitForAllRegions(listRegionInfo, 1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForAllRegionsListOfImageCompareRegionInfoLongFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );

		ArrayList<ImageCompareRegionInfo> listRegionInfo = null;
		try {
			listRegionInfo =(ArrayList)regionLocatorImpl.getRegionInfo(filepathRefXML);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
        {
            Assert.assertFalse(icProvider.waitForAllRegions(listRegionInfo, 1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForAllRegionsListOfImageCompareRegionInfoLongNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		
		ArrayList<ImageCompareRegionInfo> listRegionInfo = null;
		try
        {
            Assert.assertFalse(icProvider.waitForAllRegions(listRegionInfo, 1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForAllRegions() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage).anyTimes();
		EasyMock.replay( videoProvider );
		
		Assert.assertTrue(icProvider.waitForAllRegions(filepathRefXML, 1000));
//		 Assert.assertTrue(icProvider.waitForAllRegions("kkk.xml",1000)); //resource in jar
	}
	
	@Test
	public void testWaitForAllRegionsFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		Assert.assertFalse(icProvider.waitForAllRegions(filepathRefXML, 1000));
	}
	
	@Test
	public void testWaitForAllRegionsNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		String imgXMLPath = null;
		Assert.assertFalse(icProvider.waitForAllRegions(imgXMLPath, 1000));
	}

	

	@Test
	public void testWaitForRegionImageCompareRegionInfoLong() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage).anyTimes();
		EasyMock.replay( videoProvider );

		ImageCompareRegionInfo regionInfo = null;
		try {
			regionInfo =(ImageCompareRegionInfo)regionLocatorImpl.getRegionInfo(filepathRefXML,region2);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
        {
            Assert.assertTrue(icProvider.waitForRegion(regionInfo, 1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForRegionImageCompareRegionInfoLongFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );

		ImageCompareRegionInfo regionInfo = null;
		try {
			regionInfo =(ImageCompareRegionInfo)regionLocatorImpl.getRegionInfo(filepathRefXML,region2);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try
        {
            Assert.assertFalse(icProvider.waitForRegion(regionInfo, 1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	@Test
	public void testWaitForRegionImageCompareRegionInfoLongNull() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		
		ImageCompareRegionInfo regionInfo = null;
		try
        {
            Assert.assertFalse(icProvider.waitForRegion(regionInfo, 1000));
        }
        catch ( ImageCompareException e )
        {
            Assert.fail(e.getMessage());
        }
	}
	
	
	@Test
	public void testWaitForRegion() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(currentImage).anyTimes();
		EasyMock.replay( videoProvider );

	//	Assert.assertTrue(icProvider.waitForRegion("kkk.xml",region2, 1000)); //resource in jar
		Assert.assertTrue(icProvider.waitForRegion(filepathRefXML,region2, 1000)); //resource in filedisk
	}
	
	@Test
	public void testWaitForRegionFail() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		Assert.assertFalse(icProvider.waitForRegion(filepathRefXML,region2, 1000));
	}
	
	@Test
	public void testWaitForRegionNullTest() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		String imgXMLPath = null;
		Assert.assertFalse(icProvider.waitForRegion(imgXMLPath,region2, 1000));
	}
	
	@Test
	public void testWaitForRegionNullTest1() {
		EasyMock.expect(videoProvider.getVideoImage(expectedReferenceDimension)).andReturn(nonMatchingCurrentImage).anyTimes();
		EasyMock.replay( videoProvider );
		Assert.assertFalse(icProvider.waitForRegion(filepathRefXML,null, 1000));
	}

	@Test
	public void testSaveImageRegion()
	{
	    try
        {
	        File imgFile = new File( filepathGuideJPG );
            icProvider.saveImageRegion( imgFile.getAbsolutePath(), region_xfinity, 143, 67, 30, 33, 85 );
            ImageCompareRegionInfo icRegInfo = (ImageCompareRegionInfo) icProvider.getRegionInfo( filepathGuideXML, region_xfinity);
            Assert.assertNotNull( icRegInfo );
            Assert.assertEquals( new Integer(30), icRegInfo.getX() );
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
    public void testSaveImageRegionFail()
    {
	    boolean testPassed = false;
        try
        {
            icProvider.saveImageRegion( filepathGuideJPG, null, 143, 67, 30, 33 );
        }
        catch (IllegalArgumentException e) 
        {
            testPassed = true;            
        }
        catch ( IOException e )
        {     
            Assert.fail( e.getMessage() );
        }
        catch ( JAXBException e )
        {
            Assert.fail( e.getMessage() );
        }
        Assert.assertTrue( testPassed );
    }
}