'****************************************************************************
'@Author           : TATA
'@Description      : Function Library for SNMP WebServices 
                    'SnmpGet - Retrieve the value of an SNMP object specified by the oId from an SNMP  Agent 
					'and This will return error message when oId or target IP is null.
				    ' SnmpSet-Sets the value of an SNMP object specified by the oId from an SNMP  Agent.
'@Date             : 24/5/2010
'@Version          : 
'****************************************************************************

'     SNMP GET  METHOD  which Retrieves the value of an SNMP object specified by the oId from an SNMP  Agent. This will perform SNMP V1/V2 GET if any of the
'     parameters userName,authenticatePassword, privacyPassword is null
'     otherwise it will perform SNMP V3 GET

'     * @param oId
'     *           Object identifier representing the functionality to be obtained.
'     * @param communityName
'     *           String to represent the communityName of the target
'     * @param targetIP
'     *            IP address of target machine
'     * @param portNumber
'     *            Port number (by default 161)
'     * @param userName
'     *            the security name of the user
'     * @param authenticatePassword
'     *            the authentication password
'     * @param privacyPassword
'     *            the privacy password
'     * @return XML response which contains the resultobject, response status and service code

Function SnmpGet ( oId,  communityName, targetIP,portNumber,userName,authenticatePassword, privacyPassword)

			response = WebService("SnmpService").get( oId,  communityName, targetIP,portNumber,userName,authenticatePassword, privacyPassword)
			SnmpGet = response

End Function



'     SNMP SET  METHOD which Sets the value of an SNMP object specified by the oId from an SNMP Agent.
'     This will perform SNMP V1/V2 SET if any of the  parameters userName,authenticatePassword,privacyPassword is null
'     otherwise it will perform SNMP V3 SET
'
'     *@param oId
'     *            - String to represent the OId of the object
'     * @param communityName
'     *            -String to represent the communityName of the target
'     * @param targetIP
'     *            -String to represent the IP address of the target
'     * @param portNumber
'     *            -Represents the port Number of the target
'     * @param setvalue
'     *            -String to Represents the new Value be set
'     * @param setValueType
'     *            -String to Represents the type of new Value be set
'     * @param userName
'     *            -String to represent the user name of the target
'     * @param authenticatePassword
'     *            -String to represent the authentication Pass -word of the
'     *            target
'     * @param privacyPassword
'     *            -String to represent the privacy Password of the target
'     * @return XML response which contains the resultobject, response status and service code

Function SnmpSet ( oId,  communityName, targetIP,portNumber,setvalue, setValueType, userName,authenticatePassword, privacyPassword)

	 response = WebService("SnmpService").set( oId,  communityName, targetIP, portNumber, setvalue, setValueType, userName, authenticatePassword, privacyPassword)

      SnmpSet = response
End Function
	

'****************************************************************************
'Variable Declarations
'****************************************************************************
Dim result
Dim resultObject
Dim serviceCode

'   XML Response Parser  for getting  reponse status ,which indicates whether the snmp operation is success or  failure
Function retrieveResult(returnObj)

			Set	 xmlObj = XMLUtil.CreateXML()
			xmlObj.Load returnObj
'   Getting the reponse status , ie whether response is success or failure
		    Set child= xmlObj.GetRootElement().ChildElements().ItemByName("resultCode")
			result  = child.Value()
'   Returning the result
            retrieveResult = result

End Function


'   XML Response Parser for getting the ResultObject from XML response
'   if  operation is success the correct value is returned
'   if  response is failure  the error message is returned
Function retrieveResultObject(returnObj)

			Set	 xmlObj = XMLUtil.CreateXML()
			xmlObj.Load returnObj
			Set children = xmlObj.GetRootElement().ChildElements() 
			success = "SUCCESS"
			result =retrieveResult(returnObj)
'   Checking whether the response is success , if  success getting the ResultObject
			 If  result = success Then 
					Set child = children.ItemByName("resultObject")
			         resultObject = child.Value()
'   if  response is failure getting the error message
					 Else
					 Set child = children.ItemByName("message")
			         resultObject = child.Value()
			 End If

'   Returning the result
  		  retrieveResultObject = resultObject

End Function

'   XML Response Parser for getting the ServiceCode from XML response,ie SNMP  Operation specific  message
'   eg:SNMP_SERVICE_SUCCESS
Function retrieveserviceCode(returnObj)

			Set	 xmlObj = XMLUtil.CreateXML()
			xmlObj.Load returnObj
'   Getting the Service Code
			 Set child= xmlObj.GetRootElement().ChildElements().ItemByName("serviceCode")
			 serviceCode =  child.Value()

'   Returning the result
  		  retrieveserviceCode = serviceCode

End Function


