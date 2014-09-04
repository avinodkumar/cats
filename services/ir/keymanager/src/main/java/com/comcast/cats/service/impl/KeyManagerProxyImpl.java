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
package com.comcast.cats.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.comcast.cats.RemoteLayout;
import com.comcast.cats.keymanager.domain.IrDeviceDTO;
import com.comcast.cats.keymanager.domain.Remote;
import com.comcast.cats.keymanager.entity.IrNetBoxProEntity;
import com.comcast.cats.keymanager.service.KeyManagerService;
import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.IRHardwareEnum;
import com.comcast.cats.service.KeyManagerConstants;
import com.comcast.cats.service.KeyManagerProxy;

@Stateless
@LocalBean
@Path(KeyManagerConstants.KEYMANAGER_PATH)
public class KeyManagerProxyImpl implements KeyManagerProxy
{
    @EJB
    KeyManagerService        keyManager;

    @Override
    public List< Remote > remotes()
    {
        return keyManager.getRemoteTransferObjects();
    }

	@Override
	public List<RemoteLayout> getRemoteLayout(String remoteType) {
		return keyManager.getRemoteLayoutTrasferObjects(remoteType);
	}
    @Override
    public List< IrDeviceDTO > getIrDevices()
    {
        List< IrDeviceDTO > irDevices = new ArrayList< IrDeviceDTO >();
        List< IrNetBoxProEntity > irNetBoxEntities = keyManager.getAllIrNetBox();
        for ( IrNetBoxProEntity irNetBoxProEntity : irNetBoxEntities )
        {
            IrDeviceDTO irNetBox = new IrDeviceDTO( IRHardwareEnum.IRNETBOXPRO3,
                                                    irNetBoxProEntity.getId(),
                                                    irNetBoxProEntity.getIp(),
                                                    null,
                                                    irNetBoxProEntity.getRedratHub().getIp(),
                                                    irNetBoxProEntity.getRedratHub().getPort());
            irDevices.add( irNetBox );
        }

        return irDevices;
    }
}
