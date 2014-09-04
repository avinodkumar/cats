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
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OCRCompareResultTest {
	private static byte[] FIRST = "FirstImg".getBytes();
	private static byte[] LAST = "LastImg".getBytes();
	private static String TESTFILENAME = "target/testdata.xml";

	@Test
	public void testWrite() throws Exception {
		JAXBContext contextObj = JAXBContext
				.newInstance(OCRCompareResult.class);

		Marshaller marshal = contextObj.createMarshaller();
		marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		OCRCompareResult data = new OCRCompareResult();
		data.setAccuracy(90);
		data.setDistance(0);
		data.setErrorMsg("no error");
		data.setFirstImageCompared(FIRST);
		data.setLastImageCompared(LAST);
		marshal.marshal(data, new FileOutputStream(TESTFILENAME));

	}

	@Test(dependsOnMethods = "testWrite")
	public void testRead() throws Exception {
		JAXBContext contextObj = JAXBContext
				.newInstance(OCRCompareResult.class);

		Unmarshaller unm = contextObj.createUnmarshaller();

		OCRCompareResult data = (OCRCompareResult) unm.unmarshal(new File(
				TESTFILENAME));
		Assert.assertEquals(new Integer(90), data.getAccuracy());

		String exp = new String(FIRST);
		String act = new String(data.getFirstImageCompared());

		System.out.println("Exp string : " + exp + ", Act string: " + act);
		Assert.assertEquals(exp, act);

	}

}
