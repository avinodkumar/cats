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
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;

public class RemoteProviderDummy implements RemoteProvider
{
    protected final Logger LOGGER     = LoggerFactory.getLogger( getClass() );
    private Object         parent;
    private String         locatorStr = "gc100://localhost:4998/?port=4";
    private URI            locator;
    private String         remote     = "COMCAST_XMP";

    public RemoteProviderDummy() throws URISyntaxException
    {
        locator = new URI( locatorStr );
    }

    public URI getRemoteLocator()
    {
        return locator;
    }

    public String getRemoteType()
    {
        return remote;
    }

    public List< RemoteLayout > getValidKeys()
    {
        return null;
    }

    public boolean isAutoTuneEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean pressKey( RemoteCommand command )
    {
        LOGGER.info( "pressKey" );
        return true;
    }

    public boolean pressKeyAndHold( RemoteCommand command, Integer count )
    {
        LOGGER.info( "pressKeyAndHold" );
        return true;
    }

    public boolean pressKeys( List< RemoteCommand > commands )
    {
        LOGGER.info( "pressKeys" );
        return true;
    }

    public boolean pressKeys( List< RemoteCommand > commands, Integer delay )
    {
        LOGGER.info( "pressKeys(delay)" );// TODO Auto-generated method stub
        return true;
    }

    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
            List< Integer > delay )
    {
        LOGGER.info( "enterCustomKeySequence called" );
        return true;
    }

    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands )
    {
        LOGGER.info( "enterCustomKeySequence called with RemoteCommandSequence" );
        return true;
    }

    public void setAutoTuneEnabled( boolean autoTuneEnabled )
    {
        LOGGER.info( "setAutoTuneEnabled" );
    }

    public boolean tune( String channel )
    {
        LOGGER.info( "tune" );
        return true;
    }

    public void setParent( Object obj )
    {
        this.parent = obj;
    }

    public Object getParent()
    {
        return parent;
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
        LOGGER.info( "pressKey(delay)" );
        return true;
    }

    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {
        LOGGER.info( "pressKeys" );
        return true;
    }

    @Override
    public boolean pressKey( Integer command )
    {
        LOGGER.info( "PressKey for Ineger" );
        return true;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        LOGGER.info( "<count> times PressKey" );
        return true;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        LOGGER.info( "<count> times PressKey(delay)" );
        return true;
    }

    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        LOGGER.info( "<count> times PressKeys(delay)" );
        return true;
    }

    @Override
    public boolean tune( Integer channel )
    {
        LOGGER.info( "Integer channel tune" );
        return true;
    }

    @Override
    public boolean sendText( String arg0 )
    {
        // TODO Auto-generated method stub
        return true;
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

    }

    @Override
    public boolean performShorthandCommandSequence( String text )
    {
        return true;
    }

    @Override
    public boolean performShorthandCommandSequence( String text, Integer delay )
    {
        return true;
    }

}
