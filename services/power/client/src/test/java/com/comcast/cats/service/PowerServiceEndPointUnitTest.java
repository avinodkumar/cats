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
import javax.xml.ws.WebServiceException;

import org.testng.annotations.Test;


/**
 * The Class PowerServiceEndPointUnitTest.
 * 
 * @Author : Aneesh
 * @since : 5th Oct 2012
 * Description : The Class PowerServiceEndPointUnitTest is the unit test of {@link PowerServiceEndPoint}.
 */

public class PowerServiceEndPointUnitTest
{
    
    /** The pwr service end point. */
    PowerServiceEndpoint pwrServiceEndPoint;
    
    /**
     * Test power service endpoint contructor1.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions=WebServiceException.class)
    public void testPowerServiceEndpointContructor1() throws Exception 
    {
        pwrServiceEndPoint = new PowerServiceEndpoint(new URL( "http://192.168.120.102:8080/power-service/PowerService?wsdl" )); // This is an invalid wsdl url, hence the constructor throws exception
    }
    
    /**
     * Test power service endpoint contructor2.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions=WebServiceException.class)
    public void testPowerServiceEndpointContructor2() throws Exception 
    {
        pwrServiceEndPoint = new PowerServiceEndpoint(new URL( "http://192.168.120.102:8080/power-service/PowerService?wsdl"),new QName( "" ));// This is an invalid wsdl url, hence the constructor throws exception
    }

}
