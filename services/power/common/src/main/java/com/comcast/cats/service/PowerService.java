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

import java.net.URI;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface PowerService extends CatsWebService
{
	/**
     * Return locator for hardware that facilitates power handling. This will
     * point to the device used for Power handling on a rack.
     * 
     * @return URI containing power hardware definition.
     */
    @WebMethod
    String powerLocator();

    /**
     * Return the ON or Off status state of a WTI device power outlet.
     * @param path URI containing power hardware definition.
     * @return The power state returned by the WTI or Netboost power strips.     
     */
    @WebMethod
    String powerStatus( @WebParam( name = "path" ) final URI path );
    
    /**
     * Hard powers OFF a settop device outlet using the WTI device.
     * @param path URI containing power hardware definition.
     * @return true if operation is successful, false otherwise    
     */
    @WebMethod
    boolean hardPowerOff( @WebParam( name = "path" ) final URI path );
    
    /**
     * Hard powers ON a settop device outlet using the WTI device.
     * @param path URI containing power hardware definition.
     * @return true if operation is successful, false otherwise    
     */
    @WebMethod
    boolean hardPowerOn( @WebParam( name = "path" ) final URI path );
    
    /**
     * Hard power cycles a settop outlet OFF and then ON using the WTI device.
     * @param path URI containing power hardware definition.
     * @return true if operation is successful, false otherwise    
     */
    @WebMethod
    boolean hardPowerToggle( @WebParam( name = "path" ) final URI path );
    
    @WebMethod
    List<PowerInfo> getAllPowerDevicesInfo();
    
    @WebMethod
    List<PowerStatistics> getPowerStatisticsPerDevice(@WebParam(name = "ip") final String ip);
      
    @WebMethod 
    PowerStatistics getPowerOutletStatistics(@WebParam(name = "ip") final String ip, @WebParam(name = "outlet") final int outlet);
    
    @WebMethod
    void removePowerDevice(@WebParam(name="ip") final String ip);
}
