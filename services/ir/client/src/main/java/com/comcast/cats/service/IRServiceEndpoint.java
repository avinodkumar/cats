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

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
* Client for the IRService WebService.
* @author cfrede001
*/
@WebServiceClient(name = IRServiceConstants.IMPL_STRING)
public class IRServiceEndpoint extends Service {

	protected IRServiceEndpoint(URL wsdl, QName qName) {
		super(wsdl, qName);		
	}

	public IRServiceEndpoint(URL wsdl) {
		super(wsdl, new QName(IRServiceConstants.NAMESPACE,IRServiceConstants.IMPL_STRING));		
	}
	
	/**
    *
    * @return
    *     returns SettopService
    */
   @WebEndpoint(name = IRServiceConstants.PORT)
   public IRService getIRServiceImplPort() {
       return super.getPort(new QName(IRServiceConstants.NAMESPACE,IRServiceConstants.PORT), IRService.class);

   }

   /**
    *
    * @param features
    *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
    *     Supported features not in the <code>features</code> parameter will have their default values.
    * @return
    *     returns SettopService
    */
   @WebEndpoint(name = IRServiceConstants.PORT)
   public IRService getIRServiceImplPort(WebServiceFeature... features) {
       return super.getPort(new QName(IRServiceConstants.NAMESPACE,IRServiceConstants.PORT), IRService.class, features);

   }
}
