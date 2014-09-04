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
package com.comcast.cats.keymanager.web;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.comcast.cats.keymanager.entity.RemoteType;
import com.comcast.cats.keymanager.service.KeyManagerService;

/**
 * @author thusai000
 * 
 */
public class RemoteTypeConverter implements Converter {

	public RemoteTypeConverter() {

	}

	

	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object obj) {
		System.out.println("Returning string "
				+ ((RemoteType) obj).getRemoteTypeName());
		return ((RemoteType) obj).getRemoteTypeName();
	}

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		// Convert the unique String representation of Foo to the actual Foo
		// object.
		System.out.println("Returning " + value);
		return null;
	}

	/*
	 * public Object getAsObject( FacesContext facesContext, UIComponent
	 * uIComponent, String str) throws ConverterException { FacesContext fc =
	 * FacesContext.getCurrentInstance(); ServletContext sc =
	 * (ServletContext)fc.getExternalContext().getContext();
	 * System.out.println(str); // TODO: Retrieve list of remotes Remotes rt =
	 * (Remotes)sc.getAttribute( "remotes" );
	 * 
	 * RemoteType remote = null; try { // TODO: set remote remote =
	 * rt.getRemoteType(str); } catch( NumberFormatException nfe ) {
	 * FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_ERROR,
	 * "Unknown Remote", "The value supplied was not a remote type" ); throw new
	 * ConverterException( message ); }
	 * 
	 * if( remote == null ) { FacesMessage message = new FacesMessage(
	 * FacesMessage.SEVERITY_ERROR, "Unknown Remote",
	 * "The remote value chosen was not recognized" ); throw new
	 * ConverterException( message ); }
	 * 
	 * return remote; }
	 */

}