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

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class PowerInfo.
 * 
 * @Author : Deepa V S (deepavs@tataelxsi.co.in)
 * @since : 14th Dec 2012
 * Description : The Class PowerInfo is to store power device information .
 */

@XmlRootElement
public class PowerInfo implements Serializable {

	private static final long serialVersionUID = 6164030050030751071L;
	String type;
	String ip;
	int port;
	int numOfOutlets;
	
	List<PowerStatistics> powerStatistics = new ArrayList<PowerStatistics>();
			
	public PowerInfo(){
		
	}
	
	public PowerInfo(String type, String ip, int port,
			ArrayList<PowerStatistics> powerStatistics) {
		this.type = type;
		this.ip = ip;
		this.port = port;
		this.powerStatistics = powerStatistics;
	}
	
	@XmlAttribute(name="outlets")
	public int getNumOfOutlets( ) {
		return numOfOutlets;
	}

	public void setNumOfOutlets(int numOfOutlets) {
		this.numOfOutlets = numOfOutlets;
	}

	@XmlAttribute(name="type")
	public String getType() {
		return type;
	} 
	
	public void setType(String type) {
		this.type = type;
	} 
	
	@XmlAttribute(name="IPaddress")
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@XmlAttribute(name="port")
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@XmlAttribute(name="ON")
	public int getTotalOn() {
	    return getTotal("ON");
	}
	
	@XmlAttribute(name="OFF")
	public int getTotalOff() {
		return getTotal("OFF");
	}
	
	@XmlAttribute(name="BOOT")
	public int getTotalReboot() {
		return getTotal("BOOT");
	}
	
	@XmlAttribute(name="FailedON")
	public int getTotalOnFailures() {
		return getTotal("FAILED-ON");
	}
	
	@XmlAttribute(name="FailedOFF")
	public int getTotalOffFailures(){
		return getTotal("FAILED-OFF");
	}
	
	@XmlAttribute(name="FailedBOOT") 
	public int getTotalRebootFailures(){
		return getTotal("FAILED-BOOT");
		
	}
	
	private int getTotal(String cmd){
		int total = 0;
		for(PowerStatistics powerStats : powerStatistics){
			if(cmd.equalsIgnoreCase("ON")){
				total += powerStats.getPowerONCount();
		    }
			else if(cmd.equalsIgnoreCase("OFF")){
				total += powerStats.getPowerOFFCount();
			}
			else if(cmd.equalsIgnoreCase("BOOT")){
				total += powerStats.getPowerToggleCount();
			}
			else if(cmd.equalsIgnoreCase("FAILED-ON")){
				total += powerStats.getPowerOnFailure();
			}
			else if(cmd.equalsIgnoreCase("FAILED-OFF")){
				total += powerStats.getPowerOffFailure();
			}
			else if (cmd.equalsIgnoreCase("FAILED-BOOT")){
				total += powerStats.getPowerToggleFailure();
			}					
		}
		return total;
	}
	@XmlElementWrapper(name="PowerStatsList")
	@XmlElements({
		@XmlElement(name="PowerStats", type=PowerStatistics.class)
	})
	public List<PowerStatistics> getPowerStatistics() {
		return powerStatistics;
	}

	public void setPowerStatistics(ArrayList<PowerStatistics> powerStatistics) {
		this.powerStatistics = powerStatistics;
	}
	
	public PowerStatistics getPowerStatisticsAt(int outlet){
		for(PowerStatistics powerStats : powerStatistics){
			if(powerStats.getOutlet() == outlet){
				return powerStats;
			}
		}
		return null;
	}
	
	public void setPowerStatisticsAt(PowerStatistics powerStats, int outlet){
		PowerStatistics powerSts = getPowerStatisticsAt(outlet); 
		if(powerSts != null){
			powerStatistics.remove(powerSts);
		}
		powerStatistics.add(powerStats);
	}
	
	@Override
	public String toString(){
		return "IP = "+ ip + " Port = "+ port + " Type = "+ type 
		       + " Num Of Outlets = " + numOfOutlets + " " 
		       + " Power Statistics ArrayList = " + powerStatistics.toString();
	}
}
