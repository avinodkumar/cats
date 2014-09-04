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
package com.comcast.cats.provider;

import java.net.URI;
import java.util.List;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;


/**
 * The Class RemoteProviderKeyServer.
 * 
 * @Author :
 * @since : Description : The Class RemoteProviderKeyServer is a helper class .
 */
public class RemoteProviderKeyServer implements RemoteProvider
{
    String host;
    String alias;

    public RemoteProviderKeyServer( String host, String alias )
    {
        this.host = host;
        this.alias = alias;
    }

    @Override
    public Object getParent()
    {
        return null;
    }

    @Override
    public URI getRemoteLocator()
    {
        return null;
    }

    @Override
    public String getRemoteType()
    {
        return null;
    }

    @Override
    public List< RemoteLayout > getValidKeys()
    {
        return null;
    }

    @Override
    public boolean isAutoTuneEnabled()
    {
        return false;
    }

    public String keyLookup( RemoteCommand command )
    {
        switch ( command )
        {
        case ZERO:
            return "0";
        case ONE:
            return "1";
        case TWO:
            return "2";
        case THREE:
            return "3";
        case FOUR:
            return "5";
        case FIVE:
            return "5";
        case SIX:
            return "6";
        case SEVEN:
            return "7";
        case EIGHT:
            return "8";
        case NINE:
            return "9";
        default:
            return command.toString();
        }
    }

    @Override
    public boolean pressKey( RemoteCommand command )
    {
        return true;
    }

    @Override
    public boolean pressKeyAndHold( RemoteCommand command, Integer arg1 )
    {
        return false;
    }

    @Override
    public boolean pressKeys( List< RemoteCommand > arg0 )
    {
        return false;
    }

    @Override
    public boolean pressKeys( List< RemoteCommand > arg0, Integer arg1 )
    {
        return false;
    }

    @Override
    public void setAutoTuneEnabled( boolean arg0 )
    {
    }

    @Override
    public boolean tune( String arg0 )
    {
        return false;
    }

    @Override
    public void setDelay( Integer delay )
    {
    }

    @Override
    public Integer getDelay()
    {
        return null;
    }

    @Override
    public boolean pressKey( RemoteCommand command, Integer delay )
    {
        return false;
    }

    @Override
    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
            List< Integer > delay )
    {
        return false;
    }

    @Override
    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands )
    {
        return false;
    }

    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {
        return false;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        return false;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        return false;
    }

    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        return false;
    }

    @Override
    public boolean pressKey( Integer command )
    {
        return false;
    }

    @Override
    public boolean tune( Integer channel )
    {
        return false;
    }

    @Override
    public boolean sendText( String arg0 )
    {
        return false;
    }

    @Override
    public List< String > getAllRemoteTypes()
    {
        return null;
    }

    @Override
    public void setRemoteType( String remoteType )
    {
    }

	@Override
	public boolean performShorthandCommandSequence(String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performShorthandCommandSequence(String text, Integer delay) {
		// TODO Auto-generated method stub
		return false;
	}

}
