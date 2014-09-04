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
package com.comcast.cats.service;

import static com.comcast.cats.service.WebServiceReturnEnum.*;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * A base class for all responses returned from CAT-based web services. This is being used to address QTPs lack of exception
 * handling so that a tester can look at a result and decide what action to take next. The data field is generic, and could contain
 * either a definition of the result, or actual data retrieved from the service call.
 *
 * @author cfrede001
 */
public class WebServiceReturn implements Serializable {
	/**
     * Generated Serial ID.
     */
    private static final long serialVersionUID = 8617437599896781546L;

    /** A result code returned from the web service call. */
    private WebServiceReturnEnum result;

    /** A human-readable message corresponding to the result. */
    private String message;

    public WebServiceReturn()
    {
		result = SUCCESS;
    }

    public WebServiceReturn(final WebServiceReturnEnum result)
    {
        this.result = result;
    }

    /**
     * Constructs an instance of <code>CATSWebServiceReturnMessage</code> with the specified result code and data.
     * @param resultCode
     *            The result code associated with this <code>CATSWebServiceReturnMessage</code>.
     * @param data
     *            The data associated with this <code>CATSWebServiceReturnMessage</code>.
     */
    public WebServiceReturn(final WebServiceReturnEnum result, final String message)
    {
        this.result = result;
        this.message = message;
    }

    /**
     * @return The value of <code>resultCode</code>.
     */
	@XmlElement(name = "resultCode")
    public WebServiceReturnEnum getResult()
    {
        return result;
    }

    /**
     * @param result
     *            The value of <code>result</code> to set.
     */
    public void setResult(WebServiceReturnEnum result)
    {
        this.result = result;
    }

    /**
     * @return The value of <code>message</code>.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message
     *            The value of <code>message</code> to set.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

}
