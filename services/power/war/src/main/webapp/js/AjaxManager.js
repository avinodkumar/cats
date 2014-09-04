/*
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
/**
 * Method which creates the Object of XMLHttpRequest.
 * @param requestPath The path for processing the request.
 * @return Object of XMLHttpRequest.
 */
function retrieveAjaxRequest(requestPath) {
	var ajaxRequest;
	var ex;
	var url;
	var data;
	url = getURLFromPath(requestPath);
	data = getDataFromPath(requestPath);
	/* Creating the object of XMLHttpRequest */
	try {
		ajaxRequest = new XMLHttpRequest();
	} catch(ex) {
		try {
			ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
		} catch(ex) {
			alert('Error occured: ' + ajaxRequest.statusText);
		}
	}
	/* Opening the connection and sending request */
	ajaxRequest.open("POST", url, false);
	ajaxRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	ajaxRequest.send(data);	
	return ajaxRequest;
}
/**
 * Method getting the URL.
 * @param requestPath The path for processing the request.
 * @return The URL created from the request path.
 */
function getURLFromPath(requestPath) {
	var url = '';
	if(requestPath != null && requestPath.indexOf("?") > -1) {
		url = requestPath.substring(0, requestPath.indexOf("?"));
	}
	else if(requestPath != null && requestPath.indexOf("?") <= -1) {
		url = requestPath;
	}
	return url;
}

/**
 * Method returning the data to be sent to server, from the request path.
 * @param requestPath The path for processing the request.
 * @return The data created from the request path.
 */
function getDataFromPath(requestPath) {
	var data = '';
	if(requestPath != null && requestPath.indexOf("?") > -1) {
		data = requestPath.substring(requestPath.indexOf("?") + 1);
	}
	return data;
}
