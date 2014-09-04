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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.comcast.cats.service.power.*"%>
<%@page import="java.util.*"%>
<%@page import="com.comcast.cats.service.power.util.PowerConstants"%>
<%@page import="com.comcast.cats.service.PowerInfo"%>
<%@page import="com.comcast.cats.service.PowerStatistics"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="cache-control" content="no-cache"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/screen.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/AjaxManager.js"></script>
<title>Debugging</title>

<%
String add = "Add_";
String remove = "Remv_";
String reqParam = null;
String actionParam = null;
Integer numOfOutlets = (Integer) request.getSession().getAttribute(PowerConstants.NUM_OF_OUTLETS);
String powerDeviceType = (String)request.getSession().getAttribute(PowerConstants.POWER_DEVICE_TYPE);
String powerStatus = (String)request.getSession().getAttribute(PowerConstants.POWER_STATUS);
String result = (String)request.getAttribute(PowerConstants.RESULT);

%>


<script type="text/javascript">

function hideDetails(ip){
	
	document.getElementById("tr_"+ip).style.display="none";
	document.getElementById("divSub_"+ip).style.display="none";
	document.getElementById("Remv_"+ip).style.display="none";
	document.getElementById("Add_"+ip).style.display="inline";
}

function showDetails(ip){
	document.getElementById("tr_"+ip).style.display="inline";
	document.getElementById("divSub_"+ip).style.display="inline";
	document.getElementById("Add_"+ip).style.display="none";
	document.getElementById("Remv_"+ip).style.display="inline";
}

function onDeviceSelect()
{	
	hideMessages();
	var ip = document.getElementById('deviceFilter').value;
	document.getElementById('outletFilter').options.length = 0;
	if(ip == 'Please Select'){
		alert(" Please select a device.");
		return;
	}
	
	if(ip != '' && ip != 'undefined'){
		var urlToSubmit = "<%=request.getContextPath()%><%=PowerConstants.PATH_TO_DEBUGGING%>?"+
		"<%=PowerConstants.REQ_PARAM_ACTION%>=<%=PowerConstants.POWER_DEVICE_SELECTED%>&<%=PowerConstants.DEVICEIP%>="+ ip;
		
		var requestForAjax = retrieveAjaxRequest(urlToSubmit);
		var deviceType =  trim(getInnerTextOfTag(requestForAjax.responseText, 'PowerDeviceType'));
		document.getElementById('deviceType').value = deviceType;
		var numOfOutlets = trim(getInnerTextOfTag(requestForAjax.responseText, 'PowerDeviceNumOfOutlets'));
		createOption('Please Select', 'outletFilter');
		
		for(var i = 1; i<=numOfOutlets; i++) {
			createOption(i, 'outletFilter');
		}
		
	}
}

function trim(str)
{
    if(!str || typeof str != 'string'){
        return null;
    }
    return str.replace(/^[\s]+/,'').replace(/[\s]+$/,'').replace(/[\s]{2,}/,' ');
}

function powerAction(){
	
	var type=document.getElementById('deviceType').value;
	var ip= document.getElementById('deviceFilter').value;
	var outlet= document.getElementById('outletFilter').value;
	var action =  document.getElementById('powerFilter').value;
	
	
	
	
	if(ip == 'Please Select'){
		alert(" Please select a device.");
	}else if(outlet == 'Please Select'){
		alert(" Please select an outlet.");
	}else if(ip != '' && ip != 'undefined' && outlet != '' && outlet != 'undefined'){
	
		var path= type + "://" + ip + ":23/?outlet=" + outlet;
		document.powerDebuggingFrm.action = "<%=request.getContextPath()%><%=PowerConstants.PATH_TO_DEBUGGING%>?"+
				"<%=PowerConstants.REQ_PARAM_ACTION%>=<%=PowerConstants.POWER_ACTION%>&<%=PowerConstants.ACTION_PARAM%>="+ action +
				"&<%=PowerConstants.REQUEST_PARAM%>="+path +
				"&<%=PowerConstants.DEVICEIP%>="+ip+
				"&<%=PowerConstants.OUTLET%>="+outlet;
		document.powerDebuggingFrm.submit();
	
	}
}

function onOutletSelect(){

	hideMessages();
	var type=document.getElementById('deviceType').value;
	var ip= document.getElementById('deviceFilter').value;
	var outlet= document.getElementById('outletFilter').value;
	
	
	var path= type + "://" + ip + ":23/?outlet=" + outlet;
	
	if(ip != '' && ip != 'undefined'){
		
		var urlToSubmit = "<%=request.getContextPath()%><%=PowerConstants.PATH_TO_DEBUGGING%>?"+
		"<%=PowerConstants.REQ_PARAM_ACTION%>=<%=PowerConstants.POWER_DEVICE_STATUS_FETCH%>&<%=PowerConstants.REQUEST_PARAM%>="+ path;
		var requestForAjax = retrieveAjaxRequest(urlToSubmit);
		var powerStatus = trim(getInnerTextOfTag(requestForAjax.responseText, 'PowerStatus'));
		 var divTag = document.getElementById("message");
         divTag.className = "messageStyle";
         divTag.innerHTML = "**Power status of the device [ "+ip +" ] on Outlet [ "+outlet +" ] is " + powerStatus;
         document.getElementById("message").style.display="inline";
	}
	
}

function onPowerSelect(){
	hideMessages();
}

function createOption(value, id){
	
	var opt = document.createElement('option');
	opt.text = value;
	opt.value = value;
    document.getElementById(id).options.add(opt);
	
}

function setFooterPosition(){
	var h =  window.screen.height;
	var footerPosn = h * 72 /100;
	document.getElementById("page").style.minHeight = footerPosn + 'px';
}

function getInnerTextOfTag(text, tagName) {
	var startString = "<" + tagName + ">";
	var pos1 = text.indexOf(startString) + startString.length + 3;
	var pos2 = text.indexOf("<\/" + tagName + ">");
	var retVal = text.substring(pos1, pos2);
	return retVal;
}

function changeColor() {
	if(null != document.getElementById( 'debug' )){
		document.getElementById( 'debug' ).style.background='#3F6683';
		document.getElementById( 'debug' ).style.color='white';
	}
}

 function hideMessages(){
	if(document.getElementById("result")){
		document.getElementById("result").style.display="none";
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
<h2 align="center" style="font-family: Arial,sans-serif;">Power Device Debugging</h2>

<div id="page" class="ui-widget" align="left" style="text-align: left; margin:0%">
<div id="page-sidebar">		
		<jsp:include page="/views/templates/sidebar.jsp" />
</div>
<br/>
<br/>
<br/>
<div id="page-content">
	<!-- Statistics for WTI_IPS_1600_PowerDevice -->
<form name="powerDebuggingFrm" method="POST" action="<%=request.getContextPath()%><%=PowerConstants.PATH_TO_DEBUGGING%>"> 
	<%
	
	ArrayList<PowerInfo> powerDevicesInfoList = (ArrayList<PowerInfo>)session.getAttribute(PowerConstants.LIST_POWER_DEVICES_INFO);
		if(null != powerDevicesInfoList  && powerDevicesInfoList.size() >0){
			
	%>
		
<table style="width:100%;border:0px;">
<tr style="border:0px;">
<td style="font-weight: bold;">Power Device</td>
<td style="font-weight: bold;">Outlet</td>
<td style="font-weight: bold;">Action</td>
<td></td>
</tr>
<tr>
<td>
<select id="deviceFilter" style="width:80%;" name="deviceFilter" onChange="onDeviceSelect();">
<option value="Please Select" >Please Select</option>

<%
for (PowerInfo powerInfo : powerDevicesInfoList) {
	String ip = powerInfo.getIp();
	String type = powerInfo.getType();
%>

<option value="<%=ip%>" ><%=ip%></option>
	
<%
	
}

%>
</select>
</td>
<td>
<select id="outletFilter" style="width:80%;" name="outletFilter" onChange="onOutletSelect();">
<option value="Please Select" >Please Select</option>
</select>
</td>
<td>
<select id="powerFilter" style="width:80%;" name="powerFilter" onChange="onPowerSelect();">

<option value="<%=PowerConstants.POWER_ON%>" selected="selected">Power On</option>
<option value="<%=PowerConstants.POWER_OFF%>" >Power Off</option>
<option value="<%=PowerConstants.POWER_BOOT%>" >Reboot</option>
</select>
</td>

<td>
<input id="button"  type="button" name="execute" value="Execute" style="cursor: pointer;" onclick="powerAction();" />
<input id="deviceType" type="hidden" name="deviceType" value=""/>	
</td>

</table>
	


		<%

			}else{
				

%>

<h3>No devices available!!	</h3>
<%			
	}
%>
<br/>
<br/>
<div id="message" style="display:none"></div>

<% if(null != result){ %>
<div id="result">
<h3><%=result %></h3>
</div>

<%} %>


<div style="display: none;">
	<PowerDeviceNumOfOutlets>
	<%=numOfOutlets%>		
	</PowerDeviceNumOfOutlets>
	<PowerDeviceType>
	<%=powerDeviceType%>
	</PowerDeviceType>
	<PowerStatus>
	<%=powerStatus %>
	</PowerStatus>
	
	
</div>	

</form>

</div>
</div>
</body>
<div id="footer" align="right">
	<p>© 2012 Comcast Corporation</p>
</div>
<br />
</html>