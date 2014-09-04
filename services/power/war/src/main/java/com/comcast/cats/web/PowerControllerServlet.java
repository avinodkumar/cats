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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerStatistics;
import com.comcast.cats.service.power.util.PowerConstants;

/**
 * Simple servlet for retrieving and displaying power devices information to
 * webpage.
 * 
 * @author deepavs
 * 
 */
@WebServlet(displayName = "PowerDevices", urlPatterns = {
		PowerConstants.PATH_TO_POWERDEVICES, PowerConstants.PATH_TO_STATISTICS,
		PowerConstants.PATH_TO_DEBUGGING })
public class PowerControllerServlet extends HttpServlet {

	private static final long serialVersionUID = -7422165911888205895L;

	@EJB
	PowerService powerService;

	private static Logger LOGGER = LoggerFactory
			.getLogger(PowerControllerServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		request.getSession().setAttribute(
				PowerConstants.LIST_POWER_DEVICES_INFO,
				powerService.getAllPowerDevicesInfo());

		if (request.getRequestURL().toString()
				.contains(PowerConstants.PATH_TO_POWERDEVICES)) {
				request.getSession().setAttribute(
				PowerConstants.LIST_POWER_DEVICES_INFO,
				powerService.getAllPowerDevicesInfo());
			request.getRequestDispatcher(PowerConstants.URL_POWER_DEVICES_INFO)
					.forward(request, response);
		} else if (request.getRequestURL().toString()
				.contains(PowerConstants.PATH_TO_STATISTICS)) {
				request.getSession().setAttribute(
				PowerConstants.LIST_POWER_DEVICES_INFO,
				powerService.getAllPowerDevicesInfo());
			request.getRequestDispatcher(PowerConstants.URL_PATTERN_STATISTICS)
					.forward(request, response);
		} else if (request.getRequestURL().toString()
				.contains(PowerConstants.PATH_TO_DEBUGGING)) {
				request.getSession().setAttribute(
				PowerConstants.LIST_POWER_DEVICES_INFO,
				powerService.getAllPowerDevicesInfo());
			request.getRequestDispatcher(PowerConstants.URL_PATTERN_DEBUGGING)
					.forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		String action = request.getParameter(PowerConstants.REQ_PARAM_ACTION);
		/*
		 * Power device execution of power on, off and reboot.
		 */
		if (PowerConstants.POWER_ACTION.equals(action)) {

			String reqParam = request
					.getParameter(PowerConstants.REQUEST_PARAM);
			String actionParam = request
					.getParameter(PowerConstants.ACTION_PARAM);
			URI path = null;
			boolean result = false;
			String message = null;
			try {
				path = new URI(reqParam);
				if (actionParam.contains(PowerConstants.POWER_ON)) {
					result = powerService.hardPowerOn(path);
				} else if (actionParam.contains(PowerConstants.POWER_OFF)) {
					result = powerService.hardPowerOff(path);
				} else if (actionParam.contains(PowerConstants.POWER_BOOT)) {
					result = powerService.hardPowerToggle(path);
				}

			} catch (URISyntaxException e) {
				LOGGER.error(e.toString());
			} finally {
				if(result){
					message = "Command executed successfully!!";
				}else{
					message = "Command execution failed!!";
				}
				//request.setAttribute(PowerConstants.RESULT, result);
				request.setAttribute(PowerConstants.RESULT, message);
				request.getRequestDispatcher(
						PowerConstants.URL_PATTERN_DEBUGGING).forward(request,
						response);
			}

		} else if (PowerConstants.DELETE_DEVICE_ACTION.equals(action)) {
			/*
			 * delete a power device based on selection
			 */
			String deviceIp = (String) request
					.getParameter(PowerConstants.DEVICEIP);
			powerService.removePowerDevice(deviceIp);
			request.getSession().setAttribute(
					PowerConstants.LIST_POWER_DEVICES_INFO,
					powerService.getAllPowerDevicesInfo());
			request.getRequestDispatcher(PowerConstants.URL_POWER_DEVICES_INFO)
					.forward(request, response);

		} else if (PowerConstants.POWER_DEVICE_SELECTED.equals(action)) {
			/*
			 * Get the number of outlets of a power device.
			 */
			String deviceIp = (String) request
					.getParameter(PowerConstants.DEVICEIP);
			PowerInfo powerInfo = getPowerInfo(deviceIp);
			Integer numOfOutlets = new Integer(powerInfo.getNumOfOutlets());
			String deviceType = powerInfo.getType();
			request.getSession().setAttribute(PowerConstants.POWER_DEVICE_TYPE,
					deviceType);

			request.getSession().setAttribute(PowerConstants.NUM_OF_OUTLETS,
					numOfOutlets);
			request.getRequestDispatcher(PowerConstants.URL_PATTERN_DEBUGGING)
					.forward(request, response);

		} else if (PowerConstants.POWER_DEVICE_STATUS_FETCH.equals(action)) {
			/*
			 * Fetch the current status of a power device.
			 */
			String reqParam = request
					.getParameter(PowerConstants.REQUEST_PARAM);
			URI path = null;
			String status = null;
			try {
				path = new URI(reqParam);
				status = powerService.powerStatus(path);

			} catch (URISyntaxException e) {
				LOGGER.error(e.toString());
			} finally {
				request.getSession().setAttribute(PowerConstants.POWER_STATUS,
						status);
				request.getRequestDispatcher(
						PowerConstants.URL_PATTERN_DEBUGGING).forward(request,
						response);
			}
		} else if (PowerConstants.POWER_STATISTICS_FETCH.equals(action)) {
			/*
			 * Get the power statistics of a device.
			 */
			String deviceIp = (String) request
					.getParameter(PowerConstants.DEVICEIP);
			List<PowerStatistics> powerStatisticsList = powerService
					.getPowerStatisticsPerDevice(deviceIp);
			request.getSession().setAttribute(
					PowerConstants.LIST_POWER_STATISTICS, powerStatisticsList);
			request.getSession().setAttribute(
					PowerConstants.LIST_POWER_DEVICES_INFO,
					powerService.getAllPowerDevicesInfo());
			request.getSession()
					.setAttribute(PowerConstants.DEVICEIP, deviceIp);
			request.getRequestDispatcher(PowerConstants.URL_PATTERN_STATISTICS)
					.forward(request, response);
		}

	}

	/**
	 * Get the power information for a given power device.
	 * 
	 * @param ip
	 * @return
	 */
	private PowerInfo getPowerInfo(final String ip) {
		List<PowerInfo> powerInfoList = powerService.getAllPowerDevicesInfo();
		for (PowerInfo powerInfo : powerInfoList) {
			if (powerInfo.getIp().contains(ip)) {
				return powerInfo;
			}
		}
		return null;
	}

}
