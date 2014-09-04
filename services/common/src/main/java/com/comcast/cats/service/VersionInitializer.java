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

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nishapk
 *
 */

public abstract class VersionInitializer {

	private static final Logger logger = LoggerFactory.getLogger(VersionInitializer.class);


	public static final String UNKNOWN_VERSION="UNKNOWN";
	public static final String VERSION_PROPERTY="serviceVersion";
	public static final String PROPERTY_FILE="/version.properties";

	protected Properties properties = null;

  	public void initVersionInfo() {
  		InputStream is = getClass().getResourceAsStream(PROPERTY_FILE);
  	    try {
  			if(null!=is){
  				properties = new Properties();
  				properties.load(is);
  			}
  		} catch (Exception e) {
  			logger.error(" Failed to retrieve version info");
  		}finally{

  			try{
  				is.close();
  			}catch(Exception e){
  				logger.error(" Failed to close input stream");
  			}
  		}
  	}

  	public String getVersion(){
  		String version=UNKNOWN_VERSION;
  		if(null!= properties){
			version = properties.getProperty(VERSION_PROPERTY);
		}
		logger.debug(" getVersion returned"+version);
		return version;
  	}
}
