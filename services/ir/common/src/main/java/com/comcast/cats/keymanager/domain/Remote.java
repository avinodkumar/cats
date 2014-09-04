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
package com.comcast.cats.keymanager.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Remote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7392337586598200113L;
	
	
	String name;
	private Set<Key> keys = new HashSet<Key>(0);
	
	public Remote() {
		
	}

	public Remote(String name) {
		super();
		this.name = name;
	}
	
	public Remote(String name, Set<Key> keys) {
		super();
		this.name = name;
		this.keys = keys;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public Set<Key> getKeys() {
		return keys;
	}

	public void setKeys(Set<Key> keys) {
		this.keys = keys;
	}
	@Override
	public String toString() {
		return "Remote [name=" + name + ", keys=" + keys + "]";
	}
}
