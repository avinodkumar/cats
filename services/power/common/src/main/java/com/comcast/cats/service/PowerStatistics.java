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

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class PowerStatistics.
 * 
 * @Author : Deepa
 * @since : 13th Dec 2012
 * Description : The Class PowerStatistics shows the statistics for power ON, OFF, REBBOT occurred on the power devices.
 */
@XmlRootElement
public class PowerStatistics implements Serializable {
	
	private int powerONCount;
	private int powerOFFCount;
	private int powerToggleCount;
	
	private int powerOnFailure;
	private int powerOffFailure;
	private int powerToggleFailure;
	
	private int outlet;
	private Date lastRequestedTime;
	
	public PowerStatistics(){
		
	}
	
    public PowerStatistics(int outlet) {
		this.outlet = outlet;
	}

	@XmlAttribute
	public int getPowerOnFailure() {
		return powerOnFailure;
	}

	@XmlAttribute
	public int getPowerOffFailure() {
		return powerOffFailure;
	}

	@XmlAttribute
	public int getPowerToggleFailure() {
		return powerToggleFailure;
	}

	@XmlAttribute
	public int getPowerONCount() {
		return powerONCount;
	}

	@XmlAttribute
	public int getPowerOFFCount() {
		return powerOFFCount;
	}

	@XmlAttribute
	public int getPowerToggleCount() {
		return powerToggleCount;
	}

	public void incrementPowerOnCount(){
		powerONCount++;
	}
	
	public void incrementPowerOffCount(){
		powerOFFCount++;
	}
	
	public void incrementPowerToggleCount(){
		powerToggleCount++;
	}
	
	public void incrementPowerOnFailure(){
		powerOnFailure++;
	}
	
	public void incrementPowerOffFailure(){
		powerOffFailure++;
	}
	
	public void incrementPowerToggleFailure(){
		powerToggleFailure++;
	}
	
	@XmlAttribute
	public Date getLastRequestedTime(){
		return lastRequestedTime;
	}
	
	public void setLastRequestedTime(Date requestTime){
		lastRequestedTime = requestTime;
	}
	
	@XmlAttribute
	public int getOutlet(){
		return outlet;
	}
	
	@Override
	public String toString(){
		return " Outlet# = "+ outlet + " ON: " + powerONCount + "/" + powerOnFailure 
		       + " OFF: " + powerOFFCount + "/" + powerOffFailure + " REBOOT: " 
		       + powerToggleCount + "/" + powerToggleFailure;   
	}
}
