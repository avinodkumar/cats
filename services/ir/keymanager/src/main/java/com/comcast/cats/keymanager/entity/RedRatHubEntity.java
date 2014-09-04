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
package com.comcast.cats.keymanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * IrNetBoxPro entity.
 * 
 * @author skurup00c
 * 
 */
@Entity
@Table(name = "redrat_hub", catalog = "keymanager")
@NamedQueries( {
		@NamedQuery(name = "RedRatHubEntity.findAllRedRatHubs", query = "SELECT hub FROM RedRatHubEntity hub"),
		@NamedQuery(name = "RedRatHubEntity.findRedRatHub", query = "SELECT hub FROM RedRatHubEntity hub WHERE hub.ip =:ip")})
public class RedRatHubEntity implements Serializable{

	private static final long serialVersionUID = -2926316150088015474L;
	
	@Id
    @Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO )
	long id;
	
    @Column(name = "ip", unique = true, nullable = false)
	String ip;
    
    @Column(name = "port", nullable = false)
    Integer port;
    
	/**
	 * Default constructor.
	 */
	public RedRatHubEntity() {
	}

	public RedRatHubEntity(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
	}

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp( String ip )
    {
        this.ip = ip;
    }
    
    public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String toString(){
        return "RedRatHub id "+id+" ip "+ip;
    }
}
