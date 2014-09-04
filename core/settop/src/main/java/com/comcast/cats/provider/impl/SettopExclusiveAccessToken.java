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
package com.comcast.cats.provider.impl;

import java.io.Serializable;
import java.util.Date;

import com.comcast.cats.Settop;

/**
 * Hold allocation based information for the exclusive access purposes.
 * @author cfrede001
 */
public class SettopExclusiveAccessToken implements Serializable {
	private static final long serialVersionUID = 6670738215334882461L;
	
	private String authToken;
	private Settop settop;
	private String allocationId;
	private Integer numberOfUpdates=0;
	private Date lastUpdate;
	private Date created;
	private Integer retries=0;
	private boolean reaquire=false;
	
	public SettopExclusiveAccessToken(String authToken, Settop settop) {
		this.authToken = authToken;
		this.settop = settop;
	}
	
	public SettopExclusiveAccessToken(String authToken, Settop settop,
			String allocationId) {
		this(authToken, settop);
		this.allocationId = allocationId;
		this.created = new Date();
		this.lastUpdate = new Date();
	}
	
	public SettopExclusiveAccessToken(String authToken, Settop settop,
			String allocationId, boolean reaquire) {
		this(authToken, settop, allocationId);
		this.reaquire = reaquire;
	}
	
	/**
	 * To get the Authorized token.
	 * 
	 * @return String
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * To set the Authorized token.
	 * 
	 * @param authToken
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	/**
	 * To get the settop id.
	 * 
	 * @return String
	 */
	public String getSettopId() {
		return settop.getId();
	}
	
	/**
	 * To get the Allocation id.
	 * 
	 * @return String
	 */
	public String getAllocationId() {
		return allocationId;
	}

	/**
	 * To set the allocation Id.
	 * 
	 * @param allocationId
	 */
	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	/**
	 * To get the no of updates done.
	 * 
	 * @return Integer
	 */
	public Integer getNumberOfUpdates() {
		return numberOfUpdates;
	}

	/**
	 * To set the no of updates done.
	 * 
	 * @param numberOfUpdates
	 */
	public void setNumberOfUpdates(Integer numberOfUpdates) {
		this.numberOfUpdates = numberOfUpdates;
	}

	/**
	 * To update.
	 * 
	 */
	public void update() {
		this.incrNumberOfUpdates();
		this.updateLast();
	}
	
	/**
	 * To increment update count.
	 * 
	 */
	public void incrNumberOfUpdates() {
		numberOfUpdates++;
	}
	
	/**
	 * To get the last updated date.
	 * 
	 * @return Date
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * To set last update date.
	 * 
	 * @param lastUpdate
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * To set last update date.
	 * 
	 */
	public void updateLast() {
		this.lastUpdate = new Date();
	}
	
	/**
	 * To get the created date.
	 * 
	 * @return Date
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * To set created date.
	 * 
	 * @param created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * To get the retry count.
	 * 
	 * @return Integer
	 */
	public Integer getRetries() {
		synchronized(this) {
			return this.retries;
		}
	}

	/**
	 * To set retry count.
	 * 
	 * @param retries
	 */
	public void setRetries(Integer retries) {
		synchronized(this) {
			this.retries = retries;
		}
	}
	
	/**
	 * To clear retry count.
	 * 
	 */
	public void clearRetries() {
		synchronized(this) {
			this.retries = 0;
		}
	}
	
	/**
	 * To increment retry count.
	 * 
	 */
	public  void incrementRetries() {
		synchronized(this) {
			this.retries ++;
		}
	}
	
	/**
	 * To get settop.
	 * 
	 * @return Settop
	 */
	public Settop getSettop() {
		return settop;
	}
	
	/**
	 * To get reacquire status.
	 * 
	 * @return boolean
	 */
	public boolean isReaquire() {
		return reaquire;
	}

	/**
	 * To set reacquired status.
	 * 
	 * @param reaquire
	 */
	public void setReaquire(boolean reaquire) {
		this.reaquire = reaquire;
	}

	@Override
	public String toString() {
		return "SettopExclusiveAccessToken [allocationId=" + allocationId
				+ ", authToken=" + authToken + ", reaquire= " + reaquire + ", created=" + created
				+ ", lastUpdate=" + lastUpdate + ", numberOfUpdates="
				+ numberOfUpdates + ", settopId=" + getSettopId() + " retries=" + retries + "]";
	}
}
