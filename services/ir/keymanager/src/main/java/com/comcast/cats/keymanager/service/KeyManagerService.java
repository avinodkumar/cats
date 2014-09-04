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
package com.comcast.cats.keymanager.service;

import java.util.List;

import javax.ejb.Remote;

import com.comcast.cats.keymanager.entity.IrNetBoxProEntity;
import com.comcast.cats.keymanager.entity.KeyCodeFormat;
import com.comcast.cats.keymanager.entity.KeyLayout;
import com.comcast.cats.keymanager.entity.KeyCodes;
import com.comcast.cats.keymanager.entity.RedRatHubEntity;
import com.comcast.cats.keymanager.entity.RemoteType;

/**
 * Service definition for KeyManager.
 * @author thusai000
 *
 */
@Remote
public interface KeyManagerService {

	public void addKeyCode(KeyCodes kCode);
	public void addKeyLayout(KeyLayout layout);
	public void addRemoteType(RemoteType remote);
	public void addKeyCodeFormat(KeyCodeFormat kcFormat);
	
	public KeyCodes updateKeyCode(KeyCodes kCode);
	public KeyLayout updateKeyLayout(KeyLayout layout);
	public RemoteType updateRemoteType(RemoteType remote);
	public KeyCodeFormat updateKeyCodeFormat(KeyCodeFormat kcFormat);
	
	public void deleteKeyCode(KeyCodes kCode);
	public void deleteKeyLayout(KeyLayout layout);
	public void deleteRemoteType(RemoteType remote);
	public void deleteKeyCodeFormat(KeyCodeFormat kcFormat);
	
	public List<RemoteType> getAvailableRemotes();
	public RemoteType getRemote(String remoteType);
	public List<KeyCodeFormat> getAvailableKeyCodeFormats();
	public KeyCodeFormat getFormat(String format);
	public List<KeyCodes> getAvailableKeys(String remoteType, String formatName);
	public KeyCodes getKey(String remoteType, String format, String name);
	public KeyLayout getLayout(String remoteType, String format, String name);
	public List<KeyLayout> getLayouts(String remoteType, String format);
	
	public List<com.comcast.cats.keymanager.domain.Remote> getRemoteTransferObjects();
	public List<com.comcast.cats.RemoteLayout> getRemoteLayoutTrasferObjects(String remoteType);
    public void addIrNetBoxDevice( IrNetBoxProEntity irNetBox );
    public  List<IrNetBoxProEntity> getAllIrNetBox();
    public IrNetBoxProEntity getIrNetBox( String selectedIrNetBoxDeviceIp );
    public void updateIrNetBoxDevice( IrNetBoxProEntity selectedIrNetBoxProEntity );
    public void deleteIrNetBoxDevice( IrNetBoxProEntity toDeleteIrNetBoxDevice );
	public RedRatHubEntity getRedRatHub(String redratHubIp);
	public void addRedRatHub(RedRatHubEntity rrHub);
	public List<RedRatHubEntity> getAllRedRatHubs();
	public void updateRedRatHub(RedRatHubEntity selectedRRHub);
	public void deleteRedRatHub(RedRatHubEntity toDeleteRRHub);
}
