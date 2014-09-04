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
package com.comcast.cats.keymanager.service;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "KeyManagerServiceImpl")
public class KeyManagerServiceEndpoint extends Service {

	private static final String NAMESPACE = "http://service.keymanager.cats.comcast.com";
	private static final String PORT = "KeyManagerServicePort";
	private static final String IMPL_STRING = "KeyManagerServiceImpl";

	protected KeyManagerServiceEndpoint(URL wsdl, QName qName) {
		super(wsdl, qName);
	}

	public KeyManagerServiceEndpoint(URL wsdl) {
		super(wsdl, new QName(NAMESPACE, IMPL_STRING));
	}

	/**
	 * 
	 * @return returns SettopService
	 */
	@WebEndpoint(name = PORT)
	public KeyManagerService getIRServiceImplPort() {
		return super.getPort(new QName(NAMESPACE, PORT),
				KeyManagerService.class);

	}

	/**
	 * 
	 * @param features
	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *            on the proxy. Supported features not in the
	 *            <code>features</code> parameter will have their default
	 *            values.
	 * @return returns SettopService
	 */
	@WebEndpoint(name = PORT)
	public KeyManagerService getIRServiceImplPort(WebServiceFeature... features) {
		return super.getPort(new QName(NAMESPACE, PORT),
				KeyManagerService.class, features);

	}
}
