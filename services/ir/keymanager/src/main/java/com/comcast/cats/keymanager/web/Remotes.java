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
/**
 * 
 */
package com.comcast.cats.keymanager.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.comcast.cats.keymanager.entity.RemoteType;


/**
 * @author thusai000
 *
 */
public class Remotes {

    private List<SelectItem> remoteItems = new LinkedList<SelectItem>();
    private Map<String, RemoteType> Remotes = new HashMap<String, RemoteType>();

    /** Creates a new instance of Remotes */
    public Remotes() {}
  

    public RemoteType getRemoteType( String key ) {
        return (RemoteType)Remotes.get( key );
    }

    public List<SelectItem> getRemoteTypeItems() {
        return remoteItems;
    }

    public void setRemoteTypeItems(List<SelectItem> remoteTypeItems) {
        this.remoteItems = remoteTypeItems;
    }
}