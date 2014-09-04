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
package com.comcast.cats.info;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.service.WebServiceReturn;

/**
 * General SnmpServiceReturnMesage class used to encapsulate all SNMP messages.
 * 
 * @author TATA
 * 
 */
@XmlRootElement
public class SnmpServiceReturnMesage extends WebServiceReturn
{
    /**
     * Generated Serial Version ID.
     */
    private static final long     serialVersionUID = -8614099182546651086L;
    /**
     * Holds the status of operation represented by SnmpServiceReturnEnum.
     */
    private SnmpServiceReturnEnum serviceCode;
    /**
     * The return value received from the SNMP agent.
     */
    private String                resultObject;
    
    private Integer     	     snmpErrorCode;
   
    private List< SnmpWalkResult>    complexResultObject;

    /**
     * By default we want to select the general SNMP_SERVICE_SUCCESS. Make sure
     * the base return message is also set.
     */
    public SnmpServiceReturnMesage()
    {
        this.serviceCode = SnmpServiceReturnEnum.SNMP_SERVICE_SUCCESS;
    }

    /**
     * Constructor creates a SnmpServiceReturnMesage object with the supplied
     * SnmpServiceReturnEnum set.
     * 
     * @param serviceCode
     *            SnmpServiceReturnEnum
     */
    public SnmpServiceReturnMesage( final SnmpServiceReturnEnum serviceCode )
    {
        this.serviceCode = serviceCode;
    }

    /**
     * Method returns the status of snmp operation represented by
     * SnmpServiceReturnEnum.
     * 
     * @return SnmpServiceReturnEnum
     */
    public SnmpServiceReturnEnum getServiceCode()
    {
        return serviceCode;
    }

    /**
     * Method for setting the status of snmp operation.
     * 
     * @param serviceCode
     *            SnmpServiceReturnEnum
     */
    @XmlElement( name = "serviceCode" )
    public void setServiceCode( final SnmpServiceReturnEnum serviceCode )
    {
        this.serviceCode = serviceCode;
    }
    /**
     * Method returns the status of snmp operation represented by
     * SnmpServiceReturnEnum.
     * 
     * @return SnmpServiceReturnEnum
     */
    public Integer getSnmpErrorCode()
    {
        return snmpErrorCode;
    }

    /**
     * Method for setting the status retur from SNMP4J.
     * 
     * @param serviceCode
     *            SnmpServiceReturnEnum
     */
    @XmlElement( name = "errorCode" )
    public void setSnmpErrorCode( final Integer snmpErrorCode )
    {
        this.snmpErrorCode = snmpErrorCode;
    }

    /**
     * Getter method for the value received for the snmp operation.
     * 
     * @return The value received for the snmp operation.
     */
    public String getResultObject()
    {
        return resultObject;
    }

    /**
     * Setter method for the value received for snmp operation.
     * 
     * @param resultObject
     *            Value got from the snmp operation.
     */
    
    public void setResultObject( final String resultObject )
    {
        this.resultObject = resultObject;
    }
    
    /**
     * Returns the complex result.
     * This will be used in case of SNMP WALK.
     * @return
     */
    
    @XmlElementWrapper(name = "complexResultObjects")
    @XmlElement(name = "complexResultObject")
    public List< SnmpWalkResult > getComplexResultObject()
    {
        return complexResultObject;
    }

    /**
     * Sets the List of strings. This will be used in case of snmp walk.
     * 
     * @param complexResultObject
     */
    
    public void setComplexResultObject( final List< SnmpWalkResult > complexResultObject )
    {
        this.complexResultObject = new ArrayList< SnmpWalkResult >(complexResultObject);
    }

    @Override
    public String toString()
    {
        return "ResultObject "+ resultObject + "ComplexResultObject "+complexResultObject;
    }

}
