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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.RemoteCommandShortcutResolver;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.keymanager.domain.Remote;
import com.comcast.cats.service.IRService;

public class RemoteProviderServiceImpl implements RemoteProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4973165057686246850L;
	private static final int DELAY_BETWEEN_KEYS=100;
	public static final Logger logger = LoggerFactory.getLogger(RemoteProvider.class);
	
	private Integer delay=0;
	private Object parent;
	private URI irPath;
	private String remoteType;
	private IRService irService;
	private boolean autoTuneEnabled = false;
	
	public RemoteProviderServiceImpl(IRService irService, URI irPath, String remoteType) {
		this.irService = irService;
		assignParams(irPath, remoteType);
	}
	
	private boolean mustDelay() {
		return (delay > 0);
	}
	
	private boolean sleepOnTrue(boolean condition) {
		if(condition && mustDelay()) {
			sleep();
		}
		return condition;
	}
	
	protected void verifyDelay(int delay) {
		if(delay < 0 || delay > MAX_DELAY) {
			throw new IllegalArgumentException("Remote delay must be 0 >= delay <= " + MAX_DELAY);
		}
	}
	
	protected void sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			logger.warn("Interrupted during sleep operation.");
		}
	}
	
	protected void sleep() {
		sleep(this.delay);
	}
	
	/**
	 * Helper method to assign parameters common for all constructors.
	 * @param irPath
	 * @param remoteType
	 */
	private void assignParams(URI irPath, String remoteType) {
		this.irPath = irPath;
		this.remoteType = remoteType;
	}
	
	@Override
	public URI getRemoteLocator() {	
		return irPath;
	}

	@Override
	public String getRemoteType() {
		return remoteType;
	}
	
	
   @Override
    public void setRemoteType(String remoteType) {
        this.remoteType = remoteType;
    }

	@Override
	public List<RemoteLayout> getValidKeys() {
		return irService.getRemoteLayout(remoteType);
	}

	@Override
	public boolean isAutoTuneEnabled() {
		return autoTuneEnabled;
	}
	
	@Override
	public void setAutoTuneEnabled(boolean autoTuneEnabled) {
		this.autoTuneEnabled = autoTuneEnabled;
	}

	@Override
	public boolean pressKey(RemoteCommand command) {
		return sleepOnTrue(irService.pressKey(irPath, remoteType, command));
	}

	@Override
	public boolean pressKey(RemoteCommand command, Integer delay) {
		verifyDelay(delay);
		boolean rtn = irService.pressKey(irPath, remoteType, command);
		sleep(delay);
		return rtn;
	}
	
	@Override
	public boolean pressKeyAndHold(RemoteCommand command, Integer count) {
		return sleepOnTrue(irService.pressKeyAndHold(irPath, remoteType, command, count));
	}

	@Override
	public boolean pressKeys(List<RemoteCommand> commands) {
		return sleepOnTrue(irService.pressKeys(irPath, remoteType, commands, delay));
	}

	@Override
	public boolean pressKeys(List<RemoteCommand> commands, Integer delay) {
		return sleepOnTrue(irService.pressKeys(irPath, remoteType, commands, delay));
	}

	@Override
	public boolean tune(String channel) {
	    verifyChannelNumber( channel );
		return sleepOnTrue(irService.tune(irPath, remoteType, channel, autoTuneEnabled, DELAY_BETWEEN_KEYS));
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}
	
	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public void setDelay(Integer delay) {
		verifyDelay(delay);
		this.delay = delay;
	}

	@Override
	public Integer getDelay() {
		return delay;
	}

	@Override
	public boolean enterCustomKeySequence(List<RemoteCommand> commands,
			List<Integer> repeatCount, List<Integer> delay) {
		return sleepOnTrue(irService.enterCustomKeySequence(irPath, remoteType, commands,repeatCount, delay));
	}
	@Override
	public boolean enterRemoteCommandSequence(List<RemoteCommandSequence> commands) {
		return sleepOnTrue(irService.enterRemoteCommandSequence(irPath, remoteType, commands));
	}
    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {        
        return pressKeys( Arrays.asList( commands ) );
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        boolean toReturn = true;
        for(int i = 0; i < count; i++)
        {
            toReturn =  pressKey( command );
            if( false == toReturn )
            {
               break;
            }
        }
        return toReturn;
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        verifyDelay(delay);
        boolean toReturn = true;
        for( int i = 0; i < count; i++)
        {
            toReturn = pressKey( command );
            if( false == toReturn )
            {
                break;
            }
            sleep( delay );
        }
        return toReturn;
    }

    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        verifyDelay(delay);
        boolean toReturn = true;
        for( int i = 0; i < count; i++ )
        {
            toReturn = pressKeys( Arrays.asList( commands ), delay );
            if(false == toReturn)
            {
                break;
            }
            sleep( delay );
        }
        return toReturn;
    }

    @Override
    public boolean pressKey( Integer command )
    {
       return pressKey( RemoteCommand.parse( command ) );        
    }

    @Override
    public boolean tune( Integer channel )
    {
        return tune( channel.toString() );
    }

    @Override
    public boolean sendText( String text )
    {
        logger.info( "Text Message is sent"  );
        return irService.sendText( irPath, remoteType, text );
    }
    
    private void verifyChannelNumber( String channel )
    {
        Pattern CHANNEL_VALIDATOR = Pattern.compile( "\\d{1,4}" );
        if( !CHANNEL_VALIDATOR.matcher( channel ).matches() )
        {
            throw new IllegalArgumentException("Invalid channel number: " + channel);
        }
    }
    
    @Override
    public List<String> getAllRemoteTypes(){
        List<String> remoteTypes = new ArrayList< String >();
        List<Remote> remotes = irService.getRemotes();
        if(remotes != null){
            for(Remote remote: remotes){
                remoteTypes.add( remote.getName() );
            }
        }
        return remoteTypes;
    }

	@Override
	public boolean performShorthandCommandSequence(String text) {
		return performShorthandCommandSequence(text,DELAY_BETWEEN_KEYS);
	}

	@Override
	public boolean performShorthandCommandSequence(String text, Integer delay) {
		boolean returnVal = false ;
        if ( !(null == text || text.isEmpty()) )
        {
            char[] chars = text.toCharArray();
            List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
            for ( char character : chars )
            {
                commands.add( RemoteCommandShortcutResolver.resolveRemoteCommand(character) );
            }
            logger.info( "sendText : commands:" + commands );
            returnVal = pressKeys( commands,delay );
        } 
        return returnVal;
	}
}
