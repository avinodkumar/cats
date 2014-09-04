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
package com.comcast.cats.web;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comcast.cats.service.KeyManager;

/**
 * Simple servlet for retrieving and displaying keymanager information to webpage.
 * @author cfrede001
 *
 */
@WebServlet(displayName="Remotes", urlPatterns="/remotes")
public class IrController extends HttpServlet {
	@EJB
	KeyManager keyManager;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3384245163808437973L;
	
	/**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException,
            IOException
    {
        doPost( request, response );
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException,
            IOException
    {
    	String refresh = (String) request.getParameter("refresh");
    	
    	/*
    	Enumeration paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
          String paramName = (String)paramNames.nextElement();
          System.out.println("<TR><TD>" + paramName + "\n<TD>");
          String[] paramValues = request.getParameterValues(paramName);
          if (paramValues.length == 1) {
            String paramValue = paramValues[0];
            if (paramValue.length() == 0)
            	 System.out.print("<I>No Value</I>");
            else
            	 System.out.print(paramValue);
          } else {
        	  System.out.println("<UL>");
            for(int i=0; i<paramValues.length; i++) {
            	 System.out.println("<LI>" + paramValues[i]);
            }
            System.out.println("</UL>");
          }
        }
    	*/
    	if(refresh != null && refresh.equalsIgnoreCase("refresh")) {
    		keyManager.refresh();
    	}
    	request.setAttribute("remotes", keyManager.getRemotes());
    	request.setAttribute("lastRefreshed", keyManager.getLastRefreshed());
        request.getRequestDispatcher( "/remotes.jsp" ).forward( request, response );
    }
}
