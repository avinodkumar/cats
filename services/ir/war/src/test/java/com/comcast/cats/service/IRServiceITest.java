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
package com.comcast.cats.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cats.service.impl.KeyManagerProxyProvider;
import com.comcast.cats.service.impl.KeyManagerProxyProviderRest;

/**
 * The Class PowerControllerDeviceFactoryImplTest.
 * 
 * @Author : 
 * @since : 
 * @Description : The Class IRServiceIT is the integration test of {@link IRServiceITest}.
 */
public class IRServiceITest {
	private static final String endPointStr = "http://localhost:8080/ir-service/IRService?wsdl";
	
	@Test
	public void serviceCreate() throws MalformedURLException {
		Service srv = Service.create(new URL(endPointStr), new QName(IRServiceConstants.NAMESPACE,IRServiceConstants.IMPL_STRING));
		IRService irService = srv.getPort(IRService.class);
	}
	
   @Test
    public void keyMangerProxyRestTest() throws MalformedURLException {
       KeyManagerProxyProviderRest keyManagerProxyProvider = new KeyManagerProxyProviderRest("localhost:8080");
       KeyManagerProxy kmProxy = keyManagerProxyProvider.get();
       Assert.assertNotNull( kmProxy.remotes());
    }
}
