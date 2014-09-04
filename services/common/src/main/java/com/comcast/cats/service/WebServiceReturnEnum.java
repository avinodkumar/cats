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

/**
 * Generic Enum class for CATS Return messages.
 * enough room (100) for future response codes.
 *		Failure return codes should be negative.
 *		Success return codes should be positive.
 * Example:
 *		Error codes for Allocation might start at -10 and go to -19.
 *		Success codes for Allocation would then start at 10 and go to 19.
 * @author cfrede001
 */
public enum WebServiceReturnEnum {
	SUCCESS(0, "Success"),
	FAILURE(-1, "General Failure occurred");


	private final int returnCode;
    private final String message;

    WebServiceReturnEnum(int returnCode, String message) {
		this.returnCode = returnCode;
		this.message = message;
	}

	int getReturnCode() {
		return returnCode;
	}

	String getMessage() {
		return message;
	}
}
