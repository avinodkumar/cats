<%--

    Copyright 2014 Comcast Cable Communications Management, LLC

    This file is part of CATS.

    CATS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CATS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CATS.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page import="com.comcast.cats.service.power.*"%>
    <%@page import="java.util.*"%>
    <%@page import="com.comcast.cats.service.PowerInfo"%>
    <%@page import="com.comcast.cats.service.PowerStatistics"%>
    <%@page import="com.comcast.cats.service.power.util.PowerConstants"%>
    
    
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="cache-control" content="no-cache"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/screen.css" type="text/css">
<title>Power Devices</title>

<script type="text/javascript">
// Delete a power device given its ip.
function deleteDevice(ip){
	var answer= confirm("Are you sure you want to delete this power device? ");
	if(answer){
		document.getElementById('deviceIp').value = ip;
		document.powerDeviceFrm.action = "<%=request.getContextPath()%><%=PowerConstants.PATH_TO_POWERDEVICES%>?"+
		"<%=PowerConstants.REQ_PARAM_ACTION%>=<%=PowerConstants.DELETE_DEVICE_ACTION%>&<%=PowerConstants.DEVICEIP%>="+ip;
        document.powerDeviceFrm.submit();
		
	}
	
}
//Set the footer position to bottom.
function setFooterPosition(){
	var h =  window.screen.height;
	var footerPosn = h * 72 /100;
	document.getElementById("page").style.minHeight = footerPosn + 'px';
}

//Change the side menu tab to show selected.
function changeColor( ) {
	if(null != document.getElementById( 'devices' )){
		document.getElementById( 'devices' ).style.background='#3F6683';
		document.getElementById( 'devices' ).style.color='white';
	}
}

</script>

</head>
<body onLoad="setFooterPosition();changeColor();">
<div id="page-header">
		<jsp:include page="/views/templates/header.jsp" />
</div>
<br/>
<br/>
<br/>
<br/>
<div id="center" style="background-color: #B2CFDE; text-align: right; height: 20px;"></div>
<h2 align="center" style="font-family: Arial,sans-serif;">Power Devices</h2>

<div id="page" class="ui-widget" align="left" style="text-align: left; margin:0%">
<div id="page-sidebar">		
		<jsp:include page="/views/templates/sidebar.jsp" />
</div>

<br/>
<br/>
<br/>
<div id="page-content">
<form name="powerDeviceFrm" method="POST" action="<%=request.getContextPath()%><%=PowerConstants.PATH_TO_POWERDEVICES%>">
<%
ArrayList<PowerInfo> powerDevicesInfoList = (ArrayList<PowerInfo>)session.getAttribute("powerDevicesInfoList");
if(null != powerDevicesInfoList && powerDevicesInfoList.size() >0){
%>

<table class="dataTable" id="tblPowerDevice" BORDER="1" CELLPADDING="10" CELLSPACING="10">
		<tr>			
			<th>Device</th>
			<th>Type</th>
			<th>Port</th>
			<th>Number Of Outlets</th>
			<th></th>
		</tr>
	<%
	for(PowerInfo powerInfo: powerDevicesInfoList){
		String ip = powerInfo.getIp();
		String type = powerInfo.getType();
		int port = powerInfo.getPort();
		int numOutlets = powerInfo.getNumOfOutlets();
	%>
	
	<tr>
		<td><%=ip %></td>
		<td><%=type %></td>
		<td><%=port %></td>
		<td><%=numOutlets %></td>
		<td>
		<input type="hidden" name="deviceIp" id="deviceIp" value="" />
		<input type="button" name="delete" value="Delete" onclick="deleteDevice('<%=ip %>');" style="cursor: pointer;"/>
		</td>
	</tr>
	
	<%
	}
	%>
	</table>
	<%
}else{
	%>
	<h3>No devices available!!</h3>
	<%
}
	
	
	%>	
</form>	
</div>
</div>
</body>
<div id="footer" align="right">
	<p>© 2012 Comcast Corporation</p>
</div>
<br/>

</html>