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
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.comcast.cats.service.SettopToken"%>
<%@page import="com.comcast.cats.service.settop.SettopCatalogHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    //response.setHeader("Cache-Control","no-cache");
    //response.setHeader("Pragma","no-cache");
    //response.setDateHeader ("Expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@page
	import="com.comcast.cats.service.util.SettopApplicationConfigUtil"%>
<%@page import="java.util.Map"%><html>

<head>
<title>CATS Settop Service Application Settings</title>
<link rel="shortcut icon" type="image/x-icon"
	href="images/comcast-favicon.ico">
<link href="css/trace.css" rel="stylesheet" type="text/css"
	media="screen">
</head>

<body>
	<h2>Application Configurations</h2>
	<table border="1" cellspacing="0" cellpadding="5">

		<%
		    Map< String, String > settings = SettopApplicationConfigUtil.getSettings();
		   out.print( "<tr><td valign='middle' align='left' bgcolor='#47A3FF'>Property"
	                + "</td><td valign='middle' align='left' bgcolor='#47A3FF'>Value</td></tr>" );
		    for ( String key : settings.keySet() )
		    {
		        out.print( "<tr><td valign='middle' align='left'>" + key + "</td><td valign='middle' align='left'>"
		                + settings.get( key ) + "</td></tr>" );
		    }
		%>
	</table>
	<br />
	<h2>Token Viewer</h2>
	<table border="1" cellspacing="0" cellpadding="5">
		<%
		    Map< String, SettopToken > tokenMap = SettopCatalogHelper.getSettopTokenMap();
		    if ( !tokenMap.isEmpty() )
		    {
		        out.print( "<tr><td valign='middle' align='left' bgcolor='#47A3FF'>[MacId][AuthToken]"
		                + "</td><td valign='middle' align='left' bgcolor='#47A3FF'>SettopToken</td></tr>" );
		        Iterator< Entry< String, SettopToken > > iterator = tokenMap.entrySet().iterator();
		        Entry< String, SettopToken > entry;
		        while ( iterator.hasNext() )
		        {
		            entry = iterator.next();
		            out.print( "<tr><td valign='middle' align='left'>" + entry.getKey()
		                    + "</td><td valign='middle' align='left'>" + entry.getValue() + "</td></tr>" );
		        }
		    }
		    else
		    {
		        out.print( "No token found" );
		    }
		%>
	</table>
	<br />
	<h2>Error Viewer</h2>
	<table border="1" cellspacing="0" cellpadding="5">
		<%
		    Map< String, String > errorMap = SettopCatalogHelper.getSettopErrorMap();
		    if ( !errorMap.isEmpty() )
		    {
		        out.print( "<tr><td valign='middle' align='left' bgcolor='#47A3FF'>User&nbsp;Token"
		                + "</td><td valign='middle' align='left' bgcolor='#47A3FF'>Last&nbsp;Error&nbsp;Message</td></tr>" );
		        Iterator< Entry< String, String > > iterator = errorMap.entrySet().iterator();
		        Entry< String, String > entry;
		        while ( iterator.hasNext() )
		        {
		            entry = iterator.next();
		            out.print( "<tr><td valign='middle' align='left'>" + entry.getKey()
		                    + "</td><td valign='middle' align='left'>" + entry.getValue() + "</td></tr>" );
		        }
		    }
		    else
		    {
		        out.print( "No error found" );
		    }
		%>
	</table>
</body>
</html>