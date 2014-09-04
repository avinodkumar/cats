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

import com.comcast.cats.SettopConstants;

public class PowerServiceConstants {
	public static final String SERVICE_NAME = "PowerService";
	public static final String NAMESPACE = SettopConstants.NAMESPACE;
	public static final String PORT = "PowerServicePort";
	
	public static final String IMPL_STRING = "PowerServiceWSImplService";
	public static final String WSDL_PATH = "power-service/PowerService?WSDL";
	public static final String MAPPED_NAME = "cats/services/PowerService";
	public static final String ENDPOINT_INTERFACE = "com.comcast.cats.service.PowerService";
}
