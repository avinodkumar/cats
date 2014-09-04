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

import com.comcast.cats.service.PowerService;

/**
* Client for the PowerService WebService.
* @author jtyrre001
*/
@WebServiceClient(name = PowerServiceConstants.IMPL_STRING)
public class PowerServiceEndpoint extends Service{
	protected PowerServiceEndpoint(URL wsdl, QName qName) {
		super(wsdl, qName);
	}

	public PowerServiceEndpoint(URL wsdl) {
		super(wsdl, new QName(PowerServiceConstants.NAMESPACE,
				PowerServiceConstants.IMPL_STRING));		
	}
	
	/**
    *
    * @return       returns PowerService
    */
   @WebEndpoint(name = PowerServiceConstants.PORT)
   public PowerService getPowerServiceImplPort() {
       return super.getPort(new QName(PowerServiceConstants.NAMESPACE,
    		   PowerServiceConstants.PORT), PowerService.class);
   }

   /**
    *
    * @param features   A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
    *                   Supported features not in the <code>features</code> parameter will have their default values.
    * @return           returns PowerService
    */
   @WebEndpoint(name = PowerServiceConstants.PORT)
   public PowerService getPowerServiceImplPort(WebServiceFeature... features) {
       return super.getPort(new QName(PowerServiceConstants.NAMESPACE,
    		   PowerServiceConstants.PORT), PowerService.class, features);
   }
}
