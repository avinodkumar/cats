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
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.4.2.min.js"></script>
<title>Statistics</title>

<%
String add = "Add_";
String remove = "Remv_";
boolean checkCondition=false;
String divName = "div";
%>


<script type="text/javascript">

function showDetails(ip){
	document.getElementById("tr_"+ip).style.display="inline";
	document.getElementById("divSub_"+ip).style.display="inline";
	document.getElementById("Add_"+ip).style.display="none";
	document.getElementById("Remv_"+ip).style.display="inline";
	
}


function hideDetails(ip){
	
	document.getElementById("tr_"+ip).style.display="none";
	document.getElementById("divSub_"+ip).style.display="none";
	document.getElementById("Remv_"+ip).style.display="none";
	document.getElementById("Add_"+ip).style.display="inline";
	
}

function setFooterPosition(){
	var h =  window.screen.height;
	var footerPosn = h * 72 /100;
	document.getElementById("page").style.minHeight = footerPosn + 'px';
}

function changeColor() {
	if(null != document.getElementById( 'stat' )){
		document.getElementById( 'stat' ).style.background='#3F6683';
		document.getElementById( 'stat' ).style.color='white';
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
<h2 align="center" style="font-family: Arial,sans-serif;">Power Device Statistics</h2>

<div id="page" class="ui-widget" align="left" style="text-align: left; margin:0%">
<div id="page-sidebar">		
		<jsp:include page="/views/templates/sidebar.jsp" />
</div>
<br/>
<br/>
<br/>

<div id="loading" class="loading-invisible">
  <p><img src="<%=request.getContextPath()%>/images/loading.gif"  /></p>
</div>

<div id="page-content">
	<!-- Statistics for WTI_IPS_1600_PowerDevice -->
<form name="powerStatisticsFrm" method="POST" action="<%=request.getContextPath()%><%=PowerConstants.PATH_TO_STATISTICS%>"> 
<div>
<%
ArrayList<PowerInfo> powerDevicesInfoList = (ArrayList<PowerInfo>)session.getAttribute(PowerConstants.LIST_POWER_DEVICES_INFO);
if(null != powerDevicesInfoList && powerDevicesInfoList.size() >0){
%>
<table class="dataTable">
		<tr>
			<th style="width:150px;height:30px;">Device</th>
			<th style="height:30px;">Outlets</th>
			<th style="height:30px;">ON</th>
			<th style="height:30px;">OFF</th>
			<th style="height:30px;">REBOOT</th>
			<th style="height:30px;">ON Failure</th>
			<th style="height:30px;">OFF Failure</th>
			<th style="height:30px;">REBOOT Failure</th>
		</tr>
</table>
		<%
		
			for (PowerInfo powerInfo : powerDevicesInfoList) {
				String ip = powerInfo.getIp();
				int numOutlets = powerInfo.getNumOfOutlets();
				int onSummary = powerInfo.getTotalOn();
				int offSummary = powerInfo.getTotalOff();
				int rebootSummary = powerInfo.getTotalReboot();
				int onFailSummary = powerInfo.getTotalOnFailures();
				int offFailSummary = powerInfo.getTotalOffFailures();
				int rebootFailSummary = powerInfo.getTotalRebootFailures();
				
		%>
<table  class="tableStatistics">
		<tr>
		
			<%-- <td><a href="javascript:showDetails('<%=ip%>')"><%=ip%></a></td> --%>
			<td id="td_<%=ip%>" style="width:150px;">
			<span class=" ui-widget-content">
			   <img id="<%=add%><%=ip%>" align="left" style="display:inline;padding-right:10px;cursor: pointer;" src="<%=request.getContextPath()%>/images/add.png" onclick="showDetails('<%=ip %>');return false;" />
			   <img id="Remv_<%=ip%>" align="left" style="display:none;padding-right:10px;cursor: pointer;" src="<%=request.getContextPath()%>/images/minus.png" onclick="hideDetails('<%=ip %>');return false;" />
			 </span>
			 <span class=" ui-widget-content"><%=ip%></span>
			</td>
			<td><span class=" ui-widget-content"><%=numOutlets%></span></td>
			<td><span class=" ui-widget-content"><%=onSummary%></span></td>
			<td><span class=" ui-widget-content"><%=offSummary%></span></td>
			<td><span class=" ui-widget-content"><%=rebootSummary%></span></td>
			<td><span class=" ui-widget-content"><%=onFailSummary%></span></td>
			<td><span class=" ui-widget-content"><%=offFailSummary%></span></td>
			<td><span class=" ui-widget-content"><%=rebootFailSummary%></span></td>
		
		</tr>
</table>		
<br/>
<div id="divSub_<%=ip%>" style="display:none;">
	
<table class="dataTable" id="tr_<%=ip%>" style="display:none;padding-left:3%;">

		<tr>
		    <th class="textShadow" style="height:30px;">Outlet#</th>
			<th style="height:30px;">ON</th>
			<th style="height:30px;">OFF</th>
			<th style="height:30px;">REBOOT</th>
			<th style="height:30px;">ON Failure</th>
			<th style="height:30px;">OFF Failure</th>
			<th style="height:30px;">REBOOT Failure</th>
			<th  style="width:150px;height:30px;">Last Requested Time</th>
			
		</tr>
		
			<%
			List<PowerStatistics> powerStatisticsList = powerInfo.getPowerStatistics();
				for (int index = 0; index < powerStatisticsList.size(); index++) {
						if (null != powerStatisticsList.get(index)) {
			%>
		
		<tr>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getOutlet()%></td>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getPowerONCount()%></td>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getPowerOFFCount()%></td>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getPowerToggleCount()%></td>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getPowerOnFailure()%></td>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getPowerOffFailure()%></td>
			<td style="border-style: solid;"><%=powerStatisticsList.get(index).getPowerToggleFailure()%></td>
			<td style="width:150px;border-style: solid;"><%=powerStatisticsList.get(index).getLastRequestedTime()%></td>
			
		</tr>

		<%
			}

				}
		%>

	</table>
 </div>
 <br/>
		
		<%

			}
			}else{
				
	%>
	
	<h3>No devices available!!</h3>
	<%			
			}
		%>

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