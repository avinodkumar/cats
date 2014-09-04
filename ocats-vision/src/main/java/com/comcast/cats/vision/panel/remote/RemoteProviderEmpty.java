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
package com.comcast.cats.vision.panel.remote;

import java.net.URI;
import java.util.List;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.provider.RemoteProvider;

/**
 * Use this class to do layout development without a valid RemoteProvider implementation.
 * @author cfrede001
 *
 */
public class RemoteProviderEmpty implements RemoteProvider {

	@Override
	public URI getRemoteLocator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RemoteLayout> getValidKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAutoTuneEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pressKey(RemoteCommand command) {
		return true;
	}

	@Override
	public boolean pressKeyAndHold(RemoteCommand command, Integer count) {
		// TODO Auto-generated method stub
		return true;
		
	}

	@Override
	public boolean pressKeys(List<RemoteCommand> commands) {
		// TODO Auto-generated method stub
		return true;
		
	}

	@Override
	public boolean pressKeys(List<RemoteCommand> commands, Integer delay) {
		// TODO Auto-generated method stub
		return true;
		
	}

	@Override
	public void setAutoTuneEnabled(boolean autoTuneEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tune(String channel) {
		// TODO Auto-generated method stub
		return true;
		
	}

	@Override
	public Object getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDelay(Integer delay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getDelay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pressKey(RemoteCommand command, Integer delay) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean  enterCustomKeySequence(List<RemoteCommand> commands,
    		List<Integer> repeatCount,List<Integer> delay){
		return true;
	}

	@Override
	public boolean enterRemoteCommandSequence(List<RemoteCommandSequence> commands) {
		// TODO Auto-generated method stub
		return true;
	}

    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pressKey( Integer command )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tune( Integer channel )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean sendText( String arg0 )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List< String > getAllRemoteTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRemoteType( String remoteType )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean performShorthandCommandSequence( String arg0 )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean performShorthandCommandSequence( String arg0, Integer arg1 )
    {
        // TODO Auto-generated method stub
        return false;
    }


}
