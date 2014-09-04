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
package com.comcast.cats.vision.configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Class that stores the CATS Vision information.  This will grow as more
 * things are added to CATS Vision.
 * @author cfrede001
 */
@XmlRootElement(name = "CATSVisionInfo")
public class VisionInfo {
	Date lastLaunched;
	//Timestamp lastClosed;
	String lastUserName;

	List<SettopAccess> accessList = new ArrayList<SettopAccess>();

	@XmlElement(name = "SettopAccessList")
	public List<SettopAccess> getAccessList() {
		return accessList;
	}

	public void setAccessList(List<SettopAccess> accessList) {
		this.accessList = accessList;
	}

	@XmlAttribute(name = "LastLaunched")
	public Date getLastLaunched() {
		return lastLaunched;
	}

	public void setLastLaunched(Date lastLaunched) {
		this.lastLaunched = lastLaunched;
	}

	/*
	@XmlAttribute(name = "lastClosed")
	public Timestamp getLastClosed() {
		return lastClosed;
	}

	public void setLastClosed(Timestamp lastClosed) {
		this.lastClosed = lastClosed;
	}

	@XmlAttribute(name = "lastLaunched")
	public Timestamp getLastLaunched() {
		return lastLaunched;
	}

	public void setLastLaunched(Timestamp lastLaunched) {
		this.lastLaunched = lastLaunched;
	}
	*/
	@XmlAttribute(name = "lastUserName")
	public String getLastUserName() {
		return lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}

	/*
	public void putSettopAccess(SettopAccess access) {
		accessList.put(access.getMac(), access);
	}

	public void removeSettopAccess(SettopAccess access) {
		accessList.remove(access.getMac());
	}

	public String getAliasForMac(String mac) {
		SettopAccess access = accessList.get(mac);
		if(access != null) {
			return access.getAlias();
		}
		return "";
	}
	 * 
	 */
}
