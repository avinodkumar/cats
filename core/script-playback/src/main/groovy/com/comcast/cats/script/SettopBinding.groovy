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
package com.comcast.cats.script

import java.awt.image.BufferedImage;
import java.lang.ref.ReferenceQueue.Null;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import groovy.lang.Binding

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.Settop
import com.comcast.cats.decorator.SettopDiagnostic

class SettopBinding extends Binding {

    PausableSettop settop

    public SettopBinding(PausableSettop settop){
        this.settop=settop
        createBindings()
    }

    def createBindings = {
        createRemoteCommandBindings()

        /**
         * Variable exposes the settop object. This can be used to call any operation/properties defined on settop object.
         * Eg: settop.getManufacturer()
         */
        setVariable("settop", settop )

        this.press = settop.&pressKey
        this.pressHold = settop.&pressKeyAndHold
        this.tune = settop.&tune
        this.POWER_ON = settop.&powerOn
        this.POWER_OFF = settop.&powerOff
        this.REBOOT = settop.&reboot

        this.waitForImageRegion =  settop.&waitForImageRegion
        this.waitForFullImage = settop.&waitForFullImage
        this.waitForRegionOnTargetRegion = settop.&waitForRegionOnTargetRegion
        this.saveImageRegion = settop.&saveImageRegion
        this.saveSearchRegion = settop.&saveSearchRegion

        this.captureScreen = settop.&saveVideoImage

        this.log = settop.&logMessage
        this.showDiagMenu = settop.&diagScreenBinding
        
        this.startVideoRecording = settop.&startVideoRecording
        this.stopVideoRecording = settop.&stopVideoRecording
        this.getRecordingInfo = settop.&getRecordingInfo
		this.performShorthandSequence=settop.&performShorthandCommandSequence
		
    }
    
    def createRemoteCommandBindings = {
        /**
         * Binding each RemoteCommand enum against its name,
         * so that it can call the RemoteProvider.pressKey method directly.
         */
        RemoteCommand.each { command ->
            setVariable(command.name(), command)
        }
    }

}
