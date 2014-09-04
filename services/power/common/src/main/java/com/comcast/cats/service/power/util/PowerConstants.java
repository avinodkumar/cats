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
package com.comcast.cats.service.power.util;

public interface PowerConstants {
	/*
	 * Scheme related constants
	 */
	String WTI_SCHEME = "wti1600";

	String WTI_NPS_SCHEME = "nps1600";

	String SYNACCESS_SCHEME = "synaccess";

	String NP16_SCHEME = "np16";

	String NP16S_SCHEME = "np16s";

	/*
	 * Default power device port
	 */
	Integer DEFAULT_PORT = 23;

	/*
	 * Credentials for NPS power device.This is used for Telnet implementation
	 * of NPS power switch.
	 */
	String NPS_POWER_DEVICE_USER_NAME = "super";

	String NPS_POWER_DEVICE_PASSWORD = "super";

	String POWER_ON_SNMP_VALUE = "1";

	String POWER_OFF_SNMP_VALUE = "0";

	/**
	 * Command to switch ON the device
	 */
	String NPS_SNMP_SWITCH_ON_COMMAND = "5";
	/**
	 * Command to switch ON the device
	 */
	String NPS_SNMP_SWITCH_OFF_COMMAND = "6";
	/**
	 * Command to BOOT the device
	 */
	String NPS_SNMP_SWITCH_BOOT_COMMAND = "7";
	/**
	 * Object identifier to invoke an action on the power outlet.
	 */
	public static final String OID_PLUG_ACTION = ".1.3.6.1.4.1.2634.3.100.200.1.4.";
	/**
	 * Object identifier to get the power status of an outlet.
	 */
	public static final String OID_PLUG_STATUS = ".1.3.6.1.4.1.2634.3.100.200.1.3.";
	/*
	 * 
	 * /* Some common constants
	 */
	String LOGIN = "login: ";

	String PASSWORD = "Password: ";

	String NPS_PROMPT = "NPS> ";

	String DISCONNECTED = "Disconnected.";

	String SLASH = "/";

	String SPACE = " ";

	String STATUS_UNKNOWN = "UNKNOWN";

	/*
	 * NPS commands
	 */
	String CMD_TO_SUPPRESS_CONFIRMATION_PROMPT = ",Y";

	String PLUG_STATUS_CMD = "/S ";

	String POWER_ON = "ON";

	String POWER_OFF = "OFF";

	String POWER_BOOT = "BOOT";

	/*
	 * Used for checking and/or appending to end of string.
	 */
	String NEWLINE = "\r\n";

	/*
	 * Power status
	 */
	String POWER_STATUS_ZERO = "0";

	String POWER_STATUS_ONE = "1";

	/*
	 * Power device statistics  constants.
	 */
	String REQ_PARAM_ACTION = "REQ_PARAM_ACTION";
	String POWER_ACTION = "POWER_ACTION";
	String DELETE_DEVICE_ACTION = "DELETE_DEVICE_ACTION";
	String POWER_DEVICE_SELECTED = "POWER_DEVICE_SELECTED";
	String NUM_OF_OUTLETS = "NUM_OF_OUTLETS";
	String POWER_DEVICE_TYPE = "POWER_DEVICE_TYPE";
	String POWER_DEVICE_STATUS_FETCH = "POWER_DEVICE_STATUS_FETCH";
	String POWER_STATUS = "POWER_STATUS";
	String PATH = "PATH";
	String POWER_STATISTICS_FETCH = "POWER_STATISTICS_FETCH";
	
	/*
	 * Some power device statistics constants
	 */
	String REQUEST_PARAM = "reqParam";
	String ACTION_PARAM = "actionParam";
	String RESULT = "result";
	String DEVICEIP = "deviceIp";
	String REMOVE = "remove";
	String LIST_POWER_DEVICES_INFO = "powerDevicesInfoList";
	String OUTLET = "outlet";
	String LIST_POWER_STATISTICS = "powerStatisticsList";

	/*
	 * Power device statistics urls.
	 */
	String PATH_TO_POWERDEVICES = "/powerDevices";
	String URL_PATTERN_DEBUGGING = "/views/power/debugging.jsp";
	String PATH_TO_DEBUGGING = "/views/power/debugging";
	String URL_POWER_DEVICES_INFO = "/powerDevices.jsp";
	String PATH_TO_STATISTICS = "/views/power/statistics";
	String URL_PATTERN_STATISTICS = "/views/power/statistics.jsp";

}
