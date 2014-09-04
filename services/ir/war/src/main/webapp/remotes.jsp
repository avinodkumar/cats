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
    pageEncoding="ISO-8859-1"
    %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.comcast.cats.keymanager.domain.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="resources/css/screen.css" type="text/css">
<title>Remote</title>
</head>
<div id="page">
	<div id="header">
		<h1>
			<img align="left" src="images/logo.png" />IR Proxy Manager
		</h1>
	</div>

	<div id="container">
		<div id="content">
			<body>
				<h1>Remotes</h1>
			<body>
				<form method="POST">
					<h4>Last Refreshed: <c:out value="${lastRefreshed}" /></h4>
					<button type="submit" name="refresh" value="refresh">Refresh</button>
				</form>
				<c:forEach var="remote" items="${remotes}">
					<h2>
						<c:out value="${remote.name}" />
					</h2>
					<table class="dataTable">
						<tr>
							<th>Key</th>
							<th>Code</th>
						</tr>
						<c:forEach var="key" items="${remote.keys}">
							<tr>
								<td><c:out value="${key.name}" /></td>
								<td><c:out value="${key.value}" /></td>
							</tr>
						</c:forEach>
					</table>
				</c:forEach>

				<div id="footerBanner" align="right">
					<p>OCATS</p>
				</div>
				<br />
		</div>
	</div>
</div>
</body>
</html>