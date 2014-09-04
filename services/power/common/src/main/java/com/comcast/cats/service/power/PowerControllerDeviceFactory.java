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
package com.comcast.cats.service.power;

import java.net.URI;
import java.util.ArrayList;

import com.comcast.cats.service.PowerInfo;

public interface PowerControllerDeviceFactory {
	
   /**
    * Create Power controller device for the following path
    * @param path - URI for which the power controller device has to be created
    * @return PowerControllerDevice object.
    */
   public  PowerControllerDevice createPowerControllerDevice( final URI path );
   
   /**
    * Destroy all power controller devices.
    */
   public  void destroyAllControllers(); 
       
  /**
    * Get the power info list for all the power controller devices. 
    */
   public ArrayList<PowerInfo> getAllPowerDevicesInfo();
   
   /**
    * Remove power controller device information
    * @param ip
    */
   public void removePowerDevice(String ip);
   
 
   
}
