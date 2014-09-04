'****************************************************************************
'@Author            : TATA
'@Description       : For testing SNMP WEbServices Through associated 
					  'function library from QTP				  
'@Date              : 24/5/2010
'@Version           : 
'****************************************************************************

'****************************************************************************
'Variable Declarations
'****************************************************************************

Dim sampleV1Get
Dim sampleV1Set
Dim sampleV3Get
Dim sampleV3Set
Dim oId
Dim communityName
Dim targetIP
Dim V1targetPort
Dim V3targetPort
Dim userName
Dim authenticatePassword
Dim privacyPassword
Dim newValue
Dim readcommunity

Dim resultObject
Dim result
Dim ServiceCode
Dim RespObject
Dim Response

'****************************************************************************
'Variable Initialization
'****************************************************************************
oId= "1.3.6.1.2.1.1.6.0"
communityName="public"
writecommunity = "community"
targetIP ="192.168.161.200"
V1targetPort="161"
V3targetPort="161"
userName="myuser"
authenticatePassword="my_password"
privacyPassword="my_password"
newValue="Moon"
setValueType="String"

 code ="ServiceCode::"
 resultObject ="ResultObject::"
 result = "Result::"

'****************************************************************************
'Snmp  V1/V2  GET .
'retrieve response status, result  value and service code from
'SNMP  V1/V2 GETresponse 
'****************************************************************************

sampleV1Get = SnmpGet (oId , communityName, targetIP, V1targetPort, "","","")
Response =   retrieveResult(sampleV1Get)
RespObject  = retrieveResultObject(sampleV1Get)
ServiceCode = retrieveserviceCode(sampleV1Get)
' Displaying the  Result
MsgBox (result& Response)
MsgBox (resultObject& RespObject )
MsgBox (code& ServiceCode)

'****************************************************************************
'Snmp  V1/V2  SET . 
' retrieve response status, result  value and service code from
' SNMP  V1/V2 SETresponse 
'****************************************************************************
sampleV1Set = SnmpSet (oId , writecommunity, targetIP, V1targetPort, newValue, setValueType, "", "", "")
Response =   retrieveResult(sampleV1Set)
RespObject  =retrieveResultObject(sampleV1Set)
ServiceCode = retrieveserviceCode(sampleV1Set)
' Displaying the  Result
MsgBox (result& Response)
MsgBox (resultObject& RespObject )
MsgBox (code& ServiceCode)

'****************************************************************************
'Snmp  V3   GET  call.
'retrieve response status, result  value and service code from
' SNMP  V3 GETresponse 
'****************************************************************************
sampleV3Get = SnmpGet (oId , communityName, targetIP, V3targetPort, userName,authenticatePassword,privacyPassword)
Response =   retrieveResult(sampleV3Get)
RespObject  = retrieveResultObject(sampleV3Get)
ServiceCode = retrieveserviceCode(sampleV3Get)
' Displaying the  Result
MsgBox (result& Response)
MsgBox (resultObject& RespObject )
MsgBox (code& ServiceCode)

'****************************************************************************
'Snmp  V3   SET  call.
 'retrieve response status, result  value and service code from
' SNMP  V3  SETresponse 
'****************************************************************************
sampleV3Set = SnmpSet (oId , writecommunity, targetIP, V3targetPort,newValue, setValueType, userName,authenticatePassword,privacyPassword)
Response =   retrieveResult(sampleV3Set)
RespObject  = retrieveResultObject(sampleV3Set)
ServiceCode=retrieveserviceCode(sampleV3Set)
' Displaying the  Result
MsgBox (result& Response)
MsgBox (resultObject& RespObject )
MsgBox (code& ServiceCode)


