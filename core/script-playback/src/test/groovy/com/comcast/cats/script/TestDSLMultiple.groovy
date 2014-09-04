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

import java.net.URL;

import com.comcast.cats.Settop
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.script.mock.DummyImageCompareProviderImpl
import com.comcast.cats.script.mock.DummySettop
import com.comcast.cats.script.mock.DummyVideoProviderImpl
import com.comcast.cats.script.playback.ScriptPlayer
import com.comcast.cats.script.playback.ScriptPlayerImpl
import com.comcast.cats.script.playback.exceptions.ScriptPlaybackException;

import org.junit.Test

class TestDSLMultiple {

    ImageCompareProvider imageProvider = new DummyImageCompareProviderImpl()
    VideoProvider videoProvider = new DummyVideoProviderImpl()
    Settop settop = new DummySettop(imageProvider, videoProvider)

    private ScriptPlayer getScriptPlayer() {
        ImageCompareProvider imageProvider = new DummyImageCompareProviderImpl()
        VideoProvider videoProvider = new DummyVideoProviderImpl()
        Settop settop = new DummySettop(imageProvider, videoProvider)
        ScriptPlayer player = new ScriptPlayerImpl(settop)
        return player
    }

    @Test
    public void executeScript() {
        URL scriptUrl = getClass().getResource( "/sample1.cats" )
        
        ScriptPlayer player1 = getScriptPlayer()
        ScriptPlayer player2 = getScriptPlayer()
        ScriptPlayer player3 = getScriptPlayer()

        Thread.start {
            player1.playBackScript( scriptUrl.openStream() )
        }
        
        Thread.start {
            player2.playBackScript( scriptUrl.openStream() )
        }
        
        Thread.start {
            sleep 1000
            player2.pause()
            sleep 5000
            player2.resume()            
        }
        
        Thread.start {
            player3.playBackScript( scriptUrl.openStream() )
        }
        
        sleep 10000
    }
}
