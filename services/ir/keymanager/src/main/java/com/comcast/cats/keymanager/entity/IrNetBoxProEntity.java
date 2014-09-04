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
@Table(name = "ir_net_box", catalog = "keymanager")
@NamedQueries( {
		@NamedQuery(name = "IrNetBoxProEntity.findAllIrNetBox", query = "SELECT netbox FROM IrNetBoxProEntity netbox"),
		@NamedQuery(name = "IrNetBoxProEntity.findIrNetBox", query = "SELECT netbox FROM IrNetBoxProEntity netbox WHERE netbox.ip =:ip")})
public class IrNetBoxProEntity implements Serializable{

	private static final long serialVersionUID = -2926316150088015474L;
	
	@Id
    @Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO )
	long id;
	
    @Column(name = "ip", unique = true, nullable = false)
	String ip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_redrathub_id", nullable = false)
    RedRatHubEntity redratHub;

	/**
	 * Default constructor.
	 */
	public IrNetBoxProEntity() {
	}

	public IrNetBoxProEntity(String ip) {
		this.ip = ip;
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
    
    public RedRatHubEntity getRedratHub() {
		return redratHub;
	}

	public void setRedratHub(RedRatHubEntity redratHub) {
		this.redratHub = redratHub;
	}

	public String toString(){
        return "IrNetBoxPro id "+id+" ip "+ip;
    }
}
