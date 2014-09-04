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

import com.comcast.cats.Settop
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.script.mock.DummyImageCompareProviderImpl
import com.comcast.cats.script.mock.DummySettop
import com.comcast.cats.script.mock.DummyVideoProviderImpl
import com.comcast.cats.script.playback.ScriptPlayer
import com.comcast.cats.script.playback.ScriptPlayerImpl
import org.junit.Test

class TestDSL {

	@Test
	public void executeScript() {
        ImageCompareProvider imageProvider = new DummyImageCompareProviderImpl()
        VideoProvider videoProvider = new DummyVideoProviderImpl()
		Settop settop = new DummySettop(imageProvider, videoProvider)        
        
		ScriptPlayer player = new ScriptPlayerImpl(settop)
		URL scriptUrl = getClass().getResource("/sample.cats");
		
		player.playBackScript(scriptUrl.openStream())
	}
}
