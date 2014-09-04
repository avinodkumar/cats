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
package com.comcast.cats.script.mock

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.AbstractSettop
import com.comcast.cats.RemoteCommand
import com.comcast.cats.Settop
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.VideoProvider;

class DummySettop extends AbstractSettop {
    
    private static final Logger logger = LoggerFactory.getLogger( DummySettop.class )
    
    public DummySettop (ImageCompareProvider imageProvider, VideoProvider videoProvider){
        this.setImageCompareProvider(imageProvider )
        this.setVideo(videoProvider )
        this.setLogger( logger )
        
        SettopDesc settopDesc = new SettopDesc()
        settopDesc.hostMacAddress = "DummyMac"        
        settopDesc.manufacturer = "Motorola"        
        
        this.setSettopInfo( settopDesc )
    }    
    
    @Override
    public boolean pressKey(RemoteCommand command) {
        println("pressKey($command)")
        return true
    } 
    
    @Override
    public boolean pressKey( Integer command ) {
        println("pressKey($command)")
        return true
    }
    
    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands ) {
        count.times{
            commands.each{command ->
                pressKey(command)
                sleep(delay)
            }
        }
        return true
    }
    
    @Override
    public boolean pressKey( Integer count, RemoteCommand command ) {
        count.times{
            pressKey(command)
        }
        return true
    }
    
    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay ) {
        count.times{
            pressKey(command, delay)
        }
        return true
    }
    
    @Override
    public boolean pressKey( RemoteCommand command, Integer delay ) {
        pressKey(command)
        sleep(delay)
        return true
    }
    
    @Override
    public boolean pressKey( RemoteCommand[] commands ) {
        commands.each{command ->
            pressKey(command)
        }
        return true
    }

    @Override
    public boolean tune( Integer channel ) {
        tune(channel.toString())
        return true
    }
    
    @Override
    public boolean tune(String channel) {
        println("tune($channel)")
        return true
    }

    @Override
    public void powerOn() {
        println("Power On")
    }

    @Override
    public void powerOff() {
        println("Power Off")
    }

    @Override
    public void reboot() {
        println("Reboot")
    }
    
    @Override
    public boolean pressKeyAndHold(RemoteCommand command, Integer count) {
        println("pressHold $command : $count");
        return true
    }
}
